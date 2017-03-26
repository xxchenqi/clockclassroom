package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/6/15/0015.
 */
public class CourseMoreData {

    /**
     * code : 1
     * msg : ok
     * data : {"school":[{"id":"8","name":"测试徐汇虹梅商务大厦旗舰店"}],"course":[{"id":"14","name":"语文1","teacher_name":"张老湿","org_name":"学大教育","school_name":"测试徐汇虹梅商务大厦旗舰店","pic":"cc","single_price":"50.00"}]}
     */

    private String code;
    private String msg;
    private Course_School data;

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

    public Course_School getData() {
        return data;
    }

    public void setData(Course_School data) {
        this.data = data;
    }

    public static class Course_School {
        /**
         * id : 8
         * name : 测试徐汇虹梅商务大厦旗舰店
         */

        private List<SchoolInfo> school;
        /**
         * id : 14
         * name : 语文1
         * teacher_name : 张老湿
         * org_name : 学大教育
         * school_name : 测试徐汇虹梅商务大厦旗舰店
         * pic : cc
         * single_price : 50.00
         */

        private List<CourseInfo> course;

        public List<SchoolInfo> getSchool() {
            return school;
        }

        public void setSchool(List<SchoolInfo> school) {
            this.school = school;
        }

        public List<CourseInfo> getCourse() {
            return course;
        }

        public void setCourse(List<CourseInfo> course) {
            this.course = course;
        }

    }
}
