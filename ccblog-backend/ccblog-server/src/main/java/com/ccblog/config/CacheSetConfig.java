package com.ccblog.config;

import com.ccblog.properties.ProxyProperties;
import com.ccblog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 配置缓存
 *
 * @author czc
 * @date 2025-10-11
 */
@Configuration
@EnableConfigurationProperties(ProxyProperties.class)
@Slf4j
public class CacheSetConfig {
    @Autowired
    private ProxyProperties proxyProperties;

    // 初始化redis
    public CacheSetConfig(RedisTemplate<String, String> redisTemplate) {
        RedisUtil.setTemplate(redisTemplate);
        log.info("成功注册redis");
    }
}
