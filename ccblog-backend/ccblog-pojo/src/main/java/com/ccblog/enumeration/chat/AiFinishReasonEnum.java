package com.ccblog.enumeration.chat;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * AI结束类型
 * @author czc
 * @date 2025/10/29
 */
@Getter
public enum AiFinishReasonEnum {
    NORMAL(0, "正常"), // 对应于表中ai_type里面的用户(方便判断)
    USER(1, "用户终止"),
    OTHER(2, "其他原因终止")
    ;

    private int type;
    private String msg;

    private static Map<Integer, AiFinishReasonEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (AiFinishReasonEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    AiFinishReasonEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static AiFinishReasonEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static AiFinishReasonEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }
}