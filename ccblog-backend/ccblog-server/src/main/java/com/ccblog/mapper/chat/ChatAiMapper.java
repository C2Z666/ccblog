package com.ccblog.mapper.chat;

import com.ccblog.dto.chat.ai.ChatAiAggDTO;
import com.ccblog.dto.chat.ai.ChatAiItemDTO;
import com.ccblog.dto.chat.ai.ChatAiSessionItemDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户AI聊天查询
 * @author czc
 * @date 2025/10/28
 */
@Mapper
public interface ChatAiMapper {

    /**
     * 获取用户和AI聊天历史记录
     *
     * @param sessionId 会话id
     * @param cursor    最前一条时间信息
     * @param cursorSeq 最前一条消息会话消息seq
     * @param limit     数量
     * @return
     */
    List<ChatAiItemDTO> listCursorChatHistory(Long sessionId, LocalDateTime cursor, Long cursorSeq, int limit);


    /**
     * 批量插入/更新用户AI聊天内容
     * @param chatAiAggDTOS
     */
    void upsertContentBatch(List<ChatAiAggDTO> chatAiAggDTOS);

    /**
     * 删除聊天内容
     * @param sessionId
     * @param seq
     */
    @Update("UPDATE chat_ai_history SET deleted=1 WHERE session_id=#{sessionId} AND seq=#{seq}")
    void deleteMsgBySeq(Long sessionId, Long seq);
}