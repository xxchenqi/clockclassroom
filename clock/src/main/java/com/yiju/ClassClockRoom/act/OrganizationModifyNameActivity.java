package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.yiju.ClassClockRoom.bean.MineOrganizationBean;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:机构修改名称
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/17 15:21
 * ----------------------------------------
 */
public class OrganizationModifyNameActivity extends BaseActivity implements View.OnClickListener {
    //后退
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //完成和保存
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //保存按钮
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //修改内容
    @ViewInject(R.id.et_organization_modify_content)
    private EditText et_organization_modify_content;
    //字数
    @ViewInject(R.id.tv_organization_modify_content_number)
    private TextView tv_organization_modify_content_number;
    //删除
    @ViewInject(R.id.iv_organization_modify_delete)
    private ImageView iv_organization_modify_delete;
    //机构简介,老师详情,老师资料
    private String title;
    //被修改人的ID
    private String uid;
    private MemberDetailResult bean;
    //最大值
    private int max;
    private String type;
    //区分是否是机构帮成员修改的
    private String title_flag;
    //bean
    private MineOrganizationBean organization_bean;
    //呢人拼命干
    private String content;
    //传参
    public static final String ACTION_TITLE = "title";
    public static final String ACTION_UID = "uid";
    public static final String ACTION_BEAN = "bean";
    public static final String ACTION_TYPE = "type";
    public static final String ACTION_TITLE_FLAG = "title_flag";
    public static final String ACTION_ORGANIZATION_BEAN = "organization_bean";
    public static final String ACTION_CONTENT = "content";

    //修改老师简介
    public static int RESULT_CODE_FROM_ORGANIZATION_MODIFY_BRIEF_ACT = 1000;
    //修改昵称
    public static int RESULT_CODE_FROM_ORGANIZATION_MODIFY_NAME_ACT = 1001;
    //新建标签
    public static int RESULT_CODE_FROM_ORGANIZATION_MODIFY_NEW_TAG_ACT = 1003;
    //修改其他
    public static int RESULT_CODE_FROM_ORGANIZATION_MODIFY_OTHER_ACT = 1004;


    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        uid = intent.getStringExtra("uid");
        bean = (MemberDetailResult) intent.getSerializableExtra("bean");
        type = intent.getStringExtra("type");
        title_flag = intent.getStringExtra("title_flag");
        content = intent.getStringExtra("content");
        organization_bean = (MineOrganizationBean) intent.getSerializableExtra("organization_bean");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        head_title.setText(title);
        if (getString(R.string.txt_institutional_profile).equals(title) ||
                getString(R.string.teacher_brief).equals(title)) {
            head_right_text.setText(R.string.label_finish);
            max = 500;
            tv_organization_modify_content_number.setVisibility(View.VISIBLE);
            tv_organization_modify_content_number.setText("0/500");

            int newHeight = UIUtils.dip2px(125);
            //注意这里，到底是用ViewGroup还是用LinearLayout或者是FrameLayout，主要是看你这个EditTex
            //控件所在的父控件是啥布局，如果是LinearLayout，那么这里就要改成LinearLayout.LayoutParams
            ViewGroup.LayoutParams lp = et_organization_modify_content.getLayoutParams();
            lp.height = newHeight;
            et_organization_modify_content.setLayoutParams(lp);

        } else if (getString(R.string.new_tag).equals(title)) {
            max = 8;
            head_right_text.setText(R.string.label_save);
            tv_organization_modify_content_number.setVisibility(View.VISIBLE);
            tv_organization_modify_content_number.setText("0/6");
        } else {
            //修改名称
            max = 8;
            head_right_text.setText(R.string.label_save);
            tv_organization_modify_content_number.setVisibility(View.GONE);
        }

        if (StringUtils.isNotNullString(content)) {
            et_organization_modify_content.setText(content);
            tv_organization_modify_content_number.setText(content.length() + "/" + max);
            et_organization_modify_content.setSelection(content.length());
            if (!(getString(R.string.txt_institutional_profile).equals(title)
                    || getString(R.string.teacher_brief).equals(title))) {
                iv_organization_modify_delete.setVisibility(View.VISIBLE);
            }
        }

        et_organization_modify_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});

    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        iv_organization_modify_delete.setOnClickListener(this);
        et_organization_modify_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_organization_modify_content_number.setText(s.toString().length() + "/" + max);
                if (s.toString().length() == 0) {
                    iv_organization_modify_delete.setVisibility(View.GONE);
                } else {
                    if (!(getString(R.string.txt_institutional_profile).equals(title)
                            || getString(R.string.teacher_brief).equals(title))) {
                        iv_organization_modify_delete.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public String getPageName() {
        if (getString(R.string.txt_institutional_profile).equals(title)) {
            return getString(R.string.title_act_my_org_edit_brief);
        } else if (getString(R.string.teacher_brief).equals(title)) {
            return getString(R.string.title_act_my_teacher_edit_brief);
        } else if (getString(R.string.new_tag).equals(title)) {
            if (getString(R.string.organization_detail).equals(title_flag)) {
                return getString(R.string.title_act_my_org_new_tag);
            } else if (UIUtils.getString(R.string.member_detail).equals(title_flag)
                    || !UIUtils.getString(R.string.teacher_detail).equals(title_flag)) {
                return getString(R.string.title_act_my_org_member_new_tag);
            } else if (UIUtils.getString(R.string.teacher_detail).equals(title_flag)) {
                return getString(R.string.title_act_my_teacher_new_tag);
            }
        } else if (getString(R.string.modify_name).equals(title)) {
            return getString(R.string.title_act_my_teacher_edit_name);
        }
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_modify_name;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_relative:
                String etText = et_organization_modify_content.getText().toString();
                if ("".equals(etText)) {
                    UIUtils.showToastSafe(getString(R.string.toast_content_can_not_empty));
                    return;
                }
                if (getString(R.string.txt_institutional_profile).equals(title)) {
                    getHttpUtils4();
                } else if (getString(R.string.teacher_brief).equals(title)) {
                    //修改老师简介
                    bean.getData().setInfo(et_organization_modify_content.getText().toString());
                    HttpClassRoomApi.getInstance().askModifyMemberInfo(title_flag, uid, bean, true, false);
                } else if (getString(R.string.modify_name).equals(title)) {
                    //修改老师名称
                    bean.getData().setReal_name(et_organization_modify_content.getText().toString());
                    HttpClassRoomApi.getInstance().askModifyMemberInfo(title_flag, uid, bean, true, false);
                } else if (getString(R.string.new_tag).equals(title)) {
                    if (!"".equals(et_organization_modify_content.getText().toString())) {
                        getHttpUtils3();
                    } else {
                        UIUtils.showToastSafe(R.string.toast_cannot_add_new_people);
                    }
                }
                break;
            case R.id.iv_organization_modify_delete:
                et_organization_modify_content.setText("");
                iv_organization_modify_delete.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        if (event.getType() == DataManager.MODIFY_MEMBER_DATA_OTHER) {
            MemberDetailResult bean = (MemberDetailResult) event.getData();
            if (bean == null) {
                return;
            }
            if ("1".equals(bean.getCode())) {
                UIUtils.showLongToastSafe(UIUtils.getString(R.string.toast_edit_success));
                Intent intent = getIntent();
                if (getString(R.string.teacher_brief).equals(title)) {
                    intent.putExtra("brief_edit", et_organization_modify_content.getText().toString());
                    setResult(RESULT_CODE_FROM_ORGANIZATION_MODIFY_BRIEF_ACT, intent);
                } else if (getString(R.string.modify_name).equals(title)) {
                    intent.putExtra("name_edit", et_organization_modify_content.getText().toString());
                    setResult(RESULT_CODE_FROM_ORGANIZATION_MODIFY_NAME_ACT, intent);
                } else {
                    //修改其他内容
                    setResult(RESULT_CODE_FROM_ORGANIZATION_MODIFY_OTHER_ACT);
                }
                finish();
            } else {
                UIUtils.showToastSafe(bean.getMsg());
            }
        }
    }


    /**
     * 新增标签请求
     */
    private void getHttpUtils3() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "tags_add");
        params.addBodyParameter("tags", et_organization_modify_content.getText().toString());
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("org_uid", StringUtils.getUid());
        }
        params.addBodyParameter("type", type);
        params.addBodyParameter("uid", uid);
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
                        processData3(arg0.result);
                    }
                });
    }

    private void processData3(String result) {
        MemberDetailResult bean = GsonTools.changeGsonToBean(result,
                MemberDetailResult.class);
        if (bean == null) {
            return;
        }
        if ("1".equals(bean.getCode())) {
            Intent intent = new Intent();
            intent.putExtra("tag", et_organization_modify_content.getText().toString());
            setResult(RESULT_CODE_FROM_ORGANIZATION_MODIFY_NEW_TAG_ACT, intent);
            finish();
            UIUtils.showToastSafe(getString(R.string.toast_new_tag_success));
        } else {
            UIUtils.showToastSafe(bean.getMsg());
        }
    }

    /**
     * 机构编辑简介请求
     */
    private void getHttpUtils4() {
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
        params.addBodyParameter("info", et_organization_modify_content.getText().toString());
        params.addBodyParameter("tags", organization_bean.getData().getTags());
        params.addBodyParameter("mien_pic", imgs.toString());
        params.addBodyParameter("uid", uid);
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
                        processData4(arg0.result);
                    }
                }
        );
    }

    private void processData4(String result) {
        CommonResultBean resultData = GsonTools.fromJson(result, CommonResultBean.class);
        if ("1".equals(resultData.getCode())) {
            UIUtils.showToastSafe(R.string.toast_save_success);
            Intent intent = new Intent();
            intent.putExtra("content", et_organization_modify_content.getText().toString());
            this.setResult(RESULT_CODE_FROM_ORGANIZATION_MODIFY_BRIEF_ACT, intent);
            finish();
        } else {
            UIUtils.showToastSafe(resultData.getMsg());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
