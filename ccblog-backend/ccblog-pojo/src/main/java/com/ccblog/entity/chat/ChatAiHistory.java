package com.ccblog.entity.chat;

import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AI 聊天记录实体
 *
 * @author czc
 * @date 2025-10-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAiHistory extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键ID */
    private Long id;

    /* 会话id */
    private Long sessionId;

    /* 对话内消息序号 */
    private Integer seq;

    /* 用户ID */
    private Long userId;

    /* 发送方：0用户 1AI */
    private Integer sender;

    /* AI类型 1chatgpt3.5 2chatgpt4 3讯飞 */
    private Integer aiType;

    /* 消息内容 */
    private String content;

    /* 消耗token */
    private Integer token;

    /* 结束原因 */
    private Integer finishReason;

    /* 是否删除：0正常 1已删除 */
    private Integer deleted;
}