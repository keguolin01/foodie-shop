package com.ikgl.enums;

public enum Sex {
    woman(0,"女"),
    man(1,"男"),
    secret(2,"保密");

    public  Integer type;

    public  String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
