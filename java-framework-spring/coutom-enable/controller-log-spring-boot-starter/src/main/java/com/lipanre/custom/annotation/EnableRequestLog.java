package com.lipanre.custom.annotation;

import com.lipanre.custom.config.AnnotationLogConfig;
import com.lipanre.custom.registrar.RequestLogRegistrar;
import com.lipanre.custom.selector.RequestLogSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启记录日志
 *
 * @author lipanre
 * @date 2023/6/7 22:33
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({AnnotationLogConfig.class, RequestLogSelector.class, RequestLogRegistrar.class})
public @interface EnableRequestLog {
}
