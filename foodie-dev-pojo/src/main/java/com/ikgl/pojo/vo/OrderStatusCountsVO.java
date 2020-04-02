package com.ikgl.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单状态概览数量VO 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusCountsVO {

    private Integer waitPayCounts;
    private Integer waitDeliverCounts;
    private Integer waitReceiveCounts;
    private Integer waitCommentCounts;

//    public OrderStatusCountsVO() {
//    }
//
//    public OrderStatusCountsVO(Integer waitPayCounts, Integer waitDeliverCounts, Integer waitReceiveCounts, Integer waitCommentCounts) {
//        this.waitPayCounts = waitPayCounts;
//        this.waitDeliverCounts = waitDeliverCounts;
//        this.waitReceiveCounts = waitReceiveCounts;
//        this.waitCommentCounts = waitCommentCounts;
//    }

}