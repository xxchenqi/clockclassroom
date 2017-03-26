package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.AccompanyRead;

import java.util.List;

/**
 * 陪读返回信息
 * Created by geLiPing on 2016/8/11.
 */
public class AccompanyReadResult extends BaseEntity {

    List<AccompanyRead> data;

    public List<AccompanyRead> getData() {
        return data;
    }

    public void setData(List<AccompanyRead> data) {
        this.data = data;
    }
}
