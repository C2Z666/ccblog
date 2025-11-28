package com.ccblog.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 关注者用户信息
 *
 * @author XuYifei
 * @date 2024-07-12
 * updated by czc 2025.10.01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUserInfoVO implements Serializable {
    private static final long serialVersionUID = 7169636386013658631L;

    /**
     * 当前登录的用户与这个用户之间的关联关系id
     */
    private Long relationId;

    /**
     * 这个字段只有获得粉丝列表才有作用
     * true 表示当前登录用户关注了这个用户
     * false 标识当前登录用户没有关注这个用户
     */
    private Boolean followed;

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
    private String avatar;
}
