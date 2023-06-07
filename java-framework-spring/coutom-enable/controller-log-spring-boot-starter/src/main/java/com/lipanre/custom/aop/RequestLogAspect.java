package com.lipanre.custom.aop;

import cn.hutool.json.JSONUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * controller日志记录切面类
 *
 * @author lipanre
 * @date 2023/6/7 22:36
 */

@Aspect
public class RequestLogAspect {

    /**
     * 拦截所有被RestContrller注解标柱的类
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerPointCut() {
    }

    @Before("controllerPointCut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("请求参数：" + JSONUtil.toJsonStr(joinPoint.getArgs()));
        System.out.println("before方法被执行了…………");
    }

}
