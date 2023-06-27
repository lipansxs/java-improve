package com.lipanre;

/**
 * 测试jdk提供的子线程继承主线程threadLocal内容
 *
 * @author lipanre
 * @date 2023/6/14 16:55
 */
public class InheritableThreadLocalTest {


    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

        threadLocal.set("normal threadLocal!!!!");

        new Thread(() -> {
            System.out.println("子线程获取内容：" + threadLocal.get());
        }).start();

        System.out.println("主线程获取内容：" + threadLocal.get());


    }


}
