package com.ccblog.config;

import com.ccblog.utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 初始化lua
 * @author czc
 * @date 2025/11/14
 */
@Configuration
public class LuaConfig {

    @Bean
    public String newReadEventdSha(ResourceLoader loader) throws IOException {
        String script = StreamUtils.copyToString(
                loader.getResource("classpath:lua/newReadEvent.lua").getInputStream(),
                StandardCharsets.UTF_8);
        return RedisUtil.getLuaSHA(script);
    }
}