package com.ccblog.enumeration.user;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户对文章的操作域(主要用于redis的查询)
 * @author czc
 * @date 2025-10-20
 */
@Getter
public enum UserOperateFieldEnum {
    LIKE(1, "点赞"),
    COLLECT(2, "收藏"),
    COMMENT(3, "评论"),
    READ(4, "阅读"),
    FAN(5, "粉丝"),
    FOLLOW(6, "关注"),
    ;

    public static final String COL_LIKE = "like";
    public static final String COL_COLLECT = "collect";
    public static final String COL_COMMENT = "comment";
    public static final String COL_READ = "read";
    public static final String COL_FAN = "fan";
    public static final String COL_FOLLOW = "follow";

    public static final String[] COLS = {COL_LIKE, COL_COLLECT,COL_COMMENT,COL_READ,COL_FAN,COL_FOLLOW};

    private int type;
    private String msg;

    private static Map<Integer, UserOperateFieldEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (UserOperateFieldEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    UserOperateFieldEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static UserOperateFieldEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static UserOperateFieldEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }
}
