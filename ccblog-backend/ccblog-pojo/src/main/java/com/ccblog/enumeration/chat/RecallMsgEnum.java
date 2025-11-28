package com.ccblog.enumeration.chat;

import lombok.Getter;

/**
 * 消息操作类型
 * @author czc
 * @since 2025-10-25
 */
@Getter
public enum RecallMsgEnum {

    NORMAL(0, "正常") {
        @Override
        public int getDbStatCode() {
            return 0;
        }
    },
    USER_WITHDRAW(1, "用户撤回") {
        @Override
        public int getDbStatCode() {
            return 1;
        }
    },
    SYSTEM_WITHDRAW(2, "系统撤回") {
        @Override
        public int getDbStatCode() {
            return 2;
        }
    },
    ;

    /** 消息相关 */
    public static final String COL_SELF_RECALL = "你撤回了一条信息";
    public static final String COL_PEER_RECALL = "对方撤回了一条信息";
    public static final String COL_SYSTEM_RECALL = "系统撤回了一条信息";

    RecallMsgEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static RecallMsgEnum fromCode(Integer code) {
        for (RecallMsgEnum value : RecallMsgEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return RecallMsgEnum.NORMAL;
    }

    public abstract int getDbStatCode();
}
