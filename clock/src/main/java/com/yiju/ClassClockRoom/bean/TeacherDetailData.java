package com.yiju.ClassClockRoom.bean;


import java.util.List;

/**
 * ----------------------------------------
 * 注释:老师详情Data
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/6/23 10:38
 * ----------------------------------------
 */
public class TeacherDetailData {

    private String id;
    private String avatar;
    private String real_name;
    private String show_teacher;
    private String tags;
    private String info;
    private List<CourseInfo> course;
    private List<String> pic;
    private String org_name;
    private String is_interest;     //是否关注      0-未关注    1-已关注
    private String sex;             //性别        0-不显示   1-男     2-女

    public String getIsComplete() {
        return isComplete;
    }

    private String isComplete;      //老师完善    0-不显示   1-男     2-女

    public String getOrg_name() {
        return org_name;
    }

    public List<String> getPic() {
        return pic;
    }

    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getReal_name() {
        return real_name;
    }

    public String getShow_teacher() {
        return show_teacher;
    }

    public String getTags() {
        return tags;
    }

    public String getInfo() {
        return info;
    }

    public List<CourseInfo> getCourse() {
        return course;
    }

    public String getIs_interest() {
        return is_interest;
    }

    public void setIs_interest(String is_interest) {
        this.is_interest = is_interest;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
