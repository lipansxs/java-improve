package com.lipanre.file;

import java.io.InputStream;

/**
 * 文件路径测试
 *
 * @author LiPan
 */
public class FileTest {

    public static void main(String[] args) {
        // 通过class.getResourceAsStream获取的是相对于当前类class文件路径的文件
        InputStream resourceAsStream = FileTest.class.getResourceAsStream("file_config_properties");
        System.out.println(resourceAsStream);

        // 通过classLoader.getResourcesAsStream获取的是相对于classes目录的文件
        InputStream resourceAsStream1 = FileTest.class.getClassLoader().getResourceAsStream("config.properties");
        System.out.println(resourceAsStream1);

    }

}
