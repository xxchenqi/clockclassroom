package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 预订失败 2015/12/16/0016.
 */
public class ReservationFilter implements Serializable{
    /**
     * code : 40050
     * data : {"arr":[{"date":"2016-01-30","miss_count":2,"room_id":[]}],"flag":0}
     * msg : 房间库存不足
     */

    private String code;
    /**
     * arr : [{"date":"2016-01-30","miss_count":2,"room_id":[]}]
     * flag : 0
     */

    private FailInfo data;
    private String msg;

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(FailInfo data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public FailInfo getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static class FailInfo implements Serializable{
        private Integer flag;
        /**
         * date : 2016-01-30
         * miss_count : 2
         * room_id : []
         */

        private List<ArrEntity> arr;

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public void setArr(List<ArrEntity> arr) {
            this.arr = arr;
        }

        public Integer getFlag() {
            return flag;
        }

        public List<ArrEntity> getArr() {
            return arr;
        }

        public static class ArrEntity implements Serializable{
            private String date;
            private int miss_count;
            private List<String> room_id;

            public void setDate(String date) {
                this.date = date;
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

            public int getMiss_count() {
                return miss_count;
            }

            public List<String> getRoom_id() {
                return room_id;
            }
        }
    }


//    /**
//     * code : 1
//     * data : {"arr":[{"date":"2015-12-16","miss_count":1,"room_id":["33","34","35"]},{"date":"2015-12-17","miss_count":1,"room_id":["33","34","35"]}],"flag":0,"order2_id":0}
//     * msg : ok
//     */
//
//    private Integer code;
//    /**
//     * arr : [{"date":"2015-12-16","miss_count":1,"room_id":["33","34","35"]},{"date":"2015-12-17","miss_count":1,"room_id":["33","34","35"]}]
//     * flag : 0
//     * order2_id : 0
//     */
//
//    private FailInfo data;
//    private String msg;
//
//    public void setCode(Integer code) {
//        this.code = code;
//    }
//
//    public void setData(FailInfo data) {
//        this.data = data;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public Integer getCode() {
//        return code;
//    }
//
//    public FailInfo getData() {
//        return data;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public class FailInfo implements Serializable{
//        private Integer flag;
//        private Integer order2_id;
//        /**
//         * date : 2015-12-16
//         * miss_count : 1
//         * room_id : ["33","34","35"]
//         */
//
//        private List<ArrEntity> arr;
//
//        public void setFlag(Integer flag) {
//            this.flag = flag;
//        }
//
//        public void setOrder2_id(Integer order2_id) {
//            this.order2_id = order2_id;
//        }
//
//        public void setArr(List<ArrEntity> arr) {
//            this.arr = arr;
//        }
//
//        public Integer getFlag() {
//            return flag;
//        }
//
//        public Integer getOrder2_id() {
//            return order2_id;
//        }
//
//        public List<ArrEntity> getArr() {
//            return arr;
//        }
//
//        public  class ArrEntity implements Serializable{
//            private String date;
//            private Integer miss_count;
//            private List<String> room_id;
//
//            public void setDate(String date) {
//                this.date = date;
//            }
//
//            public void setMiss_count(Integer miss_count) {
//                this.miss_count = miss_count;
//            }
//
//            public void setRoom_id(List<String> room_id) {
//                this.room_id = room_id;
//            }
//
//            public String getDate() {
//                return date;
//            }
//
//            public Integer getMiss_count() {
//                return miss_count;
//            }
//
//            public List<String> getRoom_id() {
//                return room_id;
//            }
//        }
//    }


}
