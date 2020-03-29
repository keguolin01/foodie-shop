package com.ikgl.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.ikgl.enums.YesOrNo;
import com.ikgl.mapper.ItemsCommentsMapperCustom;
import com.ikgl.mapper.OrderItemsMapper;
import com.ikgl.mapper.OrderStatusMapper;
import com.ikgl.mapper.OrdersMapper;
import com.ikgl.pojo.OrderItems;
import com.ikgl.pojo.OrderStatus;
import com.ikgl.pojo.Orders;
import com.ikgl.pojo.bo.center.OrderItemsCommentBO;
import com.ikgl.pojo.vo.MyContentVO;
import com.ikgl.pojo.vo.OrderStatusCountsVO;
import com.ikgl.service.center.MyCommentsService;
import com.ikgl.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        return orderItemsMapper.select(orderItems);
    }

    @Transactional
    @Override
    public void saveComments(String userId, String orderId, List<OrderItemsCommentBO> orderItemsCommentBOS) {
        //1.保存item_comment
        for(OrderItemsCommentBO bos : orderItemsCommentBOS){
            bos.setCommentId(sid.nextShort());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("commentsList",orderItemsCommentBOS);
        itemsCommentsMapperCustom.saveComments(map);
        //2.修改订单表 已评价 order
        Orders orders = new Orders();
        orders.setIsComment(YesOrNo.YES.type);
        orders.setId(orderId);
        ordersMapper.updateByPrimaryKeySelective(orders);
        //3.修改订单状态表的留言时间 order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    public PagedGridResult queryMyContent(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        List<MyContentVO> myContentVOS = itemsCommentsMapperCustom.queryMyContent(map);
        PagedGridResult pagedGridResult = PagedGridResult.setPagedGridResult(myContentVOS, page);
        return pagedGridResult;
    }

}
