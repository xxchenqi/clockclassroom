package com.yiju.ClassClockRoom.common.callback;

import com.yiju.ClassClockRoom.util.net.ClassEvent;

/**
 * 作者： 葛立平
 * 2016/3/1 11:38
 */
public interface InitView {
    void initIntent();

    void initListener();

    void onRefreshEvent(ClassEvent<Object> event);

//    void handleHavePermission();
}
