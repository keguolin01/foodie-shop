package com.ikgl.controller;

import com.ikgl.pojo.Items;
import com.ikgl.pojo.ItemsImg;
import com.ikgl.pojo.ItemsParam;
import com.ikgl.pojo.ItemsSpec;
import com.ikgl.pojo.vo.CommentLevelCountsVO;
import com.ikgl.pojo.vo.ItemInfoVO;
import com.ikgl.pojo.vo.ShopCartVO;
import com.ikgl.service.ItemService;
import com.ikgl.utils.IMOOCJSONResult;
import com.ikgl.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Api(value = "商品",tags = "商品详情的接口")
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情",notes = "查询商品详情",httpMethod = "GET")
    @GetMapping("info/{itemId}")
    public IMOOCJSONResult cats(@ApiParam(name = "itemId",value = "商品id",required = true)
                                    @PathVariable("itemId") String itemId){
        if(StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg("商品不存在");
        }
        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);
        return IMOOCJSONResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价详情",notes = "查询商品评价详情",httpMethod = "GET")
    @GetMapping("commentLevel")
    public IMOOCJSONResult commentLevel(@ApiParam(name = "itemId",value = "商品id",required = true)
                                @RequestParam String itemId){
        if(StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg("商品不存在");
        }
        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评价详情",notes = "查询商品评价详情",httpMethod = "GET")
    @GetMapping("comments")
    public IMOOCJSONResult commentLevel(@ApiParam(name = "itemId",value = "商品id",required = true)
                                        @RequestParam String itemId,
                                        @ApiParam(name = "level",value = "评价等级1好2中3差",required = true)
                                        @RequestParam Integer level,
                                        @ApiParam(name = "page",value = "第几页",required = true)
                                        @RequestParam Integer page,
                                        @ApiParam(name = "pageSize",value = "一页显示几条",required = true)
                                        @RequestParam Integer pageSize){
        if(StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg("商品不存在");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = common_page_size;
        }
        PagedGridResult commentByItemIdAndLevel = itemService.getCommentByItemIdAndLevel(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(commentByItemIdAndLevel);
    }

    @ApiOperation(value = "关键字查询商品",notes = "关键字查询商品",httpMethod = "GET")
    @GetMapping("search")
    public IMOOCJSONResult searchItemByKeywords(@ApiParam(name = "keywords",value = "搜索关键字",required = true)
                                        @RequestParam String keywords,
                                        @ApiParam(name = "sort",value = "k默认,c销量,p价格",required = true)
                                        @RequestParam String sort,
                                        @ApiParam(name = "page",value = "第几页",required = true)
                                        @RequestParam Integer page,
                                        @ApiParam(name = "pageSize",value = "一页显示几条",required = true)
                                        @RequestParam Integer pageSize){
        if(StringUtils.isBlank(keywords)){
            return IMOOCJSONResult.errorMsg("请输入搜索名称");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = page_size;
        }
        PagedGridResult pagedGridResult = itemService.searchItemByKeywords(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "类别查询商品",notes = "类别查询商品",httpMethod = "GET")
    @GetMapping("catItems")
    public IMOOCJSONResult catItems(@ApiParam(name = "catId",value = "类别id",required = true)
                                                @RequestParam String catId,
                                                @ApiParam(name = "sort",value = "k默认,c销量,p价格",required = true)
                                                @RequestParam String sort,
                                                @ApiParam(name = "page",value = "第几页",required = true)
                                                @RequestParam Integer page,
                                                @ApiParam(name = "pageSize",value = "一页显示几条",required = true)
                                                @RequestParam Integer pageSize){
        if(StringUtils.isBlank(sort)){
            return IMOOCJSONResult.errorMsg("类别为空");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = page_size;
        }
        PagedGridResult pagedGridResult = itemService.searchItemByCatId(catId, sort, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "根据商品规格ids查询规格商品的信息",notes = "根据商品规格ids查询规格商品的信息",httpMethod = "GET")
    @GetMapping("refresh")
    public IMOOCJSONResult catItems(@ApiParam(name = "itemSpecIds",value = "类别id",required = true,example = "1001,2002")
                                    @RequestParam String itemSpecIds){
        if(StringUtils.isBlank(itemSpecIds)){
            return IMOOCJSONResult.ok();
        }
        List<ShopCartVO> shopCartVOS = itemService.queryItemsBySpecIds(itemSpecIds);
        return IMOOCJSONResult.ok(shopCartVOS);
    }
}