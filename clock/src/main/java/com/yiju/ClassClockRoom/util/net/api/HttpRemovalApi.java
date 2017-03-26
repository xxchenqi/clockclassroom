package com.yiju.ClassClockRoom.util.net.api;

import android.content.Intent;

import com.yiju.ClassClockRoom.act.GuideActivity;
import com.yiju.ClassClockRoom.act.MainActivity;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.util.net.HttpManage;
import com.yiju.ClassClockRoom.util.net.HttpUrl;
import com.yiju.ClassClockRoom.util.net.ResultCallImpl;

/**
 * 服务端迁移API
 * Created by geliping on 2016/8/11.
 */
public class HttpRemovalApi extends BaseSingleton {
    public String changeBaseUrl = "http://api.51shizhong.com";
    public String changeJavaUrl = "http://api2.51shizhong.com";
    public String changeBasePicUrl = "http://i.upload.file.dc.cric.com/";
    public String changeEjuPayUrl = "http://ejupay.17shihui.com/gateway-outrpc/acquirer/interact";

    public static HttpRemovalApi getInstance() {
        return getSingleton(HttpRemovalApi.class);
    }

    /**
     * 获得服务器迁移信息
     *
     * @param isFirst 是否是第一次
     */
    public void getHttpRequestForServer(final boolean isFirst) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService(HttpUrl.REMOVAL_URL)
                .getRemovalForServer(), false, new ResultCallImpl<CommonResultBean>() {
            @Override
            public void onNext(CommonResultBean bean) {
                if (bean == null) {
                    return;
                }
                if ("1".equals(bean.getCode())) {
                    CommonResultBean.NewDataEntity entity = bean.getNewData();
                    if (entity == null) {
                        return;
                    }
                    changeBaseUrl = entity.getBaseUrl();
                    changeJavaUrl = entity.getJavaUrl();
                    changeBasePicUrl = entity.getBasePicUrl();
                    changeEjuPayUrl = entity.getEjuPayUrl();
                }
                if (isFirst) {
                    initVoid();
                }
            }

            @Override
            public void onError(BaseEntity bean) {
                super.onError(bean);
                initVoid();
            }
        });
    }

    private void initVoid() {
        UIUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
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
        }, 10);
    }
}
