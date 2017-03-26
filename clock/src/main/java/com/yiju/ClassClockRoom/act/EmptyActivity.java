package com.yiju.ClassClockRoom.act;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.accompany.AccompanyReadStatusActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.act.remind.RemindSetActivity;
import com.yiju.ClassClockRoom.act.search.SearchActivity;
import com.yiju.ClassClockRoom.act.search.Search_Result_Activity;
import com.yiju.ClassClockRoom.bean.MessageBox;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.FragmentFactory;
import com.yiju.ClassClockRoom.control.SchemeControl;
import com.yiju.ClassClockRoom.control.map.NavigationUtils;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/11/9 11:17
 * ----------------------------------------
 */
public class EmptyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = getIntent();
            if (intent != null) {
                Uri uri = intent.getData();
                if (uri != null) {
                    String path = uri.getPath();
                    if (path != null) {
                        Intent intent_start = new Intent();
                        if (path.equals(UIUtils.getString(R.string.scheme_home_path))) {//首页
                            intent_start.setClass(this, MainActivity.class);
                            intent_start.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_INDEX);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_course_home_path))) {//课程首页
                            intent_start.setClass(this, MainActivity.class);
                            intent_start.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_COURSE);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_teacher_home_path))) {//老师首页
                            intent_start.setClass(this, MainActivity.class);
                            intent_start.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_TEACHER);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_personal_home_path))) {//个人中心
                            intent_start.setClass(this, MainActivity.class);
                            intent_start.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_MY);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_course_list_path))) {//课程列表
                            intent_start.setClass(this, CourseMoreActivity.class);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_course_detail_path))) {//课程详情
                            intent_start.setClass(this, CourseDetailActivity.class);
                            intent_start.putExtra("COURSE_ID", uri.getQueryParameter(SchemeControl.COURSE_ID));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_store_list_path))) {//门店列表
                            intent_start.setClass(this, MoreStoreActivity.class);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_store_detail_path))) {//门店详情
                            intent_start.setClass(this, SingleStoreActivity.class);
                            intent_start.putExtra("sid", uri.getQueryParameter(SchemeControl.STORE_ID));
                            intent_start.putExtra("school_type", uri.getQueryParameter(SchemeControl.SCHOOL_TYPE));
                            intent_start.putExtra("store_name", uri.getQueryParameter(SchemeControl.STORE_NAME));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_reserve_path))) {//预定
                            intent_start.setClass(this, ReservationActivity.class);
                            intent_start.putExtra("sid", uri.getQueryParameter(SchemeControl.STORE_ID));
                            intent_start.putExtra("name", uri.getQueryParameter(SchemeControl.STORE_NAME));
                            intent_start.putExtra("type_id", uri.getQueryParameter(SchemeControl.TYPE_ID));
                            intent_start.putExtra("room_start_time", uri.getQueryParameter(SchemeControl.ROOM_START_TIME));
                            intent_start.putExtra("room_end_time", uri.getQueryParameter(SchemeControl.ROOM_END_TIME));
                            intent_start.putExtra("room_name", uri.getQueryParameter(SchemeControl.ROOM_NAME));
                            intent_start.putExtra("instruction", uri.getQueryParameter(SchemeControl.INSTRUCTION));
                            intent_start.putExtra("confirm_type", uri.getQueryParameter(SchemeControl.CONFIRM_TYPE));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_organization_teacher_list_path))) {//机构老师列表
                            intent_start.setClass(this, TeacherMoreActivity.class);
                            intent_start.putExtra(TeacherMoreActivity.ACTION_TYPE, TeacherMoreActivity.ORGANIZATION_TYPE);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_personal_teacher_list_path))) {//个人老师列表
                            intent_start.setClass(this, TeacherMoreActivity.class);
                            intent_start.putExtra(TeacherMoreActivity.ACTION_TYPE, TeacherMoreActivity.PERSONAL_TYPE);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_teacher_detail_path))) {//老师详情
                            intent_start.setClass(this, TeacherDetailActivity.class);
                            intent_start.putExtra("id", uri.getQueryParameter(SchemeControl.TEACHER_ID));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_accompany_read_path))) {//陪读
                            intent_start.setClass(this, AccompanyReadStatusActivity.class);
                            intent_start.putExtra(SchemeControl.PASSWORD, uri.getQueryParameter(SchemeControl.PASSWORD));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_place_detail_path))) {//场地详情(H5)
                            intent_start.setClass(this, IndexDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("sid", uri.getQueryParameter(SchemeControl.STORE_ID));
                            bundle.putString("name", uri.getQueryParameter(SchemeControl.STORE_NAME));
                            bundle.putString("address", uri.getQueryParameter(SchemeControl.STORE_ADDRESS));
                            bundle.putString("tags", uri.getQueryParameter(SchemeControl.STORE_TAGS));
                            intent_start.putExtras(bundle);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_classroom_detail_path))) {//课室详情(H5)
                            intent_start.setClass(this, IndexDetailActivity.class);
                            intent_start.putExtra("sid", uri.getQueryParameter(SchemeControl.STORE_ID));
                            intent_start.putExtra("sname", uri.getQueryParameter(SchemeControl.STORE_NAME));
                            intent_start.putExtra("type_desc", uri.getQueryParameter(SchemeControl.TYPE_DESC));
                            intent_start.putExtra("typeid", uri.getQueryParameter(SchemeControl.TYPE_ID));
                            intent_start.putExtra("room_start_time", uri.getQueryParameter(SchemeControl.ROOM_START_TIME));
                            intent_start.putExtra("room_end_time", uri.getQueryParameter(SchemeControl.ROOM_END_TIME));
                            intent_start.putExtra("room_name", uri.getQueryParameter(SchemeControl.ROOM_NAME));
                            intent_start.putExtra("school_type", uri.getQueryParameter(SchemeControl.SCHOOL_TYPE));
                            intent_start.putExtra("instruction", uri.getQueryParameter(SchemeControl.INSTRUCTION));
                            intent_start.putExtra("confirm_type", uri.getQueryParameter(SchemeControl.CONFIRM_TYPE));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_map_path))) {//地图(H5)
                            intent_start.setClass(this, Common_Show_WebPage_Activity.class);
                            boolean isHaveBaidu = NavigationUtils.isInstallByread("com.baidu.BaiduMap");
                            boolean isHaveGaoDe = NavigationUtils.isInstallByread("com.autonavi.minimap");
                            String haveMap;
                            if (!isHaveBaidu && !isHaveGaoDe) {
                                haveMap = "0";
                            } else {
                                haveMap = "1";
                            }
                            String url = UrlUtils.SERVER_WEB_TO_CLASSDETAIL
                                    + "trapmap=trapmap&title=" + uri.getQueryParameter(SchemeControl.STORE_NAME)
                                    + "&to=" + uri.getQueryParameter(SchemeControl.LNG_BD) + "," + uri.getQueryParameter(SchemeControl.LAT_BD)
                                    + "&to_g=" + uri.getQueryParameter(SchemeControl.LNG) + "," + uri.getQueryParameter(SchemeControl.LAT)
                                    + "&address=" + uri.getQueryParameter(SchemeControl.STORE_ADDRESS) + "&havemap=" + haveMap;
                            intent_start.putExtra(Common_Show_WebPage_Activity.Param_String_Title, uri.getQueryParameter(SchemeControl.STORE_NAME));
                            intent_start.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
                            intent_start.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.WEB_Int_Map_Page);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_atlas_list_path))) {//图集列表(H5)
                            intent_start.setClass(this, Common_Show_WebPage_Activity.class);
                            String url = UrlUtils.SERVER_WEB_PICDES + "sid="
                                    + uri.getQueryParameter(SchemeControl.STORE_ID)
                                    + "&piclist=piclist";
                            intent_start.putExtra(Common_Show_WebPage_Activity.Param_String_Title, uri.getQueryParameter(SchemeControl.TITLE));
                            intent_start.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
                            intent_start.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.Picdes_Page);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_course_subject_path))) {//课程专题页(H5)
                            intent_start.setClass(this, Common_Show_WebPage_Activity.class);
                            intent_start.putExtra(UIUtils.getString(R.string.get_page_name),
                                    WebConstant.WEB_value_special_teacher_Page);
                            intent_start.putExtra(UIUtils.getString(R.string.redirect_open_url), URLDecoder.decode(uri.getQueryParameter(SchemeControl.URL), "utf-8"));
                            intent_start.putExtra("special_id", uri.getQueryParameter(SchemeControl.SUBJECT_ID));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_teacher_subject_path))) {//老师专题页(H5)
                            intent_start.setClass(this, Common_Show_WebPage_Activity.class);
                            intent_start.putExtra(UIUtils.getString(R.string.get_page_name),
                                    WebConstant.WEB_value_special_teacher_Page);
                            intent_start.putExtra(UIUtils.getString(R.string.redirect_open_url), URLDecoder.decode(uri.getQueryParameter(SchemeControl.URL), "utf-8"));
                            intent_start.putExtra("special_id", uri.getQueryParameter(SchemeControl.SUBJECT_ID));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_authentication_wifi_path))) {//认证WIFI(H5)
                            intent_start.setClass(this, Common_Show_WebPage_Activity.class);
                            intent_start.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.WiFi_Page);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_message_path))) {//我的消息
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, Messages_Activity.class);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_order_message_path))) {//订单消息
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MessageDetialActivity.class);
                                MessageBox messageBox = GsonTools.changeGsonToBean(uri.getQueryParameter(SchemeControl.MESSAGE_BOX), MessageBox.class);
                                intent_start.putExtra("messageBox", messageBox);
                                intent_start.putExtra("big_type", 1);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                MessageBox messageBox = GsonTools.changeGsonToBean(uri.getQueryParameter(SchemeControl.MESSAGE_BOX), MessageBox.class);
                                intent_start.putExtra("messageBox", messageBox);
                                intent_start.putExtra("big_type", 1);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_system_message_path))) {//系统消息
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MessageDetialActivity.class);
                                MessageBox messageBox = GsonTools.changeGsonToBean(uri.getQueryParameter(SchemeControl.MESSAGE_BOX), MessageBox.class);
                                intent_start.putExtra("messageBox", messageBox);
                                intent_start.putExtra("big_type", 2);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                MessageBox messageBox = GsonTools.changeGsonToBean(uri.getQueryParameter(SchemeControl.MESSAGE_BOX), MessageBox.class);
                                intent_start.putExtra("messageBox", messageBox);
                                intent_start.putExtra("big_type", 2);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_accompany_read_remind_path))) {//陪读提醒
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MessageDetialActivity.class);
                                MessageBox messageBox = GsonTools.changeGsonToBean(uri.getQueryParameter(SchemeControl.MESSAGE_BOX), MessageBox.class);
                                intent_start.putExtra("messageBox", messageBox);
                                intent_start.putExtra("big_type", 3);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                MessageBox messageBox = GsonTools.changeGsonToBean(uri.getQueryParameter(SchemeControl.MESSAGE_BOX), MessageBox.class);
                                intent_start.putExtra("messageBox", messageBox);
                                intent_start.putExtra("big_type", 3);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_search_path))) {//搜索页
                            intent_start.setClass(this, SearchActivity.class);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_search_result_path))) {//搜索结果页(H5)
                            intent_start.setClass(this, Search_Result_Activity.class);
                            intent_start.putExtra("keyword", uri.getQueryParameter(SchemeControl.KEYWORD));
                            intent_start.putExtra("flag", uri.getQueryParameter(SchemeControl.FLAG));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_around_environment_path))) {//周边环境页(H5)
                            intent_start.setClass(this, Common_Show_WebPage_Activity.class);
                            intent_start.putExtra(UIUtils.getString(R.string.redirect_open_url), URLDecoder.decode(uri.getQueryParameter(SchemeControl.URL), "utf-8"));
                            intent_start.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "周边环境");
                            intent_start.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.Around_Page);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_around_environment_map_path))) {//周边环境地图页(H5)
                            String url = URLDecoder.decode(uri.getQueryParameter(SchemeControl.URL), "utf-8");
                            Uri parseUri = Uri.parse(url);
                            String title = parseUri.getQueryParameter(WebConstant.title_Param);
                            intent_start.setClass(this, Common_Show_WebPage_Activity.class);
                            intent_start.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
                            intent_start.putExtra(Common_Show_WebPage_Activity.Param_String_Title, title);
                            intent_start.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.WEB_Int_Map_Page);
                        } else if (path.equals(UIUtils.getString(R.string.scheme_personal_information_path))) {//个人信息
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, PersonalCenter_InformationActivity.class);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_organization_authentication_path))) {//机构认证(H5)
                            intent_start.setClass(UIUtils.getContext(), Common_Show_WebPage_Activity.class);
                            intent_start.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.Organization_authentication_Page);
                            intent_start.putExtra(Common_Show_WebPage_Activity.Param_String_Title, UIUtils.getString(R.string.i_need_authentication));
                        } else if (path.equals(UIUtils.getString(R.string.scheme_personal_teacher_information_path))) {//个人老师资料
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(UIUtils.getContext(), MemberDetailActivity.class);
                                intent_start.putExtra(MemberDetailActivity.ACTION_UID, uri.getQueryParameter(SchemeControl.TEACHER_ID));
                                intent_start.putExtra(MemberDetailActivity.ACTION_SHOW_TEACHER, uri.getQueryParameter(SchemeControl.SHOW_TEACHER));
                                intent_start.putExtra(MemberDetailActivity.ACTION_ORG_AUTH, uri.getQueryParameter(SchemeControl.ORG_AUTH));
                                intent_start.putExtra(MemberDetailActivity.ACTION_MOBILE, uri.getQueryParameter(SchemeControl.MOBILE));
                                intent_start.putExtra(MemberDetailActivity.ACTION_title, uri.getQueryParameter(SchemeControl.TITLE));
                                intent_start.putExtra(MemberDetailActivity.ACTION_COURSE_FLAG, uri.getBooleanQueryParameter(SchemeControl.COURSE_FLAG, false));
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                                intent_start.putExtra(MemberDetailActivity.ACTION_UID, uri.getQueryParameter(SchemeControl.TEACHER_ID));
                                intent_start.putExtra(MemberDetailActivity.ACTION_SHOW_TEACHER, uri.getQueryParameter(SchemeControl.SHOW_TEACHER));
                                intent_start.putExtra(MemberDetailActivity.ACTION_ORG_AUTH, uri.getQueryParameter(SchemeControl.ORG_AUTH));
                                intent_start.putExtra(MemberDetailActivity.ACTION_MOBILE, uri.getQueryParameter(SchemeControl.MOBILE));
                                intent_start.putExtra(MemberDetailActivity.ACTION_title, uri.getQueryParameter(SchemeControl.TITLE));
                                intent_start.putExtra(MemberDetailActivity.ACTION_COURSE_FLAG, uri.getBooleanQueryParameter(SchemeControl.COURSE_FLAG, false));
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_organization_path))) {//我的机构
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MineOrganizationActivity.class);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_course_path))) {//我的课程
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, PersonMineCourseActivity.class);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_publish_course_path))) {//发布课程
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, PublishActivity.class);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_all_path))) {//我的订单-全部订单
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MineOrderActivity.class);
                                intent_start.putExtra(MineOrderActivity.STATUS, MineOrderActivity.STATUS_ALL);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_wait_pay_path))) {//我的订单-待支付
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MineOrderActivity.class);
                                intent_start.putExtra(MineOrderActivity.STATUS, MineOrderActivity.STATUS_WAIT_PAY);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_underway_path))) {//我的订单-进行中
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MineOrderActivity.class);
                                intent_start.putExtra(MineOrderActivity.STATUS, MineOrderActivity.STATUS_USE);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_finish_path))) {//我的订单-已完成
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MineOrderActivity.class);
                                intent_start.putExtra(MineOrderActivity.STATUS, MineOrderActivity.STATUS_FINISH);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_discount_coupon_path))) {//我的优惠券(H5)
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, PersonalCenter_CouponListActivity.class);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_mine_attention_path))) {//我的关注
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, MineWatchlistActivity.class);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_remind_set_path))) {//提醒设置
                            if (!"-1".equals(StringUtils.getUid())) {
                                intent_start.setClass(this, RemindSetActivity.class);
                            } else {
                                intent_start.setClass(this, LoginActivity.class);
                                intent_start.putExtra(SchemeControl.PATH, path);
                            }
                        } else if (path.equals(UIUtils.getString(R.string.scheme_more_path))) {//更多
                            intent_start.setClass(this, PersonalCenter_MoreActivity.class);
                        } else {//默认跳转首页
                            intent_start.setClass(this, MainActivity.class);
                            intent_start.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_INDEX);
                        }


                        intent_start.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_start);
                        overridePendingTransition(0, 0);
                        finish();
                        overridePendingTransition(0, R.anim.zoomout);
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
            } else {
                finish();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            finish();
        }

    }


}
