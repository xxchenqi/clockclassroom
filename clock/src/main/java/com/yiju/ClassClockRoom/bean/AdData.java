package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/*
 * Created by wh on 2016/3/10.
 */
public class AdData implements Serializable {

    /**
     * id : 18
     * type : 1
     * teacher_id : 0
     * order : 1
     * name : aa
     * url : http://api.51shizhong.com/h5_v1/teacherdetail.html?tid=994&amp;teacherdetail=teacherdetail
     * short_url : http://dwz.cn/2Simxo
     * start_date : 2016-03-03
     * end_date : 2016-03-31
     * pic_url : http://get.file.dc.cric.com/SZJSc70e2e94d9f5ef943e44bbaa8835da5d_639X192_0_0_1.jpg
     * ad_type : 1
     */

    private String id;
    private String type;
    private String teacher_id;
    private String order;
    private String name;
    private String url;
    private String short_url;
    private String start_date;
    private String end_date;
    private String pic_url;
    private String ad_type;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getShort_url() {
        return short_url;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getPic_url() {
        return pic_url;
    }

    public String getAd_type() {
        return ad_type;
    }
}
