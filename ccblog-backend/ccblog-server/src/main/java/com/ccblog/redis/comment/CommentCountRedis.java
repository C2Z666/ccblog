package com.ccblog.redis.comment;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.entity.comment.CommentCount;
import com.ccblog.event.CountUpdateEvent;
import com.ccblog.event.OperationEvent;
import com.ccblog.mapper.comment.CommentCountMapper;
import com.ccblog.template.HashRedisTemplate;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.JsonUtil;
import com.ccblog.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.ccblog.constant.RedisPrefixConstant.*;
import static com.ccblog.enumeration.comment.CommentOperateFieldEnum.*;

/**
 * 评论计数缓存模块
 * @author czc
 * @date 2025/10/11
 */
@Component
@Slf4j
public class CommentCountRedis extends HashRedisTemplate<Long> {
    @Autowired
    private CommentCountMapper commentCountMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    private final String AGGREGATE_COUNT = "comment:count:increase:aggregate_count";// 当前聚合池的数量
    private final String INTERACTION_INCREASE = "comment:count:increase:%s";// 增量事件清单,%s为version,里面的值就是更新清单
    private final String INCREATE_VERSION = "comment:count:increase:version"; // 增量事件的版本,自增,每日根据阈值检查

    private boolean infoFlag;
    private Integer aggregateMaxCount; // 聚合的最大数量(commentId数量，同一id多个操作算一个)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public CommentCountRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("comment");
        infoFlag = cfg.isRedisLog();
        aggregateMaxCount = cfg.getMaxCount();
    }

    /**
     * 初始化计数器
     */
    @PostConstruct
    public void init(){
        String version = RedisUtil.get(INCREATE_VERSION);
        String aggregateCount = RedisUtil.get(AGGREGATE_COUNT);
        if(version==null){
            log.info("【redis:评论计数】未发现interaction version,初始化");
            RedisUtil.set(INCREATE_VERSION,"0"); // 如果不存在需要初始化
        }
        if(aggregateCount==null){
            log.info("【redis:评论计数】未发现aggregate count,初始化");
            RedisUtil.set(AGGREGATE_COUNT,"0"); // 如果不存在需要初始化
        }
    }

    /**
     * 设置值
     */
    public void setCount(Long commentId, Map<String,String> map){
        super.setContent(commentId,map);
    }

    /**
     * 获取某条评论某个属性当前总计数（指定字段）
     * @param field     LIKE/COL_DISLIKE
     * @return 当前数值；不存在COL_返回 0
     */
    public Integer getTotalCount(Long commentId, String field) {
        String v = super.getContent(commentId,field);
        return v == null ? 0 : Integer.parseInt(v);
    }

    /**
     * 获得获取某篇文章多个所需计数（指定字段）
     * @param fields     READ/LIKE/COLLECT/COMMENT
     * @return 当前数值；不存在返回 0
     */
    public Map<String, Integer> getTotalCounts(Long commentId,List<String> fields) {
        Map<String, String> countMap = super.getContents(commentId, fields);
        if(countMap==null) return Map.of();
        return countMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Integer.valueOf(e.getValue())));
    }

    /**
     * 批量获得多篇篇文章多个所需计数（指定字段）
     * @param commentIdSet   所需的id集合
     * @param fields         READ/LIKE/COLLECT/COMMENT
     * @return 当前数值；不存在返回 null
     */
    public Map<Long,Map<String, String>> getTotalCountsBatch(Set<Long> commentIdSet,List<String> fields) {
        return super.getContentBatch(commentIdSet,fields);
    }


    /**
     * 对指定评论、指定字段做加/减
     * @param commentId 评论ID
     * @param field     LIKE/COL_DISLIKE
     * @param delta     +1 或COL_ -1（或任意长整型）
     * @return 加完后的最新值
     */
    public long incrTotalCount(Long commentId, String field, long delta) {
        String key = String.format(COMMENT_TOTAL_COUNT, commentId);
        return RedisUtil.hIncrBy(key, field, delta);
    }

    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    public void aggregate(OperationEvent event) {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE, version);
        // 增量池
        long cnt = RedisUtil.rPush(key, JsonUtil.toJsonString(event),10*RedisUtil.TTL_MINUTE);
        // 达到长度就触发落库
        if (cnt >= aggregateMaxCount) {
            if(infoFlag) log.info("【redis:评论计数】到达阈值,触发更新");
            countUpdatePublish();
        }
    }

    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    public void countUpdatePublish() {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE,version);
        // 读取全部的keys对应的redis数据,取出来的map的键就是需要的userId,值是该用户的更新文章集合
        List<String> eventList = RedisUtil.listAll(key);
        if (eventList.isEmpty())
        {
            if(infoFlag) log.info("【Redis:更新评论数量】清单为空不更新,version={}",version);
            return; // 没有数据就可以返回了(待办清单空)
        }
        RedisUtil.set(AGGREGATE_COUNT,"0"); // 清空聚合计数信息
        CountUpdateEvent updateEvent = CountUpdateEvent.builder().
                version(version)
                .evevnList(eventList).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.COMMENT_EVENT_EX, RabbitMQConstant.COMMENT_COUNT_KEY, updateEvent);
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
     * 查询key构造
     * @param commentId
     * @return
     */
    protected String buildKey(Long commentId) {
        return String.format(COMMENT_TOTAL_COUNT, commentId);
    }

    /**
     * 单独更新
     * @param commentId
     */
    protected void updateRedis(Long commentId) {
        CommentCount commentCountDTO = commentCountMapper.getCountByCommentId(commentId);
        Map<String, String> map;
        if(commentCountDTO!=null){
            map = Map.of(
                    COL_DISLIKE, String.valueOf(commentCountDTO.getDislikeCnt()),
                    COL_LIKE, String.valueOf(commentCountDTO.getLikeCnt()),
                    COL_REPLY, String.valueOf(commentCountDTO.getReplyCnt())
            );
        }
        else{ // 稳健性考虑
            map = Map.of(
                    COL_DISLIKE, "0",
                    COL_LIKE, "0",
                    COL_REPLY, "0"
            );
        }
        setCount(commentId,map);
    }

    /**
     * 批量查询更新
     * @param commentIds
     */
    protected void updateRedisBatch(List<Long> commentIds) {
        if(commentIds.isEmpty()) return;
        List<CommentCount> counts = commentCountMapper.getCommentCountBatch(commentIds);
        // 组装为List<idx>和List<Map<filed,value>>准备批量更新
        commentIds = new ArrayList<>(); // 顺序不一定和原来一致,需要按照新的顺序逐个匹配组装
        List<Map<String,String>> idMaps = new ArrayList<>();
        for(CommentCount count:counts){
            commentIds.add(count.getCommentId());
            idMaps.add(Map.of(
                    COL_REPLY, String.valueOf(count.getReplyCnt()),
                    COL_LIKE, String.valueOf(count.getLikeCnt()),
                    COL_DISLIKE, String.valueOf(count.getDislikeCnt())
            ));
        }
        // 批量更新
        super.setContentBatch(commentIds,idMaps);
    }

    //   ------------------- 定时任务
    @Component("CommentCountVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 37 1 * * ? ";
        }
    }

    @Component("CommentCountUpdate")
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