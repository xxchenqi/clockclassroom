package com.yiju.ClassClockRoom.util.net.base;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;

/**
 * 作者： 葛立平
 * 2016/2/15 15:03
 */
public interface IBaseResultCall<T> {
    void onStart();

    void onCompleted();

    void onError(BaseEntity bean);

    void onNext(T bean);
}
