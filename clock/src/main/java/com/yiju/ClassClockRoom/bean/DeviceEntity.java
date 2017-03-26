package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

public  class DeviceEntity implements Serializable {
    private String device_id;
    private String device_name;
    private String fee;
    private String id;
    private String is_refund;
    private String num;
    private String oid;
    private String poid;
    private String single_fee;
    private String unit;
    private String name;

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIs_refund(String is_refund) {
        this.is_refund = is_refund;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setPoid(String poid) {
        this.poid = poid;
    }

    public void setSingle_fee(String single_fee) {
        this.single_fee = single_fee;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public String getFee() {
        return fee;
    }

    public String getId() {
        return id;
    }

    public String getIs_refund() {
        return is_refund;
    }

    public String getNum() {
        return num;
    }

    public String getOid() {
        return oid;
    }

    public String getPoid() {
        return poid;
    }

    public String getSingle_fee() {
        return single_fee;
    }
}
