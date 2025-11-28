package com.ccblog.localCache.article;

import com.ccblog.dto.article.TagDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.sf.jsqlparser.parser.feature.Feature.limit;

/**
 * 热门标签缓存
 * @author czc
 * @date 2025/11/9
 */
@Component
public class HotTagCache {
    /** 缓存 List<TagDTO>，1h 过期 */
    private final Cache<String, ListObjCache> cache =
            Caffeine.newBuilder()
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .maximumSize(10_000)
                    .build();

    /* -------------------- 对外 API -------------------- */
    private String buildKey(int limit) {
        return "article:hotTag:"+limit;
    }

    /** 写缓存 */
    public void put(List<TagDTO> TagDTOList,int limit) {
        cache.put(buildKey(limit),new ListObjCache(TagDTOList));
    }

    /** 读缓存 */
    public List<TagDTO> get(int limit) {
        ListObjCache content = cache.getIfPresent(buildKey(limit));
        return content==null?null:content.getTagDTOList();
    }

    /** 主动失效 */
    public void evict(int limit) {
        cache.invalidate(buildKey(limit));
    }

    /**
     * 内部类辅助存储
     */
    @Data
    @AllArgsConstructor
    private class ListObjCache{
        private List<TagDTO> TagDTOList;
    }
}