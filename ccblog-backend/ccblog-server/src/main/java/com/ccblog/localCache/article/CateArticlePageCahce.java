package com.ccblog.localCache.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.dto.article.ArticleDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 缓存类别分页的前3页(后面的很冷门没必要缓存)
 * 5分钟失效,读不续期
 * @author czc
 * @date 2025/11/3
 */
@Component
@Slf4j
public class CateArticlePageCahce {
    static public final int MAX_CACHE_PAGE = 3;
    private final String ARTICLE_ID_SET_KEY = "article:categoryPage:idSet";
    private final String CACHE_KEY_SET_KEY = "article:categoryPage:keySet";

    /** 缓存 IPage<ArticleDTO>，30 秒过期 */
    private final Cache<String, IPage<ArticleDTO>> cache =
            Caffeine.newBuilder()
                    .expireAfterWrite(30, TimeUnit.SECONDS)
                    .maximumSize(10_000)
                    .build();

    /**
     * 所有key的缓存
     */
    private final Cache<String, Set<String>> keySetCache =
            Caffeine.newBuilder()
                    .expireAfterAccess(5, TimeUnit.MINUTES)
                    .build();

    /**
     * articleId缓存
     */
    private final Cache<String, Set<Long>> articleIdSetCache =
            Caffeine.newBuilder()
                    .expireAfterAccess(5, TimeUnit.MINUTES)
                    .build();

    // key构造
    private String buildKey(Long categoryId, int currentPage, int pageSize){
        return "article:categoryPage:"+categoryId+ ":" + currentPage + ":" + pageSize;
    }


    /* -------------------- 对外 API -------------------- */
    /** 写缓存并且注册id */
    public void put(Long categoryId, int currentPage, int pageSize, IPage<ArticleDTO> articlePage) {
        if(currentPage>MAX_CACHE_PAGE) return; // 不缓存
        String key = buildKey(categoryId,currentPage,pageSize);
        cache.put(key,articlePage);
        // 放id
        for(ArticleDTO dto:articlePage.getRecords()){
            Long articleId = dto.getArticleId();
            articleIdSetCache.get(ARTICLE_ID_SET_KEY, k -> ConcurrentHashMap.newKeySet()).add(articleId);
        }
        // 放key
        keySetCache.get(CACHE_KEY_SET_KEY, k -> ConcurrentHashMap.newKeySet()).add(key);
    }

    /** 读缓存 */
    public IPage<ArticleDTO> get(Long categoryId, int currentPage, int pageSize) {
        if(currentPage>MAX_CACHE_PAGE) return null;
        return cache.getIfPresent(buildKey(categoryId,currentPage,pageSize));
    }



    /** 主动失效 */
    public void evict(Long categoryId, int currentPage, int pageSize) {
        cache.invalidate(buildKey(categoryId,currentPage,pageSize));
    }

    /** 清掉整个缓存（管理接口） */
//    public void clearAll() {
//        cache.invalidateAll();
//    }

    /** 判断articleId 是否在缓存内,是的话清除*/
    // 这个一般在用户更新文章的时候保证首页一致性用
    public boolean removeIfExist(Long articleId){
        Set<Long> set = articleIdSetCache.getIfPresent(ARTICLE_ID_SET_KEY);
        if(set==null||set.isEmpty()||!set.contains(articleId)){
            return false;
        }
    //        log.info("失效缓存:articleId={}",articleId);
        // 失效所有caffeine缓存数据
        Set<String> keys = keySetCache.getIfPresent(CACHE_KEY_SET_KEY);
        if (keys != null && !keys.isEmpty()) {
            cache.invalidateAll(keys);       // 批量清
            keySetCache.invalidate(CACHE_KEY_SET_KEY); // key集合也清
            articleIdSetCache.invalidate(ARTICLE_ID_SET_KEY); // 文章集合也清
        }
        return true;
    }
}