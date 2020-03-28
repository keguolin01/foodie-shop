package com.ikgl.controller.center;

import com.ikgl.controller.BaseController;
import com.ikgl.pojo.Orders;
import com.ikgl.service.center.MyOrdersService;
import com.ikgl.utils.IMOOCJSONResult;
import com.ikgl.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "我的订单",tags = "我的订单详情的接口")
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "分页展示我的订单",notes = "分页展示我的订单",httpMethod = "POST")
    @PostMapping("query")
    public IMOOCJSONResult query(@ApiParam(name = "userId",value = "用户id",required = true)
                                     @RequestParam String userId,
                                 @ApiParam(name = "orderStatus",value = "订单状态",required = false)
                                     @RequestParam Integer orderStatus,
                                 @ApiParam(name = "page",value = "第几页",required = false)
                                     @RequestParam Integer page,
                                 @ApiParam(name = "pageSize",value = "一页显示几条",required = false)
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
        PagedGridResult result = myOrdersService.queryMyOrders(userId,orderStatus,page,pageSize);
        return IMOOCJSONResult.ok(result);
    }

    @ApiOperation(value = "商家发货接口",notes = "商家发货接口",httpMethod = "GET")
    @GetMapping("deliver")
    public IMOOCJSONResult deliver( @ApiParam(name = "orderId",value = "订单id",required = true)
                                        @RequestParam String orderId){
        if(StringUtils.isBlank(orderId)){
            return IMOOCJSONResult.errorMsg("订单id为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return IMOOCJSONResult.ok();

    }

    @ApiOperation(value = "确认收货接口",notes = "确认收货接口",httpMethod = "POST")
    @PostMapping("confirmReceive")
    public IMOOCJSONResult confirmReceive( @ApiParam(name = "orderId",value = "订单id",required = true)
                                           @RequestParam String orderId,
                                           @ApiParam(name = "userId",value = "用户id",required = true)
                                           @RequestParam String userId){
        Orders orders = checkUserOrder(userId,orderId);
        if(orders == null){
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        boolean success = myOrdersService.updateReceiveOrderStatus(orderId);
        if(!success){
            return IMOOCJSONResult.errorMsg("订单确认收货失败！请联系管理员");
        }
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "删除订单接口",notes = "删除订单接口",httpMethod = "POST")
    @PostMapping("delete")
    public IMOOCJSONResult delete( @ApiParam(name = "orderId",value = "订单id",required = true)
                                           @RequestParam String orderId,
                                           @ApiParam(name = "userId",value = "用户id",required = true)
                                           @RequestParam String userId){
        Orders orders = checkUserOrder(userId,orderId);
        if(orders == null){
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        boolean success = myOrdersService.deleteOrder(userId,orderId);
        if(!success){
            return IMOOCJSONResult.errorMsg("删除失败，请联系管理员");
        }
        return IMOOCJSONResult.ok();
    }


    private Orders checkUserOrder(String userId,String orderId){
        Orders orders =  myOrdersService.queryMyOrder(userId,orderId);
        return orders;
    }
}
