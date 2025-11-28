package com.ccblog.dto.chat.ai;

import lombok.Value;

/**
 * 用于在SSE过程中传递信息
 * @author czc
 * @date 2025/10/29
 */
@Value(staticConstructor = "of")
public class SSEContext {
    Long userId;
    Long sessionId;
}