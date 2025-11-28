package com.ccblog.entity.comment;
import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户对评论互动足迹实体
 *
 * @author czc
 * @date 2025-09-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentInteraction extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 用户ID */
    private Long userId;
    /* 评论ID */
    private Long commentId;
    /* 点赞状态：0-未点赞，1-已点赞，2-取消点赞 */
    private Integer likeStat;
    /* 点踩状态：0-无，1-已踩，2-取消 */
    private Integer dislikeStat;
    /* 举报状态 */
    private Integer reportStat;

    /* 0-正常，1-已删 */
    private Integer deleted;
}