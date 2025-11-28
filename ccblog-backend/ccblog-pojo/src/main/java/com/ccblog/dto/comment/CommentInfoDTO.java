package com.ccblog.dto.comment;

import lombok.Data;

/**
 * 用于查询评论所属文章的标题和专栏标题,用于补充通知里面评论的更多信息
 * @author czc
 * @date 2025/10/20
 */
@Data
public class CommentInfoDTO {

    /**
     * 文章/专栏标题
     */
    private String title;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 文章/专栏id
     */
    private Long docId; // 文章/专栏标题
}