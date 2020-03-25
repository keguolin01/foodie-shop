package com.ikgl.service;

import com.ikgl.pojo.OrderStatus;
import com.ikgl.pojo.bo.SubmitOrderBO;
import com.ikgl.pojo.vo.OrderVO;

public interface OrderService {
    /**
     * 生成订单
     * @param submitOrderBO
     */
   public OrderVO creatOrder(SubmitOrderBO submitOrderBO);

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
}
