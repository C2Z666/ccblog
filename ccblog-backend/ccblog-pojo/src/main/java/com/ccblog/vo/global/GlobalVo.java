package com.ccblog.vo.global;

import com.ccblog.dto.user.BaseUserInfoDTO;
import com.ccblog.vo.SeoTagVo;
import com.ccblog.vo.SiteCntVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author XuYifei
 * @date 2024-07-12
 * added by czc 2025.09.24
 * updated by czc 2025.11.6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalVo {
    /**
     * 网站相关配置
     */
    private GlobalViewVo siteInfo;

    /**
     * 站点统计信息
     */
    private SiteCntVo siteStatisticInfo;

    /**
     * 今日的站点统计
     */
    private SiteCntVo todaySiteStatisticInfo;

    /**
     * 环境
     */
    private String env;

    /**
     * 是否已登录
     */
    private Boolean isLogin;

    /**
     * 登录用户信息
     */
    private BaseUserInfoDTO user;

    /**
     * 消息通知数量
     */
    private Integer noticeMsgNum;

    /**
     * 用户消息数量
     */
    private Integer userMsgNum;

    /**
     * 在线用户人数
     */
    private Integer onlineCnt;

    private String currentDomain;

    private List<SeoTagVo> ogp;
    private String jsonLd;

}
