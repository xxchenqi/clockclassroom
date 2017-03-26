package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

public class DayTimeData implements Serializable {
    private String dayTitle;
    private String time;
    private boolean flag;

    public String getDayTitle() {
        return dayTitle;
    }

    public void setDayTitle(String dayTitle) {
        this.dayTitle = dayTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
