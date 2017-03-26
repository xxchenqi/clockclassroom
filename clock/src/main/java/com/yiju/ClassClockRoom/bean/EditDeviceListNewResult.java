package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 修改免费设备
 * Created by wh on 2016/5/17.
 */
public class EditDeviceListNewResult implements Serializable {

    /**
     * code : 1
     * msg : ok
     * data : [{"id":"51","use_id":"31","student_type":"2","device_id":"31","recommend":"6","max":"0","people_relation":"1","should":"1","create_time":"1454571086","name":"桌子","unit":"张"},{"id":"53","use_id":"31","student_type":"2","device_id":"33","recommend":"1","max":"0","people_relation":"0","should":"1","create_time":"1454571086","name":"白板","unit":"个"}]
     */

    private String code;
    private String msg;
    /**
     * id : 51
     * use_id : 31
     * student_type : 2
     * device_id : 31
     * recommend : 6
     * max : 0
     * people_relation : 1
     * should : 1
     * create_time : 1454571086
     * name : 桌子
     * unit : 张
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
        private String id;
        private String use_id;
        private String student_type;
        private String device_id;
        private String recommend;
        private String max;
        private String people_relation;//0 与人数无关 , 1 与人数有关
        private String should;
        private String create_time;
        private String name;
        private String unit;
        private String num;
        private int currentCount;
        private int localMax;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public int getLocalMax() {
            return localMax;
        }

        public void setLocalMax(int localMax) {
            this.localMax = localMax;
        }

        public int getCurrentCount() {
            return currentCount;
        }

        public void setCurrentCount(int currentCount) {
            this.currentCount = currentCount;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUse_id(String use_id) {
            this.use_id = use_id;
        }

        public void setStudent_type(String student_type) {
            this.student_type = student_type;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public void setPeople_relation(String people_relation) {
            this.people_relation = people_relation;
        }

        public void setShould(String should) {
            this.should = should;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getId() {
            return id;
        }

        public String getUse_id() {
            return use_id;
        }

        public String getStudent_type() {
            return student_type;
        }

        public String getDevice_id() {
            return device_id;
        }

        public String getRecommend() {
            return recommend;
        }

        public String getMax() {
            return max;
        }

        public String getPeople_relation() {
            return people_relation;
        }

        public String getShould() {
            return should;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getName() {
            return name;
        }

        public String getUnit() {
            return unit;
        }
    }
}
