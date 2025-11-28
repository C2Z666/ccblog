package com.ccblog.entity.article;

import com.ccblog.entity.BaseInfo;
import lombok.*;

import java.io.Serializable;

/**
 * 文章标签映射表
 * @author czc
 * @date 2025-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 文章ID */
    private Long articleId;
    /* 标签ID */
    private Long tagId;
    /* 版本号 */
    private Integer version;
}