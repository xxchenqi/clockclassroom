package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * 购物车提交失败50027 2015/12/11/0011.
 */
public class CartSeven {

    /**
     * code : 50027
     * data : [{"id":"2138","index":"4","sid":"8","sname":"徐汇旗舰店"},{"id":"2145","index":"3","sid":"8","sname":"徐汇旗舰店"},{"id":"2149","index":"2","sid":"11","sname":"闸北中心店"}]
     * msg : 过期订单有过期作废订单，请删除
     */

    private Integer code;
    private String msg;
    /**
     * id : 2138
     * index : 4
     * sid : 8
     * sname : 徐汇旗舰店
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

    public static class DataEntity {
        private String id;
        private Integer index;
        private String sid;
        private String sname;

        public void setId(String id) {
            this.id = id;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getId() {
            return id;
        }

        public Integer getIndex() {
            return index;
        }

        public String getSid() {
            return sid;
        }

        public String getSname() {
            return sname;
        }
    }
}
