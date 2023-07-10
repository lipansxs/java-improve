package com.lipanre.snakeyaml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  use to indicate the field in properties configuration class which store the configuration information of application.
 *  like Rabbitmq configuration, Mqtt configuration etc.
 *
 * @author Layton
 * @date 2022/4/24 15:07
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertiesField {

    /**
     * the field value configed in properties
     *
     * @return the filed value
     */
    String value();

    /**
     * the field class type
     *
     * @return the field class type
     */
    Class<?> type() default String.class;
}
