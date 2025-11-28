package com.ccblog.redis.user;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.entity.user.UserCount;
import com.ccblog.event.CountUpdateEvent;
import com.ccblog.event.OperationEvent;
import com.ccblog.mapper.user.UserCountMapper;
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

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ccblog.constant.RedisPrefixConstant.USER_TOTAL_COUNT;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_COLLECT;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_COMMENT;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_LIKE;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_READ;
import static com.ccblog.enumeration.user.UserOperateFieldEnum.COL_FAN;
import static com.ccblog.enumeration.user.UserOperateFieldEnum.COL_FOLLOW;

/**
 * 用户计数缓存模块
 * @author czc
 * @date 2025/10/15
 */
@Component
@Slf4j
public class UserCountRedis extends HashRedisTemplate<Long> {
    @Autowired
    private UserCountMapper userCountMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    @Autowired
    private RedissonClient redissonClient;

    private final String AGGREGATE_COUNT = "user:count:increase:aggregate_count";// 当前聚合池的数量
    private final String INTERACTION_INCREASE = "user:count:increase:%s:%s";// 增量事件清单,%s为version,里面的值就是更新清单
    private final String INCREATE_VERSION = "user:count:increase:%s:version"; // 增量事件的版本,自增,每日根据阈值检查
    private final String REDISSON_KEY = "redisson:user:count:%s:version"; // version的分布式锁
    private final long SHARD = 10; // 片大小

    private final boolean infoFlag;
    private final Integer aggregateMaxCount; // 聚合的最大数量(userId数量，同一id多个操作算一个)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public UserCountRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("user");
        infoFlag = cfg.isRedisLog();
        aggregateMaxCount = cfg.getMaxCount();
    }

    /**
     * 初始化计数器
     */
    @PostConstruct
    public void init(){
        for(long shardId=0;shardId<SHARD;shardId++){
            String version = RedisUtil.get(getVersionKey(shardId));
            if(version==null){
                log.info("【redis:用户计数】未发现interaction version,初始化");
                RedisUtil.set(getVersionKey(shardId),"0"); // 如果不存在需要初始化
            }
        }
    }

    /**
     * 设置值
     */
    public void setCount(Long userId, Map<String,String> map){
        String key = String.format(USER_TOTAL_COUNT,userId);
        RedisUtil.hMSet(key, map, RedisUtil.TTL_WEEK);
    }

    /**
     * 获取某个用户当前总计数（指定字段）
     * @param field     LIKE/DISLIKE
     * @return 当前数值；不存在返回 0
     */
    public Integer getTotalCount(Long userId, String field) {
        String v=  super.getContent(userId,field);
        return v == null ? 0 : Integer.parseInt(v);
    }

    /**
     * 获取多个filed值
     * @param fields COL_LIKE, COL_COLLECT,COL_COMMENT,COL_READ,COL_FAN,COL_FOLLOW
     * @return Map<field, count>
     */
    public Map<String, Integer> getTotalCounts(Long userId,List<String> fields) {
        Map<String, String> countMap = super.getContents(userId, fields);
        if(countMap==null)return Map.of();
        return  countMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Integer.valueOf(e.getValue())));
    }

    /**
     * 批量获得多个用户多个所需计数（指定字段）
     * @param userIdSet      所需的userId集合
     * @param fields         READ/LIKE/COLLECT/COMMENT
     * @return 当前数值；不存在返回 0
     */
    public Map<Long,Map<String, String>> getTotalCountsBatch(Set<Long> userIdSet,List<String> fields) {
        return super.getContentBatch(userIdSet,fields);
    }

    /**
     * 对指定用户、指定字段做加/减
     * @param userId 用户ID
     * @param field     LIKE/DISLIKE
     * @param delta     +1 或 -1（或任意长整型）
     * @return 加完后的最新值
     */
    public long incrTotalCount(Long userId, String field, long delta) {
        String key = String.format(USER_TOTAL_COUNT, userId);
        return RedisUtil.hIncrBy(key, field, delta);
    }

    // 下面两个聚合器,包括发布事件,计数类别应该都是一样的(包括评论计数),要是能多继承再继承一个计数模板会方便很多

    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    @Async(value = "aggregateExecutor")
    public void aggregate(OperationEvent event) {
        boolean publishFlag = false;
        String version=null;
        long shardId = getShardId(event.getTargetId());
        RLock lock =  redissonClient.getLock(String.format(REDISSON_KEY,shardId));
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
                log.info("【用户计数】到达阈值,触发更新");
            }
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

        // 按照每个事件的userId%SHARD(片)进行映射,然后每个组直接批量进redis
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
                    log.info("【用户计数】批量插入,到达阈值,触发更新");
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
        // 获取事件
        List<String> eventList = RedisUtil.listAll(key);
        if (eventList.isEmpty())
        {
            if(infoFlag&&shardId==SHARD/2) log.info("【Redis:用户计数】清单为空不更新,shard={},version={}",shardId,version);
            return; // 没有数据就可以返回了(待办清单空)
        }
        CountUpdateEvent updateEvent = CountUpdateEvent.builder()
                .version(version)
                .shardId(shardId)
                .evevnList(eventList).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.USER_EVENT_EX, RabbitMQConstant.USER_COUNT_KEY, updateEvent);
    }

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
     * 清除version对应清单
     * @param version
     */
    public void clearIncreaseList(long shardId, String version) {
        String removeKey = String.format(INTERACTION_INCREASE, shardId,version);
        RedisUtil.remove(removeKey);
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
     * 构造key
     * @param userId
     * @return
     */
    protected String buildKey(Long userId) {
        return String.format(USER_TOTAL_COUNT, userId);
    }

    /**
     * 更新单个用户的缓存
     * @param userId
     */
    protected void updateRedis(Long userId) {
        UserCount userCount = userCountMapper.getCountByUserId(userId);
        Map<String, String> map;
        if(userCount !=null){
            map = Map.of(
                    COL_FAN, String.valueOf(userCount.getFanCnt()),
                    COL_FOLLOW, String.valueOf(userCount.getFollowCnt()),
                    COL_READ, String.valueOf(userCount.getReadCnt()),
                    COL_COMMENT, String.valueOf(userCount.getCommentCnt()),
                    COL_COLLECT, String.valueOf(userCount.getCollectCnt()),
                    COL_LIKE, String.valueOf(userCount.getLikeCnt())
            );
        }
        else{ // 稳健性考虑
            map = Map.of(
                    COL_FAN, "0",
                    COL_FOLLOW, "0",
                    COL_READ, "0",
                    COL_COMMENT,"0",
                    COL_COLLECT, "0",
                    COL_LIKE, "0"
            );
        }
        super.setContent(userId,map);
    }

    /**
     * 批量更新用户缓存
     * @param userIds
     */
    protected void updateRedisBatch(List<Long> userIds) {
        if(userIds.isEmpty()) return;
        List<UserCount> counts = userCountMapper.getArticleCountBatch(userIds);
        // 组装为List<idx>和List<Map<filed,value>>准备批量更新
        userIds = new ArrayList<>(); // 顺序不一定和原来一致,需要按照新的顺序逐个匹配组装
        List<Map<String,String>> idMaps = new ArrayList<>();
        for(UserCount count:counts){
            userIds.add(count.getUserId());
            idMaps.add(Map.of(
                    COL_FAN, String.valueOf(count.getFanCnt()),
                    COL_FOLLOW, String.valueOf(count.getFollowCnt()),
                    COL_READ, String.valueOf(count.getReadCnt()),
                    COL_LIKE, String.valueOf(count.getLikeCnt()),
                    COL_COLLECT, String.valueOf(count.getCollectCnt()),
                    COL_COMMENT, String.valueOf(count.getCommentCnt())
            ));
        }
        // 批量更新
        super.setContentBatch(userIds,idMaps);
    }

    //   ------------------- 定时任务
    @Component("UserCountVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 33 1 * * ? ";
        }
    }

    @Component("UserCountUpdate")
    public class publishEvent implements TimedTaskTemplate {
        private final RedissonClient redissonClient;
        private final long delta = 30; // 每 30 秒发一轮 有redis专门计数的在不需要非常高的实时性
        private final long intervalMs = 100; // 发送间隔(ms)
        private final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(2, r -> new Thread(r, "userCount-scheduler"));

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