package com.ccblog.service.article.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.concurrentCache.CategoryCache;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.CategoryDTO;
import com.ccblog.entity.article.Category;
import com.ccblog.mapper.article.CatgeoryMapper;
import com.ccblog.service.article.ArticleService;
import com.ccblog.service.article.CategoryService;
import com.ccblog.vo.article.CategoryArticlesResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author czc
 * @date 2025-09-27
 */
@Service
public class CategroyServiceImpl implements CategoryService {

    @Autowired
    private CatgeoryMapper catgeoryMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryCache categoryCache;

    /**
     *
     * @param categoryId   类别id
     * @param currentPage  当前页
     * @param pageSize     页大小
     * @return
     */
    public CategoryArticlesResponseVO queryArticleByCategory(Long categoryId, int currentPage, int pageSize) {
        // 前端给的是一个分类id
        // 分页拿到文章
        IPage<ArticleDTO> articles = articleService.queryPageArticlesByCategory(currentPage, pageSize, categoryId);

        // todo:【未完成】调整文章顺序,
//        List<ArticleDTO> topArticles = indexRecommendHelper.topArticleList(selectedCategory);
        List<ArticleDTO> topArticles = new ArrayList<>(); // 先不管

        CategoryArticlesResponseVO responseVO = new CategoryArticlesResponseVO(articles, topArticles);
        return responseVO;
    }

    /**
     * 查询所有文章分类
     * @return
     */
    public List<CategoryDTO> loadAllCategories(boolean filtered) {
//        List<CategoryDTO> categoryDTOList = catgeoryMapper.queryAllCategory();
        // 直接从cache读类别信息
        List<Category> allCategoryList = categoryCache.getAllCategoryList();
        List<CategoryDTO> categories = new ArrayList<>();
        for(Category category:allCategoryList){
            categories.add(CategoryDTO.builder()
                    .category(category.getCategoryName())
                    .categoryId(category.getId())
                    .rank(category.getRank()).build());
        }
        categories.add(0,CategoryDTO.DEFAULT_CATEGORY);
        if(filtered){
            // 查询所有分类的对应的文章数
            Map<Long, Long> articleCnt = articleService.getArticleNumOfCategory();
            articleCnt.put(0L,articleCnt.values().stream().mapToLong(Long::longValue).sum()); // 加入默认的全部选项
            // 过滤掉文章数为0的分类
            categories.removeIf(c -> articleCnt.getOrDefault(c.getCategoryId(), 0L) <= 0L);
        }
        return categories;
    }
}