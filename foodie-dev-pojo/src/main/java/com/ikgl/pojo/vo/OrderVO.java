package com.ikgl.pojo.vo;

import com.ikgl.pojo.bo.ShopCartBO;
import lombok.Data;

import java.util.List;
@Data
public class OrderVO {

    private String orderId;

    private MerchantOrdersVO merchantOrdersVO;

    private List<ShopCartBO> toBeRemovedShopCartList;

}
