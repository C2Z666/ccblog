package com.ccblog.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 计数相关异步更新事件
 * @author czc
 * @date 2025/11/9
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountUpdateEvent {
    /**
     * 版本
     */
    private String version;

    /**
     * 片id
     */
    private Long shardId;

    /**
     * 事件列表(json格式)
     */
    private List<String> evevnList;
}