package com.ikgl.service.center;

import com.ikgl.pojo.OrderItems;
import com.ikgl.pojo.bo.center.OrderItemsCommentBO;
import com.ikgl.utils.PagedGridResult;

import java.util.List;

public interface MyCommentsService {

    /**
     * 点击评价商品跳转评价商品的信息
     * @param orderId
     * @return
     */
    public List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存评价
     * @param userId
     * @param orderId
     * @param orderItemsCommentBOS
     */
    public void saveComments(String userId, String orderId, List<OrderItemsCommentBO> orderItemsCommentBOS);

    /**
     * 查询自己评价列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyContent(String userId, Integer page, Integer pageSize);

}
