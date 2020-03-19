package com.ikgl.service.impl;

import com.ikgl.mapper.CategoryMapper;
import com.ikgl.mapper.CategoryMapperCustom;
import com.ikgl.pojo.Carousel;
import com.ikgl.pojo.Category;
import com.ikgl.pojo.vo.CategoryVO;
import com.ikgl.pojo.vo.NewItemsVO;
import com.ikgl.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryMapperCustom categoryMapperCustom;
    @Override
    public List<Category> queryAllRoot() {
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("type",1);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }

    @Override
    public List<CategoryVO> getCategoryByRootId(Integer rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }

    @Override
    public List<NewItemsVO> getSixNewItemsLazy(String rootCatId) {
        return categoryMapperCustom.getSixNewItemsLazy(rootCatId);
    }
}
