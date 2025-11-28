package com.ccblog.concurrentCache;

import com.ccblog.entity.article.Tag;
import com.ccblog.mapper.article.TagMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 缓存tag id-tag映射
 * 本来想用的但是没有用
 * @author czc
 * @date 2025/11/2
 */
@Component
public class TagCache {
    @Autowired
    private TagMapper tagMapper;

    // 本地只读缓存
    private static final ConcurrentHashMap<Long, Tag> CACHE = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // 查全表
        List<Tag> list = tagMapper.listAllTag();
        // 直接 putAll
         CACHE.putAll(list.stream().collect(Collectors.toMap(Tag::getId, Function.identity())));
    }

    /**
     * 获得单个tag名称
     */
    public Tag getTag(Long id) {
        return CACHE.get(id);
    }

    /**
     * 批量获得映射
     * @return
     */
    public Map<Long,Tag> getTagBatch(Set<Long> ids) {
        return ids.stream()
                .collect(Collectors.toMap(Function.identity(),
                        CACHE::get,
                        (a, b) -> b,
                        LinkedHashMap::new));
    }
}