package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * Created by Sandy on 2016/6/15/0015.
 */
public class TeacherInfoBean implements Serializable {
    private String id;
    private String name;
    private String avatar;
    private String tags;
    private String sex;
    private String org_name;
    private String course_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar != null ? avatar.replaceAll("\\d{1,3}X\\d{1,3}", "350X350") : "";
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getCourse_info() {
        return course_info;
    }

    public void setCourse_info(String course_info) {
        this.course_info = course_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
