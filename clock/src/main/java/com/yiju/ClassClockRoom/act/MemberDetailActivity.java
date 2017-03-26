package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.MemberDetailAdapter;
import com.yiju.ClassClockRoom.bean.CommonMsgResult;
import com.yiju.ClassClockRoom.bean.MemberDetailData;
import com.yiju.ClassClockRoom.bean.MienBean;
import com.yiju.ClassClockRoom.bean.PictureWrite;
import com.yiju.ClassClockRoom.bean.result.CodeKey;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.camera.CameraDialog;
import com.yiju.ClassClockRoom.control.camera.CameraImage;
import com.yiju.ClassClockRoom.control.camera.ResultCameraHandler;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;
import com.yiju.ClassClockRoom.widget.CircleImageView;
import com.yiju.ClassClockRoom.widget.FlowLayout;
import com.yiju.ClassClockRoom.widget.GridViewForScrollView;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.windows.SexPopUpWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:老师资料
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/16 17:16
 * ----------------------------------------
 */
public class MemberDetailActivity extends BaseActivity implements View.OnClickListener, ListItemClickHelp, MemberDetailAdapter.AddClickListener {

    public static final String ACTION_UID = "uid";
    public static final String ACTION_SHOW_TEACHER = "show_teacher";
    public static final String ACTION_ORG_AUTH = "org_auth";
    public static final String ACTION_MOBILE = "mobile";
    public static final String ACTION_TITLE = "title";
    public static final String ACTION_COURSE_FLAG = "course_flag";
    public static final String ACTION_BACK_FLAG = "back_flag";
    //回退
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //右上角按钮文案
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //状态块
    @ViewInject(R.id.rl_teacher_status)
    private RelativeLayout rl_teacher_status;
    //审核状态
    @ViewInject(R.id.tv_teacher_status)
    private TextView tv_teacher_status;
    //审核时间
    @ViewInject(R.id.tv_teacher_check_time)
    private TextView tv_teacher_check_time;
    //未通过原因
    @ViewInject(R.id.tv_teacher_un_verify_reason)
    private TextView tv_teacher_un_verify_reason;
    //提示
    @ViewInject(R.id.tv_member_tips)
    private TextView tv_member_tips;
    //老师风采
    @ViewInject(R.id.gv_teacher_elegant)
    private GridViewForScrollView gv_teacher_elegant;
    //头像
    @ViewInject(R.id.rl_member_head)
    private RelativeLayout rl_member_head;
    //性别
    @ViewInject(R.id.rl_member_gender)
    private RelativeLayout rl_member_gender;
    //简介
    @ViewInject(R.id.rl_member_teacher_brief)
    private RelativeLayout rl_member_teacher_brief;
    //简介内容
    @ViewInject(R.id.tv_teacher_brief)
    private TextView tv_teacher_brief;
    //标签
    @ViewInject(R.id.rl_member_tag)
    private RelativeLayout rl_member_tag;
    //名字内容
    @ViewInject(R.id.tv_member_name_content)
    private TextView tv_member_name_content;
    //手机内容
    @ViewInject(R.id.tv_member_mobile_content)
    private TextView tv_member_mobile_content;
    //是否公开
    @ViewInject(R.id.tv_member_is_public)
    private TextView tv_member_is_public;
    //头像图片
    @ViewInject(R.id.cv_member_head_info)
    private CircleImageView cv_member_head_info;
    //性别内容
    @ViewInject(R.id.tv_member_gender)
    private TextView tv_member_gender;
    //标签内容
    @ViewInject(R.id.fl_member_tag)
    private FlowLayout fl_member_tag;
    //是否公开
    @ViewInject(R.id.ll_member_is_public)
    private LinearLayout ll_member_is_public;
    //公开图片
    @ViewInject(R.id.iv_member_is_public)
    private ImageView iv_member_is_public;
    //名字箭头
    @ViewInject(R.id.iv_member_name_arrow)
    private ImageView iv_member_name_arrow;
    //头像箭头
    @ViewInject(R.id.iv_member_head_arrow)
    private ImageView iv_member_head_arrow;
    //性别箭头
    @ViewInject(R.id.iv_member_gender_arrow)
    private ImageView iv_member_gender_arrow;
    //简介箭头
    @ViewInject(R.id.iv_member_teacher_brief_arrow)
    private ImageView iv_member_teacher_brief_arrow;
    //标签箭头
    @ViewInject(R.id.iv_member_tag_arrow)
    private ImageView iv_member_tag_arrow;
    //移除机构按钮
    @ViewInject(R.id.btn_remove_organization)
    private Button btn_remove_organization;
    //姓名
    @ViewInject(R.id.rl_member_name)
    private RelativeLayout rl_member_name;
    //公开显示的view
    @ViewInject(R.id.v_public)
    private View v_public;
    //底部内容
    @ViewInject(R.id.tv_member_content)
    private TextView tv_member_content;
    //照相image
    private CameraImage cameraImage;
    //老师资料适配器
    private MemberDetailAdapter adapter;
    //数据源
    private List<MienBean> datas;
    //被修改人的id,传进来的id
    private String teacher_uid;
    //是否是管理员(传进来的)
    private String org_auth;// 2 为管理员
    //是否是管理员(用户本身的)
    private String user_org_auth;
    //手机
    private String mobile;
    //老师资料bean
    private MemberDetailResult bean;
    //屏幕宽度
    private int mScreenWidth;
    //屏幕高度
    private int mScreenHeight;
    //上传头像的key
    private String file_category;
    //上传头像的key
    private String key;
    //上传头像的key
    private String permit_code;
    //区分风采和头像的flag,头像:false,风采:true
    private boolean flag = false;
    //是否公开信息
    private String show_teacher;
    //标题
    private String title;
    //是否订过课室
    private String is_pay;
    //是否能编辑flag
    private boolean edit_flag;
    //机构id
    private String org_id;
    //是否认证
    private String is_auth;
    //是否是从我的课程过来的
    private boolean course_flag;
    //是否去刷新机构界面的成员列表
    private boolean back_flag = false;


    public static int RESULT_CODE_FROM_MEMBER_DETAIL_ACT = 1005;
    public static int RESULT_CODE_FROM_MEMBER_BACK_ACT = 1006;
    private String from;

    private boolean isEditMustInfo;

    @Override
    public void initIntent() {
        super.initIntent();
        teacher_uid = getIntent().getStringExtra(ACTION_UID);
        from = getIntent().getStringExtra("from");
        show_teacher = getIntent().getStringExtra(ACTION_SHOW_TEACHER);
        org_auth = getIntent().getStringExtra(ACTION_ORG_AUTH);
        title = getIntent().getStringExtra(ACTION_TITLE);
        mobile = getIntent().getStringExtra(ACTION_MOBILE);
        course_flag = getIntent().getBooleanExtra(ACTION_COURSE_FLAG, false);
    }

    @Override
    protected void initView() {
        user_org_auth = SharedPreferencesUtils.getString(
                UIUtils.getContext(),
                getResources().getString(R.string.shared_org_auth), "");
        is_pay = SharedPreferencesUtils.getString(
                UIUtils.getContext(),
                getResources().getString(R.string.shared_is_pay), "");
        org_id = SharedPreferencesUtils.getString(
                UIUtils.getContext(),
                getResources().getString(R.string.shared_org_id), "");
        is_auth = SharedPreferencesUtils.getString(
                UIUtils.getContext(),
                getResources().getString(R.string.shared_is_auth), "");

        cameraImage = new CameraImage(MemberDetailActivity.this);
        datas = new ArrayList<>();

        mScreenWidth = CommonUtil.getScreenWidth(this);
        mScreenHeight = CommonUtil.getScreenWidth(this);
    }

    @Override
    protected void initData() {
        head_title.setText(title);

        if (UIUtils.getString(R.string.teacher_detail).equals(title)) {
            //教室详情，移除机构按钮隐藏
            btn_remove_organization.setVisibility(View.GONE);
        } else {
            //老师资料
            //是否为管理员
            if ("2".equals(user_org_auth)) {
                //移除机构按钮显示
                btn_remove_organization.setVisibility(View.VISIBLE);
                //如果是管理员看管理员,隐藏
                if (StringUtils.getUid().equals(teacher_uid)) {
                    //移除机构按钮隐藏
                    btn_remove_organization.setVisibility(View.GONE);
                }
            } else {
                //不是管理员，移除机构按钮隐藏
                btn_remove_organization.setVisibility(View.GONE);
            }
        }

        //show_teacher 1公开 0不公开
        if ("1".equals(show_teacher)) {
            // 公开的话隐藏公开布局
            if (!"2".equals(org_auth)) {
                //如果点击进来的不是管理员，隐藏公开布局
                ll_member_is_public.setVisibility(View.GONE);
                v_public.setVisibility(View.VISIBLE);
            }
        }
        //点击进来的成员是否是管理员
        if ("2".equals(org_auth)) {
            tv_member_is_public.setText(UIUtils.getString(R.string.member_is_admin));
            iv_member_is_public.setImageResource(R.drawable.icon_administrators_list);
        } else {
            tv_member_is_public.setText(UIUtils.getString(R.string.member_no_public));
            iv_member_is_public.setImageResource(R.drawable.icon_closeeye);
        }

        //是否需要显示底部的内容
        if (StringUtils.isNotNullString(org_id)) {
            if ("0".equals(org_id)) {
                if ("2".equals(is_auth)) {
//                        UIUtils.showToastSafe("认证失败");
                    tv_member_content.setVisibility(View.VISIBLE);
                } else {
//                        UIUtils.showToastSafe("未认证");
                    tv_member_content.setVisibility(View.GONE);
                }
            } else if (Integer.valueOf(org_id) > 0) {
                if ("0".equals(is_auth)) {
//                        UIUtils.showToastSafe("认证中");
                    tv_member_content.setVisibility(View.VISIBLE);
                } else {
//                        UIUtils.showToastSafe("已认证");
                    tv_member_content.setVisibility(View.VISIBLE);
                }
            }
        }

        getHttpUtils();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    /**
     * 获取老师信息
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_teacher_info");
        params.addBodyParameter("teacher_uid", teacher_uid);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                });
    }

    private void processData(String result) {
        head_back_relative.setOnClickListener(this);
        rl_member_name.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        rl_member_gender.setOnClickListener(this);
        rl_member_teacher_brief.setOnClickListener(this);
        rl_member_tag.setOnClickListener(this);
        btn_remove_organization.setOnClickListener(this);
        rl_member_head.setOnClickListener(this);

        bean = GsonTools.changeGsonToBean(result,
                MemberDetailResult.class);
        if (bean == null) {
            return;
        }

        if ("1".equals(bean.getCode())) {
            org_auth = bean.getOrg_auth();//当前老师是否是管理员
            is_pay = bean.getIs_pay();//标记是否完整下过订单
            MemberDetailData data = bean.getData();
            if (data == null) {
                return;
            }
            mobile = bean.getMobile();
            //不同审核状态下初始化各种控件
            initTeacherStatus(bean, data);
            //显示隐藏各控件
            initViewVisible(data);
            //当前用户是管理员 或者 当前用户公开信息 或者 是从个人老师资料进来的 或者 当前用户点击的是自己，
            //都显示所有信息
            if ("2".equals(user_org_auth) || "1".equals(show_teacher) ||
                    UIUtils.getString(R.string.teacher_detail).equals(title)
                    || StringUtils.getUid().equals(teacher_uid)) {
                //设置名字
                tv_member_name_content.setText(data.getReal_name());
                //设置手机
                tv_member_mobile_content.setText(mobile);
                //设置头像
                if (StringUtils.isNotNullString(bean.getAvatar())) {
                    Glide.with(UIUtils.getContext()).load(bean.getAvatar()).into(cv_member_head_info);
                }
                //设置性别
                int sex = Integer.valueOf(data.getSex());
                switch (sex) {
                    case 0:
                        if ("2".equals(user_org_auth)) {
                            tv_member_gender.setText("");
                        }
                        break;
                    case 1:
                        tv_member_gender.setText(UIUtils.getString(R.string.label_male));
                        break;
                    case 2:
                        tv_member_gender.setText(UIUtils.getString(R.string.label_female));
                        break;
                }
                //设置简介
                if (StringUtils.isNotNullString(data.getInfo())) {
                    tv_teacher_brief.setVisibility(View.VISIBLE);
                    tv_teacher_brief.setText(data.getInfo());
                } else {
                    tv_teacher_brief.setVisibility(View.GONE);
                }
                //设置标签
                if (StringUtils.isNotNullString(data.getTags())) {
                    fl_member_tag.setVisibility(View.VISIBLE);
                    fl_member_tag.removeAllViews();
                    fl_member_tag.setHorizontalSpacing(UIUtils.dip2px(5));
                    fl_member_tag.setVerticalSpacing(UIUtils.dip2px(2));
                    String[] tags = data.getTags().split(",");
                    for (String tag : tags) {
                        TextView view = new TextView(this);
                        view.setText(tag);
                        view.setTextSize(10);
                        view.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tv_flowlayout_green));

                        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        view.measure(width, height);
                        fl_member_tag.addView(view);
                    }
                } else {
                    fl_member_tag.removeAllViews();
                    fl_member_tag.setVisibility(View.GONE);
                }

                //设置老师风采
                setTeacherMine(data);
            } else {
                //不公开,隐藏信息,只显示名字和手机的部分内容
                //设置名字
                String real_name = bean.getData().getReal_name();
                if (real_name.length() > 0) {
                    //显示剩余*的数量=org_dname.length()-1
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < real_name.length() - 1; i++) {
                        sb.append("*");
                    }
                    tv_member_name_content.setText(real_name.substring(0, 1) + sb.toString());
                }
                //设置手机
                if (mobile != null && mobile.length() != 0) {
                    String m = mobile.substring(0, 2) + "*****" + mobile.substring(7, 11);
                    tv_member_mobile_content.setText(m);
                }
            }
        } else {
            //尚未有该用户信息
            if ("11018".equals(bean.getCode())) {
                bean.getData().setIs_verify("-1");
                //当前用户是管理员
                if ("2".equals(user_org_auth)) {
                    //不同审核状态下初始化各种控件
                    initTeacherStatus(bean, bean.getData());
                    //显示隐藏各控件
                    initViewVisible(bean.getData());
                    //设置名字
                    rl_member_name.setClickable(true);

                    //设置手机
                    tv_member_mobile_content.setText(mobile);
                    //设置头像
                    if (StringUtils.isNotNullString(bean.getAvatar())) {
                        Glide.with(UIUtils.getContext()).load(bean.getAvatar()).into(cv_member_head_info);
                    }
                    //增加老师风采
                    if (!course_flag) {
                        setTeacherMine(bean.getData());
                    }
                } else {
                    //非管理员用户看自己
                    if (StringUtils.getUid().equals(teacher_uid)) {
                        initHeadViewVisibleClick();
                        if ("1".equals(is_pay)) {//是否订过课室
                            initViewVisibleClick();
                            head_right_text.setText(R.string.txt_submit_audit);
                            head_right_relative.setVisibility(View.VISIBLE);
                            head_right_relative.setClickable(true);
                        } else {
                            initViewInVisibleNoClick();
                            head_right_relative.setVisibility(View.INVISIBLE);
                            head_right_relative.setClickable(false);
                        }
                    } else {
                        //用户看别人,全部不能点击
                        initHeadViewInVisibleNoClick();
                        initViewInVisibleNoClick();
                        head_right_relative.setVisibility(View.INVISIBLE);
                        head_right_relative.setClickable(false);
                    }
                    if ("1".equals(show_teacher)) {
                        if (bean.getData() != null
                                && StringUtils.isNotNullString(bean.getData().getReal_name())) {
                            //设置名字
                            tv_member_name_content.setText(bean.getData().getReal_name());
                        }
                        //设置手机
                        tv_member_mobile_content.setText(mobile);
                        //设置头像
                        if (StringUtils.isNotNullString(bean.getAvatar())) {
                            Glide.with(UIUtils.getContext()).load(bean.getAvatar()).into(cv_member_head_info);
                        }
                    } else {
                        //不公开,隐藏信息,只显示名字和手机的部分内容
                        //设置名字
                        if (bean.getData() != null && StringUtils.isNotNullString(bean.getData().getReal_name())) {
                            String real_name = bean.getData().getReal_name();
                            if (real_name.length() > 0) {
                                //显示剩余*的数量=org_dname.length()-1
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < real_name.length() - 1; i++) {
                                    sb.append("*");
                                }
                                tv_member_name_content.setText(real_name.substring(0, 1) + sb.toString());
                            }
                        }
                        //设置手机
                        if (mobile != null && mobile.length() != 0) {
                            String m = mobile.substring(0, 2) + "*****" + mobile.substring(7, 11);
                            tv_member_mobile_content.setText(m);
                        }
                    }
                    setTeacherMine(bean.getData());
                }

            } else {
                UIUtils.showToastSafe(bean.getMsg());
            }
        }
    }

    /**
     * 设置老师风采
     *
     * @param data 老师信息
     */
    private void setTeacherMine(MemberDetailData data) {
        if (datas == null) {
            datas = new ArrayList<>();
        } else {
            datas.clear();
        }
        if (data != null) {
            List<MienBean> mien = data.getMien();
            if (mien != null && mien.size() > 0) {
                datas.addAll(mien);
            }
        }
        if (edit_flag) {
            //当前用户为管理员是可编辑的
            datas.add(new MienBean());
        }
        adapter = new MemberDetailAdapter(this, datas, R.layout.item_member_detail, datas.size(),
                mScreenWidth, mScreenHeight, this, user_org_auth, edit_flag);
        gv_teacher_elegant.setAdapter(adapter);
    }


    //审核状态各种控件初始化
    private void initTeacherStatus(MemberDetailResult bean, MemberDetailData data) {
        String is_verify = data.getIs_verify();
        if ("-1".equals(is_verify)) {//新建
            if ("2".equals(user_org_auth) || "1".equals(is_pay)) {//是否是机构管理员 是否订过课室)
                head_right_relative.setVisibility(View.VISIBLE);
                head_right_relative.setClickable(true);
            } else {
                head_right_relative.setVisibility(View.INVISIBLE);
                head_right_relative.setClickable(false);
            }
            if (bean.getFullteacherinfo() == 1) {//资料齐全 才可提交审核
                head_right_relative.setEnabled(true);
            } else {
                head_right_relative.setEnabled(false);
                head_right_text.setTextColor(UIUtils.getColor(R.color.gray));
            }

            head_right_text.setText(R.string.txt_submit_audit);
            rl_teacher_status.setVisibility(View.GONE);
            tv_teacher_un_verify_reason.setVisibility(View.GONE);
        } else if ("0".equals(is_verify)) {//审核未通过
            if (isEditMustInfo) {//编辑过资料
                head_right_relative.setVisibility(View.VISIBLE);
                head_right_relative.setClickable(true);
                head_right_text.setText(R.string.txt_submit_audit);
            } else {
                head_right_relative.setVisibility(View.INVISIBLE);
                head_right_relative.setClickable(false);
            }
            rl_teacher_status.setVisibility(View.VISIBLE);
            rl_teacher_status.setBackgroundColor(UIUtils.getColor(R.color.color_red_f2));
            tv_teacher_status.setText(R.string.person_course_status_fail_check);
            tv_teacher_un_verify_reason.setVisibility(View.VISIBLE);
            String str = getString(R.string.txt_not_by_reason) + data.getUnverify_reason();
            int start = getString(R.string.txt_not_by_reason).length();
            SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(str);
            mSpannableStringBuilder.setSpan
                    (new ForegroundColorSpan(UIUtils.getColor(R.color.color_black_33)),
                            0, start, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mSpannableStringBuilder.setSpan
                    (new ForegroundColorSpan(UIUtils.getColor(R.color.color_red_ff)),
                            start, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv_teacher_un_verify_reason.setText(mSpannableStringBuilder);
        } else if ("1".equals(is_verify)) {//待(未)审核
            head_right_relative.setVisibility(View.INVISIBLE);
            head_right_relative.setClickable(false);
            rl_teacher_status.setVisibility(View.VISIBLE);
            rl_teacher_status.setBackgroundColor(UIUtils.getColor(R.color.color_gay_99));
            tv_teacher_status.setText(R.string.person_course_status_wait_check);
            tv_teacher_un_verify_reason.setVisibility(View.GONE);
        } else if ("2".equals(is_verify)) {//审核通过
            if ("2".equals(user_org_auth) || "1".equals(is_pay)) {//是否订过课室)
                head_right_relative.setVisibility(View.VISIBLE);
                head_right_relative.setClickable(true);
            } else {
                head_right_relative.setVisibility(View.INVISIBLE);
                head_right_relative.setClickable(false);
            }
            tv_member_tips.setVisibility(View.GONE);
            head_right_text.setText(R.string.edit);
            rl_teacher_status.setVisibility(View.VISIBLE);
            rl_teacher_status.setBackgroundColor(UIUtils.getColor(R.color.app_theme_color));
            tv_teacher_status.setText(R.string.txt_approve);
            tv_teacher_un_verify_reason.setVisibility(View.GONE);
        }
        tv_teacher_check_time.setText(DateUtil.second2Date(data.getVerify_time()));//审核时间
    }

    //判断各控件是否显示隐藏
    private void initViewVisible(MemberDetailData data) {
        String is_verify = data.getIs_verify();
        //请求完成后进行监听，防止崩溃
        if (!course_flag) {
            if ("2".equals(user_org_auth)) {
                //用户是管理员，可编辑
                initHeadViewVisibleClick();
                if ("1".equals(is_verify) || "2".equals(is_verify)) {//待审核 或 审核通过
                    initViewInVisibleNoClick();
                } else {
                    initViewVisibleClick();
                    btn_remove_organization.setClickable(true);
                }
            } else {
                if (UIUtils.getString(R.string.member_detail).equals(title)
                        || !UIUtils.getString(R.string.teacher_detail).equals(title)) {
                    //如果是老师资料，自己点自己可以编辑
                    if (StringUtils.getUid().equals(teacher_uid)) {
                        initHeadViewVisibleClick();
                        if ("1".equals(is_pay)) {//是否订过课室
                            if ("1".equals(is_verify) || "2".equals(is_verify)) {//待审核 或 审核通过
                                initViewInVisibleNoClick();
                            } else {
                                initViewVisibleClick();
                                btn_remove_organization.setClickable(true);
                            }
                        } else {
                            initViewInVisibleNoClick();
                            head_right_relative.setVisibility(View.INVISIBLE);
                            head_right_relative.setClickable(false);
                        }
                    } else {
                        //用户看别人,全部不能点击
                        initHeadViewInVisibleNoClick();
                        initViewInVisibleNoClick();
                        head_right_relative.setVisibility(View.INVISIBLE);
                        head_right_relative.setClickable(false);
                    }
                } else {
                    //老师详情并且订过课室,订过的都能编辑，没订过的只能编辑头像
                    initHeadViewVisibleClick();
                    if ("1".equals(is_pay)) {
                        if ("1".equals(is_verify) || "2".equals(is_verify)) {//待审核 或 审核通过
                            initViewInVisibleNoClick();
                        } else {
                            initViewVisibleClick();
                            btn_remove_organization.setClickable(true);
                        }
                    } else {
                        initViewInVisibleNoClick();
                    }
                }
            }

        } else {
            btn_remove_organization.setVisibility(View.GONE);
            initHeadViewInVisibleNoClick();
            initViewInVisibleNoClick();
        }
    }

    /**
     * 设置头像可点击，并显示头像向右箭头
     */
    private void initHeadViewVisibleClick() {
        rl_member_head.setClickable(true);
        iv_member_head_arrow.setVisibility(View.VISIBLE);
    }

    /**
     * 设置头像不可点击，并隐藏头像向右箭头
     */
    private void initHeadViewInVisibleNoClick() {
        rl_member_head.setClickable(false);
        iv_member_head_arrow.setVisibility(View.INVISIBLE);
    }

    //隐藏向右箭头并取消点击事件
    private void initViewInVisibleNoClick() {
        iv_member_name_arrow.setVisibility(View.INVISIBLE);
        iv_member_gender_arrow.setVisibility(View.INVISIBLE);
        iv_member_teacher_brief_arrow.setVisibility(View.INVISIBLE);
        iv_member_tag_arrow.setVisibility(View.INVISIBLE);
        rl_member_name.setClickable(false);
        rl_member_gender.setClickable(false);
        rl_member_teacher_brief.setClickable(false);
        rl_member_tag.setClickable(false);
        edit_flag = false;
    }

    //显示向右箭头并设置为可点击状态
    private void initViewVisibleClick() {
        iv_member_name_arrow.setVisibility(View.VISIBLE);
        iv_member_gender_arrow.setVisibility(View.VISIBLE);
        iv_member_teacher_brief_arrow.setVisibility(View.VISIBLE);
        iv_member_tag_arrow.setVisibility(View.VISIBLE);
        rl_member_name.setClickable(true);
        rl_member_gender.setClickable(true);
        rl_member_teacher_brief.setClickable(true);
        rl_member_tag.setClickable(true);
        edit_flag = true;
    }

    @Override
    public String getPageName() {
        if (UIUtils.getString(R.string.teacher_detail).equals(title)) {
            return getString(R.string.title_act_my_teacher_detail);
        } else {//if (UIUtils.getString(R.string.member_detail).equals(title))
            return getString(R.string.title_act_teacher_detail);
        }
//        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_member_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://回退
                onBackPressed();
                break;
            case R.id.head_right_relative://提交审核/编辑
                if (UIUtils.getString(R.string.txt_submit_audit).equals(head_right_text.getText().toString())) {
                    getHttpUtilsForTeacherSubmitVerify("", false);
                } else {//编辑
                    CustomDialog dialog = new CustomDialog(
                            MemberDetailActivity.this,
                            UIUtils.getString(R.string.confirm),
                            UIUtils.getString(R.string.label_cancel),
                            getString(R.string.dialog_message_edit_teacher)
                    );
                    dialog.setOnClickListener(new IOnClickListener() {
                        @Override
                        public void oncClick(boolean isOk) {
                            if (isOk) {
                                initHeadViewVisibleClick();
                                initViewVisibleClick();
                                setTeacherMine(bean.getData());
                                getHttpUtilsForTeacherSubmitVerify("-1", true);
                            }
                        }
                    });
                }
                break;
            case R.id.rl_member_head://头像
                //检测是否打开读写权限
                if (!PermissionsChecker.checkPermission(
                        PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS)) {
                    //调用相机
                    flag = false;
                    new CameraDialog(this, cameraImage).creatView();
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS
                    );
                }
                break;
            case R.id.rl_member_gender://性别
                SexPopUpWindow sex_popup = new SexPopUpWindow(MemberDetailActivity.this, teacher_uid, false, bean, title);
                sex_popup.showAtLocation(rl_member_gender, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.rl_member_teacher_brief://修改老师简介
                Intent intent = new Intent(this, OrganizationModifyNameActivity.class);
                intent.putExtra(OrganizationModifyNameActivity.ACTION_BEAN, bean);
                intent.putExtra(OrganizationModifyNameActivity.ACTION_UID, teacher_uid);
                intent.putExtra(OrganizationModifyNameActivity.ACTION_TITLE,
                        UIUtils.getString(R.string.teacher_brief));
                intent.putExtra(OrganizationModifyNameActivity.ACTION_TITLE_FLAG, title);
                intent.putExtra(OrganizationModifyNameActivity.ACTION_CONTENT, bean.getData().getInfo());
                startActivityForResult(intent, 0);
                break;
            case R.id.rl_member_tag:  //修改老师标签
                Intent intent_tag = new Intent(this, ModifyTagActivity.class);
                intent_tag.putExtra(ModifyTagActivity.ACTION_BEAN, bean);
                //类型 1=机构 2=老师 默认1
                intent_tag.putExtra(ModifyTagActivity.ACTION_TYPE, ModifyTagActivity.TYPE_TWO);
                intent_tag.putExtra(ModifyTagActivity.ACTION_UID, teacher_uid);
                intent_tag.putExtra(ModifyTagActivity.ACTION_TITLE_FLAG, title);
                startActivityForResult(intent_tag, 0);
                break;
            case R.id.btn_remove_organization:   //移除机构
                CustomDialog customDialog = new CustomDialog(
                        MemberDetailActivity.this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        UIUtils.getString(R.string.dialog_msg_delete_from_organization));
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            getHttpUtilsForRemoveFromOrganization();
                        }
                    }
                });
                break;
            case R.id.rl_member_name://修改昵称
                if (bean == null) {
                    return;
                }
                Intent intent_name = new Intent(this, OrganizationModifyNameActivity.class);
                intent_name.putExtra(OrganizationModifyNameActivity.ACTION_BEAN, bean);
                intent_name.putExtra(OrganizationModifyNameActivity.ACTION_TITLE,
                        UIUtils.getString(R.string.modify_name));
                intent_name.putExtra(OrganizationModifyNameActivity.ACTION_UID, teacher_uid);
                intent_name.putExtra(OrganizationModifyNameActivity.ACTION_TITLE_FLAG, title);
                intent_name.putExtra(OrganizationModifyNameActivity.ACTION_CONTENT, bean.getData().getReal_name());
                startActivityForResult(intent_name, 0);
                break;
        }
    }

    //提交审核接口请求
    private void getHttpUtilsForTeacherSubmitVerify(final String status, final boolean isEdit) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "teacher_submit_verify");
        params.addBodyParameter("teacher_uid", teacher_uid);
        if (StringUtils.isNotNullString(status)) {
            params.addBodyParameter("status", "-1");
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForTeacherSubmitVerify(arg0.result, status, isEdit);

                    }
                });
    }

    //提交审核结果处理
    private void processDataForTeacherSubmitVerify(String result, String status, boolean isEdit) {
        CommonMsgResult msgResult = GsonTools.changeGsonToBean(result, CommonMsgResult.class);
        if (msgResult == null) {
            return;
        }

        if ("1".equals(msgResult.getCode())) {
            //刷界面
            if (StringUtils.isNullString(status) || isEdit) {
                getHttpUtils();
            }
        }
        if (StringUtils.isNullString(status)) {
            back_flag = true;
            UIUtils.showToastSafe(msgResult.getMsg());
        }
    }


    @Override
    public void onBackPressed() {
        if (OrganizationTeacherListActivity.FROM_TAG.equals(from) && back_flag) {
            //回退到OrganizationTeacherListActivity-转让管理权界面
            setResult(1);
        } else {
            Intent intent_back = new Intent();
            intent_back.putExtra(ACTION_BACK_FLAG, back_flag);
            setResult(RESULT_CODE_FROM_MEMBER_BACK_ACT, intent_back);
        }
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 设置头像返回
        if (requestCode == CameraImage.CAMERA_WITH_DATA
                || requestCode == CameraImage.PHOTO_REQUEST_CUT
                || requestCode == CameraImage.PHOTO_REQUEST_GALLERY) {
            ResultCameraHandler
                    .getInstance()
                    .setCrop(true)
                    .getPhotoFile(this,
                            data, requestCode, resultCode, cameraImage,
                            new ResultCameraHandler.CameraResult() {
                                @Override
                                public void result(File imageUri) {
                                    uploadingHeadImage(imageUri);
                                }
                            });
        } else if (resultCode == OrganizationModifyNameActivity.RESULT_CODE_FROM_ORGANIZATION_MODIFY_BRIEF_ACT
                || resultCode == OrganizationModifyNameActivity.RESULT_CODE_FROM_ORGANIZATION_MODIFY_NAME_ACT) {
            //简介
            String brief_edit = data.getStringExtra("brief_edit");
            if (StringUtils.isNotNullString(brief_edit)) {
                tv_teacher_brief.setText(brief_edit);
                if (bean != null && bean.getData() != null) {
                    bean.getData().setInfo(brief_edit);
                }
            }
            //姓名
            String name_edit = data.getStringExtra("name_edit");
            if (StringUtils.isNotNullString(name_edit)) {
                tv_member_name_content.setText(name_edit);
                if (bean != null && bean.getData() != null) {
                    bean.getData().setReal_name(name_edit);
                }
            }
            back_flag = true;
            isEditMustInfo = true;
            getHttpUtilsForTeacherSubmitVerify("-1", false);
            head_right_relative.setVisibility(View.VISIBLE);
            head_right_relative.setClickable(true);
            head_right_text.setText(R.string.txt_submit_audit);
        } else if (requestCode == OrganizationModifyNameActivity.RESULT_CODE_FROM_ORGANIZATION_MODIFY_OTHER_ACT
                || resultCode == ModifyTagActivity.RESULT_CODE_FROM_MODIFY_TAG_TEACHER_ACT
                || resultCode == OrganizationMienEditActivity.RESULT_CODE_FROM_ORGANIZATION_MIEN_EDIT_DELETE_ACT) {
            //修改其他信息、标签、风采
            back_flag = true;
            if (resultCode == OrganizationMienEditActivity.RESULT_CODE_FROM_ORGANIZATION_MIEN_EDIT_DELETE_ACT) {
                isEditMustInfo = true;
                getHttpUtilsForTeacherSubmitVerify("-1", false);
            } else {
                getHttpUtils();
            }
        }
    }

    /**
     * 上传
     *
     * @param imageUri 图片文件流
     */
    private void uploadingHeadImage(File imageUri) {
        // 3次请求,获取授权码及key,请求内部写接口和上传头像
        getHttpUtils2(imageUri);
    }

    /**
     * 获取key请求
     *
     * @param imageUri 图片文件流
     */
    private void getHttpUtils2(final File imageUri) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getcodekey");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData2(arg0.result, imageUri);

                    }
                });

    }

    /**
     * 解析 key
     *
     * @param result   s
     * @param imageUri f
     */
    private void processData2(String result, File imageUri) {
        CodeKey codeKey = GsonTools.changeGsonToBean(result, CodeKey.class);
        if (codeKey == null) {
            return;
        }
        if (codeKey.getCode().equals("1")) {
            CodeKey.Data data = codeKey.getData();
            file_category = data.getFile_category();
            key = data.getKey();
            permit_code = data.getPermit_code();
            // 请求上传头像
            getHttpUtils3(imageUri);

        } else {
            UIUtils.showToastSafe(getString(R.string.toast_request_fail));
        }
    }

    /**
     * 上传头像
     *
     * @param imageUri f
     */
    private void getHttpUtils3(File imageUri) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("permit_code", permit_code);
        params.addBodyParameter("file_category", file_category);
        params.addBodyParameter("key", key);
        params.addBodyParameter("pfile", imageUri);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.PIC_WIRTE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData3(arg0.result);

                    }
                });

    }

    private void processData3(String result) {
        PictureWrite pictureWrite = GsonTools.changeGsonToBean(result,
                PictureWrite.class);
        if (pictureWrite == null) {
            return;
        }

        if (pictureWrite.isFlag()) {
            StringBuilder sb = new StringBuilder(pictureWrite.getResult()
                    .getPic_id());
            String headUrl = "http://get.file.dc.cric.com/"
                    + sb.insert(sb.length() - 4, "_350X350_0_0_0").toString();
            if (!flag) {
                bean.setAvatar(headUrl);
            } else {
                List<MienBean> mien = bean.getData().getMien();
                MienBean mienBean = new MienBean();
                mienBean.setPic(headUrl);
                mien.add(mienBean);
            }
            HttpClassRoomApi.getInstance().askModifyMemberInfo(title, teacher_uid, bean, true, true);
        } else {
            UIUtils.showToastSafe(getString(R.string.toast_request_fail));
        }
    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        if (event.getType() == DataManager.MODIFY_MEMBER_DATA_INFO) {
            MemberDetailResult bean = (MemberDetailResult) event.getData();
            if ("1".equals(bean.getCode())) {//更改风采
                UIUtils.showToastSafe(getString(R.string.toast_edit_success));
                isEditMustInfo = true;
                back_flag = true;
                getHttpUtils();
            } else {
                UIUtils.showToastSafe(bean.getMsg());
            }
        }
    }

    /**
     * 性别点击
     *
     * @param position 位置
     */
    @Override
    public void onClickItem(int position) {
        switch (position) {
            case R.id.btn_male:
                tv_member_gender.setText(R.string.label_male);
                break;
            case R.id.btn_female:
                tv_member_gender.setText(R.string.label_female);
                break;
            default:
                break;
        }
    }

    /**
     * 添加风采的回调
     */
    @Override
    public void addClick() {
        //检测是否打开读写权限
        if (!PermissionsChecker.checkPermission(
                PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS)) {
            //调用相机
            flag = true;
            new CameraDialog(this, cameraImage).creatView();
        } else {
            PermissionsChecker.requestPermissions(
                    this,
                    PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS
            );
        }
    }

    //把当前成员移除机构请求
    private void getHttpUtilsForRemoveFromOrganization() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "exit_organization");
        params.addBodyParameter("org_auth", "1");//是否机构移出用户 0=不是 1=是
        params.addBodyParameter("uid", teacher_uid);//退出人的ID
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("org_uid", StringUtils.getUid());//机构移出用户人的ID
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataRemoveFromOrganization(arg0.result);
                    }
                });
    }

    private void processDataRemoveFromOrganization(String result) {
        CommonResultBean resultData = GsonTools.fromJson(result, CommonResultBean.class);
        if (!TextUtils.isEmpty(resultData.getCode()) && "1".equals(resultData.getCode())) {
            this.setResult(RESULT_CODE_FROM_MEMBER_DETAIL_ACT);
            finish();
        }
        UIUtils.showToastSafe(resultData.getMsg());
    }

    /**
     * 机构风采图片点击
     *
     * @param pos 点击的位置
     */
    @Override
    public void checkClick(int pos) {
        Intent intent = new Intent(this, OrganizationMienEditActivity.class);
        intent.putExtra("title", getString(R.string.teacher_mien));
        intent.putExtra("pos", pos);
        intent.putExtra("teacher_bean", bean);
        intent.putExtra("org_uid", teacher_uid);
        if (UIUtils.getString(R.string.teacher_detail).equals(title)) {
            intent.putExtra("flag", UIUtils.getString(R.string.teacher_detail));
        } else {
            intent.putExtra("flag", UIUtils.getString(R.string.member_detail));
        }
        intent.putExtra("edit_flag", edit_flag);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
//        } else {
            UIUtils.showToastSafe(getString(R.string.toast_permission_read_write_sdcard));
        }
    }
}
