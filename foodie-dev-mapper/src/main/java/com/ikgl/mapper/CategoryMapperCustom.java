package com.ikgl.mapper;

import com.ikgl.pojo.vo.CategoryVO;
import com.ikgl.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapperCustom {
    public List<CategoryVO> getSubCatList(@Param("rootCatId") Integer rootCatId);

    List<NewItemsVO> getSixNewItemsLazy(@Param("rootCatId")String rootCatId);
}