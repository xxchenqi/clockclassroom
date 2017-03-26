package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 预订失败 2015/12/16/0016.
 */
public class ReservationFail implements Serializable{


    /**
     * code : 40050
     * msg : 房间库存不足
     * data : {"flag":0,"arr":[{"date":"2016-05-17","room_count":"7","room_id":["112","127","128","199","200","201","202"],"start_time":"0900 ","end_time":" 2100","end_time_new":"2115","miss_count":0},{"date":"2016-05-18","room_count":"7","room_id":["127","128","199","200","201","202"],"start_time":"0900 ","end_time":" 2100","end_time_new":"2115","miss_count":1}]}
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
     * flag : 0
     * arr : [{"date":"2016-05-17","room_count":"7","room_id":["112","127","128","199","200","201","202"],"start_time":"0900 ","end_time":" 2100","end_time_new":"2115","miss_count":0},{"date":"2016-05-18","room_count":"7","room_id":["127","128","199","200","201","202"],"start_time":"0900 ","end_time":" 2100","end_time_new":"2115","miss_count":1}]
     */

    private DataEntity data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity implements Serializable{
        private int flag;
        /**
         * date : 2016-05-17
         * room_count : 7
         * room_id : ["112","127","128","199","200","201","202"]
         * start_time : 0900
         * end_time :  2100
         * end_time_new : 2115
         * miss_count : 0
         */

        private List<ArrEntity> arr;

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public void setArr(List<ArrEntity> arr) {
            this.arr = arr;
        }

        public int getFlag() {
            return flag;
        }

        public List<ArrEntity> getArr() {
            return arr;
        }

        public static class ArrEntity implements Serializable{
            private String date;
            private String room_count;
            private String start_time;
            private String end_time;
            private String end_time_new;
            private int miss_count;
            private List<String> room_id;

            public void setDate(String date) {
                this.date = date;
            }

            public void setRoom_count(String room_count) {
                this.room_count = room_count;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public void setEnd_time_new(String end_time_new) {
                this.end_time_new = end_time_new;
            }

            public void setMiss_count(int miss_count) {
                this.miss_count = miss_count;
            }

            public void setRoom_id(List<String> room_id) {
                this.room_id = room_id;
            }

            public String getDate() {
                return date;
            }

            public String getRoom_count() {
                return room_count;
            }

            public String getStart_time() {
                return start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public String getEnd_time_new() {
                return end_time_new;
            }

            public int getMiss_count() {
                return miss_count;
            }

            public List<String> getRoom_id() {
                return room_id;
            }
        }
    }
}
