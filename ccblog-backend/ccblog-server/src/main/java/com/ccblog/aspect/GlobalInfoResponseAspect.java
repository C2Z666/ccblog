package com.ccblog.aspect;

import com.ccblog.service.GlobalInitService;
import com.ccblog.vo.global.ResultVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @program: tech-pai
 * @description: 用于拦截界面并且给全局参数赋值
 * @author: XuYifei
 * @create: 2024-06-26
 */

@Aspect
@Component
@Slf4j
public class GlobalInfoResponseAspect {

    @Resource
    private GlobalInitService globalInitService;

    @Pointcut("execution(public com.ccblog.vo.global.ResultVo com.ccblog.controller..*.*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object modifyGlobalResponse(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("执行切面方法赋值全局变量");
//        log.info("当前全局变量情况:\n:{}",globalInitService.globalAttr());
        Object result = joinPoint.proceed(); // 继续执行原方法

        if (result instanceof ResultVo) {
            ((ResultVo<?>) result).setGlobal(globalInitService.globalAttr());
        }

        return result;
    }
}
