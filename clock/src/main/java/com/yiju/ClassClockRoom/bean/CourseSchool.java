package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * Created by Sandy on 2016/6/16/0016.
 */
public class CourseSchool implements Serializable {
    private String id;
    private String name;
    private String address;
    private String school_start_time;
    private String school_end_time;
    private String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSchool_start_time() {
        return school_start_time;
    }

    public void setSchool_start_time(String school_start_time) {
        this.school_start_time = school_start_time;
    }

    public String getSchool_end_time() {
        return school_end_time;
    }

    public void setSchool_end_time(String school_end_time) {
        this.school_end_time = school_end_time;
    }

    private String lng;
    private String lat;
    private String lng_g;
    private String lat_g;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng_g() {
        return lng_g;
    }

    public void setLng_g(String lng_g) {
        this.lng_g = lng_g;
    }

    public String getLat_g() {
        return lat_g;
    }

    public void setLat_g(String lat_g) {
        this.lat_g = lat_g;
    }
}
