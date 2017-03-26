package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/14 14:19
 * ----------------------------------------
 */
public class OrganizationBlacklistBean {


    /**
     * code : 1
     * msg : ok
     * data : [{"id":"2","name":"n1","short_name":"sn1","logo":"123"}]
     */

    private String code;
    private String msg;
    /**
     * id : 2
     * name : n1
     * short_name : sn1
     * logo : 123
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

    public class DataEntity {
        private String id;
        private String name;
        private String short_name;
        private String logo;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getShort_name() {
            return short_name;
        }

        public String getLogo() {
            return logo;
        }
    }
}
