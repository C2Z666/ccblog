package com.ccblog.redis.article;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.dto.article.ArticleCountDTO;
import com.ccblog.entity.article.ArticleCount;
import com.ccblog.event.CountUpdateEvent;
import com.ccblog.event.OperationEvent;
import com.ccblog.mapper.article.ArticleCountMapper;
import com.ccblog.mapper.article.ArticleMapper;
import com.ccblog.template.HashRedisTemplate;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.JsonUtil;
import com.ccblog.utils.NumUtil;
import com.ccblog.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ccblog.constant.RedisPrefixConstant.ARTICLE_TOTAL_COUNT;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_COLLECT;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_COMMENT;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_LIKE;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_READ;

/**
 * 文章计数缓存模块
 * @author czc
 * @date 2025/10/8
 */
@Component
@Slf4j
public class ArticleCountRedis extends HashRedisTemplate<Long>{
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleCountMapper articleCountMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

//    private final String AGGREGATE_COUNT = "article:count:increase:aggregate_count";// 当前聚合池的数量
    private final String INTERACTION_INCREASE = "article:count:increase:%s:%s";// 增量事件清单,%s为shard:version,里面的值就是更新清单
    private final String INCREATE_VERSION = "article:count:increase:%s:version"; // 每个时间片增量事件的版本,自增,每日根据阈值检查
    private final String REDISSON_KEY = "redisson:article:count:%s:version"; // version的分布式锁,%s为shard
    private final long SHARD = 15; // 片大小
    private final boolean infoFlag;
    private final Integer aggregateMaxCount; // 聚合的最大数量(articleId数量)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public ArticleCountRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("article");
        infoFlag = cfg.isRedisLog();
        aggregateMaxCount = cfg.getMaxCount();
    }

    /**
     * 似乎不需要初始化吧,没有的时候默认0,实在不想要再删(比如影响到程序执行)
     */
//    /**
//     * 初始化计数器
//     */
    @PostConstruct
    public void init(){
        //        String aggregateCount = RedisUtil.get(AGGREGATE_COUNT);
        for(long shardId=0;shardId<SHARD;shardId++){
            String version = RedisUtil.get(getVersionKey(shardId));
            if(version==null){
                log.info("【redis:文章计数】未发现interaction version,初始化");
                RedisUtil.set(getVersionKey(shardId),"0"); // 如果不存在需要初始化
            }
        }
//        if(aggregateCount==null){
//            log.info("【redis:文章计数】未发现aggregate count,初始化");
//            RedisUtil.set(AGGREGATE_COUNT,"0"); // 如果不存在需要初始化
//        }
    }



    // ------------- 文章 收藏/点赞/阅读/评论 总数
    /**
     * 获取某篇文章当前某个filed计数（指定字段）
     * @param field     READ/LIKE/COLLECT/COMMENT
     * @return 当前数值；不存在返回 0
     */
    public Integer getTotalCount(Long articleId, String field) {
        String v = super.getContent(articleId,field);
        return v == null ? 0 : Integer.parseInt(v);
    }

    /**
     * 获得获取某篇文章多个所需计数（指定字段）
     * @param fields     READ/LIKE/COLLECT/COMMENT
     * @return 当前数值；不存在返回 0
     */
    public Map<String, Integer> getTotalCounts(Long articleId,List<String> fields) {
        Map<String, String> countMap = super.getContents(articleId, fields);
        if(countMap==null)return Map.of();
        return  countMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Integer.valueOf(e.getValue())));
    }

    /**
     * 批量获得多篇篇文章多个所需计数（指定字段）
     * @param articleIdSet   所需的id集合
     * @param fields         READ/LIKE/COLLECT/COMMENT
     * @return 当前数值；不存在返回 0
     */
    public Map<Long,Map<String, String>> getTotalCountsBatch(Set<Long> articleIdSet,List<String> fields) {
        return super.getContentBatch(articleIdSet,fields);
    }

    /**
     * 设置值
     */
    public void setCount(Long articleId, Map<String,String> map){
        super.setContent(articleId,map);
    }

    /**
     * 对指定文章、指定字段做加/减
     * @param articleId 文章ID
     * @param field     READ/LIKE/COLLECT/COMMENT
     * @param delta     +1 或 -1（或任意长整型）
     * @return 加完后的最新值
     */
    public long incrTotalCount(Long articleId, String field, long delta) {
        String key = String.format(ARTICLE_TOTAL_COUNT, articleId);
        return RedisUtil.hIncrBy(key, field, delta);
    }

    // ------------------ 异步落库相关
    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    @Async(value = "aggregateExecutor")
    public void aggregate(OperationEvent event){
        boolean publishFlag = false;
        String version=null;
        long shardId = getShardId(event.getTargetId());
        RLock lock = redissonClient.getLock(String.format(REDISSON_KEY,shardId));
        try {
            lock.lock(5, TimeUnit.SECONDS);
            version = RedisUtil.get(getVersionKey(shardId));
            String key = String.format(INTERACTION_INCREASE, shardId,version);
            // 增量池
            long cnt = RedisUtil.rPush(key, JsonUtil.toJsonString(event),
                            30*RedisUtil.TTL_MINUTE+ NumUtil.randomLong(120));
            // 达到长度就触发落库
            if (cnt == aggregateMaxCount) { // 刚好等于才触发
                publishFlag = true;
                RedisUtil.incrBy(getVersionKey(shardId),1); // 版本号 +1
            }
        } finally {
            lock.unlock();
        }
        if(publishFlag&&version!=null){
            if(infoFlag){
                log.info("【文章计数】到达阈值,触发更新");
            }
//            DebugUtil.textWriterAdd("D:\\java\\projects\\cc_blog\\doc\\压测结果\\importantlog.txt",
//                    String.format(String.format("数量达到,version=%s",version)));
            countUpdatePublish(shardId,version);
        }
    }

    /**
     * 批量事件聚合
     * 对于每一篇文章都使用批量插入
     * @param operationEvents 事件列表
     */
    @Async(value = "aggregateExecutor")
    public void aggregateBatch(List<OperationEvent> operationEvents) {
        if(operationEvents.isEmpty()) return;
        if(operationEvents.size()==1) {
            aggregate(operationEvents.get(0));
            return;
        }

        // 按照每个事件的articleId%SHARD(片)进行映射,然后每个组直接批量进redis
        Map<Long, List<OperationEvent>> shardMap =
                operationEvents.stream()
                        .collect(Collectors.groupingBy(e -> e.getTargetId() % SHARD));
        for(long shardId:shardMap.keySet()){
            boolean publishFlag=false;
            String version=null;
            RLock lock = redissonClient.getLock(String.format(REDISSON_KEY,shardId));
            try {
                lock.lock(5, TimeUnit.SECONDS);
                version = RedisUtil.get(getVersionKey(shardId));
                String key = String.format(INTERACTION_INCREASE, shardId,version);
                // 增量池
                List<String> eventJsons = new ArrayList<>();
                for(OperationEvent operationEvent:shardMap.get(shardId)){
                    eventJsons.add(JsonUtil.toJsonString(operationEvent));
                }
                long cnt = RedisUtil.rPushBatch(key, eventJsons,
                        30*RedisUtil.TTL_MINUTE+ NumUtil.randomLong(120));
                // 达到长度就触发落库
                if (cnt >= aggregateMaxCount) {
                    publishFlag = true;
                    RedisUtil.incrBy(getVersionKey(shardId),1); // 版本号 +1
                }
            } finally {
                lock.unlock();
            }
            if(publishFlag) {
                if (infoFlag) {
                    log.info("【文章计数】批量插入,到达阈值,触发更新");
                }
                countUpdatePublish(shardId, version);
            }
        }

    }


    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    @Async(value = "publishExecutor")
    public void countUpdatePublish(long shardId,String version) {
        String key = String.format(INTERACTION_INCREASE,shardId,version);
        // 读取全部的keys对应的redis数据,取出的每一条都是{articleId}:{operationCode}
        List<String> eventList = RedisUtil.listAll(key);
        if (eventList.isEmpty())
        {
            if(infoFlag&&shardId==SHARD/2) log.info("【Redis:文章计数】清单为空不更新,shard={},version={}",shardId,version);
            return; // 没有数据就可以返回了(待办清单空)
        }

        CountUpdateEvent updateEvent = CountUpdateEvent.builder()
                .version(version)
                .shardId(shardId)
                .evevnList(eventList).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.ARTICLE_EVENT_EX, RabbitMQConstant.ARTICLE_COUNT_KEY, updateEvent);
    }

    /**
     * 每天检查版本号,版本计数大于阈值清空版本号
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
     * 清除version对应清单
     */
    public void clearIncreaseList(long shardId, String version) {
        String removeKey = String.format(INTERACTION_INCREASE, shardId,version);
        RedisUtil.remove(removeKey);
    }



    // --------- 内部方法

    /**
     * 获取分片id
     * @param id
     * @return
     */
    private long getShardId(long id){
        return id%SHARD;
    }

    /**
     * 获取版本key
     * @param shardId
     * @return
     */
    private String getVersionKey(long shardId){
        return String.format(INCREATE_VERSION,shardId);
    }

    /**
     * 获取 redis key
     * @param
     * @return
     */
    protected String buildKey(Long articleId) {
        return String.format(ARTICLE_TOTAL_COUNT,articleId);
    }

    /**
     * 更新单个redis数据
     * @param articleId
     */
    protected void updateRedis(Long articleId) {
        ArticleCountDTO articleCountDTO = articleCountMapper.getCountByArticleId(articleId);
        Map<String,String> map;
        if(articleCountDTO!=null){
            map = Map.of(
                    COL_READ, String.valueOf(articleCountDTO.getReadCnt()),
                    COL_LIKE, String.valueOf(articleCountDTO.getLikeCnt()),
                    COL_COLLECT, String.valueOf(articleCountDTO.getCollectCnt()),
                    COL_COMMENT, String.valueOf(articleCountDTO.getCommentCnt())
            );
        }
        else{
            map = Map.of(
                    COL_READ, "0",
                    COL_LIKE, "0",
                    COL_COLLECT,"0",
                    COL_COMMENT, "0"
            );
        }
        setCount(articleId,map);
    }

    /**
     * 批量更新userIds对应的redis数据
     * @param articleIds 需要的文章id列表
     */
    protected void updateRedisBatch(List<Long> articleIds) {
        if(articleIds.isEmpty()) return;
        List<ArticleCount> counts = articleCountMapper.getArticleCountBatch(articleIds);
        // 组装为List<idx>和List<Map<filed,value>>准备批量更新
        articleIds = new ArrayList<>(); // 顺序不一定和原来一致,需要按照新的顺序逐个匹配组装
        List<Map<String,String>> idMaps = new ArrayList<>();
        for(ArticleCount count:counts){
            articleIds.add(count.getArticleId());
            idMaps.add(Map.of(
                    COL_READ, String.valueOf(count.getReadCnt()),
                    COL_LIKE, String.valueOf(count.getLikeCnt()),
                    COL_COLLECT, String.valueOf(count.getCollectCnt()),
                    COL_COMMENT, String.valueOf(count.getCommentCnt())
            ));
        }
        // 批量更新
        super.setContentBatch(articleIds,idMaps);
    }



//   ------------------- 定时任务

    @Component("ArticleCountVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 34 1 * * ? ";
        }
    }

    @Component("ArticleCountUpdate")
    public class publishEvent implements TimedTaskTemplate {
        private final RedissonClient redissonClient;
        private final long delta = 30; // 每 30 秒发一轮 有redis专门计数的在不需要非常高的实时性
        private final long intervalMs = 100; // 发送间隔(ms)
        private final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(2, r -> new Thread(r, "articleCount-scheduler"));

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
                countUpdatePublish(shardId, version);
            }
        }

        @Override
        public String getCron() {
            return String.format("0/%d * * * * ?", delta);
        }
    }

}