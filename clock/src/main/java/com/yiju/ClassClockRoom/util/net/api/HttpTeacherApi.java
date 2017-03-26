package com.yiju.ClassClockRoom.util.net.api;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.bean.result.TeacherDetailBean;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.util.net.HttpApiParam;
import com.yiju.ClassClockRoom.util.net.HttpManage;
import com.yiju.ClassClockRoom.util.net.ResultCallImpl;

/**
 * 教师相关API
 * Created by geliping on 2016/8/11.
 */
public class HttpTeacherApi extends BaseSingleton {

    public static HttpTeacherApi getInstance() {
        return getSingleton(HttpTeacherApi.class);
    }

    /**
     * 获得老师详情
     *
     * @param uid        用户id
     * @param teacher_id 老师id
     */
    public static void getTeacherDetail(String uid, String teacher_id) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .getTeacherDetail(HttpApiParam.getTeacherDetail(uid, teacher_id)),
                new ResultCallImpl<TeacherDetailBean>() {
                    @Override
                    public void onNext(TeacherDetailBean bean) {
                        DataManager.getInstance().baseEvent(bean, DataManager.GET_TEACHER_DETAIL);
                    }

                    @Override
                    public void onError(BaseEntity bean) {
                        super.onError(bean);
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }
                });
    }
}
