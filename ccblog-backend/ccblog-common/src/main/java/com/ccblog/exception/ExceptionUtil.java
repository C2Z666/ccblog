package com.ccblog.exception;

import com.ccblog.enumeration.StatusEnum;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
public class ExceptionUtil {

    public static ForumAdviceException of(StatusEnum status, Object... args) {
        return new ForumAdviceException(status, args);
    }

}
