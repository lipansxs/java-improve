package com.lipanre.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 测试compiletableFeature
 *
 * @author LiPan
 */
public class CompletableFutureTest {

    /**
     * 测试completableFeature创建任务不会阻塞
     */
    public static void testCompletableNonBlocking() {
        CompletableFuture<Void> feature = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("async run");
        });
        System.out.println("main run");
    }

    /**
     * 测试运行具有返回值的任务
     */
    public static void testRunReturnTask() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "任务结果";
        });
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void testRunNonRetureTask() {
        CompletableFuture.runAsync(() -> {
            System.out.println("这是没有返回值的任务");
        }).join();
    }
    
    public static void main(String[] args) {

        // testCompletableNonBlocking();
        // testRunReturnTask();
        testRunNonRetureTask();


    }

}
