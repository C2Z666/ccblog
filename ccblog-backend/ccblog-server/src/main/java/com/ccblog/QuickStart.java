package com.ccblog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@ServletComponentScan // 用于WebFilter,过滤器,ReqRecordFilter获取上下文使用
@EnableScheduling // 开启定时任务调度
@EnableAsync // 异步能力
public class QuickStart {
    public static void main(String[] args) {
        SpringApplication.run(QuickStart.class, args);
        log.info("server started");
    }
}
