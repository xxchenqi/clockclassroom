package com.yiju.ClassClockRoom.util.net.api;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.result.CourseDetail;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.HttpApiParam;
import com.yiju.ClassClockRoom.util.net.HttpManage;
import com.yiju.ClassClockRoom.util.net.ResultCallImpl;

/**
 * 课程相关API
 * Created by geliping on 2016/8/11.
 */
public class HttpCourseApi extends BaseSingleton{

    public static HttpCourseApi getInstance() {
        return getSingleton(HttpCourseApi.class);
    }

    /**
     * 获得课程详情
     */
    public static void getCourseDetail(String uid, String course_id) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .getCourseDetail(HttpApiParam.getCourseDetail(uid, course_id)),
                new ResultCallImpl<CourseDetail>() {
                    @Override
                    public void onNext(CourseDetail bean) {
                        DataManager.getInstance().baseEvent(bean, DataManager.GET_COURSE_DETAIL);
                    }

                    @Override
                    public void onError(BaseEntity bean) {
                        super.onError(bean);
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }
                });
    }
}
