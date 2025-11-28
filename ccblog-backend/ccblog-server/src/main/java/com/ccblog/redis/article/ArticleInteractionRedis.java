package com.ccblog.redis.article;

//import cn.hutool.core.lang.Tuple;
import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.dto.article.ArticleStatus;
import com.ccblog.entity.article.ArticleInteraction;
import com.ccblog.event.ArticleInteractUpdEvent;
import com.ccblog.enumeration.YesOrNoEnum;
import com.ccblog.enumeration.article.ArticleCollectStatusEnum;
import com.ccblog.enumeration.article.ArticleCommentStatusEnum;
import com.ccblog.enumeration.article.ArticleLikeStatusEnum;
import com.ccblog.event.ReadEvent;
import com.ccblog.mapper.article.ArticleInteractionMapper;
import com.ccblog.template.HashRedisTemplate2;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.NumUtil;
import com.ccblog.utils.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ccblog.constant.RedisPrefixConstant.ARTICLE_INTERACTION;
import static com.ccblog.constant.RedisPrefixConstant.ARTICLE_LAST_READ_TIME;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.*;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_LIKE;


/**
 * 文章交互缓存模块
 * 将高速的last_read_time拆出来单独分析
 * @author czc
 * @date 2025-10-02
 */
@Component
@Slf4j
public class ArticleInteractionRedis extends HashRedisTemplate2<Long,Long> {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleInteractionMapper articleInteractionMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    @Resource(name = "newReadEventdSha")
    private String newReadEventdSha;

    private final String READ_EVENT_DONE = "article:interaction:read_done:%d:%d";   // 用于生成事件锁(短时间锁)
    private final String AGGREGATE_COUNT = "article:interaction:increase:aggregate_count:%s";// {shard},每个分片聚合池的数量
    private final String INTERACTION_INCREASE = "article:interaction:increase:%s:%s:%s";// {shard}:{version}:{userId}增量事件清单
    private final String INCREATE_VERSION = "article:interaction:increase:%s:version"; // 增量事件的版本,自增,每日根据阈值检查
    private final String PUBLISH_KEY = "article:interaction:increase:publish:%s:%s"; // {shard}:{version}每个分片版本的待发布key集合
    private final String REDISSON_KEY = "redisson:article:count:%s:version"; // {shard},每个shard的version的锁,按照用户id分片
    private final long SHARD = 10; // 片大小

    private boolean infoFlag;
    private Integer aggregateMaxCount; //  聚合的最大数量(<userId,articleId>组数量)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public ArticleInteractionRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("article");
        infoFlag = cfg.isRedisLog();
        aggregateMaxCount = cfg.getMaxCount();
    }

    /**
     * 初始化计数器
     * 可以改为程序执行立马发布事件,从而让version一开始就有值
     */
    @PostConstruct
    public void init(){
        for(long shardId=0;shardId<SHARD;shardId++){
            String version = RedisUtil.get(getVersionKey(shardId));
            if(version==null){
                log.info("【redis:文章交互】未发现interaction version,初始化");
                RedisUtil.set(getVersionKey(shardId),"0"); // 如果不存在需要初始化
            }
        }
    }

    // ------------- 关于文章用户增量阅读时间统计
    /**
     * 获得用户的最后阅读时间
     */
    public Long getLastReadTime(Long userId, Long articleId) {
        String key = String.format(ARTICLE_LAST_READ_TIME, userId, articleId);
        String v = RedisUtil.get(key);
        return v == null ? 0L : Long.parseLong(v);
    }

    /**
     * 批量设置最后阅读时间（毫秒级时间戳）
     * 只有时间比旧值新才写，避免滑动窗口内重复刷新(3秒内重复点击的不更新,以秒为单位)
     */
    public Set<ReadEvent> setLastReadTimeBatch(List<ReadEvent> events) {
        if (events.isEmpty()) return Collections.emptySet();

        Set<ReadEvent> newEventSet = new HashSet<>();   // 需要计数的 article

        int n = events.size();
        String[] keys = new String[n * 2];
        String[] args = new String[n * 3];

        for (int i = 0; i < n; i++) {
            ReadEvent e = events.get(i);
            keys[i * 2]     = RedisUtil.buildKey(String.format(READ_EVENT_DONE, e.getUserId(), e.getArticleId()));
            keys[i * 2 + 1] = RedisUtil.buildKey(String.format(ARTICLE_LAST_READ_TIME, e.getUserId(), e.getArticleId()));
            args[i * 3]     = String.valueOf(e.getReadTime());
            args[i * 3 + 1] = "3";
            args[i * 3 + 2] = String.valueOf(20 * 60 + NumUtil.randomLong(120));
        }

        // 一次 EVALSHA
        RBatch batch = redissonClient.createBatch();
        RScriptAsync script = batch.getScript();;   // 注意：是 batch.getScript()，不是 redissonClient.getScript()
        // 把命令注册到 batch
        script.evalShaAsync(
                RScript.Mode.READ_WRITE,
                newReadEventdSha,
                RScript.ReturnType.MULTI,
                Arrays.asList(keys),
                (Object[]) args
        );

        // 一次性 flush 并等待结果
        RFuture<BatchResult<?>> f = batch.executeAsync();
        List<Long> isNewList;
        try {
            BatchResult<?> r = f.toCompletableFuture()
                    .orTimeout(3, TimeUnit.SECONDS)
                    .join();
            isNewList = (List<Long>) r.getResponses().get(0);   // 拿到 0/1 数组
        } catch (CompletionException e) {
            throw new RuntimeException("Lua batch failed", e.getCause());
        }

        // 把 isNewList 用回到业务
        for (int i = 0; i < isNewList.size(); i++) {
            if (isNewList.get(i) == 1) newEventSet.add(events.get(i));
        }

        return newEventSet;   // 批量返回更新文章
    }



    // ------------- 关于用户阅读交互相关信息的统计 使用hash来存储,因为可能涉及到点赞状态等的操作
    /**
     * 获取某个用户的某个状态
     * @param field READ/LIKE/COLLECT/COMMENT
     * @return
     */
    public Integer getUserArticleStaus(Long userId, Long articleId, String field) {
        String v = super.getContent(Tuple.of(userId, articleId), field);
        return v == null ? 0 : Integer.parseInt(v);
    }

    /**
     * 获取某篇文章多个域状态（指定字段）
     * @param fields     READ/LIKE/COLLECT/COMMENT
     * @return 值映射
     */
    public Map<String, Integer> getTotalStatus(Long userId,Long articleId,List<String> fields) {
        Map<String, String> contents = super.getContents(Tuple.of(userId, articleId), fields);
        if(contents!=null){
            return contents.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> Integer.valueOf(e.getValue())   // 或 Integer.parseInt
                    ));
        }
        return Map.of();
    }

    /**
     * 批量获得某篇文章多个域的所需状态（指定字段）
     * @param fields     READ/LIKE/COLLECT/COMMENT
     * @return 值不再转换类型,接收端自己转
     */
    public Map<Tuple2<Long, Long>, Map<String, String>> getStatusBatch(
                                                Set<Tuple2<Long,Long>> idxSet ,List<String> fields) {
        return super.getContentBatch(idxSet, fields);
    }

    /**
     * 设置文章某个域的状态
     * @param field  READ/LIKE/COLLECT/COMMENT
     * @param status 新状态值
     */
    public void setStatusSingle(Long userId, Long articleId, String field, Integer status) {
        setContent(Tuple.of(userId,articleId),Map.of(field,status.toString()));
    }

    /**
     * 设置文章多个域的状态
     * @param map 新状态映射
     */
    public void setStatus(Long userId,Long articleId, Map<String,String> map) {
        setContent(Tuple.of(userId,articleId),map);
    }

    // ------------------ 异步落库相关
    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    @Async(value = "aggregateExecutor")
    public void aggregate(Long userId,Long articleId) {
        boolean publishFlag = false;
        String version=null;
        long shardId = getShardId(userId);
        RLock lock = redissonClient.getLock(String.format(REDISSON_KEY, shardId));
        try {
            lock.lock(5, TimeUnit.SECONDS);
            version = RedisUtil.get(getVersionKey(shardId));
            String key = String.format(INTERACTION_INCREASE, shardId,version,userId);
            // 增量池
            Boolean success = RedisUtil.add(key,articleId.toString(),20*RedisUtil.TTL_MINUTE);
            if(success){
                RedisUtil.add(getPublishKey(shardId,version),key,20*RedisUtil.TTL_MINUTE); // 注册key
                String aggregateCntKey = getAggregateCountKey(shardId);
                long cnt = RedisUtil.incrBy(aggregateCntKey,1); // 聚合数量 +1
                // 达到长度就触发落库
                if (cnt >= aggregateMaxCount) {
                    publishFlag=true;
                    RedisUtil.set(getAggregateCountKey(shardId),"0"); // 清空聚合计数信息
                    RedisUtil.incrBy(getVersionKey(shardId),1); // 版本号 +1
                }
            }
        } finally {
            lock.unlock();
        }
        if(publishFlag&&version!=null){
            if(infoFlag){
                log.info("【文章交互】到达阈值,触发更新");
            }
            interactionUpdatePublish(shardId,version);
        }
    }

    @Async(value = "aggregateExecutor")
    public void aggregateBatch(List<Tuple2<Long,Long>> userArticleIds) {
        if(userArticleIds.isEmpty()) return;
        if(userArticleIds.size()==1) {
            aggregate(userArticleIds.get(0)._1,userArticleIds.get(0)._2);
            return;
        }

        // 建立 userId->List<articleId>和shardId->userId映射
        Map<Long, List<Long>> userIdMap = new HashMap<>();
        Map<Long, Set<Long>> shardMap =new HashMap<>();
        for(Tuple2<Long,Long> userArticleId:userArticleIds){
            long userId = userArticleId._1;
            if(!userIdMap.containsKey(userId)){
                userIdMap.put(userId, new ArrayList<>());
            }
            userIdMap.get(userId).add(userArticleId._2);
            long shardId = getShardId(userId);
            if(!shardMap.containsKey(shardId)){
                shardMap.put(shardId, new HashSet<>());
            }
            shardMap.get(shardId).add(userId);
        }

        for(long shardId:shardMap.keySet()){
            boolean publishFlag = false;
            String version=null;
            RLock lock = redissonClient.getLock(String.format(REDISSON_KEY, shardId));
            try {
                lock.lock(5, TimeUnit.SECONDS);
                long addCnt = 0;
                for(Long userId:shardMap.get(shardId)){
                    version = RedisUtil.get(getVersionKey(shardId));
                    String key = String.format(INTERACTION_INCREASE, shardId,version,userId);
                    // 增量池
                    addCnt += RedisUtil.addBatch(key,
                            userIdMap.get(userId).stream().map(String::valueOf).collect(Collectors.toList()),
                            20*RedisUtil.TTL_MINUTE);
                    RedisUtil.add(getPublishKey(shardId,version),key,20*RedisUtil.TTL_MINUTE); // 注册
                }
                if(addCnt>0){
                    String aggregateCntKey = getAggregateCountKey(shardId);
                    long cnt = RedisUtil.incrBy(aggregateCntKey,addCnt); // 聚合数量 + cnt
                    // 达到长度就触发落库
                    if (cnt >= aggregateMaxCount) {
                        publishFlag=true;
                        RedisUtil.set(aggregateCntKey,"0"); // 清空聚合计数信息
                        RedisUtil.incrBy(getVersionKey(shardId),1); // 版本号 +1
                    }
                }
            } finally {
                lock.unlock();
            }
            if(publishFlag&&version!=null){
                if(infoFlag){
                    log.info("【文章交互】批量到达阈值,触发更新");
                }
                interactionUpdatePublish(shardId,version);
            }
        }

    }

    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    @Async(value = "publishExecutor")
    public void interactionUpdatePublish(long shardId,String version) {
        Set<String> keys = RedisUtil.members(getPublishKey(shardId,version)); // 获取待发布的key
        // 读取全部的keys对应的redis数据,取出来的map的键就是需要的userId,值是该用户的更新文章集合
        Map<String, Set<String>> mp = RedisUtil.multiGet(keys.stream().toList());
        if (mp.isEmpty())
        {
            if(infoFlag&&shardId==SHARD) log.info("【Redis:文章交互】清单为空不更新,shard={},version={}",shardId,version);
            return; // 没有数据就可以返回了(没有待办清单)
        }
        ArticleInteractUpdEvent articleInteractUpdEvent = ArticleInteractUpdEvent.builder()
                            .idMap(mp)
                            .shardId(shardId)
                            .version(version).build();
        // 发布更新事件
        rabbitTemplate.convertAndSend(RabbitMQConstant.ARTICLE_EVENT_EX, RabbitMQConstant.ARTICLE_INTERACTION_KEY, articleInteractUpdEvent);
    }

    /**
     * 监听清除特定版本的redis交互增量数据
     * @param version
     */
    public void clearIncreaseList(long shardId,String version) {
        String publishKey = getPublishKey(shardId,version);
        Set<String> keys = RedisUtil.members(publishKey);
        RedisUtil.removeBatch(keys.stream().toList());
        RedisUtil.remove(publishKey);
    }

    /**
     * 检查版本号,版本计数大于阈值清空版本号
     */
    public void checkVersion() {
        for(long shardId=0;shardId<SHARD;shardId++){
            String version = RedisUtil.get(getVersionKey(shardId));
            if(Long.parseLong(version)>maxVersion){
                RedisUtil.set(getVersionKey(shardId),"0"); // 清空
            }
        }
    }

    /**
     * key构造规则
     * @param idxPair  <userId,articleId>
     * @return
     */
    protected String buildKey(Tuple2<Long, Long> idxPair) {
        return String.format(ARTICLE_INTERACTION, idxPair._1,idxPair._2);
    }

    /**
     * 单个更新redis数据
     * @param idxPair <userId,articleId>
     */
    protected void updateRedis(Tuple2<Long, Long> idxPair) {
        ArticleStatus articleStatus = articleInteractionMapper.getUserStatusByArticleId(idxPair._1, idxPair._2);
        Map<String, String> map;
        if(articleStatus ==null){
            map = Map.of( // 没查到说明数据库没有,那就是什么都没
                    COL_READ,    String.valueOf(YesOrNoEnum.NO.getCode()),
                    COL_LIKE,    String.valueOf(ArticleLikeStatusEnum.NOT_LIKE.getCode()),
                    COL_COLLECT, String.valueOf(ArticleCollectStatusEnum.NOT_COLLECT.getCode()),
                    COL_COMMENT, String.valueOf(ArticleCommentStatusEnum.NOT_COMMENT.getCode()));
        }
        else{
            map = Map.of(
                    COL_READ,    String.valueOf(NumUtil.null2Zero(articleStatus.getReadStatus())),
                    COL_LIKE,    String.valueOf(NumUtil.null2Zero(articleStatus.getLikeStatus())),
                    COL_COLLECT, String.valueOf(NumUtil.null2Zero(articleStatus.getCollectStatus())),
                    COL_COMMENT, String.valueOf(NumUtil.null2Zero(articleStatus.getCommentStatus())));
        }
        super.setContent(idxPair,map);
    }

    /**
     * 批量更新redis数据
     * @param idxPairList <userId,articleId>
     */
    protected void updateRedisBatch(List<Tuple2<Long, Long>> idxPairList) {
        if(idxPairList.isEmpty()) return;
        List<ArticleInteraction> interactions = articleInteractionMapper.getUserStatusBatch(idxPairList);
        // 组装为List<idx>和List<Map<filed,value>>准备批量更新
        idxPairList = new ArrayList<>(); // 顺序不一定和原来一致,需要按照新的顺序逐个匹配组装
        List<Map<String,String>> idMaps = new ArrayList<>();
        for(ArticleInteraction interaction:interactions){
            idxPairList.add(Tuple.of(interaction.getUserId(),interaction.getArticleId()));
            idMaps.add(Map.of(
                    COL_READ, String.valueOf(interaction.getReadStat()),
                    COL_LIKE, String.valueOf(interaction.getLikeStat()),
                    COL_COLLECT, String.valueOf(interaction.getStarStat()),
                    COL_COMMENT, String.valueOf(interaction.getCommentStat())
            ));
        }
        // 批量更新
        super.setContentBatch(idxPairList,idMaps);
    }

    /**
     * 获取分片id
     * @param userId
     * @return
     */
    private long getShardId(long userId){
        return userId%SHARD;
    }

    /**
     * 获取版本key
     * @param shardId
     * @return
     */
    private String getVersionKey(long shardId){
        return String.format(INCREATE_VERSION,shardId);
    }

    private String getPublishKey(long shardId,String version){
        return String.format(PUBLISH_KEY,shardId,version);
    }

    private String getAggregateCountKey(long shardId){
        return String.format(AGGREGATE_COUNT,shardId);
    }



    //    ------------------ 定时任务
    @Component("ArticleInteractVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 30 1 * * ? ";
        }
    }

    @Component("ArticleInteractUpdate")
    public class publishEvent implements TimedTaskTemplate {
        private final RedissonClient redissonClient;
        private final long delta = 30; // 每 30 秒发一轮 有redis专门计数的在不需要非常高的实时性
        private final long intervalMs = 100; // 发送间隔(ms)
        private final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(2, r -> new Thread(r, "articleInteract-scheduler"));

        public publishEvent(@Lazy RedissonClient redissonClient) {
            this.redissonClient = redissonClient;
        }

        /**
         * cron 线程只负责“发信号”，不阻塞排班
         */
        @Override
        public void execute() {
            for (long shardId = 0; shardId < SHARD; shardId++) {
                long delayMs = shardId * intervalMs;
                long finalShardId = shardId;
                scheduler.schedule(() -> doOneShard(finalShardId), delayMs, TimeUnit.MILLISECONDS);
            }
        }

        /**
         * 工作线程
         */
        private void doOneShard(long shardId) {
            RLock lock = redissonClient.getLock(String.format(REDISSON_KEY, shardId));
            String version = null;
            lock.lock(5, TimeUnit.SECONDS);
            try {
                version = RedisUtil.get(getVersionKey(shardId));
                RedisUtil.incrBy(getVersionKey(shardId), 1);
            } finally {
                lock.unlock();
            }
            if (version != null) {
                interactionUpdatePublish(shardId, version);
            }
        }

        @Override
        public String getCron() {
            return String.format("0/%d * * * * ?", delta);
        }
    }
}