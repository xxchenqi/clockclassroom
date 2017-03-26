package com.yiju.ClassClockRoom.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.PersonalCenter_BindingThreeWayAccountActivity;
import com.yiju.ClassClockRoom.bean.WorkingPayResult;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.receiver.PushClockReceiver;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.MD5;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class OtherLoginControl {
    private static final int Third_Source_QQ = 1;
    private static final int Third_Source_WX = 2;
    private static final int Third_Source_Sina = 3;
    private static final String Md5Key = "Eju_classroom_20150827";
    public static int third_source;
    public static String THIRD_ID;

    /**
     * 注销本次登录
     */
    public static void logout(Activity mActivity, UMShareAPI mShareAPI, final SHARE_MEDIA platform) {
        mShareAPI.deleteOauth(mActivity, platform, new UMAuthListener() {

            @Override
            public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {

            }

            @Override
            public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> arg2) {

            }

            @Override
            public void onCancel(SHARE_MEDIA arg0, int arg1) {

            }
        });
    }

    /**
     * 授权。如果授权成功，则获取用户信息</br>
     */
    public static void login(final Activity mActivity, final UMShareAPI mShareAPI, final SHARE_MEDIA platform) {
        if (platform == SHARE_MEDIA.QQ) {
            third_source = Third_Source_QQ;
        } else if (platform == SHARE_MEDIA.WEIXIN) {
            third_source = Third_Source_WX;
        } else if (platform == SHARE_MEDIA.SINA) {
            third_source = Third_Source_Sina;
        }

        mShareAPI.doOauthVerify(mActivity, platform, new UMAuthListener() {

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Log.e("", t.toString());
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                // 修改
                String uid;
                if (platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.QQ) {
                    uid = data.get("openid");
                } else {
                    uid = data.get("uid");
                }
                if (!TextUtils.isEmpty(uid)) {
                    getUserInfo(mActivity, mShareAPI, platform, uid);
                } else {
                    UIUtils.showToastSafe("授权失败...");
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {

            }
        });
    }


    /**
     * 获取授权平台的用户信息</br>
     */
    private static void getUserInfo(Activity mActivity, UMShareAPI mShareAPI, final SHARE_MEDIA platform, final String uidTrl) {
        mShareAPI.getPlatformInfo(mActivity, platform, new UMAuthListener() {

            @Override
            public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {

            }

            @SuppressLint("HardwareIds")
            @Override
            public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> info) {
                if (info != null) {
                    // 修改
                    if (StringUtils.isNullString(PushClockReceiver.cid)) {
                        PushClockReceiver.cid = SharedPreferencesUtils.getString(
                                BaseApplication
                                        .getmForegroundActivity(),
                                SharedPreferencesConstant.Shared_Login_Cid,
                                "");
                    }
                    int timestamp = (int) (System.currentTimeMillis() / 1000);
                    String uid;
                    String name;
                    String icon;
                    String unionId = null;
                    if (platform == SHARE_MEDIA.WEIXIN) {
                        uid = String.valueOf(info.get("openid"));
                        unionId = String.valueOf(info.get("unionid"));
                        name = String.valueOf(info.get("nickname"));
                        icon = String.valueOf(info.get("headimgurl"));
                    } else {
                        if (platform == SHARE_MEDIA.QQ) {
                            uid = String.valueOf(info.get("openid"));
                        } else {
                            uid = String.valueOf(info.get("uid"));
                        }
                        name = String.valueOf(info.get("screen_name"));
                        icon = String.valueOf(info
                                .get("profile_image_url"));
                    }

                    if (StringUtils.isNullString(uid)) {
                        uid = uidTrl;
                    }

                    String sign;
                    try {
                        sign = MD5.md5(uid + "|" + timestamp + "|"
                                + Md5Key);
                        String deviceId = ((TelephonyManager) BaseApplication.getmForegroundActivity()
                                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

                        PostOtherLogin(third_source, uid, unionId, timestamp,
                                name, icon, sign, deviceId,
                                PushClockReceiver.cid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA arg0, int arg1) {

            }
        });
    }

    private static void PostOtherLogin(final int third_source, final String third_id, String third_id_old,
                                       int timestamp, final String nickname, final String avatar,
                                       String sign, String device_token, String cid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "third_login");
        if (StringUtils.isNotNullString(third_id_old)) {
            params.addBodyParameter("third_id_old", third_id_old);
        }
        params.addBodyParameter("third_source", third_source + "");
        params.addBodyParameter("third_id", third_id);
        params.addBodyParameter("timestamp", timestamp + "");
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("sign", sign);
        params.addBodyParameter("device_token", device_token);
        if (StringUtils.isNotNullString(cid)) {
            params.addBodyParameter("cid", cid);
        }

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe("登录失败!");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            JSONObject jsonObject = new JSONObject(arg0.result);
                            int code = jsonObject.getInt("code");
                            if (code == 1) {
                                String id = jsonObject.getString("id");
                                if (StringUtils.isNotNullString(id)) {
                                    boolean isRuning = SharedPreferencesUtils.getBoolean(
                                            UIUtils.getContext(),
                                            SharedPreferencesConstant.Shared_Count_IsRunningForeground,
                                            true);
                                    if (isRuning) {
                                        CountControl.getInstance().loginSuccess(id);
                                    }
                                    Context context = BaseApplication
                                            .getmForegroundActivity();
                                    SharedPreferencesUtils.saveBoolean(
                                            context,
                                            context.getResources().getString(
                                                    R.string.shared_isLogin),
                                            true);
                                    SharedPreferencesUtils.saveString(
                                            context,
                                            context.getResources().getString(
                                                    R.string.shared_id), id);
                                    SharedPreferencesUtils.saveString(
                                            context,
                                            context.getResources().getString(R.string.shared_username),
                                            third_id
                                    );
                                    SharedPreferencesUtils.saveString(
                                            context,
                                            context.getResources().getString(R.string.shared_third_source),
                                            third_source + ""
                                    );
                                    //名字和头像都不要,都去getInfo里的信息
//                                    SharedPreferencesUtils.saveString(
//                                            context,
//                                            context.getString(R.string.shared_nickname),
//                                            nickname);
//                                    SharedPreferencesUtils.saveString(
//                                            context,
//                                            context.getResources().getString(
//                                                    R.string.shared_avatar),
//                                            avatar);
                                    //获取支付所需的工作密钥
                                    getWorkingKey(id);
                                }
                                BaseApplication.getmForegroundActivity().setResult(Activity.RESULT_OK);
                                BaseApplication.getmForegroundActivity()
                                        .finish();
                            } else {
                                UIUtils.showToastSafe(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }


//==============================第三方账号绑定请求=======================


    /**
     * 绑定
     */
    public static void binding(final Activity mActivity, final UMShareAPI mShareAPI, final SHARE_MEDIA platform) {


        if (platform == SHARE_MEDIA.QQ) {
            third_source = Third_Source_QQ;
        } else if (platform == SHARE_MEDIA.WEIXIN) {
            third_source = Third_Source_WX;
        } else if (platform == SHARE_MEDIA.SINA) {
            third_source = Third_Source_Sina;
        }

        mShareAPI.doOauthVerify(mActivity, platform, new UMAuthListener() {

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                // 登录成功后修改三方ID,回调到activity后去做请求，并刷新界面。
                if (platform == SHARE_MEDIA.WEIXIN) {
                    THIRD_ID = data.get("unionid");
                    ((PersonalCenter_BindingThreeWayAccountActivity) BaseApplication.mForegroundActivity).refresh();
                } else {
                    THIRD_ID = data.get("uid");
                    ((PersonalCenter_BindingThreeWayAccountActivity) BaseApplication.mForegroundActivity).refresh();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
            }
        });
    }

    //请求工作密钥api
    private static void getWorkingKey(String uid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getWorkingKey");
        params.addBodyParameter("uid", uid);
//        params.addBodyParameter("expire_second", "");//密钥过期秒数，可不传，默认7天

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_API_PAY, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForWorkingKey(arg0.result);
                    }
                });
    }

    private static void processDataForWorkingKey(String result) {
        WorkingPayResult workingPayResult = GsonTools.changeGsonToBean(result, WorkingPayResult.class);
        if (workingPayResult == null) {
            return;
        }
        if ("1".equals(workingPayResult.getCode())) {
            //存储易支付初始化所需的工作密钥等参数
            EjuPaySDKUtil.saveEjuPaySdkParam(workingPayResult);
            //易支付初始化
            EjuPaySDKUtil.initEjuPaySDK(null);
        }
    }
}
