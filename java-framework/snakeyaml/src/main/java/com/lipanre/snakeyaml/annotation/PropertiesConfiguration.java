package com.lipanre.snakeyaml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * use to indicate the properties configuration class which store the configuration information of application.
 * like Rabbitmq configuration, Mqtt configuration etc.
 *
 *
 * @author Layton
 * @date 2022/4/24 15:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertiesConfiguration {
}
