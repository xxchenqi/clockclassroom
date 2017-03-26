package com.yiju.ClassClockRoom.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.control.map.LocationSingle;

public class UIUtils {

    public static Context getContext() {
        return BaseApplication.getApplication();
    }

    public static Thread getMainThread() {
        return BaseApplication.getMainThread();
    }

    private static long getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取主线程的handler
     */
    private static Handler getHandler() {
        return BaseApplication.getMainThreadHandler();
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     */
    private static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        if (BaseApplication.mForegroundActivity == null) {
            return getContext().getResources().getString(resId);
        } else {
            return BaseApplication.mForegroundActivity.getResources().getString(resId);
        }
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    // 判断当前的线程是不是在主线程
    private static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public static void startActivity(Intent intent) {

        Activity activity = BaseApplication.getmForegroundActivity();
        if (activity != null) {
            activity.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }

    /**
     * 对toast的简易封装。线程安全，可以在非UI线程调用。
     */
    public static void showToastSafe(final int resId) {
        showToastSafe(getString(resId));
    }

    /**
     * 对toast的简易封装。线程安全，可以在非UI线程调用。
     */
    public static void showToastSafe(final String str) {
        if (isRunInMainThread()) {
            showToast(str);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(str);
                }
            });
        }
    }

    /**
     * 对toast的简易封装。线程安全，可以在非UI线程调用。
     */
    public static void showLongToastSafe(final String str) {
        if (isRunInMainThread()) {
            showLongToast(str);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showLongToast(str);
                }
            });
        }
    }

    private static void showToast(String str) {
        if (BaseApplication.getApplication() != null) {
            Toast toast = Toast.makeText(BaseApplication.getApplication(), str,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            // ToastUtil.showToast(BaseApplication.getApplication(), str);
        }
    }

    private static void showLongToast(String str) {
        if (BaseApplication.getApplication() != null) {
            Toast toast = Toast.makeText(BaseApplication.getApplication(), str,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            // ToastUtil.showToast(BaseApplication.getApplication(), str);
        }
    }

    /**
     * 获取控件宽度
     *
     * @param v v
     * @return width
     */
    public static int getWidth(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredWidth();
    }

    /**
     * 获取控件高度
     *
     * @param v v
     * @return i
     */
    public static int getHeight(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredHeight();
    }

    /**
     * 获取当前经度
     *
     * @return lng
     */
    public static String getLng() {
        String lng = "";
        BDLocation currentLocation = LocationSingle.getInstance()
                .getCurrentLocation();

        if (currentLocation != null) {
            lng = currentLocation.getLongitude() + "";
        }
        return lng;
    }

    /**
     * 获取当前维度
     *
     * @return lat
     */
    public static String getLat() {
        String lat = "";
        BDLocation currentLocation = LocationSingle.getInstance()
                .getCurrentLocation();

        if (currentLocation != null) {
            lat = currentLocation.getLatitude() + "";
        }
        return lat;
    }

    /**
     * 检查网络
     *
     * @param ly_wifi             wifi 布局
     * @param iv_no_wifi          wifi图标
     * @param tv_no_wifi_content1 wifi 内容1
     * @param tv_no_wifi_content2 wifi 内容2
     */
    public static void checkWifiAndModifyWifi(RelativeLayout ly_wifi, ImageView iv_no_wifi, TextView tv_no_wifi_content1, TextView tv_no_wifi_content2) {
        ly_wifi.setVisibility(View.VISIBLE);
        if (NetWorkUtils.getNetworkStatus(UIUtils.getContext())) {
            //有网络情况,服务器故障
            iv_no_wifi.setImageResource(R.drawable.broken);
            tv_no_wifi_content1.setText(UIUtils.getString(R.string.broken_content1));
            tv_no_wifi_content2.setText(UIUtils.getString(R.string.broken_content2));
        } else {
            //无网络情况,网络失败
            iv_no_wifi.setImageResource(R.drawable.none_wifi);
            tv_no_wifi_content1.setText(UIUtils.getString(R.string.nowifi_content1));
            tv_no_wifi_content2.setText(UIUtils.getString(R.string.nowifi_content2));
        }
    }

}
