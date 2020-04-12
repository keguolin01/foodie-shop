package com.ikgl.service.impl;

import com.ikgl.enums.OrderStatusEnum;
import com.ikgl.enums.YesOrNo;
import com.ikgl.lock.RedisLock;
import com.ikgl.lock.ZkLock;
import com.ikgl.mapper.ItemsSpecMapper;
import com.ikgl.mapper.OrderItemsMapper;
import com.ikgl.mapper.OrderStatusMapper;
import com.ikgl.mapper.OrdersMapper;
import com.ikgl.pojo.*;
import com.ikgl.pojo.bo.ShopCartBO;
import com.ikgl.pojo.bo.SubmitOrderBO;
import com.ikgl.pojo.vo.MerchantOrdersVO;
import com.ikgl.pojo.vo.OrderVO;
import com.ikgl.service.AddressService;
import com.ikgl.service.ItemService;
import com.ikgl.service.OrderService;
import com.ikgl.utils.DateUtil;
import com.ikgl.utils.RedissonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.n3r.idworker.Sid;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final String orderZookeeperKey = "order";
    private final String zookeeperCuratorPath = "/order";

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

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CuratorFramework client;
//     fs依赖service时会出现bean未加载  要么那个加载文件复制过去


    @Transactional
    @Override
    public OrderVO creatOrder(List<ShopCartBO> list,SubmitOrderBO submitOrderBO) throws Exception {
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
        List<ShopCartBO> toBeRemovedShopCarts = new ArrayList<>();
        for(String specId : specids){
            ShopCartBO bo = getBuyCountFromRedis(list,specId);
            toBeRemovedShopCarts.add(bo);
            int buyCount = 1;
//            int buyCount = bo.getBuyCounts();
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
            //都可以实现集群部署定时任务加锁
            // 加redis分布式锁
//            getRedisLock(specId,buyCount);
            // 加zookeeper分布式锁
            //getZookeeperLock(specId,buyCount);
            // 加zookeeper Curator分布式锁
//            getZookeeperCurator(specId,buyCount);
            getRedissonLock(specId,buyCount);
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
        orderVO.setToBeRemovedShopCartList(toBeRemovedShopCarts);
        return orderVO;
    }

    //支持阻塞  Redis实现
    private void getRedissonLock(String specId,Integer buyCount) throws Exception {
        RedissonClient redisson = RedissonUtils.getRedissonClient();
        RLock rLock = redisson.getLock(orderZookeeperKey);
        log.info("我进入了方法！！");
        try {
            rLock.lock(30, TimeUnit.SECONDS);
            log.info("我获得了锁！！！");
            itemService.decreaseItemSpecStock(specId,buyCount);
//            Thread.sleep(10000);
        }finally {
            log.info("我释放了锁！！");
            rLock.unlock();
        }
        log.info("方法执行完成！！");
    }

    // 从获取锁到释放锁基本很快 如果出现并发也可以确保数据的原子性
    // 缺点 不支持阻塞 就是并发下单的时候 后面那位不会等待前面那个释放锁，直接执行后面的
    // 缺点 redis 默认key失效时间30s 假设超过30s 就会存在key被自动释放
    // 下一个进来可能就会出现其他的问题（库存可能不充足但还是下单成功了）
    private void getRedisLock(String specId,Integer buyCount) throws Exception{
        try (RedisLock redisLock = new RedisLock(redisTemplate,"redisKey",15)){
            if (redisLock.getLock()) {
                log.info("我进入了锁！！");
                itemService.decreaseItemSpecStock(specId,buyCount);
                Thread.sleep(16000);
            }
        }
    }
    //支持阻塞  zookeeper实现
    private void getZookeeperLock(String specId,Integer buyCount) throws Exception {
        log.info("我进入了方法！");
        try(ZkLock zkLock = new ZkLock()) {
            if (zkLock.getLock(orderZookeeperKey)){
                log.info("我获得了锁");
                itemService.decreaseItemSpecStock(specId,buyCount);
            }
        }
        log.info("方法执行完成！");
    }

    //支持阻塞  zookeeperCurator实现
    private void getZookeeperCurator(String specId,Integer buyCount) throws Exception {
        log.info("我进入了方法！");
        InterProcessMutex lock = new InterProcessMutex(client, zookeeperCuratorPath);
        try{
            if (lock.acquire(30, TimeUnit.SECONDS)){
                log.info("我获得了锁！！");
                itemService.decreaseItemSpecStock(specId,buyCount);
                Thread.sleep(15000);
            }
        } finally {
            try {
                log.info("我释放了锁！！");
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("方法执行完成！");
    }

    private ShopCartBO getBuyCountFromRedis(List<ShopCartBO> list,String specId) {
        for(ShopCartBO bo : list){
            if(bo.getSpecId().equals(specId)){
                return bo;
            }
        }
        return null;
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

    @Transactional
    @Override
    public void closeOrder() {
        //1.查询出订单状态表中 1天内未付款的用户
        OrderStatus os = new OrderStatus();
        os.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatuses = orderStatusMapper.select(os);
        for(OrderStatus orderStatus : orderStatuses){
            Date createdTime = orderStatus.getCreatedTime();
            Date currentTime = new Date();
            int daysBetween = DateUtil.daysBetween(createdTime, currentTime);
            if(daysBetween >= 1){
                updateOrder(orderStatus.getOrderId());
            }
        }
    }

    /**
     * 根据订单id 定时任务修改订单状态为交易取消
     */
    private void updateOrder(String orderId){
        //1.订单状态表状态修改
        OrderStatus os = new OrderStatus();
        os.setOrderId(orderId);
        os.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatusMapper.updateByPrimaryKeySelective(os);
        //2.查询订单中下单的商品
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        List<OrderItems> oi = orderItemsMapper.select(orderItems);
        for(OrderItems orderItem : oi){
            //3.获取他的规格id
            int buyCounts = orderItem.getBuyCounts();
            String itemSpecId = orderItem.getItemSpecId();
            //4.去查询他的库存 更新库存
            ItemsSpec itemsSpec = itemsSpecMapper.selectByPrimaryKey(itemSpecId);
            //5.库存加上取消订单的数量
            int count =  itemsSpec.getStock() + buyCounts;
            itemsSpec.setStock(count);
            itemsSpecMapper.updateByPrimaryKey(itemsSpec);
        }
    }
}
