package com.ikgl.controller;

import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class BaseController {
    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // 支付中心的调用地址
    public static final String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
    //内网穿透这个会变 需要更换哦！
   // public static final String payReturnUrl = "http://47.98.138.56:8088/foodie-dev-api/orders/notifyMerchantOrderPaid";
    public static final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    //定义网站上传图片的地址 默认会在当前盘符下建文件夹
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "workspaces"
            + File.separator + "foodie"
            + File.separator + "images"
            + File.separator + "faces";
}
