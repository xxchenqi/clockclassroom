package com.yiju.ClassClockRoom.util.net.api;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.GuideActivity;
import com.yiju.ClassClockRoom.act.MainActivity;
import com.yiju.ClassClockRoom.act.NewCourseDetailActivity;
import com.yiju.ClassClockRoom.act.SplashActivity;
import com.yiju.ClassClockRoom.act.SupplierDetailActivity;
import com.yiju.ClassClockRoom.act.ThemeWebAboutActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.bean.result.SplashResult;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * 服务端迁移API
 * Created by geliping on 2016/8/11.
 */
public class HttpRemovalApi extends BaseSingleton {
    public String changeBaseUrl = "https://api.51shizhong.com";
    public String changeJavaUrl = "https://api2.51shizhong.com";
    public String changeBasePicUrl = "http://i.upload.file.dc.cric.com/";
    public String changeEjuPayUrl = "http://ejupay.17shihui.com/gateway-outrpc/acquirer/interact";
    public String changeWebBaseUrl = "http://api.51shizhong.com";
    private ImageView iv_splash_bg;
    private boolean isFirst;

    public static HttpRemovalApi getInstance() {
        return getSingleton(HttpRemovalApi.class);
    }

    /**
     * 获得服务器迁移信息
     *
     * @param isFirst 是否是第一次
     */
    public void getHttpRequestForServer(final boolean isFirst, ImageView iv_splash_bg) {
        this.isFirst = isFirst;
        this.iv_splash_bg = iv_splash_bg;
        getHttpRequestRemovalForServer();
    }


    public void getHttpRequestRemovalForServer() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        httpUtils.configTimeout(2000);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.REMOVAL_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (iv_splash_bg != null) {
                            getHttpRequestSplashBg();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForServer(arg0.result);
                    }
                }
        );
    }

    private void processDataForServer(String result) {
        CommonResultBean commonResultBean = GsonTools.changeGsonToBean(result, CommonResultBean.class);
        if (commonResultBean == null) {
            return;
        }
        if ("1".equals(commonResultBean.getCode())) {
            CommonResultBean.NewDataEntity entity = commonResultBean.getNewData();
            if (entity == null) {
                return;
            }
            changeBaseUrl = entity.getBaseUrl();
            changeJavaUrl = entity.getJavaUrl();
            changeBasePicUrl = entity.getBasePicUrl();
            changeEjuPayUrl = entity.getEjuPayUrl();
            changeWebBaseUrl = entity.getH5BaseUrl();
        }
        if (iv_splash_bg != null) {
            getHttpRequestSplashBg();
        } else {
            initVoid();
        }

    }

    //获取启动页背景图请求
    private void getHttpRequestSplashBg() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("ad_type", "13");//13目前暂时写死
        httpUtils.configTimeout(2000);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.JAVA_SPLASH, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        handleSplashDefault();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);
                    }
                }
        );
    }


    private void processData(String result) {
        final SplashResult splashResult = GsonTools.changeGsonToBean(result, SplashResult.class);
        if (splashResult == null) {
            handleSplashDefault();
            return;
        }
        if (splashResult.getCode() == 0) {
            if (splashResult.getObj() != null) {
                Glide.with(UIUtils.getContext()).load(splashResult.getObj().getPic_url())
                        .error(R.drawable.splash)
                        .into(iv_splash_bg);
                iv_splash_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        SplashActivity.isClick = true;
                        switch (splashResult.getObj().getType()) {
                            case 1:
                                //app内活动
                                intent = new Intent(UIUtils.getContext(), ThemeWebAboutActivity.class);
                                intent.putExtra(UIUtils.getString(R.string.redirect_open_url), splashResult.getObj().getUrl());
                                intent.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.WEB_value_theme_activity_Page);
                                UIUtils.startActivity(intent);
                                break;
                            case 2:
                                //外部网页活动
                                intent = new Intent(UIUtils.getContext(), Common_Show_WebPage_Activity.class);
                                intent.putExtra(UIUtils.getString(R.string.redirect_open_url), splashResult.getObj().getUrl());
                                intent.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.WEB_Int_Action_Page);
                                UIUtils.startActivity(intent);
                                break;
                            case 4:
                                //老师
                                intent = new Intent(UIUtils.getContext(), SupplierDetailActivity.class);
                                intent.putExtra(ExtraControl.EXTRA_ID, splashResult.getObj().getTeacher_id() + "");
                                UIUtils.startActivity(intent);
                                break;
                            case 5:
                                //课程
                                intent = new Intent(UIUtils.getContext(), NewCourseDetailActivity.class);
                                intent.putExtra(ExtraControl.EXTRA_COURSE_ID, splashResult.getObj().getTeacher_id() + "");
                                UIUtils.startActivity(intent);
                                break;
                        }
                        BaseApplication.mForegroundActivity.finish();
                    }
                });

                iv_splash_bg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!SplashActivity.isClick) {
                            initVoid();
                        }
                    }
                }, 3000);

            } else {
                handleSplashDefault();
            }
        } else {
            handleSplashDefault();
        }

    }

    private void handleSplashDefault() {
        iv_splash_bg.setBackgroundResource(R.drawable.splash);
        iv_splash_bg.postDelayed(new Runnable() {
            @Override
            public void run() {
                initVoid();
            }
        }, 3000);
    }

    private void initVoid() {
        if (!isFirst) {
            return;
        }
        int status = SharedPreferencesUtils.getInt(UIUtils.getContext(),
                "firstLogin", 0);
        Intent intent;
        if (status == 1) {
            intent = new Intent(UIUtils.getContext(), MainActivity.class);
        } else {
            intent = new Intent(UIUtils.getContext(),
                    GuideActivity.class);
        }
        BaseApplication.mForegroundActivity.startActivity(intent);
        BaseApplication.mForegroundActivity.finish();
    }
}
