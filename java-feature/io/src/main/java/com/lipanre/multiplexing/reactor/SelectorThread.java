package com.lipanre.multiplexing.reactor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * selector线程
 *
 * @author lipanre
 * @date 2023/2/12 10:29
 */

@Slf4j
@Data
public class SelectorThread implements Runnable{

    private final Selector selector;

    private final SelectorThreadGroup selectorThreadGroup;

    public SelectorThread(SelectorThreadGroup selectorThreadGroup) throws IOException {
        this.selector = Selector.open();
        this.selectorThreadGroup = selectorThreadGroup;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 由于这里没有设置超时时间，所以只要没有调用这个对象的weakUp()方法，就会一直在这里阻塞
                log.info("{} start run，before select， select key count is: {}", Thread.currentThread().getName(), this.selector.keys().size());
                int selectKeysNum = this.selector.select();
                if (selectKeysNum > 0) {
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    if (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 读取数据处理器
     * @param key
     */
    private void readHandler(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.clear();

        int readCount = client.read(byteBuffer);
        if (readCount > 0) {
            // byteBuffer反转一下，准备发送到客户端
            byteBuffer.flip();

            // 判断byteBuffer里面是否还有元素
            if (byteBuffer.hasRemaining()) {
                client.write(byteBuffer);
            }
        } else {
            // 客户端断开连接
            log.info("client closed： {}", client.getRemoteAddress());
            key.cancel();
        }
    }

    /**
     * 接收连接处理器
     * @param key
     */
    private void acceptHandler(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        this.selectorThreadGroup.register(client, SelectionKey.OP_READ, ByteBuffer.allocateDirect(4096));
    }

    /**
     * 往多路复用器上注册文件描述符
     * @param channel
     * @param selectionKey
     * @param attachment
     * @throws ClosedChannelException
     */
    public void register(SelectableChannel channel, int selectionKey, Object attachment) throws ClosedChannelException {
        channel.register(this.selector, selectionKey, attachment);
        this.selector.wakeup();
    }
}
