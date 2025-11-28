package com.ccblog.event;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 评论MQ消费者,供@EventListener监听
 * @author czc
 * @date 2025/10/10
 */
@Data
@Builder
public class CommentConsumerEvent extends ApplicationEvent {
    private String version;

    public CommentConsumerEvent(Object source) {
        super(source);
    }

    public CommentConsumerEvent(Object source, Clock clock) {
        super(source, clock);
    }
}