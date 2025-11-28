package com.ccblog.localCache.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.vo.user.FollowUserInfoVO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 用户关注/粉丝用户信息本地缓存
 * 包括FAN/FOLLOW两种类型
 * @author czc
 * @date 2025/10/17
 */
@Component
public class UserRelationPageCache {
    /** 对外暴露的缓存类型 */
    public enum Type {
        FAN, FOLLOW;
        public String val() { return name(); }   // 返回 "FAN" / "FOLLOW"
    }

    /** 缓存 IPage<FollowUserInfoVO>，30 秒过期 */
    private final Cache<String, IPage<FollowUserInfoVO>> cache =
            Caffeine.newBuilder()
                    .expireAfterWrite(30, TimeUnit.SECONDS)
                    .expireAfterAccess(30, TimeUnit.SECONDS) // 每次读续期
                    .maximumSize(10_000)
                    .build();

    /* -------------------- 对外 API -------------------- */
    /**
     * 生成统一 key
     * type: fan/follow
     */
    private String buildKey(Long userId, String type, long current, long size) {
        return "user:relation:" + userId+":" +":"+ type+":" + current+":" + ":"+size;
    }

    /** 写缓存 */
    public void put(Long userId, String type, long current, long size, IPage<FollowUserInfoVO> page) {
        cache.put(buildKey(userId, type, current, size), page);
    }

    /** 读缓存 */
    public IPage<FollowUserInfoVO> get(Long userId, String type, long current, long size) {
        return cache.getIfPresent(buildKey(userId, type, current, size));
    }

    /** 主动失效 */
    public void evict(Long userId, String type, long current, long size) {
        cache.invalidate(buildKey(userId, type, current, size));
    }

    /** 清掉整个缓存（管理接口） */
    public void clearAll() {
        cache.invalidateAll();
    }
}