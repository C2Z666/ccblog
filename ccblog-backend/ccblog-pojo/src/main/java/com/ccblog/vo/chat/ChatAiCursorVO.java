package com.ccblog.vo.chat;

import com.ccblog.dto.chat.ai.ChatAiItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户消息历史返回类
 * @author czc
 * @date 2025-10-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAiCursorVO {
    /**
     * 是否还有更多(消息记录是否有更多展开)
     */
    private Boolean hasMore;

    List<ChatAiItemDTO> chatAiItems;
}