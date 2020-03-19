package com.ikgl.service;

import com.ikgl.pojo.Category;
import com.ikgl.pojo.vo.CategoryVO;
import com.ikgl.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {
    /**
     * 查询根节点（一级分类）下商品
     * @return
     */
    public List<Category> queryAllRoot();

    /**
     * 根据节点信息加载其节点下所有子节点信息
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getCategoryByRootId(Integer rootCatId);

    /**
     * 查询每个一级分类下最新的6条数据，不是主图就以时间排序
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(String rootCatId);
}
