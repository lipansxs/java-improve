package com.lipanre;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TTL线程池测试
 *
 * @author lipanre
 * @date 2023/6/14 16:57
 */
public class TTLThreadPoolTest {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

        threadLocal.set("normal threadLocal!!!!");

        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.submit(() -> {
            System.out.println("线程池获取内容：" + threadLocal.get());
        });

        System.out.println("主线程获取内容：" + threadLocal.get());


    }

}
