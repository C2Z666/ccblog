package com.ccblog.mapper.article;

import com.ccblog.dto.article.ArticleInteractUpdDTO;
import com.ccblog.dto.article.ArticleStatus;
import com.ccblog.entity.article.ArticleInteraction;
import io.vavr.Tuple2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 关于用户文章交互相关查询
 * @author czc
 * @date 2025-10-03
 */
@Mapper
public interface ArticleInteractionMapper {
    /**
     * 更新用户关于文章的交互
     * @param articleInteraction
     */
    void saveOrUpdateArticleInteraction(ArticleInteraction articleInteraction);

    /**
     * 根据
     * @param userId
     * @param articleId
     * @return
     */
    @Select("SELECT collect_stat collectStatus,like_stat likeStatus," +
            "read_stat readStatus,comment_stat commentStatus" +
            " FROM article_interaction WHERE user_id=#{userId} AND article_id=#{articleId}")
    ArticleStatus getUserStatusByArticleId(Long userId, Long articleId);

    /**
     * 批量插入/更新interacrion数据
     * @param articleInteractUpdDTOList
     */
    void upsertInteractionBatch(List<ArticleInteractUpdDTO> articleInteractUpdDTOList);

    /**
     * 批量获取用户对文章状态
     * @param idxPairs
     * @return
     */
    List<ArticleInteraction> getUserStatusBatch(List<Tuple2<Long, Long>> idxPairs);
}