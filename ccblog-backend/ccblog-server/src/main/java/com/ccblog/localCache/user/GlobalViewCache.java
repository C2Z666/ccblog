package com.ccblog.localCache.user;

import com.ccblog.vo.global.GlobalVo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 全局视图缓存  这个缓存必要性有待商榷,不过优化几次redis应该也还好,redis压力很大
 * 只缓存必要的
 * @author czc
 * @date 2025/11/6
 */
@Component
public class GlobalViewCache {
    /** 缓存 GlobalVo，30 秒过期 */
    private final Cache<String, GlobalVo> cache =
            Caffeine.newBuilder()
                    .expireAfterWrite(20, TimeUnit.SECONDS)
                    .maximumSize(10_000)
                    .build();

    private String buildKey(Long userId) {
        return "global:view:" + userId;
    }

    /* -------------------- 对外 API -------------------- */

    /** 读缓存 */
    public GlobalVo get(Long userId) {
        return cache.getIfPresent(buildKey(userId));
    }


    /** 写缓存 */
    public void put(Long userId, GlobalVo globalVo) {
        cache.put(buildKey(userId),globalVo);
    }



    /** 主动失效 */
    public void evict(Long userId) {
        cache.invalidate(buildKey(userId));
    }

}