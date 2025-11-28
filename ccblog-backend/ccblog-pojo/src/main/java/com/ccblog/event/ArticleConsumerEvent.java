package com.ccblog.event;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 文章MQ消费者更新完成事件,供@EventListener监听
 * @author czc
 * @date 2025/10/10
 */
@Data
@Builder
public class ArticleConsumerEvent extends ApplicationEvent {
    private String version;

    private Long shardId;

    public ArticleConsumerEvent(Object source) {
        super(source);
    }

    public ArticleConsumerEvent(Object source, String version, Long shardId) {
        super(source);
        this.version = version;
        this.shardId = shardId;
    }

    // 2 参构造器（常用）
    public ArticleConsumerEvent(Object source, Long shardId) {
        super(source);
        this.shardId = shardId;
    }
}