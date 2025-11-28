package com.ccblog.mapper.notice;

import com.ccblog.dto.notice.NoticeContentAggDTO;
import com.ccblog.dto.notice.NoticeDetailDTO;
import com.ccblog.vo.notice.NoticeVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 消息信息相关查询
 * @author czc
 * @date 2025/10/19
 */
@Mapper
public interface NoticeContentMapper {

    /**
     *
     * @param noticeContentUpdEventList 消息更新体
     */
    void upsertContentBatch(List<NoticeContentAggDTO> noticeContentUpdEventList);

    /**
     * 根据类型查询最新的若干条评论
     * @param userId 接收者id
     * @param type   消息类型
     * @return
     */
    Page<NoticeDetailDTO> getNoticePageByType(Long userId, Integer type);

    /**
     * 清除一个人某个类型的全部已读
     * @param userId
     * @param type 为null那么清除全部已读
     */
    void clearReadStat(Long userId, Integer type);
}