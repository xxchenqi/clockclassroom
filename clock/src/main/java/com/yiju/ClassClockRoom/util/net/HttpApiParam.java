package com.yiju.ClassClockRoom.util.net;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.MienBean;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.MD5;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.base.BaseApiParam;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者： 葛立平
 * 配置请求参数
 * 2016/2/15 11:03
 */
public class HttpApiParam extends BaseApiParam {

    private String username;
    private String password;
    private String third_source;

    /**
     * 上传图片到图片服务器
     *
     * @param file 文件
     * @return 上传参数
     */
    public static Map<String, RequestBody> uploadPhoto(File file) {
        PostParams params = new PostParams();
        String permit_code = "A003";
        try {
            String key = MD5.md5(permit_code + DateUtil.getCurrentDate("yyyyMMdd"));
            params.put("key", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("permit_code", permit_code);
        params.put("file_category", "SZJS");
        params.put("pfile", file);
        return params.getMap();
    }

    /**
     * 保存上传图片地址
     *
     * @param uid 用户id
     * @param url 图片地址
     * @return 上传参数
     */
    public static Map<String, RequestBody> saveUploadPhotoUrl(String uid, String username,
                                                              String password, String third_source, String url) {
        PostParams params = new PostParams();
        params.put("action", "modifyUPic");
        params.put("uid", uid);
        params.put("purl", url);
        params.put("url", UrlUtils.SERVER_USER_API);
        params.put("sessionId", StringUtils.getSessionId());


        return params.getMap();
    }

    /**
     * 更改是否展示我的老师信息
     *
     * @param uid    用户id
     * @param on_off 开关 1-显示，0-不显示
     */
    public static Map<String, RequestBody> switchTeacherInfo(String uid, String username,
                                                             String password, String third_source, String on_off) {
        PostParams params = new PostParams();
        params.put("action", "modifyInfo");
        params.put("type", "show_teacher");
        params.put("uid", uid);
        params.put("value", on_off);
        params.put("url", UrlUtils.SERVER_USER_API);
        params.put("sessionId", StringUtils.getSessionId());

        return params.getMap();
    }

    /**
     * 注销
     *
     * @param uid 用户id
     */
    public static Map<String, RequestBody> logout(String uid, String username,
                                                  String password, String third_source) {
        PostParams params = new PostParams();
        params.put("action", "logout");
        params.put("uid", uid);
        return params.getMap();
    }

    /**
     * 关注操作/取消关注
     *
     * @param action    路由
     * @param uid       用户id
     * @param detail_id 关注对象ID
     * @param type      关注类型
     */
    public static Map<String, RequestBody> attentionAction(String action, String uid,
                                                           String detail_id, String type) {
        PostParams params = new PostParams();
        params.put("action", action);
        params.put("uid", uid);
        params.put("detail_id", detail_id);
        params.put("type", type);
        params.put("url", UrlUtils.SERVER_API_COMMON);
        params.put("sessionId", StringUtils.getSessionId());
        return params.getMap();
    }

    /**
     * 获得课程详情
     *
     * @param uid       用户id
     * @param course_id 课程id
     */
    public static Map<String, RequestBody> getCourseDetail(String uid, String course_id) {
        PostParams params = new PostParams();
        params.put("action", "get_course_detail");
        if ("-1".equals(uid)) {
            uid = "";
        }
        params.put("own_uid", uid);
        params.put("id", course_id);
        return params.getMap();
    }

    /**
     * 获得老师详情
     *
     * @param uid        用户id
     * @param teacher_id 老师id
     */
    public static Map<String, RequestBody> getTeacherDetail(String uid, String teacher_id) {
        PostParams params = new PostParams();
        params.put("action", "get_teacher_detail");
        if (uid != null) {
            params.put("own_uid", uid);
        }
        params.put("uid", teacher_id);
        return params.getMap();
    }

    /**
     * 获得视频状态参数
     *
     * @param uid   用户id
     * @param vcode 视频密码
     */
    public static Map<String, RequestBody> getVideoState(String uid, String vcode) {
        PostParams params = new PostParams();
        params.put("action", "online_watch");
        if (StringUtils.isNotNullString(uid)) {
            params.put("uid", uid);
        }
        params.put("vcode", vcode);
        return params.getMap();
    }

    /**
     * 设置提醒状态参数
     *
     * @param hasRemember 提醒状态    0-未提醒   1-已提醒
     * @param uid         用户id
     * @param vid         视频id
     */
    public static Map<String, RequestBody> askRemind(int hasRemember, String uid, String vid) {
        PostParams params = new PostParams();
        if (hasRemember == 0) {
            params.put("action", "remember_video");
        } else {
            params.put("action", "unremember_video");
        }
        params.put("uid", uid);
        params.put("vid", vid);
        return params.getMap();
    }

    /**
     * 热搜关键字
     */
    public static Map<String, RequestBody> getSearchHot() {
        PostParams params = new PostParams();
        params.put("action", "search_keyword");
        return params.getMap();
    }

    /**
     * 已读消息标记
     */
    public static Map<String, RequestBody> readMessage(int mid) {
        String uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                "id", "");
        PostParams params = new PostParams();
        params.put("action", "message_read");
        params.put("uid", uid);
        params.put("mid", mid + "");
        params.put("url", UrlUtils.SERVER_API_COMMON);
        params.put("sessionId", StringUtils.getSessionId());
        return params.getMap();
    }

    /**
     * 登录
     *
     * @param userName     用户名
     * @param password     密码
     * @param device_token 设备号
     * @param cid          推送id
     */
    public static Map<String, RequestBody> login(String userName,
                                                 String password, String device_token, String cid) {
        PostParams params = new PostParams();
        params.put("action", "login");
        params.put("username", userName);
        params.put("password", password);
        params.put("device_token", device_token);
        if (StringUtils.isNotNullString(cid)) {
            params.put("cid", cid);
        }
        return params.getMap();
    }

    /**
     * 修改个人老师资料
     */
    public static Map<String, RequestBody> getModifyMemberInfo(String title_flag,
                                                               String teacher_id,
                                                               MemberDetailResult bean) {
        //分解机构风采
        StringBuilder sb = new StringBuilder();
        if (bean.getData().getMien().size() > 0) {
            List<MienBean> mien = bean.getData().getMien();
            for (int i = 0; i < mien.size(); i++) {
                if (i == mien.size() - 1) {
                    sb.append(mien.get(i).getPic());
                } else {
                    sb.append(mien.get(i).getPic()).append(",");
                }
            }
        }
        PostParams params = new PostParams();
        params.put("action", "edit_teacher_info");
        params.put("username", StringUtils.getUsername());
        params.put("password", StringUtils.getPassword());
        params.put("third_source", StringUtils.getThirdSource());
        //机构管理员帮别人修改需要传以下2个参数
        if (UIUtils.getString(R.string.member_detail).equals(title_flag)
                || !UIUtils.getString(R.string.teacher_detail).equals(title_flag)) {
            if (!StringUtils.getUid().equals(teacher_id)) {
                //如果用户本身是管理员并且点击的成员不是用户本身
                //是否机构帮修改 0=不是 1=是
                params.put("org_auth", "1");
                //机构帮修改人的ID
                params.put("org_uid", StringUtils.getUid());
            }
        }
        //修改人id(老师id)
        params.put("uid", teacher_id);
        //性别
        params.put("sex", bean.getData().getSex());
        //简介
        params.put("info", bean.getData().getInfo());
        //标签
        params.put("tags", bean.getData().getTags());
        //名字
        params.put("real_name", bean.getData().getReal_name());
        //风采图片
        params.put("mien_pic", sb.toString());
        //头像
        params.put("avatar", bean.getAvatar());

        return params.getMap();
    }


}
