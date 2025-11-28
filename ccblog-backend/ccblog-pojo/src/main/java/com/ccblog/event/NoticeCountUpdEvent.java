package com.ccblog.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送给rabbitmq更新用户未读通知计数的信息
 * @author czc
 * @date 2025/10/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeCountUpdEvent {
    private String version;

    private List<String> userIdList; // 需要更新的userId
}