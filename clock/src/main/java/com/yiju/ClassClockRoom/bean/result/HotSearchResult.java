package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.HotSearch;

import java.util.List;

/**
 * 热门搜索返回结果
 * Created by geliping on 2016/8/11.
 */
public class HotSearchResult extends BaseEntity{
    private List<HotSearch> data;

    public List<HotSearch> getData() {
        return data;
    }

    public void setData(List<HotSearch> data) {
        this.data = data;
    }
}
