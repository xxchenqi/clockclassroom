package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * Created by Sandy on 2016/6/20/0020.
 */
public class Course_Person_Detail_RoomInfo implements Serializable {
    private String room_id;
    private String room_no;
    private int status;
    private String time;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
