package com.lipanre;

/**
 * 测试ttl
 *
 * @author lipanre
 * @date 2023/6/14 16:50
 */
public class NormalThreadLocal {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        threadLocal.set("normal threadLocal!!!!");

        new Thread(() -> {
            System.out.println("子线程获取内容：" + threadLocal.get());
        }).start();

        System.out.println("主线程获取内容：" + threadLocal.get());


    }

}
