package com.ccblog.service.article;

import com.ccblog.dto.article.CategoryDTO;
import com.ccblog.vo.article.CategoryArticlesResponseVO;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-27
 */
public interface CategoryService {

    /**
     * 查询所有分类
     * @return
     */
    List<CategoryDTO> loadAllCategories(boolean filtered);

    /**
     * 查询某个分类下的文章信息和一些其他信息
     * @param categoryId   类别id
     * @param currentPage  当前页
     * @param pageSize     页大小
     * @return
     */
    CategoryArticlesResponseVO queryArticleByCategory(Long categoryId, int currentPage, int pageSize);
}