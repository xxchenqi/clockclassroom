package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.yiju.ClassClockRoom.adapter.MineOrganizationAdapter;
import com.yiju.ClassClockRoom.adapter.MineOrganizationPagerAdapter;
import com.yiju.ClassClockRoom.bean.MemberBean;
import com.yiju.ClassClockRoom.bean.MineOrganizationBean;
import com.yiju.ClassClockRoom.control.FragmentFactory;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.CircleImageView;
import com.yiju.ClassClockRoom.widget.ListViewForScrollView;
import com.yiju.ClassClockRoom.widget.NewFlowLayout;
import com.yiju.ClassClockRoom.widget.ObservableScrollView;
import com.yiju.ClassClockRoom.widget.dialog.ExitOrganizationDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释: 我的机构
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/14 14:52
 * ----------------------------------------
 */
public class MineOrganizationActivity extends BaseActivity implements
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        ExitOrganizationDialog.ExitOrganizationListener,
        MineOrganizationPagerAdapter.VpClickListener {

    //刷新机构风采的标志(只要删除或者增加了风采照片才去更新),true:更新
    public static boolean organization_mien_flag = false;
    //返回按钮
    @ViewInject(R.id.rl_mine_organization_back)
    private RelativeLayout rl_mine_organization_back;
    //标题
    @ViewInject(R.id.tv_mine_organization_title)
    private TextView tv_mine_organization_title;
    //返回
    @ViewInject(R.id.iv_mine_organization_back)
    private ImageView iv_mine_organization_back;
    /**
     * 编辑机构
     */
    @ViewInject(R.id.tv_mine_organization_edit)
    private TextView tv_mine_organization_edit;
    /**
     * 图片
     */
    @ViewInject(R.id.civ_mine_organization)
    private CircleImageView civ_mine_organization;
    /**
     * 热门标签
     */
    @ViewInject(R.id.fl_mine_organization)
    private NewFlowLayout fl_mine_organization;
    /**
     * 风采图
     */
    @ViewInject(R.id.vp_mine_organization_head)
    private ViewPager vp_mine_organization_head;
    /**
     * 老师listview
     */
    @ViewInject(R.id.lv_mine_organization)
    private ListViewForScrollView lv_mine_organization;
    //监听滑动scrollview
    @ViewInject(R.id.sv_mine_organization)
    private ObservableScrollView sv_mine_organization;
    /**
     * 风采图数量
     */
    @ViewInject(R.id.tv_pager_num)
    private TextView tv_pager_num;
    /**
     * 机构简介编辑按钮
     */
    @ViewInject(R.id.ll_mine_organization_brief_edit)
    private LinearLayout ll_mine_organization_brief_edit;
    /**
     * 机构添加老师
     */
    @ViewInject(R.id.ll_mine_organization_add_teacher)
    private LinearLayout ll_mine_organization_add_teacher;
    /**
     * 名称
     */
    @ViewInject(R.id.tv_mine_organization_name)
    private TextView tv_mine_organization_name;
    /**
     * 简介
     */
    @ViewInject(R.id.tv_mine_organization_brief)
    private TextView tv_mine_organization_brief;
    /**
     * 退出机构布局
     */
    @ViewInject(R.id.ll_exit_organization)
    private LinearLayout ll_exit_organization;
    //退出机构
    @ViewInject(R.id.bt_exit)
    private Button bt_exit;
    //转让管理权
    @ViewInject(R.id.bt_transfer)
    private Button bt_transfer;
    //覆盖在机构风采上的view，如果机构风采没照片，就会显示
    @ViewInject(R.id.v_mine_organization_head)
    private View v_mine_organization_head;
    //圆形回退
    @ViewInject(R.id.iv_mine_organization_back_bg)
    private CircleImageView iv_mine_organization_back_bg;
    //机构适配器
    private MineOrganizationAdapter adapter;
    //风采图片适配器
    private MineOrganizationPagerAdapter pagerAdapter;
    //机构成员数据
    private List<MemberBean.DataEntity> datas;
    //风采图片集合
    private List<ImageView> imageViews;
    //机构风采bean
    private MineOrganizationBean bean;
    //用户管理权限
    private String org_auth;
    //图片总数
    private int picSize;
    //标签的参数设置
    private ViewGroup.MarginLayoutParams lp;
    //是否退出
    private boolean destroyFlag = false;

    @Override
    protected void initView() {
        //初始化颜色和透明度
        rl_mine_organization_back.setBackgroundColor(UIUtils.getColor(R.color.white));
        rl_mine_organization_back.getBackground().setAlpha(0);
        tv_mine_organization_title.setTextColor(Color.argb(0, 102, 102, 102));
        iv_mine_organization_back_bg.getBackground().setAlpha(102);

        lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 6;
        lp.rightMargin = 6;
        lp.topMargin = 6;
        lp.bottomMargin = 6;
    }

    @Override
    protected void initData() {
        sv_mine_organization.smoothScrollTo(0, 20);
        getHttpUtils();
        getHttpUtils2();
    }

    @Override
    public void initListener() {
        super.initListener();
        tv_mine_organization_edit.setOnClickListener(this);
        civ_mine_organization.setOnClickListener(this);
        lv_mine_organization.setOnItemClickListener(this);
        ll_mine_organization_add_teacher.setOnClickListener(this);
        bt_exit.setOnClickListener(this);
        bt_transfer.setOnClickListener(this);
        ll_mine_organization_brief_edit.setOnClickListener(this);
        iv_mine_organization_back.setOnClickListener(this);
//        v_mine_organization_head.setOnClickListener(this);
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

                    int z = y * 102 / UIUtils.dip2px(216);
                    iv_mine_organization_back_bg.getBackground().setAlpha(102 - z);
                }
            }
        });
    }

    /**
     * 机构请求
     */
    private void getHttpUtils() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_organization_info");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                });
    }

    private void processData(String result) {
        bean = GsonTools.changeGsonToBean(result,
                MineOrganizationBean.class);
        if (bean == null) {
            return;
        }
        //退出机构显示
        ll_exit_organization.setVisibility(View.VISIBLE);
        if(destroyFlag){
            return;
        }
        if ("1".equals(bean.getCode())) {
            org_auth = bean.getOrg_auth();
            if (!"2".equals(org_auth)) {
                //不是管理员,隐藏各种编辑按钮
                tv_mine_organization_edit.setVisibility(View.GONE);
                ll_mine_organization_brief_edit.setVisibility(View.GONE);
                ll_mine_organization_add_teacher.setVisibility(View.GONE);
                bt_transfer.setVisibility(View.GONE);
            } else {
                v_mine_organization_head.setOnClickListener(this);
                tv_mine_organization_edit.setVisibility(View.VISIBLE);
                ll_mine_organization_brief_edit.setVisibility(View.VISIBLE);
                ll_mine_organization_add_teacher.setVisibility(View.VISIBLE);
                bt_transfer.setVisibility(View.VISIBLE);
            }
            MineOrganizationBean.DataEntity data = bean.getData();
            if (data == null) {
                return;
            }
            //头像
            Glide.with(this).load(data.getLogo()).placeholder(R.drawable.user_unload).into(civ_mine_organization);
            //机构名字
            tv_mine_organization_name.setText(data.getName());
            //机构标签
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

                if ("2".equals(org_auth)) {
                    //是管理员就添加编辑标签
                    LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.include_edit, null);
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MineOrganizationActivity.this, ModifyTagActivity.class);
                            intent.putExtra(ModifyTagActivity.ACTION_TITLE_FLAG,
                                    UIUtils.getString(R.string.organization_detail));
                            intent.putExtra(ModifyTagActivity.ACTION_ORGANIZATION_BEAN, bean);
                            intent.putExtra(ModifyTagActivity.ACTION_TYPE,
                                    ModifyTagActivity.TYPE_ONE);
                            startActivityForResult(intent, 1);
                        }
                    });
                    fl_mine_organization.addView(ll, lp);
                }


            } else {
                if ("2".equals(org_auth)) {
                    //只添加编辑标签
                    LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.include_edit, null);
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MineOrganizationActivity.this,
                                    ModifyTagActivity.class);
                            intent.putExtra(ModifyTagActivity.ACTION_TITLE_FLAG,
                                    UIUtils.getString(R.string.organization_detail));
                            intent.putExtra(ModifyTagActivity.ACTION_ORGANIZATION_BEAN, bean);
                            intent.putExtra(ModifyTagActivity.ACTION_TYPE,
                                    ModifyTagActivity.TYPE_ONE);
                            startActivityForResult(intent, 1);
                        }
                    });
                    fl_mine_organization.addView(ll, lp);
                }
            }

            //简介
            tv_mine_organization_brief.setText(data.getInfo());
            //机构风采
            List<MineOrganizationBean.DataEntity.MienEntity> mien = data.getMien();
            //图片总数
            imageViews = new ArrayList<>();
            if (mien != null) {
                picSize = mien.size();
                for (int i = 0; i < picSize; i++) {
                    ImageView imageView = new ImageView(this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (StringUtils.isNotNullString(mien.get(i).getPic())) {
                        Glide.with(this).load(mien.get(i).getPic()).into(imageView);
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
        } else {
            UIUtils.showToastSafe(bean.getMsg());
        }

        ProgressDialog.getInstance().dismiss();
    }

    /**
     * 成员列表请求
     */
    private void getHttpUtils2() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "organization_members");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData2(arg0.result);

                    }
                });
    }

    private void processData2(String result) {
        MemberBean memberBean = GsonTools.changeGsonToBean(result,
                MemberBean.class);
        if (memberBean == null) {
            return;
        }
        if ("1".equals(memberBean.getCode())) {
            datas = memberBean.getData();
            adapter = new MineOrganizationAdapter(this, datas, R.layout.item_mine_organization);
            lv_mine_organization.setAdapter(adapter);
        } else {
            UIUtils.showToastSafe(memberBean.getMsg());
        }
    }


    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_org);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_mine_organization;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mine_organization_back://返回
                onBackPressed();
                break;
            case R.id.tv_mine_organization_edit://编辑机构风采
                Intent editIntent = new Intent(this, OrganizationMienActivity.class);
                editIntent.putExtra("bean", bean);
                startActivityForResult(editIntent, 1);
                break;
            case R.id.civ_mine_organization://认证信息详情页
                if ("2".equals(org_auth)) {
                    Intent intent = new Intent(this, OrganizationInformationActivity.class);
                    intent.putExtra("bean", bean);
                    startActivity(intent);
                }
                break;
            case R.id.ll_mine_organization_brief_edit://机构简介
                Intent intent_brief = new Intent(this, OrganizationModifyNameActivity.class);
                intent_brief.putExtra("title", UIUtils.getString(R.string.txt_institutional_profile));
                intent_brief.putExtra(OrganizationModifyNameActivity.ACTION_CONTENT, bean.getData().getInfo());
                intent_brief.putExtra(OrganizationModifyNameActivity.ACTION_ORGANIZATION_BEAN, bean);
                startActivityForResult(intent_brief, 1);
                break;
            case R.id.bt_exit://退出机构
                if ("2".equals(org_auth)) {
                    UIUtils.showToastSafe(getString(R.string.toast_transfer_management));
                } else {
                    new ExitOrganizationDialog(MineOrganizationActivity.this, this).createView();
                }
                break;
            case R.id.bt_transfer://转让管理权
                Intent it = new Intent(this, OrganizationTeacherListActivity.class);
                it.putExtra("teacher_list", (Serializable) datas);
                startActivityForResult(it, 1);
                break;
            case R.id.ll_mine_organization_add_teacher://添加成员
                if ("1".equals(bean.getData().getIs_available())) {
                    Intent intent_add_teacher = new Intent(this, OrganizationAddTeacherActivity.class);
                    startActivityForResult(intent_add_teacher, 1);
                } else {
                    UIUtils.showToastSafe(getString(R.string.toast_cannot_add_new_people));
                }
                break;
            case R.id.v_mine_organization_head://view跳转机构风采
                Intent editIntent2 = new Intent(this, OrganizationMienActivity.class);
                editIntent2.putExtra("bean", bean);
                startActivityForResult(editIntent2, 1);
                break;
        }
    }

    //老师资料列表点击
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        Intent intent = new Intent(this, MemberDetailActivity.class);
        if (StringUtils.isNullString(datas.get(position).getReal_name())) {
            intent.putExtra("title", UIUtils.getString(R.string.member_detail));
        } else {
            intent.putExtra("title", datas.get(position).getReal_name());
        }
        intent.putExtra("uid", datas.get(position).getId());
        intent.putExtra("show_teacher", datas.get(position).getShow_teacher());
        intent.putExtra("org_auth", datas.get(position).getOrg_auth());
        intent.putExtra("mobile", datas.get(position).getMobile());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == OrganizationModifyNameActivity.RESULT_CODE_FROM_ORGANIZATION_MODIFY_BRIEF_ACT) {
            //修改机构简介
            String content = data.getStringExtra("content");
            tv_mine_organization_brief.setText(content);
        } else if (resultCode == ModifyTagActivity.RESULT_CODE_FROM_MODIFY_TAG_ORGANIZATION_ACT) {
            //修改标签
            getHttpUtils();
        } else if (resultCode == OrganizationMienActivity.RESULT_CODE_FROM_ORGANIZATION_MIEN_EDIT_ACT) {
            //编辑机构风采
            if (org_auth.equals("2")) {
                if (organization_mien_flag) {
                    imageViews.clear();
                    String content = data.getStringExtra("content");
                    if (!"".equals(content)) {
                        //没有图片
                        String[] contents = content.split(",");
                        for (String content_url : contents) {
                            ImageView imageView = new ImageView(this);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            Glide.with(this).load(content_url).into(imageView);
                            imageViews.add(imageView);
                        }
                        if (pagerAdapter != null) {
                            pagerAdapter.setData(imageViews);
                            pagerAdapter.notifyDataSetChanged();
                        } else {
                            pagerAdapter = new MineOrganizationPagerAdapter(imageViews, this);
                            vp_mine_organization_head.setAdapter(pagerAdapter);
                        }

                        v_mine_organization_head.setVisibility(View.GONE);
                        tv_pager_num.setVisibility(View.VISIBLE);
                        tv_pager_num.setText(vp_mine_organization_head.getCurrentItem() + 1 + "/" + imageViews.size());

                        picSize = imageViews.size();
                    } else {
                        imageViews.clear();
                        pagerAdapter.notifyDataSetChanged();
                        tv_pager_num.setVisibility(View.GONE);
                        v_mine_organization_head.setVisibility(View.VISIBLE);
                    }
                    organization_mien_flag = false;
                }

            }

        } else if (resultCode == OrganizationAddTeacherActivity.RESULT_CODE_FROM_ORGANIZATION_ADD_TEACHER_ACT) {
            //添加老师
            getHttpUtils2();
        } else if (resultCode == OrganizationTeacherListActivity.RESULT_CODE_FROM_ORGANIZATION_TEACHER_LIST_ACT) {
            //转让管理权
            getHttpUtils();
            getHttpUtils2();
        } else if (resultCode == MemberDetailActivity.RESULT_CODE_FROM_MEMBER_DETAIL_ACT) {
            //移除机构
            getHttpUtils2();
        } else if (resultCode == MemberDetailActivity.RESULT_CODE_FROM_MEMBER_BACK_ACT) {
            //老师资料修改
            boolean back_flag = data.getBooleanExtra(MemberDetailActivity.ACTION_BACK_FLAG, false);
            if (back_flag) {
                getHttpUtils2();
            }
        }
    }

    @Override
    public void exitOrganization() {
        setResult(1);
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_MY);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    //viewpager的点击，进入机构风采页
    @Override
    public void vpClick() {
        Intent editIntent2 = new Intent(this, OrganizationMienActivity.class);
        editIntent2.putExtra("bean", bean);
        startActivityForResult(editIntent2, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyFlag = true;
    }
}
