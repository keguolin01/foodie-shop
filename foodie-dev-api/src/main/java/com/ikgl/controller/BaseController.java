package com.ikgl.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer common_page_size = 10;
    public static final Integer page_size = 20;

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
    //内网穿透这个会变 需要更换哦！
    String payReturnUrl = "http://gxjs2w.natappfree.cc/orders/notifyMerchantOrderPaid";
}
