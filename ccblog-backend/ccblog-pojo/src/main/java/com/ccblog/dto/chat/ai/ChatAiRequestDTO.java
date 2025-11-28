package com.ccblog.dto.chat.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 前端ai请求内容
 * @author czc
 * @date 2025/10/28
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatAiRequestDTO {

    /**
     * 会话id
     */
    private Long sessionId;

    /**
     * 模型名称
     */
    private String aiType;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;



}