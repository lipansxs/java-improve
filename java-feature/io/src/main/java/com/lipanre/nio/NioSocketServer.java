package com.lipanre.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 利用Nio实现的socket服务端
 *
 * @author lipanre
 * @date 2023/1/1 10:48
 */
@Slf4j
public class NioSocketServer {

    public static void main(String[] args) {
        int serverPort = 9999;

        SocketAddress serverAddress = new InetSocketAddress(serverPort);
        // Nio的socket，需要通过ServerSocketChannel.open来获取一个ServerSocketChannel对象，这样才可以设置非阻塞
        // 通过ServerSocket.getChannel获取到的结果会是null
        // 客户端使用的是SocketChannel
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.bind(serverAddress);
            // 设置为非阻塞，即可实现nio
            // 在accept的时候不会阻塞
            serverChannel.configureBlocking(false);

            List<SocketChannel> clients = new ArrayList<>();
            List<SocketChannel> needRemoveClients = new ArrayList<>();

            while (true) {
                // 每次循环停顿1s，防止cpu空转
                Thread.sleep(1000);

                // 因为设置了socketChannel为非阻塞的
                // 所以在这里accept的时候，如果有客户端连接进来就会返回连接进来的客户端的文件描述符（socket对象）
                // 如果没有客户端连接进来，那么返回的就是一个null
                SocketChannel client = serverChannel.accept();

                if (Objects.isNull(client)) {
                    log.info("当前没有客户端连接进来：{}", System.currentTimeMillis());
                } else {
                    // 设置客户端的socket也为非阻塞的
                    // 这样在利用客户端的socket读取数据的时候，就不会阻塞读取数据
                    client.configureBlocking(false);
                    log.info("当前已有客户端连接进来：{}", client.getRemoteAddress());
                    clients.add(client);
                }

                // 读取和回复数据
                for (SocketChannel tempClient : clients) {
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                    int readLength = 0;
                    try {
                        readLength = tempClient.read(byteBuffer);
                    } catch (IOException e) {
                        // 通过捕获这个异常，可以防止客户端异常导致抛出：Connection reset by peer
                        tempClient.close();
                        needRemoveClients.add(tempClient);
                        log.error("客户端连接异常，断开连接：" + tempClient.getRemoteAddress(), e);
                    }
                    if (readLength > 0) {
                        // 位置反转一下，准备从byteBuffer里面读取数据
                        byteBuffer.flip();
                        byte[] bytes = new byte[readLength];
                        byteBuffer.get(bytes);
                        log.info("接收到客户端发送过来的消息：{}", new String(bytes));
                    } else if(readLength == -1) {
                        // 如果读取数据返回-1，代表读取到了流的末尾
                        log.warn("客户端流读取完毕，断开连接：{}", tempClient.getRemoteAddress());
                        tempClient.close();
                        needRemoveClients.add(tempClient);
                    }
                }
                // 如果需要移除的客户端不为空
                // 移除已经关闭连接的客户端
                if (!needRemoveClients.isEmpty()) {
                    clients.removeAll(needRemoveClients);
                }
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }


    }
}
