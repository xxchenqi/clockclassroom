package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by admin on 2016/8/9.
 */
public class WatchlistTeacherResult {


    /**
     * code : 1
     * msg : ok
     * data : [{"uid":"2","avatar":"http://get.file.dc.cric.com/SZJS5a249064fecc62a705a5ec062ae65182.jpg","real_name":"133","sex":"0","org_name":"哈哈测试机构","course_name":"语文1","tag":"有和很是你的"},{"uid":"1349","avatar":"","real_name":"aaa静静","sex":"0","org_name":"哈哈测试机构","course_name":"","tag":""},{"uid":"31","avatar":"http://get.file.dc.cric.com/SZJS82b9312913122c81d5aaff09d594cbe9_350X350_0_0_0.jpg","real_name":"张老湿","sex":"0","org_name":"测试机构_玉甲","course_name":"","tag":"老人"}]
     */

    private String code;
    private String msg;
    /**
     * uid : 2
     * avatar : http://get.file.dc.cric.com/SZJS5a249064fecc62a705a5ec062ae65182.jpg
     * real_name : 133
     * sex : 0
     * org_name : 哈哈测试机构
     * course_name : 语文1
     * tag : 有和很是你的
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
        private String teacher_id;
        private String avatar;
        private String real_name;
        private String sex;
        private String org_name;
        private String course_name;
        private String tag;
        private String show_teacher;

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setOrg_name(String org_name) {
            this.org_name = org_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }


        public String getAvatar() {
            return avatar;
        }

        public String getReal_name() {
            return real_name;
        }

        public String getSex() {
            return sex;
        }

        public String getOrg_name() {
            return org_name;
        }

        public String getCourse_name() {
            return course_name;
        }

        public String getTag() {
            return tag;
        }

        public String getShow_teacher() {
            return show_teacher;
        }

        public void setShow_teacher(String show_teacher) {
            this.show_teacher = show_teacher;
        }
    }
}
