package com.ccblog.concurrentCache;

import com.ccblog.entity.article.Category;
import com.ccblog.mapper.article.CatgeoryMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 缓存Category id-name映射
 * @author czc
 * @date 2025/11/2
 */
@Component
public class CategoryCache {
    @Autowired
    private CatgeoryMapper catgeoryMapper;

    // 本地只读缓存
    private static final ConcurrentHashMap<Long, Category> CACHE = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // 查全表
        List<Category> list = catgeoryMapper.listAll();
        // 直接 putAll
         CACHE.putAll(list.stream().collect(Collectors.toMap(Category::getId, Function.identity())));
    }

    /**
     * 获得单个category名称
     */
    public Category getCategory(Long id) {
        return CACHE.get(id);
    }

    /**
     * 批量获得映射
     * @return
     */
    public Map<Long,Category> getCategoryBatch(Set<Long> ids){
        return ids.stream()
                .collect(Collectors.toMap(Function.identity(),
                        CACHE::get,
                        (a, b) -> b,
                        LinkedHashMap::new));
    }

    /**
     * 返回全部 Category 的只读 List
     */
    public List<Category> getAllCategoryList() {
        return List.copyOf(CACHE.values());
    }
}