package com.lipanre.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Slf4j
public class BIOStream {

    public static void main(String[] args) throws FileNotFoundException {

        URL resource = BIOStream.class.getClassLoader().getResource("resource.txt");

        if (Objects.isNull(resource)) {
            log.error("未找到资源文件");
            return;
        }

        byte[] buffer = new byte[1024];
        int length = -1;
        try (FileInputStream fis = new FileInputStream(resource.getFile());
             BufferedInputStream bfis = new BufferedInputStream(fis)) {

            // read这里会阻塞
            // 并且数据的内容是由我们自己从内核读取到程序中的，所以是同步
            // 同步阻塞io
            while ((length = bfis.read(buffer)) != -1) {
                log.info("读取到内容：{}", new String(buffer, 0 ,length));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
