package com.yiju.ClassClockRoom.util;


import com.yiju.ClassClockRoom.BaseApplication;

public class LogUtil {

    public static void v(String tag, String msg) {
        if (BaseApplication.LogFlag)
            android.util.Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable t) {
        if (BaseApplication.LogFlag)
            android.util.Log.v(tag, msg, t);
    }

    public static void d(String tag, String msg) {
        if (BaseApplication.LogFlag)
            android.util.Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (BaseApplication.LogFlag)
            android.util.Log.d(tag, msg, t);
    }

    public static void i(String tag, String msg) {
        if (BaseApplication.LogFlag)
            android.util.Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable t) {
        if (BaseApplication.LogFlag)
            android.util.Log.i(tag, msg, t);
    }

    public static void w(String tag, String msg) {
        if (BaseApplication.LogFlag)
            android.util.Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        if (BaseApplication.LogFlag)
            android.util.Log.w(tag, msg, t);
    }

    public static void e(String tag, String msg) {
        if (BaseApplication.LogFlag)
            android.util.Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (BaseApplication.LogFlag)
            android.util.Log.e(tag, msg, t);
    }
}
