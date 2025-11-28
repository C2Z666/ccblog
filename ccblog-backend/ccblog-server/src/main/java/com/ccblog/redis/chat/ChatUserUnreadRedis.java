package com.ccblog.redis.chat;

import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.mapper.chat.ChatUserSessionMapper;
import com.ccblog.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 专门缓存用户未读数
 * @author czc
 * @date 2025/11/6
 */
@Component
public class ChatUserUnreadRedis {
    @Autowired
    private ChatUserSessionMapper chatUserSessionMapper;

    private String buildKey(Long userId){
        return String.format(RedisPrefixConstant.CHAT_USER_TOTAL_UNREAD,userId);
    }

    /**
     * 未读+
     * @param userId
     * @param delta
     */
    public void incrUnread(Long userId,long delta){
        String key = buildKey(userId);
        if(!RedisUtil.hasKey(key)){
            updateContent(userId); // 防止失效导致从0开始加
        }
        RedisUtil.incrBy(key,delta);
    }

    /**
     * 重置计数
     */
    public void resetUnread(Long userId){
        setTotalUnread(userId,0);
    }

    /**
     * 获得未读数
     * @return
     */
    public int getTotalUnread(Long userId){
        String key = buildKey(userId);
        String v = RedisUtil.get(key);
        if(v==null){
            updateContent(userId);
            v = RedisUtil.get(key);
        }
        return v==null?0:Integer.parseInt(v);
    }

    /**
     * 设置值
     * @param userId
     * @param v
     */
    private void setTotalUnread(Long userId,Integer v){
        if(v==null)v=0;
        RedisUtil.set(buildKey(userId),v.toString(),RedisUtil.TTL_DAY);
    }

    /**
     * 更新
     * @param userId
     */
    private void updateContent(Long userId) {
        Integer unread = chatUserSessionMapper.getUnreadByUserId(userId);
        if(unread==null){ // 其实不会,以防万一
            unread=0;
        }
        setTotalUnread(userId,unread);
    }


}