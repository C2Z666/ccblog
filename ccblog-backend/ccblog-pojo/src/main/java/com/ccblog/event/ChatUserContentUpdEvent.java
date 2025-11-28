package com.ccblog.event;

import com.ccblog.dto.chat.user.ChatUserItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送给rabbitmq更新用户聊天内容的信息
 * @author czc
 * @date 2025/10/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatUserContentUpdEvent {
    private String version;

    private List<ChatUserItemDTO> chatUserItemDTOList; // 用户聊天信息

}