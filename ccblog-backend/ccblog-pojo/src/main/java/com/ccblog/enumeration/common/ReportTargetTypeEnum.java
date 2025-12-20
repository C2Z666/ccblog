package com.ccblog.enumeration.common;

import com.ccblog.enumeration.comment.CommentDislikeStatusEnum;
import lombok.Getter;

/**
 * 举报对象类型枚举
 * @author czc
 * @date 2025/12/20
 */
@Getter
public enum ReportTargetTypeEnum {
    ARTICLE(1, "文章"),
    COMMENT(2, "评论"),
    USER(4, "用户");

    ReportTargetTypeEnum(Integer code, String desc) {
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