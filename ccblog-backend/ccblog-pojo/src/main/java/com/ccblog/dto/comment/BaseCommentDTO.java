package com.ccblog.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 评论树状结构
 *
 * @author XuYifei
 * @since 2024-07-12
 */
@Data
public class BaseCommentDTO implements Comparable<BaseCommentDTO> {

    /**
     * 评论对象的用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userPhoto;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 点赞数量
     */
    private Integer likeCnt;

    /**
     * 点踩数量
     */
    private Integer dislikeCnt;

    /**
     * 举报数量
     */
    private Integer reportCnt;


    /**
     * true 表示已经点赞
     */
    private Boolean like;

    /**
     * true 表示已经点踩
     */
    private Boolean dislike;

    /**
     * true 表示已经举报
     */
    private Boolean report;


    @Override
    public int compareTo(@NotNull BaseCommentDTO o) {
        return Long.compare(o.getCommentTime().getTime(), this.commentTime.getTime());
    }
}
