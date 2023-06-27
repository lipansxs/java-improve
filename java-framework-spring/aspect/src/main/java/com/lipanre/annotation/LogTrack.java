package com.lipanre.annotation;

import java.lang.annotation.*;

/**
 * 日志记录注解
 *
 * @author lipanre
 * @date 2023/6/14 22:54
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogTrack {
}
