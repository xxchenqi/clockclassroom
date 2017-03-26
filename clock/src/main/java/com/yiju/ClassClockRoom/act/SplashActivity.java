package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import com.igexin.sdk.PushManager;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.control.map.LocationSingle;
import com.yiju.ClassClockRoom.util.net.api.HttpRemovalApi;

/**
 * 暂时不检测版本，1秒后跳转到主页面
 *
 * @author len
 */
public class SplashActivity extends BaseActivity {

    public static boolean isLocationInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
            isLocationInit = false;
        } else {
            // 定位
            isLocationInit = true;
            LocationSingle.getInstance().init(this.getApplicationContext(), true);
        }
        PushManager.getInstance().initialize(this.getApplicationContext());
        DataManager.isRequest = false;

        //服务器迁移_判断请求url
        HttpRemovalApi.getInstance().getHttpRequestForServer(true);
        CountControl.getInstance().unRunningTime = System.currentTimeMillis();
    }


    @Override
    public int setContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_launch);
    }
}
