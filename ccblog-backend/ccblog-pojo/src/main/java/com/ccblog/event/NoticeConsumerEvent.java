package com.ccblog.event;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 通知MQ消费者更新完成事件,供@EventListener监听
 * @author czc
 * @date 2025/10/18
 */
@Data
@Builder
public class NoticeConsumerEvent extends ApplicationEvent {
    private String version;

    public NoticeConsumerEvent(Object source) {
        super(source);
    }

    public NoticeConsumerEvent(Object source, Clock clock) {
        super(source, clock);
    }
}