package com.ccblog.enumeration.comment;

import com.ccblog.enumeration.YesOrNoEnum;
import com.ccblog.enumeration.article.ArticleCollectStatusEnum;
import com.ccblog.enumeration.article.ArticleLikeStatusEnum;
import lombok.Getter;

/**
 * 评论操作类型
 *
 * @author czc
 * @since 2025-10-04
 */
@Getter
public enum CommentOperateTypeEnum {

    EMPTY(0, "") {
        @Override
        public int getDbStatCode() {
            return 0;
        }
    },
    LIKE(2, "点赞") {
        @Override
        public int getDbStatCode() {
            return ArticleLikeStatusEnum.LIKE.getCode();
        }
    },
    DISLIKE(3, "点踩") {
        @Override
        public int getDbStatCode() {
            return ArticleCollectStatusEnum.COLLECT.getCode();
        }
    },
    CANCEL_LIKE(4, "取消点赞") {
        @Override
        public int getDbStatCode() {
            return ArticleLikeStatusEnum.CANCEL_LIKE.getCode();
        }
    },
    CANCEL_DISLIKE(5, "取消点踩") {
        @Override
        public int getDbStatCode() {
            return ArticleCollectStatusEnum.CANCEL_COLLECT.getCode();
        }
    },
    REPORT(6, "举报") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    DELETE(7, "删除") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    NEW(8, "新增") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    REPLY(9, "回复") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    ;

    CommentOperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static CommentOperateTypeEnum fromCode(Integer code) {
        for (CommentOperateTypeEnum value : CommentOperateTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return CommentOperateTypeEnum.EMPTY;
    }

    public abstract int getDbStatCode();
}
