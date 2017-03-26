package com.yiju.ClassClockRoom.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.control.map.LocationSingle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends BaseSingleton {

    public static String lat = "";
    public static String lng = "";


    /**
     * 实例化
     */
    public static StringUtils getInstance() {
        return getSingleton(StringUtils.class);
    }

    public static String getLat() {
        return lat;
    }

    public static void setLat(String lat) {
        StringUtils.lat = lat;
    }

    public static String getLng() {
        return lng;
    }

    public static void setLng(String lng) {
        StringUtils.lng = lng;
    }

    /**
     * 判断字符串是 空
     *
     * @param str null、“ ”、“null”都返回true
     * @return b
     */
    public static boolean isNullString(String str) {
        return (null == str || str.trim().isEmpty() || "null".equals(str.trim()
                .toLowerCase(Locale.getDefault())));
    }

    /**
     * 判断字符串不是 空
     *
     * @param str null、“ ”、“null”都返回true
     * @return b
     */
    public static boolean isNotNullString(String str) {
        return !isNullString(str);
    }

    public static String formatNullString(String str) {
        return isNotNullString(str) ? str : "";
    }

    /**
     * 特殊比较字符串 ""、null、"null"
     *
     * @param lhs s
     * @param rhs s
     * @return b
     */
    public static boolean equalSpecialStr(String lhs, String rhs) {
        if (isNullString(lhs) && isNullString(rhs)) {
            return true;
        } else {
            if (lhs.equals(rhs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 截取数字
     *
     * @return s
     */
    public static String cutNumber(String content) {
        if (isNotNullString(content)) {
            Pattern p = Pattern.compile("[^0-9]");
            Matcher m = p.matcher(content);
            return m.replaceAll("");
        } else {
            return "";
        }
    }

    /*
     *替换日期格式
     * 2016-05-13 --> 2016/05/13
    */
    public static String replaceDate(String Date) {
        return Date.replaceAll("-", "/");
    }


    /**
     * 时间转换
     * 900 --> 09:00
     * 2100 --> 21:00
     */
    public static String changeTime(String start_time) {
        String h;
        String m;
        if (start_time.length() < 4) {
            h = "0" + start_time.substring(0, 1);
            m = start_time.substring(1);
        } else {
            h = start_time.substring(0, 2);
            m = start_time.substring(2);
        }
        return h + ":" + m;
    }

    /**
     * 重复日期拼接
     * 1,2,3 -- >周一、周二、周三
     */
    public static String getRepeatWeek(String repeat) {
        // repeat: "2,3,4,5,6"
        if ("".equals(repeat)) {
            return "每天";
        } else {
            String[] repeats = repeat.split(",");
            StringBuilder sb = new StringBuilder();
            String week = "";

            for (int i = 0; i < repeats.length; i++) {
                Integer valueOf = Integer.valueOf(repeats[i]);

                switch (valueOf) {
                    case 1:
                        week = "周一";
                        break;
                    case 2:
                        week = "周二";
                        break;
                    case 3:
                        week = "周三";
                        break;
                    case 4:
                        week = "周四";
                        break;
                    case 5:
                        week = "周五";
                        break;
                    case 6:
                        week = "周六";
                        break;
                    case 7:
                        week = "周日";
                        break;
                    default:
                        break;
                }

                if (i == repeats.length - 1) {
                    sb.append(week);
                } else {
                    sb.append(week).append("、");
                }

            }
            return sb.toString();

        }

    }

    /**
     * 根据设备生成一个唯一标识
     *
     * @return s
     */
    public static String generateOpenUDID() {
        // Try to get the ANDROID_ID
        @SuppressLint("HardwareIds") String OpenUDID = Settings.Secure.getString(UIUtils.getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (OpenUDID == null || OpenUDID.equals("9774d56d682e549c")
                | OpenUDID.length() < 15) {
            // if ANDROID_ID is null, or it's equals to the GalaxyTab generic
            // ANDROID_ID or bad, generates a new one
            final SecureRandom random = new SecureRandom();
            OpenUDID = new BigInteger(64, random).toString(16);
        }
        return OpenUDID;
    }

    /**
     * 获取sessionId
     *
     * @return sessionId
     */
    public static String getSessionId() {
        return SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_session_id), "");
    }

    /**
     * 获取用户id
     *
     * @return uid
     */
    public static String getUid() {
        return SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_id), "-1");
    }

    /**
     * 获取username
     *
     * @return username
     */
    public static String getUsername() {
        return SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_username), "");
    }

    /**
     * 获取password
     *
     * @return password
     */
    public static String getPassword() {
        return SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_password), "");
    }

    /**
     * 获取third_source
     *
     * @return third_source
     */
    public static String getThirdSource() {
        return SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_source), "0");
    }

    /**
     * 获取电话
     *
     * @return mobile
     */
    public static String getMobile() {
        return SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_mobile), "");
    }


    /**
     * 获取昵称
     */
    public static String getNickName() {
        return SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_nickname), "");
    }


    /**
     * 获取昵称
     */
    public static String getAvatar() {
        return SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_avatar), "");
    }


    /**
     * 根据经纬度获取距离
     *
     * @param lat "33.33"
     * @param lng "33.33"
     * @return 例:10Km
     */
    public static String getDistanceStr(String lat, String lng) {
        BDLocation location = LocationSingle.getInstance()
                .getCurrentLocation();
        if (location == null) {
            return "0m";
        } else {
            return getDistance(location, lat, lng);
        }
    }

    /**
     * 根据经纬度获取距离
     *
     * @param lat "33.33"
     * @param lng "33.33"
     * @return 例:10.00
     */
    public static double getDistance(String lat, String lng) {
        BDLocation location = LocationSingle.getInstance()
                .getCurrentLocation();
        if (location == null) {
            return 0;
        } else {
            return Double.valueOf(getDistanceKm(location, lat, lng).replaceAll("m|km", ""));
        }
    }

    /**
     * 根据经纬度获取距离km,只做距离排序所用
     *
     * @param bdLocation 当前位置
     * @param lat        "33.33"
     * @param lng        "33.33"
     * @return 例:"10.00"
     */
    private static String getDistanceKm(BDLocation bdLocation, String lat, String lng) {
        if (StringUtils.isNullString(lat) || StringUtils.isNullString(lng)) {
            return "";
        }
        double longitude = bdLocation.getLongitude();
        double latitude = bdLocation.getLatitude();
        SharedPreferencesUtils.saveString(UIUtils.getContext(), "LONGITUDE", longitude + "");
        SharedPreferencesUtils.saveString(UIUtils.getContext(), "LATITUDE", latitude + "");
        if (longitude == 4.9E-324D || latitude == 4.9E-324D) {
            // 默认经纬度
            return "0m";
        }
        LatLng p1 = new LatLng(latitude, longitude);
        LatLng p2 = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        double distance = DistanceUtil.getDistance(p1, p2);
        return NumberUtil.getDecimal((distance / 1000) + "", 1) + "km";
    }

    /**
     * 根据经纬度获取距离m或km
     *
     * @param bdLocation 当前位置
     * @param lat        "33.33"
     * @param lng        "33.33"
     * @return 例:"10.00"
     */
    private static String getDistance(BDLocation bdLocation, String lat, String lng) {
        if (StringUtils.isNullString(lat) || StringUtils.isNullString(lng)) {
            return "";
        }
        double longitude = bdLocation.getLongitude();
        double latitude = bdLocation.getLatitude();
        SharedPreferencesUtils.saveString(UIUtils.getContext(), "LONGITUDE", longitude + "");
        SharedPreferencesUtils.saveString(UIUtils.getContext(), "LATITUDE", latitude + "");
        if (longitude == 4.9E-324D || latitude == 4.9E-324D) {
            // 默认经纬度
            return "0m";
        }
        LatLng p1 = new LatLng(latitude, longitude);
        LatLng p2 = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        double distance = DistanceUtil.getDistance(p1, p2);
        if (distance >= 1000) {
            return NumberUtil.getDecimal((distance / 1000) + "", 1) + "km";
        } else {
            return NumberUtil.getDecimal(distance + "", 1) + "m";
        }
    }

    /**
     * 距离解析 k或者km
     *
     * @param distance 距离字符串
     * @return
     */
    public static String changeDistance(String distance) {
        if (StringUtils.isNotNullString(distance)) {
            Double dis = Double.valueOf(distance);
            if (dis >= 1000) {
                return NumberUtil.getDecimal((dis / 1000) + "", 1) + "km";
            } else {
                return NumberUtil.getDecimal(dis + "", 1) + "m";
            }
        } else {
            return "";
        }
    }

    /**
     * String 转 double 并保留2位小数
     * 6.2041   -->  6.20
     *
     * @param str s
     * @return s
     */
    public static String getDecimal(String str) {
        Double cny = Double.parseDouble(str);//6.2041
        DecimalFormat df = new DecimalFormat("0.00");
        String result = df.format(cny);
        return result + "";
    }

    /**
     * 改日期是否是周末
     *
     * @param date 日期
     * @return true 是周末 ， false 不是周末
     */
    public static boolean isWeekend(String date) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d = format.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        } catch (ParseException e) {
            return false;
        }

    }

    /**
     * 生成随机数，由数字或大小写字母组成
     *
     * @param len
     * @return
     */
    public static String randomStr(int len) {
        if (len == 0) {
            return "";
        }
        int a = (int) (Math.random() * 3);
        if (a == 0) {
            return ((int) (Math.random() * 10)) + randomStr(len - 1);
        } else if (a == 1) {
            return ((char) ((int) (Math.random() * 26) + 65))
                    + randomStr(len - 1);
        } else {
            return ((char) ((int) (Math.random() * 26) + 97))
                    + randomStr(len - 1);
        }
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            return "";
        }
    }

}
