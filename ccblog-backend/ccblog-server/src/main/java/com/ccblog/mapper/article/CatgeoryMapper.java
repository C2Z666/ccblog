package com.ccblog.mapper.article;

import com.ccblog.dto.article.ArticleNumberOfCategoryDTO;
import com.ccblog.dto.article.CategoryDTO;
import com.ccblog.entity.article.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-28
 */
@Mapper
public interface CatgeoryMapper {

    /**
     * 查询所有ids对应的文章类型
     * @param categoryIds
     * @return
     */
    List<CategoryDTO> listByIds(List<Long> categoryIds);

    /**
     * 查找所有CategoryDTO
     * @return
     */
    @Select("select id as category_id,category_name as category,`rank`,`status` from category")
    List<CategoryDTO> queryAllCategory();

    /**
     * 查询每种类别的文章数
     * @return
     */
    @Select("select c.id categoryId,count(c.id) articleNum from article a left join category c on a.category_id=c.id group by c.id")
    List<ArticleNumberOfCategoryDTO> queryArticleNumberByCategory();

    /**
     * 查找所有类别
     *
     * @return
     */
    @Select("SELECT * FROM category WHERE deleted=0 ORDER BY `rank`")
    List<Category> listAll();
}