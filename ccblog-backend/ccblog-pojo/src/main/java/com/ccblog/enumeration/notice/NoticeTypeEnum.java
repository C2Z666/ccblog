package com.ccblog.enumeration.notice;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author czc
 * @date 2025-10-20
 */
@Getter
public enum NoticeTypeEnum {
    COMMENT(1, "评论"),
    REPLY(2, "回复"),
    LIKE(3, "点赞"),
    COLLECT(4, "收藏"),
    FOLLOW(5, "关注消息"),
    SYSTEM(6, "私信消息"),
    DELETE_COMMENT(1, "删除评论"),
    DELETE_REPLY(2, "删除回复"),
    CANCEL_PRAISE(3, "取消点赞"),
    CANCEL_COLLECT(4, "取消收藏"),
    CANCEL_FOLLOW(5, "取消关注");

    public static final String COL_COMMENT = "comment";
    public static final String COL_REPLY = "reply";
    public static final String COL_LIKE = "like";
    public static final String COL_COLLECT = "collect";
    public static final String COL_FOLLOW = "follow";
    public static final String COL_SYSTEM = "system";

    public static final String[] COLS = {COL_COMMENT,COL_REPLY,COL_LIKE,COL_COLLECT,COL_FOLLOW, COL_SYSTEM};




    private int type;
    private String msg;

    private static Map<Integer, NoticeTypeEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (NoticeTypeEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    NoticeTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static NoticeTypeEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static NoticeTypeEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }
}
