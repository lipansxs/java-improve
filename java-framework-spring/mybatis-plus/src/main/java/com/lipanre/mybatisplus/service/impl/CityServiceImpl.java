package com.lipanre.mybatisplus.service.impl;

import com.lipanre.mybatisplus.service.ICityService;
import com.lipanre.mybatisplus.entity.City;
import com.lipanre.mybatisplus.mapper.CityMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lipanre
 * @since 2023-03-02 00:37:14
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {

}
