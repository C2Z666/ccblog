package com.ccblog.redis.notice;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.entity.notice.NoticeCount;
import com.ccblog.event.NoticeCountUpdEvent;
import com.ccblog.mapper.notice.NoticeCountMapper;
import com.ccblog.template.HashRedisTemplate;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ccblog.constant.RedisPrefixConstant.NOTICE_TOTAL_COUNT;
import static com.ccblog.enumeration.notice.NoticeTypeEnum.*;

/**
 * 通知计数缓存模块
 * @author czc
 * @date 2025/10/15
 */
@Component
@Slf4j
public class NoticeCountRedis extends HashRedisTemplate<Long> {
    @Autowired
    private NoticeCountMapper noticeCountMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    private final String AGGREGATE_COUNT = "notice:count:increase:aggregate_count";// 当前聚合池的数量
    private final String INTERACTION_INCREASE = "notice:count:increase:%s";// 增量事件清单,%s为version,里面的值就是更新清单
   private final String INCREATE_VERSION = "notice:count:increase:version"; // 增量事件的版本,自增,每日根据阈值检查

    private final boolean infoFlag;
    private final Integer aggregateMaxCount; // 聚合的最大数量(noticeId数量，同一id多个操作算一个)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public NoticeCountRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("notice");
        infoFlag = cfg.isRedisLog();
        aggregateMaxCount = cfg.getMaxCount();
    }

    /**
     * 初始化计数器
     */
//    @PostConstruct
//    public void init(){
//        String version = RedisUtil.get(INCREATE_VERSION);
//        String aggregateCount = RedisUtil.get(AGGREGATE_COUNT);
//        if(version==null){
//            log.info("【redis:通知计数】未发现interaction version,初始化");
//            RedisUtil.set(INCREATE_VERSION,"0"); // 如果不存在需要初始化
//        }
//        if(aggregateCount==null){
//            log.info("【redis:通知计数】未发现aggregate count,初始化");
//            RedisUtil.set(AGGREGATE_COUNT,"0"); // 如果不存在需要初始化
//        }
//    }

    /**
     * 设置值
     */
    public void setCount(Long userId, Map<String,String> map){
        String key = String.format(NOTICE_TOTAL_COUNT,userId);
        RedisUtil.hMSet(key, map, RedisUtil.TTL_DAY);
    }

    /**
     * 获取某个用户当前未读通知总计数（指定字段）
     * @param field     COL_SYSTEM/COL_COLLECT/COL_LIKE/COL_COMMENT/COL_REPLY/COL_REPLY
     * @return 当前数值；不存在返回 0
     */
    public Integer getTotalCount(Long userId, String field) {
        String v = super.getContent(userId,field);
        return v == null ? 0 : Integer.parseInt(v);
    }

    /**
     * 获得获取某个用户多个类型所需计数（指定字段）
     * @param fields COL_SYSTEM/COL_COLLECT/COL_LIKE/COL_COMMENT/COL_REPLY/COL_REPLY
     * @return Map<field, count>
     */
    public Map<String, Integer> getTotalCounts(Long userId, List<String> fields) {
        Map<String, String> countMap = super.getContents(userId, fields);
        if(countMap==null) return Map.of();
        return countMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Integer.valueOf(e.getValue())));
    }

    /**
     * 获得获取某个用户全部未读计数
     **/
    public int getTotolUnread(Long userId) {
        Map<String,Integer> unreadMap = getTotalCounts(userId, Arrays.stream(COLS).toList());
        int res = 0;
        for(String field:COLS){
            res+=unreadMap.getOrDefault(field,0);
        }
        return res;
    }

    /**
     * 对指定用户、指定字段做加/减
     * @param userId 用户ID
     * @param field     COL_SYSTEM/COL_COLLECT/COL_LIKE/COL_COMMENT/COL_REPLY/COL_REPLY
     * @param delta     +1 或 -1（或任意长整型）
     * @return 加完后的最新值
     */
    public long incrTotalCount(Long userId, String field, long delta) {
        String key = String.format(NOTICE_TOTAL_COUNT, userId);
        return RedisUtil.hIncrBy(key, field, delta);
    }

    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    public void aggregate(Long userId) {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE, version);
        // 增量池
        Boolean success = RedisUtil.add(key,userId.toString(),RedisUtil.TTL_DAY); // 是否需要主动失效?
        if(success){
            long cnt = RedisUtil.incrBy(AGGREGATE_COUNT,1); // 聚合数量 +1
            // 达到长度就触发落库
            if (cnt >= aggregateMaxCount) {
                if(infoFlag) log.info("【redis:通知计数】到达阈值,触发更新");
                countUpdatePublish();
            }
        }
    }

    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    public void countUpdatePublish() {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE,version);
        // 需要更新的用户集合
        List<String> userIdList = RedisUtil.members(key).stream().toList();
        if (userIdList.isEmpty())
        {
            if(infoFlag) log.info("【Redis:通知计数】清单为空不更新,version={}",version);
            return; // 没有数据就可以返回了(待办清单空)
        }
        RedisUtil.set(AGGREGATE_COUNT,"0"); // 清空聚合计数信息
        NoticeCountUpdEvent noticeCountUpdEvent = NoticeCountUpdEvent.builder().
                version(version)
                .userIdList(userIdList).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.NOTICE_EVENT_EX, RabbitMQConstant.NOTICE_COUNT_KEY, noticeCountUpdEvent);
        // 版本自增
        RedisUtil.incrBy(INCREATE_VERSION,1); // 版本号 +1
    }


    /**
     * 清除version对应清单
     * @param version
     */
    public void clearIncreaseList(String version) {
        String pattern = String.format(INTERACTION_INCREASE,version); // 清除某个版本的所有key
        long cnt = RedisUtil.deleteByPattern(pattern); // 删除缓存
    }

    /**
     * 检查版本号,版本计数大于阈值清空版本号
     */
    public void checkVersion() {
        if(infoFlag) log.info("清空版本号");
        String version = RedisUtil.get(INCREATE_VERSION);
        if(Long.parseLong(version)>maxVersion){
            RedisUtil.set(INCREATE_VERSION,"0"); // 清空
        }
    }

    /**
     * 更新所有关于用户的未读通知计数信息:COL_SYSTEM/COL_COLLECT/COL_LIKE/COL_COMMENT/COL_REPLY/COL_REPLY
     */
    private void updateNoticeCount(Long userId) {

    }

    @Override
    protected String buildKey(Long userId) {
        return String.format(NOTICE_TOTAL_COUNT, userId);
    }

    /**
     * 更新单个
     * @param userId
     */
    protected void updateRedis(Long userId) {
        NoticeCount noticeCount = noticeCountMapper.getCountByUserId(userId);
        Map<String, String> map;
        if(noticeCount!=null){
            map = Map.of(
                    COL_SYSTEM, String.valueOf(noticeCount.getUnreadSystem()),
                    COL_COLLECT, String.valueOf(noticeCount.getUnreadCollect()),
                    COL_LIKE, String.valueOf(noticeCount.getUnreadLike()),
                    COL_COMMENT, String.valueOf(noticeCount.getUnreadComment()),
                    COL_REPLY, String.valueOf(noticeCount.getUnreadReply()),
                    COL_FOLLOW, String.valueOf(noticeCount.getUnreadFollow())
            );
        }
        else{ // 稳健性考虑
            map = Map.of(
                    COL_SYSTEM, "0",
                    COL_COLLECT, "0",
                    COL_LIKE, "0",
                    COL_COMMENT, "0",
                    COL_REPLY, "0",
                    COL_FOLLOW,"0"
            );
        }
        setCount(userId,map);
    }

    /**
     * 没有批量拿的需求先不管这个
     * @param idxs
     */
    protected void updateRedisBatch(List<Long> idxs) {
        ;
    }

//    -------------- 定时任务
    @Component("NoticeCountVersion")
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

    @Component("NoticeCountUpdate")
    public class publishEvent implements TimedTaskTemplate {
        @Override
        public void execute() {
            countUpdatePublish();
        }

        @Override
        public String getCron() {
            return "0/3 * * * * ? ";
        }
    }
}