package com.ikgl.service.impl;

import com.github.pagehelper.PageHelper;
import com.ikgl.enums.CommentLevel;
import com.ikgl.mapper.*;
import com.ikgl.pojo.*;
import com.ikgl.pojo.vo.CommentLevelCountsVO;
import com.ikgl.pojo.vo.ItemCommentVO;
import com.ikgl.pojo.vo.SearchItemsVO;
import com.ikgl.pojo.vo.ShopCartVO;
import com.ikgl.service.ItemService;
import com.ikgl.utils.DesensitizationUtil;
import com.ikgl.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;


    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        example.createCriteria().andEqualTo("itemId",itemId);
        List<ItemsImg> itemsImgs = itemsImgMapper.selectByExample(example);
        return itemsImgs;
    }

    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        example.createCriteria().andEqualTo("itemId",itemId);
        List<ItemsSpec> itemsSpecs = itemsSpecMapper.selectByExample(example);
        return itemsSpecs;
    }

    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        example.createCriteria().andEqualTo("itemId",itemId);
        ItemsParam itemsParam = itemsParamMapper.selectOneByExample(example);
        return itemsParam;
    }

    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer good = getCommentCounts(itemId, CommentLevel.good.type);
        Integer bad = getCommentCounts(itemId, CommentLevel.bad.type);
        Integer normal = getCommentCounts(itemId, CommentLevel.normal.type);
        Integer all = good + bad + normal;
        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setBadCounts(bad);
        countsVO.setGoodCounts(good);
        countsVO.setNormalCounts(normal);
        countsVO.setTotalCounts(all);
        return countsVO;
    }

    @Override
    public PagedGridResult getCommentByItemIdAndLevel(String itemId, Integer level,Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<ItemCommentVO> list = itemsMapperCustom.getCommentByItemIdAndLevel(itemId, level);
        for(ItemCommentVO vo : list){
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }
        PagedGridResult pagedGridResult = PagedGridResult.setPagedGridResult(list, page);
        return pagedGridResult;
    }

    @Override
    public PagedGridResult searchItemByCatId(String catId, String sort, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);
        List<SearchItemsVO> list = itemsMapperCustom.searchItemByCatId(map);
        PagedGridResult pagedGridResult = PagedGridResult.setPagedGridResult(list, page);
        return pagedGridResult;
    }

    @Override
    public PagedGridResult searchItemByKeywords(String keywords, String sort, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("keywords",keywords);
        map.put("sort",sort);
        List<SearchItemsVO> list = itemsMapperCustom.searchItemByKeywords(map);
        PagedGridResult pagedGridResult = PagedGridResult.setPagedGridResult(list, page);
        return pagedGridResult;
    }

    @Override
    public List<ShopCartVO> queryItemsBySpecIds(String specIds) {
        List<String> specIdsList =new ArrayList<>();
//        String[] ids = specIds.split(",");
//        Collections.addAll(specIdsList,ids);
        //此做法不大合适 但也是可以 详情看xml
        return itemsMapperCustom.queryItemsBySpecIds(specIds);
    }

    public Integer getCommentCounts(String itemId,Integer level){
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        condition.setCommentLevel(level);
        return itemsCommentsMapper.selectCount(condition);
    }
}
