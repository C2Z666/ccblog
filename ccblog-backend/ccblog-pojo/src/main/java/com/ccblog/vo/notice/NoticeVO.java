package com.ccblog.vo.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.dto.notice.NoticeDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author czc
 * @date 2025/10/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeVO {
    /**
     * 消息详情表
     */
    private IPage<NoticeDetailDTO> list;

    /**
     * 未读消息数表
     */
    private Map<String,Integer> unreadCountMap;

    /**
     * 当前消息类型
     */
    private String selectType;

}