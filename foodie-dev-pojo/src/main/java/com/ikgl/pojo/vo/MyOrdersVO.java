package com.ikgl.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class MyOrdersVO {
    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private Integer orderStatus;

    private List<MySubOrderItemVO> subOrderItemList;

}
