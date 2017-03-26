package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

public class DayTimeAllData implements Serializable{
    private List<DayTimeData> list;

    public List<DayTimeData> getList() {
        return list;
    }

    public void setList(List<DayTimeData> list) {
        this.list = list;
    }
}
