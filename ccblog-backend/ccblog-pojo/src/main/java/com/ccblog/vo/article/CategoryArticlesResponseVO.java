package com.ccblog.vo.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.dto.article.ArticleDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author : czc
 * @create: 2025-11-03
 */
@Builder
public record CategoryArticlesResponseVO(
        /**
         * 文章
         */
        IPage<ArticleDTO> articles,

        /**
         * 置顶文章
         */
        List<ArticleDTO> topArticles
) {}
