package com.yiju.ClassClockRoom.util.net;


import com.yiju.ClassClockRoom.bean.PictureWrite;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.result.AccompanyReadResult;
import com.yiju.ClassClockRoom.bean.result.CourseApplyResult;
import com.yiju.ClassClockRoom.bean.result.CourseDetail;
import com.yiju.ClassClockRoom.bean.result.HotSearchResult;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.bean.result.MessageBoxNoReadResult;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.bean.result.TeacherDetailBean;
import com.yiju.ClassClockRoom.bean.result.UserInfo;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * 作者： 葛立平
 * 2016/2/18 14:54
 */
public interface IApiService {

    /**
     * 个人相关基类接口
     */
    @Multipart
    @POST(UrlUtils.SERVER_USER_ABOUT_API)
    Call<BaseEntity> baseUserApi(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 通用相关基类接口
     */
    @Multipart
    @POST(UrlUtils.SERVER_COMMON_API)
    Call<BaseEntity> baseCommonApi(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 课室相关基类接口
     */
    @Multipart
    @POST(UrlUtils.SERVER_CALSS_ROOM_API)
    Call<BaseEntity> baseClassRoomApi(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 图片上传
     */
    @Multipart
    @POST(UrlUtils.UPLOAD_PHOTO)
    Call<PictureWrite> uploadPhoto(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 存储图片的URL
     */
    @Multipart
    @POST(UrlUtils.SERVER_USER_ABOUT_API)
    Call<MineOrder> saveUploadPhotoUrl(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 获得课程详情
     */
    @Multipart
    @POST(UrlUtils.SERVER_COURSE_API)
    Call<CourseDetail> getCourseDetail(@PartMap Map<String, RequestBody> bodyMap);


    /**
     * 报名课程
     */
    @Multipart
    @POST(UrlUtils.SERVER_COURSE_API)
    Call<CourseApplyResult> applyCourse(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 获得老师详情
     */
    @Multipart
    @POST(UrlUtils.SERVER_TEACHER_API)
    Call<TeacherDetailBean> getTeacherDetail(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 请求视频信息
     */
    @Multipart
    @POST(UrlUtils.SERVER_CALSS_ROOM_API)
    Call<AccompanyReadResult> getVideoState(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 搜索热门列表
     */
    @Multipart
    @POST(UrlUtils.SERVER_CALSS_ROOM_API)
    Call<HotSearchResult> getSearchHot(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 阅读消息
     */
    @Multipart
    @POST(UrlUtils.SERVER_COMMON_API)
    Call<MessageBoxNoReadResult> readMessage(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 登录
     */
    @Multipart
    @POST(UrlUtils.SERVER_USER_ABOUT_API)
    Call<UserInfo> login(@PartMap Map<String, RequestBody> bodyMap);

    /**
     * 修改老师资料
     */
    @Multipart
    @POST(UrlUtils.SERVER_USER_ABOUT_API)
    Call<MemberDetailResult> modifyMemberInfo(@PartMap Map<String, RequestBody> bodyMap);
}
