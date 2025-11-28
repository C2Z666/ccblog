package com.ccblog.event;

import com.ccblog.dto.chat.ai.ChatAiAggDTO;
import com.ccblog.dto.chat.user.ChatUserItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送给rabbitmq更新用户AI聊天内容的信息
 * @author czc
 * @date 2025/10/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatAiContentUpdEvent {
    private String version;

    private List<ChatAiAggDTO> chatAiAggDTOS; // 用户聊天信息

}