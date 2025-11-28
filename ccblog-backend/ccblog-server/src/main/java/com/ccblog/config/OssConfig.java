package com.ccblog.config;

import com.ccblog.properties.AliOssProperties;
import com.ccblog.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类,用于创建AliOssUtil对象(因为一开始默认是不会初始化这个工具对象的,所以需要在配置类里面实例化)
 */
@Configuration
@Slf4j
public class OssConfig {

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        if(aliOssProperties.getEndpoint()==null||aliOssProperties.getEndpoint().isBlank()){
            log.warn("未配置阿里云OSS信息");
        }
        else {
            log.info("开始创建阿里云文件上传工具类对象:{}",aliOssProperties);
        }
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getBucketName());
    }
}
