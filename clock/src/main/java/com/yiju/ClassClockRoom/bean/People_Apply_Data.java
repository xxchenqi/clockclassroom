package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/6/27/0027.
 */
public class People_Apply_Data {

    private String id;
    private String remain_count;
    private String have_enroll;
    /**
     * nickname : 4444444
     * pic : http://get.file.dc.cric.com/SZJSe3b44e91bb146fac5015a380db7ce4f5.jpg
     * contactname : 成都type桂圆肉
     * mobile : 13534455511
     * daterange : 2016.06.17-2016.09.23
     * totaltime : 2
     * totalfee : 100
     * createtime : 1465805248
     */

    private List<People_Apply_Courselist> courselist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemain_count() {
        return remain_count;
    }

    public void setRemain_count(String remain_count) {
        this.remain_count = remain_count;
    }

    public String getHave_enroll() {
        return have_enroll;
    }

    public void setHave_enroll(String have_enroll) {
        this.have_enroll = have_enroll;
    }

    public List<People_Apply_Courselist> getCourselist() {
        return courselist;
    }

    public void setCourselist(List<People_Apply_Courselist> courselist) {
        this.courselist = courselist;
    }

}
