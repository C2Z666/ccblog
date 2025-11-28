package com.ccblog.event;

import com.ccblog.dto.notice.NoticeContentAggDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 发送给rabbitmq更新通知内容的信息
 * @author czc
 * @date 2025/10/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeContentUpdEvent {
    private String version;

    private List<NoticeContentAggDTO> noticeContentUpdEventList; // userId:Se<articleId>

}