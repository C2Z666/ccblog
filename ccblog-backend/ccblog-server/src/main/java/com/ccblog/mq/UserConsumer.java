package com.ccblog.mq;

import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.entity.user.UserCount;
import com.ccblog.entity.user.UserRelation;
import com.ccblog.enumeration.user.UserOperateTypeEnum;
import com.ccblog.enumeration.user.UserOperateTypeEnum;
import com.ccblog.event.*;
import com.ccblog.mapper.user.UserCountMapper;
import com.ccblog.mapper.user.UserRelationMapper;
import com.ccblog.redis.user.UserCountRedis;
import com.ccblog.redis.user.UserFollowRedis;
import com.ccblog.utils.JsonUtil;
import com.ccblog.utils.NumUtil;
import com.ccblog.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.ccblog.constant.RedisPrefixConstant.*;

/**
 * 处理用户相关的mq请求
 * @author czc
 * @date 2025/10/16
 */
@Component
@Slf4j
public class UserConsumer {
    @Autowired
    private UserCountMapper userCountMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserCountRedis userCountRedis;

    @Autowired
    private UserFollowRedis userFollowRedis;

    @Autowired
    private UserRelationMapper userRelationMapper;

    @Value("${aggregate.user.mq-log:false}")   // 冒号后面是默认值
    private boolean infoFlag;

    /**
     * 用户计数更新
     * @param updateEvent 用户计数更新事件
     * @param channel 确认相关
     * @param tag 计数器
     */
    @RabbitListener(queues = RabbitMQConstant.USER_COUNT_Q,ackMode = "MANUAL")
    @Transactional
    public void updateUserCount(CountUpdateEvent updateEvent, Channel channel,
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:用户计数】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<String> evevnList = updateEvent.getEvevnList();

        // 事件回放得到更新增量(前面)
        // 注意这里计算出来存的都是增量!!!
        List<UserCount> userCountList = new ArrayList<>();
        Map<Long,Integer> indexMap = new HashMap<>();
        for(String eventJson:evevnList){
            OperationEvent event = JsonUtil.fromJson(eventJson,OperationEvent.class);
            Long userId = event.getTargetId();
            int type = event.getOperationCode();
            int index = indexMap.getOrDefault(userId,-1);
            if(index==-1){
                userCountList.add(UserCount.builder().userId(userId).build());
                index = userCountList.size()-1;
                indexMap.put(userId,index);
            }
            UserCount userCount = userCountList.get(index);
            // 这里可以定义为map:<类型:调用方法>简化这里的判断,不过性能提升不大,暂时不改(这种可读性还好一点)
            if(type== UserOperateTypeEnum.READ.getCode()){
                userCount.setReadCnt(NumUtil.null2Zero(userCount.getReadCnt())+1);
            }
            else if(type==UserOperateTypeEnum.LIKE.getCode()){
                userCount.setLikeCnt(NumUtil.null2Zero(userCount.getLikeCnt())+1);
            }
            else if(type==UserOperateTypeEnum.FOLLOW.getCode()){
                userCount.setFollowCnt(NumUtil.null2Zero(userCount.getFollowCnt())+1);
            }
            else if(type==UserOperateTypeEnum.FAN.getCode()){
                userCount.setFanCnt(NumUtil.null2Zero(userCount.getFanCnt())+1);
            }
            else if(type==UserOperateTypeEnum.CANCEL_FOLLOW.getCode()){
                userCount.setFollowCnt(NumUtil.null2Zero(userCount.getFollowCnt())-1);
            }
            else if(type==UserOperateTypeEnum.CANCEL_FAN.getCode()){
                userCount.setFanCnt(NumUtil.null2Zero(userCount.getFanCnt())-1);
            }
            else if(type==UserOperateTypeEnum.COMMENT.getCode()){
                userCount.setCommentCnt(NumUtil.null2Zero(userCount.getCommentCnt())+1);
            }
            else if(type==UserOperateTypeEnum.CANCEL_LIKE.getCode()){
                userCount.setLikeCnt(NumUtil.null2Zero(userCount.getLikeCnt())-1);
            }
            else if(type==UserOperateTypeEnum.COLLECT.getCode()){
                userCount.setCollectCnt(NumUtil.null2Zero(userCount.getCollectCnt())+1);
            }
            else if(type==UserOperateTypeEnum.CANCEL_COLLECT.getCode()){
                userCount.setCollectCnt(NumUtil.null2Zero(userCount.getCollectCnt())-1);
            }
            else if(type==UserOperateTypeEnum.NEW.getCode()){
                ; // 不管
            }
        }

        try {
            userCountMapper.upsertDeltaCountBatch(userCountList);
            if(infoFlag) log.info("【RabbitMQ:用户计数】成功更新{}条数据, tag={}",userCountList.size(), tag);
            channel.basicAck(tag, false); // 发送处理完消息给mq可以删掉了,false表示不批量确认
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            UserConsumerEvent event = new UserConsumerEvent(method,updateEvent.getVersion(),updateEvent.getShardId()); // this为当前类
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,不批量确认不重投
                log.warn("【RabbitMQ:用户计数】推送至死信队列,tag={}",tag);
            } catch (IOException ioEx) {
                log.error("NACK 失败", ioEx);
            }
        }
    }

    /**
     * 用户关注关系更新
     * @param userFollowUpdEvent 用户关注关系更新事件
     */
    @RabbitListener(queues = RabbitMQConstant.USER_FOLLOW_Q,ackMode = "MANUAL")
    @Transactional
    public void updateUserFollow(UserFollowUpdEvent userFollowUpdEvent, Channel channel,
                                 @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:用户粉丝】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<String> followItemList = userFollowUpdEvent.getFollowItemList();

        // 组合需要的数据集合  需要注意要对(id,id)去重,保留最后一组(也就是用户多次对一个用户的操作只保留最后一种操作)
        // 还有一点需要注意,如果一批数据只有取关,那么不更新数据库(数据库只记录第一次关注时间),如果有关注有取关,那么状态为取关,时间为关注
        // 先按顺序解析
        Map<String, UserRelation> latestMap = new LinkedHashMap<>(followItemList.size());
        // 记录每对ID第一次出现“关注”时的时间戳
        Map<String, Long> firstFollowTimeMap = new HashMap<>();
        for (String followItem : followItemList) {
            String[] parts = followItem.split(":");
            if (parts.length != 4) {
                log.warn("【redis:用户关注】非法格式 item: {}", followItem);
                continue;
            }
            Long selfId    = Long.valueOf(parts[0]);
            Long followId  = Long.valueOf(parts[1]);
            Integer state  = Integer.valueOf(parts[2]);
            Long timestamp = Long.valueOf(parts[3]);
            String key = selfId + ":" + followId;
            // 1. 如果是关注，且这对ID还没有“首次关注时间”，则记录下来
            if (state == 1 && !firstFollowTimeMap.containsKey(key)) {
                firstFollowTimeMap.put(key, timestamp);
            }
            UserRelation ur = new UserRelation();
            ur.setUserId(selfId);
            ur.setFollowUserId(followId);
            ur.setFollowState(state);
            // 2. 决定 lastFollowTime
            if (state == 1) {
                // 这批里有过关注，用“首次关注时间”
                ur.setLastFollowTime(LocalDateTime.ofEpochSecond(firstFollowTimeMap.get(key), 0, ZoneOffset.ofHours(8)));
            } else {
                // 纯取关，置 null，数据库不更新
                ur.setLastFollowTime(null);
            }
            // 3. 去重：靠后覆盖靠前
            latestMap.put(key, ur);
        }
        List<UserRelation> userRelationList = new ArrayList<>(latestMap.values());

        try {
            userRelationMapper.upsertRelationBatch(userRelationList);
            if(infoFlag) log.info("【RabbitMQ:用户关注】成功更新{}条数据, tag={}",userRelationList.size(), tag);
            channel.basicAck(tag, false); // 发送处理完消息给mq可以删掉了,false表示不批量确认
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            UserConsumerEvent event = new UserConsumerEvent(method); // this为当前类
            event.setVersion(userFollowUpdEvent.getVersion());
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,不批量确认不重投
                log.warn("【RabbitMQ:用户关注】推送至死信队列,tag={}",tag);
            } catch (IOException ioEx) {
                log.error("NACK 失败", ioEx);
            }
        }
    }



    /**
     * 监听清理缓存请求
     * @param event 事件参数
     */
    @EventListener
    protected void handleClearInteraction(UserConsumerEvent event){
        if(event.getSource().toString().equals("updateUserCount")){
            userCountRedis.clearIncreaseList(event.getShardId(),event.getVersion()); // 调用redis清理函数功能
        } else if (event.getSource().toString().equals("updateUserFollow")) {
            userFollowRedis.clearIncreaseList(event.getVersion());
        } else{
            ;
        }
    }
}