package com.ikgl.service;


import com.ikgl.pojo.Items;
import com.ikgl.pojo.ItemsImg;
import com.ikgl.pojo.ItemsParam;
import com.ikgl.pojo.ItemsSpec;
import com.ikgl.pojo.vo.CommentLevelCountsVO;
import com.ikgl.pojo.vo.ShopCartVO;
import com.ikgl.utils.PagedGridResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品id查询商品信息
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询出对应的图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询出商品的规格信息
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询出商品的参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     *根据商品id查询评价数量
     * @param itemId
     * @return
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据评价等级和商品id获取评价内容
     * @param itemId
     * @param level
     * @return
     */
    public PagedGridResult getCommentByItemIdAndLevel(String itemId,Integer level,Integer page,Integer pageSize);

    /**
     * 检索商品
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItemByKeywords(String keywords,String sort,Integer page,Integer pageSize);

    /**
     * 查询类别下的所有商品 分页
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItemByCatId(String catId, String sort, Integer page, Integer pageSize);

    /**
     * 加入购物车时展示购物车内容 根据规格id查询
     * @param specIds
     * @return
     */
    public List<ShopCartVO> queryItemsBySpecIds(String specIds);
}
