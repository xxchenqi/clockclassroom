package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/6/14/0014.
 */
public class CourseData {
    /**
     * code : 1
     * msg : ok
     * data : {"special":[{"name":"课程专题2","type":"0","relation_id":"0","pic":"2","url":"http://api.51shizhong.com/2","short_url":""}],"course":[{"id":"14","name":"语文1","teacher_name":"张老湿","org_name":"学大教育","school_name":"测试徐汇虹梅商务大厦旗舰店","pic":"cc","single_price":"50.00"}]}
     */

    private Integer code;
    private String msg;
    private Special_Course data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Special_Course getData() {
        return data;
    }

    public void setData(Special_Course data) {
        this.data = data;
    }

    public static class Special_Course {
        /**
         * name : 课程专题2
         * type : 0
         * relation_id : 0
         * pic : 2
         * url : http://api.51shizhong.com/2
         * short_url :
         */

        private List<SpecialInfo> special;
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

        public List<SpecialInfo> getSpecial() {
            return special;
        }

        public void setSpecial(List<SpecialInfo> special) {
            this.special = special;
        }

        public List<CourseInfo> getCourse() {
            return course;
        }

        public void setCourse(List<CourseInfo> course) {
            this.course = course;
        }
    }

}
