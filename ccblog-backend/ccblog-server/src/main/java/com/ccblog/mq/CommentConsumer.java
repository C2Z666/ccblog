package com.ccblog.mq;

import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.entity.comment.CommentCount;
import com.ccblog.entity.comment.CommentInteraction;
import com.ccblog.enumeration.comment.CommentOperateFieldEnum;
import com.ccblog.enumeration.comment.CommentOperateTypeEnum;
import com.ccblog.event.*;
import com.ccblog.mapper.comment.CommentCountMapper;
import com.ccblog.mapper.comment.CommentInteractionMapper;
import com.ccblog.redis.comment.CommentCountRedis;
import com.ccblog.redis.comment.CommentInteractionRedis;
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
import org.springframework.data.util.Pair;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

import static com.ccblog.constant.RedisPrefixConstant.*;

/**
 * 处理评论相关的mq请求
 * @author czc
 * @date 2025/10/8
 */
@Component
@Slf4j
public class CommentConsumer {
    @Autowired
    private CommentCountMapper commentCountMapper;

    @Autowired
    private CommentInteractionMapper commentInteractionMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CommentCountRedis commentCountRedis;

    @Autowired
    private CommentInteractionRedis commentInteractionRedis;

    @Value("${aggregate.comment.mq-log:false}")   // 冒号后面是默认值
    private boolean infoFlag;

    /**
     * 评论数量更新
     * @param updateEvent 评论数量更新事件
     * @param channel 确认相关
     * @param tag 计数器
     */
    @RabbitListener(queues = RabbitMQConstant.COMMENT_COUNT_Q,ackMode = "MANUAL")
    @Transactional
    public void updateCommentCount(CountUpdateEvent updateEvent, Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:评论计数】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<String> evevnList = updateEvent.getEvevnList();

        // 事件回放得到更新增量(前面)
        // 注意这里计算出来存的都是增量!!!
        List<CommentCount> commentCountList = new ArrayList<>();
        Map<Long,Integer> indexMap = new HashMap<>();
        for(String eventJson:evevnList){
            OperationEvent event = JsonUtil.fromJson(eventJson,OperationEvent.class);
            Long commentId = event.getTargetId();
            int type = event.getOperationCode();
            int index = indexMap.getOrDefault(commentId,-1);
            if(index==-1){
                commentCountList.add(CommentCount.builder().commentId(commentId).build());
                index = commentCountList.size()-1;
                indexMap.put(commentId,index);
            }
            CommentCount commentCount = commentCountList.get(index);
            // 这里可以定义为map:<类型:调用方法>简化这里的判断,不过性能提升不大,暂时不改(这种可读性还好一点)
            if(type== CommentOperateTypeEnum.LIKE.getCode()){
                commentCount.setLikeCnt(NumUtil.null2Zero(commentCount.getLikeCnt())+1);
            }
            else if(type==CommentOperateTypeEnum.NEW.getCode()){
                ; // 不管
            }
            else if(type==CommentOperateTypeEnum.REPLY.getCode()){
                commentCount.setReplyCnt(NumUtil.null2Zero(commentCount.getReplyCnt())+1);
            }
            else if(type==CommentOperateTypeEnum.CANCEL_LIKE.getCode()){
                commentCount.setLikeCnt(NumUtil.null2Zero(commentCount.getLikeCnt())-1);
            }
            else if(type==CommentOperateTypeEnum.DISLIKE.getCode()){
                commentCount.setDislikeCnt(NumUtil.null2Zero(commentCount.getDislikeCnt())+1);
            }
            else if(type==CommentOperateTypeEnum.CANCEL_DISLIKE.getCode()){
                commentCount.setDislikeCnt(NumUtil.null2Zero(commentCount.getDislikeCnt())-1);
            }
        }

        commentCountMapper.upsertDeltaCountBatch(commentCountList);
        try {
            if(infoFlag) log.info("【RabbitMQ:评论计数】成功更新{}条数据, tag={}",commentCountList.size(), tag);
            channel.basicAck(tag, false); // 发送处理完消息给mq可以删掉了,false表示不批量确认
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            CommentConsumerEvent event = new CommentConsumerEvent(method); // this为当前类
            event.setVersion(updateEvent.getVersion());
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,不批量确认不重投
                log.warn("【RabbitMQ:评论计数】推送至死信队列,tag={}",tag);
            } catch (IOException ioEx) {
                log.error("NACK 失败", ioEx);
            }
        }
    }

    /**
     * 评论交互更新
     * @param commentInteractUpdEvent 评论交互更新事件
     */
    @RabbitListener(queues = RabbitMQConstant.COMMENT_INTERACTION_Q,ackMode = "MANUAL")
    @Transactional
    public void updateCommentInteraction(CommentInteractUpdEvent commentInteractUpdEvent, Channel channel,
                                         @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:评论交互】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        Map<String, Set<String>> mp = commentInteractUpdEvent.getIdxMp();

        // 拿到数据
        List<String> interactionKeys = new ArrayList<>(); // 查interaction需要的key集合
        List<Pair<String,String>> indexList = new ArrayList<>(); // 预分配位置,方便后续组装
        for(String k: mp.keySet()){
            String userId = k.substring(k.lastIndexOf(':') + 1);
            // 处理用户需要的信息
            for(Object t:mp.get(k)){ // 这里的key实际对应就是articleId
                String commentId = t.toString();
                interactionKeys.add(String.format(COMMENT_INTERACTION,userId,commentId));
                indexList.add(Pair.of(userId,commentId));
            }
        }
        // 获得所有相应数据
        List<Map<Object, Object>> interactionMap = RedisUtil.mHGetAll(interactionKeys);
        // 组装为需要的数据
        List<CommentInteraction> commentInteractionList = new ArrayList<>();
        for(int i=0;i<indexList.size();i++){
            // 创建基本的更新单元
            Long userId = Long.parseLong(indexList.get(i).getFirst());
            Long commentId = Long.parseLong(indexList.get(i).getSecond());
            CommentInteraction commentInteraction = CommentInteraction.builder()
                    .userId(userId)
                    .commentId(commentId).build();
            // 加入点赞状态等信息
            Map<Object, Object> status = interactionMap.get(i);
            if(status!=null){
                for(Object filed:status.keySet()){
                    String value = status.get(filed).toString();
                    switch (filed.toString()){
                        case CommentOperateFieldEnum.COL_LIKE:
                            commentInteraction.setLikeStat(Integer.parseInt(value)); // 取出索引位置的对象并赋值
                            break;
                        case CommentOperateFieldEnum.COL_DISLIKE:
                            commentInteraction.setDislikeStat(Integer.parseInt(value));
                            break;
                        default:
                            // 后续可能扩展其他(比如打分、举报等,一般低频的直接写数据库可能更好)
                            ;
                    }
                }
            }
            commentInteractionList.add(commentInteraction);
        }

        try {
            commentInteractionMapper.upsertInteractionBatch(commentInteractionList);
            if(infoFlag) log.info("【RabbitMQ:评论交互】成功更新{}条数据, tag={}",commentInteractionList.size(), tag);
            channel.basicAck(tag, false);
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            ArticleConsumerEvent event = new ArticleConsumerEvent(method); // this为当前类
            event.setVersion(commentInteractUpdEvent.getVersion());
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,第一个表示不批量确认,第二个表示是否进行重投
                log.warn("【RabbitMQ:评论交互】推送至死信队列,tag={}",tag);
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
    protected void handleClearInteraction(CommentConsumerEvent event){
        if(event.getSource().toString().equals("updateCommentCount")){
            commentCountRedis.clearIncreaseList(event.getVersion()); // 调用redis清理函数功能
        } else if (event.getSource().toString().equals("updateCommentInteraction")) {
            commentInteractionRedis.clearIncreaseList(event.getVersion());
        }
    }
}