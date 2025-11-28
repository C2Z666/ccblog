package com.ccblog.service.article.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.TagDTO;
import com.ccblog.localCache.article.HotTagCache;
import com.ccblog.mapper.article.ArticleTagMapper;
import com.ccblog.mapper.article.TagMapper;
import com.ccblog.service.article.ArticleService;
import com.ccblog.service.article.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-29
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private HotTagCache hotTagCache;

    /**
     * 查找所有标签类
     * @return
     */
    public List<TagDTO> listAllTags() {
        List<TagDTO> tagDTOS = tagMapper.queryAllTag();
        return tagDTOS;
    }

    /**
     * 查找标签对应文章
     * @param tagId        标签id
     * @param currentPage  当前页
     * @param pageSize     页大小
     * @return
     */
    public IPage<ArticleDTO> queryArticleByTagId(Long tagId, int currentPage, int pageSize) {
        IPage<ArticleDTO> articles = articleService.queryPageArticlesByTagId(currentPage, pageSize, tagId);
        return articles;
    }

    /**
     * 获取热门标签
     * 目前计算热度为标签对应文章数量,后续可以改为其他
     * @return
     */
    public List<TagDTO> getHotTag(int limit) {
        List<TagDTO> result;
        result = hotTagCache.get(limit);
        if(result!=null){
            return result;
        }
//        实测10000文章大约需要40ms,加上缓存可以接受
        result = articleTagMapper.getTagByHot(limit);
        hotTagCache.put(result,limit);
        return result;
    }
}