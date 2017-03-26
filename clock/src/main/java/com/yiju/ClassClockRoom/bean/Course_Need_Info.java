package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sandy on 2016/7/5/0005.
 */
public class Course_Need_Info implements Serializable {


    /**
     * start_date : 2016-07-29
     * end_date : 2016-08-01
     * repeat : 1,2,3,4,5,6,7
     * start_time : 800
     * end_time : 1000
     * adjust : [{"date":"2016-07-29","room_in":[{"start_time":900,"end_time":1000},{"start_time":1000,"end_time":1100}]},{"date":"2016-07-30","room_in":[{"start_time":900,"end_time":1000},{"start_time":1000,"end_time":1100}]}]
     */

    private String start_date;
    private String end_date;
    private String repeat;
    private int start_time;
    private int end_time;
    /**
     * date : 2016-07-29
     * room_in : [{"start_time":900,"end_time":1000},{"start_time":1000,"end_time":1100}]
     */

    private List<AdjustBean> adjust;

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public List<AdjustBean> getAdjust() {
        return adjust;
    }

    public void setAdjust(List<AdjustBean> adjust) {
        this.adjust = adjust;
    }

    public static class AdjustBean implements Serializable{
        private String date;
        /**
         * start_time : 900
         * end_time : 1000
         */

        private List<RoomInBean> room_in;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<RoomInBean> getRoom_in() {
            return room_in;
        }

        public void setRoom_in(List<RoomInBean> room_in) {
            this.room_in = room_in;
        }

        public static class RoomInBean implements Serializable{
            private int start_time;
            private int end_time;

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
            }
        }
    }
}
