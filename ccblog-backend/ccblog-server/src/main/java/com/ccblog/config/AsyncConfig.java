package com.ccblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务线程池
 * @author czc
 * @date 2025/11/13
 */
@Configuration
public class AsyncConfig {

    /**
     * 异步发布事件
     * @return
     */
    @Bean("publishExecutor")
    public Executor publishExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(16);
        exec.setMaxPoolSize(32);
        exec.setQueueCapacity(2000);
        exec.setThreadNamePrefix("publish-");
//        exec.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        exec.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        exec.initialize();
        return exec;
    }

    /**
     * 异步聚合事件
     * @return
     */
    @Bean("aggregateExecutor")
    public Executor aggregateExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(32);
        exec.setMaxPoolSize(64);
        exec.setQueueCapacity(2000);
        exec.setThreadNamePrefix("aggregate-");
//        exec.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        exec.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        exec.initialize();
        return exec;
    }


    /**
     * 外部调用聚合事件,专门负责调用聚合事件,调用完就跑
     * @return
     */
    @Bean("eventExecutor")
    public Executor eventWriteExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(32);
        exec.setMaxPoolSize(64);
        exec.setQueueCapacity(2000);
        exec.setThreadNamePrefix("event-");
//        exec.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        exec.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        exec.initialize();
        return exec;
    }
}