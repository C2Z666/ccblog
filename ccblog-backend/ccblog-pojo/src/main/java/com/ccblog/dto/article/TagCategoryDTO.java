package com.ccblog.dto.article;

import lombok.Data;

import java.util.List;

/**
 * 标签和类别信息(为了避免连接查询)
 * @author czc
 * @date 2025/11/2
 */
@Data
public class TagCategoryDTO {
    /**
     * 文章id
     * 这个主要是给回源的时候用的(查到的id可以放进来)
     */
    private Long articleId;

    /**
     * 标签id
     */
    private List<Long> tagIds;

    /**
     * 类别id
     */
    private Long categoryId;
}