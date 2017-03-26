package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * 调整日期 ：取消或添加的日期
 * Created by wh on 2016/5/10.
 */
public class AdjustmentData implements Serializable{

    private String date;
    private String start_time;
    private String end_time;
    private String room_count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRoom_count() {
        return room_count;
    }

    public void setRoom_count(String room_count) {
        this.room_count = room_count;
    }
}
