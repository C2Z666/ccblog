package com.ccblog.localCache.chat;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket 一次性 ticket 缓存
 * 30 秒过期，集群场景可替换为 Redis
 *
 * @author czc
 * @date 2025/10/24
 */
@Component
public class TicketCache {

    private final Cache<String, String> cache =
            Caffeine.newBuilder()
                    .maximumSize(100_000)
                    .expireAfter(new Expiry<String, String>() {
                        // 初次写入：30 s 后过期
                        @Override
                        public long expireAfterCreate(String key, String value, long now) {
                            return TimeUnit.SECONDS.toNanos(30);
                        }

                        // 更新：同样 30 s
                        @Override
                        public long expireAfterUpdate(String key, String value, long now, long cur) {
                            return TimeUnit.SECONDS.toNanos(30);
                        }

                        // 每次读：续期到 180 s（从当前时间重新算）
                        @Override
                        public long expireAfterRead(String key, String value, long now, long cur) {
                            long newDur = TimeUnit.SECONDS.toNanos(180);
                            // 如果当前剩余时间已经 > 180 s，则保持；否则续到 180 s
                            return Math.max(cur, newDur);
                        }
                    })
                    .build();

    /* -------------------- 对外 API -------------------- */

    private String buildKey(String ticket) {
        return "ws:ticket:" + ticket;
    }

    /** 放入映射 */
    public void put(String ticket, String userId) {
        cache.put(buildKey(ticket), userId);
    }

    /** 读取 userId，不存在返回 null */
    public String get(String ticket) {
        return cache.getIfPresent(buildKey(ticket));
    }

    /** 主动失效（踢人） */
    public void evict(String ticket) {
        cache.invalidate(buildKey(ticket));
    }

    /** 清空所有 ticket（管理接口） */
    public void clearAll() {
        cache.invalidateAll();
    }
}