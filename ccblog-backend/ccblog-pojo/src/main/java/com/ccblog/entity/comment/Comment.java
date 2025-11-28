package com.ccblog.entity.comment;

import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 评论实体
 *
 * @author czc
 * @date 2025-09-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 文章ID */
    private Long articleId;
    /* 用户ID（评论者） */
    private Long userId;
    /* 评论内容 */
    private String content;
    /* 顶级评论ID（0表示本身就是顶级） */
    private Long topCommentId;
    /* 父评论ID（0表示顶级） */
    private Long parentCommentId;
    /* 点赞数 */
    private Integer likeCnt;
    /* 举报数 */
    private Integer reportCnt;
    /* 0-正常，1-已删 */
    private Integer deleted;
}