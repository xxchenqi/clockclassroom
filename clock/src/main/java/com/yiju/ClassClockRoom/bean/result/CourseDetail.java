package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.CourseDetailData;

/**
 * Created by Sandy on 2016/6/17/0017.
 */
public class CourseDetail extends BaseEntity {

    private CourseDetailData data;

    public CourseDetailData getData() {
        return data;
    }

    public void setData(CourseDetailData data) {
        this.data = data;
    }

}
