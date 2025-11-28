package com.ccblog.dto.chat.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户聊天信息单元
 * @author czc
 * @date 2025/10/23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserItemDTO {
//    /**
//     * 消息id
//     */
//    private Long messageId;

    /**
     * 序列号
     */
    private Long seq;

    /**
     * 发送者id
     */
    private Long recvUserId;

    /**
     * 接收者id
     */
    private Long sendUserId;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 内容
     */
    private String content;

    /**
     * 已读状态
     */
    private boolean readFlag;

    /**
     * 撤回状态
     */
    private Integer status;

    /**
     * 时间
     */
    private LocalDateTime createTime;
}