package com.ccblog.entity.chat;

import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AI 聊天会话汇总实体
 *
 * @author czc
 * @date 2025-10-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAiSession extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 会话id */
    private Long id;

    /* 用户ID */
    private Long userId;

    /* 会话标题 */
    private String title;

    /* 最后消息seq*/
    private Long seq;

    /* 是否删除：0正常 1已删除 */
    private Integer deleted;

    /* 最后消息时间*/
    private LocalDateTime lastMsgTime;


}