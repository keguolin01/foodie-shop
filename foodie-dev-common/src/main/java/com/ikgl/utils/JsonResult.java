package com.ikgl.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResult {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 状态
     */
    private Integer status;

    /**
     * 数据
     */
    private Object data;

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public Integer getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 返回的信息
     */
    private String msg;

    public JsonResult(Integer status, Object data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public JsonResult() {
        this.status = 200;
        this.data = data;
        this.msg = "";
    }

    public static JsonResult build(Integer status, Object data, String msg){
        return new JsonResult(status,data,msg);
    }
    public static JsonResult ok(){
        return new JsonResult();
    }
    public static JsonResult errorMsg(String msg){
        return new JsonResult(500,null,msg);
    }
}
