package com.ccblog.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 将List<entity>按照给定顺序indexList重新排列
 * @author czc
 * @date 2025-09-28
 */
public final class RecorderUtil {
    /**
     * 按索引列表重排实体列表
     * @param indexList 目标顺序的 id 列表
     * @param dbList    数据库查到的实体列表
     * @param idGetter  实体取 id 的函数（返回 Long）
     * @param <T>       实体类型
     * @return 与 indexList 顺序一一对应的实体列表
     */
    public static <T> List<T> reorder(List<Long> indexList,
                                      List<T> dbList,
                                      Function<T, Long> idGetter) {
        Map<Long, T> map = dbList.stream()
                .collect(Collectors.toMap(idGetter, Function.identity()));
        return indexList.stream()
                .map(map::get)
                .collect(Collectors.toList());
    }
}