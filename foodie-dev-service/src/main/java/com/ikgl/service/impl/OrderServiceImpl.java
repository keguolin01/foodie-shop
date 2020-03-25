package com.ikgl.service.impl;

import com.ikgl.enums.OrderStatusEnum;
import com.ikgl.enums.YesOrNo;
import com.ikgl.mapper.OrderItemsMapper;
import com.ikgl.mapper.OrderStatusMapper;
import com.ikgl.mapper.OrdersMapper;
import com.ikgl.pojo.*;
import com.ikgl.pojo.bo.SubmitOrderBO;
import com.ikgl.pojo.vo.MerchantOrdersVO;
import com.ikgl.pojo.vo.OrderVO;
import com.ikgl.service.AddressService;
import com.ikgl.service.ItemService;
import com.ikgl.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private Sid sid;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Transactional
    @Override
    public OrderVO creatOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String addressId = submitOrderBO.getAddressId();
        int payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
                //1.订单实体新增
        String orderId = sid.nextShort();
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());
        orders.setLeftMsg(leftMsg);
        orders.setPayMethod(payMethod);
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setIsComment(YesOrNo.NO.type);
        orders.setUserId(userId);
        //邮费设置为0
        int postAmount = 0;
        orders.setPostAmount(postAmount);
        //收件人姓名 需要从地址信息表取 条件userId,addressId
        UserAddress userAddress = addressService.queryUserAddress(userId,addressId);

        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getProvince()+" "+userAddress.getCity()
        +" "+userAddress.getDistrict()+" "+userAddress.getDetail());

        int totalAmount = 0;
        int realPayAmount = 0;
        String[] specids = itemSpecIds.split(",");
        for(String specId : specids){
            //TODO 整合redis后 购买数量从redis中获取
            int buyCount = 1;
            //根据规格id 查出相对应的价格
            ItemsSpec itemsSpec = itemService.getItemsSpecBySpecId(specId);
            totalAmount += itemsSpec.getPriceNormal() * buyCount;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCount;

            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String url = itemService.queryItemMainImgUrl(itemId);
            //商品信息表
            String subOrderId = sid.nextShort();
            OrderItems orderItems = new OrderItems();
            orderItems.setBuyCounts(buyCount);
            orderItems.setItemId(itemId);
            orderItems.setId(subOrderId);
            orderItems.setOrderId(orderId);
            orderItems.setItemSpecId(specId);
            orderItems.setItemName(items.getItemName());
            orderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItems.setItemImg(url);
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItemsMapper.insert(orderItems);
            //相对应需要减库存 规格表中
            itemService.decreaseItemSpecStock(specId,buyCount);
        }

        //订单总价格
        orders.setTotalAmount(totalAmount);
        //实际支付总价格
        orders.setRealPayAmount(realPayAmount);
        //保存订单表
        ordersMapper.insert(orders);
        //保存订单状态表
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreatedTime(new Date());
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatusMapper.insert(orderStatus);
        //构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setPayMethod(payMethod);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        return orderVO;
    }

    @Transactional
    @Override
    public void updateOrderStatus(String merchantOrderId, int waitDeliver) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(merchantOrderId);
        orderStatus.setPayTime(new Date());
        orderStatus.setOrderStatus(waitDeliver);
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }
}
