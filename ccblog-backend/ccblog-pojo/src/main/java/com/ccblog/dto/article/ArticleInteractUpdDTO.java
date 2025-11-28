package com.ccblog.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章交互信息的增量类,保存用户状态等信息,用于redis存储方便发布批量事件
 * 是ArticleInteraction的子集
 * @author czc
 * @date 2025/10/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleInteractUpdDTO {
    /* 用户ID */
    private Long userId;
    /* 文章ID */
    private Long articleId;
    /* 收藏状态：0-未收藏，1-已收藏，2-取消收藏 */
    private Integer collectStat;
    /* 阅读状态：0-未读，1-已读 */
    private Integer readStat;
    /* 评论状态：0-未评论，1-已评论，2-删除评论 */
    private Integer commentStat;
    /* 点赞状态：0-未点赞，1-已点赞，2-取消点赞 */
    private Integer likeStat;
    /* 用户打分：1-5，NULL表示未评分 */
    private Integer score;
    /* 末次阅读时间 */
    private LocalDateTime lastReadTime;
}