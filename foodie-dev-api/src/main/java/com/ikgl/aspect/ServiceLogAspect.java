package com.ikgl.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ServiceLogAspect {

    public static final Logger log = LoggerFactory.getLogger(ServiceLogAspect.class);
    /**
     * AOP通知：
     * 1.前置通知：在方法调用之前执行
     * 2.后置通知：在方法正常调用之后执行
     * 3.环绕通知：在方法调用之前和之后，都可以分别执行的通知
     * 4.异常通知：如果在方法调用过程中发生异常，则会通知
     * 5.最终通知：在方法调用之后执行
     */

    /**
     * 切面表达式
     * execution 所要执行的表达式主体
     * 第一处 * 代表返回类型 *代表所有类型
     * 第二处 包名 代表aop监控的类所在的包
     * 第三处 .. 代表该包和其子包下所有类方法
     * 第四处 * 代表类名 *代表所有类
     * 第五处 *(..) *代表类中的方法名 （..）表示方法中的任何参数
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.ikgl.service.impl..*.*(..))")
    public Object readTimeLog(ProceedingJoinPoint point) throws Throwable {
        log.info("===== 开始执行{}.{} =====",point.getTarget().getClass(),point.getSignature().getName());

        Long begin = System.currentTimeMillis();

        Object result = point.proceed();

        Long end = System.currentTimeMillis();
        Long takeTime = end - begin;

        if(takeTime > 3000){
            log.error("===== 执行结束 耗时:{} 毫秒=====",takeTime);
        }else if(takeTime > 2500){
            log.warn("===== 执行结束 耗时:{} 毫秒=====",takeTime);
        }else{
            log.info("===== 执行结束 耗时:{} 毫秒=====",takeTime);
        }
        return result;
    }
}
