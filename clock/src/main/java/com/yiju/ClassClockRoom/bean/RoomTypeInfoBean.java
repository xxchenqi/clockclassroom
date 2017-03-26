package com.yiju.ClassClockRoom.bean;

/**
 * ----------------------------------------
 * 注释:根据房间类型ID获取预订时需要带入的信息
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/1/26 15:13
 * ----------------------------------------
 */
public class RoomTypeInfoBean {


    /**
     * code : 1
     * msg : ok
     * data : {"sid":"8","sname":"徐汇虹梅商务大厦旗舰店","max_member":"30","is_meeting":"1"}
     */

    private String code;
    private String msg;
    /**
     * sid : 8
     * sname : 徐汇虹梅商务大厦旗舰店
     * max_member : 30
     * is_meeting : 1
     */

    private DataEntity data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String sid;
        private String sname;
        private String max_member;
        private String is_meeting;

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public void setMax_member(String max_member) {
            this.max_member = max_member;
        }

        public void setIs_meeting(String is_meeting) {
            this.is_meeting = is_meeting;
        }

        public String getSid() {
            return sid;
        }

        public String getSname() {
            return sname;
        }

        public String getMax_member() {
            return max_member;
        }

        public String getIs_meeting() {
            return is_meeting;
        }
    }
}
