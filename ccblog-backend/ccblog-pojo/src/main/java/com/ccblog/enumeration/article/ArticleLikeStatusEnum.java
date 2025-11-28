package com.ccblog.enumeration.article;

import lombok.Getter;

/**
 * 点赞状态枚举
 *
 * @author XuYifei
 * @since 2024-07-12
 */
@Getter
public enum ArticleLikeStatusEnum {

    NOT_LIKE(0, "未点赞"),
    LIKE(1, "已点赞"),
    CANCEL_LIKE(2, "取消点赞");

    ArticleLikeStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ArticleLikeStatusEnum formCode(Integer code) {
        for (ArticleLikeStatusEnum value : ArticleLikeStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleLikeStatusEnum.NOT_LIKE;
    }
}
