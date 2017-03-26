package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.ContactTelName;

import java.io.Serializable;

/**
 * Created by Sandy on 2016/6/23/0023.
 */
public class CourseApplyResult extends BaseEntity implements Serializable {


    /**
     * code : 1
     * msg : 报名成功
     * order2_id : 49
     * single_price : 50.00
     * total_hours : 1
     * total_fee : 50
     * course_name : 语文1
     * teacher_name : aaaaa
     * course_pic : http://get.file.dc.cric.com/SZJS08f4ee30e17c77303a928c7a45d8dbfb_196X246_0_0_0.jpg
     * sname : 测试徐汇虹梅商务大厦旗舰店
     * contact : {"id":"232","name":"zz","mobile":"18516757557"}
     */

    private int order2_id;
    private String single_price;
    private int total_hours;
    private int total_fee;
    private String course_name;
    private String teacher_name;
    private String course_pic;
    private String sname;
    /**
     * id : 232
     * name : zz
     * mobile : 18516757557
     */

    private ContactTelName contact;

    public int getOrder2_id() {
        return order2_id;
    }

    public void setOrder2_id(int order2_id) {
        this.order2_id = order2_id;
    }

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }

    public int getTotal_hours() {
        return total_hours;
    }

    public void setTotal_hours(int total_hours) {
        this.total_hours = total_hours;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getCourse_pic() {
        return course_pic;
    }

    public void setCourse_pic(String course_pic) {
        this.course_pic = course_pic;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public ContactTelName getContact() {
        return contact;
    }

    public void setContact(ContactTelName contact) {
        this.contact = contact;
    }

}
