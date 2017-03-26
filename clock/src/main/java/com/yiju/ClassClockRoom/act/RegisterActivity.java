package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.UserVerifyInfo;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.InputValidate;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.circular.CircularProgressButton;

/**
 * ----------------------------------------
 * 注释: 注册
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/6/6 14:18
 * ----------------------------------------
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {

    /**
     * 退出按钮
     */
    @ViewInject(R.id.rl_register_back)
    private RelativeLayout rl_register_back;
    /**
     * 用户名输入框
     */
    @ViewInject(R.id.et_register_username)
    private EditText et_register_username;
    /**
     * 验证码输入框
     */
    @ViewInject(R.id.et_register_verify)
    private EditText et_register_verify;
    /**
     * 获取验证码
     */
    @ViewInject(R.id.btn_get_verify)
    private Button btn_get_verify;
    /**
     * 下一步
     */
    @ViewInject(R.id.circular_button_next)
    private CircularProgressButton circular_button_next;
    /**
     * 协议文案
     */
    @ViewInject(R.id.tv_protocol)
    private TextView tv_protocol;
    /**
     * 同意协议文案
     */
    @ViewInject(R.id.tv_register_agree)
    private TextView tv_register_agree;
    /**
     * 删除按钮
     */
    @ViewInject(R.id.iv_register_delete)
    private ImageView iv_register_delete;

    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 验证码
     */
    private String verifyNumber;
    /**
     * 标题
     */
    private String title;
    /**
     * 注册类型
     */
    private String type;
    /**
     * 计时器
     */
    private TimeCount timeCount;

    @Override
    public int setContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (intent != null) {
            title = getIntent().getStringExtra("title");
        }
    }

    @Override
    public void initView() {
        if (BaseApplication.ImmersionFlag) {
            tintManager.setTintAlpha(0);
        }
        timeCount = new TimeCount(60000, 1000);
    }

    @Override
    public void initListener() {
        super.initListener();

        rl_register_back.setOnClickListener(this);
        btn_get_verify.setOnClickListener(this);
        circular_button_next.setOnClickListener(this);
        circular_button_next.setClickable(false);
        tv_protocol.setOnClickListener(this);
        iv_register_delete.setOnClickListener(this);
        et_register_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String contents = s.toString();
                int length = contents.length();

                if (!"".equals(contents)) {
                    iv_register_delete.setVisibility(View.VISIBLE);
                    circular_button_next.setClickable(true);
                    circular_button_next.setTextColor(UIUtils.getColor(R.color.white));
                } else {
                    iv_register_delete.setVisibility(View.GONE);
                    circular_button_next.setClickable(false);
                    circular_button_next.setTextColor(UIUtils.getColor(R.color.color_lucency_white));
                }

                if (length == 4) {
                    if (contents.substring(3).equals(" ")) {
                        contents = contents.substring(0, 3);
                        et_register_username.setText(contents);
                        et_register_username.setSelection(contents.length());
                    } else {
                        contents = contents.substring(0, 3) + " " + contents.substring(3);
                        et_register_username.setText(contents);
                        et_register_username.setSelection(contents.length());
                    }
                } else if (length == 9) {
                    if (contents.substring(8).equals(" ")) { // -
                        contents = contents.substring(0, 8);
                        et_register_username.setText(contents);
                        et_register_username.setSelection(contents.length());
                    } else {// +
                        contents = contents.substring(0, 8) + " " + contents.substring(8);
                        et_register_username.setText(contents);
                        et_register_username.setSelection(contents.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        if (getString(R.string.label_forget_password).equals(title)) {
            type = "2";
            //隐藏协议等
            tv_protocol.setVisibility(View.GONE);
            tv_register_agree.setVisibility(View.GONE);
        } else {
            type = "1";
        }
    }

    /**
     * 请求手机号是否被注册
     */
    private void getHttpUtils(final String phoneNumber) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "register_prev");
        params.addBodyParameter("username", phoneNumber);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result, phoneNumber);
                    }
                });
    }

    private void processData(String result, String phoneNumber) {
        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
                UserVerifyInfo.class);
        if (userVerifyInfo == null) {
            return;
        }
        if (title.equals(getString(R.string.label_forget_password))) {
            if ("1".equals(userVerifyInfo.getCode())) {
                UIUtils.showToastSafe(getString(R.string.toast_the_user_is_not_registered));
            } else {
                //获取验证码请求
                timeCount.start();
                getHttpUtils2(phoneNumber, type);
            }
        } else {
            if ("1".equals(userVerifyInfo.getCode())) {
                //获取验证码请求
                timeCount.start();
                getHttpUtils2(phoneNumber, type);
            } else {
                UIUtils.showToastSafe(getString(R.string.toast_user_already_exists));
            }
        }
    }

    /**
     * 获取验证码
     *
     * @param phoneNumber 手机号
     * @param type        注册:1,忘记密码:2
     */
    private void getHttpUtils2(String phoneNumber, String type) {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "sms_send");
        params.addBodyParameter("mobile", phoneNumber);
        params.addBodyParameter("type", type);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
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
        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
                UserVerifyInfo.class);
        if (userVerifyInfo == null) {
            return;
        }
        if (!userVerifyInfo.getCode().equals("1")) {
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
        }
    }


    /**
     * 验证验证码请求
     *
     * @param phoneNumber  手机号
     * @param verifyNumber 验证码
     */
    private void getHttpUtils3(final String phoneNumber, String verifyNumber) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "verify_code");
        params.addBodyParameter("username", phoneNumber);
        params.addBodyParameter("code", verifyNumber);
        params.addBodyParameter("type", type);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData3(arg0.result, phoneNumber);
                    }
                });
    }

    private void processData3(String result, String phoneNumber) {
        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
                UserVerifyInfo.class);
        if (userVerifyInfo == null) {
            return;
        }
        if (userVerifyInfo.getCode().equals("1")) {
            Intent intent = new Intent(this, PasswordActivity.class);
            intent.putExtra("phoneNumber", phoneNumber);//手机号
            intent.putExtra("title", title);//标题
            startActivity(intent);
        } else {
            UIUtils.showToastSafe(getString(R.string.toast_verification_code_error));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_register_back://返回
                finish();
                break;
            case R.id.iv_register_delete://删除
                et_register_username.setText("");
                break;
            case R.id.btn_get_verify:
                //获取验证码
                phoneNumber = et_register_username.getText().toString().replaceAll(" ", "");
                if (!"".equals(phoneNumber) && phoneNumber.length() == 11 &&
                        InputValidate.checkedIsTelephone(phoneNumber)) {
                    //这个请求是查看用户是否被注册
                    getHttpUtils(phoneNumber);
                } else {
                    UIUtils.showToastSafe(R.string.toast_enter_a_correct_phone_number_format);
                }
                break;

            case R.id.circular_button_next:
                //下一步,验证验证码
                phoneNumber = et_register_username.getText().toString().replaceAll(" ", "");
                verifyNumber = et_register_verify.getText().toString();

                if (!"".equals(phoneNumber) && phoneNumber.length() == 11 &&
                        InputValidate.checkedIsTelephone(phoneNumber)) {
                    if (!"".equals(verifyNumber) && verifyNumber.length() == 4) {
                        //验证验证码
                        getHttpUtils3(phoneNumber, verifyNumber);
                    } else {
                        UIUtils.showToastSafe(getString(R.string.toast_enter_correct_verification_code));
                    }
                } else {
                    UIUtils.showToastSafe(R.string.toast_enter_a_correct_phone_number_format);
                }
                break;
            case R.id.tv_protocol://用户协议
                Intent intent = new Intent(this, Common_Show_WebPage_Activity.class);
                intent.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.WEB_Int_UserAgreement_Page);//页面page
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 计时器类
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btn_get_verify.setText(UIUtils.getString(R.string.get_verify));
            btn_get_verify.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btn_get_verify.setClickable(false);
            btn_get_verify.setText(String.format(UIUtils.getString(R.string.second),
                    String.valueOf(millisUntilFinished / 1000)));
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


    @Override
    public String getPageName() {
        if (title.equals(getString(R.string.label_forget_password))) {
            return getString(R.string.title_act_register_forgetpassword);
        } else {
            return getString(R.string.title_act_register);
        }
    }
}
