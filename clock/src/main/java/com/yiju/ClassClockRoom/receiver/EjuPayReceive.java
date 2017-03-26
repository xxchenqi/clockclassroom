package com.yiju.ClassClockRoom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;

/**
 * EjuPay 验签错误广播接收器
 * Created by wh on 2016/8/4.
 */
public class EjuPayReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("ejupay_sign_error_action")) {
            //签名问题广播
//            String msg= intent.getStringExtra("SignError");
            if (!EjuPaySDKUtil.isRunning) {
                EjuPaySDKUtil.clearEjuPaySdkParam();
                EjuPaySDKUtil.ejuPaySDKHandler();
            }
        }
    }
}
