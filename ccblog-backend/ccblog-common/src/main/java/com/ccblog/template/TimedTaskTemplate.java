package com.ccblog.template;

/**
 * 定时任务模板
 * @author czc
 * @date 2025/10/9
 */
public interface TimedTaskTemplate {
    void execute();
    String getCron();
}