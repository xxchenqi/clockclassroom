package com.yiju.ClassClockRoom.util.net.api;

import com.yiju.ClassClockRoom.common.base.BaseSingleton;

/**
 * 预订相关API
 * Created by geliping on 2016/8/11.
 */
public class HttpScheduleApi extends BaseSingleton{
    public static HttpScheduleApi getInstance() {
        return getSingleton(HttpScheduleApi.class);
    }
}
