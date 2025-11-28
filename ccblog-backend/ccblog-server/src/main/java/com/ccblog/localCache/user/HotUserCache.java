package com.ccblog.localCache.user;

import com.ccblog.vo.user.SimpleUserInfoVO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 热门用户缓存
 * @author czc
 * @date 2025/11/9
 */
@Component
public class HotUserCache {
    /** 缓存 List<SimpleUserInfoVO>，1h 过期 */
    private final Cache<String, ListObjCache> cache =
            Caffeine.newBuilder()
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .maximumSize(10_000)
                    .build();

    /* -------------------- 对外 API -------------------- */
    private String buildKey(int limit) {
        return "user:hotUser:"+limit;
    }

    /** 写缓存 */
    public void put(List<SimpleUserInfoVO> simpleUserInfoVOList,int limit) {
        cache.put(buildKey(limit),new ListObjCache(simpleUserInfoVOList));
    }

    /** 读缓存 */
    public List<SimpleUserInfoVO> get(int limit) {
        ListObjCache content = cache.getIfPresent(buildKey(limit));
        return content==null?null:content.getSimpleUserInfoVOList();
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
        private List<SimpleUserInfoVO> simpleUserInfoVOList;
    }
}