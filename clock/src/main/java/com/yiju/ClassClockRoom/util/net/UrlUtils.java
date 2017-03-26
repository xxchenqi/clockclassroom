package com.yiju.ClassClockRoom.util.net;

import com.yiju.ClassClockRoom.BaseApplication;

public class UrlUtils {
    // 测试地址为: http://172.28.70.47
    // 正式地址为: http://api.51shizhong.com
    // JAVA测试地址:http://172.28.70.47:8102
//    {
//        "code": "1",
//            "msg": "ok",
//            "data": "https://api.51shizhong.cn",
//            "newData": {
//        "baseUrl": "https://api.51shizhong.cn",
//                "javaUrl": "https://api2.51shizhong.cn",
//                "basePicUrl": "http://i.upload.file.dc.cric.com/",
//                "ejuPayUrl": "http://ejupay.17shihui.cn/gateway-outrpc/acquirer/interact",
//                "h5BaseUrl": "http://api.51shizhong.cn"
//    }
//    }
    public static String BASE_URL;
    public static String WEB_BASE_URL;
    public static String EJU_PAY_URL;
    public static String JAVA_URL;
    public static String BASE_PIC_WRITE;

    static {
        switch (BaseApplication.FORMAL_ENVIRONMENT) {
            case 1:
                BASE_URL = "http://172.28.70.47";
                WEB_BASE_URL = "http://172.28.70.47";
                JAVA_URL = "http://172.28.70.47:8102";
                EJU_PAY_URL = "https://inte.ejupay.cn/gateway-outrpc/acquirer/interact";
                BASE_PIC_WRITE = "http://i.upload.file.dc.cric.com/";
                break;
            case 2:
            case 3:
                BASE_URL = "https://api.51shizhong.com";
                WEB_BASE_URL = "http://api.51shizhong.com";
                JAVA_URL = "https://api2.51shizhong.com";
                EJU_PAY_URL = "https://ejupay.17shihui.com/gateway-outrpc/acquirer/interact";
                BASE_PIC_WRITE = "http://i.upload.file.dc.cric.com/";
                break;
        }
        changeUrl(BASE_URL, WEB_BASE_URL, EJU_PAY_URL, JAVA_URL, BASE_PIC_WRITE);
    }

    public static final String API_VERSION = "api_v9";
    public static final String H5_VERSION = "h5_v11";

    /**
     * 以下url固定不变
     */
    // 迁移接口
    public static String REMOVAL_API = "http://101.231.84.177/weburl.html";
    //==========================okhttp使用的参数========================================
    // 用户相关接口
    public static final String SERVER_USER_ABOUT_API = UrlUtils.API_VERSION + "/user_api.php";

    // 通用接口
    public static final String SERVER_COMMON_API = UrlUtils.API_VERSION + "/common_api.php";

    // 课程接口
    public static final String SERVER_COURSE_API = UrlUtils.API_VERSION + "/course_api.php";

    // 老师接口
    public static final String SERVER_TEACHER_API = UrlUtils.API_VERSION + "/teacher_api.php";

    //课室相关
    public static final String SERVER_CALSS_ROOM_API = UrlUtils.API_VERSION + "/classroom_api.php";


    /**
     * 以下url需要通过服务器迁移重新复制
     */
    // 上传图片的内部写接口
    public static String PIC_WIRTE;
    private static String TEST_BASE_URL;
    public static String H5_BASE_URL;
    // 首页ListView数据
    public static String SERVER_INDEX_NEW;
    // 预订页面
    public static String SERVER_RESERVATION;
    // 购物车提交
    public static String SERVER_COMMIT_CART;
    // 课程
    public static String SERVER_COURSE;
    // 老师
    public static String SERVER_TEACHER_MORE_JAVA;
    public static String SERVER_TEACHER_API_JAVA;
    public static String SERVER_TEACHER;
    //体验课
    public static String SERVER_EXPERIENCE_CLASS;
    //我的互动之 我赞过的
    public static String SERVER_INTERACTION_PRAISE;
    //我的互动之 我的评论
    public static String SERVER_INTERACTION_COMMENT;
    //我的互动之 我参与的活动
    public static String SERVER_INTERACTION_ACTIVITY;
    // 用户API
    public static String SERVER_USER_API;
    // 优惠券
    public static String SERVER_USER_COUPON;
    // 检测版本
    public static String SERVER_API_COMMON;
    //获取工作密钥
    public static String SERVER_API_PAY;
    //首页_地址点击进入导航webView
    public static String SERVER_WEB_TO_CLASSDETAIL;
    //首页,场馆详情
    public static String SERVER_WEB_CLASSDETAIL;
    //首页,周边环境详情： url： /h5_v1/around.html?sid={场馆ID} 可选参数：
    public static String SERVER_WEB_AROUND;
    /*
     * 首页,课室图片列表： url： /h5_v1/picdes.html?sid={场馆ID}
     * 可选参数：bigid：图片ID（如果带有这个参数表示直接显示查看大图页面）
     */
    public static String SERVER_WEB_PICDES;
    /*
     * 良师详情页面：
     * url：/h5_v1/teacherdetail.html?tid={老师ID}&teacherdetail=teacherdetail
     * 可选参数：uid (登录后传入未登录不传或者传入0) teacherdetail：为了和点赞区别
     */
    public static String SERVER_WEB_TEACHERDETAIL;
    /*
     * 我的（个人中心）, 订单, 退款金额明细：
     * url：/h5_v1/refundamountrecord.html?oid={订单ID}&uid={用户ID} 可选参数：
     * 注:已退款成功的订单点击‘订单’进入
     */
    public static String SERVER_WEB_REFUNDAMOUNT;
    /*
     * 我的（个人中心）, 我的优惠券： url：/h5_v1/ couponlist.html?uid={用户ID} 可选参数：
     */
    public static String SERVER_WEB_COUPONLIST;
    /*
     * 我的（个人中心）, 优惠券使用说明： url：/h5_v1/coupondescription.html 可选参数：
     */
    public static String SERVER_WEB_COUPONDESCRIPTION;
    /*
     * 我的（个人中心）, 更多-》用户协议： url：/h5_v1/useragent.html 可选参数：
     */
    public static String SERVER_WEB_USERAGENT;
    /*
     * 充返活动协议
     */
    public static String SERVER_WEB_CHARGE_BACK;
    /*
     * 搜索结果页 url： /h5_v1/search.html
     */
    public static String SERVER_WEB_SEARCH_RESULT;
    // ====================================陪读相关==============================================

    // h5_v1老师推荐
    public static String H5_ACCOMPANY_TEACHER;
    // 我的订单
    public static String SERVER_MINE_ORDER;
    //课室布置
    public static String SERVER_CLASSROOM_ARRANGE;
    // 订单日志h5_v1
    public static String ORDER_LOG;
    // WIFI认证h5_v1
    public static String WIFI;
    // 类型详情h5_v1
    public static String TYPE_DESC;
    //购物车课室可查看单价明细, 费用清单： url：/h5/shoppingcart_listfee.html?oid2={页面ID} 可选参数：
    public static String SERVER_WEB_STORE_PRICE;
    //购物车课室可查看单价明细, 费用清单： url：/h5/shoppingcart_listfee.html?oid2={页面ID} 可选参数：
    public static String SERVER_WEB_DEVICE_PRICE;
    //机构认证页
    public static String H5_ORGANIZATION_AUTHENTICATION;
    //如何开具发票页面
    public static String H5_DRAW_UP_INVOICES;
    //增值税专用发票信息
    public static String H5_ADDED_VALUE_TAX;
    //课室含税与不含税单价一览表
    public static String H5_CLASSROOM_VALUE_TAX;
    public static String H5_ADDED_VALUE_TAX_IMG;
    public static String JAVA_MORE_STORE;
    //门店详情
    public static String JAVA_STORE_DETAIL;
    // 主题
    public static String H5_Theme;
    //往期主题
    public static String H5_Past_Theme;
    //往期主题详情
    public static String H5_Past_Theme_Detail;
    //活动详情
    public static String H5_Theme_Activity;
    //资讯详情
    public static String H5_Theme_News;
    //报名
    public static String JAVA_SIGN_UP;
    //评论
    public static String JAVA_COMMENT;
    //保存评论
    public static String JAVA_SAVE_COMMENT;
    //删除评论
    public static String JAVA_DELETE_COMMENT;
    //取消点赞
    public static String JAVA_DEL_PRAISE;
    //增加点赞
    public static String JAVA_ADD_PRAISE;
    //splash
    public static String JAVA_SPLASH;
    //活动/资讯详情是否被关注
    public static String JAVA_NEWS_OR_ACTIVITY_GET_ATTENTION;
    //关注资讯
    public static String JAVA_NEWS_ATTENTION;
    //关注活动
    public static String JAVA_ACTIVITY_CANCEL_ATTENTION;

    public static void setBaseUrl(String base_url, String web_base_url, String eju_pay_url, String java_url, String base_pic_write) {
        BASE_URL = base_url;
        WEB_BASE_URL = web_base_url;
        EJU_PAY_URL = eju_pay_url;
        JAVA_URL = java_url;
        BASE_PIC_WRITE = base_pic_write;
        changeUrl(base_url, web_base_url, eju_pay_url, java_url, base_pic_write);
    }

    public static void changeUrl(String BASE_URL, String WEB_BASE_URL, String EJU_PAY_URL, String JAVA_URL, String BASE_PIC_WRITE) {
        PIC_WIRTE = BASE_PIC_WRITE + "FileWriterInner.php";
        TEST_BASE_URL = BASE_URL + "/" + API_VERSION;
        H5_BASE_URL = WEB_BASE_URL + "/" + H5_VERSION;
        SERVER_INDEX_NEW = JAVA_URL + "/dubboServiceConsumer/v2.6/school/findSchool.action?";
        SERVER_RESERVATION = TEST_BASE_URL + "/schedule_api.php?";
        SERVER_COMMIT_CART = TEST_BASE_URL + "/order_api.php";
        SERVER_COURSE = TEST_BASE_URL + "/course_api.php";
        SERVER_TEACHER_MORE_JAVA = JAVA_URL + "/dubboServiceConsumer/teacher/get_teacher_list.action";
        SERVER_TEACHER_API_JAVA = JAVA_URL + "/dubboServiceConsumer/teacher/get_teacher_detail.action";
        SERVER_TEACHER = JAVA_URL + "/dubboServiceConsumer/teacher/index.action?";
        SERVER_EXPERIENCE_CLASS = JAVA_URL + "/dubboServiceConsumer/course/experienceCourse.action?";
        SERVER_INTERACTION_PRAISE = JAVA_URL + "/dubboServiceConsumer/clickpraise/findall.action?";
        SERVER_INTERACTION_COMMENT = JAVA_URL + "/dubboServiceConsumer/comment/findMeComment.action?";
        SERVER_INTERACTION_ACTIVITY = JAVA_URL + "/dubboServiceConsumer/aactivityInteres/findArticleActivityInterest.action?";
        SERVER_USER_API = TEST_BASE_URL + "/user_api.php?";
        SERVER_USER_COUPON = TEST_BASE_URL + "/order_api.php?";
        SERVER_API_COMMON = TEST_BASE_URL + "/common_api.php?";
        SERVER_API_PAY = TEST_BASE_URL + "/pay_api.php?";
        SERVER_WEB_CLASSDETAIL = H5_BASE_URL + "/classroomdetail.html?";
        SERVER_WEB_AROUND = H5_BASE_URL + "/around.html?";
        SERVER_WEB_PICDES = H5_BASE_URL + "/picdes.html?";
        SERVER_WEB_TEACHERDETAIL = H5_BASE_URL + "/teacherdetail.html?tid=%s&teacherdetail=teacherdetail&uid=%s";
        SERVER_WEB_REFUNDAMOUNT = H5_BASE_URL + "/refundamountrecord.html?";
        SERVER_WEB_COUPONLIST = H5_BASE_URL + "/couponlist.html?";
        SERVER_WEB_COUPONDESCRIPTION = H5_BASE_URL + "/coupondescription.html?";
        SERVER_WEB_USERAGENT = H5_BASE_URL + "/useragent.html";
        SERVER_WEB_CHARGE_BACK = H5_BASE_URL + "/recharge.html";
        SERVER_WEB_SEARCH_RESULT = H5_BASE_URL + "/search.html?newpic=1&";//newpic=1为新版读图新加参数
        SERVER_WEB_TO_CLASSDETAIL = H5_BASE_URL + "/trapmap2.html?";
        H5_ACCOMPANY_TEACHER = H5_BASE_URL + "/recommendteacher.html?recommend=recommend" + "&uid=%s";
        SERVER_MINE_ORDER = TEST_BASE_URL + "/order_api.php?";
        SERVER_CLASSROOM_ARRANGE = TEST_BASE_URL + "/schedule_api.php?";
        ORDER_LOG = H5_BASE_URL + "/orderrecord.html?";
        TYPE_DESC = H5_BASE_URL + "/picclass.html?";
        SERVER_WEB_STORE_PRICE = H5_BASE_URL + "/orderfees_rooms.html?";
        SERVER_WEB_DEVICE_PRICE = H5_BASE_URL + "/orderfees_equipment.html?";
        H5_ORGANIZATION_AUTHENTICATION = H5_BASE_URL + "/certifyInfo.html?";
        H5_DRAW_UP_INVOICES = H5_BASE_URL + "/invoiceInfo.html?";
        H5_ADDED_VALUE_TAX = H5_BASE_URL + "/varinvoice.html?";
        H5_CLASSROOM_VALUE_TAX = H5_BASE_URL + "/room_price.html?";
        H5_ADDED_VALUE_TAX_IMG = H5_BASE_URL + "/imgs/varexcel.gif";
        JAVA_MORE_STORE = JAVA_URL + "/dubboServiceConsumer/school/moreSchool.action";
        JAVA_STORE_DETAIL = JAVA_URL + "/dubboServiceConsumer/v2.6/school/findSchoolDetail.action";
        H5_Theme = H5_BASE_URL + "/theme.html";
        H5_Past_Theme = H5_BASE_URL + "/pasttheme.html";
        H5_Past_Theme_Detail = H5_BASE_URL + "/pastthemedetail.html";
        H5_Theme_Activity = H5_BASE_URL + "/themeactivity.html";
        H5_Theme_News = H5_BASE_URL + "/themenews.html";
        JAVA_SIGN_UP = JAVA_URL + "/dubboServiceConsumer/enroll/saveEnroll.action";
        JAVA_COMMENT = JAVA_URL + "/dubboServiceConsumer/comment/findComment.action";
        JAVA_SAVE_COMMENT = JAVA_URL + "/dubboServiceConsumer/comment/saveComment.action";
        JAVA_DELETE_COMMENT = JAVA_URL + "/dubboServiceConsumer/comment/deleteComment.action";
        JAVA_DEL_PRAISE = JAVA_URL + "/dubboServiceConsumer/comment/delPraise.action";
        JAVA_ADD_PRAISE = JAVA_URL + "/dubboServiceConsumer/comment/addPraise.action";
        JAVA_SPLASH = JAVA_URL + "/dubboServiceConsumer/foucePic/findFoucePic.action";
        JAVA_NEWS_OR_ACTIVITY_GET_ATTENTION = JAVA_URL + "/dubboServiceConsumer/clickpraise/findClickPraise.action";
        JAVA_NEWS_ATTENTION = JAVA_URL + "/dubboServiceConsumer/articlenews/clickNewsPraise.action";
        JAVA_ACTIVITY_CANCEL_ATTENTION = JAVA_URL + "/dubboServiceConsumer/aactivity/clickActivityPraise.action";
        //wifi 原网址 : http://api.51shizhong.com/h5/wifi_check.html?tel=
        //特殊处理
        if (WEB_BASE_URL.equals("http://172.28.70.47")) {
            WIFI = "http://api.51shizhong.com/h5/wifi_check.html?tel=";
        } else {
            WIFI = WEB_BASE_URL + "/h5/wifi_check.html?tel=";
        }
    }

}
