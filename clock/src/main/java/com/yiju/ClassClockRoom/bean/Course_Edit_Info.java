package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sandy on 2016/7/5/0005.
 */
public class Course_Edit_Info implements Serializable {

    private String sid;
    private String address;
    private String start_date;
    private String end_date;
    private int start_time;
    private int end_time;
    private String repeat;
    private String school_start_time;
    private String school_end_time;
    private String name;//课程名
    private String remain_count;//招生剩余人数
    private String total_count;//招生总人数
    private String single_price;//课程单价
    private String desc;//课程简介
    private List<String> mien_pics;//课程图片
    private String teacher_uid;//上课人id
    private String teacher_name;//上课人名字
//    private Course_Need_Info course_info;//时间参数

    public String getSchool_end_time() {
        return school_end_time;
    }

    public void setSchool_end_time(String school_end_time) {
        this.school_end_time = school_end_time;
    }

    public String getSchool_start_time() {
        return school_start_time;
    }

    public void setSchool_start_time(String school_start_time) {
        this.school_start_time = school_start_time;
    }


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemain_count() {
        return remain_count;
    }

    public void setRemain_count(String remain_count) {
        this.remain_count = remain_count;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getMien_pics() {
        return mien_pics;
    }

    public void setMien_pics(List<String> mien_pics) {
        this.mien_pics = mien_pics;
    }


    public String getTeacher_uid() {
        return teacher_uid;
    }

    public void setTeacher_uid(String teacher_uid) {
        this.teacher_uid = teacher_uid;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
}
