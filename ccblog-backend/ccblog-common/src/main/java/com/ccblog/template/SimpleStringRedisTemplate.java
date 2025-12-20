package com.ccblog.template;

import com.ccblog.utils.RedisUtil;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

/**
 * redis 简易string缓存读写模板
 * 模板只实现针对字符串存储且只需要读写和删除的
 * 不提供回源和批量查询操作
 * @author czc
 * @date 2025/12/20
 */
@Component
public abstract class SimpleStringRedisTemplate {

    private final long ttlMinutes;

    protected SimpleStringRedisTemplate() {
        this.ttlMinutes = 2*RedisUtil.TTL_MINUTE;
    }

    protected SimpleStringRedisTemplate(long ttlMinutes) {
        this.ttlMinutes = ttlMinutes;
    }

    /* key构造规则 */
    protected abstract String buildKey(Object... args);

    /* 删除key pattern 构造规则 */
    protected abstract String buildRemovePattern(Object... args);

    /**
     * 获取值
     * @param args
     * @return
     */
    @Nullable
    public String getContent(Object... args) {
        String key = buildKey(args);
        return RedisUtil.get(key);
    }

    /**
     * 设置值
     * @param content
     * @param args
     * @return
     */
    public boolean setContent(String content, Object... args) {
        String key = buildKey(args);
        try {
            RedisUtil.set(key, content, ttlMinutes * RedisUtil.TTL_MINUTE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 删除数据
     * @param args
     */
    public void removeContent(Object... args) {
        String pattern = buildRemovePattern(args);
        RedisUtil.deleteByPattern(pattern);
    }

}