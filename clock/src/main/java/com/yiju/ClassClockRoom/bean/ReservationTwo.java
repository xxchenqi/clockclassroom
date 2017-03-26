package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 预订第一页的数据2015/11/27/0027.
 */
public class ReservationTwo {

    /**
     * code : 1
     * msg : ok
     * data : [{"desc":"迷你间","price_weekday":"32.00","price_weekend":"40.00","use":"数学","type_id":"20","room":[{"id":"77","no":"205"}]},{"desc":"小间","price_weekday":"36.00","price_weekend":"68.00","use":"数学","type_id":"21","room":[{"id":"76","no":"203"},{"id":"91","no":"270"},{"id":"100","no":"230"},{"id":"90","no":"269"},{"id":"92","no":"271"},{"id":"101","no":"229"}]},{"desc":"会议室","price_weekday":"50.00","price_weekend":"50.00","use":"数学","type_id":"25","room":[{"id":"108","no":"会议室"}]},{"desc":"中间","price_weekday":"56.00","price_weekend":"108.00","use":"数学","type_id":"22","room":[{"id":"75","no":"201"},{"id":"84","no":"216"},{"id":"83","no":"213"},{"id":"104","no":"223"}]},{"desc":"大间","price_weekday":"72.00","price_weekend":"136.00","use":"数学","type_id":"23","room":[{"id":"103","no":"226"},{"id":"87","no":"256"},{"id":"89","no":"268"},{"id":"102","no":"228"},{"id":"88","no":"266"},{"id":"93","no":"272"}]},{"desc":"大会议室","price_weekday":"200.00","price_weekend":"200.00","use":"数学","type_id":"24","room":[{"id":"105","no":"218"}]}]
     */

    private Integer code;
    private String msg;
    /**
     * desc : 迷你间
     * price_weekday : 32.00
     * price_weekend : 40.00
     * use : 数学
     * type_id : 20
     * room : [{"id":"77","no":"205"}]
     */

    private List<DataEntity> data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity implements Serializable{
        private String desc;
        private String price_weekday;
        private String price_weekend;
        private String use;
        private String type_id;
        /**
         * id : 77
         * no : 205
         */

        private List<RoomEntity> room;

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setPrice_weekday(String price_weekday) {
            this.price_weekday = price_weekday;
        }

        public void setPrice_weekend(String price_weekend) {
            this.price_weekend = price_weekend;
        }

        public void setUse(String use) {
            this.use = use;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public void setRoom(List<RoomEntity> room) {
            this.room = room;
        }

        public String getDesc() {
            return desc;
        }

        public String getPrice_weekday() {
            return price_weekday;
        }

        public String getPrice_weekend() {
            return price_weekend;
        }

        public String getUse() {
            return use;
        }

        public String getType_id() {
            return type_id;
        }

        public List<RoomEntity> getRoom() {
            return room;
        }

        public static class RoomEntity {
            private String id;
            private String no;

            public void setId(String id) {
                this.id = id;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getId() {
                return id;
            }

            public String getNo() {
                return no;
            }
        }
    }
}
