package com.yiju.ClassClockRoom.bean;

/**
 * Created by Sandy on 2016/6/15/0015.
 */
public class SchoolInfo {
    public SchoolInfo(String name) {
        this.name = name;
    }

    private String id;
    private String name;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
