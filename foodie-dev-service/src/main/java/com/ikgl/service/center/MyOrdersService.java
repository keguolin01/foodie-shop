package com.ikgl.service.center;

import com.ikgl.pojo.Orders;
import com.ikgl.pojo.vo.OrderStatusCountsVO;
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

    /**
     * 获取用户各个状态下 订单状态的数量
     * @param userId
     * @return
     */
    public OrderStatusCountsVO getStatusCount(String userId);

    /**
     * 获取20 30 40 状态下的订单动态
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize);
}
