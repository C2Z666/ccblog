package com.ccblog.service.chat.helper;

import com.ccblog.entity.chat.ChatUserSession;
import com.ccblog.enumeration.chat.MessageTypeEnum;
import com.ccblog.localCache.user.GlobalViewCache;
import com.ccblog.redis.chat.ChatUserSessionRedis;
import com.ccblog.redis.chat.ChatUserUnreadRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.ccblog.enumeration.chat.RecallMsgEnum.*;
import static com.ccblog.enumeration.chat.RecallMsgEnum.COL_PEER_RECALL;
import static com.ccblog.enumeration.chat.UserSessionFieldEnum.*;
import static com.ccblog.enumeration.chat.UserSessionFieldEnum.COL_UNREAD_CNT;

/**
 *
 * @author czc
 * @date 2025/11/27
 */
@Component
public class ChatUserHelper {
    @Autowired
    private ChatUserSessionRedis chatUserSessionRedis;

    @Autowired
    private ChatUserUnreadRedis chatUserUnreadRedis; // 总未读计数

    @Autowired
    private GlobalViewCache globalViewCache; // 全局用户视图缓存(缓存了聊天总数)

    /**
     * 获取更新redis所需的新session值
     * @param chatUserSession 最新快照信息
     * @return
     */
    public Map<String, String> getNewSessionMap(ChatUserSession chatUserSession) {
        Map<String,String> map;
        if(chatUserSession ==null){
            map = Map.of(
                    COL_SNAPSHOT, "",
                    COL_DISPLAY_SEQ,"0"
            );
        }
        else{
            map = Map.of(
                    COL_LAST_TIME, chatUserSession.getLastMsgTime().toString(),
                    COL_SNAPSHOT, chatUserSession.getSnapshot(),
                    COL_DISPLAY_SEQ, chatUserSession.getDisplaySeq().toString()
            );
        }
        return map;
    }


    public String getContentShow(String content, Integer type){ // 获取消息预览类型(文字保留,图片、语音等转为相应文字)
        if(type== MessageTypeEnum.TEXT.getType()){
            ;
        }
        else if(type==MessageTypeEnum.IMAGE.getType()){
            content= MessageTypeEnum.IMAGE.getMsg();
        } else if (type==MessageTypeEnum.VOICE.getType()) {
            content = MessageTypeEnum.VOICE.getMsg();
        } else if (type==MessageTypeEnum.VIDEO.getType()) {
            content = MessageTypeEnum.VIDEO.getMsg();
        }
        return content;
    }

    /**
     * 根据是否自己发送返回撤回显示语句
     * @param isSender
     * @return
     */
    public String getWithdrawTxt(boolean isSender, Integer status){
        if(status.equals(SYSTEM_WITHDRAW.getCode())){
            return COL_SYSTEM_RECALL;
        }
        if(isSender) return COL_SELF_RECALL;
        else return COL_PEER_RECALL;
    }

    /**
     * 清除某个会话的已读
     * @param selfId 自己
     * @param peerId 对方
     */
    public void clearUnread(Long selfId, Long peerId){
        String unreadStr = chatUserSessionRedis.getSession(selfId,peerId,COL_UNREAD_CNT).toString();
        if(unreadStr!=null){
            int unread = Integer.parseInt(unreadStr);
            if(unread>0){
                chatUserSessionRedis.setSession(selfId,peerId,Map.of(COL_UNREAD_CNT,"0"));
                chatUserSessionRedis.aggregate(selfId,peerId);
                chatUserUnreadRedis.incrUnread(selfId,-unread); // 总未读-会话未读
                globalViewCache.evict(selfId);
            }
        }
    }
}