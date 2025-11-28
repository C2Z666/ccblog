package com.ccblog.dto.chat.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户AI聊天信息单元
 * @author czc
 * @date 2025/10/23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatAiItemDTO {
    private static final long serialVersionUID = 1L;

    /* 会话ID */
    private Long sessionId;

    /**
     * 序列号
     */
    private Long seq;

    /**
     * 发送者  user/...(model_name)
     */
    private Integer sender;

    /**
     * 内容
     */
    private String content;

    /**
     * 时间
     */
    private LocalDateTime createTime;
}