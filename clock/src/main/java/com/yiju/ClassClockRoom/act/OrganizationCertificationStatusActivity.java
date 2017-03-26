package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.MineOrganizationBean;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.ListViewForScrollView;

/*
 * 机构认证状态
 * Created by wh on 2016/3/21.
 */
public class OrganizationCertificationStatusActivity extends BaseActivity
        implements View.OnClickListener {
    //认证失败
    public static final int STATUS_FAIL = 10000;
    //认证中
    public static final int STATUS_ING = 10001;
    //认证状态
    public static final String STATUS = "status";
    //传进来的状态
    private int status;
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //认证状态
    @ViewInject(R.id.tv_authentication_status)
    private TextView tv_authentication_status;
    //如何认证
    @ViewInject(R.id.tv_how_authentication)
    private TextView tv_how_authentication;
    //认证信息
    @ViewInject(R.id.rl_authentication_information)
    private RelativeLayout rl_authentication_information;
    //机构名字
    @ViewInject(R.id.tv_organization_name)
    private TextView tv_organization_name;
    //失败内容
    @ViewInject(R.id.lv_authentication_fail_content)
    private ListViewForScrollView lv_authentication_fail_content;
    //认证图片
    @ViewInject(R.id.iv_authentication_status)
    private ImageView iv_authentication_status;
    //bean
    private MineOrganizationBean bean;
    //适配器
    private ArrayAdapter<String> adapter;


    @Override
    public void initIntent() {
        super.initIntent();
        status = getIntent().getIntExtra(STATUS, -1);
    }

    @Override
    protected void initView() {
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        tv_how_authentication.setOnClickListener(this);
        rl_authentication_information.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.mine_organization));
        if (status == STATUS_FAIL) {
            lv_authentication_fail_content.setVisibility(View.VISIBLE);
            iv_authentication_status.setBackgroundResource(R.drawable.register_failed);
            tv_authentication_status.setText(UIUtils.getString(R.string.authentication_fail));
        } else if (status == STATUS_ING) {
            iv_authentication_status.setBackgroundResource(R.drawable.register_ing);
            tv_authentication_status.setText(UIUtils.getString(R.string.authentication_ing));
        }
        getHttpUtils();
    }


    private void getHttpUtils() {
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
        if ("1".equals(bean.getCode())) {
            tv_organization_name.setText(bean.getData().getName());
            if (bean.getData() != null && StringUtils.isNotNullString(bean.getData().getAuth_remark())) {
                String[] split = bean.getData().getAuth_remark().split("\\|");
                for (int i = 0; i < split.length; i++) {
                    split[i] = i + 1 + "." + split[i];
                }
                adapter = new ArrayAdapter<>(this, R.layout.item_lv_authentication_fail_content,
                        R.id.tv_item_teacher_fail, split);
                lv_authentication_fail_content.setAdapter(adapter);
            }

        } else {
            UIUtils.showToastSafe(bean.getMsg());
        }
    }


    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_org_certification_status);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organzition_certification_status;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
            case R.id.tv_how_authentication:
                Intent intent_h5 = new Intent();
                intent_h5.putExtra(UIUtils.getString(R.string.get_page_name),
                        WebConstant.Organization_authentication_Page);
                intent_h5.setClass(UIUtils.getContext(),
                        Common_Show_WebPage_Activity.class);
                intent_h5.putExtra(
                        Common_Show_WebPage_Activity.Param_String_Title, "机构认证");
                startActivity(intent_h5);
                break;
            case R.id.rl_authentication_information:
                Intent intent = new Intent(this, OrganizationInformationActivity.class);
                intent.putExtra("bean", bean);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, PersonalCenterActivity.class);
//            intent.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_MY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}
