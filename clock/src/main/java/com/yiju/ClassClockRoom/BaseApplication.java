package com.yiju.ClassClockRoom;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.bugtags.library.Bugtags;
import com.eju.cysdk.collection.CYConfig;
import com.eju.cysdk.collection.CYIO;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.yiju.ClassClockRoom.bean.ReservationTwo.DataEntity;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.sdcard.CrashHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseApplication extends MultiDexApplication {
    //环境 1:线下环境 2:线上测试 3:正式环境
    public static Integer FORMAL_ENVIRONMENT = BuildConfig.ENVIRONMENT;
    public static boolean BugTagsFlag;
    public static boolean LogFlag;

    //沉浸式开关
    public static final boolean ImmersionFlag = false;

    // BUG-keyID
    private static final String BugTagsId = "2c6c06f29111c6df6d0686089dd81189";
    // 统计APPID
    private static final String CountAppId = "1066663095";

    private static BaseApplication mContext;

    public static Activity mForegroundActivity;

    private static Handler mMainHandler;

    private static Thread mMainThread;

    private static int mMainThreadId;

    private static Looper mMainThreadLooper;

    private List<DataEntity> mLists = new ArrayList<>();
    private int count;// 记录订单的数量
    private boolean check;// 记录订单是否被选中
    private float price = 0;// 记录订单的价格
    private Map<String, Integer> mOrder = new HashMap<>();// 存放选中的订单id
    private String position;
    private String couponID;
    //由于做了分包，所以数据统计要求加此判断（如有分包，则只初始化一次）
    private boolean isInitCYSDK = false;

    public String getCouponID() {
        return couponID;
    }

    public void setCouponID(String couponID) {
        this.couponID = couponID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Map<String, Integer> getmOrder() {
        return mOrder;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getProcessName(this);
        if (processName!= null) {
            if(processName.equals("com.yiju.ClassClockRoom")){
                switch (FORMAL_ENVIRONMENT) {
                    case 1:
                        //线下
                        LogFlag = true;
                        BugTagsFlag = true;
                        break;
                    case 2:
                        //线上测试
                        LogFlag = true;
                        BugTagsFlag = true;
                        break;
                    case 3:
                        //正式
                        LogFlag = false;
                        BugTagsFlag = false;
                        break;
                }

                // 百度地图初始化
                SDKInitializer.initialize(getApplicationContext());
                mContext = (BaseApplication) getApplicationContext();
                mMainHandler = new Handler();
                mMainThread = Thread.currentThread();
                mMainThreadId = android.os.Process.myTid();
                mMainThreadLooper = getMainLooper();
                // BugTags初始化
                if (BugTagsFlag) {
                    Bugtags.start(BugTagsId, this, Bugtags.BTGInvocationEventBubble);
                }
//        initCeash();
                // 初始化友盟
                initUmeng();
                // 统计初始化
                initCount();
                EjuPaySDKUtil.initEjuPaySDK(null);

            }
        }
    }


    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    private void initUmeng() {
        PlatformConfig.setWeixin("wxbde359c10bcaefde",
                "f03e9cb497c5833d188499a70e877394");
        PlatformConfig.setQQZone("1104992742", "0iyd7v8xL8Wd94YQ");
        PlatformConfig.setSinaWeibo("4678972",
                "102969f5b1202cc68232fdd53f58db43");
    }

    /**
     * 初始化统计
     */
    private void initCount() {
        //umeng bi
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        //场景类型设置(普通统计场景类型)
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        /*String channelId = getMetaDataValue("CHANNEL_NAME");//渠道号
        if (StringUtils.isNullString(channelId)) {
            channelId = "2010031003";                       //默认web渠道
        }
        WeimiCountConfiguration.Builder builder = new WeimiCountConfiguration.Builder(
                getApplicationContext());
        builder.setAppId(CountAppId);
        builder.setDeviceId(UniqueID.getNDeviceID(getApplicationContext()));
        builder.setChannelId(channelId);
        WeimiCount.getInstance().init(builder.build());*/
        if (!isInitCYSDK) {
            //新的BI数据统计初始化
            switch (FORMAL_ENVIRONMENT) {
                case 1:
                case 2:
                    CYConfig.isTest = true;//true 为发送到测试地址，false为发送到证书地址
                    break;
                case 3:
                    CYConfig.isTest = false;//true 为发送到测试地址，false为发送到证书地址
                    break;
            }
            CYIO.startTracing(getApplicationContext(), "1066663095");
            if (!"-1".equals(StringUtils.getUid())) {
                CYIO.getInstance().setUid(StringUtils.getUid());
            }else{
                CYIO.getInstance().setUid("");
            }
            isInitCYSDK = true;
        }
        SharedPreferencesUtils.saveBoolean(UIUtils.getContext(),
                SharedPreferencesConstant.Shared_Count_IsRunningForeground,
                true);
    }

    /**
     * 获取全局上下文
     *
     * @return mContext
     */
    public static BaseApplication getApplication() {
        return mContext;
    }

    /**
     * 获取主线程handler
     *
     * @return mMainHandler
     */
    public static Handler getMainThreadHandler() {
        return mMainHandler;
    }

    /**
     * 获取主线程
     *
     * @return mMainThread
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程id
     *
     * @return mMainThreadId
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程轮询器
     *
     * @return mMainThreadLooper
     */
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public void setList(List<DataEntity> mListss) {
        mLists = mListss;
    }

    public List<DataEntity> getList() {
        return mLists;
    }

    public static Activity getmForegroundActivity() {
        return mForegroundActivity;
    }

    public static void setmForegroundActivity(Activity mForegroundActivity) {
        BaseApplication.mForegroundActivity = mForegroundActivity;
    }

    // 获取清单文件的metaData
    private String getMetaDataValue(String key) {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 程序是否在前台运行
     *
     * @return boolean
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 崩溃记录初始化
     */
    private void initCeash() {

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

}
