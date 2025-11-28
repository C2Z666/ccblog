package com.ccblog.mq;

import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.dto.notice.NoticeContentAggDTO;
import com.ccblog.entity.notice.NoticeCount;
import com.ccblog.enumeration.notice.NoticeTypeEnum;
import com.ccblog.event.NoticeConsumerEvent;
import com.ccblog.event.NoticeContentUpdEvent;
import com.ccblog.event.NoticeCountUpdEvent;
import com.ccblog.event.UserConsumerEvent;
import com.ccblog.mapper.notice.NoticeContentMapper;
import com.ccblog.mapper.notice.NoticeCountMapper;
import com.ccblog.redis.notice.NoticeContentRedis;
import com.ccblog.redis.notice.NoticeCountRedis;
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
import java.util.*;

import static com.ccblog.constant.RedisPrefixConstant.*;

/**
 * 处理用户相关的mq请求
 * @author czc
 * @date 2025/10/16
 */
@Component
@Slf4j
public class NoticeConsumer {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private NoticeContentMapper noticeContentMapper;

    @Autowired
    private NoticeContentRedis noticeContentRedis;

    @Autowired
    private NoticeCountMapper noticeCountMapper;

    @Autowired
    private NoticeCountRedis noticeCountRedis;

    @Value("${aggregate.notice.mq-log:false}")   // 冒号后面是默认值
    private boolean infoFlag;

    /**
     * 通知内容更新
     * @param noticeContentUpdEvent 通知内容更新事件
     * @param channel 确认相关
     * @param tag 计数器
     */
    @RabbitListener(queues = RabbitMQConstant.NOTICE_CONTENT_Q,ackMode = "MANUAL")
    @Transactional
    public void updateNoticeContent(NoticeContentUpdEvent noticeContentUpdEvent, Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:通知内容】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<NoticeContentAggDTO> noticeContentUpdEventList = noticeContentUpdEvent.getNoticeContentUpdEventList();

        try {
            noticeContentMapper.upsertContentBatch(noticeContentUpdEventList);
            // 目前的方案是连续追加
            if(infoFlag) log.info("【RabbitMQ:通知内容】成功更新{}条数据, tag={}",noticeContentUpdEventList.size(), tag);
            channel.basicAck(tag, false); // 发送处理完消息给mq可以删掉了,false表示不批量确认
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            UserConsumerEvent event = new UserConsumerEvent(method); // this为当前类
            event.setVersion(noticeContentUpdEvent.getVersion());
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,不批量确认不重投
                log.warn("【RabbitMQ:通知内容】推送至死信队列,tag={}",tag);
            } catch (IOException ioEx) {
                log.error("NACK 失败", ioEx);
            }
        }
    }

    /**
     * 通知计数更新
     * @param noticeCountUpdEvent 用户未读计数更新事件
     * @param channel 确认相关
     * @param tag 计数器
     */
    @RabbitListener(queues = RabbitMQConstant.NOTICE_COUNT_Q,ackMode = "MANUAL")
    @Transactional
    public void updateUserCount(NoticeCountUpdEvent noticeCountUpdEvent, Channel channel,
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:通知计数】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<String> userIdList = noticeCountUpdEvent.getUserIdList();

        // 组合需要的key
        List<String> countKeys = new ArrayList<>();
        for(String userId:userIdList){
            countKeys.add(String.format(NOTICE_TOTAL_COUNT,userId));
        }
        // 查到所有需要的数据
        List<Map<Object, Object>> countMapList = RedisUtil.mHGetAll(countKeys);
        List<NoticeCount> noticeCountList = new ArrayList<>(countMapList.size());
        for(int i=0;i<countMapList.size();i++) {
            Map<Object, Object> count = countMapList.get(i);
            Long userId = Long.parseLong(userIdList.get(i));
            NoticeCount noticeCount = NoticeCount.builder()
                    .userId(userId).build();
            for(Object field:count.keySet()){
                String value = count.get(field).toString();
                switch (field.toString()){
                    case NoticeTypeEnum.COL_COMMENT:
                        noticeCount.setUnreadComment(Integer.parseInt(value));
                        break;
                    case NoticeTypeEnum.COL_LIKE:
                        noticeCount.setUnreadLike(Integer.parseInt(value));
                        break;
                    case NoticeTypeEnum.COL_COLLECT:
                        noticeCount.setUnreadCollect(Integer.parseInt(value));
                        break;
                    case NoticeTypeEnum.COL_REPLY:
                        noticeCount.setUnreadReply(Integer.parseInt(value));
                        break;
                    case NoticeTypeEnum.COL_FOLLOW:
                        noticeCount.setUnreadFollow(Integer.parseInt(value));
                        break;
                    case NoticeTypeEnum.COL_SYSTEM:
                        noticeCount.setUnreadSystem(Integer.parseInt(value));
                        break;
                    default:
                        ;
                }
            }
            noticeCountList.add(noticeCount);
        }

        try {
            noticeCountMapper.upsertNoticeCountBatch(noticeCountList);
            if(infoFlag) log.info("【RabbitMQ:通知计数】成功更新{}条数据, tag={}",noticeCountList.size(), tag);
            channel.basicAck(tag, false); // 发送处理完消息给mq可以删掉了,false表示不批量确认
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            NoticeConsumerEvent event = new NoticeConsumerEvent(method); // this为当前类
            event.setVersion(noticeCountUpdEvent.getVersion());
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,不批量确认不重投
                log.warn("【RabbitMQ:通知计数】推送至死信队列,tag={}",tag);
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
        if(event.getSource().toString().equals("updateNoticeContent")){
            noticeContentRedis.clearIncreaseList(event.getVersion()); // 调用redis清理函数功能
        } else if (event.getSource().toString().equals("updateUserCount")) {
            noticeCountRedis.clearIncreaseList(event.getVersion());
        } else{
            ;
        }
    }
}