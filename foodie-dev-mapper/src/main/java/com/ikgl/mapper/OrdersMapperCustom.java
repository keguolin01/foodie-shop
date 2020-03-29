package com.ikgl.mapper;

import com.ikgl.pojo.OrderStatus;
import com.ikgl.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {
    /**
     * 获取自己订单详情
     * @param paramsMap
     * @return
     */
    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap")Map<String, Object> paramsMap);

    /**
     * 获取各个状态下数量
     * @param paramsMap
     * @return
     */
    int getStatusCount(@Param("paramsMap")Map<String, Object> paramsMap);

    /**
     * 根据userId 获取订单的状态信息20 30 40
     * @param paramsMap
     * @return
     */
    List<OrderStatus> getOrdersTrend(@Param("paramsMap")Map<String, Object> paramsMap);
}
