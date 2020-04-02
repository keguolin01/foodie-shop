package com.ikgl.pojo.bo;

import lombok.Data;

@Data
public class ShopCartBO {
    private String itemId;

    private String itemImgUrl;

    private String itemName;

    private String specId;

    private String specName;

    private Integer buyCounts;

    private String priceDiscount;

    private String priceNormal;
}
