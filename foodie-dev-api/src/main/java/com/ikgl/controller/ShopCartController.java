package com.ikgl.controller;

import com.ikgl.pojo.bo.ShopCartBO;
import com.ikgl.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车相关接口",tags = "购物车相关接口")
@RestController
@RequestMapping("shopcart")
public class ShopCartController {

    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("add")
    public IMOOCJSONResult add(@RequestParam String userId,@RequestBody ShopCartBO shopCartBO,
                               HttpServletRequest request, HttpServletResponse response){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("用户id为空");
        }
        System.out.println(shopCartBO);
        //TODO 前端在登录的情况下 添加商品到购物车，会同时在后端同步购物车到redis
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "从购车中删除商品",notes = "从购车中删除商品",httpMethod = "POST")
    @PostMapping("del")
    public IMOOCJSONResult del(@RequestParam String userId,@RequestParam String itemSpecId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        //TODO 用户在已登录的情况下 要清除redis中缓存的数据
        return IMOOCJSONResult.ok();
    }
}
