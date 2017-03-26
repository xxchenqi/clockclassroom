package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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
import com.yiju.ClassClockRoom.adapter.CourseAdapter;
import com.yiju.ClassClockRoom.adapter.MineOrganizationPagerAdapter;
import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.bean.TeacherDetailData;
import com.yiju.ClassClockRoom.bean.result.TeacherDetailBean;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.share.ShareDialog;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpCommonApi;
import com.yiju.ClassClockRoom.widget.CircleImageView;
import com.yiju.ClassClockRoom.widget.ListViewForScrollView;
import com.yiju.ClassClockRoom.widget.NewFlowLayout;
import com.yiju.ClassClockRoom.widget.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释: 老师详情页
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/6/23 10:35
 * ----------------------------------------
 */
public class TeacherDetailActivity extends BaseActivity implements MineOrganizationPagerAdapter.VpClickListener, View.OnClickListener, AdapterView.OnItemClickListener {
    //返回
    @ViewInject(R.id.iv_mine_organization_back)
    private ImageView iv_mine_organization_back;
    //标题
    @ViewInject(R.id.tv_mine_organization_title)
    private TextView tv_mine_organization_title;
    //标题栏
    @ViewInject(R.id.rl_mine_organization_back)
    private RelativeLayout rl_mine_organization_back;
    //sv
    @ViewInject(R.id.sv_mine_organization)
    private ObservableScrollView sv_mine_organization;
    //vp
    @ViewInject(R.id.vp_mine_organization_head)
    private ViewPager vp_mine_organization_head;
    //图片数量
    @ViewInject(R.id.tv_pager_num)
    private TextView tv_pager_num;
    //头像
    @ViewInject(R.id.civ_mine_organization)
    private CircleImageView civ_mine_organization;
    //名字
    @ViewInject(R.id.tv_mine_organization_name)
    private TextView tv_mine_organization_name;
    //标记
    @ViewInject(R.id.fl_mine_organization)
    private NewFlowLayout fl_mine_organization;
    //简介
    @ViewInject(R.id.tv_mine_organization_brief)
    private TextView tv_mine_organization_brief;
    //默认的头部图片
    @ViewInject(R.id.v_mine_organization_head)
    private View v_mine_organization_head;
    //课程列表
    @ViewInject(R.id.lv_course)
    private ListViewForScrollView lv_course;
    //是否认证
    @ViewInject(R.id.iv_teacher_auth)
    private ImageView iv_teacher_auth;
    //老师性别
    @ViewInject(R.id.iv_teacher_sex)
    private ImageView iv_teacher_sex;
    //分享
    @ViewInject(R.id.iv_share)
    private ImageView iv_share;
    //关注老师
    @ViewInject(R.id.iv_teacher_attention)
    private ImageView iv_teacher_attention;
    //返回背景
//    @ViewInject(R.id.iv_mine_organization_back_bg)
//    private CircleImageView iv_mine_organization_back_bg;
    //机构名称
    @ViewInject(R.id.tv_mine_organization_org_name)
    private TextView tv_mine_organization_org_name;
    //老师id
    private String teacher_id;
    //标签的参数设置
    private LinearLayout.LayoutParams lp;
    //图片集合
    private ArrayList<ImageView> imageViews;
    //图片总数
    private int picSize;
    //风采图片适配器
    private MineOrganizationPagerAdapter pagerAdapter;
    //课程适配器
    private CourseAdapter courseAdapter;
    //已开课程数据源
    private ArrayList<Object> objectList;
    //课程数据
    private List<CourseInfo> courses;
    //老师数据
    private TeacherDetailData data;

    private boolean destroy_flag = false;

    private final int Teacher_Detail_Login = 1000;       // 老师详情登录请求码
    private final String Attention_Type_Teacher = "1";   //关注操作的类型-老师
    private int pos;

    @Override
    public int setContentViewId() {
        return R.layout.activity_teacher_detail;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        teacher_id = getIntent().getStringExtra("id");
        pos = getIntent().getIntExtra("pos", -1);
    }

    @Override
    protected void initView() {
        iv_teacher_attention.setVisibility(View.INVISIBLE);
        //初始化颜色和透明度
        rl_mine_organization_back.setBackgroundColor(UIUtils.getColor(R.color.white));
        rl_mine_organization_back.getBackground().setAlpha(0);
        tv_mine_organization_title.setTextColor(Color.argb(0, 102, 102, 102));

        lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(6, 6, 6, 6);
    }

    @Override
    protected void initData() {
        objectList = new ArrayList<>();
        courseAdapter = new CourseAdapter(objectList);
        lv_course.setAdapter(courseAdapter);
        if (StringUtils.isNotNullString(teacher_id)) {
            getHttpData(teacher_id);
        }
    }


    private void getHttpData(String teacher_id) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        if ("-1".equals(StringUtils.getUid())){
            params.addBodyParameter("own_uid", "");
        }else {
            params.addBodyParameter("own_uid", StringUtils.getUid());
        }
        params.addBodyParameter("uid", teacher_id);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_TEACHER_API_JAVA, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        processData(arg0.result);
                    }
                }
        );
    }

    @Override
    public void initListener() {
        super.initListener();
        iv_mine_organization_back.setOnClickListener(this);
        lv_course.setOnItemClickListener(this);
        iv_share.setOnClickListener(this);
        iv_teacher_attention.setOnClickListener(this);
        vp_mine_organization_head.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_pager_num.setText(position + 1 + "/" + picSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        sv_mine_organization.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
                if (y < UIUtils.dip2px(216)) {
                    //透明度变化范围(0~255)
                    int i = y * 255 / UIUtils.dip2px(216);
                    rl_mine_organization_back.getBackground().setAlpha(i);
                    tv_mine_organization_title.setTextColor(Color.argb(i, 102, 102, 102));
//                    int z = y * 102 / UIUtils.dip2px(216);
//                    iv_mine_organization_back_bg.getBackground().setAlpha(102 - z);
                }
            }
        });
        iv_share.setClickable(false);
    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        super.onRefreshEvent(event);
        /*if (DataManager.GET_TEACHER_DETAIL == event.getType()) {
            TeacherDetailBean teacherDetailBean = (TeacherDetailBean) event.getData();
            if (teacherDetailBean != null && teacherDetailBean.getData() != null) {
                processData(teacherDetailBean);
            }
        } else */
        if (DataManager.ATTENTION_TEACHER == event.getType()) {              //已关注
            data.setIs_interest("1");
            setAttentionState();
        } else if (DataManager.ATTENTION_CANCEL_TEACHER == event.getType()) {       //取消关注
            data.setIs_interest("0");
            setAttentionState();
        }
    }

    private void processData(String result/*TeacherDetailBean teacherDetailBean*/) {
        TeacherDetailBean teacherDetailBean = GsonTools.changeGsonToBean(result, TeacherDetailBean.class);
        if (teacherDetailBean == null) {
            return;
        }
        if ("0".equals(teacherDetailBean.getCode())) {
            if (destroy_flag) {
                return;
            }
            iv_share.setClickable(true);
            data = teacherDetailBean.getData();

            setAttentionState();                                //设置关注
            iv_teacher_attention.setVisibility(View.VISIBLE);

            if ("0".equals(data.getSex())) {           //设置老师性别
                iv_teacher_sex.setVisibility(View.GONE);
            } else {
                if ("1".equals(data.getSex())) {
                    iv_teacher_sex.setImageResource(R.drawable.male);
                } else if ("2".equals(data.getSex())) {
                    iv_teacher_sex.setImageResource(R.drawable.female);
                }
                iv_teacher_sex.setVisibility(View.VISIBLE);
            }

            if (!"".equals(data.getAvatar())) {
                Glide.with(this).load(data.getAvatar()).into(civ_mine_organization);
            }
            if (!"".equals(data.getOrg_name())) {
                iv_teacher_auth.setVisibility(View.VISIBLE);
                tv_mine_organization_org_name.setText(data.getOrg_name());
            }
            if (!"".equals(data.getReal_name())) {
                tv_mine_organization_name.setText(data.getReal_name());
            } else {
                tv_mine_organization_name.setText(R.string.txt_no_data);
            }

            if (!"".equals(data.getInfo())) {
                tv_mine_organization_brief.setText(data.getInfo());
            } else {
                tv_mine_organization_brief.setText(R.string.txt_no_data);
            }
            //标签设置
            if (fl_mine_organization != null) {
                fl_mine_organization.removeAllViews();
            }
            if (!"".equals(data.getTags())) {
                String[] tags = data.getTags().split(",");
                //计算限制数量最多为2行
                int sum = 0;
                int lines = 0;
                for (String tag : tags) {
                    TextView view = new TextView(this);
                    view.setText(tag);
                    view.setTextSize(10);
                    view.setGravity(Gravity.CENTER_VERTICAL);
                    view.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tv_tags_green));

                    int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    view.measure(width, height);
                    sum += view.getMeasuredWidth() + 12;
                    if (sum < fl_mine_organization.getWidth() && lines <= 1) {
                        fl_mine_organization.addView(view, lp);
                    } else {
                        lines++;
                        sum = 0;
                        if (lines <= 1) {
                            sum += view.getMeasuredWidth() + 12;
                            fl_mine_organization.addView(view, lp);
                        }
                    }
                }
            }
            //老师风采图片设置
            List<String> pic = data.getPic();
            //图片总数
            imageViews = new ArrayList<>();
            if (pic != null) {
                picSize = pic.size();
                for (int i = 0; i < picSize; i++) {
                    ImageView imageView = new ImageView(this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (StringUtils.isNotNullString(pic.get(i))) {
                        Glide.with(this).load(pic.get(i)).into(imageView);
                    }
                    imageViews.add(imageView);
                }
                if (picSize == 0) {
                    v_mine_organization_head.setVisibility(View.VISIBLE);
                } else {
                    v_mine_organization_head.setVisibility(View.GONE);
                }

            }

            pagerAdapter = new MineOrganizationPagerAdapter(imageViews, this);
            vp_mine_organization_head.setAdapter(pagerAdapter);
            if (imageViews.size() == 0) {
                tv_pager_num.setVisibility(View.GONE);
            } else {
                tv_pager_num.setVisibility(View.VISIBLE);
                tv_pager_num.setText("1/" + imageViews.size());
            }

            //设置已开课程
            courses = data.getCourse();
            if (null != courses && courses.size() > 0) {
                objectList.addAll(courses);
            }
            courseAdapter.notifyDataSetChanged();

        } else {
            UIUtils.showToastSafe(teacherDetailBean.getMsg());
        }

    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_teacher_detail_Page);
    }

    @Override
    public void vpClick() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mine_organization_back://返回
                onBackPressed();
                break;
            case R.id.iv_share: //分享
                ShareDialog
                        .getInstance()
                        .setCurrent_Type(ShareDialog.Type_Share_Teacher_Detail)
                        .setTeacher_id(data.getId())
                        .setTeacher_name(data.getReal_name())
                        .showDialog();
                break;
            case R.id.iv_teacher_attention:     //关注老师/取消关注老师
                if (loginJudge() && null != teacher_id) {
                    if ("0".equals(data.getIs_interest())) {
                        HttpCommonApi.attentionAction("interest", StringUtils.getUid(), teacher_id, Attention_Type_Teacher);     //关注
                    } else {
                        HttpCommonApi.attentionAction("uninterest", StringUtils.getUid(), teacher_id, Attention_Type_Teacher);   //取消关注
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (courses != null && courses.size() > position) {
            CourseInfo courseInfo = courses.get(position);
            if (courseInfo != null) {
                Intent intent = new Intent(UIUtils.getContext(), CourseDetailActivity.class);
                intent.putExtra("COURSE_ID", courseInfo.getId());
                UIUtils.startActivity(intent);
            }
        }
    }

    /**
     * 设置关注状态
     */
    private void setAttentionState() {
        if (data == null) {
            return;
        }
        if ("1".equals(data.getIs_interest())) {     //已关注
            iv_teacher_attention.setBackgroundResource(R.drawable.attention_circle_solid);
        } else {              //未关注
            iv_teacher_attention.setBackgroundResource(R.drawable.attention_circle);
        }
    }

    /**
     * 登录相关判断处理
     */
    private boolean loginJudge() {
        if ("-1".equals(StringUtils.getUid())) {
            Intent intentLogin = new Intent(this, LoginActivity.class);
            startActivityForResult(intentLogin,
                    Teacher_Detail_Login);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Teacher_Detail_Login && resultCode == RESULT_OK) {
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy_flag = true;
    }

    @Override
    public void onBackPressed() {
        if (pos != -1) {
            Intent intent = getIntent();
            intent.putExtra("pos", pos);
            if (data != null && StringUtils.isNotNullString(data.getIs_interest())) {
                intent.putExtra("Is_interest", data.getIs_interest());
            }
            setResult(1, intent);
        }
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 2);
    }


}