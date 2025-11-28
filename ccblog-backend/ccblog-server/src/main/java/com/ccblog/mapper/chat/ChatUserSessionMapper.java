package com.ccblog.mapper.chat;

import com.ccblog.dto.chat.user.ChatUserSessionItemDTO;
import com.ccblog.entity.chat.ChatUserSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户会话相关查询
 * @author czc
 * @date 2025/10/25
 */
@Mapper
public interface ChatUserSessionMapper {

    /**
     * 批量插入用户会话
     * @param chatUserSessions
     */
    void upsertSessionBatch(List<ChatUserSession> chatUserSessions);

    /**
     * 获取用户聊天快照
     * <user_id,peer_id>作为主键天然唯一
     * @param userId
     * @param peerId
     * @return
     */
    @Select("SELECT * FROM chat_user_session " +
            "WHERE user_id=#{userId} AND peer_id=#{peerId}")
    ChatUserSession getSessionByUserId(Long userId, Long peerId);

    /**
     * 游标方式获取用户会话列表
     * @param userId
     * @param cursor
     * @param cursorId
     * @param limit 数量
     * @return
     */
    List<ChatUserSessionItemDTO> getCursorSessionByUserId(Long userId, LocalDateTime cursor, Long cursorId, int limit);

    /**
     * 更新聊天快照
     * @param chatUserSession
     */
    void updateSession(ChatUserSession chatUserSession);

    /**
     * 删除会话
     * @param sessionId
     */
    @Update("UPDATE chat_user_session SET is_delete=1,unread_count=0,is_top=0 WHERE id=#{sessionId}")
    void deleteSessionBySid(Long sessionId);

    /**
     * 根据id更新会话置顶状态
     * @param sessionId
     * @param dbStatCode
     */
    @Update("UPDATE chat_user_session SET is_top=#{dbStatCode} WHERE id=#{sessionId}")
    void topSessionBySid(Long sessionId, int dbStatCode);

    /**
     * 根据id更新会话通知状态
     * @param sessionId
     * @param dbStatCode
     */
    @Update("UPDATE chat_user_session SET is_mute=1 WHERE id=#{sessionId}")
    void muteSessionBySid(Long sessionId, int dbStatCode);

    /**
     * 获得用户的总未读数
     * @param userId
     * @return
     */
    @Select("SELECT SUM(unread_count) FROM chat_user_session WHERE user_id=#{userId}")
    Integer getUnreadByUserId(Long userId);

    /**
     * 获取sessionId对应的会话信息
     *
     * @param sessionId
     * @return
     */
    @Select("SELECT * FROM chat_user_session WHERE id=#{sessionId}")
    ChatUserSession getSessionBySessionId(Long sessionId);

    /**
     * 清空用户已读
     * @param userId
     */
    @Update("UPDATE chat_user_session SET unread_count=0 WHERE user_id=#{userId}")
    void resetUnreadByUserId(Long userId);
}