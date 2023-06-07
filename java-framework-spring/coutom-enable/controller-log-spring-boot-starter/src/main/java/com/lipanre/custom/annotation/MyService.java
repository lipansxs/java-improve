package com.lipanre.custom.annotation;

import java.lang.annotation.*;

/**
 * @author lipanre
 * @date 2023/6/7 23:48
 */

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MyService {
}
