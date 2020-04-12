package com.ikgl.controller;

import com.ikgl.pojo.bo.ShopCartBO;
import com.ikgl.utils.ResponseJSONResult;
import com.ikgl.utils.JsonUtils;
import com.ikgl.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "购物车相关接口",tags = "购物车相关接口")
@RestController
@RequestMapping("shopcart")
public class ShopCartController extends BaseController{

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("add")
    public ResponseJSONResult add(@RequestParam String userId, @RequestBody ShopCartBO shopCartBO,
                                  HttpServletRequest request, HttpServletResponse response){
        if(StringUtils.isBlank(userId)){
            return ResponseJSONResult.errorMsg("用户id为空");
        }
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART +":" + userId);
        List<ShopCartBO> list = null;
        boolean isExist = false;
        if(StringUtils.isNotBlank(shopCartJson)){
            list = JsonUtils.jsonToList(shopCartJson,ShopCartBO.class);
            for(ShopCartBO bo : list){
                if(bo.getSpecId().equals(shopCartBO.getSpecId())){
                    bo.setBuyCounts(bo.getBuyCounts() + shopCartBO.getBuyCounts());
                    isExist = true;
                }
            }
            if(!isExist){
                list.add(shopCartBO);
            }
        }else{
            list = new ArrayList<>();
            list.add(shopCartBO);
        }
        redisOperator.set(FOODIE_SHOPCART +":" + userId, JsonUtils.objectToJson(list));
        return ResponseJSONResult.ok();
    }

    @ApiOperation(value = "从购车中删除商品",notes = "从购车中删除商品",httpMethod = "POST")
    @PostMapping("del")
    public ResponseJSONResult del(@RequestParam String userId, @RequestParam String itemSpecId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return ResponseJSONResult.errorMsg("参数不能为空");
        }
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART +":" + userId);
        if(StringUtils.isNotBlank(shopCartJson)){
            List<ShopCartBO> list = JsonUtils.jsonToList(shopCartJson,ShopCartBO.class);
            for(ShopCartBO bo : list){
                if(bo.getSpecId().equals(itemSpecId)){
                    list.remove(bo);
                    break;
                }
            }
            redisOperator.set(FOODIE_SHOPCART +":" + userId, JsonUtils.objectToJson(list));
        }
        return ResponseJSONResult.ok();
    }
}
