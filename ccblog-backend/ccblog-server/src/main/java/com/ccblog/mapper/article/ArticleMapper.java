package com.ccblog.mapper.article;

import com.ccblog.annotation.AutoFill;
import com.ccblog.dto.article.ArticleEditDTO;
import com.ccblog.dto.user.YearArticleDTO;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.entity.article.Article;
import com.ccblog.entity.article.ArticleDetail;
import com.ccblog.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 文章有关查询
 * @author czc
 * @date 2025-09-27
 */
@Mapper
public interface ArticleMapper {

    /**
     * 保存文章
     * @param article
     */
    @AutoFill(value = OperationType.INSERT) // 自动插入时间
    void upsertArticle(Article article);

    /**
     * 插入文章详情
     * @param articleDetail
     */
    @AutoFill(value = OperationType.INSERT) // 自动插入时间
    @Insert("insert into article_detail(article_id,version,content,create_time,update_time)" +
            "values(#{articleId},#{version},#{content},#{createTime},#{updateTime})")
    void saveArticleDetail(ArticleDetail articleDetail);

    /**
     * 根据articleId查询version(默认没删除的)
     * @param articleId
     * @return
     */
    @Select("select version from article_detail where and article_id=#{articleId}")
    Long getVersionByArticleId(Long articleId);

    /**
     * 根据articleId移除文章
     * @param articleId
     */
    @Update("update article set deleted=1 where id=#{articleId}")
    void removeArticleByArticleId(Long articleId);

    /**
     * 根据articleId移除文章详情
     * @param articleId
     */
    @Update("update article_detail set deleted=1 where article_id=#{articleId}")
    void removeArticleDetailByArticleId(Long articleId);

    /**
     * 根据用户id查询文章
     * @param userId
     * @return
     */
    Page<ArticleDTO> getPageByUserId(Long userId);

    /**
     * 根据类别查询文章
     * @param categoryId
     * @return
     */
    Page<ArticleDTO> queryPageArticleByCategory(Long categoryId);

    /**
     * 根据用户id查询ArticleDTO信息
     * @param userId
     * @return
     */
    Page<ArticleDTO> queryPageArticleByUserId(Long userId);


    /**
     * 查询用户每年发布的文章数量
     * @param userId
     * @return
     */
    List<YearArticleDTO> listYearArticle(Long userId);

    /**
     * 获取文章详细信息
     * @param articleId
     * @return
     */
    ArticleDTO getArticleDetailByArticleId(Long articleId);

    /**
     * 获取用户阅读历史
     * @param userId
     * @return
     */
    Page<ArticleDTO> listReadHistoryByUserId(Long userId);

    /**
     * 获取用户收藏历史
     * @param userId
     * @return
     */
    Page<ArticleDTO> listStarByUserId(Long userId);

    /**
     * 删除文章
     * @param articleId
     */
    @Update("UPDATE article SET deleted=1 WHERE id=#{articleId}")
    void deleteByArticleId(Long articleId);

    /**
     * 获得文章编辑信息
     * @param articleId
     * @return
     */
    ArticleEditDTO getArticleEdit(Long articleId);

    /**
     * 根据id获得标题用于通知摘要
     * @param articleId
     * @return
     */
    @Select("SELECT title FROM article WHERE id=#{articleId}")
    String getTitleByArticleId(Long articleId);

    /**
     * 根据tagId查询单纯文章信息
     * @param tagId
     * @return
     */
    Page<ArticleDTO> queryPageArticleByTagId(Long tagId);

    /**
     * 分页搜索文章
     * @param query
     * @return
     */
    Page<ArticleDTO> queryPageArticleByQuery(String query);

    /**
     * 获得最新的草稿id
     * @return
     */
    @Select("SELECT id FROM article WHERE user_id=#{userId} AND `status`=0 ORDER BY create_time DESC LIMIT 1")
    Long getLastDraftArticleId(Long userId);
}