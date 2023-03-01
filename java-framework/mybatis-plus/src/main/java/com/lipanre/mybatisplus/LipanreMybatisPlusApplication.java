package com.lipanre.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * mybatisplus服务启动类
 *
 * @author lipanre
 * @date 2023/3/1 22:08
 */
@MapperScan(basePackages = "com.lipanre.mybatisplus.mapper")
@SpringBootApplication
public class LipanreMybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(LipanreMybatisPlusApplication.class, args);
    }
}

