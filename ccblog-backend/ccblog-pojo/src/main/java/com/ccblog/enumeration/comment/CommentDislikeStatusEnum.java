package com.ccblog.enumeration.comment;

import lombok.Getter;

/**
 * 收藏状态枚举
 *
 * @author XuYifei
 * @since 2024-07-12
 */
@Getter
public enum CommentDislikeStatusEnum {

    NOT_DISLIKE(0, "未点踩"),
    DISLIKE(1, "已点踩"),
    CANCEL_DISLIKE(2, "取消点踩");

    CommentDislikeStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static CommentDislikeStatusEnum formCode(Integer code) {
        for (CommentDislikeStatusEnum value : CommentDislikeStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return CommentDislikeStatusEnum.NOT_DISLIKE;
    }
}
