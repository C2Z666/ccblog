package com.ccblog.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ip信息
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpInfo implements Serializable {
    private static final long serialVersionUID = -4612222921661930429L;

    private String firstIp;

    private String firstRegion;

    private String latestIp;

    private String latestRegion;
}