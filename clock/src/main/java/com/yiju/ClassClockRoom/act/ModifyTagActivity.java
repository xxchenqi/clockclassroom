package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.MemberDetailData;
import com.yiju.ClassClockRoom.bean.MienBean;
import com.yiju.ClassClockRoom.bean.MineOrganizationBean;
import com.yiju.ClassClockRoom.bean.TagBean;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;
import com.yiju.ClassClockRoom.widget.NewFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:修改标签
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/21 14:17
 * ----------------------------------------
 */
public class ModifyTagActivity extends BaseActivity implements View.OnClickListener {

    public static final String ACTION_BEAN = "bean";
    public static final String ACTION_TYPE = "type";
    public static final String ACTION_UID = "uid";
    public static final String ACTION_TITLE_FLAG = "title_flag";
    public static final String ACTION_ORGANIZATION_BEAN = "organization_bean";
    public static final String TYPE_ONE = "1";
    public static final String TYPE_TWO = "2";
    //保存成功
    public static int RESULT_CODE_FROM_MODIFY_TAG_ORGANIZATION_ACT = 1001;
    //修改老师标签
    public static int RESULT_CODE_FROM_MODIFY_TAG_TEACHER_ACT = 1002;

    //标签
    @ViewInject(R.id.fl_modify_tags)
    private NewFlowLayout fl_modify_tags;
    //保存
    @ViewInject(R.id.btn_modify_tag_save)
    private Button btn_modify_tag_save;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //新增
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //新增
    @ViewInject(R.id.head_right_image)
    private ImageView head_right_image;
    //老师资料bean
    private MemberDetailResult bean;
    //机构bean
    private MineOrganizationBean organization_bean;
    //数据集合
    private List<String> datas;
    //传进来的类型,1=机构 2=老师 默认1
    private String type;
    //被修改人的id,传进来的id
    private String uid;
    //标题内容
    private String title_flag;
    //身份信息
    private String org_auth;     // 1-普通成员 2-管理员

    private String tags;

    @Override
    public void initIntent() {
        super.initIntent();
        bean = (MemberDetailResult) getIntent().getSerializableExtra(ACTION_BEAN);
        type = getIntent().getStringExtra(ACTION_TYPE);
        uid = getIntent().getStringExtra(ACTION_UID);
        title_flag = getIntent().getStringExtra(ACTION_TITLE_FLAG);
        organization_bean = (MineOrganizationBean) getIntent().getSerializableExtra(ACTION_ORGANIZATION_BEAN);
    }

    @Override
    protected void initView() {
        datas = new ArrayList<>();
    }

    @Override
    protected void initData() {
        org_auth = SharedPreferencesUtils.getString(UIUtils.getContext(),
                getResources().getString(R.string.shared_org_auth), "");
        head_title.setText(UIUtils.getString(R.string.modify_tag));
        head_right_image.setBackgroundResource(R.drawable.tag_add);
        if (UIUtils.getString(R.string.organization_detail).equals(title_flag)) {
            initChildViews2();
        } else {
            initChildViews();
        }


    }

    /**
     * 个人老师资料标签
     */
    private void initChildViews() {
        if (bean.getData() != null) {
            List<TagBean> tags = bean.getTags();
            //已有的tag
            String[] split = bean.getData().getTags().split(",");
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 8;
            lp.rightMargin = 8;
            lp.topMargin = 10;
            lp.bottomMargin = 10;
            for (int i = 0; i < tags.size(); i++) {
                TextView view = new TextView(this);
                view.setText(tags.get(i).getName());
                view.setTextSize(12);
                view.setTextColor(UIUtils.getColor(R.color.color_green_1e));
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tv_flowlayout_green));

                for (String aSplit : split) {
                    if (tags.get(i).getName().equals(aSplit)) {
                        //如果已有
                        view.setTextColor(UIUtils.getColor(R.color.white));
                        view.setBackgroundResource(R.drawable.bg_green_corners);
                        datas.add(view.getText().toString());
                        break;
                    }
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView tv = (TextView) v;
                        String content = tv.getText().toString();
                        if (datas.contains(content)) {
                            //包含,要删除
                            tv.setTextColor(UIUtils.getColor(R.color.color_green_1e));
                            tv.setBackgroundResource(R.drawable.bg_tv_flowlayout_green);
                            datas.remove(content);
                        } else {
                            //不包含,要增加
                            if (datas.size() == 6) {
                                UIUtils.showToastSafe(getString(R.string.toast_most_remaining_six));
                            } else {
                                tv.setTextColor(UIUtils.getColor(R.color.white));
                                tv.setBackgroundResource(R.drawable.bg_green_corners);
                                datas.add(content);
                            }
                        }
                    }
                });
                fl_modify_tags.addView(view, lp);
            }
        } else {
            List<TagBean> tags = bean.getTags();
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 8;
            lp.rightMargin = 8;
            lp.topMargin = 10;
            lp.bottomMargin = 10;
            for (int i = 0; i < tags.size(); i++) {
                TextView view = new TextView(this);
                view.setText(tags.get(i).getName());
                view.setTextSize(12);
                view.setTextColor(UIUtils.getColor(R.color.color_green_1e));
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tv_flowlayout_green));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView tv = (TextView) v;
                        String content = tv.getText().toString();
                        if (datas.contains(content)) {
                            //包含,要删除
                            tv.setTextColor(UIUtils.getColor(R.color.color_green_1e));
                            tv.setBackgroundResource(R.drawable.bg_tv_flowlayout_green);
                            datas.remove(content);
                        } else {
                            //不包含,要增加
                            if (datas.size() == 6) {
                                UIUtils.showToastSafe(getString(R.string.toast_most_remaining_six));
                            } else {
                                tv.setTextColor(UIUtils.getColor(R.color.white));
                                tv.setBackgroundResource(R.drawable.bg_green_corners);
                                datas.add(content);
                            }
                        }
                    }
                });
                fl_modify_tags.addView(view, lp);
            }
        }
    }

    /**
     * 机构标签
     */
    private void initChildViews2() {
        List<MineOrganizationBean.TagsEntity> tags = organization_bean.getTags();
        //已有的tag
        String[] split = organization_bean.getData().getTags().split(",");
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 8;
        lp.rightMargin = 8;
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        for (int i = 0; i < tags.size(); i++) {
            TextView view = new TextView(this);
            view.setText(tags.get(i).getName());
            view.setTextSize(12);
            view.setTextColor(UIUtils.getColor(R.color.color_green_1e));
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tv_flowlayout_green));

            for (String aSplit : split) {
                if (tags.get(i).getName().equals(aSplit)) {
                    //如果已有
                    view.setTextColor(UIUtils.getColor(R.color.white));
                    view.setBackgroundResource(R.drawable.bg_green_corners);
                    datas.add(view.getText().toString());
                    break;
                }
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView tv = (TextView) v;
                    String content = tv.getText().toString();
                    if (datas.contains(content)) {
                        //包含,要删除
                        tv.setTextColor(UIUtils.getColor(R.color.color_green_1e));
                        tv.setBackgroundResource(R.drawable.bg_tv_flowlayout_green);
                        datas.remove(content);
                    } else {
                        //不包含,要增加
                        if (datas.size() == 6) {
                            UIUtils.showToastSafe(getString(R.string.toast_most_remaining_six));
                        } else {
                            tv.setTextColor(UIUtils.getColor(R.color.white));
                            tv.setBackgroundResource(R.drawable.bg_green_corners);
                            datas.add(content);
                        }
                    }
                }
            });
            fl_modify_tags.addView(view, lp);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        btn_modify_tag_save.setOnClickListener(this);
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);

        if ("2".equals(org_auth)) {                            //管理员
            head_right_relative.setClickable(true);
            head_right_image.setVisibility(View.VISIBLE);
        } else {
            head_right_relative.setClickable(false);
            head_right_image.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        if (event.getType() == DataManager.MODIFY_MEMBER_DATA_OTHER) {
            MemberDetailResult bean = (MemberDetailResult) event.getData();
            if ("1".equals(bean.getCode())) {
                UIUtils.showLongToastSafe(UIUtils.getString(R.string.toast_edit_success));
                Intent intent = new Intent();
                intent.putExtra("content", tags);
                setResult(RESULT_CODE_FROM_MODIFY_TAG_TEACHER_ACT, intent);
                finish();
            } else {
                UIUtils.showToastSafe(bean.getMsg());
            }
        }
    }


    /**
     * 机构修改标签请求
     *
     * @param tags 标签内容
     */
    private void getHttpUtils2(String tags) {
        StringBuilder imgs = new StringBuilder();
        List<MineOrganizationBean.DataEntity.MienEntity> mien = organization_bean.getData().getMien();
        for (int i = 0; i < mien.size(); i++) {
            if (i == mien.size() - 1) {
                imgs.append(mien.get(i).getPic());
            } else {
                imgs.append(mien.get(i).getPic()).append(",");
            }
        }
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "edit_organization_info");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("org_uid", StringUtils.getUid());
        }
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("info", organization_bean.getData().getInfo());
        params.addBodyParameter("tags", tags);
        params.addBodyParameter("mien_pic", imgs.toString());

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
                }
        );
    }

    private void processData2(String result) {
        CommonResultBean resultData = GsonTools.fromJson(result, CommonResultBean.class);
        if ("1".equals(resultData.getCode())) {
            UIUtils.showToastSafe(R.string.toast_save_success);
            this.setResult(RESULT_CODE_FROM_MODIFY_TAG_ORGANIZATION_ACT);
            finish();
        } else {
            UIUtils.showToastSafe(resultData.getMsg());
        }
    }


    @Override
    public String getPageName() {
        if (UIUtils.getString(R.string.member_detail).equals(title_flag)
                || !UIUtils.getString(R.string.teacher_detail).equals(title_flag)) {
            return getString(R.string.title_act_my_org_member_edit_tag);
        } else if (UIUtils.getString(R.string.teacher_detail).equals(title_flag)) {
            return getString(R.string.title_act_my_teacher_edit_tag);
        } else if (getString(R.string.organization_detail).equals(title_flag)) {
            return getString(R.string.title_act_my_org_edit_tag);
        }
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_modify_tag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_tag_save://保存
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < datas.size(); i++) {
                    if (i == datas.size() - 1) {
                        sb.append(datas.get(i));
                    } else {
                        sb.append(datas.get(i)).append(",");
                    }
                }

                tags = sb.toString();

                if (UIUtils.getString(R.string.organization_detail).equals(title_flag)) {
                    getHttpUtils2(sb.toString());
                } else {
                    bean.getData().setTags(tags);
                    HttpClassRoomApi.getInstance().askModifyMemberInfo(title_flag, uid, bean, true,false);
//                    getHttpUtils(sb.toString());
                }
                break;
            case R.id.head_back_relative://返回
                finish();
                break;
            case R.id.head_right_relative://新增
                Intent intent = new Intent(this, OrganizationModifyNameActivity.class);
                intent.putExtra(OrganizationModifyNameActivity.ACTION_TITLE,
                        UIUtils.getString(R.string.new_tag));
                intent.putExtra(OrganizationModifyNameActivity.ACTION_TYPE, type);
                intent.putExtra(OrganizationModifyNameActivity.ACTION_TITLE_FLAG, title_flag);
                startActivityForResult(intent, 0);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 新增标签返回
        if (resultCode == OrganizationModifyNameActivity.RESULT_CODE_FROM_ORGANIZATION_MODIFY_NEW_TAG_ACT) {
            String tag = data.getStringExtra("tag");
            TextView view = new TextView(this);
            view.setText(tag);
            view.setTextSize(12);
            view.setTextColor(UIUtils.getColor(R.color.color_green_1e));
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tv_flowlayout_green));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    String content = tv.getText().toString();
                    if (datas.contains(content)) {
                        //包含,要删除
                        tv.setTextColor(UIUtils.getColor(R.color.color_green_1e));
                        tv.setBackgroundResource(R.drawable.bg_tv_flowlayout_green);
                        datas.remove(content);
                    } else {
                        //不包含,要增加
                        if (datas.size() == 6) {
                            UIUtils.showToastSafe(getString(R.string.toast_most_remaining_six));
                        } else {
                            tv.setTextColor(UIUtils.getColor(R.color.white));
                            tv.setBackgroundResource(R.drawable.bg_green_corners);
                            datas.add(content);
                        }
                    }
                }
            });
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 8;
            lp.rightMargin = 8;
            lp.topMargin = 10;
            lp.bottomMargin = 10;
            fl_modify_tags.addView(view, lp);
        }
    }

}
