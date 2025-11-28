package com.ccblog.entity.notice;

import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通知内容实体
 *
 * @author czc
 * @date 2025-10-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeContent extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 接收者用户ID */
    private Long userId;
    /* 触发者用户ID */
    private Long operateUserId;
    /* 通知类型：1私信 2回复 3评论 4点赞 5收藏 6关注 */
    private Integer type;
    /* 目标类型：1文章 2评论 3专栏 */
    private Integer targetType;
    /* 目标主键 */
    private Long targetId;
    /* 列表摘要（前50字） */
    private String relatedInfo;
    /* 已读标记：0未读 1已读 */
    private Integer readFlag;
    /* 是否截断：0未截断 1已截断 */
    private Integer truncated;
    /* 状态位：0无效 1有效 */
    private Integer status;
    /* 逻辑删除：0未删除 1已删除 */
    private Integer deleted;
}