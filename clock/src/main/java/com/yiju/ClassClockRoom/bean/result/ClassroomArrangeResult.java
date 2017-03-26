package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.ClassroomArrangeData;
import com.yiju.ClassClockRoom.bean.StudentType;

import java.util.ArrayList;

/**
 * 课室布置接口返回值bean类
 * Created by wh on 2016/5/13.
 */
public class ClassroomArrangeResult {

    private String code;
    private String msg;
    private ArrayList<ClassroomArrangeData> data;
    private int is_meeting;
    private ArrayList<StudentType> student_type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<ClassroomArrangeData> getData() {
        return data;
    }

    public void setData(ArrayList<ClassroomArrangeData> data) {
        this.data = data;
    }

    public int getIs_meeting() {
        return is_meeting;
    }

    public void setIs_meeting(int is_meeting) {
        this.is_meeting = is_meeting;
    }

    public ArrayList<StudentType> getStudent_type() {
        return student_type;
    }

    public void setStudent_type(ArrayList<StudentType> student_type) {
        this.student_type = student_type;
    }
}
