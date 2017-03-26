package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.yiju.ClassClockRoom.adapter.MineOrganizationEditAdapter;
import com.yiju.ClassClockRoom.bean.MemberBean;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 我的机构_转让管理权
 * Created by wh on 2016/3/23.
 */
public class OrganizationTeacherListActivity extends BaseActivity
        implements View.OnClickListener {
    public static final String FROM_TAG = "organization_teacher_list_act";
    //后退按钮
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //转让那妞
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //转让文案
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    @ViewInject(R.id.rl_no_teacher)
    private RelativeLayout rl_no_teacher;
    //列表
    @ViewInject(R.id.lv_teacher_list)
    private ListView lv_teacher_list;
    //选择老师内容
    @ViewInject(R.id.tv_teacher_choose_content)
    private TextView tv_teacher_choose_content;
    //适配器
    private MineOrganizationEditAdapter adapter;
    //数据
    private List<MemberBean.DataEntity> datas;
    //选择位置
    private int selectPos = -1;
    //返回码
    public static int RESULT_CODE_FROM_ORGANIZATION_TEACHER_LIST_ACT = 1004;
    private boolean haveUpdate;


    @Override
    public void initIntent() {
        super.initIntent();
        datas = (List<MemberBean.DataEntity>) getIntent().getSerializableExtra("teacher_list");
    }

    @Override
    protected void initView() {
        head_title.setText(UIUtils.getString(R.string.transfer_of_power));
        head_right_text.setText(UIUtils.getString(R.string.transfer));
        rl_no_teacher.setVisibility(View.GONE);
        tv_teacher_choose_content.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);

        lv_teacher_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (datas == null) {
                    return;
                }
                Intent intent = new Intent(OrganizationTeacherListActivity.this, MemberDetailActivity.class);
                String uid = datas.get(position).getId();
                String show_teacher = datas.get(position).getShow_teacher();
                intent.putExtra("uid", uid);
                intent.putExtra("show_teacher", show_teacher);
                intent.putExtra("org_auth", datas.get(position).getOrg_auth());
                intent.putExtra("title", UIUtils.getString(R.string.member_detail));
                intent.putExtra("mobile", datas.get(position).getMobile());
                intent.putExtra("from", FROM_TAG);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void initData() {
        if (datas == null) {
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            if ("2".equals(datas.get(i).getOrg_auth())) {
                datas.get(i).setCheck(true);
                selectPos = i;
            } else {
                datas.get(i).setCheck(false);
            }
        }
        adapter = new MineOrganizationEditAdapter(
                this, datas, R.layout.item_mine_organization,
                new ListItemClickHelp() {
                    @Override
                    public void onClickItem(int position) {
                        selectPos = position;
                        for (int i = 0; i < datas.size(); i++) {
                            if (i == position) {
                                datas.get(i).setCheck(true);
                            } else {
                                datas.get(i).setCheck(false);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        lv_teacher_list.setAdapter(adapter);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_org_teacher_list);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_teacher_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
            case R.id.head_right_relative://转让
                if (datas == null) {
                    return;
                }
                int dPos = -1;
                for (int i = 0; i < datas.size(); i++) {
                    if ("2".equals(datas.get(i).getOrg_auth())) {
                        dPos = i;
                        break;
                    }
                }
                if (dPos == selectPos) {
                    UIUtils.showToastSafe(getString(R.string.toast_the_currently_selected_for_administrators));
                } else {
                    getHttpUtils();
                }

                break;
        }
    }

    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "update_user_auth");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("to_uid", datas.get(selectPos).getId());//被改权限人的ID
        params.addBodyParameter("auth", "2");//2=管理员 （转让）

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
        CommonResultBean resultData = GsonTools.fromJson(result, CommonResultBean.class);
        if (resultData == null) {
            return;
        }
        if ("1".equals(resultData.getCode())) {
            SharedPreferencesUtils.saveString(getApplicationContext(),
                    getResources().getString(R.string.shared_org_auth),
                    "1");
            UIUtils.showToastSafe(resultData.getMsg());
            this.setResult(RESULT_CODE_FROM_ORGANIZATION_TEACHER_LIST_ACT);
            finish();
        } else {
            UIUtils.showToastSafe(resultData.getMsg());
        }
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
            if (datas != null) {
                datas.clear();
            } else {
                datas = new ArrayList<>();
            }
            datas = memberBean.getData();
            for (int i = 0; i < datas.size(); i++) {
                if ("2".equals(datas.get(i).getOrg_auth())) {
                    datas.get(i).setCheck(true);
                    selectPos = i;
                } else {
                    datas.get(i).setCheck(false);
                }
            }
            adapter.updateDatas(datas);
        } else {
            UIUtils.showToastSafe(memberBean.getMsg());
        }
    }

    @Override
    public void onBackPressed() {
        if (haveUpdate) {
            Intent intent = new Intent();
            intent.putExtra(MemberDetailActivity.ACTION_BACK_FLAG, true);
            setResult(MemberDetailActivity.RESULT_CODE_FROM_MEMBER_BACK_ACT, intent);
        }
        finish();
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
        if (resultCode == 1) {
            haveUpdate = true;
            getHttpUtils2();
        }
    }
}
