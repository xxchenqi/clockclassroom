package com.yiju.ClassClockRoom.bean;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/6/29 10:31
 * ----------------------------------------
 */
public class CourseParticularsInfo {

    /**
     * date : 2016-06-27
     * start_time : 900
     * end_time : 1000
     * no : A302
     * name : 测试闵行新乐坊中心店
     */

    private String date;
    private String start_time;
    private String end_time;
    private String no;
    private String name;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }
}
