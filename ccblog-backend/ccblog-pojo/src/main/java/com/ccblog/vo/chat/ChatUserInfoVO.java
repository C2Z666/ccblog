package com.ccblog.vo.chat;

import com.ccblog.dto.user.CommonInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聊天的时候双方的信息
 * @author czc
 * @date 2025/10/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatUserInfoVO {
    /**
     * 自己的信息
     */
    private CommonInfoDTO selfInfo;

    /**
     * 聊天对象信息
     */
    private CommonInfoDTO peerInfo;
}