package com.lipanre.nio;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class ByteBufferDemo {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 2);
        byteBuffer.put((byte) 3);
        byteBuffer.put((byte) 4);

        log.info("byteBuffer: {}", byteBuffer);

        // 准备读
        byteBuffer.flip();
        log.info("byteBuffer: {}", byteBuffer);

        // 准备写
        byteBuffer.compact();
        log.info("byteBuffer: {}", byteBuffer);
    }

}
