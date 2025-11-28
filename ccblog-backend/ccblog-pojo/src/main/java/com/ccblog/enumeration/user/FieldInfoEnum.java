package com.ccblog.enumeration.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户常见信息域(用于redis请求)
 */
@Getter
@AllArgsConstructor
public enum FieldInfoEnum {
    USER_NAME("user_name"),
    PHOTO    ("photo"),
    FAN  ("fan")
    ;

    public static final String COL_PHOTO    = "photo";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_FAN = "fan";

    public static final String[] COLS = {COL_PHOTO,COL_USER_NAME,COL_FAN};

    private final String column;

    /** 类名直接取列名 */
    public static String val(String enumName) {
        return valueOf(enumName).column;
    }
}