package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.adapter.CourseDetailMoreAdapter;
import com.yiju.ClassClockRoom.adapter.CourseDetailPagerAdapter;
import com.yiju.ClassClockRoom.bean.CourseDetailData;
import com.yiju.ClassClockRoom.bean.CourseTimeList;
import com.yiju.ClassClockRoom.bean.result.CourseApplyResult;
import com.yiju.ClassClockRoom.bean.result.CourseDetail;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.map.NavigationUtils;
import com.yiju.ClassClockRoom.control.share.ShareDialog;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpCommonApi;
import com.yiju.ClassClockRoom.util.net.api.HttpCourseApi;
import com.yiju.ClassClockRoom.widget.MyListView;
import com.yiju.ClassClockRoom.widget.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程详情页
 * Created by Sandy on 2016/6/14/0014.
 */
public class CourseDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.rl_course_detail_all)//顶部标题栏
    private RelativeLayout rlCourseDetailAll;

    @ViewInject(R.id.iv_course_detail_back)//返回
    private ImageView ivCourseDetailBack;

    @ViewInject(R.id.tv_course_detail_title)//标题
    private TextView tvCourseDetailTitle;

    @ViewInject(R.id.iv_course_detail_share)//分享
    private ImageView ivCourseDetailShare;

    @ViewInject(R.id.iv_course_detail_attention)
    private ImageView ivCourseDetailAttention;//关注

    @ViewInject(R.id.sl_course_detail)
    private ObservableScrollView slCourseDetail;

    @ViewInject(R.id.vp_course_detail)//顶部轮播图
    private ViewPager vpCourseDetail;

    @ViewInject(R.id.ll_course_point_group)
    private LinearLayout llCoursePointGroup;

    @ViewInject(R.id.tv_course_detail_price)//课程价格
    private TextView tvCourseDetailPrice;

    @ViewInject(R.id.tv_course_detail_first_time)//报名状态(示例:2015.06.21 18:00)
    private TextView tvCourseDetailFirstTime;

    @ViewInject(R.id.tv_course_detail_content)//课程标题(示例:4-5岁儿童体验式能力评估)
    private TextView tvCourseDetailContent;

    @ViewInject(R.id.tv_course_detail_all_class_time)//课时时间(示例:8次课 班课总人数200人，可报名3人)
    private TextView tvCourseDetailAllClassTime;

    @ViewInject(R.id.tv_course_detail_address_sh)//上课地址(示例:闸北区)
    private TextView tvCourseDetailAddressSh;

    @ViewInject(R.id.tv_course_detail_intro_content)//课程简介
    private TextView tvCourseDetailIntroContent;

    @ViewInject(R.id.tv_course_detail_intro_content_line)//课程简介分割线
    private View tvCourseDetailIntroContentLine;

    @ViewInject(R.id.tv_course_detail_more_intro)//课程简介更多 点击事件
    private TextView tvCourseDetailMoreIntro;

    @ViewInject(R.id.tv_course_detail_open_time)//开课时间(示例:06月01日开课 06月29日杰克 共8节)
    private TextView tvCourseDetailOpenTime;

    @ViewInject(R.id.tv_course_detail_open_last_time)//剩余课程4次
    private TextView tvCourseDetailOpenLastTime;

    @ViewInject(R.id.lv_course_detail_time)//开课时间列表
    private MyListView lvCourseDetailTime;

    @ViewInject(R.id.tv_course_detail_more)//更多课程时间 点击事件
    private TextView tvCourseDetailMore;

    @ViewInject(R.id.ll_show_teacher)
    private LinearLayout llShowTeacher;

    @ViewInject(R.id.iv_course_detail_teacher)//课程老师图片
    private ImageView ivCourseDetailTeacher;

    @ViewInject(R.id.iv_course_detail_teacher_sex)//课程老师性别
    private ImageView ivCourseDetailTeacherSex;

    @ViewInject(R.id.tv_course_detail_teacher_name)//课程老师姓名
    private TextView tvCourseDetailTeacherName;

    @ViewInject(R.id.tv_course_detail_teacher_tags)//课程老师教龄
    private TextView tvCourseDetailTeacherTags;

    @ViewInject(R.id.tv_course_detail_teacher_where)//课程老师地址
    private TextView tvCourseDetailTeacherWhere;

    @ViewInject(R.id.tv_course_detail_teacher_content)//课程老师课程(示例:开设课程: 拉丁舞初级、现代舞初级)
    private TextView tvCourseDetailTeacherContent;

    @ViewInject(R.id.tv_course_detail_baoming)//课程报名 点击事件
    private TextView tvCourseDetailBaoming;

//    @ViewInject(R.id.iv_mine_organization_back_bg)//返回背景
//    private CircleImageView iv_mine_organization_back_bg;

    @ViewInject(R.id.tv_course_detail_more)
    private TextView tv_course_detail_more;


    private final int Course_Detail_Login = 1000;       // 课程详情登录请求码
    private final String Attention_Type_Course = "2";   //关注操作的类型-课程

    private String course_id;
    private CourseDetailData courseDetailData;
    private int maxLine = 4;
    private List<CourseTimeList> mLists;
    private CourseDetailMoreAdapter courseDetailMoreAdapter;
    private boolean flag = false;
    private boolean lineFlag = false;
    private boolean listenerFlag = true;
    private int prePointPosition = 0;
    private List<ImageView> imageViews;
    private String uid;
    private int pos;

    @Override
    public int setContentViewId() {
        return R.layout.activity_course_detail;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (null != intent) {
            course_id = intent.getStringExtra("COURSE_ID");
            pos = getIntent().getIntExtra("pos", -1);
        }
    }

    @Override
    protected void initView() {
        slCourseDetail.setVisibility(View.GONE);
        rlCourseDetailAll.setBackgroundColor(UIUtils.getColor(R.color.white));
        rlCourseDetailAll.getBackground().setAlpha(0);
        tvCourseDetailMore.setVisibility(View.GONE);
        ivCourseDetailAttention.setVisibility(View.INVISIBLE);
        unUseClick();
        ivCourseDetailShare.setClickable(false);
    }

    @Override
    protected void initData() {
        uid = SharedPreferencesUtils.getString(this, "id", null);
        if (null != course_id) {
            HttpCourseApi.getInstance().getCourseDetail(uid, course_id);
        }
    }

    @Override
    public void initListener() {
        tvCourseDetailMoreIntro.setOnClickListener(this);
        tvCourseDetailMore.setOnClickListener(this);
        tvCourseDetailBaoming.setOnClickListener(this);
        ivCourseDetailBack.setOnClickListener(this);
        ivCourseDetailShare.setOnClickListener(this);
        tvCourseDetailAddressSh.setOnClickListener(this);
        llShowTeacher.setOnClickListener(this);
        ivCourseDetailAttention.setOnClickListener(this);

        slCourseDetail.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
                if (y < UIUtils.dip2px(216)) {
                    //透明度变化范围(0~255)
                    int i = y * 255 / UIUtils.dip2px(216);
                    rlCourseDetailAll.getBackground().setAlpha(i);
                    tvCourseDetailTitle.setTextColor(Color.argb(i, 0, 0, 0));
//                    int z = y * 102 / UIUtils.dip2px(216);
//                    iv_mine_organization_back_bg.getBackground().setAlpha(102 - z);
                }
            }
        });
        slCourseDetail.smoothScrollTo(0, 20);
        vpCourseDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (imageViews.size() > 1) {
                    int currentPosition = position % imageViews.size();
                    llCoursePointGroup.getChildAt(prePointPosition).setEnabled(false);
                    llCoursePointGroup.getChildAt(currentPosition).setEnabled(true);
                    prePointPosition = currentPosition;
                } else {
                    vpCourseDetail.setCurrentItem(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        super.onRefreshEvent(event);
        if (event.getType() == DataManager.GET_COURSE_DETAIL) {               //获得课程详情返回数据
            CourseDetail courseDetail = (CourseDetail) event.getData();
            if (courseDetail != null && courseDetail.getData() != null) {
                courseDetailData = courseDetail.getData();
                showPage();
            }
        } else if (event.getType() == DataManager.APPLY_COURSE_DATA) {         //预订结果返回
            CourseApplyResult courseApplyResult = (CourseApplyResult) event.getData();
            if (null != courseApplyResult) {
                jumpCourseOrderPage(courseApplyResult);
            }
        } else if (DataManager.ATTENTION_COURSE == event.getType()) {              //已关注
            courseDetailData.setIs_interest("1");
            setAttentionState();
        } else if (DataManager.ATTENTION_CANCEL_COURSE == event.getType()) {       //取消关注
            courseDetailData.setIs_interest("0");
            setAttentionState();
        }
    }

    /**
     * 展示页面
     */
    private void showPage() {
        slCourseDetail.setVisibility(View.VISIBLE);
        //设置分享按钮是否可用
        ivCourseDetailShare.setClickable(true);
        //设置关注按钮状态
        setAttentionState();
        ivCourseDetailAttention.setVisibility(View.VISIBLE);

        //设置滚动图片
        List<String> pics = courseDetailData.getPics();
        if (null != pics && pics.size() > 0) {
            imageViews = new ArrayList<>();
            showViewPager(pics);
        }

        //设置描述
        tvCourseDetailIntroContent.setText(courseDetailData.getDesc());
        tvCourseDetailPrice.setText(courseDetailData.getSingle_price());
        tvCourseDetailContent.setText(courseDetailData.getName());
        String schoolName = courseDetailData.getSchool().getName();
        if (null != schoolName && !"".equals(schoolName)) {
            tvCourseDetailAddressSh.setText(schoolName);
            tvCourseDetailAddressSh.setEnabled(true);
        } else {
            tvCourseDetailAddressSh.setEnabled(false);
        }
        ViewTreeObserver vto = tvCourseDetailIntroContent.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (listenerFlag) {
                    final int lineCount = tvCourseDetailIntroContent.getLineCount();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (lineCount <= maxLine) {
                                tvCourseDetailMoreIntro.setVisibility(View.GONE);
                                tvCourseDetailIntroContentLine.setVisibility(View.GONE);
                            } else {
                                tvCourseDetailIntroContent.setMaxLines(maxLine);
                                tvCourseDetailMoreIntro.setVisibility(View.VISIBLE);
                                tvCourseDetailIntroContentLine.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                return true;
            }

        });
        List<CourseTimeList> time_list = courseDetailData.getSchedule().getTime_list();
        if (null != time_list && time_list.size() > 0) {
            mLists = time_list;
            showTimeList();
        }
        String avatar = courseDetailData.getTeacher().getAvatar();
        if (null != avatar && !"".equals(avatar)) {
            Glide.with(UIUtils.getContext()).load(avatar).into(ivCourseDetailTeacher);
        }
        /*List<String> teacherPics = courseDetailData.getTeacher().getPics();
        if(null != teacherPics && teacherPics.size()>0){
            String pic = teacherPics.get(0);
            Glide.with(this).load(pic).into(ivCourseDetailTeacher);
        }*/
        tvCourseDetailTeacherName.setText(courseDetailData.getTeacher().getReal_name());    //设置老师名字

        if ("0".equals(courseDetailData.getTeacher().getSex())) {
            ivCourseDetailTeacherSex.setVisibility(View.GONE);
        } else {
            if ("1".equals(courseDetailData.getTeacher().getSex())) {                             //设置老师性别
                ivCourseDetailTeacherSex.setImageResource(R.drawable.male);
            } else if ("2".equals(courseDetailData.getTeacher().getSex())) {
                ivCourseDetailTeacherSex.setImageResource(R.drawable.female);
            }
            ivCourseDetailTeacherSex.setVisibility(View.VISIBLE);
        }

        String teacherTag;
        String tag = courseDetailData.getTeacher().getTags();
        if (tag.contains(",")) {
            String[] tags = tag.split(",");
            teacherTag = tags[0];
        } else {
            teacherTag = tag;
        }
        if (null != teacherTag && !"".equals(teacherTag)) {
            tvCourseDetailTeacherTags.setText(teacherTag);
        } else {
            tvCourseDetailTeacherTags.setVisibility(View.GONE);
        }
        String org_name = courseDetailData.getTeacher().getOrg_name();
        String org_teacher;
        if (null != org_name && !"".equals(org_name)) {
            org_teacher = org_name;
            tvCourseDetailTeacherWhere.setText(
                    String.format(
                            getString(R.string.txt_subsidiary_organ),
                            org_teacher
                    ));
        } else {
            org_teacher = getString(R.string.txt_personal_teacher);
            tvCourseDetailTeacherWhere.setText(org_teacher);
        }
        tvCourseDetailTeacherContent.setText(
                String.format(
                        getString(R.string.txt_open_course_info),
                        courseDetailData.getTeacher().getCourse_info()
                ));
        String show_teacher = courseDetailData.getTeacher().getShow_teacher();
        if ("1".equals(show_teacher)) {
            llShowTeacher.setVisibility(View.VISIBLE);
        } else {
            llShowTeacher.setVisibility(View.GONE);
        }
    }

    /**
     * 展示列表
     */
    private void showTimeList() {
        //8次课 班课总人数200人，可报名3人
        tvCourseDetailAllClassTime.setText(/*courseDetailData.getSchedule().getAll_course_hour()*/
                mLists.size() +
                        "次课 班课总人数" + courseDetailData.getTotal_count() + "人,可报名" +
                        courseDetailData.getRemain_count() + "人");
        tvCourseDetailFirstTime.setText(mLists.get(0).getTime());
        tvCourseDetailOpenTime.setText(courseDetailData.getSchedule().getDate_section() + " 共" +
                mLists.size() + "次");
        int count = 0;
        int statusCount = 0;
        for (CourseTimeList ct : mLists) {
            int APPLY = 1;
            int FINISH = 3;
            if (ct.getStatus() == APPLY) {
                statusCount++;
            }
            if (ct.getStatus() != FINISH) {
                count++;
            }
        }
        if (count == 0) {
            tvCourseDetailOpenLastTime.setText(R.string.txt_course_over);
        } else {
            tvCourseDetailOpenLastTime.setText(
                    String.format(
                            getString(R.string.format_surplus_course),
                            count
                    ));
        }
        if (statusCount != 0) {
            useClick();
        } else {
            unUseClick();
        }
        int maxCourse = 4;
        if (mLists.size() < maxCourse) {
            tvCourseDetailMore.setVisibility(View.GONE);
        } else {
            tvCourseDetailMore.setVisibility(View.VISIBLE);
        }
        if (null != courseDetailMoreAdapter) {
            courseDetailMoreAdapter.notifyDataSetChanged();
        } else {
            courseDetailMoreAdapter = new CourseDetailMoreAdapter(this, mLists);
            lvCourseDetailTime.setAdapter(courseDetailMoreAdapter);
        }
    }

    private void showViewPager(List<String> pics) {
        ImageView imageView;
        View v;
        llCoursePointGroup.removeAllViews();
        for (int i = 0; i < pics.size(); i++) {
            imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(UIUtils.getContext()).load(pics.get(i)).into(imageView);
            imageViews.add(imageView);
            v = new View(getApplicationContext());
            v.setBackgroundResource(R.drawable.point_bg);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            if (i != 0) {
                params.leftMargin = 40;
            }
            v.setLayoutParams(params);
            v.setEnabled(false);
            llCoursePointGroup.addView(v);
        }
        CourseDetailPagerAdapter courseDetailPagerAdapter = new CourseDetailPagerAdapter(vpCourseDetail, imageViews);
        vpCourseDetail.setAdapter(courseDetailPagerAdapter);
        llCoursePointGroup.getChildAt(0).setEnabled(true);
//        int maxValue = Integer.MAX_VALUE;
//        int item = maxValue - maxValue % imageViews.size();
//        int item = 10000 - 10000 % imageViews.size();
//        vpCourseDetail.setCurrentItem(item);
    }

    private void unUseClick() {
        tvCourseDetailBaoming.setEnabled(false);
        tvCourseDetailBaoming.setBackgroundColor(UIUtils.getColor(R.color.app_theme_color_no));
    }

    private void useClick() {
        tvCourseDetailBaoming.setEnabled(true);
        tvCourseDetailBaoming.setBackgroundColor(UIUtils.getColor(R.color.app_theme_color));
    }

    @Override
    public String getPageName() {
        return getString(R.string.course_detial_page);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_course_detail_more_intro:
                listenerFlag = false;
                if (lineFlag) {
                    lineFlag = false;
                    tvCourseDetailMoreIntro.setText(UIUtils.getString(R.string.course_detial_more_intro));
                    tvCourseDetailIntroContent.setMaxLines(maxLine);
                    Drawable nav_up = getResources().getDrawable(R.drawable.down);
                    if (nav_up != null) {
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        tvCourseDetailMoreIntro.setCompoundDrawables(null, null, nav_up, null);
                    }
                } else {
                    lineFlag = true;
                    tvCourseDetailMoreIntro.setText(UIUtils.getString(R.string.course_detial_more_intro_pull));
                    tvCourseDetailIntroContent.setMaxLines(Integer.MAX_VALUE);
                    Drawable nav_up = getResources().getDrawable(R.drawable.up);
                    if (nav_up != null) {
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        tvCourseDetailMoreIntro.setCompoundDrawables(null, null, nav_up, null);
                    }
                }

                break;
            case R.id.tv_course_detail_more:
                if (flag) {
                    tv_course_detail_more.setText(UIUtils.getString(R.string.course_detial_more_intro));
                    Drawable nav_up = getResources().getDrawable(R.drawable.down);
                    if (nav_up != null) {
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        tv_course_detail_more.setCompoundDrawables(null, null, nav_up, null);
                    }
                } else {
                    tv_course_detail_more.setText(UIUtils.getString(R.string.course_detial_more_intro_pull));
                    Drawable nav_up = getResources().getDrawable(R.drawable.up);
                    if (nav_up != null) {
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        tv_course_detail_more.setCompoundDrawables(null, null, nav_up, null);
                    }
                }
                flag = !flag;
                courseDetailMoreAdapter.setFlag(flag);
                courseDetailMoreAdapter.notifyDataSetChanged();
                break;

            case R.id.iv_course_detail_back:
                onBackPressed();
                break;
            case R.id.iv_course_detail_share:
                ShareDialog
                        .getInstance()
                        .setCurrent_Type(ShareDialog.Type_Share_Course_Detail)
                        .setSchool_name(courseDetailData.getSchool().getName())
                        .setCourse_id(courseDetailData.getId())
                        .setCourse_name(courseDetailData.getName())
                        .setTeacher_name(courseDetailData.getTeacher().getReal_name())
                        .showDialog();
                break;
            case R.id.tv_course_detail_address_sh:
                if (null != courseDetailData) {
//                    jumpAdressPage();
                    jumpSchoolPage();

                }
                break;
            case R.id.tv_course_detail_baoming:
                if (loginJudge() && null != course_id) {
                    HttpCourseApi.applyCourse(uid, course_id);
                }
                break;
            case R.id.ll_show_teacher:
                jumpTeacherDetialPage();
                break;
            case R.id.iv_course_detail_attention:   //关注课程/取消关注课程
                if (loginJudge() && null != course_id) {
                    if ("0".equals(courseDetailData.getIs_interest())) {
                        HttpCommonApi.attentionAction("interest", uid, course_id, Attention_Type_Course);     //关注
                    } else {
                        HttpCommonApi.attentionAction("uninterest", uid, course_id, Attention_Type_Course);   //取消关注
                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * 登录相关判断处理
     */
    private boolean loginJudge() {
        if (StringUtils.isNullString(uid)) {
            Intent intentLogin = new Intent(this, LoginActivity.class);
            startActivityForResult(intentLogin,
                    Course_Detail_Login);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置关注状态
     */
    private void setAttentionState() {
        if (courseDetailData == null) {
            return;
        }
        if ("1".equals(courseDetailData.getIs_interest())) {     //已关注
            ivCourseDetailAttention.setBackgroundResource(R.drawable.attention_circle_solid);
        } else {              //未关注
            ivCourseDetailAttention.setBackgroundResource(R.drawable.attention_circle);
        }
    }

    /**
     * 跳转门店详情页面
     */
    private void jumpSchoolPage() {
        Intent intent = new Intent(UIUtils.getContext(),
                IndexDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("sid", courseDetailData.getSchool().getId());
        bundle.putString("name", courseDetailData.getSchool().getName());
        bundle.putString("address", courseDetailData.getSchool().getAddress());
        bundle.putString("tags", courseDetailData.getSchool().getTags());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转老师资料页面
     */
    private void jumpTeacherDetialPage() {
        if (courseDetailData == null) {
            return;
        }
        if (courseDetailData.getTeacher() == null) {
            return;
        }
        Intent intent = new Intent(this, TeacherDetailActivity.class);
        intent.putExtra("id", courseDetailData.getTeacher().getTeacher_id());
        startActivity(intent);
    }

    /**
     * 跳往课程订单页面
     *
     * @param courseApplyResult 预订成功结果
     */
    private void jumpCourseOrderPage(CourseApplyResult courseApplyResult) {
        Intent OrderCourseIntent = new Intent(this, OrderCourseActivity.class);
        OrderCourseIntent.putExtra("ORDERCOURSE", courseApplyResult);
        startActivity(OrderCourseIntent);
    }

    /**
     * 跳往地图页面
     */
    private void jumpAdressPage() {
        boolean isHaveBaidu = NavigationUtils.isInstallByread("com.baidu.BaiduMap");
        boolean isHaveGaoDe = NavigationUtils.isInstallByread("com.autonavi.minimap");
        String haveMap;
        if (!isHaveBaidu && !isHaveGaoDe) {
            haveMap = "0";
        } else {
            haveMap = "1";
        }
        String url = UrlUtils.SERVER_WEB_TO_CLASSDETAIL
                + "trapmap=trapmap&title=" + courseDetailData.getSchool().getName()
                + "&to=" + courseDetailData.getSchool().getLng() + "," +
                courseDetailData.getSchool().getLat()
                + "&to_g=" + courseDetailData.getSchool().getLng_g() + "," +
                courseDetailData.getSchool().getLat_g()
                + "&address=" + courseDetailData.getSchool().getAddress() + "&havemap=" + haveMap;
        String title = courseDetailData.getSchool().getName();
        int pageValue = WebConstant.WEB_Int_Map_Page;

        gotoWebPage(url, title, pageValue, "");
    }

    private void gotoWebPage(String url, String title, int pageValue,
                             String tid) {
        Intent intent = new Intent(
                UIUtils.getContext(),
                Common_Show_WebPage_Activity.class
        );
        intent.putExtra(
                Common_Show_WebPage_Activity.Param_String_Title,
                title
        );
        intent.putExtra(
                UIUtils.getString(R.string.redirect_open_url),
                url
        );
        intent.putExtra(
                UIUtils.getString(R.string.get_page_name),
                pageValue
        );
        intent.putExtra(
                UIUtils.getString(R.string.redirect_tid),
                tid
        );
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Course_Detail_Login && resultCode == RESULT_OK) {
            initData();
        }
    }

    @Override
    public void onBackPressed() {
        if (pos != -1) {
            Intent intent = getIntent();
            intent.putExtra("pos", pos);
            intent.putExtra("Is_interest", courseDetailData.getIs_interest());
            setResult(1, intent);
        }
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 1);
    }
}
