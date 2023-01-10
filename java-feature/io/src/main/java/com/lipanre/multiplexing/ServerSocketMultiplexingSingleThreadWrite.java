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
public class ServerSocketMultiplexingSingleThreadWrite {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public ServerSocketMultiplexingSingleThreadWrite(int port) throws IOException {

        // 创建多路复用器
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();

        // 设置为非阻塞
        this.serverSocketChannel.configureBlocking(false);

        // 绑定到指定端口，监听
        this.serverSocketChannel.bind(new InetSocketAddress(port));

        // 将serverSocketChannel注册到多路复用器上
        this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 启动服务
     */
    public void start() {
        while (true) {
            try {
                if (this.selector.select(500) > 0) {
                    Set<SelectionKey> keys = this.selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = keys.iterator();
                    if (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        // 移除防止重复
                        keyIterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandle(key);
                        } else if (key.isReadable()) {
                            readHandle(key);
                        } else if (key.isWritable()) {
                            writeHandle(key);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 写处理
     * @param key
     */
    private void writeHandle(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];

        byteBuffer.get(bytes, 0, bytes.length);
        String message = new String(bytes);
        log.info("接收到客户端发送过来的消息：{}", message);

        byteBuffer.compact();
        byteBuffer.put(("server：" + message).getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.register(this.selector, SelectionKey.OP_READ, byteBuffer);
    }

    private void readHandle(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.clear();
        int readLength = -1;
        try {
            readLength = client.read(byteBuffer);
        } catch (IOException e) {
            log.error("客户端异常，断开连接");
            client.close();
        }
        if (readLength > 0) {
            // 注册写事件到多路复用器上
            client.register(this.selector, SelectionKey.OP_WRITE, byteBuffer);
        } else {
            log.info("客户端断开连接");
            client.close();
        }
    }

    /**
     * 接收客户端连接处理
     * @param key
     */
    private void acceptHandle(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();

        // 设置为非阻塞
        client.configureBlocking(false);

        // 注册到多路复用器上
        client.register(this.selector, SelectionKey.OP_READ, ByteBuffer.allocateDirect(1024));

        log.info("客户端{}已连接成功", client.getRemoteAddress());
    }

    public static void main(String[] args) throws IOException {
        var s = new ServerSocketMultiplexingSingleThreadWrite(9999);
        s.start();
    }

}
