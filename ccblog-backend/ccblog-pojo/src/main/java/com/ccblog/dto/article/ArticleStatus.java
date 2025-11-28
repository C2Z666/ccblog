package com.ccblog.dto.article;

import lombok.Data;

/**
 * @author czc
 * @date 2025-10-02
 */
@Data
public class ArticleStatus {
    /**
     * 表示当前查看的用户点赞状态
     */
    private Integer likeStatus;

    /**
     * 表示当用户评论状态
     */
    private Integer commentStatus;

    /**
     * 表示当前用户收藏状态
     */
    private Integer collectStatus;

    /**
     * 表示当前用户阅读状态
     */
    private Integer readStatus;
}