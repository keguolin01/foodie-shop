package com.ikgl.controller;

import com.ikgl.enums.YesOrNo;
import com.ikgl.pojo.Carousel;
import com.ikgl.pojo.Category;
import com.ikgl.pojo.Users;
import com.ikgl.pojo.bo.UserBO;
import com.ikgl.pojo.vo.CategoryVO;
import com.ikgl.pojo.vo.NewItemsVO;
import com.ikgl.service.CarouselService;
import com.ikgl.service.CategoryService;
import com.ikgl.service.UsersService;
import com.ikgl.utils.CookieUtils;
import com.ikgl.utils.IMOOCJSONResult;
import com.ikgl.utils.JsonUtils;
import com.ikgl.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@Api(value = "首页",tags = "首页展示的相关接口")
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "首页轮播图展示",notes = "首页轮播图展示",httpMethod = "GET")
    @GetMapping("carousel")
    public IMOOCJSONResult getCarouselPicture(){
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);
        return IMOOCJSONResult.ok(carousels);
    }
    @ApiOperation(value = "获取商品所有分类详情",notes = "获取商品所有分类详情",httpMethod = "GET")
    @GetMapping("subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable("rootCatId") Integer rootCatId){
        List<CategoryVO> categories = categoryService.getCategoryByRootId(rootCatId);
        if(CollectionUtils.isEmpty(categories)){
            return IMOOCJSONResult.errorMsg("该分类下商品不存在");
        }
        return IMOOCJSONResult.ok(categories);
    }

    @ApiOperation(value = "获取商品分类（二级分类）",notes = "获取商品分类（二级分类）",httpMethod = "GET")
    @GetMapping("cats")
    public IMOOCJSONResult cats(){
        List<Category> categories = categoryService.queryAllRoot();
        return IMOOCJSONResult.ok(categories);
    }

    @ApiOperation(value = "查询每个一级分类下最新的6条数据",notes = "查询每个一级分类下最新的6条数据",httpMethod = "GET")
    @GetMapping("sixNewItems/{rootCatId}")
    public IMOOCJSONResult cats(@ApiParam(name = "rootCatId",value = "一级分类id",required = true)
                                    @PathVariable("rootCatId") String rootCatId){
        if(StringUtils.isBlank(rootCatId)){
            return IMOOCJSONResult.errorMsg("该分类下商品不存在");
        }
        List<NewItemsVO> newItemsVOS = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(newItemsVOS);
    }
}