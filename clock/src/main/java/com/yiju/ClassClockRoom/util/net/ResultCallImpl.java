package com.yiju.ClassClockRoom.util.net;


import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.util.net.base.IBaseResultCall;

/**
 * 作者： 葛立平
 * 2016/2/15 15:05
 */
 public abstract class ResultCallImpl<T> implements IBaseResultCall<T> {
    public void onStart() {

    }

    public void onCompleted() {

    }

    public void onError(BaseEntity bean) {

    }
}
