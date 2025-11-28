package com.ccblog.enumeration.article;

import lombok.Getter;

/**
 * 收藏状态枚举
 *
 * @author XuYifei
 * @since 2024-07-12
 */
@Getter
public enum ArticleCollectStatusEnum {

    NOT_COLLECT(0, "未收藏"),
    COLLECT(1, "已收藏"),
    CANCEL_COLLECT(2, "取消收藏");

    ArticleCollectStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ArticleCollectStatusEnum formCode(Integer code) {
        for (ArticleCollectStatusEnum value : ArticleCollectStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleCollectStatusEnum.NOT_COLLECT;
    }
}
