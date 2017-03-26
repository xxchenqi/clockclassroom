package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sandy on 2016/6/16/0016.
 */
public class CourseTeacher implements Serializable {
    private String id;
    private String teacher_id;

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    private String real_name;
    private String org_name;
    private String show_teacher;
    private String mobile;
    private String org_auth;
    private String tags;
    private String course_info;
    private String sex;

    public String getAvatar() {
        return avatar != null ? avatar.replaceAll("\\d{1,3}X\\d{1,3}", "350X350") : "";
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String avatar;
    private List<String> pics;

    public String getOrg_auth() {
        return org_auth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getShow_teacher() {
        return show_teacher;
    }

    public void setShow_teacher(String show_teacher) {
        this.show_teacher = show_teacher;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCourse_info() {
        return course_info;
    }

    public void setCourse_info(String course_info) {
        this.course_info = course_info;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
