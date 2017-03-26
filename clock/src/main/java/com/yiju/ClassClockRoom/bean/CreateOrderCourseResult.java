package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 *
 * Created by wh on 2016/6/23.
 */
public class CreateOrderCourseResult implements Serializable{


    /**
     * code : 1
     * msg : 生成主订单成功
     * order1_id : 2
     */

    private String code;
    private String msg;
    private int order1_id;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setOrder1_id(int order1_id) {
        this.order1_id = order1_id;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getOrder1_id() {
        return order1_id;
    }
}
