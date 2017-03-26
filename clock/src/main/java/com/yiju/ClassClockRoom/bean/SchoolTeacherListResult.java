package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/7/8.
 */
public class SchoolTeacherListResult implements Serializable {

    /**
     * code : 1
     * msg : ok
     * data : {"type":"personal","list":[{"uid":"2","avatar":"http://get.file.dc.cric.com/SZJS51a681e71cb565a2f8e8e1ec4f18602f_350X350_0_0_0.jpg","name":"133","org_auth":"1","teacher_id":"12","show_teacher":"1"}]}
     */

    private String code;
    private String msg;
    /**
     * type : personal
     * list : [{"uid":"2","avatar":"http://get.file.dc.cric.com/SZJS51a681e71cb565a2f8e8e1ec4f18602f_350X350_0_0_0.jpg","name":"133","org_auth":"1","teacher_id":"12","show_teacher":"1"}]
     */

    private SchoolTeacherBean data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(SchoolTeacherBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SchoolTeacherBean getData() {
        return data;
    }

    public static class SchoolTeacherBean implements Serializable {
        private String type;
        /**
         * uid : 2
         * avatar : http://get.file.dc.cric.com/SZJS51a681e71cb565a2f8e8e1ec4f18602f_350X350_0_0_0.jpg
         * name : 133
         * org_auth : 1
         * teacher_id : 12
         * show_teacher : 1
         */

        private List<ListEntity> list;

        public void setType(String type) {
            this.type = type;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public String getType() {
            return type;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class ListEntity implements Serializable {
            private String uid;
            private String avatar;
            private String name;
            private String real_name;
            private String org_auth;
            private String teacher_id;
            private String show_teacher;
            private String fullteacherinfo;//0：不全 1：全
            private boolean check;

            public String getFullteacherinfo() {
                return fullteacherinfo;
            }

            public void setFullteacherinfo(String fullteacherinfo) {
                this.fullteacherinfo = fullteacherinfo;
            }

            public String getReal_name() {
                return real_name;
            }

            public void setReal_name(String real_name) {
                this.real_name = real_name;
            }

            public boolean isCheck() {
                return check;
            }

            public void setCheck(boolean check) {
                this.check = check;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOrg_auth(String org_auth) {
                this.org_auth = org_auth;
            }

            public void setTeacher_id(String teacher_id) {
                this.teacher_id = teacher_id;
            }

            public void setShow_teacher(String show_teacher) {
                this.show_teacher = show_teacher;
            }

            public String getUid() {
                return uid;
            }

            public String getAvatar() {
                return avatar;
            }

            public String getName() {
                return name;
            }

            public String getOrg_auth() {
                return org_auth;
            }

            public String getTeacher_id() {
                return teacher_id;
            }

            public String getShow_teacher() {
                return show_teacher;
            }
        }
    }
}
