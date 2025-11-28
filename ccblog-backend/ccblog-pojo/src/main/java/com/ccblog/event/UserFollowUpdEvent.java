package com.ccblog.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送给rabbitmq更新用户计数的信息
 * @author czc
 * @date 2025/10/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowUpdEvent {
    private String version;

    private List<String> followItemList; // 需要更新的userId
}