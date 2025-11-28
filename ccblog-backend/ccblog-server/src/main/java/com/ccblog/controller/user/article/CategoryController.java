package com.ccblog.controller.user.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.concurrentCache.CategoryCache;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.CategoryDTO;
import com.ccblog.service.article.ArticleService;
import com.ccblog.service.article.CategoryService;
import com.ccblog.vo.article.CategoryArticlesResponseVO;
import com.ccblog.vo.global.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文章分类相关接口
 * @author czc
 * @date 2025-09-27
 */
@Slf4j
@Tag(name="文章分类接口",description = "文章分类接口")
@RequestMapping("/category")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取指定分类下的文章信息
     */
    @GetMapping("/articles")
    @Operation(summary = "主页查询文章")
    public ResultVo<CategoryArticlesResponseVO> getArticlesByCategory(@RequestParam(name = "categoryId", required = false) Long categoryId,
                                                                      @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                                      @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        CategoryArticlesResponseVO categoryArticlesResponseVO =
                                categoryService.queryArticleByCategory(categoryId,currentPage,pageSize);
        return ResultVo.ok(categoryArticlesResponseVO);
    }

    /**
     * 获取所有文章分类
     * @param filtered 是否过滤掉数量为0的类别
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有文章分类")
    public ResultVo<List<CategoryDTO>> listAllCategories(@RequestParam(name = "filtered", required = false) boolean filtered){
        List<CategoryDTO> categoryDTOS = categoryService.loadAllCategories(filtered);
        return ResultVo.ok(categoryDTOS);
    }

}
