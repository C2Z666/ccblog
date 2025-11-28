package com.ccblog.dto.article;

import lombok.Data;

/**
 * @author czc
 * @date 2025-09-28
 */
@Data
public class ArticleNumberOfCategoryDTO {
    private Long categoryId; // 种类id
    private Long articleNum; // 种类的数量
}