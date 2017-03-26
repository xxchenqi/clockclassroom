package com.yiju.ClassClockRoom.bean;

import java.util.List;

/*
 * Created by wh on 2016/3/10.
 */
public class IndexAds {
    private Integer code;
    private String msg;
    private List<AdData> data;

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

    public List<AdData> getData() {
        return data;
    }

    public void setData(List<AdData> data) {
        this.data = data;
    }
}
