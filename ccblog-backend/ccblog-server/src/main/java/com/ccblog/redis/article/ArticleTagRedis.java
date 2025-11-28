package com.ccblog.redis.article;

import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.dto.article.ArticleIdTagDTO;
import com.ccblog.mapper.article.ArticleTagMapper;
import com.ccblog.template.ObjectRedisTemplate;
import com.ccblog.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 只缓存 articleId+tagIds(articleId可以不存,但是基本没什么影响,不新建对象了)
 * @author czc
 * @date 2025/11/2
 */
@Component
public class ArticleTagRedis extends ObjectRedisTemplate<Long, ArticleIdTagDTO> {

    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 获取单个key数据
     * @param articleId
     * @return
     */
    public ArticleIdTagDTO getTag(Long articleId){
        return super.getContent(articleId);
    }

    /**
     * 批量获得key数据
     * @param articleIdSet
     * @return
     */
    public Map<Long,ArticleIdTagDTO> getTagBatch(Set<Long> articleIdSet){
        return super.getContentBatch(articleIdSet);
    }

    /**
     * 设置数据
     * @param articleId
     * @param tag            域对应值
     * @return
     */
    public boolean setTag(Long articleId,ArticleIdTagDTO tag){
        return super.setContent(articleId,tag);
    }

    /**
     * 批量设置
     * @param articleIds
     * @param tags    标签列表
     * @return
     */
    public void setTagBatch(List<Long> articleIds,List<ArticleIdTagDTO> tags){
        super.setContentBatch(articleIds,tags);
    }

    /**
     * 失效
     */
    public void removeTag(Long articleId){
        RedisUtil.remove(buildKey(articleId));
    }

    @Override
    protected String buildKey(Long articleId) {
        return String.format(RedisPrefixConstant.ARTICLE_TAG_CATEGORY,articleId);
    }

    /**
     * 单数据回源
     * @param articleId
     */
    protected void updateRedis(Long articleId) {
        ArticleIdTagDTO articleIdTagDTO = articleTagMapper.getByArticleId(articleId);
        setTag(articleId,articleIdTagDTO);
    }

    /**
     * 批量回源
     * @param articleIds
     */
    protected void updateRedisBatch(List<Long> articleIds) {
        List<ArticleIdTagDTO> articleIdTagDTOS = articleTagMapper.listByArticleIds(articleIds);
        articleIds = articleIdTagDTOS.stream().map(ArticleIdTagDTO::getArticleId).toList(); // 重新赋值为和tagDTO匹配
        setTagBatch(articleIds,articleIdTagDTOS);
    }

    /**
     * 获得类
     * @return 类
     */
    protected Class<ArticleIdTagDTO> getRedisClass() {
        return ArticleIdTagDTO.class;
    }


    /**
     * 设置ttl
     */
    public ArticleTagRedis(){
        // 不怎么变,直接存储7天
        super(RedisUtil.TTL_DAY*7);
    }
}