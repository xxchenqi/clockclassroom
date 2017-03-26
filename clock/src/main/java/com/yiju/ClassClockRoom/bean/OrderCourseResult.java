package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 *
 * Created by wh on 2016/6/22.
 */
public class OrderCourseResult {


    /**
     * code : 1
     * msg : ok
     * data : [{"course_name":"数学1","school_name":"测试徐汇虹梅商务大厦旗舰店","start_date":"2016-06-17","end_date":"2016-06-18","course_num":2,"status":"0","expire_time":"1565810848","real_fee":"100.00"},{"course_name":"语文1","school_name":"测试徐汇虹梅商务大厦旗舰店","start_date":"2016-06-17","end_date":"2016-06-18","course_num":2,"status":"3","expire_time":"1565811198","real_fee":"100.00"},{"course_name":"语文1","school_name":"测试徐汇虹梅商务大厦旗舰店","start_date":"2016-06-17","end_date":"2016-06-17","course_num":1,"status":"1","expire_time":"1565807407","real_fee":"100.00"}]
     */

    private String code;
    private String msg;
    private int servertime;
    private List<OrderCourseData> data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<OrderCourseData> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<OrderCourseData> getData() {
        return data;
    }

    public int getServertime() {
        return servertime;
    }

    public void setServertime(int servertime) {
        this.servertime = servertime;
    }
}
