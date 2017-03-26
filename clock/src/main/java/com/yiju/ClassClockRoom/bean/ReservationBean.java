package com.yiju.ClassClockRoom.bean;

/**
  * ============================================================
  *
  * 作      者  : Sandy
  *
  * 日      期  : 2016/5/5/0005 15:39
  *
  * 描      述  :
  *
  * ============================================================
  */
import java.io.Serializable;
import java.util.List;

public class ReservationBean implements Serializable{

    /**
     * code : 1
     * msg : ok
     * data : [{"id":"27"},{"id":"25"},{"id":"23"},{"id":"30"},{"id":"26"},{"id":"189"},{"id":"28"},{"id":"24"}]
     * device : [{"stock":"4","id":"8","name":"投影仪","fee":"10.00","unit":"台"},{"stock":"5","id":"9","name":"小蜜蜂","fee":"10.00","unit":" 台"}]
     */

    private Integer code;
    private String msg;
    /**
     * id : 27
     */

    private List<String> data;
    /**
     * stock : 4
     * id : 8
     * name : 投影仪
     * fee : 10.00
     * unit : 台
     */

    private List<ReservationDevice> device;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setDevice(List<ReservationDevice> device) {
        this.device = device;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<String> getData() {
        return data;
    }

    public List<ReservationDevice> getDevice() {
        return device;
    }

    public static class ReservationDevice implements Serializable{
        private String stock;
        private String id;
        private String name;
        private String fee;
        private String unit;
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getStock() {
            return stock;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getFee() {
            return fee;
        }

        public String getUnit() {
            return unit;
        }
    }
}
