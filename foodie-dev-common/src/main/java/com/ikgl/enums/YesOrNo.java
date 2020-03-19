package com.ikgl.enums;

public enum YesOrNo {

    NO(0,"否"),
    YES(1,"是");

    public  Integer type;

    public  String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
