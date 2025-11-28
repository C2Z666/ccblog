package com.ccblog.mq;

import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.dto.chat.ai.ChatAiAggDTO;
import com.ccblog.dto.chat.user.ChatUserItemDTO;
import com.ccblog.entity.chat.ChatAiSession;
import com.ccblog.entity.chat.ChatUserSession;
import com.ccblog.event.*;
import com.ccblog.mapper.chat.ChatAiMapper;
import com.ccblog.mapper.chat.ChatAiSessionMapper;
import com.ccblog.mapper.chat.ChatUserMapper;
import com.ccblog.mapper.chat.ChatUserSessionMapper;
import com.ccblog.redis.chat.ChatAiRedis;
import com.ccblog.redis.chat.ChatAiSessionRedis;
import com.ccblog.redis.chat.ChatUserRedis;
import com.ccblog.redis.chat.ChatUserSessionRedis;
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
import java.util.*;

import static com.ccblog.constant.RedisPrefixConstant.*;
import static com.ccblog.enumeration.chat.UserSessionFieldEnum.*;

/**
 * 处理聊天相关的mq请求
 * @author czc
 * @date 2025/10/24
 */
@Component
@Slf4j
public class ChatConsumer {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ChatUserMapper chatUserMapper;

    @Autowired
    private ChatUserRedis chatUserRedis;

    @Autowired
    private ChatUserSessionMapper chatUserSessionMapper;

    @Autowired
    private ChatUserSessionRedis chatUserSessionRedis;

    @Autowired
    private ChatAiSessionMapper chatAiSessionMapper;

    @Autowired
    private ChatAiRedis chatAiRedis;

    @Autowired
    private ChatAiMapper chatAiMapper;

    @Value("${aggregate.chat.mq-log:false}")   // 冒号后面是默认值
    private boolean infoFlag;

    /**
     * 聊天内容更新
     * @param chatUserUpdEvent 用户聊天更新事件
     * @param channel 确认相关
     * @param tag 计数器
     */
    @RabbitListener(queues = RabbitMQConstant.CHAT_USER_Q,ackMode = "MANUAL")
    @Transactional
    public void updateChatUser(ChatUserContentUpdEvent chatUserUpdEvent, Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:用户聊天】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<ChatUserItemDTO> chatUserItemDTOList = chatUserUpdEvent.getChatUserItemDTOList();

        try {
            chatUserMapper.upsertContentBatch(chatUserItemDTOList);
            if(infoFlag) log.info("【RabbitMQ:用户聊天】成功更新{}条数据, tag={}",chatUserItemDTOList.size(), tag);
            channel.basicAck(tag, false); // 发送处理完消息给mq可以删掉了,false表示不批量确认
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            UserConsumerEvent event = new UserConsumerEvent(method); // this为当前类
            event.setVersion(chatUserUpdEvent.getVersion());
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,不批量确认不重投
                log.warn("【RabbitMQ:用户聊天】推送至死信队列,tag={}",tag);
            } catch (IOException ioEx) {
                log.error("NACK 失败", ioEx);
            }
        }
    }

    /**
     * 用户会话信息更新
     * @param chatUserSessionUpdEvent 用户会话更新事件
     * @param channel 确认相关
     * @param tag 计数器
     */
    @RabbitListener(queues = RabbitMQConstant.CHAT_USER_SESSION_Q,ackMode = "MANUAL")
    @Transactional
    public void updateChatUserSession(ChatUserSessionUpdEvent chatUserSessionUpdEvent, Channel channel,
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:用户会话】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<String> idPairList = chatUserSessionUpdEvent.getIdPairList();

        // 组合需要的key并且直接把id放进对应的实体类
        List<ChatUserSession> chatUserSessions = new ArrayList<>(idPairList.size()); // 实体类列表
        List<String> countKeys = new ArrayList<>();
        for(String idPair:idPairList){
            String[] parts = idPair.split(":");
            chatUserSessions.add(ChatUserSession.builder()
                    .userId(Long.parseLong(parts[0]))
                    .peerId(Long.parseLong(parts[1])).build());
            countKeys.add(String.format(CHAT_USER_SESSION,parts[0],parts[1]));
        }

        // 查到所有需要的数据
        List<Map<Object, Object>> sessionMapList = RedisUtil.mHGetAll(countKeys);
        for(int i=0;i<sessionMapList.size();i++) {
            Map<Object, Object> userSession = sessionMapList.get(i);
            ChatUserSession chatUserSession = chatUserSessions.get(i);
            for(Object field:userSession.keySet()){
                String value = userSession.get(field).toString();
                switch (field.toString()){
                    case COL_SNAPSHOT:
                        chatUserSession.setSnapshot(value);
                        break;
                    case COL_LAST_TIME:
                        chatUserSession.setLastMsgTime(LocalDateTime.parse(value));
                        break;
                    case COL_UNREAD_CNT:
                        chatUserSession.setUnreadCount(Integer.parseInt(value));
                        break;
                    case COL_DISPLAY_SEQ:
                        chatUserSession.setDisplaySeq(Long.parseLong(value));
                        break;
                    case COL_SEQ:
                        chatUserSession.setSeq(Long.parseLong(value));
                        break;
                    default:
                        ;
                }
            }
        }

        try {
            chatUserSessionMapper.upsertSessionBatch(chatUserSessions);
            if(infoFlag) log.info("【RabbitMQ:用户会话】成功更新{}条数据, tag={}",chatUserSessions.size(), tag);
            channel.basicAck(tag, false); // 发送处理完消息给mq可以删掉了,false表示不批量确认
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            ChatConsumerEvent event = new ChatConsumerEvent(method); // this为当前类
            event.setVersion(chatUserSessionUpdEvent.getVersion());
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,不批量确认不重投
                log.warn("【RabbitMQ:用户会话】推送至死信队列,tag={}",tag);
            } catch (IOException ioEx) {
                log.error("NACK 失败", ioEx);
            }
        }
    }

    /**
     * AI聊天内容更新
     * @param chatAiContentUpdEvent 用户AI聊天更新事件
     * @param channel 确认相关
     * @param tag 计数器
     */
    @RabbitListener(queues = RabbitMQConstant.CHAT_AI_Q,ackMode = "MANUAL")
    @Transactional
    public void updateChatAi(ChatAiContentUpdEvent chatAiContentUpdEvent, Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:用户AI聊天】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<ChatAiAggDTO> chatAiAggDTOS = chatAiContentUpdEvent.getChatAiAggDTOS();
        // 提取更新会话需要的数据
        Set<Long> set = new HashSet<>();
        List<ChatAiSession> chatAiSessions = new ArrayList<>();
        for(int i=chatAiAggDTOS.size()-1;i>=0;i--){ // 后往前
            Long sessionId = chatAiAggDTOS.get(i).getSessionId();
            if(set.contains(sessionId)) continue;
            set.add(sessionId);
            chatAiSessions.add(ChatAiSession.builder()
                    .id(sessionId)
                    .lastMsgTime(chatAiAggDTOS.get(i).getCreateTime())
                    .seq(chatAiAggDTOS.get(i).getSeq()).build());
        }

        try {
            chatAiMapper.upsertContentBatch(chatAiAggDTOS);
            chatAiSessionMapper.updateChatSessionBatch(chatAiSessions);
            if(infoFlag) log.info("【RabbitMQ:用户AI聊天】成功更新, tag={}", tag);
            channel.basicAck(tag, false); // 发送处理完消息给mq可以删掉了,false表示不批量确认
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            ChatConsumerEvent event = new ChatConsumerEvent(method); // this为当前类
            event.setVersion(chatAiContentUpdEvent.getVersion());
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列,不批量确认不重投
                log.warn("【RabbitMQ:用户AI聊天】推送至死信队列,tag={}",tag);
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
    protected void handleClearInteraction(ChatConsumerEvent event){
        if(event.getSource().toString().equals("updateChatUser")){
            chatUserRedis.clearIncreaseList(event.getVersion()); // 调用redis清理函数功能
        } else if (event.getSource().toString().equals("updateChatUserSession")) {
            chatUserSessionRedis.clearIncreaseList(event.getVersion());
        }else if (event.getSource().toString().equals("updateChatAi")) {
            chatAiRedis.clearIncreaseList(event.getVersion());
        }else{
            ;
        }
    }


}