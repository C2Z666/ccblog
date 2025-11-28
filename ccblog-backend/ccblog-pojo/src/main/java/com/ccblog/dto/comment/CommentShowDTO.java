package com.ccblog.dto.comment;

import lombok.Data;

import java.util.List;

/**
 * @author czc
 * @date 2025-10-04
 */
@Data
public class CommentShowDTO {
    /**
     * 评论信息(顶级+子评论)
     */
    private List<TopCommentDTO> comments;

    /**
     * 热门评论(定义为点赞数大于0且最多)
     */
    private TopCommentDTO hotComment;
}