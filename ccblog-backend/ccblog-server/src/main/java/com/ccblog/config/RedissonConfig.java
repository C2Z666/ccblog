package com.ccblog.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis") // 告诉spring配置位置
@Data // 可以赋值
@Slf4j
public class RedissonConfig {
    @Value("${spring.redis.host}") // 用了ConfigurationProperties就不需要了,这里要注意不要忘记顶级的spring
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.database:0}") // 默认0
    private int database;


    @Bean
    public RedissonClient redissonClient() {
        String address = "redis://" + host + ":" + port;
        Config config = new Config();
        config.setCodec(StringCodec.INSTANCE);   // 全局用 String 编解码（主要是lua脚本执行不能有byte,ArticleInteractionRedis里面的）
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password.isBlank() ? null : password)
                .setDatabase(database);
        return Redisson.create(config);
    }
}