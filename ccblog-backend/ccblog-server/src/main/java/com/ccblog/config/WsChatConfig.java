package com.ccblog.config;

import com.ccblog.handler.AuthHandshakeHandler;
import com.ccblog.interceptor.websocket.AuthHandshakeInterceptor;
import com.ccblog.interceptor.websocket.AuthInChannelInterceptor;
import com.ccblog.interceptor.websocket.AuthOutChannelInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * stomp协议的websocket实时聊天
 * @author czc
 * @date 2025-10-23
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker // 开启websocket代理
public class WsChatConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 定义的是客户端接收服务端消息的相关信息
     *
     * @param config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 开启一个简单的基于内存的消息代理，前缀是/user的将消息会转发给消息代理 broker
        // 然后再由消息代理，将消息广播给当前连接的客户端
        config.enableSimpleBroker("/chat", "/user");

// 定义订阅队列的前缀,默认是/user,后续系统拼接为 /user/principle.getName()+自定义的 SendToUser("...")
//        config.setUserDestinationPrefix("/user");

        // 表示配置一个或多个前缀，通过这些前缀过滤出需要被注解方法处理的消息。
        // 例如，前缀为 /app 的 destination 可以通过@MessageMapping注解的方法处理，
        // 而其他 destination （例如 /topic /queue）将被直接交给 broker 处理
        config.setApplicationDestinationPrefixes("/app");
        log.info("成功初始化websocket");

    }


    /**
     * 握手地址(建立连接的地址)
     * 添加一个服务端点，来接收客户端的连接
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 握手阶段不带cookie
        registry.addEndpoint("/chat/{ticket}") // 端点不用改,context-path的不会到这里
                .setAllowedOriginPatterns("*") // 生产要改精确域名
                .setHandshakeHandler(handshakeHandler()) //
                .addInterceptors(handshakeInterceptor()); // 握手拦截器
    }


    /**
     * 配置接收消息的拦截器
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInInterceptor());
    }

    /**
     * 配置返回消息的拦截器
     *
     * @param registration
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelOutInterceptor());
    }


    @Bean
    public HandshakeHandler handshakeHandler() {
        return new AuthHandshakeHandler();
    }

    @Bean
    public HttpSessionHandshakeInterceptor handshakeInterceptor() {
        return new AuthHandshakeInterceptor();
    }

    @Bean
    public ChannelInterceptor channelInInterceptor() {
        return new AuthInChannelInterceptor();
    }

    @Bean
    public ChannelInterceptor channelOutInterceptor() {
        return new AuthOutChannelInterceptor();
    }
}
