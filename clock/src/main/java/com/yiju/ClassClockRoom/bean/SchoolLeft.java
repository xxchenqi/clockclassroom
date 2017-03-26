package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/9/5/0005.
 */
public class SchoolLeft {
    private String id;
    private String dist_name;
    private boolean flag = false;
    /**
     * sid : -1
     * school_name : 全部门店
     */

    private List<SchoolRight> school_list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDist_name() {
        return dist_name;
    }

    public void setDist_name(String dist_name) {
        this.dist_name = dist_name;
    }

    public List<SchoolRight> getSchool_list() {
        return school_list;
    }

    public void setSchool_list(List<SchoolRight> school_list) {

        this.school_list = school_list;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
