package com.ccblog.annotation;

import com.ccblog.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解,用于标识某个方法需要进行公共字段自动填充
 * 实际上没什么用,就是应用一下学过的知识
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill { // 这是定义一个注解
    // 数据库操作类型:update,insert,这两种需要添加自动注解
    // Opeartion实际上是个枚举类,这里把枚举类纳入对象是为了在给函数添加注解的时候可以获取里面的值
    // 比如@AutoFill(value=Operation.INSERT)就是获得里面的insert
    OperationType value();
}
