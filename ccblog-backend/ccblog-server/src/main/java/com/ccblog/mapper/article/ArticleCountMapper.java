package com.ccblog.mapper.article;

import com.ccblog.dto.article.ArticleCountDTO;
import com.ccblog.entity.article.ArticleCount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author czc
 * @date 2025/10/7
 */
@Mapper
public interface ArticleCountMapper {

    /**
     * 插入一条计数信息
     * @param articleCount
     */
    @Insert("INSERT INTO article_count(article_id) VALUES(#{articleId})")
    void saveArticleCount(ArticleCount articleCount);

    /**
     * 批量插入/更新文章计数信息
     * @param articleCountList 文章计数信息集合
     */
    void updateCountbyArticleIdBatch(List<ArticleCount> articleCountList);

    /**
     * 根据文章id查询文章的点赞+收藏+阅读+评论数
     * @param articleId
     * @return
     */
    @Select("SELECT read_cnt AS readCnt,like_cnt AS likeCnt,collect_cnt AS collectCnt," +
            "comment_cnt AS commentCnt, report_cnt AS reportCnt FROM article_count WHERE article_id=#{articleId}")
    ArticleCountDTO getCountByArticleId(Long articleId);

    /**
     * 插入/更新文章计数
     * 使用的是增量
     * @param articleCountList  增量计数
     */
    void upsertDeltaCountBatch(List<ArticleCount> articleCountList);

    /**
     * 批量更新文章计数数据
     * @param articleIds
     * @return
     */
    List<ArticleCount> getArticleCountBatch(List<Long> articleIds);
}