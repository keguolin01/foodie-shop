package com.ikgl.mapper;

import com.ikgl.my.mapper.MyMapper;
import com.ikgl.pojo.ItemsComments;
import com.ikgl.pojo.vo.MyContentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom{
    /**
     * 商品评论表
     * @param map
     */
    public void saveComments(Map<String,Object> map);

    /**
     * 查询自己的评论列表
     * @param map
     * @return
     */
    List<MyContentVO> queryMyContent(@Param("paramsMap") Map<String, Object> map);
}