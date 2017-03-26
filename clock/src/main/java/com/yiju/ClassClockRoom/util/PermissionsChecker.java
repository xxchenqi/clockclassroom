package com.yiju.ClassClockRoom.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

/**
 * 权限检测类
 * Created by wh on 2016/4/12.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PermissionsChecker {

    private final Context mContext;
    public static final int REQUEST_EXTERNAL_STORAGE = 1;

    //首页所需权限
    public static final String[] INDEX_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,//粗略定位
            Manifest.permission.ACCESS_FINE_LOCATION,//精确定位
            Manifest.permission.READ_EXTERNAL_STORAGE,//读取
            Manifest.permission.WRITE_EXTERNAL_STORAGE//写入
    };
    //定位权限
    public static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,//粗略定位
            Manifest.permission.ACCESS_FINE_LOCATION//精确定位
    };
    // 读写SD卡权限
    public static final String[] READ_WRITE_SDCARD_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,//读取
            Manifest.permission.WRITE_EXTERNAL_STORAGE//写入
    };
    //读取通讯录权限
    public static final String[] READ_CONTACTS_PERMISSIONS = new String[]{
            Manifest.permission.READ_CONTACTS
    };
    //获取设备权限
    public static final String[] READ_PHONE_STATE_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    //获取打电话权限
    public static final String[] CALL_PHONE_PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE
    };


    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 检查是否开启了定位权限
     *
     * @return true：缺少权限  false :不缺
     */
    public static boolean checkPermission(String... permissions) {
        return lacksPermissions(permissions);
    }

    /**
     * activity 请求权限
     *
     * @param activity    ac
     * @param permissions per
     */
    public static void requestPermissions(Activity activity, String... permissions) {
        ActivityCompat.requestPermissions(
                activity,
                permissions,
                REQUEST_EXTERNAL_STORAGE
        );
    }

    /**
     * fragment 请求权限
     *
     * @param fragment    fg
     * @param permissions per
     */
    public static void requestPermissions(Fragment fragment, String... permissions) {
        fragment.requestPermissions(
                permissions,
                REQUEST_EXTERNAL_STORAGE
        );
    }

    // 判断权限集合
    public static boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private static boolean lacksPermission(String permission) {
        int per = ActivityCompat.checkSelfPermission(UIUtils.getContext(), permission);
        /*return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;*/
        return per != PackageManager.PERMISSION_GRANTED;
    }

    // 判断是否含有全部的权限
    public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

}
