package com.ccblog.dto.chat.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI聊天消息聚合单元
 * @author czc
 * @date 2025/10/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAiAggDTO {
    private static final long serialVersionUID = 1L;

    /* 会话ID */
    private Long sessionId;

    /* 对话内消息序号 */
    private Long seq;

    /* 用户ID */
    private Long userId;

    /* 发送方：0用户 1 AI */
    private Integer sender;

    /* AI类型：1chatgpt3.5 2chatgpt4 3讯飞 */
    private Integer aiType;

    /* 消息内容 */
    private String content;

    /* 消耗token */
    private Integer token;

    /* 时间 */
    private LocalDateTime createTime;

    /* 结束原因 */
    private Integer finishReason;
}