package com.ikgl.mapper;


import com.ikgl.my.mapper.MyMapper;
import com.ikgl.pojo.ItemsComments;
import com.ikgl.pojo.vo.ItemCommentVO;
import com.ikgl.pojo.vo.SearchItemsVO;
import com.ikgl.utils.PagedGridResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsCustom {
    public List<ItemCommentVO> getCommentByItemIdAndLevel(@Param("itemId") String itemId, @Param("level") Integer level);

    public List<SearchItemsVO>  searchItemByKeywords(@Param("paramsMap")Map<String,Object> paramsMap);

    List<SearchItemsVO> searchItemByCatId(@Param("paramsMap")Map<String, Object> paramsMap);
}