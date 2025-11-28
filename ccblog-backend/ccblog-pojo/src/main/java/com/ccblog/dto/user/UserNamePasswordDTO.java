package com.ccblog.dto.user;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 保存用户名+密码登录的请求
 *
 * @author XuYifei
 * @create 2024-06-21
 */

@Data
public class UserNamePasswordDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7169636386013658631L;

    private String username;
    private String password;
}
