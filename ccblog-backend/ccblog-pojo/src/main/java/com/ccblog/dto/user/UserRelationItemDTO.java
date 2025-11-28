package com.ccblog.dto.user;

import lombok.Data;

/**
 * 用户关系入参
 *
 * @author czc
 * @date 2025-11-9
 */
@Data
public class UserRelationItemDTO {


//    /**
//     * 用户ID
//     */
//    private Long userId;

    /**
     * 粉丝用户ID
     */
    private Long followId;

    /**
     * 是否关注当前用户(可以理解为目标状态)
     */
    private Boolean followed;
}
