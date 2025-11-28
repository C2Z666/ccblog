package com.ccblog.interceptor;

import com.ccblog.annotation.Permission;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.enumeration.user.UserRole;
import com.ccblog.service.GlobalInitService;
import com.ccblog.utils.JsonUtil;
import com.ccblog.vo.global.ResVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * 注入全局的配置信息：
 * 站点信息，基本信息，在这里注入
 *
 * @author XuYifei
 * @date 2024-07-12
 * added by czc 2025.09.24
 */
@Slf4j
@Component
public class GlobalViewInterceptor implements AsyncHandlerInterceptor {

    @Autowired
    private GlobalInitService globalInitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {if (handler instanceof HandlerMethod) {
//            log.error("----执行拦截器"); // 每次访问一个链接都会执行这个
        // todo: 【未完成】执行拦截器相关
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Permission permission = handlerMethod.getMethod().getAnnotation(Permission.class);
//            if (permission == null) {
//                permission = handlerMethod.getBeanType().getAnnotation(Permission.class);
//            }
//
//            if (ReqInfoContext.getReqInfo() == null || ReqInfoContext.getReqInfo().getUserId() == null) {
//                if (handlerMethod.getMethod().getAnnotation(ResponseBody.class) != null
//                        || handlerMethod.getMethod().getDeclaringClass().getAnnotation(RestController.class) != null) {
//                    // 访问需要登录的rest接口
//                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//                    response.getWriter().println(JsonUtil.toStr(ResVo.fail(StatusEnum.FORBID_NOTLOGIN)));
//                    response.getWriter().flush();
//                    return false;
//                } else if (request.getRequestURI().startsWith("/api/admin/") || request.getRequestURI().startsWith("/admin/")) {
//                    response.sendRedirect("/admin");
//                } else {
//                    // 访问需要登录的页面时，直接跳转到登录界面
//                    response.sendRedirect("/");
//                }
//                return false;
//            }
//            if (permission.role() == UserRole.ADMIN && !UserRole.ADMIN.name().equalsIgnoreCase(ReqInfoContext.getReqInfo().getUser().getRole())) {
//                // 设置为无权限
//                response.setStatus(HttpStatus.FORBIDDEN.value());
//                return false;
//            }
        }
        return true;
    }
}
