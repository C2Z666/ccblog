package com.ccblog.enumeration.chat;

import com.ccblog.enumeration.YesOrNoEnum;
import com.ccblog.enumeration.article.ArticleCollectStatusEnum;
import com.ccblog.enumeration.article.ArticleLikeStatusEnum;
import lombok.Getter;

/**
 * 消息操作类型
 * @author czc
 * @since 2025-10-25
 */
@Getter
public enum MessageOperateTypeEnum {

    EMPTY(0, "") {
        @Override
        public int getDbStatCode() {
            return 0;
        }
    },
    DELETE(1, "删除") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    WITHDRAW(2, "撤回") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.NO.getCode();
        }
    },
    ;

    MessageOperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static MessageOperateTypeEnum fromCode(Integer code) {
        for (MessageOperateTypeEnum value : MessageOperateTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return MessageOperateTypeEnum.EMPTY;
    }

    public abstract int getDbStatCode();
}
