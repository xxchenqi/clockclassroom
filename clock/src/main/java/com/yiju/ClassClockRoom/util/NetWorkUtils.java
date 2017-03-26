package com.yiju.ClassClockRoom.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.common.callback.INetWorkRunnable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetWorkUtils {
    private static boolean flag;

    // 判断是否连接
    public static void checkURL(final String url, final INetWorkRunnable iNetWorkRunnable) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                flag = false;
                HttpURLConnection conn;
                try {
                    conn = (HttpURLConnection) new URL("http://" + url).openConnection();
                    conn.setConnectTimeout(1000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        flag = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BaseApplication.getmForegroundActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iNetWorkRunnable.isconnection(flag);
                    }
                });
            }
        }).start();
    }

    //判断WIFI是否打开
    public static boolean isWiFiActive(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.getTypeName().equals("WIFI") && ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean getNetworkStatus(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) UIUtils
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (netType == ConnectivityManager.TYPE_WIFI) {
            return info.isConnected();
        } else if (netType == ConnectivityManager.TYPE_MOBILE) {
            switch (netSubtype) {
                case TelephonyManager.NETWORK_TYPE_UNKNOWN://0
                    return false;
                case TelephonyManager.NETWORK_TYPE_GPRS:// 1
                    return true; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:// 2
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_UMTS:// 3
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:// 4
                    return true; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:// 6
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_1xRTT:// 7
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:// 8
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:// 9
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:// 10
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_IDEN:// 11
                    return true;
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // 12
                    return true;
                case TelephonyManager.NETWORK_TYPE_LTE: // 13
                    return true;
                case TelephonyManager.NETWORK_TYPE_EHRPD: // 14
                    return true;
                case TelephonyManager.NETWORK_TYPE_HSPAP:// 15
                    return true;
//				case TelephonyManager.NETWORK_TYPE_GSM://16
//					return true;
//				case TelephonyManager.NETWORK_TYPE_TD_SCDMA://17
//					return true;
//				case TelephonyManager.NETWORK_TYPE_IWLAN://18
//					return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}
