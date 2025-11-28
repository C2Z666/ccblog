package com.ccblog.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ccblog.entity.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户关系表
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_relation")
public class UserRelation extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主用户ID
     */
    private Long userId;

    /**
     * 粉丝用户ID
     */
    private Long followUserId;

    /**
     * 最后关注时间
     */
    private LocalDateTime lastFollowTime;

    /**
     * 关注状态: 0-未关注，1-已关注，2-取消关注
     */
    private Integer followState;
}
