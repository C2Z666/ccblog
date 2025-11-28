package com.ccblog.enumeration.article;

import com.ccblog.enumeration.YesOrNoEnum;
import lombok.Getter;

/**
 * 操作类型
 * @author czc
 * @since 2025-10-30
 */
@Getter
public enum ArticleOperateTypeEnum {

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
    COLLECT(3, "收藏") {
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
    CANCEL_COLLECT(5, "取消收藏") {
        @Override
        public int getDbStatCode() {
            return ArticleCollectStatusEnum.CANCEL_COLLECT.getCode();
        }
    },
    COMMENT(6,"评论"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    READ(7,"阅读"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    REPORT(8, "举报") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    DELETE(9,"删除"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    NEW(10,"新增文章"){
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    ;

    ArticleOperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ArticleOperateTypeEnum fromCode(Integer code) {
        for (ArticleOperateTypeEnum value : ArticleOperateTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleOperateTypeEnum.EMPTY;
    }

    public abstract int getDbStatCode();
}
