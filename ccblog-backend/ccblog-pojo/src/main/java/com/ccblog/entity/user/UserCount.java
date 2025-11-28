package com.ccblog.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户计数表
 * @author czc
 * @date 2025/10/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCount {
    /**
     * 主键
     */ private Long id;

    /**
     * 用户ID（全局唯一）
     */
    private Long userId;

    /**
     * 粉丝数
     */
    private Integer fanCnt;

    /**
     * 关注数
     */
    private Integer followCnt;

    /**
     * 被阅读总数
     */
    private Integer readCnt;

    /**
     * 被评论总数
     */
    private Integer commentCnt;

    /**
     * 被点赞总数
     */
    private Integer likeCnt;

    /**
     * 被收藏总数
     */
    private Integer collectCnt;

    /**
     * 最后更新时间(MySQL 自动维护)
     */
    private LocalDateTime updateTime;
}
