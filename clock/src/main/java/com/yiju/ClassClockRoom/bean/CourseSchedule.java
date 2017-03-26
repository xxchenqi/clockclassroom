package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/6/16/0016.
 */
public class CourseSchedule {
    private String all_course_hour;
    private String active_course_hour;
    private String date_section;
    /**
     * status : 3
     * time : 2016.06.16 9:00-10:00
     */

    private List<CourseTimeList> time_list;

    public String getAll_course_hour() {
        return all_course_hour;
    }

    public void setAll_course_hour(String all_course_hour) {
        this.all_course_hour = all_course_hour;
    }

    public String getActive_course_hour() {
        return active_course_hour;
    }

    public void setActive_course_hour(String active_course_hour) {
        this.active_course_hour = active_course_hour;
    }

    public String getDate_section() {
        return date_section;
    }

    public void setDate_section(String date_section) {
        this.date_section = date_section;
    }

    public List<CourseTimeList> getTime_list() {
        return time_list;
    }

    public void setTime_list(List<CourseTimeList> time_list) {
        this.time_list = time_list;
    }


}
