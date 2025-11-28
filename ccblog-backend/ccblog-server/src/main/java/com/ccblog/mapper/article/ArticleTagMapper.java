package com.ccblog.mapper.article;

import com.ccblog.dto.article.ArticleIdTagDTO;
import com.ccblog.dto.article.TagDTO;
import com.ccblog.entity.article.ArticleTag;
import com.ccblog.entity.article.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-27
 */
@Mapper
public interface ArticleTagMapper {
    /**
     * 插入article-tag映射
     * @param articleTags article_tag 表 item
     */
    void addByTagIdAndArticleId(List<ArticleTag> articleTags);

    /**
     * 批量获取文章的tag信息
     * @param articleIds 索引
     * @return
     */
    List<ArticleIdTagDTO> listByArticleIds(List<Long> articleIds);

    /**
     * 获取某篇文章的tag信息
     * @param articleId 索引
     * @return
     */
    ArticleIdTagDTO getByArticleId(Long articleId);

    /**
     * 根据热度获取limit标签
     * @param limit
     * @return
     */
    List<TagDTO> getTagByHot(int limit);
}