package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.InputValidate;
import com.yiju.ClassClockRoom.util.MD5;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * ----------------------------------------
 * 注释: 修改密码类
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/1/20 10:56
 * ----------------------------------------
 */
public class PersonalCenter_ChangePasswordActivity extends BaseActivity
        implements OnClickListener {

    /**
     * 退出按钮
     */
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    @ViewInject(R.id.head_title)
    private TextView head_title;
    /**
     * 原始密码
     */
    @ViewInject(R.id.et_old_password)
    private EditText et_old_password;
    /**
     * 新密码
     */
    @ViewInject(R.id.et_new_password)
    private EditText et_new_password;
    /**
     * 再次输入新密码
     */
    @ViewInject(R.id.et_new_password_check)
    private EditText et_new_password_check;
    /**
     * 提交
     */
    @ViewInject(R.id.btn_commit)
    private Button btn_commit;

    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_password;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.person_information_modify_password));
        head_back_relative.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        hideCommitButton();

        et_old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!"".equals(s.toString())
                        && !"".equals(et_new_password.getText().toString())
                        && !"".equals(et_new_password_check.getText().toString())){
                    showCommitButton();
                }else{
                    hideCommitButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!"".equals(s.toString())
                        && !"".equals(et_old_password.getText().toString())
                        && !"".equals(et_new_password_check.getText().toString())){
                    showCommitButton();
                }else{
                    hideCommitButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_new_password_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!"".equals(s.toString())
                        && !"".equals(et_new_password.getText().toString())
                        && !"".equals(et_old_password.getText().toString())){
                    showCommitButton();
                }else{
                    hideCommitButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                this.finish();
                break;
            case R.id.btn_commit://保存

                if (!et_new_password.getText().toString()
                        .equals(et_new_password_check.getText().toString())) {
                    UIUtils.showToastSafe(getString(R.string.toast_enter_same_password_twice));
                } else if (et_old_password.getText().toString()
                        .equals(et_new_password_check.getText().toString())) {
                    UIUtils.showToastSafe(getString(R.string.toast_old_and_new_password_cannot_be_consistent));
                } else if (InputValidate.checkPassword(et_new_password.getText()
                        .toString())) {
                    // 请求
                    getHttpUtils();
                } else {
                    UIUtils.showToastSafe(getString(R.string.toast_password_input_rule));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 修改密码请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "modifyPass");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("pwd", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("password", et_new_password_check.getText().toString());
        params.addBodyParameter("old_password", et_old_password.getText()
                .toString());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
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
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            UIUtils.showToastSafe(getString(R.string.toast_password_update_success));
            try {
                String mD5password = MD5.md5(et_new_password_check.getText().toString().trim());
                SharedPreferencesUtils.saveString(getApplicationContext(),
                        getResources().getString(R.string.shared_password), mD5password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.finish();
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }


    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center_change_password);
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

    private void hideCommitButton() {
        if (StringUtils.isNullString(et_old_password.getText().toString())
                || StringUtils.isNullString(et_new_password.getText().toString())
                || StringUtils.isNullString(et_new_password_check.getText().toString())){
            btn_commit.setBackgroundResource(R.drawable.background_gray_dddddd_radius_70);
            btn_commit.setEnabled(false);
        }
    }

    private void showCommitButton() {
        if (StringUtils.isNotNullString(et_old_password.getText().toString())
                && StringUtils.isNotNullString(et_new_password.getText().toString())
                && StringUtils.isNotNullString(et_new_password_check.getText().toString())){
            btn_commit.setBackgroundResource(R.drawable.background_green_1eb482_radius_70);
            btn_commit.setEnabled(false);
        }
    }
}
