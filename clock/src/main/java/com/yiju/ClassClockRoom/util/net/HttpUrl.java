package com.yiju.ClassClockRoom.util.net;

import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpRemovalApi;

/**
 * Created by geliping on 2016/8/1.
 */
public class HttpUrl {
    public static final String BASE_URL = UrlUtils.BASE_URL + "/";         //测试
    //    public static String BASE_URL = "http://api.51shizhong.com";              //正式地址

    // 迁移地址
    public static final String REMOVAL_URL = "http://101.231.84.177/";

    // 迁移接口
    public static final String REMOVAL_API = "weburl.html";
    public static final String REMOVAL_API_TEST = "/api/test_weburl.html";

    // 图片服务基类地址
    public static String BASE_PIC_WIRTE = "http://i.upload.file.dc.cric.com/";

    static {
        if (StringUtils.isNotNullString(HttpRemovalApi.getInstance().changeBasePicUrl)) {
            BASE_PIC_WIRTE = HttpRemovalApi.getInstance().changeBasePicUrl;
        }
    }

    // 上传图片接口
    public static final String UPLOAD_PHOTO = "FileWriterInner.php";

    // 用户相关接口
    public static final String SERVER_USER_API = UrlUtils.API_VERSION + "/user_api.php";

    // 通用接口
    public static final String SERVER_COMMON_API = UrlUtils.API_VERSION + "/common_api.php";

    // 课程接口
    public static final String SERVER_COURSE_API = UrlUtils.API_VERSION + "/course_api.php";

    // 老师接口
    public static final String SERVER_TEACHER_API = UrlUtils.API_VERSION + "/teacher_api.php";

    //课室相关
    public static final String SERVER_CALSS_ROOM_API = UrlUtils.API_VERSION + "/classroom_api.php";
}
