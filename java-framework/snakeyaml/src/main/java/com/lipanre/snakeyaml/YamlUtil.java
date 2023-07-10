package com.lipanre.snakeyaml;

import org.yaml.snakeyaml.Yaml;

/**
 * yaml文件工具类
 *
 * @author LiPan
 */
public class YamlUtil {

    private static final String CONFIG_FILE_NAME = "application.yml";



    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        Iterable<Object> objects = yaml.loadAll(YamlUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
        objects.forEach(System.out::println);
    }


}
