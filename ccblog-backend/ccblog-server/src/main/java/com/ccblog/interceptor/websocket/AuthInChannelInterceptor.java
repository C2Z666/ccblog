package com.ccblog.interceptor.websocket;

import com.ccblog.localCache.chat.TicketCache;
import com.ccblog.utils.mdc.MdcUtil;
import com.ccblog.utils.mdc.SelfTraceIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Map;

/**
 * 权限拦截器，消息出/入 channel 前/后进行拦截
 *
 * @author czc
 * @date 2025-10-24
 */
@Slf4j
public class AuthInChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private TicketCache ticketCache;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return null;

        log.info("收到消息:{}",message);

//        String ticket = accessor.getFirstNativeHeader("ticket");
//        String userId = ticketCache.get(ticket);
        if (!StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String ticket = accessor.getFirstNativeHeader("ticket");
            String userId = ticketCache.get(ticket);
            if (userId == null) {
                throw new MessagingException("invalid or expired ticket");
            }
        }

        // 加入MDC,只对“发送给 @MessageMapping 的请求”生效
        if (accessor.getMessageType() == SimpMessageType.MESSAGE) {
            Map<String, Object> sessAttrs = accessor.getSessionAttributes();
            if (sessAttrs != null) {
                String traceId = (String) sessAttrs.get(MdcUtil.TRACE_ID_KEY);
                if (traceId == null) {
                    traceId = SelfTraceIdGenerator.generate();
                }
                MDC.put(MdcUtil.TRACE_ID_KEY, traceId);
            }
        }
        return message;
    }


    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
            ;
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return ChannelInterceptor.super.postReceive(message, channel);
    }
}
