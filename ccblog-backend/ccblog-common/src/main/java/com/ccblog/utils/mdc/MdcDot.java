package com.ccblog.utils.mdc;

import java.lang.annotation.*;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MdcDot {
    String bizCode() default "";
}
