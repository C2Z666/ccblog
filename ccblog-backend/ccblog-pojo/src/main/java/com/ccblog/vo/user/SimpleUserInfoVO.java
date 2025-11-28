package com.ccblog.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 简单用户信息
 * @author czc
 * @date 2025/11/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserInfoVO {
    private Long userId;

    private String userName;

    private String avatar;

    /**
     * 粉丝数
     */
    private Integer fansCount;
}