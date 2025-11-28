package com.ccblog.entity.article;

import com.ccblog.entity.BaseInfo;
import lombok.*;

import java.io.Serializable;

/**
 * 文章详情表
 * @author czc
 * @date 2025-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetail extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 文章ID */
    private Long articleId;
    /* 版本号 */
    private Integer version;
    /* 文章内容（HTML/Markdown） */
    private String content;
}