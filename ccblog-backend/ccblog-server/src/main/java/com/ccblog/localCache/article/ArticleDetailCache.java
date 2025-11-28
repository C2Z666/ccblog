package com.ccblog.localCache.article;

import com.ccblog.vo.article.ArticleDetailVO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 文章详情模块缓存
 * @author czc
 * @date 2025/10/18
 */
@Component
public class ArticleDetailCache {
    /** 缓存 ArticleDetailVO，30 秒过期 */
    private final Cache<String, ArticleDetailVO> cache =
            Caffeine.newBuilder()
                    .expireAfterWrite(30, TimeUnit.SECONDS)
                    .expireAfterAccess(30, TimeUnit.SECONDS) // 每次读续期
                    .maximumSize(10_000)
                    .build();

    /* -------------------- 对外 API -------------------- */
    private String buildKey(Long articleId) {
        return "article:detail:" + articleId;
    }

    /** 写缓存 */
    public void put(Long articleId, ArticleDetailVO articleDetailVO) {
        cache.put(buildKey(articleId),articleDetailVO);
    }

    /** 读缓存 */
    public ArticleDetailVO get(Long articleId) {
        return cache.getIfPresent(buildKey(articleId));
    }

    /** 主动失效 */
    public void evict(Long articleId) {
        cache.invalidate(buildKey(articleId));
    }

    /** 清掉整个缓存（管理接口） */
    public void clearAll() {
        cache.invalidateAll();
    }
}