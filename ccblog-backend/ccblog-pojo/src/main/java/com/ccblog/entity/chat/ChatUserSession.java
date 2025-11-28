package com.ccblog.entity.chat;

import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户聊天会话实体
 *
 * @author czc
 * @date 2025-10-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserSession extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键ID */
    private Long id;

    /* 当前用户ID */
    private Long userId;

    /* 对方用户ID */
    private Long peerId;

    /* 当前快照seq */
    private Long displaySeq;

    /* 当前seq */
    private Long seq;

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

    /* 是否删除会话：0正常 1已删除 */
    private Integer isDelete;


}