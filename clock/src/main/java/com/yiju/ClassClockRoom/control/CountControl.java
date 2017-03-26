package com.yiju.ClassClockRoom.control;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.eju.cysdk.collection.CYIO;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.map.LocationSingle;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * 统计逻辑
 *
 * @author geliping
 */
public class CountControl {
    // 新浪微博注册
    public static final String Register_Type_Sina = "1";
    // 微信注册
    public static final String Register_Type_Wechat = "2";
    // 手机注册
    public static final String Register_Type_Phone = "3";

    private static CountControl sInstance;
    private boolean isfirstLocation = true;
    private boolean isRuning;
    public long unRunningTime = 0;

    /**
     * 实例化
     */
    public static CountControl getInstance() {
        if (sInstance == null) {
            sInstance = new CountControl();
        }
        return sInstance;
    }

    // 启动app统计
    public void startApp() {
        BDLocation curr_location = LocationSingle.getInstance().getCurrentLocation();
        if (curr_location != null && isfirstLocation) {
            isfirstLocation = false;
            /*WeimiCount.getInstance().recordClientStart(
                    curr_location.getLongitude(), curr_location.getLatitude());*/
            SharedPreferencesUtils.saveBoolean(UIUtils.getContext(),
                    SharedPreferencesConstant.Shared_Count_IsRunningForeground,
                    true);
        }
    }

    // 注册成功统计
    public void registerSuccess(String uid, String type) {
        BDLocation location = LocationSingle.getInstance().getCurrentLocation();
        if (location != null) {
            /*WeimiCount.getInstance().recordRegister(null != uid ? uid : "", type,
                    location.getLongitude(), location.getLatitude());*/
            if (!"-1".equals(uid) && StringUtils.isNotNullString(uid)) {
                CYIO.getInstance().setUid(uid);
            }
        }
    }

    // 登录成功统计
    public void loginSuccess(String uid) {
        Log.d("=====", "loginSuccess");
        if (!"-1".equals(uid) && StringUtils.isNotNullString(uid)) {
            CYIO.getInstance().setUid(uid);
        }
    }

    /**
     * 登出
     */
    public void loginOut() {
        CYIO.getInstance().setUid("");
    }

}
