package com.ccblog.mapper.chat;

import com.ccblog.dto.chat.user.ChatUserItemDTO;
import com.ccblog.dto.chat.user.SessionFromHistoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author czc
 * @date 2025/10/23
 */
@Mapper
public interface ChatUserMapper {

    /**
     * 游标获取两人聊天记录
     * @param userId 自己id
     * @param peerId 对方id
     * @param cursor
     * @param cursorSeq
     * @param limit
     * @return
     */
    List<ChatUserItemDTO> listCursorChatHistory(Long userId, Long peerId, LocalDateTime cursor, Long cursorSeq, Integer limit);

    /**
     * 批量插入用户聊天信息
     * @param chatUserItemDTOList
     */
    void upsertContentBatch(List<ChatUserItemDTO> chatUserItemDTOList);


    /**
     * 获取用户聊天快照
     *
     * @param userId
     * @param peerId
     * @return
     */
    SessionFromHistoryDTO getNewSessionByUserId(Long userId, Long peerId);

    /**
     * 删除消息
     *
     * @param userId
     * @param peerId
     * @param seq
     * @param isSender
     */
    void deleteMsg(Long userId, Long peerId, Long seq, boolean isSender);

    /**
     * 撤回消息
     */
    @Update("UPDATE chat_user_history SET status=1 " +
            "WHERE (sender_id,receiver_id) in ((#{userId},#{peerId}),(#{peerId},#{userId})) AND seq=#{seq}")
    void recallMsg(Long userId, Long peerId, Long seq);
}