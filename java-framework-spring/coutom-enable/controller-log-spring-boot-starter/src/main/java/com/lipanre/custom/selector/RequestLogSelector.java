package com.lipanre.custom.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author lipanre
 * @date 2023/6/7 23:24
 */
public class RequestLogSelector implements ImportSelector {


    /**
     * 可以获得使用@Import导入RequestLogSelector注解的注解所标柱的类上的所有注解
     * @param importingClassMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        for (String annotationType : importingClassMetadata.getAnnotationTypes()) {
            System.out.println(annotationType);
        }
        return new String[0];
    }
}
