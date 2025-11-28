package com.ccblog.localCache.comment;

import com.ccblog.vo.comment.CommentCursorVO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/** 评论信息缓存
 * 评论游标模块缓存
 * @author czc
 * @date 2025/10/18
 */
@Component
public class CommentCursorCache {
    /** 缓存 CommentCursorVO，30 秒过期 */
    private final Cache<String, CommentCursorVO> cache =
            Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .expireAfterAccess(30, TimeUnit.SECONDS) // 每次读续期
                .maximumSize(10_000)
                .build();

    /**
     * 索引缓存
     */
    private final Cache<String, Set<String>> indexCache =
            Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES) // 存活10min(如果对实时性要求非常高,就改成时间更长,比如一天)
                .build();


    /* -------------------- 对外 API -------------------- */
    String buildKey(Long articleId,Long parentId, LocalDateTime cursor, Integer limit){
        long seconds = cursor.toEpochSecond(ZoneOffset.ofHours(8)); // 东八区
        return "comment:cursor:"+articleId+ ":" + parentId + ":" + seconds + ":" + limit;
    }

    /** 写缓存并且注册id */
    public void put(Long articleId,Long parentId, LocalDateTime cursor, Integer limit, CommentCursorVO commentCursorVO) {
        String key = buildKey(articleId,parentId,cursor,limit);
        cache.put(key,commentCursorVO);
        // 登记到缓存
        String indexKey = "commentIdx:" + articleId + ":" + parentId;
        indexCache.get(indexKey, k -> ConcurrentHashMap.newKeySet()).add(key);
    }

    /** 读缓存 */
    public CommentCursorVO get(Long articleId,Long parentId, LocalDateTime cursor, Integer limit) {
        return cache.getIfPresent(buildKey(articleId,parentId,cursor,limit));
    }

    /**
     * 新增评论后失效对应缓存
     * @param articleId 文章id
     * @param parentId  父评论id
     */
    public void evictById(Long articleId, Long parentId) {
        String indexKey = "commentIdx:" + articleId + ":" + parentId;
        Set<String> keys = indexCache.getIfPresent(indexKey);
        if (keys != null && !keys.isEmpty()) {
            cache.invalidateAll(keys);   // 批量清
            indexCache.invalidate(indexKey); // 索引本身也清
        }
    }

    /** 主动失效 */
    public void evict(Long articleId,Long parentId, LocalDateTime cursor, Integer limit) {
        cache.invalidate(buildKey(articleId,parentId,cursor,limit));
    }

    /** 清掉整个缓存（管理接口） */
    public void clearAll() {
        cache.invalidateAll();
    }
}