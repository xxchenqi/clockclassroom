package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.MineOrderData;

import java.io.Serializable;
import java.util.ArrayList;

public class MineOrder extends BaseEntity implements Serializable {
    private ArrayList<MineOrderData> data;
    private int time;
    private int commission_rate;//手续费
    public ArrayList<MineOrderData> getData() {
        return data;
    }

    public int getCommission_rate() {
        return commission_rate;
    }

    public int getTime() {
        return time;
    }
}
