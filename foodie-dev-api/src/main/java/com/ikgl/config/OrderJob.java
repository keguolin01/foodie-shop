package com.ikgl.config;

import com.ikgl.service.OrderService;
import com.ikgl.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    /**
     *  使用定时任务的缺点
     *  1.假设 10:40下单 11:00查询时未超过1小时  12:00已超过1小时20分了 会出现时间差
     *
     *  2.在单体可以  在集群  假设10个集群 就会执行10次  不支持集群
     *  解决方案：集群中有一节点执行就可以
     *
     *  3.在修改订单状态时，进行的是全表扫描，即使建立索引也会出现资源消耗，影响数据库性能
     *
     *  消息队列可解决上述问题
     */
//    @Scheduled(cron = "0/3 * * * * ?")
    //每2小时执行一次
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void autoCloseOrder(){
        orderService.closeOrder();
        System.out.println("开启定时任务，当前时间"+
                DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
