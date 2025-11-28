package com.ccblog.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author:kimi(工具类模板基本都是k老师写的)
 * 字符串截断工具
 * 1. 按字节长度截断（UTF-8 下 1~4 字节/字符）
 * 2. 返回是否被截断 & 截断后字符串
 */
public final class TruncateUtil {

    /** 结果封装：截断后字符串 + 是否被截断 */
    @Getter
    @AllArgsConstructor
    public static class Result {
        private final String text;
        private final boolean truncated;
    }

    /** 默认最大字节长度（对应数据库 varchar(200)） */
    private static final int DEFAULT_MAX_BYTES = 200;

    /**
     * 按默认 200 字节截断
     */
    public static Result truncate(String input) {
        return truncate(input, DEFAULT_MAX_BYTES);
    }

    /**
     * 按指定字节长度截断
     * @param input   原始字符串
     * @param maxBytes 最大字节数
     * @return 截断结果
     */
    public static Result truncate(String input, int maxBytes) {
        if (input == null || input.isEmpty()) {
            return new Result("", false);
        }

        // UTF-8 字节数组
        byte[] bytes = input.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length <= maxBytes) {
            return new Result(input, false);
        }

        // 需要截断
        int cutPos = maxBytes;
        // 避免截断在 UTF-8 多字节中间
        while ((bytes[cutPos] & 0x80) != 0 && (bytes[cutPos] & 0xC0) != 0xC0) {
            cutPos--;
        }

        String truncated = new String(bytes, 0, cutPos, java.nio.charset.StandardCharsets.UTF_8);
        return new Result(truncated, true);
    }
}