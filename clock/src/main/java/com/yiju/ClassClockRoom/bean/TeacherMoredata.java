package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/6/15/0015.
 */
public class TeacherMoreData {

    /**
     * code : 1
     * msg : ok
     * data : [{"id":"2","avatar":"http://get.file.dc.cric.com/SZJS23f7905a0f3046e788e5d9cb10ae6858_350X350_0_0_0.jpg","tags":"语文","sex":"1","org_name":"学大教育","course_info":"测试2"}]
     */

    private Integer code;
    private String msg;
    /**
     * id : 2
     * avatar : http://get.file.dc.cric.com/SZJS23f7905a0f3046e788e5d9cb10ae6858_350X350_0_0_0.jpg
     * tags : 语文
     * sex : 1
     * org_name : 学大教育
     * course_info : 测试2
     */

    private List<TeacherInfoBean> obj;

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

    public List<TeacherInfoBean> getObj() {
        return obj;
    }

    public void setObj(List<TeacherInfoBean> obj) {
        this.obj = obj;
    }
}

