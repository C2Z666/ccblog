package com.ccblog.dto.chat.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI会话游标单元
 * @author czc
 * @date 2025/10/25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatAiSessionItemDTO {
    /* 会话id */
    private Long sessionId;

    /* 会话名 */
    private String title;

    /* 最后信息的时间 */
    private LocalDateTime lastMsgTime;

}