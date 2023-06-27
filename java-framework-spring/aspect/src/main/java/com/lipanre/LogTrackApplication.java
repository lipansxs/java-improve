package com.lipanre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lipanre
 * @date 2023/6/14 22:57
 */

@EnableAspectJAutoProxy
@SpringBootApplication
public class LogTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogTrackApplication.class);
    }

}
