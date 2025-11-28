package com.ccblog.service.chat.Impl;

import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.chat.user.ChatUserItemDTO;
import com.ccblog.dto.chat.user.ChatUserSessionItemDTO;
import com.ccblog.dto.chat.user.SessionFromHistoryDTO;
import com.ccblog.dto.user.CommonInfoDTO;
import com.ccblog.entity.chat.ChatUserSession;
import com.ccblog.enumeration.chat.RecallMsgEnum;
import com.ccblog.localCache.chat.TicketCache;
import com.ccblog.localCache.user.GlobalViewCache;
import com.ccblog.mapper.chat.ChatUserMapper;
import com.ccblog.mapper.chat.ChatUserSessionMapper;
import com.ccblog.redis.chat.ChatUserMsgRedis;
import com.ccblog.redis.chat.ChatUserRedis;
import com.ccblog.redis.chat.ChatUserSessionRedis;
import com.ccblog.redis.chat.ChatUserUnreadRedis;
import com.ccblog.redis.notice.NoticeContentRedis;
import com.ccblog.redis.notice.NoticeCountRedis;
import com.ccblog.redis.user.UserInfoRedis;
import com.ccblog.service.chat.ChatUserService;
import com.ccblog.service.chat.helper.ChatUserHelper;
import com.ccblog.utils.TruncateUtil;
import com.ccblog.vo.chat.ChatUserCursorVO;
import com.ccblog.vo.chat.ChatUserInfoVO;
import com.ccblog.vo.chat.ChatUserNewSessionVO;
import com.ccblog.vo.chat.ChatUserSessionCursorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccblog.enumeration.chat.UserSessionFieldEnum.*;
import static com.ccblog.enumeration.chat.RecallMsgEnum.*;
import static com.ccblog.enumeration.chat.UserSessionOperateTypeEnum.*;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_PHOTO;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_USER_NAME;

/**
 * 用户相关的聊天服务实现类
 * @author czc
 * @date 2025/10/23
 */
@Service
public class ChatUserServiceImpl implements ChatUserService {

    @Autowired
    private ChatUserMapper chatUserMapper;

    @Autowired
    private UserInfoRedis userInfoRedis;

    @Autowired
    private NoticeContentRedis noticeContentRedis;

    @Autowired
    private SimpUserRegistry registry;

    @Autowired
    private NoticeCountRedis noticeCountRedis;

    @Autowired
    private ChatUserRedis chatUserRedis;

    @Autowired
    private ChatUserSessionRedis chatUserSessionRedis;

    @Autowired
    private ChatUserSessionMapper chatUserSessionMapper;

    @Autowired
    private TicketCache ticketCache;

    @Autowired
    private ChatUserMsgRedis chatUserMsgRedis;

    @Autowired
    private ChatUserUnreadRedis chatUserUnreadRedis; // 总未读计数

    @Autowired
    private GlobalViewCache globalViewCache; // 全局用户视图缓存(缓存了聊天总数)

    @Autowired
    private ChatUserHelper chatUserHelper;

    /**
     * 获取用户会话列表
     * @param cursor     最后一条时间信息
     * @param cursorId   最后一条消息会话消息id
     * @param limit      数量
     * @return
     */
    public ChatUserSessionCursorVO getChatSession(LocalDateTime cursor, Long cursorId, Integer limit) {
        cursor = (cursor == null ? LocalDateTime.of(8970, 1, 1, 0, 0):cursor);
        cursorId = (cursorId == null ? Long.MAX_VALUE:cursorId);
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        List<ChatUserSessionItemDTO> chatUserSessionDTOs = chatUserSessionMapper.getCursorSessionByUserId(userId,cursor,cursorId,limit+1);

        // 补充用户信息
        Set<Long> operatorIds = chatUserSessionDTOs
                .stream()
                .map(ChatUserSessionItemDTO::getPeerId)
                .collect(Collectors.toSet());
        Map<Long,Map<String, String>> infoMap = userInfoRedis.getInfosBatch(operatorIds,List.of(COL_PHOTO,COL_USER_NAME));
        for (ChatUserSessionItemDTO dto : chatUserSessionDTOs) {
            Map<String, String> info = infoMap.get(dto.getPeerId());
            dto.setPeerPhoto(info.get(COL_PHOTO));
            dto.setPeerName(info.get(COL_USER_NAME));
        }
        int size=chatUserSessionDTOs.size();
        if(size>limit)chatUserSessionDTOs.remove(size-1);
        return ChatUserSessionCursorVO.builder()
                .chatUserSessionItemDTOS(chatUserSessionDTOs)
                .hasMore(size>limit).build();
    }

    /**
     * 获取连接许可ticket
     * @return
     */
    public String getTicket() {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        if (userId == null) {
            return null;
        }
        String ticket = UUID.randomUUID().toString();
        ticketCache.put(ticket,String.valueOf(userId));
        return ticket;
    }

    /**
     * 删除消息
     *
     * @param chatUserItemDTO 信息体
     * @return
     */
    @Transactional
    public ChatUserNewSessionVO deleteMessage(ChatUserItemDTO chatUserItemDTO) {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        boolean isSender = (userId==chatUserItemDTO.getSendUserId());
        Long peerId = chatUserItemDTO.getSendUserId();
        if(isSender){
            peerId = chatUserItemDTO.getRecvUserId();
        }
        chatUserMapper.deleteMsg(userId,peerId,chatUserItemDTO.getSeq(),isSender);
        // 更新快照(对比seq判断是否为最后一条)
        if(chatUserItemDTO.getSeq()
                .equals(Long.parseLong(chatUserSessionRedis.getSession(userId,peerId, COL_DISPLAY_SEQ).toString()))){
            // 从历史记录查(只有删除才会找历史记录)
            SessionFromHistoryDTO sessionFromHistoryDTO = chatUserMapper.getNewSessionByUserId(userId,peerId);
            ChatUserSession chatUserSession=null;
            if(sessionFromHistoryDTO!=null){
                chatUserSession = ChatUserSession.builder()
                        .userId(userId)
                        .peerId(peerId)
                        .snapshot(sessionFromHistoryDTO.getSnapshot())
                        .lastMsgTime(sessionFromHistoryDTO.getLastMsgTime())
                        .displaySeq(sessionFromHistoryDTO.getDisplaySeq()).build();
                if(sessionFromHistoryDTO.getStatus()!= RecallMsgEnum.NORMAL.getCode()){ // 撤回状态
                    chatUserSession.setSnapshot(chatUserHelper.getWithdrawTxt(
                            userId==sessionFromHistoryDTO.getUserId(),sessionFromHistoryDTO.getStatus()));
                }
            }
            // 更新redis
            Map<String, String> map = chatUserHelper.getNewSessionMap(chatUserSession);
            chatUserSessionRedis.setSession(userId,peerId,map);
            chatUserSessionRedis.aggregate(userId,peerId);
//            chatUserSessionMapper.updateSession(chatUserSession); // 更新表
        }
        ChatUserNewSessionVO newSession = ChatUserNewSessionVO.builder()
                .snapshot(chatUserSessionRedis.getSession(userId,peerId,COL_SNAPSHOT).toString())
                .lastMsgTime(LocalDateTime.parse(chatUserSessionRedis.getSession(userId,peerId,COL_LAST_TIME).toString()))
                .build();
        // 失效当前用户最新页面消息缓存
        chatUserMsgRedis.removeMessage(userId,peerId);
        return newSession;
    }

    /**
     * 撤回消息
     *
     * @param chatUserItemDTO 消息体
     * @return
     */
    @Transactional
    public ChatUserNewSessionVO recallMessage(ChatUserItemDTO chatUserItemDTO) {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        Long peerId = chatUserItemDTO.getSendUserId();
        if(userId==chatUserItemDTO.getSendUserId()){
            peerId = chatUserItemDTO.getRecvUserId();
        }
        chatUserMapper.recallMsg(userId,peerId,chatUserItemDTO.getSeq());
        // 更新快照(对比seq判断是否为最后一条),两个人要单独考虑(不一定同时为最后一条,但是如果是最后一条需要更新)
        if(chatUserItemDTO.getSeq()
                .equals(Long.parseLong(chatUserSessionRedis.getSession(userId,peerId, COL_DISPLAY_SEQ).toString()))){
            chatUserSessionRedis.setSession(userId,peerId,
                    Map.of(COL_SNAPSHOT, COL_SELF_RECALL,
                            COL_LAST_TIME,LocalDateTime.now().toString()));
            chatUserSessionRedis.aggregate(userId,peerId);
        }
        if(chatUserItemDTO.getSeq()
                .equals(Long.parseLong(chatUserSessionRedis.getSession(peerId,userId, COL_DISPLAY_SEQ).toString()))){
            chatUserSessionRedis.setSession(peerId,userId,
                    Map.of(COL_SNAPSHOT, COL_PEER_RECALL,
                            COL_LAST_TIME,LocalDateTime.now().toString()));
            chatUserSessionRedis.aggregate(peerId,userId); // 更新对应的快照为撤回状态
        }
        ChatUserNewSessionVO newSession = ChatUserNewSessionVO.builder()
                .snapshot(chatUserSessionRedis.getSession(userId,peerId,COL_SNAPSHOT).toString())
                .lastMsgTime(LocalDateTime.parse(chatUserSessionRedis.getSession(userId,peerId,COL_LAST_TIME).toString()))
                .build();
        // 失效双边缓存
        chatUserMsgRedis.removeMessage(userId,peerId);
        chatUserMsgRedis.removeMessage(peerId,userId);
        return newSession;
    }

    /**
     * 会话操作
     * @param type       操作类型
     * @param sessionId  会话id
     */
    @Transactional
    public void operateSession(Integer type, Long sessionId) {
        if(type==null|| type.equals(EMPTY.getCode())){
            return;
        }
        if(type.equals(DELETE.getCode())){
            // 更新未读数(删除认为清除未读清空)
            ChatUserSession session = chatUserSessionMapper.getSessionBySessionId(sessionId); // 主要是获得用户信息
            long unread = session.getUnreadCount();
            if(unread>0){
                // 清除已读
                Long selfId = session.getUserId();
                Long peerId = session.getPeerId();
                chatUserUnreadRedis.incrUnread(selfId,-session.getUnreadCount());
                chatUserSessionRedis.setSession(selfId,peerId,Map.of(COL_UNREAD_CNT,"0")); // 不需要聚合因为sql下面会更新
                globalViewCache.evict(selfId);
            }
            chatUserSessionMapper.deleteSessionBySid(sessionId);
        }
        else if(type.equals(TOP.getCode())){
            chatUserSessionMapper.topSessionBySid(sessionId, TOP.getDbStatCode());
        }
        else if(type.equals(CANCEL_TOP.getCode())){
            chatUserSessionMapper.topSessionBySid(sessionId, CANCEL_TOP.getDbStatCode());
        }
        else if(type.equals(MUTE.getCode())){
            chatUserSessionMapper.muteSessionBySid(sessionId, CANCEL_TOP.getDbStatCode());
        }
        else if(type.equals(CANCEL_MUTE.getCode())){
            chatUserSessionMapper.muteSessionBySid(sessionId, CANCEL_TOP.getDbStatCode());
        }

    }

    /**
     * 清除某个用户的所有已读
     */
    public void clearAllUnread() {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        chatUserUnreadRedis.resetUnread(userId);
        chatUserSessionRedis.restUnreadByUserId(userId);
        // 写入数据库
        chatUserSessionMapper.resetUnreadByUserId(userId);
        globalViewCache.evict(userId); // 已读变化
    }

    /**
     * 获取聊天记录
     * @param peerId     聊天对方id
     * @param cursor     最后一条时间信息
     * @param cursorSeq  最后一条消息seq
     * @param limit      数量
     * @return
     */
    public ChatUserCursorVO getChatHistory(Long peerId, LocalDateTime cursor, Long cursorSeq, Integer limit) {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        boolean lastestFlag = cursor==null;
        ChatUserCursorVO chatUserCursorVO;
        if(lastestFlag){
            chatUserCursorVO = chatUserMsgRedis.getMessage(userId,peerId,limit);
            if(chatUserCursorVO!=null) return chatUserCursorVO;
        }
        // 非最新一批/未命中
        if(lastestFlag){
            cursor = LocalDateTime.of(8970, 1, 1, 0, 0);
            cursorSeq = Long.MAX_VALUE;
        }
        List<ChatUserItemDTO> chatUserItemDTOs =
                chatUserMapper.listCursorChatHistory(userId,peerId,cursor,cursorSeq,limit+1);
        int size = chatUserItemDTOs.size();
        if(size>limit) chatUserItemDTOs.remove(size-1);
        // 检查撤回
        for(ChatUserItemDTO chatUserItemDTO:chatUserItemDTOs){
            if(chatUserItemDTO.getStatus()!= RecallMsgEnum.NORMAL.getCode()){
                chatUserItemDTO.setContent(
                        chatUserHelper.getWithdrawTxt(userId==chatUserItemDTO.getSendUserId(),chatUserItemDTO.getStatus()));
            }
        }
        chatUserCursorVO = ChatUserCursorVO.builder()
                .chatUserItems(chatUserItemDTOs)
                .hasMore(size>limit).build();
        // 缓存
        if(lastestFlag){
            chatUserMsgRedis.setMessage(chatUserCursorVO,userId,peerId,limit);
        }
        return chatUserCursorVO;
    }

    /**
     * 获取聊天双方基本信息
     * @param peerId 对方id
     * @return
     */
    public ChatUserInfoVO getChatUserInfo(Long peerId) {
        Long selfId = ReqInfoContext.getReqInfo().getUserId();
        Map<Long,Map<String, String>> infoMap =
                userInfoRedis.getInfosBatch(Set.of(selfId, peerId), List.of(COL_USER_NAME, COL_PHOTO));
        CommonInfoDTO selfInfo = CommonInfoDTO.builder()
                .photo(infoMap.get(selfId).get(COL_PHOTO))
                .userName(infoMap.get(selfId).get(COL_USER_NAME))
                .userId(selfId).build();
        CommonInfoDTO peerInfo = CommonInfoDTO.builder()
                .photo(infoMap.get(peerId).get(COL_PHOTO))
                .userName(infoMap.get(peerId).get(COL_USER_NAME))
                .userId(peerId).build();
        ChatUserInfoVO chatUserInfoVO = ChatUserInfoVO.builder()
                .peerInfo(peerInfo)
                .selfInfo(selfInfo).build();

        // 更新未读数(点击就把未读清空)
        chatUserHelper.clearUnread(selfId,peerId);
        return chatUserInfoVO;
    }

    /**
     * 保存消息
     *
     * @param chatUserItemDTO 消息体
     * @param sendUserId      发送者id
     * @param recvUserId      接收者id
     * @return 添加最新seq后的ChatUserItemDTO
     */
    public ChatUserItemDTO saveChatInfo(ChatUserItemDTO chatUserItemDTO, Long sendUserId, Long recvUserId) {
        boolean isOnline = registry.getUser(recvUserId.toString()) != null;
        String content= chatUserHelper.getContentShow(chatUserItemDTO.getContent(),chatUserItemDTO.getType());

        // --- 处理聊天相关信息
        // 获取最新seq
        long seq = chatUserSessionRedis.incrSeq(sendUserId,recvUserId,1);
        chatUserItemDTO.setSeq(seq); // 获得最新的seq
        // 聊天会话(两方都要更新)
        // 发送端更新快照、时间和最后一条显示信息的seq
        Map<String,String> map = Map.of(
                COL_LAST_TIME, chatUserItemDTO.getCreateTime().toString(),
                COL_SNAPSHOT, TruncateUtil.truncate(content).getText(),
                COL_DISPLAY_SEQ,String.valueOf(seq)
        );
        chatUserSessionRedis.setSession(sendUserId,recvUserId,map);
        chatUserSessionRedis.aggregate(sendUserId,recvUserId);
        // 接收端更新快照时间和未读(假如不在线)
        if(!isOnline){
            chatUserSessionRedis.incrUnread(recvUserId,sendUserId,1); // 会话未读+1
            chatUserUnreadRedis.incrUnread(recvUserId,1); // 总未读+1
        }
        chatUserSessionRedis.setSession(recvUserId,sendUserId,map);
        chatUserSessionRedis.aggregate(recvUserId,sendUserId);
        // 聊天内容
        chatUserRedis.aggregate(chatUserItemDTO);
        // 失效双边缓存
        chatUserMsgRedis.removeMessage(sendUserId,recvUserId);
        chatUserMsgRedis.removeMessage(recvUserId,sendUserId);
        return chatUserItemDTO;
    }
}