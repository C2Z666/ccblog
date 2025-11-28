package com.ccblog.mapper.chat;

import com.ccblog.dto.chat.ai.ChatAiItemDTO;
import com.ccblog.dto.chat.ai.ChatAiSessionItemDTO;
import com.ccblog.entity.chat.ChatAiSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户AI聊天会话查询
 * @author czc
 * @date 2025/10/28
 */
@Mapper
public interface ChatAiSessionMapper {
    /**
     * 新建一条会话
     * @param chatAiSession
     */
    void saveSession(ChatAiSession chatAiSession);

    /**
     * 游标方式查询用户会话信息
     * @param userId
     * @param cursorId
     * @param limit
     * @return
     */
    List<ChatAiSessionItemDTO> listCursorChatSession(Long userId, Long cursorId, int limit);

    /**
     * 根据sessionId查询相关信息
     * @param sessionId
     * @return
     */
    @Select("SELECT * FROM chat_ai_session WHERE id=#{sessionId}")
    ChatAiSession getSessionBySessionId(Long sessionId);

    /**
     * 批量更新用户会话内容
     * @param chatAiSessions
     */
    void updateChatSessionBatch(List<ChatAiSession> chatAiSessions);


    /**
     * 删除会话
     * @param sessionId
     */
    @Update("UPDATE chat_ai_session SET deleted=1 WHERE id=#{sessionId}")
    void deleteSession(Long sessionId);

    /**
     * 重命名会话
     * @param sessionId
     * @param title
     */
    @Update("UPDATE chat_ai_session SET title=#{title} WHERE id=#{sessionId}")
    void renameSession(Long sessionId, String title);
}