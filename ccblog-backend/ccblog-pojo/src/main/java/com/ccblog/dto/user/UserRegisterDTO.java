package com.ccblog.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 基于用户名密码登录的相关请求信息
 *
 * @author XuYifei
 * @date 2024-07-12
 * updated by czc 2025-09-27
 */
@Data
@Accessors(chain = true)
public class UserRegisterDTO implements Serializable {
    private static final long serialVersionUID = 2139742660700910738L;

    /**
     * 登录用户名
     */
    private String userName;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 邮箱
     */
    String email;

    /**
     * 验证码
     */
    String verifyCode;
}
