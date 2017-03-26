package com.yiju.ClassClockRoom.common.callback;

import android.view.View;

/**
 * listview点击事件接口(两个参数)
 *
 */
public interface ListItemClickTwoData {
    void onClickItem(int position);
    void onDeleteItem(View v,int position);
}