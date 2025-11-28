package com.ccblog.enumeration.article;

import lombok.Getter;

/**
 * 评论状态枚举
 *
 * @author XuYifei
 * @since 2024-07-12
 */
@Getter
public enum ArticleCommentStatusEnum {

    NOT_COMMENT(0, "未评论"),
    COMMENT(1, "已评论"),
    DELETE_COMMENT(2, "删除评论");

    ArticleCommentStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ArticleCommentStatusEnum formCode(Integer code) {
        for (ArticleCommentStatusEnum value : ArticleCommentStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleCommentStatusEnum.NOT_COMMENT;
    }
}
