package com.yiju.ClassClockRoom.control;

import com.ejupay.sdk.EjuPayConfiguration;
import com.ejupay.sdk.EjuPayManager;
import com.ejupay.sdk.service.EjuPayConfigBuilder;
import com.ejupay.sdk.service.SdkInitLister;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.WorkingPayResult;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * 易支付
 * Created by wh on 2016/7/29.
 */
public class EjuPaySDKUtil {

    public static boolean isInitSuccess = false;
    public static boolean isRunning = false;
    public static IEjuPayInit mEjuPayInit;

    public interface IEjuPayInit {
        void onSuccess();
    }

    public static void initEjuPaySDK(IEjuPayInit ejuPayInit) {
        mEjuPayInit = ejuPayInit;
        ejuPaySDKHandler();
    }

    public static void ejuPaySDKHandler() {
        WorkingPayResult workingPayResult = getEjuPaySdkParam();
        isRunning = true;
        if (workingPayResult == null || isInitSuccess) {
            if (!isInitSuccess) {
                getWorkingKey();
            } else {
                if (workingPayResult != null) {
                    configEjupayParam(workingPayResult);
                }
                isRunning = false;
            }
            return;
        }
        configEjupayParam(workingPayResult);
    }

    /**
     * 存储易支付相关参数
     */
    public static void saveEjuPaySdkParam(WorkingPayResult workingPayResult) {
        SharedPreferencesUtils.saveString(UIUtils.getContext(),
                "WorkingPayResult", GsonTools.createGsonString(workingPayResult));
    }

    /**
     * 获取存储的易支付相关参数
     *
     * @return workingPayResult     易居参数
     */
    public static WorkingPayResult getEjuPaySdkParam() {
        String json = SharedPreferencesUtils.getString(UIUtils.getContext(), "WorkingPayResult", null);
        WorkingPayResult info = null;
        if (json != null) {
            info = GsonTools.changeGsonToBean(json, WorkingPayResult.class);
        }
        return info;
    }

    /**
     * 清空支付参数
     */
    public static void clearEjuPaySdkParam() {
        isInitSuccess = false;
        SharedPreferencesUtils.saveString(UIUtils.getContext(),
                "WorkingPayResult", null);
    }

    /**
     * 获取工作密钥
     */
    private static void getWorkingKey() {
        if ("-1".equals(StringUtils.getUid())) {
            return;
        }
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getWorkingKey");
        params.addBodyParameter("uid", StringUtils.getUid());
//        params.addBodyParameter("expire_second", "");//密钥过期秒数，可不传，默认7天

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_PAY, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        isRunning = false;
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForWorkingKey(arg0.result);
                        isRunning = false;
                    }
                }
        );
    }

    private static void processDataForWorkingKey(String result) {
        WorkingPayResult workingPayResult = GsonTools.changeGsonToBean(result, WorkingPayResult.class);
        if (workingPayResult == null) {
            return;
        }
        if ("1".equals(workingPayResult.getCode())) {
            //存储易支付初始化所需的工作密钥等参数
            EjuPaySDKUtil.saveEjuPaySdkParam(workingPayResult);
            if (!isInitSuccess) {
                configEjupayParam(workingPayResult);
            }
        }
    }

    /**
     * 配置支付启动参数，并且初始化
     */
    private static void configEjupayParam(WorkingPayResult workingPayResult) {
        EjuPayConfigBuilder builder = new EjuPayConfiguration(UIUtils.getContext());//参数配置类初始化
        builder.setMemberId(workingPayResult.getPay_uid());
        builder.setSignatureKey(workingPayResult.getSignatureKey());
        builder.setCipherKey(workingPayResult.getCipherKey());
        builder.setPartnerToken(workingPayResult.getPartner_id() + "");
        builder.setBaseUrl(UrlUtils.EJU_PAY_URL);//sdk请求地址
        builder.setStyleColor(UIUtils.getColor(R.color.app_theme_color));              //风格颜色
        EjuPayManager.getInstance().init(builder, new SdkInitLister() {
            @Override
            public void initStart() {
            }

            @Override
            public void initSucess() {
                isInitSuccess = true;
                if (mEjuPayInit != null) {
                    mEjuPayInit.onSuccess();
                }
//                UIUtils.showToastSafe("易支付初始化成功");
            }

            @Override
            public void initFail() {
                isInitSuccess = false;
//                if (!isFromBaseApplication && isRunning) {
//                    UIUtils.showToastSafe("因网络或信号原因，页面加载失败，请稍后再试");
//                }
            }
        });
        isRunning = false;
    }

}
