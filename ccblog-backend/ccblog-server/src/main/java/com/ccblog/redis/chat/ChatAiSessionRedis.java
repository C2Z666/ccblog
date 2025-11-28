package com.ccblog.redis.chat;

import com.ccblog.entity.chat.ChatAiSession;
import com.ccblog.enumeration.chat.AiSessionFieldEnum;
import com.ccblog.mapper.chat.ChatAiSessionMapper;
import com.ccblog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static com.ccblog.constant.RedisPrefixConstant.CHAT_AI_SESSION;
import static com.ccblog.enumeration.chat.AiSessionFieldEnum.*;
import static com.ccblog.enumeration.chat.UserSessionFieldEnum.COL_SEQ;

/**
 * 存储用户会话相关信息(单条会话)
 * 不负责落库只读写加速
 * @author czc
 * @date 2025/10/28
 */
@Component
@Slf4j
public class ChatAiSessionRedis {

    @Autowired
    private ChatAiSessionMapper chatAiSessionMapper;

    /**
     * 设置值
     */
    public void setSession(Long sessionId, Map<String,String> map){
        String key = String.format(CHAT_AI_SESSION,sessionId);
        RedisUtil.hMSet(key, map, RedisUtil.TTL_DAY);
    }

    /**
     * 获取用户AI会话某个域的值
     * @param field
     * @return 当前数值；不存在返回不知道
     */
    public String getSession(Long sessionId, String field) {
        String key = String.format(CHAT_AI_SESSION,sessionId);
        String v = RedisUtil.hGet(key, field);
        if(v==null){ // 这种情况是redis的数据丢失
            updateChatSession(sessionId);
            v = RedisUtil.hGet(key, field);
        }
        return v;
    }


    /**
     * seq++
     * @param sessionId
     */
    public long incrSeq(Long sessionId,long delta) {
        String key = String.format(CHAT_AI_SESSION, sessionId);
        if(!RedisUtil.hasKey(key)){
            updateChatSession(sessionId);
        }
        return RedisUtil.hIncrBy(key, AiSessionFieldEnum.COL_SEQ, delta);
    }

    /**
     * 更新用户会话信息
     * @param sessionId
     */
    public void updateChatSession(Long sessionId){
        ChatAiSession chatAiSession = chatAiSessionMapper.getSessionBySessionId(sessionId);
        Map<String, String> map;
        if(chatAiSession !=null){
            map = Map.of(
                    COL_LAST_TIME, chatAiSession.getLastMsgTime().toString(),
                    COL_SEQ, chatAiSession.getSeq().toString()
            );
        }
        else{ // 稳健性考虑(查不到聊天记录)
            map = Map.of(
                    COL_LAST_TIME, LocalDateTime.now().toString(),
                    COL_SEQ,"0"
            );
        }
        setSession(sessionId,map);
    }
}