package com.lipanre.custom.config;

import com.lipanre.custom.aop.RequestLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lipanre
 * @date 2023/6/7 22:58
 */

@EnableAspectJAutoProxy
@Configuration
@ConditionalOnProperty(value = "lipanre.annotation.log.enable", havingValue = "true")
public class AnnotationLogConfig {

    @Bean
    public RequestLogAspect requestLogAspect() {
        return new RequestLogAspect();
    }

}
