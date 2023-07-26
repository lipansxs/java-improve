package com.lipanre.queue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 延时队列测试
 *
 * @author LiPan
 */
public class DelayQueueTest {

    public static void main(String[] args) {
        DelayQueue<DelayMessage> delayQueue = new DelayQueue();

        // 消费
        Runnable consumerRunnable = () -> {
            while (true) {
                DelayMessage message = null;
                try {
                    message = delayQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("消息： " + message.getPayload() + "已过期: " + System.currentTimeMillis());
            }
        };

        Runnable producerRunnable = () -> {
            AtomicInteger count = new AtomicInteger(0);
            while (true) {
                String payload = "测试消息" + count.getAndIncrement() + " 当前时间：" + System.currentTimeMillis();
                delayQueue.offer(new DelayMessage(payload, 3000L));
                if (count.get() % 2 == 0) {
                    System.out.println("移除队列消息：" + payload);
                    if (delayQueue.remove(DelayMessage.create(payload))) {
                        System.out.println("移除成功");
                    } else {
                        System.out.println("移除失败");
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(consumerRunnable);
        executorService.submit(producerRunnable);

    }

}
