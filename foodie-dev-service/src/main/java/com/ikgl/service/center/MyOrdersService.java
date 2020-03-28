package com.ikgl.service.center;

import com.ikgl.pojo.Orders;
import com.ikgl.utils.PagedGridResult;

public interface MyOrdersService {
    /**
     * 查询用户订单信息
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    /**
     * 商家发货修改订单状态
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);

    /**
     * 查询是否真的有这个订单
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 确认收货 修改收货状态
     * @param orderId
     * @return
     */
    boolean updateReceiveOrderStatus(String orderId);

    /**
     * 逻辑删除订单
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId, String orderId);
}
