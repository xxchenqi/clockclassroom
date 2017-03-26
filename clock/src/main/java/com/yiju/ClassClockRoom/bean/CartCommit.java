package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * 购物车提交成功 2015/12/11/0011.
 */
public class CartCommit implements Serializable{

    /**
     * code : 1
     * msg : ok
     * order1_id : 489
     * room_count : 2
     * type_desc : 大间
     * use_desc : 数学
     */

    private Integer code;
    private String msg;
    private int order1_id;
    private int room_count;
    private String type_desc;
    private String use_desc;
    private String trade_id;//易支付新增字段
    private String confirm_type;//确认类型 0=无需确认 1=支付前确认 2=支付后确认 0,2跳转收银台 1跳转待确认

    public String getConfirm_type() {
        return confirm_type;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setOrder1_id(int order1_id) {
        this.order1_id = order1_id;
    }

    public void setRoom_count(int room_count) {
        this.room_count = room_count;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public void setUse_desc(String use_desc) {
        this.use_desc = use_desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getOrder1_id() {
        return order1_id;
    }

    public int getRoom_count() {
        return room_count;
    }

    public String getType_desc() {
        return type_desc;
    }

    public String getUse_desc() {
        return use_desc;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }
}
