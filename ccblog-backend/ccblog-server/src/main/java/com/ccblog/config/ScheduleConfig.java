package com.ccblog.config;

import com.ccblog.template.TimedTaskTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.List;

/**
 * 定时任务初始化类
 * @author czc
 * @date 2025/11/9
 */
@Configuration
@Slf4j
public class ScheduleConfig implements SchedulingConfigurer {
    @Autowired
    private List<TimedTaskTemplate> tasks;

    /**
     * 注册所有定时任务
     * @param taskRegistrar
     */
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        log.info("注册定时任务");
        for (TimedTaskTemplate task : tasks) {
            taskRegistrar.addCronTask(task::execute, task.getCron());
        }
    }
}