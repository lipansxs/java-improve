package com.lipanre.multiplexing.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * selector线程组
 *
 * @author lipanre
 * @date 2023/2/12 10:28
 */
public class SelectorThreadGroup {

    /**
     * selector线程集合，这个线程组维护的selector线程
     */
    private final List<SelectorThread> selectorThreadList;

    private AtomicInteger position;

    public static SelectorThreadGroup createSelectorThreadGroup(int threadCount) throws IOException {
        SelectorThreadGroup selectorThreadGroup = new SelectorThreadGroup(threadCount);
        // 初始化启动线程
        selectorThreadGroup.init();
        return selectorThreadGroup;
    }

    /**
     * 创建指定数目的selector线程
     * @param threadCount
     */
    private SelectorThreadGroup(int threadCount) throws IOException {
        List<SelectorThread> selectorThreads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            selectorThreads.add(new SelectorThread(this));
        }
        this.selectorThreadList = selectorThreads;
        this.position = new AtomicInteger(0);
    }

    /**
     * 启动所有线程
     */
    private void init() {
        this.selectorThreadList.stream().map(Thread::new).forEach(Thread::start);
    }

    /**
     * 从这个线程组中维护的selector线程中拿出一个selector监听指定端口
     * @param port 需要监听的端口
     */
    public void bind(int port) throws IOException {

        // 选出一个selectorThread监听指定端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));

        // 注册到多路复用器上，监听客户端连接
        register(serverSocketChannel);
    }

    public void register(SelectableChannel channel) {
        SelectorThread selectorThread = this.getNextSelectorThread();
        selectorThread.addChannel(channel);
    }

    /**
     * 目前实现：选择第一个selectorThread用来接收客户端连接
     * @return
     */
    private SelectorThread getNextSelectorThread() {
        int index = this.position.getAndIncrement() % this.selectorThreadList.size();
        return this.selectorThreadList.get(index);
    }

}
