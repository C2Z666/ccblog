package com.ccblog.enumeration.article;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户对文章的操作域(主要用于redis的查询)
 * @author czc
 * @date 2025-10-20
 */
@Getter
public enum ArticleOperateFieldEnum {
    LIKE(1, "点赞"),
    COLLECT(2, "收藏"),
    COMMENT(3, "评论"),
    READ(3, "阅读"),
    ;

    public static final String COL_LIKE = "like";
    public static final String COL_COLLECT = "collect";
    public static final String COL_COMMENT = "comment";
    public static final String COL_READ = "read";

    public static final String[] COLS = {COL_LIKE, COL_COLLECT,COL_COMMENT,COL_READ};

    private int type;
    private String msg;

    private static Map<Integer, ArticleOperateFieldEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (ArticleOperateFieldEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    ArticleOperateFieldEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static ArticleOperateFieldEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static ArticleOperateFieldEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }
}
