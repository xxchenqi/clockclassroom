package com.yiju.ClassClockRoom.util.net;


import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.common.constant.ParamConstant;
import com.yiju.ClassClockRoom.util.net.jsonconverter.JsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者： 葛立平
 * 2016/2/18 14:56
 * 请求基础框架配置管理
 */
public class HttpManage {
    private static HttpManage instance;
    private Retrofit retrofit;

    public static HttpManage getInstance() {
        synchronized (HttpManage.class) {
            if (instance == null) {
                instance = new HttpManage();
            }
            return instance;
        }
    }

    private HttpManage() {
    }

    /**
     * 初始化Gson类型的数据传输
     */
    public IApiService getApiService() {
        return getApiService(UrlUtils.BASE_URL+"/");
    }

    /**
     * 初始化Gson类型的数据传输
     *
     * @param base_url 基类base地址
     */
    public IApiService getApiService(String base_url) {
        setHeader(base_url);
        return retrofit.create(IApiService.class);
    }

    /**
     * 初始化String类型的数据传输
     *
     * @param base_url 基类base地址
     */
    public IApiService getApiServiceForJson(String base_url) {
        setHeader(JsonConverterFactory.create(), base_url);
        return retrofit.create(IApiService.class);
    }

    //设置报文头
    private void setHeader(String base_url) {
        setHeader(GsonConverterFactory.create(), base_url);
    }

    /**
     * 设置配置信息
     *
     * @param factory  数据转换器
     * @param base_url 基类base地址
     */
    private void setHeader(Converter.Factory factory, String base_url) {
        OkHttpClient client = new OkHttpClient();
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(factory)
                .client(client)
                .build();
    }

    /**
     * 返回数据继承BaseEntity的请求
     */
    public <T extends BaseEntity> void getBaseEntity(Call<T> call, final Boolean isShowDialog,
                                                     final ResultCallImpl<T> resultCallImp) {
        resultCallImp.onStart();
        startDialog(isShowDialog);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T bean = response.body();
                if (bean == null) {
                    resultCallImp.onError(
                            new BaseEntity(ParamConstant.ERROR_FAIL + "", "数据获取失败!"));
                    resultCallImp.onCompleted();
                    endDialog(isShowDialog);
                    return;
                }
                if (ParamConstant.RESULT_CODE_SUCCESS == bean.getIntCode()                   //请求成功
                        || ParamConstant.RESULT_CODE_VIDEO_END == bean.getIntCode()           //视频结束
                        || ParamConstant.RESULT_CODE_VIDEO_STAY_START == bean.getIntCode()     //视频还未开始
                        || ParamConstant.RESULT_CODE_VIDEO_PWD_ERROR == bean.getIntCode()     //密码错误
                        || ParamConstant.RESULT_CODE_REMIND == bean.getIntCode()             //已加入提醒
                        || ParamConstant.RESULT_CODE_REMIND_FAIL == bean.getIntCode()) {     //加入提醒失败
                    resultCallImp.onNext(bean);
                } else {                                                                    //请求失败
                    UIUtils.showToastSafe(bean.getMsg());
                    resultCallImp.onError(new BaseEntity(bean.getCode(), bean.getMsg()));
                }
                resultCallImp.onCompleted();
                endDialog(isShowDialog);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                resultCallImp.onError(new BaseEntity(t.hashCode() + "", t.getMessage()));
                resultCallImp.onCompleted();
                endDialog(isShowDialog);
            }
        });
    }

    /**
     * 返回数据继承BaseEntity的请求
     */
    public <T extends BaseEntity> void getBaseEntity(Call<T> call,
                                                     ResultCallImpl<T> resultCallImp) {
        getBaseEntity(call, true, resultCallImp);
    }

    /**
     * 返回数据继承Object的请求
     */
    public <T extends Object> void getObject(Call<T> call, final Boolean isShowDialog,
                                             final ResultCallImpl<T> resultCallImp) {
        resultCallImp.onStart();
        startDialog(isShowDialog);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T bean = response.body();
                if (bean == null) {
                    resultCallImp.onError(new
                            BaseEntity(ParamConstant.ERROR_FAIL + "", "数据获取失败!"));
                    resultCallImp.onCompleted();
                    endDialog(isShowDialog);
                    return;
                }

                resultCallImp.onNext(bean);
                resultCallImp.onCompleted();
                endDialog(isShowDialog);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                resultCallImp.onError(new BaseEntity(t.hashCode() + "", t.getMessage()));
                resultCallImp.onCompleted();
                endDialog(isShowDialog);
            }
        });
    }

    /**
     * 返回数据继承Object的请求
     */
    public <T extends Object> void getObject(Call<T> call, ResultCallImpl<T> resultCallImp) {
        getObject(call, true, resultCallImp);
    }

    /**
     * 返回数据继承String的请求
     * 注：无法上传图片
     */
    public void getObjectForJson(Call<String> call, final Boolean isShowDialog, final ResultCallImpl<String> resultCallImp) {
        resultCallImp.onStart();
        startDialog(isShowDialog);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                resultCallImp.onNext(response.body());
                resultCallImp.onCompleted();
                endDialog(isShowDialog);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                resultCallImp.onError(new BaseEntity(t.hashCode() + "", t.getMessage()));
                resultCallImp.onCompleted();
                endDialog(isShowDialog);
            }
        });
    }

    /**
     * 返回数据继承String的请求
     * 注：无法上传图片
     */
    public void getObjectForJson(Call<String> call, ResultCallImpl<String> resultCallImp) {
        getObjectForJson(call, true, resultCallImp);
    }

    /**
     * 开始进度条
     *
     * @param isShow b
     */

    private void startDialog(Boolean isShow) {
        if (isShow) {
            ProgressDialog.getInstance().show();
        }
    }

    /**
     * 关闭进度条
     *
     * @param isShow b
     */
    private void endDialog(Boolean isShow) {
        if (isShow) {
            ProgressDialog.getInstance().dismiss();
        }
    }
}
