package com.yiju.ClassClockRoom.bean;

import java.util.List;

public class RoomInfo {


    /**
     * date : 2016-06-01
     * room_in : [{"device_add":{"8":1,"9":1},"end_time":1000,"start_time":900},{"device_add":{"8":2,"9":2},"end_time":1100,"start_time":1000}]
     */

    private String date;
    /**
     * device_add : {"8":1,"9":1}
     * end_time : 1000
     * start_time : 900
     */

    private List<RoomInEntity> room_in;

    public void setDate(String date) {
        this.date = date;
    }

    public void setRoom_in(List<RoomInEntity> room_in) {
        this.room_in = room_in;
    }

    public String getDate() {
        return date;
    }

    public List<RoomInEntity> getRoom_in() {
        return room_in;
    }

    public static class RoomInEntity {
        private String end_time;
        private String start_time;

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getStart_time() {
            return start_time;
        }
    }
}
