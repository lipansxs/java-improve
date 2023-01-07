package com.lipanre.multiplexing;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用单线程实现
 *
 * @author lipanre
 * @date 2023/1/2 17:22
 */
@Slf4j
public class ServerSocketMultiplexingSingleThread {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    /**
     * 创建一个多路复用单线程服务端对象
     * @param port
     * @throws IOException
     */
    public ServerSocketMultiplexingSingleThread(int port) throws IOException {

        serverSocketChannel = ServerSocketChannel.open();

        // 设置为非阻塞
        // 如果使用多路复用，必须要设置serverSocketChannel为非阻塞的，否则会抛出异常
        serverSocketChannel.configureBlocking(false);

        // 监听指定的端口
        serverSocketChannel.bind(new InetSocketAddress(port));

        // 多路复用器总共有三种实现方式：select、poll、epoll
        // 如果是select和poll的话，下面这个操作是在jvm创建一个数组用于后面维护文件描述符
        // 如果是epoll，那么下面这个操作将会调用系统调用epoll_create
        selector = Selector.open();

        // 监听是否可以建立连接
        // 如果是select和poll，下面这个操作将serverSocketChannel监听可连接的文件描述符放进前面创建的数组中
        // 如果是epoll，下面这个操作将会将serverSocketChannel监听的文件描述符放进内核空间维护的红黑树中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 开始启动服务端
     */
    public void start() {
        while (true) {
            // 通过多路复用器判断有哪些文件描述符已经准备好可连接或可读
            // 设置一个超时时间
            // 如果超时时间设置为0，那么就会阻塞，可以通过selector的wakeup()方法叫醒
            try {
                // 如果可操作的文件描述符数量 > 0
                if (this.selector.select(500) > 0) {
                    Set<SelectionKey> keys = this.selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = keys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        // 将当前游标指向的元素移除
                        // 如果不移除就会重复的调用调用
                        keyIterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandle(key);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 读取指定key发送过来的数据
     * @param key
     */
    private void readHandle(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();

        byteBuffer.clear();
        int readLength = 0;
        try {
            readLength = client.read(byteBuffer);
        } catch (IOException e) {
            log.error("客户端断开连接：" + client.getRemoteAddress(), e);
            client.close();
            return;
        }

        if (readLength == -1) {
            log.info("客户端断开连接：{}", client.getRemoteAddress());
            client.close();
            return;
        }

        byteBuffer.flip();
        var bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        String receiveMessage = new String(bytes).trim();
        log.info("接收到客户端发送过来的消息：{}", receiveMessage);

        byteBuffer.compact();
        byteBuffer.put(("接收到客户端发送过来的消息：" + receiveMessage + "\n").getBytes());
        byteBuffer.flip();
        client.write(byteBuffer);

    }

    /**
     * 接收客户端连接处理器
     * @param key
     */
    private void acceptHandler(SelectionKey key) throws IOException {
        var serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel client = serverSocketChannel.accept();

        // 因为要将客户端注册到多路复用器上，所以需要将SocketChannel设置为非阻塞
        // 否则在将客户端注册到多路复用器上的时候会抛出异常
        client.configureBlocking(false);

        // 监听客户端的可读事件
        client.register(this.selector, SelectionKey.OP_READ, ByteBuffer.allocate(8192));

        log.info("客户端连接成功：{}", client.getRemoteAddress());
    }


    public static void main(String[] args) throws IOException {
        var server = new ServerSocketMultiplexingSingleThread(9999);
        server.start();
    }

}
