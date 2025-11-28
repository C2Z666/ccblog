package com.ccblog.enumeration.chat;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户会话redis存储域
 * @author czc
 * @date 2025/10/24
 */
@Getter
public enum UserSessionFieldEnum {
    ;

    public static final String COL_SNAPSHOT    = "snapshot";   // 最新消息快照（前50字）
    public static final String COL_LAST_TIME   = "lastTime";   // 最后一条消息发送时间（LocalDateTime.toString()）
    public static final String COL_UNREAD_CNT  = "unread";     // 未读消息数量
    public static final String COL_DISPLAY_SEQ = "displaySeq"; // 当前最后显示seq
    public static final String COL_SEQ         = "seq";        // 最新seq

    /* 所有字段数组，方便批量操作 */
    public static final String[] COLS = {COL_SNAPSHOT, COL_LAST_TIME, COL_UNREAD_CNT,COL_DISPLAY_SEQ,COL_SEQ};



    private int type;
    private String msg;

    private static Map<Integer, UserSessionFieldEnum> mapper;

    static {
        mapper = new HashMap<>();
        for (UserSessionFieldEnum type : values()) {
            mapper.put(type.type, type);
        }
    }

    UserSessionFieldEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public static UserSessionFieldEnum typeOf(int type) {
        return mapper.get(type);
    }

    public static UserSessionFieldEnum typeOf(String type) {
        return valueOf(type.toUpperCase().trim());
    }


}