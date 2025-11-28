package com.ccblog.disruptor.readEvent;

import com.ccblog.event.ReadEvent;
import com.ccblog.redis.article.ArticleCountRedis;
import com.ccblog.redis.article.ArticleInteractionRedis;
import com.ccblog.redis.user.UserCountRedis;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文章阅读事件高速队列
 * @author czc
 * @date 2025/11/14
 */
@Slf4j
@Component
public class ReadEventQueue {

    private final Disruptor<ReadEvent> disruptor;
    private final RingBuffer<ReadEvent> ringBuffer;

    public ReadEventQueue(ReadEventHandler handler) {
        this.disruptor = new Disruptor<>(
                new ReadEventFactory(),
                1 << 14,
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BlockingWaitStrategy());

        disruptor.setDefaultExceptionHandler(new ExceptionHandler<ReadEvent>() {
            public void handleEventException(Throwable ex, long sequence, ReadEvent event) {
                ex.printStackTrace();      // 打印报错日志
            }
            public void handleOnStartException(Throwable ex) {}
            public void handleOnShutdownException(Throwable ex) {}
        });

        // 把 Spring 传进来的依赖交给 Handler
        this.disruptor.handleEventsWith(handler);
        this.disruptor.start();
        this.ringBuffer = disruptor.getRingBuffer();
    }

    /**
     * 入队
     * @param userId
     * @param authorId
     * @param articleId
     */
    public void publish(long userId, long authorId, long articleId) {
        long sequence = ringBuffer.next();
        try {
            ReadEvent event = ringBuffer.get(sequence);
            event.setArticleId(articleId);
            event.setReadTime(System.currentTimeMillis()+ 8 * 3600 * 1000); // 改为北京时间
            event.setUserId(userId);
            event.setAuthorId(authorId);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void shutdown() {
        disruptor.shutdown();
    }

}