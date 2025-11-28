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
public enum AiSessionFieldEnum {
    ;

    public static final String COL_LAST_TIME   = "lastTime";   // 最后一条消息发送时间（LocalDateTime.toString()）
    public static final String COL_SEQ         = "seq";        // 最新seq

    public static final String[] COLS = {COL_LAST_TIME, COL_SEQ};



    private int type;
    private String msg;

    private static Map<Integer, AiSessionFieldEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (AiSessionFieldEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    AiSessionFieldEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static AiSessionFieldEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static AiSessionFieldEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }


}