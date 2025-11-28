package com.ccblog.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 某篇文章的计数信息
 * @author czc
 * @date 2025-10-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCountDTO {
    /**
     * 文章被点赞数
     */
    private Integer likeCnt;

    /**
     * 文章被阅读数
     */
    private Integer readCnt;

    /**
     * 文章被收藏数
     */
    private Integer collectCnt;

    /**
     * 文章被评论数
     */
    private Integer commentCnt;

    /**
     * 文章被举报数
     */
    private Integer reportCnt;

}