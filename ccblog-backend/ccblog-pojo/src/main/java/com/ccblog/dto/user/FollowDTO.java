package com.ccblog.dto.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 主要用于存储查询得到的id和时间
 * @author czc
 * @date 2025/10/16
 */
@Data
public class FollowDTO {
    private static final long serialVersionUID = 7169636386013658631L;
    private Long userId;

    private LocalDateTime lastFollowTime;

}