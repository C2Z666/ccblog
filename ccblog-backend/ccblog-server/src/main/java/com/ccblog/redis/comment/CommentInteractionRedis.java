package com.ccblog.redis.comment;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.event.CommentInteractUpdEvent;
import com.ccblog.entity.comment.CommentInteraction;
import com.ccblog.enumeration.comment.CommentDislikeStatusEnum;
import com.ccblog.enumeration.comment.CommentLikeStatusEnum;
import com.ccblog.mapper.comment.CommentInteractionMapper;
import com.ccblog.template.HashRedisTemplate2;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.NumUtil;
import com.ccblog.utils.RedisUtil;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.ccblog.constant.RedisPrefixConstant.*;
import static com.ccblog.enumeration.comment.CommentOperateFieldEnum.COL_LIKE;
import static com.ccblog.enumeration.comment.CommentOperateFieldEnum.COL_DISLIKE;

/**
 * 评论交互缓存模块
 * 将高速的last_read_time拆出来单独分析
 * @author czc
 * @date 2025-10-02
 */
@Component
@Slf4j
public class CommentInteractionRedis extends HashRedisTemplate2<Long,Long> {
    @Autowired
    private CommentInteractionMapper commentInteractionMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    private final String AGGREGATE_COUNT = "comment:interaction:increase:aggregate_count";// 当前聚合池的数量
    private final String INTERACTION_INCREASE = "comment:interaction:increase:%s:%s";// 增量事件清单,%s:%s为version:userId
    private final String INCREATE_VERSION = "comment:interaction:increase:version"; // 增量事件的版本,自增,每日根据阈值检查

    private boolean infoFlag;
    private Integer aggregateMaxCount; // 聚合的最大数量(<userId,commentId>组数量)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public CommentInteractionRedis(AggregateProps props){
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
            log.info("【Redis:评论交互模块】未发现interaction version,初始化");
            RedisUtil.set(INCREATE_VERSION,"0"); // 如果不存在需要初始化
        }
        if(aggregateCount==null){
            log.info("【Redis:评论交互模块】未发现aggregate count,初始化");
            RedisUtil.set(AGGREGATE_COUNT,"0"); // 如果不存在需要初始化
        }
    }

    // ------------- 关于用户评论交互相关信息的统计 使用hash来存储
    /**
     * 获取某个用户的某个状态
     * @param field LIKE/DISLIKE
     * @return
     */
    public Integer getUserCommentStaus(Long userId, Long commentId, String field) {
        String v = super.getContent(Tuple.of(userId,commentId),field);
        return v==null?0:Integer.parseInt(v);
    }

    /**
     * 获取评论多个域状态（指定字段）
     * @param fields     READ/LIKE/COLLECT/COMMENT
     * @return 值映射
     */
    public Map<String, Integer> getTotalStatus(Long userId,Long commentId,List<String> fields) {
        Map<String, String> contents = super.getContents(Tuple.of(userId, commentId), fields);
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
     * 批量获取某篇文章多个域的所需状态（指定字段）
     * @param fields     READ/LIKE/COLLECT/COMMENT
     * @return 值不再转换类型,接收端自己转
     */
    public Map<Tuple2<Long, Long>, Map<String, String>> getStatusBatch(
            Set<Tuple2<Long,Long>> idxSet ,List<String> fields) {
        return super.getContentBatch(idxSet, fields);
    }

    /**
     * 设置评论某个域的状态
     * @param field  LIKE/DISLIKE
     * @param status 新状态值
     */
    public void setSingleStatus(Long userId, Long commentId, String field, Integer status) {
        super.setContent(Tuple.of(userId,commentId),Map.of(field,status.toString()));
    }

    /**
     * 设置评论多个域的状态
     * @param map 新状态映射
     */
    public void setStatus(Long userId,Long commentId, Map<String,String> map) {
        setContent(Tuple.of(userId,commentId),map);
    }

    // ------------------ 异步落库相关
    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    public void aggregate(Long userId,Long commentId) {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE, version,userId);
        // 增量池
        Boolean success = RedisUtil.add(key,commentId.toString(),RedisUtil.TTL_DAY);
        if(success){
            long cnt = RedisUtil.incrBy(AGGREGATE_COUNT,1); // 聚合数量 +1
            // 达到长度就触发落库
            if (cnt >= aggregateMaxCount) {
                if(infoFlag) log.info("【Redis:评论交互】到达阈值,触发更新");
                interactionUpdatePublish();
            }
        }
    }

    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    public void interactionUpdatePublish() {
        String version = RedisUtil.get(INCREATE_VERSION);
        String pattern = String.format(INTERACTION_INCREASE,version,"*");
        Set<String> keys = RedisUtil.getKeys(pattern); // 获取所有匹配的key
        // 读取全部的keys对应的redis数据,取出来的map的键就是需要的userId,值是该用户的更新评论集合
        Map<String, Set<String>> mp = RedisUtil.multiGet(keys.stream().toList());
        if (mp.isEmpty())
        {
            if(infoFlag) log.info("【Redis:更新评论交互】清单为空不更新,version={}",version);
            return; // 没有数据就可以返回了(没有待办清单)
        }
        RedisUtil.set(AGGREGATE_COUNT,"0"); // 清空聚合计数信息
        CommentInteractUpdEvent commentInteractUpdEvent = CommentInteractUpdEvent.builder()
                            .idxMp(mp)
                            .version(version).build();
        // 发布更新事件
        rabbitTemplate.convertAndSend(RabbitMQConstant.COMMENT_EVENT_EX, RabbitMQConstant.COMMENT_INTERACTION_KEY, commentInteractUpdEvent);
        // 版本自增
        RedisUtil.incrBy(INCREATE_VERSION,1); // 版本号 +1
    }

    /**
     * 监听清除特定版本的redis交互增量数据
     * @param version
     */
    public void clearIncreaseList(String version) { // 默认监听和参数匹配的
        String pattern = String.format(INTERACTION_INCREASE,version,"*"); // 清除某个版本的所有key
        long cnt = RedisUtil.deleteByPattern(pattern); // 删除缓存
    }

    /**
     * 每天检查版本号,版本计数大于阈值清空版本号
     */
    @Scheduled(cron = "* 30 1 * * ? ")
    public void checkVersion() {
        if(infoFlag) log.info("【Redis:评论交互模块】清空版本号");
        String version = RedisUtil.get(INCREATE_VERSION);
        if(Long.parseLong(version)>maxVersion){
            RedisUtil.set(INCREATE_VERSION,"0"); // 清空
        }
    }

    /**
     * 获得key的规则
     * @param idxPair
     * @return
     */
    protected String buildKey(Tuple2<Long, Long> idxPair) {
        return String.format(COMMENT_INTERACTION, idxPair._1,idxPair._2);
    }

    /**
     * 更新用户对评论状态的redis
     * @param idxPair
     */
    protected void updateRedis(Tuple2<Long, Long> idxPair) {
        CommentInteraction commentStatus = commentInteractionMapper.getUserStatusByCommentId(idxPair._1, idxPair._2);
        Map<String, String> map;
        if(commentStatus==null){
            map = Map.of( // 没查到说明数据库没有,那就是什么都没
                    COL_LIKE,    String.valueOf(CommentLikeStatusEnum.NOT_LIKE.getCode()),
                    COL_DISLIKE, String.valueOf(CommentDislikeStatusEnum.NOT_DISLIKE.getCode()));
        }
        else{
            map = Map.of(
                    COL_LIKE,    String.valueOf(NumUtil.null2Zero(commentStatus.getLikeStat())),
                    COL_DISLIKE, String.valueOf(NumUtil.null2Zero(commentStatus.getDislikeStat())));
        }
        super.setContent(idxPair,map);
    }

    /**
     * 批量更新redis
     * @param idxPairs
     */
    protected void updateRedisBatch(List<Tuple2<Long, Long>> idxPairs) {
        if(idxPairs.isEmpty()) return;
        List<CommentInteraction> interactions = commentInteractionMapper.getUserStatusBatch(idxPairs);
        // 组装为List<idx>和List<Map<filed,value>>准备批量更新
        idxPairs = new ArrayList<>(); // 顺序不一定和原来一致,需要按照新的顺序逐个匹配组装
        List<Map<String,String>> idMaps = new ArrayList<>();
        for(CommentInteraction interaction:interactions){
            idxPairs.add(Tuple.of(interaction.getUserId(),interaction.getCommentId()));
            idMaps.add(Map.of(
                    COL_LIKE, String.valueOf(interaction.getLikeStat()),
                    COL_DISLIKE, String.valueOf(interaction.getDislikeStat())
            ));
        }
        // 批量更新
        super.setContentBatch(idxPairs,idMaps);
    }

    //    ------------------ 定时任务
    @Component("CommentInteractVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 40 1 * * ? ";
        }
    }

    @Component("CommentInteractUpdate")
    public class publishEvent implements TimedTaskTemplate {
        @Override
        public void execute() {
            interactionUpdatePublish();
        }

        @Override
        public String getCron() {
            return "1/10 * * * * ? ";
        }
    }
}