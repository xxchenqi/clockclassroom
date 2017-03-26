package com.yiju.ClassClockRoom.control;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.eju.cysdk.collection.CYIO;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.base.BaseFragmentActivity;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.map.LocationSingle;
import com.yiju.ClassClockRoom.fragment.BaseFragment;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpRemovalApi;

import java.util.List;

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
//        if (StringUtils.isNotNullString(uid)) {
        Log.d("=====", "loginSuccess");
//        WeimiCount.getInstance().recordLogin(null != uid ? uid : "");
        if (!"-1".equals(uid) && StringUtils.isNotNullString(uid)) {
            CYIO.getInstance().setUid(uid);
        }
//        }
    }

    /**
     * 登出
     *
     * @param isBackstage true：后台：false：退出
     */
    public void loginOut(boolean isBackstage) {
//        String uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
//                UIUtils.getString(R.string.shared_id), "-1");
//        WeimiCount.getInstance().recordLogout(null != uid ? uid : "", isBackstage);
        CYIO.getInstance().setUid("");
    }

    // 页面启动BaseActivity
    private void pageStart(BaseActivity activity) {
        String uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_id), "-1");
        if (activity != null && StringUtils.isNotNullString(activity.getPageName())) {
            /*WeimiCount.getInstance()
                    .recordPagepath(uid, activity.getPageName());*/
        }
    }

    // 页面启动BaseFragment
    private void pageStart(BaseFragment fragment) {
        String uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_id), "-1");
        if (fragment != null) {
            if (StringUtils.isNotNullString(fragment.getPageName())) {
                /*WeimiCount.getInstance()
                        .recordPagepath(uid, fragment.getPageName());*/
            }
        }
    }

    // 页面启动BaseFragment
    private void pageStart(BaseFragmentActivity activity) {
        String uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_id), "-1");
        if (activity != null) {
            if (StringUtils.isNotNullString(activity.getPageName())) {
                /*WeimiCount.getInstance()
                        .recordPagepath(uid, activity.getPageName());*/
            }
        }
    }

    /**
     * 切换前台
     */
    public <T> void skipRuning(T t) {
        String uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_id), "-1");
        isRuning = SharedPreferencesUtils.getBoolean(
                UIUtils.getContext(),
                SharedPreferencesConstant.Shared_Count_IsRunningForeground,
                true);
        if (!isRuning) {
            if ("-1".equals(uid)) {
                uid = "";
            }
//            CountControl.getInstance().loginSuccess(uid);
            SharedPreferencesUtils.saveBoolean(UIUtils.getContext(),
                    SharedPreferencesConstant.Shared_Count_IsRunningForeground,
                    true);
            long skipRunningTime = System.currentTimeMillis();
            long c = (skipRunningTime - unRunningTime) / 1000 / 60 / 60;
            //判断后台切换到前台时间间隔是否超过了半天,超过半天则重新请求
            if (c > 12) {
                HttpRemovalApi.getInstance().getHttpRequestForServer(false, null);
            }
        }
        if (t instanceof BaseFragment) {
            pageStart((BaseFragment) t);
        } else if (t instanceof BaseActivity) {
            pageStart((BaseActivity) t);
        } else if (t instanceof BaseFragmentActivity && ((BaseFragmentActivity) t).getPageName() != null) {
            pageStart((BaseFragmentActivity) t);
        }
    }


    /**
     * 切换后台
     */
    public void skipUnRuning(Activity activity) {
        if (!BaseApplication.isAppOnForeground(activity)) {
            SharedPreferencesUtils.saveBoolean(UIUtils.getContext(),
                    SharedPreferencesConstant.Shared_Count_IsRunningForeground,
                    false);
            //记录切换到后台时的时间
            unRunningTime = System.currentTimeMillis();
            //注释掉是因为新的bi数据统计不需要切换到后台的时候做处理
//            loginOut(true);
        }
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return flag
     */
    public static boolean isExsitMianActivity(Class<?> cls) {
        Intent intent = new Intent(UIUtils.getContext(), cls);
        ComponentName cmpName = intent.resolveActivity(UIUtils.getContext().getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) UIUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }
}
