package com.ccblog.event;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 聊天MQ消费者,供@EventListener监听
 * @author czc
 * @date 2025/10/24
 */
@Data
@Builder
public class ChatConsumerEvent extends ApplicationEvent {
    private String version;

    public ChatConsumerEvent(Object source) {
        super(source);
    }

    public ChatConsumerEvent(Object source, Clock clock) {
        super(source, clock);
    }
}