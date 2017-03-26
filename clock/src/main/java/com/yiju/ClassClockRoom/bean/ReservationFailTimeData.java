package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

public class ReservationFailTimeData implements Serializable{


    /**
     * code : 1
     * data_after : {"arr":[{"date":"2016-05-21","end_time":"2200","end_time_new":"2215","miss_count":1,"room_count":"5","room_id":["77","203","204","206"],"start_time":"1000"}],"end_time":0,"flag":0,"start_time":0}
     * data_pre : {"arr":[{"date":"2016-05-21","end_time":"2000","end_time_new":"2015","miss_count":1,"room_count":"5","room_id":["77","203","204","206"],"start_time":"0800"}],"end_time":0,"flag":0,"start_time":0}
     * msg : ok
     */

    private Integer code;
    /**
     * arr : [{"date":"2016-05-21","end_time":"2200","end_time_new":"2215","miss_count":1,"room_count":"5","room_id":["77","203","204","206"],"start_time":"1000"}]
     * end_time : 0
     * flag : 0
     * start_time : 0
     */

    private DataAfterEntity data_after;
    /**
     * arr : [{"date":"2016-05-21","end_time":"2000","end_time_new":"2015","miss_count":1,"room_count":"5","room_id":["77","203","204","206"],"start_time":"0800"}]
     * end_time : 0
     * flag : 0
     * start_time : 0
     */

    private DataPreEntity data_pre;
    private String msg;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData_after(DataAfterEntity data_after) {
        this.data_after = data_after;
    }

    public void setData_pre(DataPreEntity data_pre) {
        this.data_pre = data_pre;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public DataAfterEntity getData_after() {
        return data_after;
    }

    public DataPreEntity getData_pre() {
        return data_pre;
    }

    public String getMsg() {
        return msg;
    }

    public static class DataAfterEntity implements Serializable{
        private String end_time;
        private int flag;
        private String start_time;
        /**
         * date : 2016-05-21
         * end_time : 2200
         * end_time_new : 2215
         * miss_count : 1
         * room_count : 5
         * room_id : ["77","203","204","206"]
         * start_time : 1000
         */

        private List<ArrEntity> arr;

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public void setArr(List<ArrEntity> arr) {
            this.arr = arr;
        }

        public String getEnd_time() {
            return end_time;
        }

        public int getFlag() {
            return flag;
        }

        public String getStart_time() {
            return start_time;
        }

        public List<ArrEntity> getArr() {
            return arr;
        }

        public static class ArrEntity {
            private String date;
            private String end_time;
            private String end_time_new;
            private int miss_count;
            private String room_count;
            private String start_time;
            private List<String> room_id;

            public void setDate(String date) {
                this.date = date;
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

            public void setRoom_count(String room_count) {
                this.room_count = room_count;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public void setRoom_id(List<String> room_id) {
                this.room_id = room_id;
            }

            public String getDate() {
                return date;
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

            public String getRoom_count() {
                return room_count;
            }

            public String getStart_time() {
                return start_time;
            }

            public List<String> getRoom_id() {
                return room_id;
            }
        }
    }

    public static class DataPreEntity implements Serializable{
        private String end_time;
        private int flag;
        private String start_time;
        /**
         * date : 2016-05-21
         * end_time : 2000
         * end_time_new : 2015
         * miss_count : 1
         * room_count : 5
         * room_id : ["77","203","204","206"]
         * start_time : 0800
         */

        private List<ArrEntity> arr;

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public void setArr(List<ArrEntity> arr) {
            this.arr = arr;
        }

        public String getEnd_time() {
            return end_time;
        }

        public int getFlag() {
            return flag;
        }

        public String getStart_time() {
            return start_time;
        }

        public List<ArrEntity> getArr() {
            return arr;
        }

        public static class ArrEntity implements Serializable{
            private String date;
            private String end_time;
            private String end_time_new;
            private int miss_count;
            private String room_count;
            private String start_time;
            private List<String> room_id;

            public void setDate(String date) {
                this.date = date;
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

            public void setRoom_count(String room_count) {
                this.room_count = room_count;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public void setRoom_id(List<String> room_id) {
                this.room_id = room_id;
            }

            public String getDate() {
                return date;
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

            public String getRoom_count() {
                return room_count;
            }

            public String getStart_time() {
                return start_time;
            }

            public List<String> getRoom_id() {
                return room_id;
            }
        }
    }
}
