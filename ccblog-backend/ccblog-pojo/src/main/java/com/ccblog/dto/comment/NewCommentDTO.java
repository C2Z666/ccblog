package com.ccblog.dto.comment;

import lombok.Data;

/**
 * 评论列表入参
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
public class NewCommentDTO {

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 文章作者id
     */
    private Long authorId;

    /**
     * 文章ID    /专栏ID(目前未扩展)
     */
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 父评论ID  回复时会给
     */
    private Long parentCommentId;

    /**
     * 顶级评论ID  回复时会给
     */
    private Long topCommentId;
}
