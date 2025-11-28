package com.ccblog.enumeration.comment;

import lombok.Getter;

/**
 * 点赞状态枚举
 *
 * @author XuYifei
 * @since 2024-07-12
 */
@Getter
public enum CommentLikeStatusEnum {

    NOT_LIKE(0, "未点赞"),
    LIKE(1, "已点赞"),
    CANCEL_LIKE(2, "取消点赞");

    CommentLikeStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static CommentLikeStatusEnum formCode(Integer code) {
        for (CommentLikeStatusEnum value : CommentLikeStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return CommentLikeStatusEnum.NOT_LIKE;
    }
}
