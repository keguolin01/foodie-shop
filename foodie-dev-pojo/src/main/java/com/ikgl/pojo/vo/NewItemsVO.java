package com.ikgl.pojo.vo;

import lombok.Data;

import java.util.List;
@Data
public class NewItemsVO {

    private String rootCatId;

    private String rootCatName;

    private String slogan;

    private String catImage;

    private String bgColor;

    List<SimpleItemVO> simpleItemList;

}
