package com.ikgl.enums;

public enum CommentLevel {
    good(1,"好评"),
    normal(2,"中评"),
    bad(3,"差评");

    public  Integer type;

    public  String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
