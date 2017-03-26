package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * ----------------------------------------
 * 注释: 设备Bean
 * <p>
 * 作者: cq
 * <p>
 * 时间: 2016/1/22 15:23
 * ----------------------------------------
 */
public class DeviceBean {


    /**
     * code : 1
     * msg : ok
     * data : [{"device_id":"33","name":"白板","unit":"个","num":"1"},{"device_id":"32","name":"椅子","unit":"把","num":"5"}]
     */

    private String code;
    private String msg;
    /**
     * device_id : 33
     * name : 白板
     * unit : 个
     * num : 1
     */

    private List<DataEntity> data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String device_id;
        private String name;
        private String unit;
        private String num;
        private String people_relation;
        private int max;
        private int currentCount;

        public String getPeople_relation() {
            return people_relation;
        }

        public void setPeople_relation(String people_relation) {
            this.people_relation = people_relation;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getCurrentCount() {
            return currentCount;
        }

        public void setCurrentCount(int currentCount) {
            this.currentCount = currentCount;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getDevice_id() {
            return device_id;
        }

        public String getName() {
            return name;
        }

        public String getUnit() {
            return unit;
        }

        public String getNum() {
            return num;
        }
    }
}
