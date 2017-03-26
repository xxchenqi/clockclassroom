package com.yiju.ClassClockRoom.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ejupay.sdk.EjuPayManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.AvailableWifiStoreActivity;
import com.yiju.ClassClockRoom.act.InvoiceOrderActivity;
import com.yiju.ClassClockRoom.act.LoginActivity;
import com.yiju.ClassClockRoom.act.MemberDetailActivity;
import com.yiju.ClassClockRoom.act.Messages_Activity;
import com.yiju.ClassClockRoom.act.MineOrderActivity;
import com.yiju.ClassClockRoom.act.MineOrderCourseActivity;
import com.yiju.ClassClockRoom.act.MineOrganizationActivity;
import com.yiju.ClassClockRoom.act.MineWatchlistActivity;
import com.yiju.ClassClockRoom.act.MyWalletActivity;
import com.yiju.ClassClockRoom.act.OrganizationCertificationStatusActivity;
import com.yiju.ClassClockRoom.act.PersonMineCourseActivity;
import com.yiju.ClassClockRoom.act.PersonMineInteractionActivity;
import com.yiju.ClassClockRoom.act.PersonalCenter_CouponListActivity;
import com.yiju.ClassClockRoom.act.PersonalCenter_InformationActivity;
import com.yiju.ClassClockRoom.act.PersonalCenter_MoreActivity;
import com.yiju.ClassClockRoom.act.PublishActivity;
import com.yiju.ClassClockRoom.act.accompany.AccompanyReadActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.MessageBoxNoRead;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.bean.result.UserInfo;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * ----------------------------------------
 * 注释: 个人中心
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/6/6 18:19
 * ----------------------------------------
 */
public class PersonalCenterFragment extends BaseFragment implements
        OnClickListener {

    //头部布局
    private RelativeLayout rl_avatar;
    //昵称
    private TextView txt_nickname;
    //头像
    private CircleImageView iv_avatar;
    private TextView tv_message;

    //优惠券数量
    private TextView tv_person_center_discount;
    //未支付订单的显示视图
//    private BadgeView badgeView;
    //认证wifi
    private RelativeLayout rl_mywifi_arrow;
    //查看可用门店列表
    private TextView tv_check_stores;
    /* //待支付
     private LinearLayout ll_will_pay_order;
     //已支付
     private LinearLayout ll_using_order;
     //已完成
     private LinearLayout ll_achived_order;*/
    //发票
    private RelativeLayout rl_myinvoice_arrow;
    //余额
    private RelativeLayout rl_balance_arrow;
    //银行卡
    private RelativeLayout rl_bankcard_arrow;
    //优惠券
    private RelativeLayout rl_mycoupons_arrow;
    //我的关注
    private RelativeLayout rl_myfocuson_arrow;
    //陪读
    private RelativeLayout rl_video_arrow;
    //我的消息
    private RelativeLayout rl_message_arrow;
    //更多信息
    private RelativeLayout rl_more_info;
    //我的订单
    private RelativeLayout rl_mine_order;
    //我的课程订单
    private RelativeLayout rl_mine_course_order;
    //我的老师信息
    private RelativeLayout rl_mine_teacher_info;
    //个人老师资料信息说明
    private TextView tv_teacher_info;
    //我的机构
    private RelativeLayout rl_mine_organization;
    //机构名字
    private TextView tv_mine_organization;
    //是否绑定手机号
    private TextView txt_nickname_visa;
    //钱包
    private RelativeLayout rl_mywallet_arrow;
    //我的课程
    private RelativeLayout rl_course_arrow;
    //发布课程
    private RelativeLayout rl_show_course;
    //我的互动
    private RelativeLayout rl_my_interactive_arrow;
    //课室订单未支付数量
    private TextView tv_classroom_order_count;
    //课程订单未支付数量
    private TextView tv_course_order_count;

    //intent
    private Intent intent;
    //是否登录
    private boolean isLogin;
    //未支付订单的数量
    private String count;
    //是否退出的标志
    private boolean destroyFlag = false;
    //订单id
    private String org_id;
    //是否认证
    private String is_auth;
    //老师id
    private String teacher_id;
    //是否显示老师
    private String show_teacher;
    //管理权
    private String org_auth;
    //是否订过课室
    private String is_pay;

    @Override
    public int setContentViewId() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void onResume() {
        super.onResume();
        //每次都要去刷新用户信息数据
        destroyFlag = false;
        isLogin = SharedPreferencesUtils.getBoolean(UIUtils.getContext(),
                getResources().getString(R.string.shared_isLogin), false);
        changeInfoByLogin();
        if (NetWorkUtils.getNetworkStatus(UIUtils.getContext()) && isLogin) {
            getHttpForNoReadMsg();
        }
    }

    @Override
    public void initView() {
        rl_avatar = (RelativeLayout) currentView.findViewById(R.id.rl_avatar);
        tv_classroom_order_count = (TextView) currentView
                .findViewById(R.id.tv_classroom_order_count);
        tv_course_order_count = (TextView) currentView
                .findViewById(R.id.tv_course_order_count);
        tv_message = (TextView) currentView
                .findViewById(R.id.tv_message);
        rl_message_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_message_arrow);
        /*ll_will_pay_order = (LinearLayout) currentView
                .findViewById(R.id.ll_will_pay_order);
        ll_using_order = (LinearLayout) currentView
                .findViewById(R.id.ll_using_order);
        ll_achived_order = (LinearLayout) currentView
                .findViewById(R.id.ll_achived_order);*/
        rl_myinvoice_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_myinvoice_arrow);
        rl_balance_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_balance_arrow);
        rl_bankcard_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_bankcard_arrow);
        rl_mycoupons_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_mycoupons_arrow);
        rl_myfocuson_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_myfocuson_arrow);
        rl_video_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_video_arrow);
        rl_mywifi_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_mywifi_arrow);
        rl_more_info = (RelativeLayout) currentView
                .findViewById(R.id.rl_more_info);
        rl_mine_order = (RelativeLayout) currentView
                .findViewById(R.id.rl_mine_order);
        rl_mine_course_order = (RelativeLayout) currentView
                .findViewById(R.id.rl_mine_course_order);
        rl_mine_teacher_info = (RelativeLayout) currentView
                .findViewById(R.id.rl_mine_teacher_info);
        tv_teacher_info = (TextView) currentView
                .findViewById(R.id.tv_teacher_info);
        rl_mine_organization = (RelativeLayout) currentView
                .findViewById(R.id.rl_mine_organization);
        tv_check_stores = (TextView) currentView
                .findViewById(R.id.tv_check_stores);

        iv_avatar = (CircleImageView) currentView.findViewById(R.id.iv_avatar);
        tv_person_center_discount = (TextView) currentView
                .findViewById(R.id.tv_person_center_discount);
        txt_nickname = (TextView) currentView.findViewById(R.id.txt_nickname);
        tv_mine_organization = (TextView) currentView.findViewById(R.id.tv_mine_organization);
//        ImageView iv_will_pay_order = (ImageView) currentView
//                .findViewById(R.id.iv_will_pay_order);
//        badgeView = new BadgeView(UIUtils.getContext(), iv_will_pay_order);

        txt_nickname_visa = (TextView) currentView.findViewById(R.id.txt_nickname_visa);
        rl_mywallet_arrow = (RelativeLayout) currentView.findViewById(R.id.rl_mywallet_arrow);
        rl_course_arrow = (RelativeLayout) currentView.findViewById(R.id.rl_course_arrow);
        rl_show_course = (RelativeLayout) currentView.findViewById(R.id.rl_show_course);
        rl_my_interactive_arrow = (RelativeLayout) currentView.findViewById(R.id.rl_my_interactive_arrow);
    }


    @Override
    public void initListener() {
        super.initListener();
        rl_avatar.setOnClickListener(this);
        rl_message_arrow.setOnClickListener(this);
        rl_my_interactive_arrow.setOnClickListener(this);
        /*ll_will_pay_order.setOnClickListener(this);
        ll_using_order.setOnClickListener(this);
        ll_achived_order.setOnClickListener(this);*/
        rl_myinvoice_arrow.setOnClickListener(this);
        rl_balance_arrow.setOnClickListener(this);
        rl_bankcard_arrow.setOnClickListener(this);
        rl_mycoupons_arrow.setOnClickListener(this);
        rl_myfocuson_arrow.setOnClickListener(this);
        rl_mywifi_arrow.setOnClickListener(this);
        rl_video_arrow.setOnClickListener(this);

        rl_more_info.setOnClickListener(this);
        rl_mine_order.setOnClickListener(this);
        rl_mine_course_order.setOnClickListener(this);
        rl_mine_teacher_info.setOnClickListener(this);
        rl_mine_organization.setOnClickListener(this);
//        iv_avatar.setOnClickListener(this);
        rl_mywallet_arrow.setOnClickListener(this);
        rl_course_arrow.setOnClickListener(this);
        rl_show_course.setOnClickListener(this);
        tv_check_stores.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        txt_nickname.setText(getResources().getString(
                R.string.label_chooseLogin));
    }


    private void changeInfoByLogin() {

        if (isLogin) {
            // 如果用户是已经登录的状态，更新头像信息
            String name = StringUtils.getNickName();
            if (StringUtils.isNotNullString(name)) {
                txt_nickname.setText(name);
            } else {
                if ("0".equals(StringUtils.getThirdSource())) {
                    // 手机登录
                    //则自动取用户手机号为昵称，同时第4位到第7位以“*”展示
                    StringBuilder sb = new StringBuilder(StringUtils.getMobile());
                    sb.replace(3, 7, "****");
                    txt_nickname.setText(sb.toString());
                } else {
                    // 三方登录
                    //无法获取用户昵称则生成随机昵称“用户yymmddxxx”（yy为年份后两位，mm为月份，dd为日期，xxx为随机数，由数字或大小写字母组成）；
                    String nickname = new SimpleDateFormat("yyMMdd", Locale.CHINESE)
                            .format(Calendar.getInstance().getTime()) + StringUtils.randomStr(3);
                    txt_nickname.setText(nickname);
                    //不需要请求
//                    getHttpNickName(nickname);
                }
            }
            String headUrl = SharedPreferencesUtils.getString(UIUtils.getContext(), getResources()
                    .getString(R.string.shared_avatar), null);
            if (headUrl != null) {
                Glide.with(UIUtils.getContext()).load(headUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.personal_center_logo).into(iv_avatar);
            } else {
                iv_avatar.setImageResource(R.drawable.personal_center_logo);
            }
            // 更新所有数据
            getHttpUtils();
        } else {
            noLoginSet();
        }
    }

    private void noLoginSet() {
        // 设置初始值
        txt_nickname.setText(UIUtils.getString(R.string.label_chooseLogin_or_registered));
        iv_avatar.setImageResource(R.drawable.personal_center_logo);

        // 设置优惠券数量
        tv_person_center_discount.setText("");
        // 隐藏订单数量
//        badgeView.hide();
        //隐藏我的老师、我的机构、我的课程、发布课程、
        rl_mine_teacher_info.setVisibility(View.GONE);
        rl_mine_organization.setVisibility(View.GONE);
        rl_course_arrow.setVisibility(View.GONE);
        rl_show_course.setVisibility(View.GONE);
        //隐藏绑定手机号
        txt_nickname_visa.setVisibility(View.GONE);
        tv_message.setVisibility(View.GONE);
    }

    /**
     * 个人信息请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getInfo");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);
                    }
                });
    }

    private void processData(String result) {
        UserInfo userInfo = GsonTools.changeGsonToBean(result, UserInfo.class);
        if (userInfo == null) {
            return;
        }
        if ("1".equals(userInfo.getCode())) {
            UserInfo.Data data = userInfo.getData();
            if (StringUtils.isNotNullString(data.getMobile())) {
                txt_nickname_visa.setVisibility(View.VISIBLE);
                txt_nickname_visa.setText("已绑定");
            }
            if (!destroyFlag) {
                //存取到数据库所有信息(其实这步骤没用，可以直接获取)
                parseUserInfo(data, userInfo);

                //取出数据库所有信息
                //用户的个人信息，不是在login里获得的数据去存储，
                // 而是在getInfo里获得的信息去存储（因为手机号和三方账号绑定后的信息不一样，所以只取uid的信息）

                //资料待补全信息展示
                if ("0".equals(data.getFullteacherinfo())) {//0:不全 1:全
                    tv_teacher_info.setVisibility(View.VISIBLE);
                } else {
                    tv_teacher_info.setVisibility(View.GONE);
                }

                //设置优惠券数量
                String cnum = data.getCnum();
                if (cnum != null && !"".equals(cnum) && !"0".equals(cnum)) {
                    tv_person_center_discount.setText(String.format(UIUtils.getString(R.string.piece_of), cnum));
                }
                // 设置待支付数量
                count = userInfo.getAlert_order().getCount();
                if (count != null && !"".equals(count) && !"0".equals(count)) {
                    tv_classroom_order_count.setText(String.format(UIUtils.getString(R.string.format_no_payment_count), count));
                    tv_classroom_order_count.setTextColor(UIUtils.getColor(R.color.color_red_f1));
                } else {
                    tv_classroom_order_count.setText(UIUtils.getString(R.string.label_checkAllOrder));
                    tv_classroom_order_count.setTextColor(UIUtils.getColor(R.color.color_gay_99));
                }
                //课程待支付数量
                if (userInfo.getAlert_order_course() != null) {
                    String course_count = userInfo.getAlert_order_course().getCount();
                    if (StringUtils.isNotNullString(userInfo.getAlert_order_course().getCount())
                            && !"0".equals(userInfo.getAlert_order_course().getCount())) {
                        tv_course_order_count.setText(String.format(UIUtils.getString(R.string.format_no_payment_count), course_count));
                        tv_course_order_count.setTextColor(UIUtils.getColor(R.color.color_red_f1));
                    } else {
                        tv_course_order_count.setText(UIUtils.getString(R.string.label_checkAllOrder));
                        tv_course_order_count.setTextColor(UIUtils.getColor(R.color.color_gay_99));
                    }
                }
                //设置昵称
                String nickName = data.getNickname();
                if (StringUtils.isNotNullString(nickName)) {
                    txt_nickname.setText(nickName);
                } else {
                    if ("0".equals(StringUtils.getThirdSource())) {
                        // 手机登录
                        //则自动取用户手机号为昵称，同时第4位到第7位以“*”展示
                        StringBuilder sb = new StringBuilder(StringUtils.getMobile());
                        sb.replace(3, 7, "****");
                        txt_nickname.setText(sb.toString());
                        //请求
                        getHttpNickName(sb.toString());
                    } else {
                        // 三方登录
                        //无法获取用户昵称则生成随机昵称“用户yymmddxxx”（yy为年份后两位，mm为月份，dd为日期，xxx为随机数，由数字或大小写字母组成）；
                        String nickname = new SimpleDateFormat("yyMMdd", Locale.CHINESE)
                                .format(Calendar.getInstance().getTime()) + StringUtils.randomStr(3);
                        txt_nickname.setText(nickname);
                        //请求
                        getHttpNickName(nickname);
                    }
                }

                //设置简介
//                String teacher_info = data.getTeacher_info();
//                if (teacher_info != null && !"".equals(teacher_info)) {
//                    txt_nickname_visa.setVisibility(View.VISIBLE);
//                    txt_nickname_visa.setText(teacher_info);
//                } else {
//                    txt_nickname_visa.setVisibility(View.GONE);
//                }

                //设置头像
                String headUrl = data.getAvatar();
                // 根据头像地址加载图像
                if (headUrl != null && !"".equals(headUrl)) {
                    Glide.with(UIUtils.getContext()).load(headUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.user_unload).into(iv_avatar);
                } else {
                    iv_avatar.setImageResource(R.drawable.user_unload);
                }
                //初始化老师信息
                teacher_id = data.getTeacher_id();
                show_teacher = data.getShow_teacher();
                org_auth = data.getOrg_auth();
                //初始化认证信息
                org_id = userInfo.getData().getOrg_id();
                is_auth = userInfo.getData().getIs_auth();
                is_pay = userInfo.getData().getIs_pay();
                //设置机构名字
                tv_mine_organization.setText(userInfo.getData().getOrg_name());
                //3.0版本隐藏我的老师资料与我的机构
                /*if (org_id != null) {
                    if ("0".equals(org_id)) {
                        if ("2".equals(is_auth)) {
//                        UIUtils.showToastSafe("认证失败");
                            rl_mine_teacher_info.setVisibility(View.GONE);
                            rl_mine_organization.setVisibility(View.GONE);
                        } else {
//                        UIUtils.showToastSafe("未认证");
                            //如果订过课室，那么显示个人老师资料
                            if ("1".equals(is_pay)) {
                                rl_mine_teacher_info.setVisibility(View.GONE);
                            } else {
                                rl_mine_teacher_info.setVisibility(View.GONE);
                                rl_mine_organization.setVisibility(View.GONE);
                            }
                        }
                    } else if (Integer.valueOf(org_id) > 0) {
                        if ("0".equals(is_auth)) {
//                        UIUtils.showToastSafe("认证中");
                            rl_mine_teacher_info.setVisibility(View.GONE);
                            rl_mine_organization.setVisibility(View.GONE);
                        } else {
//                        UIUtils.showToastSafe("已认证");
                            rl_mine_teacher_info.setVisibility(View.GONE);
                            rl_mine_organization.setVisibility(View.GONE);
                        }
                    }
                }*/
            }

            if ("1".equals(is_pay)) {
                //显示我的课程和发布课程
                rl_course_arrow.setVisibility(View.GONE);
                rl_show_course.setVisibility(View.GONE);
            } else {
                rl_course_arrow.setVisibility(View.GONE);
                rl_show_course.setVisibility(View.GONE);
            }

        } else {
            UIUtils.showToastSafe(userInfo.getMsg());
        }
    }

    /**
     * 信息存储
     *
     * @param userInfo 用户信息
     */
    public static void parseUserInfo(UserInfo.Data userInfo, UserInfo user) {
        Context context = UIUtils.getContext();
        // 成功获取到用户信息，写入本地
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_id), userInfo.getId());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_name),
                userInfo.getName());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_sex),
                userInfo.getSex());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_age),
                userInfo.getAge());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_mobile),
                userInfo.getMobile());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_email),
                userInfo.getEmail());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_nickname),
                userInfo.getNickname());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_avatar), userInfo.getAvatar());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_praise),
                userInfo.getPraise());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_credit),
                userInfo.getCredit());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_create_time),
                userInfo.getCreate_time());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_login_time),
                userInfo.getLogin_time());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_is_teacher),
                userInfo.getIs_teacher());
//        SharedPreferencesUtils.saveString(context,
//                context.getResources().getString(R.string.shared_third_source),
//                userInfo.getThird_source());
//        SharedPreferencesUtils.saveString(context,
//                context.getResources().getString(R.string.shared_third_id),
//                userInfo.getThird_id());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_remerber),
                userInfo.getRemerber());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_is_remerber),
                userInfo.getIs_remerber());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_is_reserve_remerber),
                userInfo.getIs_reserve_remerber());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_is_pay_remerber),
                userInfo.getIs_pay_remerber());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_trouble_btime),
                userInfo.getTrouble_btime());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_trouble_etime),
                userInfo.getTrouble_etime());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_is_sys_remerber),
                userInfo.getIs_sys_remerber());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_is_order_remerber),
                userInfo.getIs_order_remerber());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_badge),
                userInfo.getBadge());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_cnum),
                userInfo.getCnum());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_third_qq),
                userInfo.getThird_qq());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_third_wechat),
                userInfo.getThird_wechat());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_third_weibo),
                userInfo.getThird_weibo());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_real_name),
                userInfo.getReal_name());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_show_teacher),
                userInfo.getShow_teacher());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_org_id),
                userInfo.getOrg_id());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_org_name),
                userInfo.getOrg_name());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_is_auth),
                userInfo.getIs_auth());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_org_auth),
                userInfo.getOrg_auth());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_teacher_id),
                userInfo.getTeacher_id());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_teacher_info),
                userInfo.getTeacher_info());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_black_count),
                user.getOrg_black_count());
        SharedPreferencesUtils.saveString(context,
                context.getResources().getString(R.string.shared_is_pay),
                userInfo.getIs_pay());
    }

    /**
     * 获取未读消息数请求
     */
    public void getHttpForNoReadMsg() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "message_box_noread");
        params.addBodyParameter("uid", StringUtils.getUid());
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForNoReadMsg(arg0.result);
                    }
                }
        );
    }

    /**
     * 未读消息接口请求结果处理
     *
     * @param result result
     */
    private void processDataForNoReadMsg(String result) {
        MessageBoxNoRead messageBoxNoread = GsonTools.changeGsonToBean(result, MessageBoxNoRead.class);
        if (messageBoxNoread != null) {
            if ("1".equals(messageBoxNoread.getCode())) {
                int count = Integer.parseInt(messageBoxNoread.getNoread_count());
                if (count <= 0) {
                    count = 0;
                    tv_message.setVisibility(View.GONE);
                } else {
                    tv_message.setVisibility(View.VISIBLE);
                }
                if (count > 99) {
                    tv_message.setText(UIUtils.getString(R.string.jiujiujia));
                } else {
                    tv_message.setText(count + "");
                }
            }
        }
    }


    /**
     * 修改昵称请求
     */
    private void getHttpNickName(final String name) {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "modifyInfo");
        params.addBodyParameter("type", "nickname");
        params.addBodyParameter("value", name);
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataNickName(arg0.result, name);

                    }
                });
    }

    private void processDataNickName(String result, String name) {
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            SharedPreferencesUtils.saveString(getActivity(),
                    getResources().getString(R.string.shared_nickname), name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_avatar:
                //头像点击个人中心信息
                startActivityPage(PersonalCenter_InformationActivity.class, true);
                break;
            case R.id.rl_message_arrow://我的消息
                if (!"-1".equals(StringUtils.getUid())) {
                    Intent intentMessage = new Intent(UIUtils.getContext(),
                            Messages_Activity.class);
                    UIUtils.startActivity(intentMessage);
                } else {
                    Intent intentLogin = new Intent(UIUtils.getContext(),
                            LoginActivity.class);
                    UIUtils.startActivity(intentLogin);
                }
                break;
            case R.id.rl_my_interactive_arrow:
                //我的互动
                startActivityPage(PersonMineInteractionActivity.class, true);
                break;
            case R.id.rl_mine_teacher_info:
                //个人老师资料
                //当前老师信息为完善并且当前不是管理员并且未定过课室
               /* if ("0".equals(teacher_id) && !"2".equals(org_auth) && !"1".equals(is_pay)) {
                    //未完善老师资料
                    startActivity(new Intent(getActivity(), TeacherInformationFailActivity.class));
                } else {*/
                //已完善老师资料->个人老师资料详情
                String mobile = SharedPreferencesUtils.getString(
                        UIUtils.getContext(),
                        getResources().getString(R.string.shared_mobile), "");
                Intent intent = new Intent(getActivity(), MemberDetailActivity.class);
                intent.putExtra(MemberDetailActivity.ACTION_UID, StringUtils.getUid());
                intent.putExtra(MemberDetailActivity.ACTION_SHOW_TEACHER, show_teacher);
                intent.putExtra(MemberDetailActivity.ACTION_ORG_AUTH, org_auth);
                intent.putExtra(MemberDetailActivity.ACTION_MOBILE, mobile);
                intent.putExtra(MemberDetailActivity.ACTION_TITLE,
                        UIUtils.getString(R.string.teacher_detail));
                startActivity(intent);
//                }
                break;
            case R.id.rl_mine_organization:
                //我的机构
                if ("0".equals(org_id)) {
                    if ("2".equals(is_auth)) {
                        //认证失败
                        intent = new Intent(UIUtils.getContext(),
                                OrganizationCertificationStatusActivity.class);
                        intent.putExtra(OrganizationCertificationStatusActivity.STATUS,
                                OrganizationCertificationStatusActivity.STATUS_FAIL);
                        startActivity(intent);
                    }
                } else if (Integer.valueOf(org_id) > 0) {
                    if ("0".equals(is_auth)) {
                        //认证中
                        intent = new Intent(UIUtils.getContext(),
                                OrganizationCertificationStatusActivity.class);
                        intent.putExtra(OrganizationCertificationStatusActivity.STATUS,
                                OrganizationCertificationStatusActivity.STATUS_ING);
                        startActivity(intent);
                    } else {
                        //已认证，进入我的机构
                        intent = new Intent(UIUtils.getContext(),
                                MineOrganizationActivity.class);
                        startActivityForResult(intent, 0);
                    }
                }
                break;
            case R.id.rl_course_arrow://我的课程
                Intent intent_personMineCourse = new Intent(UIUtils.getContext(), PersonMineCourseActivity.class);
                UIUtils.startActivity(intent_personMineCourse);
                break;
            case R.id.rl_show_course://发布课程
                jumpReservation();
                break;
            case R.id.rl_mine_order:
                // 我的订单(全部订单)
                startActivityOrder(MineOrderActivity.class, MineOrderActivity.STATUS_ALL);
                break;
            case R.id.rl_mine_course_order://我的课程订单
                startActivityOrder(MineOrderCourseActivity.class, MineOrderCourseActivity.STATUS_ALL);
                break;
            /*case R.id.ll_will_pay_order:
                // 待支付
                startActivityOrder(MineOrderActivity.class, MineOrderActivity.STATUS_WAIT_PAY);
                break;
            case R.id.ll_using_order:
                // 已支付
                startActivityOrder(MineOrderActivity.class, MineOrderActivity.STATUS_USE);
                break;
            case R.id.ll_achived_order:
                // 已完成
                startActivityOrder(MineOrderActivity.class, MineOrderActivity.STATUS_FINISH);
                break;*/
            case R.id.rl_myinvoice_arrow:
                //发票
                startActivityPage(InvoiceOrderActivity.class, true);
                break;
            case R.id.rl_mywallet_arrow:
                //我的钱包
                startActivityPage(MyWalletActivity.class, true);
                break;
            case R.id.rl_balance_arrow://余额
                if (isLogin) {
                    //易支付初始化
                    EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
                        @Override
                        public void onSuccess() {
                            EjuPayManager.getInstance().switchBalance(getActivity());
                        }
                    });
                } else {
                    intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_bankcard_arrow://银行卡
                if (isLogin) {
                    //易支付初始化
                    EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
                        @Override
                        public void onSuccess() {
                            EjuPayManager.getInstance().switchCard(getActivity());
                        }
                    });
                } else {
                    intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_mycoupons_arrow:
                //优惠券
                startActivityPage(PersonalCenter_CouponListActivity.class, true);
                break;
            case R.id.rl_myfocuson_arrow:
                //我的关注
                if (isLogin) {
                  /*  intent = new Intent();
                    intent.putExtra(UIUtils.getString(R.string.redirect_open_url),
                            UrlUtils.SERVER_WEB_MYFAVORITE + "uid=" + uid);
                    intent.putExtra(
                            Common_Show_WebPage_Activity.Param_String_Title,
                            UIUtils.getString(R.string.label_myAttention));
                    intent.setClass(UIUtils.getContext(),
                            Common_Show_WebPage_Activity.class);
                    intent.putExtra(UIUtils.getString(R.string.get_page_name),
                            WebConstant.Attention_Page);*/
                    intent = new Intent(UIUtils.getContext(), MineWatchlistActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_mywifi_arrow:
                // 认证WIFI,如果电话号码为空，就传111，这里的url要写死，不能更改
                intent = new Intent(UIUtils.getContext(),
                        Common_Show_WebPage_Activity.class);
                intent.putExtra(UIUtils.getString(R.string.get_page_name),
                        WebConstant.WiFi_Page);
                startActivity(intent);
                break;
            case R.id.tv_check_stores://查看可用门店列表
                intent = new Intent(UIUtils.getContext(), AvailableWifiStoreActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_video_arrow:       //陪读
                intent = new Intent(UIUtils.getContext(), AccompanyReadActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_more_info:
                //更多
                startActivityPage(PersonalCenter_MoreActivity.class, false);
                break;
            default:
                break;
        }
    }

    /**
     * 跳转发布课程选择日期页面
     */
    private void jumpReservation() {
        Intent intent_CourseReservation = new Intent(UIUtils.getContext(), PublishActivity.class);
        UIUtils.startActivity(intent_CourseReservation);
    }

    /**
     * 跳转页面方法
     *
     * @param cls     类名
     * @param isCheck 是否登录
     */
    private void startActivityPage(Class<?> cls, boolean isCheck) {
        if (!isCheck) {
            intent = new Intent(UIUtils.getContext(), cls);
            startActivity(intent);
            return;
        }
        if (isLogin) {
            intent = new Intent(UIUtils.getContext(), cls);
            startActivity(intent);
        } else {
            intent = new Intent(UIUtils.getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 跳转订单方法
     *
     * @param cls    类名
     * @param status 状态码
     */
    private void startActivityOrder(Class<?> cls, String status) {

        if (isLogin) {
            intent = new Intent(UIUtils.getContext(), cls);
            intent.putExtra(MineOrderActivity.STATUS, status);
            intent.putExtra(MineOrderActivity.COUNT, count);
            startActivityForResult(intent, 0);
        } else {
            intent = new Intent(UIUtils.getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyFlag = true;
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            //当前用户（非机构管理员）退出机构 成功
            rl_mine_organization.setVisibility(View.GONE);
        }
    }
}
