package com.yiju.ClassClockRoom.control.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.act.SplashActivity;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.io.File;

/**
 * 地图导航相关
 * Created by wh on 2016/4/29.
 */
public class NavigationUtils {

    private String endLng;//终点经度
    private String endLat;//终点维度
    private String endLng_g;//终点经度
    private String endLat_g;//终点维度
    private String endAddress;//终点地址

    public NavigationUtils(String addressLat, String addressLng, String addressLat_g, String addressLng_g, String endAddress) {
        this.endLat = addressLat;
        this.endLng = addressLng;
        this.endLat_g = addressLat_g;
        this.endLng_g = addressLng_g;
        this.endAddress = endAddress;
    }

    public void setEndLat_g(String endLat_g) {
        this.endLat_g = endLat_g;
    }

    public void setEndLng_g(String endLng_g) {
        this.endLng_g = endLng_g;
    }

    public void setEndLng(String endLng) {
        this.endLng = endLng;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public void navigationHandle(final int pos) {

        LocationSingle.getInstance().init(BaseApplication.getmForegroundActivity(), SplashActivity.isLocationInit,
                new LocationSingle.ILocationRunnable() {
                    @Override
                    public void locationResult(BDLocation bdLocation) {
                        if (bdLocation == null) {
                            return;
                        }
                        String startLng = bdLocation.getLongitude() + "";//起点经度
                        String startLat = bdLocation.getLatitude() + "";//起点维度
                        if (pos == 0) {//百度
                            openBaiduMapByURI(startLat, startLng);
                        } else if (pos == 1) {//高德
                            openAutoNavMapByURI(startLat, startLng);
                        }
                    }
                });
    }

    /**
     * 百度地图
     */
    public void openBaiduMapByURI(String startLat, String startLng) {
        try {
            Intent intent = Intent
                    .getIntent("intent://map/direction?origin=name:"
                            + "我的位置"
                            + "|latlng:"
                            + startLat
                            + ","
                            + startLng
                            + "&destination=name:"
                            + endAddress
                            + "|latlng:" + endLat + "," + endLng
                            + "&mode=driving"
                            + "&coord_type=bd09"
                            + "&src=com.yiju.ClassClockRoom|时钟教室"
                            + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isInstallByread("com.baidu.BaiduMap")) {
                BaseApplication.mForegroundActivity.startActivity(intent); // 启动调用
                Log.e("GasStation", "百度地图客户端已经安装");
            } else {
                Toast.makeText(UIUtils.getContext(), "请先安装百度地图客户端",
                        Toast.LENGTH_LONG).show();
                Log.e("GasStation", "没有安装百度地图客户端");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调取高德地图
     */
    public void openAutoNavMapByURI(String startLat, String startLng) {
        try {
            // 线路规划
            Intent intent = Intent
                    .getIntent("androidamap://route?sourceApplication=时钟教室&slat="
                            + startLat + "&slon="
                            + startLng + "&sname=我的位置&dlat="
                            + endLat_g + "&dlon="
                            + endLng_g + "&dname="
                            + endAddress + "&dev=0&m=0&t=2");
            if (isInstallByread("com.autonavi.minimap")) {
                BaseApplication.mForegroundActivity.startActivity(intent); // 启动调用
                Log.e("GasStation", "高德地图客户端已经安装");
            } else {
                Toast.makeText(UIUtils.getContext(), "请先安装高德地图客户端",
                        Toast.LENGTH_LONG).show();
                Log.e("GasStation", "没有安装高德地图客户端");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    @SuppressLint("SdCardPath")
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}
