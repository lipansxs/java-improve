package com.lipanre.bio;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

@Slf4j
public class BIOSocketServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9999);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new ChatThread(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        serverSocket.close();

    }

    @AllArgsConstructor
    static class ChatThread implements Runnable {

        private Socket client;

        @Override
        public void run() {

            try (InputStream is = client.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));
                 OutputStream os = client.getOutputStream();
                 BufferedOutputStream bos = new BufferedOutputStream(os)) {
                while (true) {

                    String clientMessage = null;
                    try {
                        clientMessage = br.readLine();
                    } catch (IOException e) {
                        log.error("客户端异常，断开连接：", e);
                        // 跳出循环关闭客户端连接
                        break;
                    }

                    if (Objects.nonNull(clientMessage) && !clientMessage.isBlank()) {
                        log.info("接收到客户端发送过来的消息：{}", clientMessage);

                        // 发送数据给客户端数据
                        bos.write(("服务端接收到了客户端发送过来的消息：" + clientMessage + "\n").getBytes());
                        bos.flush();
                    } else if (Objects.isNull(clientMessage)) {
                        log.info("客户端程序关闭，断开连接");
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
