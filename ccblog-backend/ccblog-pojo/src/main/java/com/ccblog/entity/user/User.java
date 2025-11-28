package com.ccblog.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ccblog.entity.BaseInfo;
import lombok.*;

import java.io.Serializable;

/**
 * 用户登录表
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 第三方用户ID
     */
    private String thirdAccountId;

    /**
     * 登录方式: 0-微信登录，1-账号密码登录
     */
    private Integer loginType;

    /**
     * 删除标记
     */
    private Integer deleted;

    /**
     * 登录用户名
     */
    private String userName;

    /**
     * 登录密码，密文存储
     */
    private String password;

}
