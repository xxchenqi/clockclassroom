package com.yiju.ClassClockRoom.common.constant;

/**
 * 定义的不变的web交互规则相关参数
 *
 * @author geliping
 */
public class WebConstant {
    // ==========================h5交互===============================
    // 参数 register = register 需要登录
    public static final String reStartLogin_Param = "register";
    // 参数teacherdetail = teacherdetail 跳转老师详情
    public static final String teacherDetail_Param = "teacherdetail";
    // 参数identity = identity 跳活动页
    public static final String action_Param = "identity";
    // 参数identity = timeclock 跳活动页
    public static final String timeclock_Param = "timeclock";
    // 参数room 跳转良师
    public static final String room_Param = "room";
    // 参数trapmap 地图页面
    public static final String trapmap = "trapmap";
    // 参数classroomdetail 教室详情
    public static final String classroomdetail = "classroomdetail";
    // 参数teacherlist
    public static final String teacherlist = "teacherlist";
    // 参数checkloginpage
    public static final String checkloginpage = "checkloginpage";
    // 参数picdes
    public static final String picdes = "picdes";
    // 参数around
    public static final String around = "around";
    // varinvoice
    public static final String varinvoice = "varinvoice";
    // 单价一览表
    public static final String room_price = "room_price";
    // 往期主题
    public static final String past_theme = "pasttheme";

    // ==========================h5传值===============================
    // URL参数 老师ID
    public static final String teacher_Id = "tid";
    // URL参数 自己ID
    public static final String owner_Id = "owner";
    // URL参数 标题
    public static final String bannertitle_Param = "bannertitle";
    // URL参数 餐馆标题
    public static final String title_Param = "title";

    // ==========================本地页面交互===============================
    // web老师信息页面
    public static final int WEB_Int_TeachInfo_Page = 1;
    // web用户协议
    public static final int WEB_Int_UserAgreement_Page = 2;
    // web优惠券
    public static final int WEB_Int_Coupon_Page = 3;
    // web活动页
    public static final int WEB_Int_Action_Page = 4;
    // web地图页
    public static final int WEB_Int_Map_Page = 5;
    // web教室搜索页
    public static final int WEB_Int_Class_Page = 6;
    // 订单日志页
    public static final int Order_Log_Page = 7;
    // 教室费用页
    public static final int Classroom_Page = 8;
    // 认证wifi页
    public static final int WiFi_Page = 9;
    // 评论页
    public static final int Comment_Page = 10;
    // 图片详情页
    public static final int Picclass_Page = 11;
    // 照片列表页
    public static final int Picdes_Page = 12;
    // 周边环境页
    public static final int Around_Page = 13;

    //我的关注
    public static final int Attention_Page = 14;
    //费用清单
    public static final int Expense_list_Page = 15;
    //机构认证
    public static final int Organization_authentication_Page = 16;

    //如何开具发票
    public static final int Draw_up_invoices_Page = 17;
    //增值税发票
    public static final int Added_value_tax_Page = 18;
    //课室含税与不含税单价一览表
    public static final int Classroom_value_tax_Page = 19;
    //老师专题
    public static final int WEB_value_special_teacher_Page = 20;
    //课程专题
    public static final int WEB_value_special_course_Page = 21;
    //充返活动协议
    public static final int WEB_value_charge_back_Page = 22;

    //往期主题
    public static final int WEB_value_past_theme_Page = 23;
    //往期主题详情
    public static final int WEB_value_past_theme_detail_Page = 24;
    //活动详情
    public static final int WEB_value_theme_activity_Page = 25;
    //资讯详情
    public static final int WEB_value_theme_news_Page = 26;
    //启动页跳转的app内部活动页
    public static final int WEB_value_splash_news_Page = 27;

}
