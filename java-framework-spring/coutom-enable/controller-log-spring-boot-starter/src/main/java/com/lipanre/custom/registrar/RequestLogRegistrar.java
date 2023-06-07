package com.lipanre.custom.registrar;

import com.lipanre.custom.annotation.MyService;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * @author lipanre
 * @date 2023/6/7 23:42
 */
public class RequestLogRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        for (String annotationType : importingClassMetadata.getAnnotationTypes()) {
            System.out.println("RequestLogRegistrar " + annotationType);
        }

        // 可以吧MyService注解的类注入到Spring容器中
        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(registry);
        classPathBeanDefinitionScanner.addIncludeFilter(new AnnotationTypeFilter(MyService.class));
        classPathBeanDefinitionScanner.scan("com");

        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }
}
