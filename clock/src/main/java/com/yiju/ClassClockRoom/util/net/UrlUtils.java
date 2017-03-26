package com.yiju.ClassClockRoom.util.net;

import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpRemovalApi;

public class UrlUtils {
    // 测试地址为: http://172.28.70.47
    // 正式地址为: http://api.51shizhong.com
    // JAVA测试地址:http://172.28.70.47:8102

    public static String BASE_URL;
    public static String WEB_BASE_URL;
    public static String EJU_PAY_URL;
    public static String JAVA_URL;

    static {
        switch (BaseApplication.FORMAL_ENVIRONMENT) {
            case 1:
                BASE_URL = "http://172.28.70.47";
                WEB_BASE_URL = "http://172.28.70.47";
                JAVA_URL = "http://172.28.70.47:8102";
                EJU_PAY_URL = "http://inte.ejupay.cn:8130/gateway-outrpc/acquirer/interact";
                break;
            case 2:
            case 3:
                BASE_URL = "https://api.51shizhong.com";
                WEB_BASE_URL = "http://api.51shizhong.com";
                JAVA_URL = "https://api2.51shizhong.com";
                EJU_PAY_URL = "http://ejupay.17shihui.com/gateway-outrpc/acquirer/interact";

                if (StringUtils.isNotNullString(HttpRemovalApi.getInstance().changeBaseUrl)) {
                    BASE_URL = HttpRemovalApi.getInstance().changeBaseUrl;
                }
                if (StringUtils.isNotNullString(HttpRemovalApi.getInstance().changeJavaUrl)) {
                    JAVA_URL = HttpRemovalApi.getInstance().changeJavaUrl;
                }
                if (StringUtils.isNotNullString(HttpRemovalApi.getInstance().changeEjuPayUrl)) {
                    EJU_PAY_URL = HttpRemovalApi.getInstance().changeEjuPayUrl;
                }
                if (StringUtils.isNotNullString(HttpRemovalApi.getInstance().changeWebBaseUrl)) {
                    WEB_BASE_URL = HttpRemovalApi.getInstance().changeWebBaseUrl;
                }
                break;
        }
    }

    // 迁移接口
    public static String REMOVAL_API = "http://101.231.84.177/weburl.html";

    public static final String API_VERSION = "api_v9";
    //    public static final String API_VERSION = "api_develop";//api_develop(测试环境打包时使用)
    private static String TEST_BASE_URL = BASE_URL + "/" + API_VERSION;
    public static String H5_BASE_URL = WEB_BASE_URL + "/h5_v9";

    private static String BASE_CALSS_ROOM = TEST_BASE_URL
            + "/classroom_api.php?";

    // 首页ListView数据
//    public static String SERVER_INDEX = TEST_BASE_URL + "/api_merge/classroom_api.php?";
    public static String SERVER_INDEX = JAVA_URL + "/dubboServiceConsumer/redis/findClassroom_api.action";
    public static String SERVER_INDEX_NEW = JAVA_URL + "/dubboServiceConsumer/v2.6/school/findSchool.action?";
    // 教室详情页面
    public static String SERVER_SCHOOL_DETAIL = TEST_BASE_URL
            + "/classroom_api.php?";
    // 预订页面
    public static String SERVER_RESERVATION = TEST_BASE_URL
            + "/schedule_api.php?";
    // 购物车提交
    public static String SERVER_COMMIT_CART = TEST_BASE_URL + "/order_api.php";
    // 课程
    public static String SERVER_COURSE = TEST_BASE_URL + "/course_api.php";
    // 老师
    public static String SERVER_TEACHER_MORE = TEST_BASE_URL + "/teacher_api.php";
    public static String SERVER_TEACHER_MORE_JAVA = JAVA_URL + "/dubboServiceConsumer/teacher/get_teacher_list.action";
    public static String SERVER_TEACHER_API_JAVA = JAVA_URL + "/dubboServiceConsumer/teacher/get_teacher_detail.action";
    public static String SERVER_TEACHER = JAVA_URL + "/dubboServiceConsumer/teacher/index.action?";
    //体验课
    public static String SERVER_EXPERIENCE_CLASS = JAVA_URL + "/dubboServiceConsumer/course/experienceCourse.action?";
    //我的互动之 我赞过的
    public static String SERVER_INTERACTION_PRAISE = JAVA_URL + "/dubboServiceConsumer/clickpraise/findall.action?";
    //我的互动之 我的评论
    public static String SERVER_INTERACTION_COMMENT = JAVA_URL + "/dubboServiceConsumer/comment/findMeComment.action?";
    //我的互动之 我参与的活动
    public static String SERVER_INTERACTION_ACTIVITY = JAVA_URL + "/dubboServiceConsumer/aactivityInteres/findArticleActivityInterest.action?";
    // 预订第三个页面返回
    public static String SERVER_RESERVATION_THREE_BACK = TEST_BASE_URL
            + "/schedule_api.php";
    // 用户API
    public static final String SERVER_USER_API = TEST_BASE_URL + "/user_api.php?";
    // 优惠券
    public static String SERVER_USER_COUPON = TEST_BASE_URL + "/order_api.php?";
    // 检测版本
    public static String SERVER_API_COMMON = TEST_BASE_URL + "/common_api.php?";
    //获取工作密钥
    public static String SERVER_API_PAY = TEST_BASE_URL + "/pay_api.php?";


    // h5_v1页面地址
    /**
     * 首页_地址点击进入导航webView
     */
    public static String SERVER_WEB_TO_CLASSDETAIL = H5_BASE_URL
            + "/trapmap2.html?";

    // h5_v1页面地址
    /*
     * 首页,场馆详情：
	 * url：/h5_v1/classroomdetail.html?sid={场馆ID}&distance={当前用户位置距学校的距离} 可选参数：无
	 * 注:分享和预订按钮需app开发
	 */
    public static String SERVER_WEB_CLASSDETAIL = H5_BASE_URL
            + "/classroomdetail.html?";

    /*
     * 首页,周边环境详情： url： /h5_v1/around.html?sid={场馆ID} 可选参数：
     */
    public static String SERVER_WEB_AROUND = H5_BASE_URL
            + "/around.html?";

    /*
     * 首页,课室图片列表： url： /h5_v1/picdes.html?sid={场馆ID}
     * 可选参数：bigid：图片ID（如果带有这个参数表示直接显示查看大图页面）
     */
    public static String SERVER_WEB_PICDES = H5_BASE_URL
            + "/picdes.html?";

    /*
     * 良师 url：/h5_v1/teacherlist.html？uid={用户id} 可选参数：uid (登录后传入,未登录则不传或者传入0)
     * 注:该页面的点赞功能需app捕获request对象，判断用户是否登录
     * /h5_v1/checkloginpage.html?register=register
     */
    public static String SERVER_WEB_TEACHERLIST = H5_BASE_URL
            + "/teacherlist.html?uid=%s";

    /*
     * 良师详情页面：
     * url：/h5_v1/teacherdetail.html?tid={老师ID}&teacherdetail=teacherdetail
     * 可选参数：uid (登录后传入未登录不传或者传入0) teacherdetail：为了和点赞区别
     */
    public static String SERVER_WEB_TEACHERDETAIL = H5_BASE_URL
            + "/teacherdetail.html?tid=%s&teacherdetail=teacherdetail&uid=%s";

    /*
     * 我的（个人中心）, 订单, 费用详情： url：/h5_v1/listingfees.html?oid={订单ID}&uid={用户ID}
     * 可选参数： 注:在订单详情页点击‘订单金额’进入
     */
    public static String SERVER_WEB_LISTINGFEES = H5_BASE_URL
            + "/listingfees.html?";

    /*
     * 我的（个人中心）, 订单, 订单日志： url：/h5_v1/orderrecord.html?oid={订单ID} 可选参数：
     * 注:订单详情页点击'订单状态'进入
     */
    public static String SERVER_WEB_ORDERRECORD = H5_BASE_URL
            + "/orderrecord.html?";

    /*
     * 我的（个人中心）, 订单, 退款金额明细：
     * url：/h5_v1/refundamountrecord.html?oid={订单ID}&uid={用户ID} 可选参数：
     * 注:已退款成功的订单点击‘订单’进入
     */
    public static String SERVER_WEB_REFUNDAMOUNT = H5_BASE_URL
            + "/refundamountrecord.html?";

    /*
     * 我的（个人中心）, 我的优惠券： url：/h5_v1/ couponlist.html?uid={用户ID} 可选参数：
     */
    public static String SERVER_WEB_COUPONLIST = H5_BASE_URL
            + "/couponlist.html?";

    /*
     * 我的（个人中心）, 优惠券使用说明： url：/h5_v1/coupondescription.html 可选参数：
     */
    public static String SERVER_WEB_COUPONDESCRIPTION = H5_BASE_URL
            + "/coupondescription.html?";

    /*
     * 我的（个人中心）, 我的关注： url：/h5_v1/myfavorite.html?uid={用户ID} 可选参数：
     */
    public static String SERVER_WEB_MYFAVORITE = H5_BASE_URL
            + "/myfavorite.html?";

    /*
     * 我的（个人中心）, 更多-》用户协议： url：/h5_v1/useragent.html 可选参数：
     */
    public static String SERVER_WEB_USERAGENT = H5_BASE_URL
            + "/useragent.html";
    /**
     * 充返活动协议
     */
    public static String SERVER_WEB_CHARGE_BACK = H5_BASE_URL + "/recharge.html";

    /*
     * 搜索结果页 url： /h5_v1/search.html
     */
    public static String SERVER_WEB_SEARCH_RESULT = H5_BASE_URL
            + "/search.html?newpic=1&";//newpic=1为新版读图新加参数

    // ====================================陪读相关==============================================
    // 陪读密码登录房间
    public static String SERVER_ACCOMPANY_VERIFICATION = BASE_CALSS_ROOM
            + "action=online_watch";
    // 加入提醒
    public static String SERVER_ACCOMPANY_REMIND = BASE_CALSS_ROOM
            + "action=remember_video";
    // 取消提醒
    public static String SERVER_ACCOMPANY_CANLCE_REMIND = BASE_CALSS_ROOM
            + "action=unremember_video";
    // h5_v1老师推荐
    public static String H5_ACCOMPANY_TEACHER = H5_BASE_URL
            + "/recommendteacher.html?recommend=recommend" + "&uid=%s";

    // 我的订单
    // public static String SERVER_MINE_ORDER =
    // BASE_URL+"/order_api.php?";
    public static String SERVER_MINE_ORDER = TEST_BASE_URL + "/order_api.php?";
    //课室布置
    public static String SERVER_CLASSROOM_ARRANGE = TEST_BASE_URL + "/schedule_api.php?";
    // 订单日志h5_v1
    public static String ORDER_LOG = H5_BASE_URL + "/orderrecord.html?";
    // 教室费用h5_v1
    public static String CLASSROOM_PRICE = H5_BASE_URL
            + "/listingfees.html?";
    // WIFI认证h5_v1
    // public static String WIFI = BASE_URL + "/h5_v1/wifi_check.html?tel=";
    public static String WIFI = "http://api.51shizhong.com/h5/wifi_check.html?tel=";
    // 类型详情h5_v1
    public static String TYPE_DESC = H5_BASE_URL + "/picclass.html?";
    // 上传图片的内部写接口
    public static final String PIC_WIRTE = "http://i.upload.file.dc.cric.com/FileWriterInner.php";

    // 预订第三个页面返回
    public static String SERVER_FACILITY = TEST_BASE_URL + "/schedule_api.php";

    /*
     * 购物车课室可查看单价明细, 费用清单： url：/h5/shoppingcart_listfee.html?oid2={页面ID} 可选参数：
     */
    public static String SERVER_WEB_STORE_PRICE = H5_BASE_URL
            + "/orderfees_rooms.html?";
    /*
     * 购物车课室可查看单价明细, 费用清单： url：/h5/shoppingcart_listfee.html?oid2={页面ID} 可选参数：
     */
    public static String SERVER_WEB_DEVICE_PRICE = H5_BASE_URL
            + "/orderfees_equipment.html?";

    /**
     * 机构认证页
     */
    public static String H5_ORGANIZATION_AUTHENTICATION = H5_BASE_URL
            + "/certifyInfo.html?";

    /**
     * 如何开具发票页面
     */
    public static String H5_DRAW_UP_INVOICES = H5_BASE_URL
            + "/invoiceInfo.html?";
    /**
     * 增值税专用发票信息
     */
    public static String H5_ADDED_VALUE_TAX = H5_BASE_URL
            + "/varinvoice.html?";
    /**
     * 课室含税与不含税单价一览表
     */
    public static String H5_CLASSROOM_VALUE_TAX = H5_BASE_URL
            + "/room_price.html?";

    public static String H5_ADDED_VALUE_TAX_IMG = H5_BASE_URL
            + "/imgs/varexcel.gif";
    public static String H5_SPECIAL_TEACHER = H5_BASE_URL
            + "/subject_teacher.html";
    public static String H5_SPECIAL_COURSE = H5_BASE_URL
            + "/subject_course.html";
    public static String JAVA_MORE_STORE = JAVA_URL + "/dubboServiceConsumer/school/moreSchool.action";
    //门店详情
    public static String JAVA_STORE_DETAIL = JAVA_URL + "/dubboServiceConsumer/v2.6/school/findSchoolDetail.action";

    // 主题
    public static String H5_Theme = H5_BASE_URL + "/theme.html";
    //往期主题
    public static String H5_Past_Theme = H5_BASE_URL + "/pasttheme.html";
    //往期主题详情
    public static String H5_Past_Theme_Detail = H5_BASE_URL + "/pastthemedetail.html";
    //活动详情
    public static String H5_Theme_Activity = H5_BASE_URL + "/themeactivity.html";
    //资讯详情
    public static String H5_Theme_News = H5_BASE_URL + "/themenews.html";

    //报名
    public static String JAVA_SIGN_UP = JAVA_URL + "/dubboServiceConsumer/enroll/saveEnroll.action";
    //评论
    public static String JAVA_COMMENT = JAVA_URL + "/dubboServiceConsumer/comment/findComment.action";
    //保存评论
    public static String JAVA_SAVE_COMMENT = JAVA_URL + "/dubboServiceConsumer/comment/saveComment.action";
    //删除评论
    public static String JAVA_DELETE_COMMENT = JAVA_URL + "/dubboServiceConsumer/comment/deleteComment.action";
    //取消点赞
    public static String JAVA_DEL_PRAISE = JAVA_URL + "/dubboServiceConsumer/comment/delPraise.action";
    //增加点赞
    public static String JAVA_ADD_PRAISE = JAVA_URL + "/dubboServiceConsumer/comment/addPraise.action";
    //splash
    public static String JAVA_SPLASH = JAVA_URL + "/dubboServiceConsumer/foucePic/findFoucePic.action";
    //活动/资讯详情是否被关注
    public static String JAVA_NEWS_OR_ACTIVITY_GET_ATTENTION = JAVA_URL + "/dubboServiceConsumer/clickpraise/findClickPraise.action";
    //关注资讯
    public static String JAVA_NEWS_ATTENTION = JAVA_URL + "/dubboServiceConsumer/articlenews/clickNewsPraise.action";
    //关注活动
    public static String JAVA_ACTIVITY_CANCEL_ATTENTION = JAVA_URL + "/dubboServiceConsumer/aactivity/clickActivityPraise.action";
}
