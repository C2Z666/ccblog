package com.ccblog.dto.article;

import com.ccblog.enumeration.article.SourceTypeEnum;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 点击文章编辑的时候返回的(没有文章详情相关的关于用户点赞等信息)
 * @author czc
 * @date 2025/10/13
 */
@Data
public class ArticleEditDTO {
    private static final long serialVersionUID = -793906904770296838L;

    private Long articleId;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 文章类型：1-博文，2-问答
     */
    private Integer articleType;

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
}