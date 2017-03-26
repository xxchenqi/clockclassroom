package com.yiju.ClassClockRoom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

public class LockReceiver extends BroadcastReceiver {
    private String action = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (action != null && action.equals(intent.getAction())) {
            return;
        }
        action = intent.getAction();
//        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏

//        } else
        if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            boolean isRuning = SharedPreferencesUtils.getBoolean(
                    UIUtils.getContext(),
                    SharedPreferencesConstant.Shared_Count_IsRunningForeground,
                    true);
            if (isRuning) {
                SharedPreferencesUtils.saveBoolean(UIUtils.getContext(),
                        SharedPreferencesConstant.Shared_Count_IsRunningForeground,
                        false);
                //注释掉是因为新的bi数据统计不需要切换到后台的时候做处理
//                CountControl.getInstance().loginOut(true);
            }
        }
//        else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁

//        }
    }

}
