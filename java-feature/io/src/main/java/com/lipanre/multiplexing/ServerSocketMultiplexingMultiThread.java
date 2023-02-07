package com.lipanre.multiplexing;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * IO多路复用多线程
 *
 * @author lipanre
 * @date 2023/1/14 12:09
 */

@Slf4j
public class ServerSocketMultiplexingMultiThread {

    private final Selector selector;

    /**
     * 多路复用获取内核超时时间
     */
    private static final long SELECT_TIME_OUT_MS = 500;

    public ServerSocketMultiplexingMultiThread(int port) throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException {
        while (true) {
            if (this.selector.select(SELECT_TIME_OUT_MS) > 0) {
                Set<SelectionKey> keys = this.selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();
                if (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isAcceptable()) {
                        acceptHandle(key);
                    } else if (key.isReadable()) {
                        readHandle(key);
                        key.interestOps(SelectionKey.OP_WRITE);
                    } else if (key.isWritable()) {
                        writeHandle(key);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        }
    }

    private void writeHandle(SelectionKey key) {
        new Thread(() -> {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
            byteBuffer.clear();
            byteBuffer.put("Hello\n".getBytes());
            byteBuffer.flip();
            try {
                socketChannel.write(byteBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void readHandle(SelectionKey key) {

        new Thread(() -> {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();

            byteBuffer.clear();
            try {
                int readLength = client.read(byteBuffer);
                if (readLength == -1) {
                    log.warn("客户端连接已正常关闭");
                    client.close();
                    return;
                }
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.limit()];
                byteBuffer.get(bytes);

                log.info("接收到客户端消息：{}", new String(bytes));

            } catch (IOException e) {
                log.error("客户端异常断开连接：", e);
            }

        }).start();
    }

    /**
     * 处理客户端连接
     *
     * @param key
     * @throws IOException
     */
    private void acceptHandle(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(this.selector, SelectionKey.OP_READ, ByteBuffer.allocateDirect(1024));
        log.info("有一个客户端连接进来了：{}:{}", client.getLocalAddress(), client.getRemoteAddress());
    }

    public static void main(String[] args) throws IOException {
        var s = new ServerSocketMultiplexingMultiThread(9999);
        s.start();
    }

}
