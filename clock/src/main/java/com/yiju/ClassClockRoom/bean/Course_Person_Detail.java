package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * Created by Sandy on 2016/6/16/0016.
 */
public class Course_Person_Detail implements Serializable{


    /**
     * code : 1
     * msg : ok
     * data : {"id":"1","name":"测试1","single_price":"0.00","old_price":"0.00","course_type":"1","remain_count":"10","total_count":"50","desc":"","pics":[],"schedule":{"all_course_hour":0,"active_course_hour":0,"date_section":"1970.01.01-01.01"},"course_status":4,"teacher":{"id":"30","real_name":"","org_name":"陈奇奇","tags":"语文,能力,全科,太可怜了,门口了,all 里","pics":["http://get.file.dc.cric.com/SZJS00425f290b95426709858a63af3eaa69_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5e886a31f39dc8542fdf51e380e669e3_320X240_0_0_0.jpg"]},"school":{"id":"9","name":"测试普陀友通数码中心店","address":"大渡河路1517号友通数码港","lng":"121.40399900869","lat":"31.25126585574","lng_g":"121.39741565980","lat_g":"31.24591350460"}}
     */

    private Integer code;
    private String msg;
    /**
     * id : 1
     * name : 测试1
     * single_price : 0.00
     * old_price : 0.00
     * course_type : 1
     * remain_count : 10
     * total_count : 50
     * desc :
     * pics : []
     * schedule : {"all_course_hour":0,"active_course_hour":0,"date_section":"1970.01.01-01.01"}
     * course_status : 4
     * teacher : {"id":"30","real_name":"","org_name":"陈奇奇","tags":"语文,能力,全科,太可怜了,门口了,all 里","pics":["http://get.file.dc.cric.com/SZJS00425f290b95426709858a63af3eaa69_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5e886a31f39dc8542fdf51e380e669e3_320X240_0_0_0.jpg"]}
     * school : {"id":"9","name":"测试普陀友通数码中心店","address":"大渡河路1517号友通数码港","lng":"121.40399900869","lat":"31.25126585574","lng_g":"121.39741565980","lat_g":"31.24591350460"}
     */

    private Course_Person_Detail_Data data;

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

    public Course_Person_Detail_Data getData() {
        return data;
    }

    public void setData(Course_Person_Detail_Data data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
