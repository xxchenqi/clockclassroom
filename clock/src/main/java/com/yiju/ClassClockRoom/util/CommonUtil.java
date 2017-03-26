package com.yiju.ClassClockRoom.util;


import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.BaseApplication;

import java.util.List;

/**
 * 公共工具
 */
public class CommonUtil {

    /**
     * 是否有SD卡
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获得文件路径
     */
    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
        }
    }

    /**
     * 检查网络状态
     */
    public static boolean checkNetState(Context context) {
        boolean netstate = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        netstate = true;
                        break;
                    }
                }
            }
        }
        return netstate;
    }

    /**
     * 获得屏幕宽
     *
     * @param context 上下文
     */
    public static int getScreenWidth(Context context) {
        if (context == null) {
            context = BaseApplication.mForegroundActivity;
        }
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获得屏幕高
     *
     * @param context 上下文
     */
    public static int getScreenHeight(Context context) {
        if (context == null) {
            context = BaseApplication.mForegroundActivity;
        }
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }


    /**
     * 获得屏幕宽
     */
    public static int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        BaseApplication.mForegroundActivity.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获得屏幕高
     */
    public static int getScreenHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        BaseApplication.mForegroundActivity.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.heightPixels;
    }


    /**
     * 文本框长度限制
     *
     * @param length 长度
     */
    public static InputFilter getTextLengthFilter(final int length) {
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                try {
                    int destLen = dest.length();
                    int sourceLen = source.length();
                    if (destLen + sourceLen > length) {
                        UIUtils.showToastSafe(BaseApplication.mForegroundActivity
                                .getString(R.string.toast_course_name_length));
                        return "";
                    }
                    if (source.length() < 1 && (dend - dstart >= 1)) {
                        return dest.subSequence(dstart, dend - 1);
                    }
                    return source;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        };
        return inputFilter;
    }

    /**
     * 拼接图片链接
     *
     * @param url url
     * @return s
     */
    public static String jointHeadUrl(String url, int width, int height) {
        return url + ".jpeg@imageView2/1/w/" + width + "/h/" + height;
    }


//    /**废弃
//     * 拼接图片链接(如果没有后缀添加后缀)
//     *
//     * @param url     url
//     * @param replace 替换大小 ："_350X350_0_0_0"
//     * @return s
//     */
//    public static String jointHeadUrl(String url, String replace) {
//        if (url.matches(".*(\\d{1,3}X\\d{1,3}).*") || StringUtils.isNullString(url)) {
//            return url;
//        } else {
//            StringBuilder sb = new StringBuilder(url);
//            url = sb.insert(sb.length() - 4, replace).toString();
//        }
//        return url;
//    }


    /**
     * 判断当前Activity是否在栈顶！
     *
     * @param cmdName 类名
     * @param context 上下文
     */
    public static boolean isTopActivity(String cmdName, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        String cmpNameTemp = null;
        if (null != runningTaskInfos) {
            cmpNameTemp = runningTaskInfos.get(0).topActivity.getClassName();
        }

        return null != cmpNameTemp && cmpNameTemp.equals(cmdName);
    }
}
