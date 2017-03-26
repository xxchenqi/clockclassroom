package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

public class RoomAdjustEntity implements Serializable {
    private List<AdjustmentData> add_date;
    private List<AdjustmentData> cancel_date;

    public List<AdjustmentData> getAdd_date() {
        return add_date;
    }

    public List<AdjustmentData> getCancel_date() {
        return cancel_date;
    }
}
