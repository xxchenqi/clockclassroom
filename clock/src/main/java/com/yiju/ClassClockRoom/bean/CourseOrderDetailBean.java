package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/6/27 16:46
 * ----------------------------------------
 */
public class CourseOrderDetailBean {

    /**
     * code : 1
     * msg : ok
     * data : {"order_id":"14","course_name":"语文11","school_name":"测试徐汇虹梅商务大厦旗舰店","real_fee":"50.00","create_time":"1466679003","expire_time":"1466680203","pic":"http://get.file.dc.cric.com/SZJSc18925ea8e01eb608d547350de4d5089_350X350_0_0_0.jpg","classlist":[{"date":"2016-09-23","start_time":"1000","end_time":"1100","single_fee":"50.00"}],"single_fee":"50.00","status":"4","contact_name":"zz","mobile":"18516757557"}
     */

    private String code;
    private String msg;
    /**
     * order_id : 14
     * course_name : 语文11
     * school_name : 测试徐汇虹梅商务大厦旗舰店
     * real_fee : 50.00
     * create_time : 1466679003
     * expire_time : 1466680203
     * pic : http://get.file.dc.cric.com/SZJSc18925ea8e01eb608d547350de4d5089_350X350_0_0_0.jpg
     * classlist : [{"date":"2016-09-23","start_time":"1000","end_time":"1100","single_fee":"50.00"}]
     * single_fee : 50.00
     * status : 4
     * contact_name : zz
     * mobile : 18516757557
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
        private String order_id;
        private String course_id;
        private String course_name;
        private String school_name;
        private String real_fee;
        private String refund_fee;
        private String create_time;
        private String expire_time;
        private String server_time;
        private String pic;
        private String single_fee;
        private String countclasstime;
        private String status;
        private String contact_name;
        private String mobile;
        /**
         * date : 2016-09-23
         * start_time : 1000
         * end_time : 1100
         * single_fee : 50.00
         */

        private List<ClassListEntity> classlist;

        public String getCourse_id() {
            return course_id;
        }

        public String getCountclasstime() {
            return countclasstime;
        }

        public String getServer_time() {
            return server_time;
        }

        public String getRefund_fee() {
            return refund_fee;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public void setReal_fee(String real_fee) {
            this.real_fee = real_fee;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public void setSingle_fee(String single_fee) {
            this.single_fee = single_fee;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setClasslist(List<ClassListEntity> classlist) {
            this.classlist = classlist;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getCourse_name() {
            return course_name;
        }

        public String getSchool_name() {
            return school_name;
        }

        public String getReal_fee() {
            return real_fee;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public String getPic() {
            return pic;
        }

        public String getSingle_fee() {
            return single_fee;
        }

        public String getStatus() {
            return status;
        }

        public String getContact_name() {
            return contact_name;
        }

        public String getMobile() {
            return mobile;
        }

        public List<ClassListEntity> getClasslist() {
            return classlist;
        }

        public static class ClassListEntity {
            private String date;
            private String start_time;
            private String end_time;
            private String single_fee;
            private String classstatus;

            public String getClassstatus() {
                return classstatus;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public void setSingle_fee(String single_fee) {
                this.single_fee = single_fee;
            }

            public String getDate() {
                return date;
            }

            public String getStart_time() {
                return start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public String getSingle_fee() {
                return single_fee;
            }
        }
    }
}
