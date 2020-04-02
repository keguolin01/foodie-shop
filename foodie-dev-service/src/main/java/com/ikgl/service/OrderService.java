package com.ikgl.service;

import com.ikgl.pojo.OrderStatus;
import com.ikgl.pojo.bo.ShopCartBO;
import com.ikgl.pojo.bo.SubmitOrderBO;
import com.ikgl.pojo.vo.OrderVO;

import java.util.List;

public interface OrderService {
    /**
     * 生成订单
     * @param list
     * @param submitOrderBO
     */
   public OrderVO creatOrder(List<ShopCartBO> list, SubmitOrderBO submitOrderBO) throws Exception;

    /**
     * 根据订单id  改变状态为已支付，待发货
     * @param merchantOrderId
     * @param waitDeliver
     */
    void updateOrderStatus(String merchantOrderId, int waitDeliver);

    /**
     * 根据订单id获取订单状态
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    void closeOrder();
}
