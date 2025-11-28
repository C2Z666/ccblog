package com.ccblog.interceptor.websocket;

import com.ccblog.context.ReqInfoContext;
import com.ccblog.localCache.chat.TicketCache;
import com.ccblog.service.GlobalInitService;
import com.ccblog.service.user.LoginService;
import com.ccblog.utils.SessionUtil;
import com.ccblog.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 握手拦截器, 用于身份验证识别
 *
 * @author czc
 * @date 2025-10-24
 */
@Slf4j
public class AuthHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Autowired
    private TicketCache ticketCache;

    /**
     * 握手前，进行用户身份校验识别
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes: 即对应的是Message中的 simpSessionAttributes 请求头
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        log.info("准备连接");
        String path = request.getURI().getPath();  //  /api/chat/abc123
        // 去掉最左边的 /api/chat  得到  abc123
        String ticket = path.replaceFirst("/api/chat/", ""); // 路径多了/api

        // 验证
        if (ticket.isEmpty()) return false;        // 路径不对直接拒绝
        String userId = ticketCache.get(ticket);
        if (userId == null) return false;

        // 修改用户principle
        attributes.put("principal", new UsernamePasswordAuthenticationToken(userId, null));

        return true;                        // 允许握手
    }

    /**
     * 握手后做的操作
     * @param request
     * @param response
     * @param wsHandler
     * @param ex
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        ;
    }
}
