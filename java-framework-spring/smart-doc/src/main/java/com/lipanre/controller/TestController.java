package com.lipanre.controller;

import com.lipanre.entity.TestEntity;
import com.lipanre.query.TestQuery;
import org.springframework.web.bind.annotation.*;

/**
 * 测试controller
 *
 * @author lipanre
 * @date 2023/5/30 01:48
 */

@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试通过id获取数据
     *
     * @param dataId 数据id
     * @return 数据实体
     */
    @GetMapping("/{dataId}")
    public String getData(@PathVariable("dataId") String dataId) {
        return "this is the target data";
    }

    /**
     * 查询数据
     *
     * @param testQuery 查询实体类
     * @return
     */
    @RequestMapping("/get")
    public void getData(@RequestBody TestQuery testQuery) {
        return;
    }

    /**
     * 获取用户实体对象
     *
     * @return
     */
    @GetMapping
    public TestEntity getData() {
        return new TestEntity();
    }
}
