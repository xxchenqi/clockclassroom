package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * ----------------------------------------
 * 注释:课程表
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/6/21 16:21
 * ----------------------------------------
 */
public class ClassCourseSchedule implements Serializable {

    /**
     * id : 8771
     * course_id : 0
     * date : 2016-07-07
     * start_time : 1000
     * end_time : 1100
     * room_id : 23
     * uid : 2
     * order3_id : 33210
     */

    private String id;
    private String course_id;
    private String date;
    private String start_time;
    private String end_time;
    private String room_id;
    private String uid;
    private String order3_id;

    public void setId(String id) {
        this.id = id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setOrder3_id(String order3_id) {
        this.order3_id = order3_id;
    }

    public String getId() {
        return id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getDate() {
        return date;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getUid() {
        return uid;
    }

    public String getOrder3_id() {
        return order3_id;
    }
}
