package com.ccblog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存模板初始化
 * @author: czc
 * @date 2025-10-11
 */

@Configuration
@Slf4j
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory); // 建立redis并连接

        // === 纯文本序列化器：Key、HashKey、HashValue 全部用 UTF-8 字符串 ===
        StringRedisSerializer utf8 = StringRedisSerializer.UTF_8;

        // Key 序列化：Redis Key 存成纯文本（如 "article:1001"）
        template.setKeySerializer(utf8);

        // HashKey 序列化：Hash 的 field 存成纯文本（如 "read"）
        template.setHashKeySerializer(utf8);

        // HashValue 序列化：Hash 的 value 存成纯文本（如 "120"）
        // → 支持 hGet/hSet/hIncrBy 字段级读写
        template.setHashValueSerializer(utf8);

        template.afterPropertiesSet(); // 立即设置配置
        return template;
    }


}