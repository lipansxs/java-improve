package com.lipanre;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * TTL测试子线程
 *
 * @author lipanre
 * @date 2023/6/14 16:56
 */
public class TTLSubThreadTest {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

        threadLocal.set("normal threadLocal!!!!");

        new Thread(() -> {
            System.out.println("子线程获取内容：" + threadLocal.get());
        }).start();

        System.out.println("主线程获取内容：" + threadLocal.get());


    }

}
