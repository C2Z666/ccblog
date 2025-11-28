package com.ccblog.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * 握手处理器
 *
 * @author czc
 * @date 2025-10-25
 */
@Slf4j
public class AuthHandshakeHandler extends DefaultHandshakeHandler {

    /**
     * principle后续每次收到消息都可以得到,在这里重写里面的获得值
     * @param request
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        return (Principal) attributes.get("principal"); // 返回在握手阶段注入的数据
    }
}
