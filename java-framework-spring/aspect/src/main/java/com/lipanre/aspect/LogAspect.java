package com.lipanre.aspect;

import com.lipanre.annotation.LogTrack;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志切面类
 *
 * @author lipanre
 * @date 2023/6/14 22:53
 */

@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(com.lipanre.annotation.LogTrack)")
    public void logTracK() {}

    @Before("logTracK() && @annotation(logTrack)")
    public void mdcInit(LogTrack logTrack) {
        System.out.println("mdcinit method invoked");
    }

}
