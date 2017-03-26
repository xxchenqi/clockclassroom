package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 *
 * Created by wh on 2016/6/22.
 */
public class OrderCourseResult {

    private String code;
    private String msg;
    private int server_time;
    private List<OrderCourseData> data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<OrderCourseData> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<OrderCourseData> getData() {
        return data;
    }

    public int getServertime() {
        return server_time;
    }

    public void setServertime(int server_time) {
        this.server_time = server_time;
    }
}
