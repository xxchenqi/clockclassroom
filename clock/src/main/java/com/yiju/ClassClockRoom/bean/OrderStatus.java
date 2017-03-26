package com.yiju.ClassClockRoom.bean;

/**
 * 订单状态2015/12/15/0015.
 */
public class OrderStatus {

    /**
     * code : 1
     * data : {"count":"1","expire_time":"1450171366","nowtime":1450171046}
     * msg : ok
     */

    private Integer code;
    /**
     * count : 1
     * expire_time : 1450171366
     * nowtime : 1450171046
     */

    private OrderDesc data;
    private String msg;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(OrderDesc data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public OrderDesc getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static class OrderDesc {
        private String count;
        private String expire_time;
        private int nowtime;

        public void setCount(String count) {
            this.count = count;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public void setNowtime(int nowtime) {
            this.nowtime = nowtime;
        }

        public String getCount() {
            return count;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public int getNowtime() {
            return nowtime;
        }
    }
}
