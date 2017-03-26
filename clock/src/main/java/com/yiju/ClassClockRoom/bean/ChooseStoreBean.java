package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:选择门店bean
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/7/5 11:47
 * ----------------------------------------
 */
public class ChooseStoreBean implements Serializable{

    /**
     * code : 1
     * msg : ok
     * data : [{"sid":8,"name":"徐汇虹梅中心","address":"徐汇区沪闵路8075号8楼","start_time":"800","end_time":"2100"},{"sid":9,"name":"普陀友通中心","address":"大渡河路1517号友通数码港","start_time":"800","end_time":"2100"},{"sid":10,"name":"闵行新乐坊中心","address":"合川路2928号新乐坊A幢3楼","start_time":"800","end_time":"2100"},{"sid":11,"name":"闸北达人湾中心","address":"中山北路756号达人湾创智广场2楼","start_time":"800","end_time":"2100"},{"sid":16,"name":"浦东龙阳中心","address":"浦东新区龙阳路1990号(地铁2线龙阳路站上盖)","start_time":"800","end_time":"2100"},{"sid":18,"name":"技术中心测试门店","address":"闸北区广中路788号","start_time":"900","end_time":"2100"}]
     */

    private String code;
    private String msg;
    /**
     * sid : 8
     * name : 徐汇虹梅中心
     * address : 徐汇区沪闵路8075号8楼
     * start_time : 800
     * end_time : 2100
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

    public static class DataEntity implements Serializable{
        private String sid;
        private String name;
        private String address;
        private String start_time;
        private String end_time;
        private String lat_g;
        private String lng_g;
        private String school_type;

        public String getLat_g() {
            return lat_g;
        }

        public String getLng_g() {
            return lng_g;
        }

        public String getSchool_type() {
            return school_type;
        }

        private boolean flag;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getSid() {
            return sid;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }
    }
}
