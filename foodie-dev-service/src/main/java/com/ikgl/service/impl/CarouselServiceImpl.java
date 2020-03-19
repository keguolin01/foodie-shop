package com.ikgl.service.impl;

import com.ikgl.mapper.CarouselMapper;
import com.ikgl.pojo.Carousel;
import com.ikgl.pojo.Users;
import com.ikgl.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> queryAll(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        example.createCriteria().andEqualTo("isShow",isShow);
        List<Carousel> carousels = carouselMapper.selectByExample(example);
        return carousels;
    }
}
