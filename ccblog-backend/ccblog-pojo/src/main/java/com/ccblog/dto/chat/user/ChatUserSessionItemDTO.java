package com.ccblog.dto.chat.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户会话游标单元
 * @author czc
 * @date 2025/10/25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserSessionItemDTO {
    /* 会话id */
    private Long sessionId;

    /* 当前用户ID */
    private Long userId;

    /* 对方用户ID */
    private Long peerId;

    /**
     * 对方用户头像
     */
    private String peerPhoto;

    /* 当前快照seq */
    private Long displaySeq;

    /**
     * 对方用户名
     */
    private String peerName;

    /* 最新消息快照（前50字） */
    private String snapshot;

    /* 最后一条消息发送时间 */
    private LocalDateTime lastMsgTime;

    /* 未读消息数量 */
    private Integer unreadCount;

    /* 是否置顶：0不置顶 1置顶 */
    private Integer isTop;

    /* 是否关闭通知：0接收 1不接收 */
    private Integer isMute;


}