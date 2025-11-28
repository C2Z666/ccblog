package com.ccblog.redis.chat;

import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.template.SimpleObjectRedisTemplate;
import com.ccblog.vo.chat.ChatUserCursorVO;
import org.springframework.stereotype.Component;

/**
 * 用户最新聊天内容缓存
 * @author czc
 * @date 2025/10/30
 */
@Component
public class ChatUserMsgRedis extends SimpleObjectRedisTemplate<ChatUserCursorVO> {

    @Override
    protected String buildKey(Object... args) {
        Long userId = (Long) args[0];
        Long peerId = (Long) args[1];
        Integer limit = (Integer) args[2];
        return String.format(RedisPrefixConstant.CHAT_USER_LATEST_MSG,userId,peerId,limit);
    }

    @Override
    protected String buildRemovePattern(Object... args) {
        Long userId = (Long) args[0];
        Long peerId = (Long) args[1];
        return String.format(RedisPrefixConstant.CHAT_USER_LATEST_MSG,userId,peerId,"*");
    }

    @Override
    protected Class<ChatUserCursorVO> getRedisClass() {
        return ChatUserCursorVO.class;
    }

    /**
     * 拿数据
     */
    public ChatUserCursorVO getMessage(Long userId, Long peerId, Integer limit){
        return super.getContent(userId,peerId,limit);
    }

    /**
     * 存数据
     */
    public boolean setMessage(ChatUserCursorVO chatUserCursorVO,Long userId, Long peerId, Integer limit){
        return super.setContent(chatUserCursorVO,userId,peerId,limit);
    }

    /**
     * 主动失效
     */
    public void removeMessage(Long userId,Long peerId){
        super.removeContent(userId,peerId);
    }
}