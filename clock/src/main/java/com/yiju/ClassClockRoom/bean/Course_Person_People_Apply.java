package com.yiju.ClassClockRoom.bean;

/**
 * Created by Sandy on 2016/6/27/0027.
 */
public class Course_Person_People_Apply {


    /**
     * code : 1
     * msg : ok
     * data : {"id":"14","remain_count":"8","have_enroll":"4","courselist":[{"nickname":"4444444","pic":"http://get.file.dc.cric.com/SZJSe3b44e91bb146fac5015a380db7ce4f5.jpg","contactname":"成都type桂圆肉","mobile":"13534455511","daterange":"2016.06.17-2016.09.23","totaltime":2,"totalfee":100,"createtime":"1465805248"}]}
     */

    private Integer code;
    private String msg;
    /**
     * id : 14
     * remain_count : 8
     * have_enroll : 4
     * courselist : [{"nickname":"4444444","pic":"http://get.file.dc.cric.com/SZJSe3b44e91bb146fac5015a380db7ce4f5.jpg","contactname":"成都type桂圆肉","mobile":"13534455511","daterange":"2016.06.17-2016.09.23","totaltime":2,"totalfee":100,"createtime":"1465805248"}]
     */

    private People_Apply_Data data;

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

    public People_Apply_Data getData() {
        return data;
    }

    public void setData(People_Apply_Data data) {
        this.data = data;
    }
}
