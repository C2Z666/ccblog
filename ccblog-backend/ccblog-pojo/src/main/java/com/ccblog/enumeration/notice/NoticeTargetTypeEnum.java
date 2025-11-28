package com.ccblog.enumeration.notice;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 记录目标类型
 * @author czc
 * @date 2025-10-20
 */
@Getter
public enum NoticeTargetTypeEnum {
    ARTICLE(1, "文章"),
    COMMENT(2, "评论"),
    COLUMN(3, "专栏"),
    USER(4, "用户"),
    PRIVATE(5,"私信");

    public static final String COL_USER = "user";
    public static final String COL_ARTICLE = "article";
    public static final String COL_COMMENT = "comment";
    public static final String COL_COLUMN = "column";
    public static final String COL_PRIVATE = "private";

    private int type;
    private String msg;

    private static Map<Integer, NoticeTargetTypeEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (NoticeTargetTypeEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    NoticeTargetTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static NoticeTargetTypeEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static NoticeTargetTypeEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }
}
