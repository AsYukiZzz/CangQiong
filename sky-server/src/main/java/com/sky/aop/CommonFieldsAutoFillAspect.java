package com.sky.aop;

import com.sky.anno.Autofill;
import com.sky.context.CurrentHolderInfo;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Component
@Aspect
public class CommonFieldsAutoFillAspect {

    @Before("@annotation(com.sky.anno.Autofill)")
    public void AutoFill(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {

        //获取被拦截的Mapper层方法签名对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("开始公共字段自动填充，{}", method.getName());

        //通过MethodSignature获取Method对象，并通过该Method对象获取标注在该Method对象的注解的value值
        OperationType type = method.getAnnotation(Autofill.class).value();

        //获取被拦截的Mapper方法的实际参数（对象）
        Object arg = joinPoint.getArgs()[0];
        Class<?> argClass = joinPoint.getArgs()[0].getClass();

        //若要进行改动，请注意此处的switch穿透问题
        switch (type) {
            case INSERT:
                //获取要自动填充的字段对象
                Field createTime = argClass.getDeclaredField("createTime");
                Field createUser = argClass.getDeclaredField("createUser");
                //临时赋予私有字段操作权限
                createTime.setAccessible(true);
                createUser.setAccessible(true);
                //对字段进行赋值
                createTime.set(arg, LocalDateTime.now());
                createUser.set(arg, CurrentHolderInfo.getCurrentHolder());
            case UPDATE:
                //获取要进行自动填充的字段对象
                Field updateTime = argClass.getDeclaredField("updateTime");
                Field updateUser = argClass.getDeclaredField("updateUser");
                //临时赋予私有字段操作权限
                updateTime.setAccessible(true);
                updateUser.setAccessible(true);
                //对字段进行赋值
                updateTime.set(arg, LocalDateTime.now());
                updateUser.set(arg, CurrentHolderInfo.getCurrentHolder());
                break;
        }
    }
}
