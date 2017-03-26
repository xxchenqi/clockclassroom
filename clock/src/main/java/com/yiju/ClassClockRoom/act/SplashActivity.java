package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;
import com.weimi.push.YouYunHttpCallback;
import com.weimi.push.YouyunInstance;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.control.map.LocationSingle;
import com.yiju.ClassClockRoom.util.LogUtil;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpRemovalApi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 暂时不检测版本，1秒后跳转到主页面
 *
 * @author len
 */
public class SplashActivity extends BaseActivity {

    public static boolean isLocationInit;
    public static boolean isClick;
    private boolean isOnline;
    public static String youYunUDid;

    private ImageView iv_splash_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //umeng统计测试模式设置
        switch (BaseApplication.FORMAL_ENVIRONMENT) {
            case 1:
                isOnline = false;
                //友盟统计
                MobclickAgent.setDebugMode(true);
                break;
            case 2:
            case 3:
                isOnline = true;
                MobclickAgent.setDebugMode(false);
                break;
        }

        isClick = false;
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
        initYouYunPushSDK();

        LogUtil.i("deviceInfo----", StringUtils.getDeviceInfo(this));
    }

    private void initYouYunPushSDK() {
        String udid = StringUtils.generateOpenUDID();
        String clientId;
        String clientSecret;

        if (isOnline) {//正式you yun key
            clientId = "1-20115-b4fe7681dff0100a32c6af6331c85202-andriod";
            clientSecret = "9177e74df234d03b31fd8a48ea54a7fc";
        } else {//测试
            clientId = "1-20135-ba41200e282fd059d365ca72fad3c9f3-andriod";
            clientSecret = "d9efb154f8aa7fe76fd0400af0bd87ee";
        }
        // first step 初始化SDK
        YouyunInstance.getInstance().initSDK(UIUtils.getContext(), clientId, clientSecret, udid, isOnline, new YouYunHttpCallback() {

            @Override
            public void onResponse(String s) {
                LogUtil.i("YouYun Push :", "response:" + s);
                if (!TextUtils.isEmpty(s)) {
                    try {
                        JSONObject object = new JSONObject(s);
                        int status = object.getInt("apistatus");
                        String result = object.getString("result");
                        String regId = MiPushClient.getRegId(getApplicationContext());
                        youYunUDid = result;
                        //存储cid 为登录时传给服务器所用
                        SharedPreferencesUtils.saveString(getApplicationContext(),
                                SharedPreferencesConstant.Shared_Login_Udid, youYunUDid);
                        if (1 == status) {
                            // second step 启动PUSH
                            boolean startResult = YouyunInstance.getInstance().startPush(SplashActivity.this, isOnline, true);
                            if (startResult) {
                                // third step 注册用户信息,首次创建用户，必须调用次方法,之后随意
                                YouyunInstance.getInstance().registerEquipment(SplashActivity.this, null, 120, "register");
                            }
                        }
                        LogUtil.i("YouYun Push :", "result:" + result + "---status:" + status + "---regId--" + regId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                LogUtil.i("YouYun Push :", "error:" + e.getMessage());
            }
        }, 60);
    }


    @Override
    public int setContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        iv_splash_bg = (ImageView) findViewById(R.id.iv_splash_bg);
    }

    @Override
    public void initData() {
        //服务器迁移_判断请求url
        HttpRemovalApi.getInstance().getHttpRequestForServer(true, iv_splash_bg);
        CountControl.getInstance().unRunningTime = System.currentTimeMillis();
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_launch);
    }
}
