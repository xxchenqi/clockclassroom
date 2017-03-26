package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

public class RoomTypeInfo implements Serializable {
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    private String id;
    private String desc;
    private String attr;
    private String area;
    private String desc_app;
    private String price_weekday;
    private String price_weekend;
    private String max_member;
    private String is_meeting;
    private String pic_small;
    private boolean check;

    public void setArea(String area) {
        this.area = area;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDesc_app(String desc_app) {
        this.desc_app = desc_app;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public void setPrice_weekday(String price_weekday) {
        this.price_weekday = price_weekday;
    }

    public void setPrice_weekend(String price_weekend) {
        this.price_weekend = price_weekend;
    }

    public String getArea() {
        return area;
    }

    public String getDesc() {
        return desc;
    }

    public String getDesc_app() {
        return desc_app;
    }

    public String getId() {
        return id;
    }

    public String getPic_small() {
        return pic_small;
    }

    public String getPrice_weekday() {
        return price_weekday;
    }

    public String getPrice_weekend() {
        return price_weekend;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getMax_member() {
        return max_member;
    }

    public void setMax_member(String max_member) {
        this.max_member = max_member;
    }

    public String getIs_meeting() {
        return is_meeting;
    }

    public void setIs_meeting(String is_meeting) {
        this.is_meeting = is_meeting;
    }
}
