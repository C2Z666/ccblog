package com.ccblog.enumeration.chat;

import com.ccblog.enumeration.YesOrNoEnum;
import lombok.Getter;

/**
 * 消息操作类型
 * @author czc
 * @since 2025-11-05
 */
@Getter
public enum UserSessionOperateTypeEnum {

    EMPTY(0, "") {
        @Override
        public int getDbStatCode() {
            return 0;
        }
    },
    DELETE(1, "删除会话") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    TOP(2, "置顶会话") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    CANCEL_TOP(3, "取消置顶会话") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.NO.getCode();
        }
    },
    MUTE(4, "不再通知") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.YES.getCode();
        }
    },
    CANCEL_MUTE(5, "取消不再通知") {
        @Override
        public int getDbStatCode() {
            return YesOrNoEnum.NO.getCode();
        }
    },
    ;

    UserSessionOperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static UserSessionOperateTypeEnum fromCode(Integer code) {
        for (UserSessionOperateTypeEnum value : UserSessionOperateTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return UserSessionOperateTypeEnum.EMPTY;
    }

    public abstract int getDbStatCode();
}
