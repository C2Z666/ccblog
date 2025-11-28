package com.ccblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author czc
 * @date 2025/10/28
 */
@Configuration
public class WebClientConfig {

    /**
     * SSE web代理
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                // 统一 baseUrl、过滤器、编解码器等可在这里配
                // .baseUrl("http://api.xxx.com")
                // .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}