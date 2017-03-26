package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
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
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * ----------------------------------------
 * 注释: 修改邮箱类
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/1/20 10:51
 * ----------------------------------------
 */
public class PersonalCenter_ChangeEmailActivity extends BaseActivity implements
        OnClickListener, OnFocusChangeListener {
    /**
     * 取消按钮
     */
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    @ViewInject(R.id.head_title)
    private TextView head_title;
    /**
     * 保存文案
     */
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    /**
     * email输入框
     */
    @ViewInject(R.id.et_change_email)
    private EditText et_change_email;
    /**
     * 本地邮箱
     */
    private String local_email;
    /**
     * 从接口获取到的邮箱
     */
    private String email;


    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_email;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.label_modify_mail));
        head_back_relative.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
        et_change_email.setOnFocusChangeListener(this);

        if (!"-1".equals(StringUtils.getUid())) {
            //根据用户id来取出本地邮箱
            local_email = SharedPreferencesUtils.getString(
                    this, "local_email_" + StringUtils.getUid(), "");
        }

        email = SharedPreferencesUtils.getString(this,
                UIUtils.getString(R.string.shared_email), "");
        if ("".equals(email)) {
            head_right_text.setText(R.string.txt_binding);
        } else {
            head_right_text.setText(R.string.txt_change);
        }
    }

    @Override
    public void initData() {
        if (!"".equals(local_email)) {
            //本地邮箱不为空，就设置
            et_change_email.setText(local_email);
        } else {
            //本地邮箱为空，就设置接口返回的邮箱
            et_change_email.setText(email);
        }
    }

    /**
     * 修改邮箱请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "send_mail");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("email", et_change_email.getText().toString());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        head_right_text.setClickable(true);
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
            head_right_text.setClickable(true);
            UIUtils.showToastSafe(mineOrder.getMsg());
            if (!"-1".equals(StringUtils.getUid())) {
                SharedPreferencesUtils.saveString(this,
                        "local_email_" + StringUtils.getUid(),
                        et_change_email.getText().toString());
            }
            this.setResult(2);
            this.finish();
        } else {
            head_right_text.setClickable(true);
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                this.finish();
                break;
            case R.id.head_right_text://保存
                if (InputValidate.checkedIsEmail(et_change_email.getText()
                        .toString())) {
                    head_right_text.setClickable(false);
                    // 请求
                    getHttpUtils();

                } else {
                    UIUtils.showToastSafe(getString(R.string.toast_enter_the_correct_email_format));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.et_change_nickname:
                    et_change_email.setText("");
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                    break;
                default:
                    break;
            }
        } else {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center_change_emial);
    }

}
