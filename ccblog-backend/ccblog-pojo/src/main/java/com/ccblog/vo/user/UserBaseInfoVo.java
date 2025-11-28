package com.ccblog.vo.user;

import com.ccblog.dto.user.BaseUserInfoDTO;
import com.ccblog.dto.user.YearArticleDTO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @program: tech-pai
 * @description: 用户个人信息页面所需信息
 * @author: XuYifei
 * @create: 2024-07-06
 * updated by czc 2025.09.30
 */

@Data
@ToString(callSuper = true)
public class UserBaseInfoVo extends BaseUserInfoDTO {
    // 不知道是干嘛的,查看相关注释"返回关注用户选择列表标签"可能是后端返回点开关注页之后的关注和取消关注状态?感觉是个历史遗留垃圾
//    private List<TagSelectDTO> followSelectTags;

    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 粉丝数
     */
    private Integer fansCount;

    /**
     * 是否关注当前用户
     */
    private Boolean followed;

    /**
     * 文章被评论数
     */
    private Integer commentCount;


    /**
     * 文章被点赞数
     */
    private Integer likeCount;

    /**
     * 文章被阅读数
     */
    private Integer readCount;

    /**
     * 文章被收藏数
     */
    private Integer collectCount;

    /**
     * 加入天数
     */
    private Integer joinDayCount;

    /**
     * 已发布文章数
     */
    private Integer articleCount;

    /**
     * 身份信息完整度百分比
     */
    private Integer infoPercent;

    /**
     * 创造历程-每年发布文章数量
     */
    private List<YearArticleDTO> yearArticleList;
}
