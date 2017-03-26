package com.yiju.ClassClockRoom.util.net.api;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.bean.AccompanyRead;
import com.yiju.ClassClockRoom.bean.result.AccompanyReadResult;
import com.yiju.ClassClockRoom.bean.result.HotSearchResult;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.constant.ParamConstant;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.HttpApiParam;
import com.yiju.ClassClockRoom.util.net.HttpManage;
import com.yiju.ClassClockRoom.util.net.ResultCallImpl;

/**
 * 课室相关API
 * Created by geliping on 2016/8/11.
 */
public class HttpClassRoomApi extends BaseSingleton {

    public static HttpClassRoomApi getInstance() {
        return getSingleton(HttpClassRoomApi.class);
    }

    /**
     * 请求视频信息
     *
     * @param pwd        密码
     * @param uid        用户id
     * @param isProgress 进度条是否显示
     */
    public void getVideoState(final String pwd, String uid, final boolean isProgress) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .getVideoState(HttpApiParam.getVideoState(uid, pwd)), isProgress,
                new ResultCallImpl<AccompanyReadResult>() {

                    @Override
                    public void onNext(AccompanyReadResult bean) {
                        if (ParamConstant.RESULT_CODE_VIDEO_PWD_ERROR == bean.getIntCode()) {//密码错误
                            DataManager.getInstance().updateAccompanyRead(bean.getIntCode(), null);
                        } else {
                            if (bean.getData() != null && bean.getData().size() > 0) {
                                DataManager.getInstance()
                                        .updateAccompanyRead(bean.getIntCode(),
                                                bean.getData().get(0));
                            } else {
                                UIUtils.showToastSafe(R.string.fail_data_request);
                            }
                        }
                    }

                    @Override
                    public void onError(BaseEntity bean) {
                        super.onError(bean);
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        SharedPreferencesUtils.saveString(BaseApplication.getmForegroundActivity(),
                                UIUtils.getString(R.string.shared_accompany_read_pwd), pwd);
                    }
                });
    }

    /**
     * 设置提醒状态参数
     *
     * @param accompanyRead 视频信息
     * @param uid           用户id
     */
    public void askRemind(String uid, final AccompanyRead accompanyRead) {

        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .baseClassRoomApi(HttpApiParam.askRemind(accompanyRead.getHasRemember(),
                                uid, accompanyRead.getVideo_id())),
                new ResultCallImpl<BaseEntity>() {

                    @Override
                    public void onNext(BaseEntity bean) {
                        if (bean.getIntCode() == 1) {
                            if (accompanyRead.getHasRemember() == 1) {
                                accompanyRead.setHasRemember(0);
                                UIUtils.showToastSafe(R.string.accompany_cancel_remind_success);
                            } else {
                                accompanyRead.setHasRemember(1);
                                UIUtils.showToastSafe(R.string.accompany_add_remind_success);
                            }
                        } else {
                            if (accompanyRead.getHasRemember() == 1) {
                                UIUtils.showToastSafe(R.string.accompany_cancel_remind_fail);
                            } else {
                                UIUtils.showToastSafe(R.string.accompany_add_remind_fail);
                            }
                        }
                        DataManager.getInstance().updateAccompanyRead(accompanyRead);
                    }

                });
    }


    /**
     * 获得搜索热门列表
     */
    public static void getSearchHot() {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                .getSearchHot(HttpApiParam.getSearchHot()), new ResultCallImpl<HotSearchResult>() {
            @Override
            public void onNext(HotSearchResult bean) {
                if (bean != null && bean.getData() != null) {
                    DataManager.getInstance().updateHotSearchList(bean.getData());
                }
            }
        });
    }

    /**
     * 老师资料修改接口
     * @param title_flag 页面标识(标题):个人老师资料，老师资料，或者其他,用来区分接口里传参,如果是机构帮老师修改就要传另外2个参数
     * @param teacher_id 老师id，被修改人id
     * @param memberDetailResult 老师资料bean，里面带有老师所有信息
     * @param isProgress 是否要加进度条
     * @param flag 区分是哪个页面，老师资料页传true，其他页面传 false，主要在子页面去请求的时候不触发老师页面的onRefreshEvent方法
     */
    public void askModifyMemberInfo(String title_flag, String teacher_id, MemberDetailResult memberDetailResult, final boolean isProgress, final boolean flag) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .modifyMemberInfo(HttpApiParam.getModifyMemberInfo(title_flag, teacher_id, memberDetailResult)),
                isProgress, new ResultCallImpl<MemberDetailResult>() {
                    @Override
                    public void onNext(MemberDetailResult bean) {
                        DataManager.getInstance().modifyMemberInfo(bean,flag);
                    }

                    @Override
                    public void onError(BaseEntity bean) {
                        super.onError(bean);
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                });
    }


}
