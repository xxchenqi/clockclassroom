package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/6/29 10:30
 * ----------------------------------------
 */
public class CourseParticularsData {

    private String course_name;
    private List<CourseParticularsInfo> complete;
    private List<CourseParticularsInfo> nocomplete;
    private List<CourseParticularsInfo> remove;

    public String getCourse_name() {
        return course_name;
    }

    public List<CourseParticularsInfo> getComplete() {
        return complete;
    }

    public List<CourseParticularsInfo> getNocomplete() {
        return nocomplete;
    }

    public List<CourseParticularsInfo> getRemove() {
        return remove;
    }
}
