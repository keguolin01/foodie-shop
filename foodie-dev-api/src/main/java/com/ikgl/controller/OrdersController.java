package com.ikgl.controller;

import com.ikgl.enums.OrderStatusEnum;
import com.ikgl.enums.PayMethod;
import com.ikgl.pojo.OrderStatus;
import com.ikgl.pojo.bo.SubmitOrderBO;
import com.ikgl.pojo.vo.MerchantOrdersVO;
import com.ikgl.pojo.vo.OrderVO;
import com.ikgl.service.OrderService;
import com.ikgl.utils.IMOOCJSONResult;
import com.sun.media.jfxmedia.logging.Logger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "订单",tags={"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController{


    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "用户修改地址",notes = "用户修改地址",httpMethod = "POST")
    @PostMapping("create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(submitOrderBO.getPayMethod()!= PayMethod.WEIXIN.type &&
                submitOrderBO.getPayMethod()!= PayMethod.ALIPAY.type){
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        //1.创建订单
        OrderVO orderVO = orderService.creatOrder(submitOrderBO);
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);
        //便于测试 这边都以一分钱
        merchantOrdersVO.setAmount(1);
        //2.移除购物车中已结算（已提交的商品）
        //TODO 现在暂时是全部清空
        //CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);
        //3.向支付中心发送当前的订单
        //1.可以使用Http.post 2.用spring集成的http请求
        //1.Http.post
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse responsep = null;
//        String url = paymentUrl;
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Content-Type", "application/json");
//        post.addHeader("imoocUserId","imooc");
//        post.addHeader("password","imooc");
//        String s = JsonUtils.objectToJson(merchantOrdersVO);
//        StringEntity stringEntity = new StringEntity(s);
//        post.setEntity(stringEntity);
//        responsep = client.execute(post);
//        String	result = EntityUtils.toString(responsep.getEntity(),"UTF-8");
//        System.out.println(result);
        //2.spring集成的http
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","imooc");
        headers.add("password","imooc");
        //入参的格式
        HttpEntity<MerchantOrdersVO> httpEntity = new HttpEntity<MerchantOrdersVO>(merchantOrdersVO,headers);
        //返回的参数格式
        ResponseEntity<IMOOCJSONResult> resultResponseEntity = restTemplate.postForEntity(paymentUrl,httpEntity,IMOOCJSONResult.class);
        IMOOCJSONResult result = resultResponseEntity.getBody();
        if(result.getStatus() != 200){
            IMOOCJSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }
        return IMOOCJSONResult.ok(merchantOrdersVO.getMerchantOrderId());
    }
    @ApiOperation(value = "用户支付成功后回调的地址，更新订单状态表的状态",notes = "用户支付成功后回调的地址，更新订单状态表的状态",httpMethod = "POST")
    @PostMapping("notifyMerchantOrderPaid")
    public int notifyMerchantOrderPaid(String merchantOrderId){
        //1.创建订单
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "支付完成后获取订单状态表的数据",notes = "支付完成后获取订单状态表的数据",httpMethod = "POST")
    @PostMapping("getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(String orderId){
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return IMOOCJSONResult.ok(orderStatus);
    }
}
