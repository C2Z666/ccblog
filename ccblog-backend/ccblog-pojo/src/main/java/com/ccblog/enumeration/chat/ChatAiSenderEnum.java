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
public enum ChatAiSenderEnum {
    USER(0, "用户"),
    AI(1, "AI");


    private int type;
    private String msg;

    private static Map<Integer, ChatAiSenderEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (ChatAiSenderEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    ChatAiSenderEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static ChatAiSenderEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static ChatAiSenderEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }


}