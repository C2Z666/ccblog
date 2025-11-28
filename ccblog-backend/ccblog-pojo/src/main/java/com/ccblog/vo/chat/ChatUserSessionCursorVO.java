package com.ccblog.vo.chat;

import com.ccblog.dto.chat.user.ChatUserSessionItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户会话列表返回类
 * @author czc
 * @date 2025-10-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserSessionCursorVO {
    /**
     * 是否还有更多(聊天会话是否有更多展开)
     */
    private Boolean hasMore;

    List<ChatUserSessionItemDTO> chatUserSessionItemDTOS;
}