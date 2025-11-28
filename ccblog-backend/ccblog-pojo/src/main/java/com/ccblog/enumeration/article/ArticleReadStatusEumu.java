package com.ccblog.enumeration.article;

import lombok.Getter;

/**
 * 阅读状态枚举
 *
 * @author XuYifei
 * @since 2024-07-12
 */
@Getter
public enum ArticleReadStatusEumu {

    NOT_READ(0, "未读"),
    READ(1, "已读");

    ArticleReadStatusEumu(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ArticleReadStatusEumu formCode(Integer code) {
        for (ArticleReadStatusEumu value : ArticleReadStatusEumu.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleReadStatusEumu.NOT_READ;
    }
}
