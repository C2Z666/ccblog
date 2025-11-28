package com.ccblog.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息详情 DTO
 * 对应前端 NoticeMsgType
 * @author czc
 * @date 2025-10-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息主键 id
     */
    private Long msgId;

    /**
     * 关联主体 ID(文章/评论/专栏/用户)
     */
    private Long relatedId;

    /**
     * 关联主体类型(1:文章 2:评论 3:专栏 4:用户)
     */
    private Integer relatedType;

    /**
     * 展示摘要: 标题(文章)或 前50字(评论,私信)
     */
    private String relatedInfo;

    /**
     * 触发用户 id
     */
    private Long operateUserId;

    /**
     * 触发用户昵称
     */
    private String operateUserName;

    /**
     * 触发用户头像 url
     */
    private String operateUserPhoto;

    /**
     * 消息类型 @NoticeTypeEnum
     */
    private Integer type;

    /**
     * 阅读状态：0 未读 1 已读
     */
    private Integer state;

    /**
     * 消息产生时间
     */
    private LocalDateTime createTime;

    /**
     * 扩展id,用于主体内容是评论的时候提供上下文
     */
    private Long extendId;

    /**
     * 扩展内容
     */
    private String extendInfo;
}