package com.ikgl.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVO {

    private String id;

    private String name;

    private Integer type;

    private Integer fatherId;

    private List<SubCategoryVO> subCatList;

}
