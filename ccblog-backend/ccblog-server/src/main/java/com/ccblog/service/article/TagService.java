package com.ccblog.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.TagDTO;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-29
 */
public interface TagService {

    /**
     * 查找所有标签类
     * @return
     */
    List<TagDTO> listAllTags();

    /**
     * 根据标签获取文章
     * @param tagId        标签id
     * @param currentPage  当前页
     * @param pageSize     页大小
     * @return
     */
    IPage<ArticleDTO> queryArticleByTagId(Long tagId, int currentPage, int pageSize);

    /**
     * 获取热门标签
     * @return 热门标签信息
     */
    List<TagDTO> getHotTag(int limit);
}