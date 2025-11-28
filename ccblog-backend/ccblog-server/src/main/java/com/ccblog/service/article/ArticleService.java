package com.ccblog.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.ArticleEditDTO;
import com.ccblog.dto.article.NewArticleDTO;
import com.ccblog.vo.article.ArticleDetailVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

/**
 * @author czc
 * @date 2025-09-27
 */
public interface ArticleService {

    /**
     * 新增文章
     * @param newArticleDTO
     */
    void saveArticle(NewArticleDTO newArticleDTO);

    /**
     * 根据类别查询文章
     * @param currentPage
     * @param pageSize
     * @param category
     * @return
     */
    IPage<ArticleDTO> queryPageArticlesByCategory(int currentPage, int pageSize, Long category);


    /**
     * 查询所有类别id和对应的文章数量
     * @return
     */
    Map<Long, Long> getArticleNumOfCategory();


    /**
     * 根据用户id分页查询文章信息
     * @return
     * @param userId
     * @param currentPage
     * @param pageSize
     */
    IPage<ArticleDTO> queryPageArticleByUserId(Long userId, int currentPage, int pageSize);

    /**
     * 获取文章详情
     * @return
     * @param articleId
     */
    ArticleDetailVO getArticleDetail(Long articleId,Long commentId) throws JsonProcessingException;

    /**
     * 对文章的操作
     *
     * @param articleId
     * @param authorId
     * @param opeatationType 操作代码
     */
    void operateArticle(Long articleId, Long authorId, Integer opeatationType);

    /**
     * 获取用户浏览历史
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<ArticleDTO> getReadHistory(Long userId, int currentPage, int pageSize);

    /**
     * 用户收藏
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<ArticleDTO> getStarArticle(Long userId, int currentPage, int pageSize);

    /**
     * 删除文章
     * @param articleId
     */
    void deleteArticle(Long articleId);

    /**
     * 获取编辑文章需要的信息
     * @param articleId
     * @return
     */
    ArticleEditDTO queryArticle(Long articleId);

    /**
     * 根据类别查询文章
     * @param currentPage
     * @param pageSize
     * @param tagId
     * @return
     */
    IPage<ArticleDTO> queryPageArticlesByTagId(int currentPage, int pageSize, Long tagId);

    /**
     * 根据查询条件获取文章
     *
     * @param query       查询条件
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<ArticleDTO> getArticleByQuery(String query, int currentPage, int pageSize);

    /**
     * 获得草稿文章信息
     * @return
     */
    ArticleEditDTO getLatestDraft();

    /**
     * 获得文章详情的实时数据
     *
     * @param articleId
     * @param queryFix
     * @param categoryId
     * @param authorId
     * @return
     */
    ArticleDetailVO getFullInfo(Long articleId, boolean queryFix, Long categoryId, Long authorId);
}