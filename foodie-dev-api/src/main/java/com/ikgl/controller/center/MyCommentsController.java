package com.ikgl.controller.center;

import com.ikgl.controller.BaseController;
import com.ikgl.enums.YesOrNo;
import com.ikgl.pojo.OrderItems;
import com.ikgl.pojo.Orders;
import com.ikgl.pojo.bo.center.OrderItemsCommentBO;
import com.ikgl.service.center.MyCommentsService;
import com.ikgl.utils.IMOOCJSONResult;
import com.ikgl.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "用户中心我的订单",tags = "用户中心我的订单相关的接口")
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {
    //Autowired 默认是以type注入 就是从上下文中找到类型匹配的唯一bean进行装配
    //Qualifier 可结合Autowired 根据name 匹配  不能单个使用
    //Resource 默认是以name匹配 有name就会匹配 找不到抛异常 没有name 就会根据type进行查找
    @Autowired
    private MyCommentsService myCommentsService;
    //有构造器可以下面这样使用
//    public MyCommentsController(AddressService addressService,@Qualifier("aa") MyCommentsService myCommentsService){
//        this.addressService = addressService;
//        this.myCommentsService = myCommentsService;
//    }

    @ApiOperation(value = "查询用户此订单是否有评价，进入评价页面",notes = "查询用户此订单是否有评价，进入评价页面",httpMethod = "POST")
    @PostMapping("pending")
    public IMOOCJSONResult pending(
            @ApiParam(value = "用户id",name = "userId",required = true)
            @RequestParam String userId,
            @ApiParam(value = "订单id",name = "orderId",required = true)
            @RequestParam String orderId){
        Orders orders = checkUserOrder(userId, orderId);
        if(orders == null){
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        if(orders.getIsComment() == YesOrNo.YES.type){
            return IMOOCJSONResult.errorMsg("订单已评价");
        }

        List<OrderItems> orderItems = myCommentsService.queryPendingComment(orderId);
        return IMOOCJSONResult.ok(orderItems);
    }

    @ApiOperation(value = "保存评价",notes = "保存评价",httpMethod = "POST")
    @PostMapping("saveList")
    public IMOOCJSONResult saveList(
            @ApiParam(value = "用户id",name = "userId",required = true)
            @RequestParam String userId,
            @ApiParam(value = "订单id",name = "orderId",required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> orderItemsCommentBO){
        Orders orders = checkUserOrder(userId, orderId);
        if(orders == null){
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        if(orderItemsCommentBO.isEmpty() || orderItemsCommentBO.size() == 0 || orderItemsCommentBO == null){
            return IMOOCJSONResult.errorMsg("评价内容不能为空");
        }
        myCommentsService.saveComments(userId, orderId, orderItemsCommentBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "查询我的评价详情",notes = "查询我的评价详情",httpMethod = "POST")
    @PostMapping("query")
    public IMOOCJSONResult commentLevel(@ApiParam(name = "userId",value = "用户id",required = true)
                                        @RequestParam String userId,
                                        @ApiParam(name = "page",value = "第几页",required = true)
                                        @RequestParam Integer page,
                                        @ApiParam(name = "pageSize",value = "一页显示几条",required = true)
                                        @RequestParam Integer pageSize){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("用户id为空");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = myCommentsService.queryMyContent(userId, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }

}
