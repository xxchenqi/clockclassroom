package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;


public class Room implements Serializable {
    private String id;
    private String tags;
    private String name;
    private String address;
    private String lng;
    private String lat;
    private String lng_bd;
    private String lat_bd;
    private String start_time;
    private String end_time;
    private String pic_big;
    private String cate;
    private String can_schedule;//can_schedule判断 =1是可预订 =0是不可预订
    private String school_type;//1=直营店 2=合作店 3=合营店 4=加盟店
    private String instruction;
    private String confirm_type;
    private List<RoomTypeInfo> room_type;
    private List<RoomDevice> device;
    private StoreShareBean share;

    public StoreShareBean getShare() {
        return share;
    }

    public void setShare(StoreShareBean share) {
        this.share = share;
    }

    public String getCan_schedule() {
        return can_schedule;
    }

    public String getConfirm_type() {
        return confirm_type;
    }

    public String getSchool_type() {
        return school_type;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getLng_bd() {
        return lng_bd;
    }

    public void setLng_bd(String lng_bd) {
        this.lng_bd = lng_bd;
    }

    public String getLat_bd() {
        return lat_bd;
    }

    public void setLat_bd(String lat_bd) {
        this.lat_bd = lat_bd;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public List<RoomDevice> getDevice() {
        return device;
    }

    public void setDevice(List<RoomDevice> device) {
        this.device = device;
    }

    public List<RoomTypeInfo> getRoom_type() {
        return room_type;
    }

    public void setRoom_type(List<RoomTypeInfo> room_type) {
        this.room_type = room_type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getId() {
        return id;
    }

    public String getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }

    public String getPic_big() {
        return pic_big;
    }


}
