package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sandy on 2016/6/16/0016.
 */
public class Course_DataInfo implements Serializable {
    private String id;
    private String name;//课程名
    private Course_Need_Info course_info;
    private String desc;//课程描述
    private String real_name;//授课老师名
    private String school_id;
    private String school_name;
    private String school_start_time;
    private String school_end_time;
    private String course_status;
    private String have_enroll;//已报名人数
    private String remain_count;//剩余人数
    private String single_price;//课程单价
    private String pic;
    private List<String> pics;
    private String date_section;
    private String all_course_hour;
    //    private  schedule;{}
    private String teacher_id;//授课老师id


    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public Course_Need_Info getCourse_info() {
        return course_info;
    }

    public void setCourse_info(Course_Need_Info course_info) {
        this.course_info = course_info;
    }

    public String getDate_section() {
        return date_section;
    }

    public void setDate_section(String date_section) {
        this.date_section = date_section;
    }

    public String getAll_course_hour() {
        return all_course_hour;
    }

    public void setAll_course_hour(String all_course_hour) {
        this.all_course_hour = all_course_hour;
    }

    /**
     * time_list : [{"status":3,"time":"2016.06.13 9:00-10:00"},{"status":3,"time":"2016.06.13 12:00-13:00"}]
     * date_section : 2016.06.13-06.13
     * all_course_hour : 2
     */
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

    public String getCourse_status() {
        return course_status;
    }

    public void setCourse_status(String course_status) {
        this.course_status = course_status;
    }

    public String getHave_enroll() {
        return have_enroll;
    }

    public void setHave_enroll(String have_enroll) {
        this.have_enroll = have_enroll;
    }

    public String getRemain_count() {
        return remain_count;
    }

    public void setRemain_count(String remain_count) {
        this.remain_count = remain_count;
    }

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
