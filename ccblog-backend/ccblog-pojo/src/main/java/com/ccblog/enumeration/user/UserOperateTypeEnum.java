package com.ccblog.enumeration.user;

import com.ccblog.enumeration.YesOrNoEnum;
import com.ccblog.enumeration.article.ArticleCollectStatusEnum;
import com.ccblog.enumeration.article.ArticleLikeStatusEnum;
import lombok.Getter;

/**
 * 用户相关操作类型
 * 被点赞表示写的文章被点赞,其他类似
 * @author czc
 * @since 2025-10-30
 */
@Getter
public enum UserOperateTypeEnum {

    EMPTY(0, "") {
        @Override
        public int getDbStatCode() {
            return 0;
        }
    },
    LIKE(2, "被点赞") {
        @Override
        public int getDbStatCode() {
            return ArticleLikeStatusEnum.LIKE.getCode();
        }
    },
    COLLECT(3, "被收藏") {
        @Override
        public int getDbStatCode() {
            return ArticleCollectStatusEnum.COLLECT.getCode();
        }
    },
    CANCEL_LIKE(4, "被取消点赞") {
        @Override
        public int getDbStatCode() {
            return ArticleLikeStatusEnum.CANCEL_LIKE.getCode();
        }
    },
    CANCEL_COLLECT(5, "被取消收藏") {
        @Override
        public int getDbStatCode() {
            return ArticleCollectStatusEnum.CANCEL_COLLECT.getCode();
        }
    },
    COMMENT(6,"被评论"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    READ(7,"被阅读"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    REPORT(8, "被举报") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    FAN(9,"被关注"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    CANCEL_FAN(10,"被取消关注"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    FOLLOW(11,"关注"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    CANCEL_FOLLOW(12,"取消关注"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    NEW(13,"新增"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    ;

    UserOperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static UserOperateTypeEnum fromCode(Integer code) {
        for (UserOperateTypeEnum value : UserOperateTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return UserOperateTypeEnum.EMPTY;
    }

    public abstract int getDbStatCode();
}
