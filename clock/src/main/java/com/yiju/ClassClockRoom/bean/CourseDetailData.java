package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sandy on 2016/6/17/0017.
 */
public class CourseDetailData implements Serializable {
    private String id;//课程id
    private String name;//课程名字
    private String ctype;//1是体验课 0 是正式课
    private String status;//状态
    private String sp_id;//供应商id
    private String sp_type;//供应商类型1=个人 2=企业
    private String log;//老师或者机构头像
    private String sp_name;//供应商名字
    private String one_desc;//一句话简介
    private String total_count;//报名总人数
    private String have_enroll;//没用
    private String have_enroll_false;//已报名人数
    private String sid;//门店id
    private String school_name;//门店名字
    private String address;//门店地址
    private String course_str;//课程报名时间
    private String desc;//课程简介
    private String total_price;//课程价格
    private String can_enroll;//1是能报名 0是不能报名
    private List<String> pics;//精彩图集
    private List<RelationData> relation;//关联课程
    private String is_interest;//是否关注 0-未关注    1-已关注
    private String mapURL;//地图
    private String sp_sex;//性别 1-男 2-女 0-未填
    private String finish_enroll;//0是未完成报名 1是已完成报名
    private ContactTelName contact;

    //没用
    private String single_price;
    private String remain_count;
    private CourseTeacher teacher;
    private CourseSchool school;
    private CourseSchedule schedule;

    public ContactTelName getContact() {
        return contact;
    }

    public String getFinish_enroll() {
        return finish_enroll;
    }

    public String getSingle_price() {
        return single_price;
    }

    public String getRemain_count() {
        return remain_count;
    }

    public String getSp_sex() {
        return sp_sex;
    }

    public String getHave_enroll_false() {
        return have_enroll_false;
    }
    public String getMapURL() {
        return mapURL;
    }
    public String getSp_id() {
        return sp_id;
    }

    public String getSp_type() {
        return sp_type;
    }

    public String getLog() {
        return log;
    }

    public String getSp_name() {
        return sp_name;
    }

    public String getOne_desc() {
        return one_desc;
    }

    public List<RelationData> getRelation() {
        return relation;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCtype() {
        return ctype;
    }

    public String getStatus() {
        return status;
    }

    public String getTotal_count() {
        return total_count;
    }

    public String getHave_enroll() {
        return have_enroll;
    }

    public String getSid() {
        return sid;
    }

    public String getSchool_name() {
        return school_name;
    }

    public String getAddress() {
        return address;
    }

    public String getCourse_str() {
        return course_str;
    }

    public String getDesc() {
        return desc;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getCan_enroll() {
        return can_enroll;
    }

    public List<String> getPics() {
        return pics;
    }

    public String getIs_interest() {
        return is_interest;
    }

    public CourseTeacher getTeacher() {
        return teacher;
    }

    public CourseSchool getSchool() {
        return school;
    }

    public CourseSchedule getSchedule() {
        return schedule;
    }

    public void setIs_interest(String is_interest) {
        this.is_interest = is_interest;
    }
}
