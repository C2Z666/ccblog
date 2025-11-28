package com.ccblog.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送给rabbitmq更新用户聊天会话的信息
 * @author czc
 * @date 2025/10/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatUserSessionUpdEvent {
    private String version;

    private List<String> idPairList; // {userId}:{peerId}

}