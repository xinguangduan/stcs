package org.stcs.server.annotation;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class TakeTimeAspect {
    // 统计请求的处理时间
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    ThreadLocal<Long> endTime = new ThreadLocal<>();

    /**
     * 带有@TakeTime注解的方法
     */
//    @Pointcut("within(com.lwx.backend.user.controller.*)")
//    @Pointcut("execution(* com.lwx.backend.user.controller.*.*(..))")
    @Pointcut("@annotation(org.stcs.server.annotation.TakeTime)")
    public void TakeTime() {

    }

    //    @Before("within(com.lwx.backend.user.controller.*)")
    @Before("TakeTime()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 获取方法的名称
        String methodName = joinPoint.getSignature().getName();
        // 获取方法入参
//        Object[] param = joinPoint.getArgs();
//        StringBuilder sb = new StringBuilder();
//        for (Object o : param) {
//            sb.append(o + ";");
//        }
//        log.info("进入[{}]方法, 参数为: {}", methodName, sb);
        startTime.set(System.currentTimeMillis());
        //接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //记录请求的内容
        log.info("access [{}] method, url:{}, METHOD: {}", methodName, request.getRequestURL().toString(), request.getMethod());
    }

    //    @AfterReturning(returning = "ret", pointcut = "within(com.lwx.backend.user.controller.*)")
    @AfterReturning(returning = "ret", pointcut = "TakeTime()")
    public void doAfterReturning(Object ret) {
        //处理完请求后，返回内容
        endTime.set(System.currentTimeMillis());
        log.info("cost time(ms):{}, return:{}", (endTime.get() - startTime.get()), JSON.toJSONString(ret));
    }
}
