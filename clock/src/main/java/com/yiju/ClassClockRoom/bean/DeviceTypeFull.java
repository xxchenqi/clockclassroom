package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

public class DeviceTypeFull implements Serializable {


    /**
     * code : 40051
     * msg : 收费设备库存不足
     * stock_arr : [{"date":"2016-05-17","start_time":"0900 ","end_time":" 2100"},{"date":"2016-05-18","start_time":"0900 ","end_time":" 2100"}]
     */

    private Integer code;
    private String msg;
    private String type_desc;

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    /**
     * date : 2016-05-17
     * start_time : 0900
     * end_time :  2100
     */

    private List<StockArrEntity> stock_arr;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStock_arr(List<StockArrEntity> stock_arr) {
        this.stock_arr = stock_arr;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<StockArrEntity> getStock_arr() {
        return stock_arr;
    }

    public static class StockArrEntity implements Serializable{
        private String date;
        private String start_time;
        private String end_time;
        private Integer room_count;
        private Integer room_available;

        public Integer getStock_available() {
            return room_available;
        }

        public void setStock_available(Integer room_available) {
            this.room_available = room_available;
        }

        public Integer getRoom_count() {
            return room_count;
        }

        public void setRoom_count(Integer room_count) {
            this.room_count = room_count;
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

        public String getDate() {
            return date;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

    }
}
