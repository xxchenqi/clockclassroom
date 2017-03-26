package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * 我的关注_课程列表结果json
 * Created by wh on 2016/8/9.
 */
public class WatchlistCourseResult {

    /**
     * code : 1
     * msg : ok
     * data : [{"course_name":"幼小衔接","single_price":"","pic":"","start_date":"2016-07-16","end_date":"2016-08-27","countclasstime":0},{"course_name":"兴趣思维","single_price":"","pic":"","start_date":"2016-09-07","end_date":"2017-07-26","countclasstime":0.47},{"course_name":"儿童乐高教育","single_price":"","pic":"","start_date":"2016-09-03","end_date":"2016-11-27","countclasstime":0},{"course_name":"奥数","single_price":"","pic":"","start_date":"2016-07-01","end_date":"2016-08-28","countclasstime":0},{"course_name":"逻辑思维","single_price":"","pic":"","start_date":"2016-07-02","end_date":"2016-08-27","countclasstime":0}]
     */

    private String code;
    private String msg;
    /**
     * course_name : 幼小衔接
     * single_price :
     * pic :
     * start_date : 2016-07-16
     * end_date : 2016-08-27
     * countclasstime : 0
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
        private String course_id;
        private String course_name;
        private String single_price;
        private String pic;
        private String start_date;
        private String end_date;
        private String countclasstime;
        private String course_status;

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public void setSingle_price(String single_price) {
            this.single_price = single_price;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public void setCountclasstime(String countclasstime) {
            this.countclasstime = countclasstime;
        }

        public String getCourse_name() {
            return course_name;
        }

        public String getSingle_price() {
            return single_price;
        }

        public String getPic() {
            return pic;
        }

        public String getStart_date() {
            return start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public String getCountclasstime() {
            return countclasstime;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getCourse_status() {
            return course_status;
        }

        public void setCourse_status(String course_status) {
            this.course_status = course_status;
        }
    }
}
