package com.ccblog.service.chat;

import cn.hutool.json.JSONObject;
import com.ccblog.dto.chat.ai.ChatAiRequestDTO;
import com.ccblog.vo.chat.ChatAiCursorVO;
import com.ccblog.vo.chat.ChatAiSessionCursorVO;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * 用户ai聊天接口
 * @author czc
 * @date 2025/10/28
 */
public interface ChatAiService {

    /**
     * 获得AI回复
     * @param userRequset 用户请求消息体
     * @return
     */
    Flux<ServerSentEvent<JSONObject>> getAnswer(ChatAiRequestDTO userRequset);

    /**
     * 获取用户AI聊天历史
     * @param sessionId 会话id
     * @param cursor    最前一条时间信息
     * @param cursorSeq 最前一条seq
     * @param limit     数量
     */
    ChatAiCursorVO getChatHistory(Long sessionId, LocalDateTime cursor, Long cursorSeq, int limit);

    /**
     *
     * @param cursorId  游标sessionId
     * @param limit     数量
     * @return
     */
    ChatAiSessionCursorVO getChatSession(Long cursorId, int limit);

    /**
     * 用户删除会话
     * @param sessionId 会话id
     */
    void deleteSession(Long sessionId);

    /**
     * 重命名会话
     * @param sessionId
     * @param title
     */
    void renameSession(Long sessionId, String title);

    /**
     * 用户删除聊天记录
     * @param sessionId
     * @param seq       会话内递增序号
     */
    void deleteMessage(Long sessionId, Long seq);
}