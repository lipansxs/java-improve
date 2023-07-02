package com.lipanre.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件Nio实现读写
 *
 * @author lipanre
 * @date 2023/1/1 11:26
 */
@Slf4j
public class FileNio {

    public static void main(String[] args) throws FileNotFoundException {
        String userDir = System.getProperty("user.dir");
        log.info("user.dir: {}", userDir);

        String projectPath = userDir + "/java-feature/io/";
        log.info("project path: {}", projectPath);

        String filePath = projectPath + "niofile.txt";
        log.info("file path: {}", filePath);

        // RandomAccessFile可以通过seek移动光标写入指定位置
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {

            // 先顺序写入一部分内容
            raf.write("hello lipanre\n".getBytes());
            raf.write("hello lipanre\n".getBytes());
            log.info("written======================================");
            System.in.read();

            // 将游标seek到第四个字节
            raf.seek(4);
            raf.write("ooxx\n".getBytes());
            log.info("seeked======================================");
            System.in.read();

            // 通过raf.write写内容到文件中是需要从用户态转换到内核态将数据写入到pageCache中
            // java nio提供呢了FileChannel.map，可以直接将文件映射到pageCache中，写入内容到pageCache也不需要经过用户态和内核态的转换
            // position和size代表了将文件的哪一个区域映射到pageCache
            // 然后通过这个map写入到pageCache中
            FileChannel rafChannel = raf.getChannel();
            MappedByteBuffer mappedByteBuffer = rafChannel.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
            mappedByteBuffer.put("aaa\n".getBytes());
            mappedByteBuffer.force(); // 刷新缓存
            log.info("map written======================================");
            System.in.read();

            // 准备从文件中读取内容
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

            // 先将文件seek到开始位置，不然会从前面seek后的位置开始读
            raf.seek(0);
            int readSize = rafChannel.read(byteBuffer);
            log.info("byteBuffer read file size: {}", readSize);

            // 准备读内容
            byteBuffer.flip();

            for (int i = 0; i < byteBuffer.limit(); i++) {
                Thread.sleep(300);
                System.out.print((char) byteBuffer.get(i));
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
