package com.lipanre.mybatisplus.service.impl;

import com.lipanre.mybatisplus.entity.Country;
import com.lipanre.mybatisplus.mapper.CountryMapper;
import com.lipanre.mybatisplus.service.ICountryService;
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
public class CountryServiceImpl extends ServiceImpl<CountryMapper, Country> implements ICountryService {

}
