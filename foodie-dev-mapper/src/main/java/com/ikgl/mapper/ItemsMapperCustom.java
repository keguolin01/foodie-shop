package com.ikgl.mapper;


import com.ikgl.pojo.vo.ItemCommentVO;
import com.ikgl.pojo.vo.SearchItemsVO;
import com.ikgl.pojo.vo.ShopCartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {
    public List<ItemCommentVO> getCommentByItemIdAndLevel(@Param("itemId") String itemId, @Param("level") Integer level);

    public List<SearchItemsVO>  searchItemByKeywords(@Param("paramsMap")Map<String,Object> paramsMap);

    List<SearchItemsVO> searchItemByCatId(@Param("paramsMap")Map<String, Object> paramsMap);

    List<ShopCartVO> queryItemsBySpecIds(@Param("paramsList")String specIdsList);

    int decreaseItemSpecStock(@Param("specId")String specId,@Param("buyCount") int buyCount);
}