package com.ccblog.vo.chat;

import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 更新后的聊天会话
 *
 * @author czc
 * @date 2025-10-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserNewSessionVO extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键ID */
    private Long id;

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