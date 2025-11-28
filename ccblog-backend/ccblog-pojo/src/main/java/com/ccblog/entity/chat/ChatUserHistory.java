package com.ccblog.entity.chat;

import com.ccblog.entity.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户聊天记录实体
 *
 * @author czc
 * @date 2025-10-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserHistory extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键ID */
    private Long id;
    /* 对话内消息序号 */
    private Integer seq;
    /* 发送者ID */
    private Long senderId;
    /* 接收者ID */
    private Long receiverId;
    /* 消息内容 */
    private String content;
    /* 消息类型：0文本 1图片 2文件 3语音 4视频 */
    private Integer msgType;
    /* 状态：0正常 1发送者撤回 2系统删除 */
    private Integer status;
    /* 阅读标识：0未读 1已读 */
    private Integer readFlag;
    /* 发送方删除：0正常 1已删除 */
    private Integer deletedByS;
    /* 接收方删除：0正常 1已删除 */
    private Integer deletedByR;
}