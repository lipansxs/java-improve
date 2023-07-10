package com.lipanre.mybatisplus.controller;

import com.lipanre.mybatisplus.entity.City;
import com.lipanre.mybatisplus.service.ICityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author LiPan
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private ICityService cityService;

    @GetMapping
    public City getOne() {
        return cityService.getById(129L);
    }

}
