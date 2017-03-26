package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
  * ============================================================
  *
  * 作      者  : Sandy
  *
  * 日      期  : 2016/5/5/0005 15:39
  *
  * 描      述  :
  *
  * ============================================================
  */
public class ReservationDate implements Serializable {

    private List<Date> date;

    public List<Date> getDate() {
        return date;
    }

    public void setDate(List<Date> date) {
        this.date = date;
    }

    private Set<Date> bDate;

    private Map<String, List<DayTimeData>> map;

    public Map<String, List<DayTimeData>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<DayTimeData>> map) {
        this.map = map;
    }

    public Set<Date> getbDate() {
        return bDate;
    }

    public void setbDate(Set<Date> bDate) {
        this.bDate = bDate;
    }

}
