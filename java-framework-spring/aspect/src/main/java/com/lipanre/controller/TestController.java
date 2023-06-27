package com.lipanre.controller;

import com.lipanre.annotation.LogTrack;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lipanre
 * @date 2023/6/14 22:59
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @LogTrack
    @GetMapping
    public void test() {
        System.out.println("test method invoked");
    }


}
