package com.ccblog.controller.user.user;

import com.ccblog.annotation.Permission;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.user.UserNamePasswordDTO;
import com.ccblog.dto.user.UserRegisterDTO;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.enumeration.user.UserRole;
import com.ccblog.service.user.LoginService;
import com.ccblog.service.user.helper.UserSessionHelper;
import com.ccblog.utils.SessionUtil;
import com.ccblog.vo.global.ResVo;
import com.ccblog.vo.global.ResultVo;
import com.ccblog.vo.user.LoginInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

/**
 * 关于用户登录
 *
 * @author czc
 * @date 2025-09-25
 */
@RestController
@Slf4j
@Tag(name="用户登录",description = "用户登录")
@RequestMapping()
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserSessionHelper userSessionHelper;

    /**
     * 根据用户名和密码登录
     * @param userNamePasswordDTO
     * @param response
     * @return
     */
    @Operation(summary = "账号密码登录")
    @PostMapping("/login/username")
    public ResultVo<LoginInfoVO> login(@RequestBody UserNamePasswordDTO userNamePasswordDTO,
                                         HttpServletResponse response) {
        LoginInfoVO loginInfoVO = loginService.loginByUserPwd(userNamePasswordDTO.getUsername(), userNamePasswordDTO.getPassword());
        String session = userSessionHelper.genToken(loginInfoVO.getUserId());
        if (StringUtils.isNotBlank(session)) {
            response.addCookie(SessionUtil.newCookie(loginService.SESSION_KEY, session));
            return ResultVo.ok(loginInfoVO);
        } else {
            return ResultVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "用户名和密码登录异常，请稍后重试");
        }
    }

    /**
     * 注册账号
     * @param userRegisterDTO
     * @param response
     * @return
     */
    @Operation(summary = "账密注册用户")
    @PostMapping("/register/username")
    public ResVo<LoginInfoVO> register(@RequestBody UserRegisterDTO userRegisterDTO,
                                   HttpServletResponse response) {
        LoginInfoVO loginInfoVO = loginService.registerByUserPwd(userRegisterDTO);
        String session = userSessionHelper.genToken(loginInfoVO.getUserId());
        if (StringUtils.isNotBlank(session)) {
            // cookie中写入用户登录信息，用于身份识别
            response.addCookie(SessionUtil.newCookie(LoginService.SESSION_KEY, session));

            return ResVo.ok(loginInfoVO);
        } else {
            return ResVo.fail(StatusEnum.LOGIN_FAILED_MIXED, "用户名和密码注册异常，请稍后重试");
        }
    }

    @Permission(role = UserRole.LOGIN) // 注解方式表示当前状态
    @PostMapping("/logout")
    public ResVo<Boolean> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 释放会话
        Optional.ofNullable(ReqInfoContext.getReqInfo()).ifPresent(s -> loginService.logout(s.getSession()));
        // 移除cookie
        response.addCookie(SessionUtil.delCookie(LoginService.SESSION_KEY));
        // 重定向到当前页面
//        String referer = request.getHeader("Referer");
//        if (StringUtils.isBlank(referer)) {
//            referer = "/";
//        }
//        response.sendRedirect(referer);
        return ResVo.ok(true);
    }
}