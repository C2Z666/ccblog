package com.ccblog.dto.chat.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 从历史记录插最新会话视图
 * @author czc
 * @date 2025/10/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionFromHistoryDTO {

    /* 当前用户ID */
    private Long userId;
    /* 对方用户ID */
    private Long peerId;
    /* 最新消息快照（前50字） */
    private String snapshot;
    /* 最后一条消息发送时间 */
    private LocalDateTime lastMsgTime;
    /* 展示对应的seq */
    private Long displaySeq;
    /* 未读消息数量 */
    private Integer unreadCount;
    /* 是否置顶：0不置顶 1置顶 */
    private Integer isTop;
    /* 是否关闭通知：0接收 1不接收 */
    private Integer isMute;
    /* 是否删除会话：0正常 1已删除 */
    private Integer isDelete;

    /**
     * 撤回状态
     */
    private Integer status;
}