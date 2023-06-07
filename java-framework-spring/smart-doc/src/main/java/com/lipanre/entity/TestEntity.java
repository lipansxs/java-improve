package com.lipanre.entity;

import com.lipanre.enums.SEX;
import lombok.Data;

/**
 * 测试实体类
 *
 * @author lipanre
 * @date 2023/5/30 02:23
 */

@Data
public class TestEntity {

    /**
     * 用户名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     *
     * @see SEX
     */
    private SEX sex;

}
