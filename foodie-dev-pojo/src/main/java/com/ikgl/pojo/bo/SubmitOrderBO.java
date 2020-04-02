package com.ikgl.pojo.bo;

import lombok.Data;

@Data
public class SubmitOrderBO {

    private String userId;

    private String itemSpecIds;

    private String addressId;

    private Integer payMethod;

    private String leftMsg;
}
