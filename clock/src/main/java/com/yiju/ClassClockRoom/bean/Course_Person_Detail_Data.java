package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sandy on 2016/6/16/0016.
 */
public class Course_Person_Detail_Data implements Serializable {
    private String id;
    private String name;
    private String single_price;
    private String old_price;
    private String course_type;
    private String remain_count;
    private String have_enroll;
    private String total_count;
    private String desc;
    private String auth_reason;//审核未通过原因
    private String update_time;
//    private String teacher_id;//授课老师id

    /**
     * all_course_hour : 0
     * active_course_hour : 0
     * date_section : 1970.01.01-01.01
     */

    private Course_Need_Info course_info;
    private Course_Person_Schedule schedule;
    private String course_status;
    /**
     * id : 30
     * real_name :
     * org_name : 陈奇奇
     * tags : 语文,能力,全科,太可怜了,门口了,all 里
     * pics : ["http://get.file.dc.cric.com/SZJS00425f290b95426709858a63af3eaa69_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5e886a31f39dc8542fdf51e380e669e3_320X240_0_0_0.jpg"]
     */

    private CourseTeacher teacher;
    /**
     * id : 9
     * name : 测试普陀友通数码中心店
     * address : 大渡河路1517号友通数码港
     * lng : 121.40399900869
     * lat : 31.25126585574
     * lng_g : 121.39741565980
     * lat_g : 31.24591350460
     */

    private CourseSchool school;

    private List<String> pics;

    private List<String> avatar;

    public String getHave_enroll() {
        return have_enroll;
    }

    public void setHave_enroll(String have_enroll) {
        this.have_enroll = have_enroll;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Course_Need_Info getCourse_info() {
        return course_info;
    }

    public void setCourse_info(Course_Need_Info course_info) {
        this.course_info = course_info;
    }

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

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Course_Person_Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Course_Person_Schedule schedule) {
        this.schedule = schedule;
    }

    public String getCourse_status() {
        return course_status;
    }

    public void setCourse_status(String course_status) {
        this.course_status = course_status;
    }

    public CourseTeacher getTeacher() {
        return teacher;
    }

    public void setTeacher(CourseTeacher teacher) {
        this.teacher = teacher;
    }

    public CourseSchool getSchool() {
        return school;
    }

    public void setSchool(CourseSchool school) {
        this.school = school;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getAvatar() {
        return avatar;
    }

    public void setAvatar(List<String> avatar) {
        this.avatar = avatar;
    }

    public String getAuth_reason() {
        return auth_reason;
    }

    public void setAuth_reason(String auth_reason) {
        this.auth_reason = auth_reason;
    }
}
