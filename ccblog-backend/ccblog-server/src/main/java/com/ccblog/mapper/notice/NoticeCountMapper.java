package com.ccblog.mapper.notice;

import com.ccblog.entity.notice.NoticeCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 通知计数相关
 * @author czc
 * @date 2025/10/20
 */
@Mapper
public interface NoticeCountMapper {

    /**
     * 查询用户未读通知计数
     * @param userId
     * @return
     */
    @Select("SELECT user_id            AS userId, " +
            "       unread_system     AS unreadSystem, " +
            "       unread_reply       AS unreadReply, " +
            "       unread_comment     AS unreadComment, " +
            "       unread_like        AS unreadLike, " +
            "       unread_collect     AS unreadCollect, " +
            "       unread_follow      AS unreadFollow " +
            "FROM notice_count " +
            "WHERE user_id = #{userId}")
    NoticeCount getCountByUserId(Long userId);

    /**
     * 批量插入/更新用户未读信息数
     * @param noticeCountList
     */
    void upsertNoticeCountBatch(List<NoticeCount> noticeCountList);
}