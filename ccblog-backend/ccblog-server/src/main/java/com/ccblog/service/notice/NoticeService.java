package com.ccblog.service.notice;

import com.ccblog.vo.notice.NoticeVO;

/**
 * 通知服务
 * @author czc
 * @date 2025/10/19
 */
public interface NoticeService {
    /**
     * 获取消息
     * @param type 消息类型
     * @param currentPage 当前页
     * @param pageSize    页大小
     * @return
     */
    NoticeVO getNotices(String type, Integer currentPage, Integer pageSize);

    /**
     * 清除所有已读状态
     */
    void clearAllUnread();
}