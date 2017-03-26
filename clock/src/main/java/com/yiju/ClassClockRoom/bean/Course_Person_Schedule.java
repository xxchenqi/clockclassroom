package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sandy on 2016/6/20/0020.
 */
public class Course_Person_Schedule implements Serializable {

    /**
     * type_info : [{"room_info":[{"room_id":"23","room_no":"A302","status":3,"time":"2016.06.16 (周一) 12:00-13:00"}],"type_id":34,"type_desc":"小间课室"}]
     * all_course_hour : 1
     * active_course_hour : 0
     * date_section : 2016.06.16-06.16
     */

    private int all_course_hour;
    private int active_course_hour;
    private String date_section;
    /**
     * room_info : [{"room_id":"23","room_no":"A302","status":3,"time":"2016.06.16 (周一) 12:00-13:00"}]
     * type_id : 34
     * type_desc : 小间课室
     */

//    private List<Course_Person_Detail_TypeInfo> type_info;

    private int type_id;
    private String type_desc;
    /**
     * room_id : 23
     * room_no : A302
     * status : 3
     * time : 2016.06.16 (周一) 12:00-13:00
     */

    private List<Course_Person_Detail_RoomInfo> room_info;

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public List<Course_Person_Detail_RoomInfo> getRoom_info() {
        return room_info;
    }

    public void setRoom_info(List<Course_Person_Detail_RoomInfo> room_info) {
        this.room_info = room_info;
    }
    public int getAll_course_hour() {
        return all_course_hour;
    }

    public void setAll_course_hour(int all_course_hour) {
        this.all_course_hour = all_course_hour;
    }

    public int getActive_course_hour() {
        return active_course_hour;
    }

    public void setActive_course_hour(int active_course_hour) {
        this.active_course_hour = active_course_hour;
    }

    public String getDate_section() {
        return date_section;
    }

    public void setDate_section(String date_section) {
        this.date_section = date_section;
    }

//    public List<Course_Person_Detail_TypeInfo> getType_info() {
//        return type_info;
//    }
//
//    public void setType_info(List<Course_Person_Detail_TypeInfo> type_info) {
//        this.type_info = type_info;
//    }
}
