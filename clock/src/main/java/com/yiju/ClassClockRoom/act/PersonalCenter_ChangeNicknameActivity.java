package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.control.FailCodeControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.InputValidate;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * --------------------------------------
 * <p/>
 * 注释:修改昵称
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-23 上午10:31:48
 * <p/>
 * --------------------------------------
 */
public class PersonalCenter_ChangeNicknameActivity extends BaseActivity
        implements OnClickListener, OnFocusChangeListener {

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
     * 保存按钮
     */
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    /**
     * 保存文案
     */
    @ViewInject(R.id.bt_nickname_confirm_edit)
    private Button bt_nickname_confirm_edit;
    /**
     * 修改名字输入框
     */
    @ViewInject(R.id.et_change_nickname)
    private EditText et_change_nickname;

    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_nickname;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.label_nickname));
        head_back_relative.setOnClickListener(this);
        bt_nickname_confirm_edit.setOnClickListener(this);
        et_change_nickname.setOnFocusChangeListener(this);

    }

    @Override
    public void initData() {
        et_change_nickname.setText(SharedPreferencesUtils.getString(
                getApplicationContext(),
                getResources().getString(R.string.shared_nickname), "设置个昵称呗"));
    }

    /**
     * 修改昵称请求
     */
    private void getHttpUtils() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "modifyInfo");
        params.addBodyParameter("type", "nickname");
        params.addBodyParameter("value", et_change_nickname.getText()
                .toString());
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("url", UrlUtils.SERVER_USER_API);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());

        httpUtils.send(HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
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
            UIUtils.showToastSafe(getString(R.string.toast_update_nickname_success));
            SharedPreferencesUtils.saveString(getApplicationContext(),
                    getResources().getString(R.string.shared_nickname),
                    et_change_nickname.getText().toString());
            this.setResult(1);
            this.finish();
        } else {
            FailCodeControl.checkCode(mineOrder.getCode());
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_064");
                this.finish();
                break;
            case R.id.bt_nickname_confirm_edit://保存
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_065");
                if (InputValidate.checkNickname(et_change_nickname.getText()
                        .toString())) {
                    // 请求后，打印toast，finish
                    getHttpUtils();
                } else {
                    String name = et_change_nickname.getText().toString();
                    if (name.length() < 4 || name.length() > 16) {
                        UIUtils.showToastSafe(getString(R.string.toast_input_correct_nickname_length));
                    } else {
                        UIUtils.showToastSafe(getString(R.string.toast_input_correct_nickname));
                    }

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
                    et_change_nickname.setText("");
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
        return getString(R.string.title_act_personal_edite_nickname);
    }

}
