package com.ccblog.enumeration.chat;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型
 * @author czc
 * @date 2025/10/24
 */
@Getter
public enum MessageTypeEnum {
    TEXT(0, "文本"),
    IMAGE(1, "图片"),
    FILE(2, "文件"),
    VOICE(3, "语音"),
    VIDEO(4, "视频");

    public static final String COL_TEXT = "text";
    public static final String COL_IMAGE = "image";
    public static final String COL_FILE = "file";
    public static final String COL_VOICE = "voice";
    public static final String COL_VIDEO = "video";

    public static final String[] COLS = {COL_TEXT, COL_IMAGE, COL_FILE, COL_VOICE, COL_VIDEO};



    private int type;
    private String msg;

    private static Map<Integer, MessageTypeEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (MessageTypeEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    MessageTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static MessageTypeEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static MessageTypeEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }


}