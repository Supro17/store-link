package com.ojbk.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeAspect {

    @Around("execution(* com.ojbk.controller..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime= System.currentTimeMillis();
        Object result;

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();


        try {
            result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            log.info("正常执行：{}.{} 参数：{}，耗时：{}ms",className,methodName,args,(endTime - startTime));
            return result;
        } catch (Throwable e) {

            long cost=System.currentTimeMillis() - startTime;

            log.error("执行异常：{}.{},耗时：{}ms",className,methodName,cost,e);

            throw e;
        }

    }

}
