package com.yiju.ClassClockRoom.bean;

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

    private String server_time;

    public String getServer_time() {
        return server_time;
    }

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
        private String id;
        private String status;
        private String fee;
        private String real_fee;
        private String create_time;
        private String expire_time;
        private String contact_name;
        private String contact_mobile;
        private String sname;
        private String ctype;
        private String course_name;
        private String course_str;
        private String course_id;
        private String pay_name;
        private String pay_method;

        public String getPay_method() {
            return pay_method;
        }

        public void setPay_method(String pay_method) {
            this.pay_method = pay_method;
        }

        private String receipt_no_pay;
        private String pic;
        private String sid;
        private String trade_id;

        public String getTrade_id() {
            return trade_id;
        }

        public String getSid() {
            return sid;
        }

        public String getSchool_type() {
            return school_type;
        }

        private String school_type;

        public String getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public String getFee() {
            return fee;
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

        public String getContact_name() {
            return contact_name;
        }

        public String getContact_mobile() {
            return contact_mobile;
        }

        public String getSname() {
            return sname;
        }

        public String getCtype() {
            return ctype;
        }

        public String getCourse_name() {
            return course_name;
        }

        public String getCourse_str() {
            return course_str;
        }

        public String getCourse_id() {
            return course_id;
        }

        public String getPay_name() {
            return pay_name;
        }

        public String getReceipt_no_pay() {
            return receipt_no_pay;
        }

        public String getPic() {
            return pic;
        }
    }
}
