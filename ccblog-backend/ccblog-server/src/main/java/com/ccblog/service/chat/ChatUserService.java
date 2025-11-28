package com.ccblog.service.chat;

import com.ccblog.dto.chat.user.ChatUserItemDTO;
import com.ccblog.vo.chat.ChatUserCursorVO;
import com.ccblog.vo.chat.ChatUserInfoVO;
import com.ccblog.vo.chat.ChatUserNewSessionVO;
import com.ccblog.vo.chat.ChatUserSessionCursorVO;

import java.time.LocalDateTime;

/**
 * 用户相关的聊天服务
 * @author czc
 * @date 2025/10/23
 */
public interface ChatUserService {
    /**
     * 获取用户聊天历史记录
     * @param peerId     对方id信息
     * @param cursor     最前一条时间信息
     * @param cursorSeq  最前一条消息seq
     * @param limit      数量
     * @return           游标消息历史记录
     */
    ChatUserCursorVO getChatHistory(Long peerId, LocalDateTime cursor, Long cursorSeq, Integer limit);

    /**
     * 获取聊天双方基本信息
     * @param peerId 对方id
     * @return
     */
    ChatUserInfoVO getChatUserInfo(Long peerId);

    /**
     * 存储用户信息
     *
     * @param chatUserItemDTO 消息体
     * @param sendUserId      发送者id
     * @param recvUserId      接收者id
     * @return
     */
    ChatUserItemDTO saveChatInfo(ChatUserItemDTO chatUserItemDTO, Long sendUserId, Long recvUserId);

    /**
     * 游标方式获取用户会话列表
     * @param cursor     最后一条时间信息
     * @param cursorId   最后一条消息会话消息id
     * @param limit      数量
     * @return           游标用户会话列表
     */
    ChatUserSessionCursorVO getChatSession(LocalDateTime cursor, Long cursorId, Integer limit);

    /**
     * 生成ticket
     * @return
     */
    String getTicket();

    /**
     * 删除一条信息
     *
     * @param chatUserItemDTO 信息体
     * @return
     */
    ChatUserNewSessionVO deleteMessage(ChatUserItemDTO chatUserItemDTO);

    /**
     * 撤回消息
     *
     * @param chatUserItemDTO 消息体
     * @return
     */
    ChatUserNewSessionVO recallMessage(ChatUserItemDTO chatUserItemDTO);

    /**
     * 会话操作
     * @param type       操作类型
     * @param sessionId  会话id
     */
    void operateSession(Integer type, Long sessionId);

    /**
     * 清除所有已读
     */
    void clearAllUnread();
}