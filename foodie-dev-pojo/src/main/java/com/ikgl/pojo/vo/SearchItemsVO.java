package com.ikgl.pojo.vo;

import lombok.Data;

@Data
public class SearchItemsVO {
    private String itemName;

    private String itemId;

    private int sellCounts;

    private String imgUrl;

    private int price;

}
