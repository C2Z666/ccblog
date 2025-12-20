package com.ccblog.service.user;

import com.ccblog.dto.user.BaseUserInfoDTO;
import com.ccblog.dto.user.CommonInfoDTO;
import com.ccblog.dto.user.UserRegisterDTO;
import com.ccblog.vo.user.LoginInfoVO;

/**
 * 处理跟鉴权、账户加密相关的
 * @author czc
 * @date 2025-09-25
 */
public interface LoginService {
    String SESSION_KEY = "f-session";
    String USER_DEVICE_KEY = "f-device";

    /**
     * 根据用户session更新ip信息
     * @param session
     * @param clientIp
     * @return
     */
    BaseUserInfoDTO getAndUpdateUserIpInfoBySessionId(String session, String clientIp);

    /**
     * 用户密码账号登录
     * @param username
     * @param password
     * @return
     */
    LoginInfoVO loginByUserPwd(String username, String password);

    /**
     * 用户密码账号注册
     * @param userRegisterDTO
     * @return
     */
    LoginInfoVO registerByUserPwd(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登出
     * @param session
     */
    void logout(String session);

    /**
     * 发送验证码
     * @param email
     */
    void sendVerifyCode(String email);
}