package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/3/21 16:40
 * ----------------------------------------
 */
public class MemberBean {


    /**
     * code : 1
     * msg : ok
     * data : [{"id":"2","real_name":"","mobile":"13333333333","is_auth":"1","org_auth":"2","avatar":"http://get.file.dc.cric.com/SZJS23f7905a0f3046e788e5d9cb10ae6858_350X350_0_0_0.jpg","show_teacher":"0"},{"id":"24","real_name":"","mobile":"13120567078","is_auth":0,"org_auth":"0","avatar":"http://get.file.dc.cric.com/SZJSd25b7e1e6c7abb6931dcb1aa9ea4e520_350X350_0_0_0.jpg","show_teacher":"0"},{"id":"135","real_name":"aaa","mobile":"13334444444","is_auth":0,"org_auth":"0","avatar":"aaa","show_teacher":"0"},{"id":"138","real_name":"bbaa","mobile":"13334433443","is_auth":0,"org_auth":"0","avatar":"","show_teacher":"0"}]
     */

    private String code;
    private String msg;
    /**
     * id : 2
     * real_name :
     * mobile : 13333333333
     * is_auth : 1
     * org_auth : 2
     * avatar : http://get.file.dc.cric.com/SZJS23f7905a0f3046e788e5d9cb10ae6858_350X350_0_0_0.jpg
     * show_teacher : 0
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

    public class DataEntity implements Serializable {
        private String id;
        private String real_name;
        private String mobile;
        private String is_auth;
        private String org_auth;
        private String avatar;
        private String show_teacher;
        private String org_dname;
        private boolean check;
        private String fullteacherinfo;//资料是否齐全  0：不全  1：全

        public String getOrg_dname() {
            return org_dname;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setIs_auth(String is_auth) {
            this.is_auth = is_auth;
        }

        public void setOrg_auth(String org_auth) {
            this.org_auth = org_auth;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setShow_teacher(String show_teacher) {
            this.show_teacher = show_teacher;
        }

        public String getId() {
            return id;
        }

        public String getReal_name() {
            return real_name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getIs_auth() {
            return is_auth;
        }

        public String getOrg_auth() {
            return org_auth;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getShow_teacher() {
            return show_teacher;
        }

        public String getFullteacherinfo() {
            return fullteacherinfo;
        }

        public void setFullteacherinfo(String fullteacherinfo) {
            this.fullteacherinfo = fullteacherinfo;
        }
    }
}
