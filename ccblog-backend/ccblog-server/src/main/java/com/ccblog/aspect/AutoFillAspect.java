package com.ccblog.aspect;

import com.ccblog.annotation.AutoFill;
import com.ccblog.entity.TimeSupport;
import com.ccblog.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 自定义切面，用于实现公共字段自动填充
 */
@Aspect // 表示切面
@Component
@Slf4j
public class AutoFillAspect {
    // 注意切点里面的内容要根据项目修改,这里因为我在mapper下面有子文件夹,所以要注意递归扫描
    @Pointcut("execution(* com.ccblog.mapper..*(..)) && @annotation(com.ccblog.annotation.AutoFill)")
    //注解的含义是所有在com.ccblog.mapper下面的所有 被AutoFill注解的 所有函数都被拦截(pointcut就是切点)
    public void autoFillPointCut(){}

    
    @Before("autoFillPointCut()") // 拦截到的函数,在执行函数前执行下面语句(在执行sql语句之前执行下面代码对相关字段赋值)
    public void autoFill(JoinPoint joinPoint){
//        log.info("开始进行公共字段填充...");
        // 获取到当前被拦截的方法上的数据库操作类型(insert or update or ?)
        // 这里的MethodSignature是Signature的子类(ctrl+h可以看类的子类),这里运用了多态
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value(); // 获取到拦截到的操作类型,下面要分类讨论

        // 获取到当前被拦截的方法的参数---实体对象(employee or category or ?)
        Object[] args = joinPoint.getArgs();
        if(args==null || args.length==0){
            return;
        }
        Object entity = args[0]; // 这里约定了执行函数的第一个参数就是需要的实体(比如employee或者category)

        // 准备赋值的数据(后面赋值需要的数据)
        LocalDateTime now = LocalDateTime.now();
        TimeSupport t = (TimeSupport) entity;

        // 根据不同的操作类型,为相应的属性通过反射来赋值
        if(operationType==OperationType.INSERT){
            t.setCreateTime(now);
            t.setUpdateTime(now);
        }
        else if(operationType==operationType.UPDATE){
            t.setUpdateTime(now);
        }
    }



}
