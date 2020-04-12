package com.ikgl.controller;

import com.ikgl.enums.YesOrNo;
import com.ikgl.pojo.Carousel;
import com.ikgl.pojo.Category;
import com.ikgl.pojo.vo.CategoryVO;
import com.ikgl.pojo.vo.NewItemsVO;
import com.ikgl.service.CarouselService;
import com.ikgl.service.CategoryService;
import com.ikgl.utils.ResponseJSONResult;
import com.ikgl.utils.JsonUtils;
import com.ikgl.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "首页",tags = "首页展示的相关接口")
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "首页轮播图展示",notes = "首页轮播图展示",httpMethod = "GET")
    @GetMapping("carousel")
    public ResponseJSONResult getCarouselPicture(){
        List<Carousel> list = new ArrayList<>();
        String carousel = redisOperator.get("carousel");
        if(StringUtils.isBlank(carousel)){
            list = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        }else{
            list = JsonUtils.jsonToList(carousel,Carousel.class);
        }
        list = carouselService.queryAll(YesOrNo.YES.type);
        return ResponseJSONResult.ok(list);
    }

    //1.如果轮播图发生更改  一般就删除缓存 然后重置
    //2.如果比较多，采取定时重置的手法，  凌晨更新

    @ApiOperation(value = "获取商品所有分类详情",notes = "获取商品所有分类详情",httpMethod = "GET")
    @GetMapping("subCat/{rootCatId}")
    public ResponseJSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable("rootCatId") Integer rootCatId){
        if(rootCatId == null){
            return ResponseJSONResult.errorMsg("该分类下商品不存在");
        }
        List<CategoryVO> list = new ArrayList<>();
        String subCats = redisOperator.get("subCat:"+rootCatId);
        if(StringUtils.isBlank(subCats)){
            list= categoryService.getCategoryByRootId(rootCatId);
            redisOperator.set("sub:"+rootCatId,JsonUtils.objectToJson(list));
        }else{
            list = JsonUtils.jsonToList(subCats,CategoryVO.class);
        }
        return ResponseJSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品分类（二级分类）",notes = "获取商品分类（二级分类）",httpMethod = "GET")
    @GetMapping("cats")
    public ResponseJSONResult cats(){
        List<Category> list = new ArrayList<>();
        String cats = redisOperator.get("cats");
        if(StringUtils.isBlank(cats)){
            list = categoryService.queryAllRoot();
            redisOperator.set("cats",JsonUtils.objectToJson(list));
        }else{
            list = JsonUtils.jsonToList(cats,Category.class);
        }
        return ResponseJSONResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下最新的6条数据",notes = "查询每个一级分类下最新的6条数据",httpMethod = "GET")
    @GetMapping("sixNewItems/{rootCatId}")
    public ResponseJSONResult cats(@ApiParam(name = "rootCatId",value = "一级分类id",required = true)
                                    @PathVariable("rootCatId") String rootCatId){
        if(StringUtils.isBlank(rootCatId)){
            return ResponseJSONResult.errorMsg("该分类下商品不存在");
        }
        List<NewItemsVO> newItemsVOS = categoryService.getSixNewItemsLazy(rootCatId);
        return ResponseJSONResult.ok(newItemsVOS);
    }
}