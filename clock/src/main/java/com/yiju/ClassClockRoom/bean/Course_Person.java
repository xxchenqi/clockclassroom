package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sandy on 2016/6/16/0016.
 */
public class Course_Person implements Serializable {

    /**
     * code : 1
     * msg : ok
     * data : [{"id":"17","name":"数学1","status":"4","finish_enroll":"0","single_price":"50.00","pic":"aa","course_schedule":{"time_list":[{"status":3,"time":"2016.06.13 9:00-10:00"},{"status":3,"time":"2016.06.13 12:00-13:00"}],"date_section":"2016.06.13-06.13","all_course_hour":2}}]
     */

    private Integer code;
    private String msg;
    /**
     * id : 17
     * name : 数学1
     * status : 4
     * finish_enroll : 0
     * single_price : 50.00
     * pic : aa
     * course_schedule : {"time_list":[{"status":3,"time":"2016.06.13 9:00-10:00"},{"status":3,"time":"2016.06.13 12:00-13:00"}],"date_section":"2016.06.13-06.13","all_course_hour":2}
     */

    private List<Course_DataInfo> data;

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

    public List<Course_DataInfo> getData() {
        return data;
    }

    public void setData(List<Course_DataInfo> data) {
        this.data = data;
    }
}
