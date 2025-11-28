package com.ccblog.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知内容异步落库聚合体
 *
 * @author czc
 * @date 2025-10-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeContentAggDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 接收者用户ID */
    private Long userId;

    /* 触发者用户ID */
    private Long operateUserId;

    /* 通知类型：1私信 2回复 3评论 4点赞 5收藏 6关注 */
    private Integer type;

    /* 目标类型：1文章 2评论 3专栏 */
    private Integer targetType;

    /* 已读标记 */
    private boolean readFlag;

    /* 目标主键 */
    private Long targetId;

    /* 列表摘要（≤200 Bytes） */
    private String relatedInfo;

    /* 是否被截断 */
    private Boolean truncated;

    /* 状态 */
    private Boolean status;

    /* 消息产生时间 */
    private LocalDateTime createTime;

}