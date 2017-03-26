package com.yiju.ClassClockRoom.control.share;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yiju.ClassClockRoom.bean.ShareEntityBean;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

///分享数据请求
class ShareGetData {
    private static final String Visible_PWD = "1";
    private static final String INVisible_PWD = "0";


    private int current_Type;
    private int current_Way;

    private String sid;
    private String address;
    private String tags;
    private String start_time;
    private String end_time;
    private String video_pas;
    private String vid;
    private String room_no;
    private String date;
    private String school_name;
    private String teacher_id;
    private String teacher_name;
    private String course_id;
    private String course_name;
    private boolean isVisiblePass;
    private String special_id;

    private IShareDateReturn shareDateReturn;

    public ShareGetData(int type, int Way, String school_name, String sid,
                        String address, String tags, boolean isVisiblePass) {
        initBaseData(type, Way, school_name, isVisiblePass);
        this.sid = sid;
        this.address = address;
        this.tags = tags;
        getShareContent();
    }

    public ShareGetData(int type, int Way, String school_name,
                        String start_time, String end_time, String video_pas, String vid, boolean isVisiblePass) {
        initBaseData(type, Way, school_name, isVisiblePass);
        this.start_time = start_time;
        this.end_time = end_time;
        this.video_pas = video_pas;
        this.vid = vid;
        getShareContent();
    }

    public ShareGetData(int type, int Way, String school_name,
                        String start_time, String end_time, String video_pas, String vid,
                        String room_no, String date, boolean isVisiblePass) {
        initBaseData(type, Way, school_name, isVisiblePass);
        this.start_time = start_time;
        this.end_time = end_time;
        this.video_pas = video_pas;
        this.vid = vid;
        this.room_no = room_no;
        this.date = date;
        getShareContent();
    }

    //老师详情/资讯/活动
    public ShareGetData(int current_Type, int current_Way, String teacher_id, String teacher_name) {
        this.current_Type = current_Type;
        this.current_Way = current_Way;
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
        getShareContent();
    }

    public ShareGetData(int current_Type, int current_Way, String school_name, String course_id,
                        String course_name, String teacher_name) {
        this.current_Type = current_Type;
        this.current_Way = current_Way;
        this.school_name = school_name;
        this.course_id = course_id;
        this.course_name = course_name;
        this.teacher_name = teacher_name;
        getShareContent();
    }

    public ShareGetData(int current_Type, int current_Way, String special_id) {
        this.current_Type = current_Type;
        this.current_Way = current_Way;
        this.special_id = special_id;
        getShareContent();
    }

    private void initBaseData(int type, int Way, String school_name, boolean isVisiblePass) {
        this.current_Type = type;
        this.current_Way = Way;
        this.school_name = school_name;
        this.isVisiblePass = isVisiblePass;
    }

    private void getShareContent() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getShare");
        params.addBodyParameter("type", current_Type + "");
        params.addBodyParameter("way", current_Way + "");
        if (current_Type == ShareDialog.Type_Share_ClassRoom_Info) {
            params.addBodyParameter("newpic", "1");//新版读图新加参数
            params.addBodyParameter("school_name", school_name);
            params.addBodyParameter("sid", sid);
            params.addBodyParameter("address", address);
            params.addBodyParameter("tags", tags);
        }
        if (current_Type == ShareDialog.Type_Share_Accompany_Video
                || current_Type == ShareDialog.Type_Share_Accompany_Key) {
            params.addBodyParameter("newpic", "1");//新版读图新加参数
            params.addBodyParameter("school_name", school_name);
            params.addBodyParameter("start_time", start_time);
            params.addBodyParameter("end_time", end_time);
            params.addBodyParameter("video_pas", video_pas);
            params.addBodyParameter("vid", vid);
            if (current_Type == ShareDialog.Type_Share_Accompany_Key) {
                params.addBodyParameter("room_no", room_no);
                params.addBodyParameter("date", date);
            }
            if (isVisiblePass) {
                params.addBodyParameter("sharevideopass", Visible_PWD);
            } else {
                params.addBodyParameter("sharevideopass", INVisible_PWD);
            }
        }

        if (current_Type == ShareDialog.Type_Share_Teacher_Detail) {
            params.addBodyParameter("teacher_id", teacher_id);
            params.addBodyParameter("teacher_name", teacher_name);
        }
        if (current_Type == ShareDialog.Type_Share_Course_Detail) {
            params.addBodyParameter("school_name", school_name);
            params.addBodyParameter("course_id", course_id);
            params.addBodyParameter("course_name", course_name);
            params.addBodyParameter("teacher_name", teacher_name);
        }

        if (current_Type == ShareDialog.Type_Share_Special) {
            params.addBodyParameter("special_id", special_id);
        }

        if (current_Type == ShareDialog.Type_Share_Supplier_Detail) {
            //供应商详情
            params.addBodyParameter("sp_id", teacher_id);
            params.addBodyParameter("sp_name", teacher_name);
        }
        if (current_Type == ShareDialog.Type_Share_Theme_News) {
            //资讯分享
            params.addBodyParameter("news_id", teacher_id);
            params.addBodyParameter("news_title", teacher_name);
        }
        if (current_Type == ShareDialog.Type_Share_Theme_Activity) {
            //活动分享
            params.addBodyParameter("activity_id", teacher_id);
            params.addBodyParameter("activity_title", teacher_name);
        }
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ShareEntityBean bean = GsonTools.fromJson(arg0.result,
                                ShareEntityBean.class);
                        if (bean != null && bean.getData() != null
                                && shareDateReturn != null) {
                            shareDateReturn.getShareBean(bean.getData());
                        }
                    }

                });
    }

    public void setShareDateReturn(IShareDateReturn shareDateReturn) {
        this.shareDateReturn = shareDateReturn;
    }
}
