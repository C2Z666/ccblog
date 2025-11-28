package com.ccblog.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录成功后返回给用户的信息
 * @author czc
 * @date 2025/10/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoVO {
    private static final long serialVersionUID = 7169636386013658631L;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String photo;

}