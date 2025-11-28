package com.ccblog.localCache.user;

import com.ccblog.vo.user.UserBaseInfoVo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/** 用户信息缓存
 * 文章详情模块缓存
 * @author czc
 * @date 2025/10/18
 */
@Component
public class UserInfoCache {
    /** 缓存 UserBaseInfoVo，30 秒过期 */
    private final Cache<String, UserBaseInfoVo> cache =
            Caffeine.newBuilder()
                    .expireAfterWrite(30, TimeUnit.SECONDS)
                    .expireAfterAccess(30, TimeUnit.SECONDS) // 每次读续期
                    .maximumSize(10_000)
                    .build();

    /* -------------------- 对外 API -------------------- */
    private String buildKey(Long userId) {
        return "user:info:" + userId;
    }

    /** 写缓存 */
    public void put(Long userId, UserBaseInfoVo userBaseInfoVo) {
        cache.put(buildKey(userId),userBaseInfoVo);
    }

    /** 读缓存 */
    public UserBaseInfoVo get(Long userId) {
        return cache.getIfPresent(buildKey(userId));
    }

    /** 主动失效 */
    public void evict(Long userId) {
        cache.invalidate(buildKey(userId));
    }

}