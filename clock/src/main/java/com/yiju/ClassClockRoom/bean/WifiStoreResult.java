package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * 可选门店列表
 * Created by wh on 2016/11/9.
 */

public class WifiStoreResult {

    /**
     * code : 1
     * msg : ok
     * data : [{"name":"闵行新乐坊中心"},{"name":"浦东龙阳中心"},{"name":"普陀友通中心"},{"name":"徐汇虹梅中心"},{"name":"闸北达人湾中心"}]
     */

    private String code;
    private String msg;
    private List<DataEntity> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * name : 闵行新乐坊中心
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
