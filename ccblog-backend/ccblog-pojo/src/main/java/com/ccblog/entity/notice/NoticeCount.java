package com.ccblog.entity.notice;

import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通知未读计数实体
 *
 * @author czc
 * @date 2025-10-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCount extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 用户ID */
    private Long userId;
    /* 未读私信数 */
    private Integer unreadSystem;
    /* 未读回复数 */
    private Integer unreadReply;
    /* 未读评论数 */
    private Integer unreadComment;
    /* 未读点赞数 */
    private Integer unreadLike;
    /* 未读收藏数 */
    private Integer unreadCollect;
    /* 未读关注数 */
    private Integer unreadFollow;
}