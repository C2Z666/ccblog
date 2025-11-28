package com.ccblog.entity.article;
import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户文章记录实体
 *
 * @author czc
 * @date 2025-09-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleInteraction extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 用户ID */
    private Long userId;
    /* 文章ID */
    private Long articleId;
    /* 发布该文章的用户ID */
    private Long articleUserId;

    /* 收藏状态：0-未收藏，1-已收藏，2-取消收藏 */
    private Integer starStat;
    /* 阅读状态：0-未读，1-已读 */
    private Integer readStat;
    /* 评论状态：0-未评论，1-已评论，2-删除评论 */
    private Integer commentStat;
    /* 点赞状态：0-未点赞，1-已点赞，2-取消点赞 */
    private Integer likeStat;
    /* 用户打分：1-5，NULL表示未评分 */
    private Integer score;
    /* 阅读时长（秒） */
    private Integer readDuration;
    /* 首次阅读时间 */
    private LocalDateTime firstReadTime;
    /* 末次阅读时间 */
    private LocalDateTime lastReadTime;

    /* 0-正常，1-已删 */
    private Integer deleted;
}