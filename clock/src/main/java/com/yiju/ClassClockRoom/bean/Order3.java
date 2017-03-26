package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/5/18 10:27
 * ----------------------------------------
 */
public class Order3 implements Serializable {
    private String id;
    private String pid;
    private String room_id;
    private String room_no;
    private String date;
    private String start_time;
    private String end_time;
    private String single_fee;
    private String fee;
    private String real_fee;
    private String is_refund;
    private String video_pas;
    private String is_weekend;
    private String commission;
    private String is_sign;
    private String sign_user;
    private String sign_mobile;
    private String sign_time;
    private String signf_time;
    private String sign_remark;
    private String coupon_id;
    private String video_id;
    private String device_refund;
    private List<ClassCourseSchedule> course_schedule;
    private boolean course_flag;

    public boolean isCourse_flag() {
        return course_flag;
    }

    public void setCourse_flag(boolean course_flag) {
        this.course_flag = course_flag;
    }

    public List<ClassCourseSchedule> getCourse_schedule() {
        return course_schedule;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getDevice_refund() {
        return device_refund;
    }

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getRoom_no() {
        return room_no;
    }

    public String getDate() {
        return date;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getSingle_fee() {
        return single_fee;
    }

    public String getFee() {
        return fee;
    }

    public String getReal_fee() {
        return real_fee;
    }

    public String getIs_refund() {
        return is_refund;
    }

    public String getVideo_pas() {
        return video_pas;
    }

    public String getIs_weekend() {
        return is_weekend;
    }

    public String getCommission() {
        return commission;
    }

    public String getIs_sign() {
        return is_sign;
    }

    public String getSign_user() {
        return sign_user;
    }

    public String getSign_mobile() {
        return sign_mobile;
    }

    public String getSign_time() {
        return sign_time;
    }

    public String getSignf_time() {
        return signf_time;
    }

    public String getSign_remark() {
        return sign_remark;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

}