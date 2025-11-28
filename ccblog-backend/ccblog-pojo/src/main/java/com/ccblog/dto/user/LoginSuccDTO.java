package com.ccblog.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录成功返回值
 * @author czc
 * @date 2025/10/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccDTO {
    private Long userId;
    
    private String session;
}