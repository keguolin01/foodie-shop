package com.ikgl.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.ikgl.enums.OrderStatusEnum;
import com.ikgl.enums.YesOrNo;
import com.ikgl.mapper.OrderStatusMapper;
import com.ikgl.mapper.OrdersMapper;
import com.ikgl.mapper.OrdersMapperCustom;
import com.ikgl.pojo.OrderStatus;
import com.ikgl.pojo.Orders;
import com.ikgl.pojo.vo.MyOrdersVO;
import com.ikgl.service.center.MyOrdersService;
import com.ikgl.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId",userId);
        paramsMap.put("status",orderStatus);
        List<MyOrdersVO> myOrdersVOS = ordersMapperCustom.queryMyOrders(paramsMap);
        PagedGridResult pagedGridResult = PagedGridResult.setPagedGridResult(myOrdersVOS, page);
        return pagedGridResult;
    }

    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus orderStatus =new OrderStatus();
        orderStatus.setDeliverTime(new Date());
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        Example example = new Example(OrderStatus.class);
        example.createCriteria().andEqualTo("orderId",orderId)
                                .andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        orderStatusMapper.updateByExampleSelective(orderStatus,example);

    }

    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.NO.type);
        return ordersMapper.selectOne(orders);
    }

    @Transactional
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus updatePojo = new OrderStatus();
        updatePojo.setSuccessTime(new Date());
        updatePojo.setOrderStatus(OrderStatusEnum.SUCCESS.type);

        Example example = new Example(OrderStatus.class);
        example.createCriteria().andEqualTo("orderId",orderId)
                .andEqualTo("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);
        int result = orderStatusMapper.updateByExampleSelective(updatePojo, example);
        return result == 1 ? true : false;
    }

    @Override
    public boolean deleteOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());
        Example example = new Example(Orders.class);
        example.createCriteria().andEqualTo("id",orderId)
                .andEqualTo("userId",userId);
        int result = ordersMapper.updateByExampleSelective(orders, example);
        return result == 1 ? true : false;
    }
}
