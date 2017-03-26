package com.yiju.ClassClockRoom.util.net.api;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.result.UserInfo;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.control.FailCodeControl;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.HttpApiParam;
import com.yiju.ClassClockRoom.util.net.HttpManage;
import com.yiju.ClassClockRoom.util.net.ResultCallImpl;

/**
 * 用户相关API
 * Created by geliping on 2016/8/11.
 */
public class HttpUserApi extends BaseSingleton {

    public static HttpUserApi getInstance() {
        return getSingleton(HttpUserApi.class);
    }

    /**
     * 登录
     *
     * @param userName     用户名
     * @param password     密码
     * @param device_token 设备号
     * @param isSend       是否消息分发
     */
    public static void login(String userName, String password, String device_token,String cid,
                             final boolean isSend) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .login(HttpApiParam.login(userName, password, device_token,cid)),
                new ResultCallImpl<UserInfo>() {
                    @Override
                    public void onNext(UserInfo bean) {
                        if (isSend) {
                            DataManager.getInstance().baseEvent(bean, DataManager.LOGIN_USER_DATA);
                        }
                    }

                    @Override
                    public void onError(BaseEntity bean) {
                        super.onError(bean);
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }
                });
    }


    /**
     * 注销
     *
     * @param uid 用户id
     */
    public static void logout(String uid,String username,
                              String password,String third_source) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                .baseUserApi(HttpApiParam.logout(uid,username,password,third_source)),
                new ResultCallImpl<BaseEntity>() {
            @Override
            public void onNext(BaseEntity bean) {
                DataManager.getInstance().logout(bean);
            }

            @Override
            public void onError(BaseEntity bean) {
                super.onError(bean);
                UIUtils.showToastSafe(R.string.fail_network_request);
            }
        });
    }

    /**
     * 更改是否展示我的老师信息
     *
     * @param uid    用户id
     * @param on_off 开关 1-显示，0-不显示
     */
    public void switchTeacherInfo(String uid, String username,
                                  String password,String third_source,String on_off) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getJavaService()
                        .baseProxyApi(HttpApiParam.switchTeacherInfo(uid,username,password,third_source, on_off)),
                new ResultCallImpl<BaseEntity>() {
                    @Override
                    public void onNext(BaseEntity bean) {
                        FailCodeControl.checkCode(bean.getCode());
                        DataManager.getInstance().switchTeacherInfo(bean);
                    }

                    @Override
                    public void onError(BaseEntity bean) {
                        super.onError(bean);
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }
                });
    }

}
