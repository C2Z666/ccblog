package com.ccblog.dto.comment;

import lombok.Data;

/**
 * 评论信息需要的相关元素
 * @author czc
 * @date 2025-10-04
 */
@Data
public class CommentItemDTO extends BaseCommentDTO{
    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 父评论id
     */
    private Long parentCommentId;


    /**
     * 顶级评论id
     */
    private Long topCommentId;

    /**
     * 回复数
     */
    private Integer replyCnt;


    /**
     * 是否删除
     */
    private Boolean deleted;
}