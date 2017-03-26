package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/6/15/0015.
 */
public class TeacherData {

    /**
     * code : 1
     * msg : ok
     * data : {"special":[{"name":"老师专题1","type":"0","relation_id":"0","pic":"1","url":"http://api.51shizhong.com/1","short_url":""}],"teacher":{"organization":[{"id":"2","avatar":"123","tags":"语文","sex":"1","org_name":"学大教育","course_info":"测试2"}],"personal":[{"id":"24","avatar":"http://get.file.dc.cric.com/SZJSc20537bca709883f3714c0be80485f84_350X350_0_0_0.jpg","tags":"门口了","sex":"0","org_name":"","course_info":""}]}}
     */

    private String code;
    private String msg;
    /**
     * special : [{"name":"老师专题1","type":"0","relation_id":"0","pic":"1","url":"http://api.51shizhong.com/1","short_url":""}]
     * teacher : {"organization":[{"id":"2","avatar":"123","tags":"语文","sex":"1","org_name":"学大教育","course_info":"测试2"}],"personal":[{"id":"24","avatar":"http://get.file.dc.cric.com/SZJSc20537bca709883f3714c0be80485f84_350X350_0_0_0.jpg","tags":"门口了","sex":"0","org_name":"","course_info":""}]}
     */

    private Teacher_Special data;

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

    public Teacher_Special getData() {
        return data;
    }

    public void setData(Teacher_Special data) {
        this.data = data;
    }

    public static class Teacher_Special {
        private TeacherBean teacher;
        /**
         * name : 老师专题1
         * type : 0
         * relation_id : 0
         * pic : 1
         * url : http://api.51shizhong.com/1
         * short_url :
         */

        private List<SpecialTeacherInfo> special;

        public TeacherBean getTeacher() {
            return teacher;
        }

        public void setTeacher(TeacherBean teacher) {
            this.teacher = teacher;
        }

        public List<SpecialTeacherInfo> getSpecial() {
            return special;
        }

        public void setSpecial(List<SpecialTeacherInfo> special) {
            this.special = special;
        }

        public static class TeacherBean {
            /**
             * id : 2
             * avatar : 123
             * tags : 语文
             * sex : 1
             * org_name : 学大教育
             * course_info : 测试2
             */

            private List<TeacherInfoBean> organization;
            /**
             * id : 24
             * avatar : http://get.file.dc.cric.com/SZJSc20537bca709883f3714c0be80485f84_350X350_0_0_0.jpg
             * tags : 门口了
             * sex : 0
             * org_name :
             * course_info :
             */

            private List<TeacherInfoBean> personal;

            public List<TeacherInfoBean> getOrganization() {
                return organization;
            }

            public void setOrganization(List<TeacherInfoBean> organization) {
                this.organization = organization;
            }

            public List<TeacherInfoBean> getPersonal() {
                return personal;
            }

            public void setPersonal(List<TeacherInfoBean> personal) {
                this.personal = personal;
            }
        }

    }
}
