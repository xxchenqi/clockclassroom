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
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.AvailableWifiStoreActivity;
import com.yiju.ClassClockRoom.act.LoginActivity;
import com.yiju.ClassClockRoom.act.Messages_Activity;
import com.yiju.ClassClockRoom.act.MineOrderActivity;
import com.yiju.ClassClockRoom.act.MineOrderCourseActivity;
import com.yiju.ClassClockRoom.act.MyWalletActivity;
import com.yiju.ClassClockRoom.act.PersonMineInteractionActivity;
import com.yiju.ClassClockRoom.act.PersonalCenter_InformationActivity;
import com.yiju.ClassClockRoom.act.PersonalCenter_MoreActivity;
import com.yiju.ClassClockRoom.act.accompany.AccompanyReadActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.MessageBoxNoRead;
import com.yiju.ClassClockRoom.bean.result.UserInfo;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.control.FailCodeControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.CircleImageView;

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
    //未读消息
    private TextView tv_message;
    //认证wifi
    private RelativeLayout rl_mywifi_arrow;
    //查看可用门店列表
    private TextView tv_check_stores;
    //发票
    private RelativeLayout rl_myinvoice_arrow;
    //余额
    private RelativeLayout rl_balance_arrow;
    //银行卡
    private RelativeLayout rl_bankcard_arrow;
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
    //是否绑定手机号
    private TextView txt_nickname_visa;
    //钱包
    private RelativeLayout rl_mywallet_arrow;
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
    private boolean destroyFlag;
    //缓存头像
    private String headUrl;

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
        rl_myinvoice_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_myinvoice_arrow);
        rl_balance_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_balance_arrow);
        rl_bankcard_arrow = (RelativeLayout) currentView
                .findViewById(R.id.rl_bankcard_arrow);
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
        tv_check_stores = (TextView) currentView
                .findViewById(R.id.tv_check_stores);
        iv_avatar = (CircleImageView) currentView.findViewById(R.id.iv_avatar);
        txt_nickname = (TextView) currentView.findViewById(R.id.txt_nickname);
        txt_nickname_visa = (TextView) currentView.findViewById(R.id.txt_nickname_visa);
        rl_mywallet_arrow = (RelativeLayout) currentView.findViewById(R.id.rl_mywallet_arrow);
        rl_my_interactive_arrow = (RelativeLayout) currentView.findViewById(R.id.rl_my_interactive_arrow);
    }


    @Override
    public void initListener() {
        super.initListener();
        rl_avatar.setOnClickListener(this);
        rl_message_arrow.setOnClickListener(this);
        rl_my_interactive_arrow.setOnClickListener(this);
        rl_myinvoice_arrow.setOnClickListener(this);
        rl_balance_arrow.setOnClickListener(this);
        rl_bankcard_arrow.setOnClickListener(this);
        rl_mywifi_arrow.setOnClickListener(this);
        rl_video_arrow.setOnClickListener(this);
        rl_more_info.setOnClickListener(this);
        rl_mine_order.setOnClickListener(this);
        rl_mine_course_order.setOnClickListener(this);
        rl_mywallet_arrow.setOnClickListener(this);
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
            //设置昵称
            if (StringUtils.isNotNullString(StringUtils.getNickName())) {
                txt_nickname.setText(StringUtils.getNickName());
            }
            headUrl = SharedPreferencesUtils.getString(UIUtils.getContext(), getResources()
                    .getString(R.string.shared_avatar), "");
            if (StringUtils.isNotNullString(headUrl)) {
                Glide.with(UIUtils.getContext())
                        .load(headUrl)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.personal_center_logo)
                        .error(R.drawable.personal_center_logo)
                        .into(iv_avatar);
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
        //课室待支付数量
        tv_classroom_order_count.setText(UIUtils.getString(R.string.label_checkAllOrder));
        tv_classroom_order_count.setTextColor(UIUtils.getColor(R.color.color_gay_99));
        //课程待支付数量
        tv_course_order_count.setText(UIUtils.getString(R.string.label_checkAllOrder));
        tv_course_order_count.setTextColor(UIUtils.getColor(R.color.color_gay_99));
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
        params.addBodyParameter("uid", StringUtils.getUid());
        params.addBodyParameter("url", UrlUtils.SERVER_USER_API);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());

        httpUtils.send(HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
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
                txt_nickname_visa.setText(UIUtils.getString(R.string.label_has_binding));
            }
            if (!destroyFlag) {
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
                }
                //设置头像
                if (StringUtils.isNullString(headUrl)) {
                    if (StringUtils.isNotNullString(data.getAvatar())) {
                        Glide.with(UIUtils.getContext())
                                .load(data.getAvatar())
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .placeholder(R.drawable.personal_center_logo)
                                .error(R.drawable.personal_center_logo)
                                .into(iv_avatar);
                    } else {
                        iv_avatar.setImageResource(R.drawable.personal_center_logo);
                    }
                }
                //存取到数据库所有信息(其实这步骤没用，可以直接获取)
                parseUserInfo(data, userInfo);
            }
        } else {
            FailCodeControl.checkCode(userInfo.getCode());
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
        params.addBodyParameter("url", UrlUtils.SERVER_API_COMMON);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
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
                    tv_message.setText(String.valueOf(count));
                }
            }else{
                FailCodeControl.checkCode(messageBoxNoread.getCode());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_avatar:
                //头像点击个人中心信息
                if (isLogin) {
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_040");
                } else {
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_039");
                }
                startActivityPage(PersonalCenter_InformationActivity.class, true);
                break;
            case R.id.rl_message_arrow://我的消息
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_041");
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
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_042");
                startActivityPage(PersonMineInteractionActivity.class, true);
                break;
            case R.id.rl_mine_order:
                // 我的订单(全部订单)
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_043");
                startActivityOrder(MineOrderActivity.class, MineOrderActivity.STATUS_ALL);
                break;
            case R.id.rl_mine_course_order://我的课程订单
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_044");
                startActivityOrder(MineOrderCourseActivity.class, MineOrderCourseActivity.STATUS_ALL);
                break;
            case R.id.rl_myinvoice_arrow:
                //发票
                break;
            case R.id.rl_mywallet_arrow:
                //我的钱包
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_045");
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
            case R.id.rl_mywifi_arrow:
                // 认证WIFI,如果电话号码为空，就传111，这里的url要写死，不能更改
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_046");
                intent = new Intent(UIUtils.getContext(),
                        Common_Show_WebPage_Activity.class);
                intent.putExtra(UIUtils.getString(R.string.get_page_name),
                        WebConstant.WiFi_Page);
                startActivity(intent);
                break;
            case R.id.tv_check_stores://查看可用门店列表
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_047");
                intent = new Intent(UIUtils.getContext(), AvailableWifiStoreActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_video_arrow:       //陪读
                intent = new Intent(UIUtils.getContext(), AccompanyReadActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_more_info:
                //更多
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_048");
                startActivityPage(PersonalCenter_MoreActivity.class, false);
                break;
            default:
                break;
        }
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
}
