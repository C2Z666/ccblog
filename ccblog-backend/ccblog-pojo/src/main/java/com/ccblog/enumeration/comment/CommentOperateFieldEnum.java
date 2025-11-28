package com.ccblog.enumeration.comment;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户对评论的操作域(主要用于redis的查询)
 * @author czc
 * @date 2025-11-17
 */
@Getter
public enum CommentOperateFieldEnum {
    LIKE(1, "点赞"),
    DISLIKE(2, "点踩"),
    REPLY(3, "回复"),
    ;

    public static final String COL_LIKE = "like";
    public static final String COL_DISLIKE = "dislike";
    public static final String COL_REPLY = "reply";

    public static final String[] COLS = {COL_LIKE, COL_DISLIKE,COL_REPLY};

    private int type;
    private String msg;

    private static Map<Integer, CommentOperateFieldEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (CommentOperateFieldEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    CommentOperateFieldEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static CommentOperateFieldEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static CommentOperateFieldEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }
}
