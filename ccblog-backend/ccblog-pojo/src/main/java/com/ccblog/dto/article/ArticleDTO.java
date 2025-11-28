package com.ccblog.dto.article;

import com.ccblog.enumeration.article.SourceTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 文章信息
 *
 * @author czc
 * @date 2025-09-28
 * 这个类很多跟表article对不上
 */
@Data
public class ArticleDTO implements Serializable {
    private static final long serialVersionUID = -793906904770296838L;

    private Long articleId;

    /**
     * 文章类型：1-博文，2-问答
     * 预备字段
     */
    private Integer articleType;

    /**
     * 作者uid
     */
    private Long author;

    /**
     * 作者名
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 短标题
     */
    private String shortTitle;

    /**
     * 简介
     */
    private String summary;

    /**
     * 封面
     */
    private String cover;

    /**
     * 正文
     */
    private String content;

    /**
     * 文章来源
     *
     * @see SourceTypeEnum
     */
    private String sourceType;

    /**
     * 原文地址
     */
    private String sourceUrl;

    /**
     * 0 未发布 1 已发布
     */
    private Integer status;

    /**
     * 是否官方
     */
    private Integer officalStat;

    /**
     * 是否置顶
     */
    private Integer toppingStat;

    /**
     * 是否加精
     */
    private Integer creamStat;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    private Long categoryId; // 主要用于代码正常运行

    /**
     * 分类
     */
    private CategoryDTO category;

    /**
     * 标签  每篇文章会有多个标签
     */
    private List<TagDTO> tags;


    /**
     * 当前用户对文章的状态
     */
//    private ArticleUserStatus articleUserStatus;
    private Boolean liked;
    private Boolean commented;
    private Boolean collected; // 暂时先放着

    /**
     * 点赞用户信息
     */
//    private List<SimpleUserInfoDTO> likeUserInfo;

    /**
     * 文章对应的统计计数
     */
    private ArticleCountDTO count;
}
