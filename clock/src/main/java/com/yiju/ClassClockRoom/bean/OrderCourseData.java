package com.yiju.ClassClockRoom.bean;

/**
 * 课程订单列表data_module
 * Created by wh on 2016/6/22.
 */
public class OrderCourseData {
    /**
     * course_name : 数学1
     * school_name : 测试徐汇虹梅商务大厦旗舰店
     * start_date : 2016-06-17
     * end_date : 2016-06-18
     * course_num : 2
     * status : 0
     * expire_time : 1565810848
     * real_fee : 100.00
     */
    private String id;
    private String course_id;
    private String course_name;
    private String school_name;
    private String start_date;
    private String end_date;
    private int course_num;
    private String status;
    private String expire_time;
    private String real_fee;
    private String pic;
    private String course_str;
    private String sname;
    private String school_type;
    private String trade_id;
    private String ctype;
    private String pay_method;

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    private int time;

    public String getCtype() {
        return ctype;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public String getId() {
        return id;
    }

    public String getSchool_type() {
        return school_type;
    }

    public String getSname() {
        return sname;
    }

    public String getCourse_str() {
        return course_str;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setCourse_num(int course_num) {
        this.course_num = course_num;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public void setReal_fee(String real_fee) {
        this.real_fee = real_fee;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getSchool_name() {
        return school_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public int getCourse_num() {
        return course_num;
    }

    public String getStatus() {
        return status;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public String getReal_fee() {
        return real_fee;
    }
}
