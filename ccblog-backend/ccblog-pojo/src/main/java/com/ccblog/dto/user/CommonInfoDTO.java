package com.ccblog.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户常用信息(主要给UserInfoRedis使用)
 * @author czc
 * @date 2025/10/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonInfoDTO {
    private static final long serialVersionUID = 7169636386013658631L;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户图像
     */
    private String photo;
}