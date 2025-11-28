package com.ccblog.enumeration.chat;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 可调用大模型类型
 * @author czc
 * @date 2025/10/24
 */
@Getter
public enum AiTypeEnum {
    USER(0, "用户"), // 对应于表中ai_type里面的用户(方便判断)
    QWEN(1, "千问"),
    CHATGPT(2, "chatgpt"),
    DEEPSEEK(3, "deepseek"),
    ;

    public static final String COL_QWEN = "qwen";
    public static final String COL_CHATGPT = "chatgpt";
    public static final String COL_DEEPSEEK = "deepseek";

    public static final String[] COLS = {COL_QWEN, COL_CHATGPT,COL_DEEPSEEK};



    private int type;
    private String msg;

    private static Map<Integer, AiTypeEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (AiTypeEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    AiTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static AiTypeEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static AiTypeEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }


}