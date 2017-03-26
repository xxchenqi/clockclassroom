package com.yiju.ClassClockRoom.bean;


public class CartStatus {
    private Integer code;
    private String msg;
    private Integer data;// 0：不显示购物车 1：显示购物车

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}
