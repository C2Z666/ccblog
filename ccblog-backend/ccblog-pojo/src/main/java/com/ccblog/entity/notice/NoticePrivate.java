package com.ccblog.entity.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户私信详情实体
 *
 * @author czc
 * @date 2025-10-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticePrivate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 私信完整原文
     */
    private String content;

    /**
     * 记录创建时间
     */
    private LocalDateTime createdTime;
}