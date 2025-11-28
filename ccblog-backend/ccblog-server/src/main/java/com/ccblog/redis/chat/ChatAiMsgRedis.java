package com.ccblog.redis.chat;

import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.template.SimpleObjectRedisTemplate;
import com.ccblog.vo.chat.ChatAiCursorVO;
import org.springframework.stereotype.Component;

/**
 * 用户AI聊天最新聊天内容缓存
 * @author czc
 * @date 2025/10/30
 */
@Component
public class ChatAiMsgRedis extends SimpleObjectRedisTemplate<ChatAiCursorVO> {

    @Override
    protected String buildKey(Object... args) {
        Long sessionId = (Long) args[0];
        Integer limit = (Integer) args[1];
        return String.format(RedisPrefixConstant.CHAT_AI_LATEST_MSG,sessionId,limit);
    }

    @Override
    protected String buildRemovePattern(Object... args) {
        Long sessionId = (Long) args[0];
        return String.format(RedisPrefixConstant.CHAT_AI_LATEST_MSG,sessionId,"*");
    }

    @Override
    protected Class<ChatAiCursorVO> getRedisClass() {
        return ChatAiCursorVO.class;
    }

    /**
     * 拿数据
     */
    public ChatAiCursorVO getMessage(Long sessionId, Integer limit) {
        return super.getContent(sessionId,limit);
    }

    /**
     * 存数据
     */
    public boolean setMessage(ChatAiCursorVO chatAiCursorVO, Long sessionId, Integer limit) {
        return super.setContent(chatAiCursorVO,sessionId,limit);
    }

    /**
     * 主动失效
     */
    public void removeMessage(Long sessionId){
        super.removeContent(sessionId);
    }
}