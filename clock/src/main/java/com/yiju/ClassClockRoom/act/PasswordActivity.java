package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

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
import com.yiju.ClassClockRoom.bean.UserVerifyInfo;
import com.yiju.ClassClockRoom.bean.result.UserInfo;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.InputValidate;
import com.yiju.ClassClockRoom.util.MD5;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.circular.CircularProgressButton;

/**
 * ----------------------------------------
 * 注释:修改密码
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/6/6 14:35
 * ----------------------------------------
 */
public class PasswordActivity extends BaseActivity implements OnClickListener,
        OnCheckedChangeListener {
    /**
     * 退出按钮
     */
    @ViewInject(R.id.rl_password_back)
    private RelativeLayout rl_password_back;
    /**
     * 密码输入框
     */
    @ViewInject(R.id.et_password)
    private EditText et_password;
    /**
     * 显示密码
     */
    @ViewInject(R.id.cb_password_is_show)
    private CheckBox cb_password_is_show;
    /**
     * 立即登录按钮
     */
    @ViewInject(R.id.circular_immediately_login)
    private CircularProgressButton circular_immediately_login;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String password;
    /**
     * 标题
     */
    private String title;
    //手机管理
    private TelephonyManager tm;
    //cid
    private String cid;
    //动画
    private Animation shake;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //立即登录
            password = et_password.getText().toString();
            if (InputValidate.checkPassword(password)) {
                if (getString(R.string.label_forget_password).equals(title)) {
                    // 通过短信修改密码
                    getHttpUtils2();
                } else {
                    // 注册
                    getHttpUtils();
                }
            } else {
                UIUtils.showToastSafe(getString(R.string.toast_input_rule));
                circular_immediately_login_fail();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public int setContentViewId() {
        return R.layout.activity_password;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (intent != null) {
            title = getIntent().getStringExtra("title");
            phoneNumber = getIntent().getStringExtra("phoneNumber");
        }
    }

    @Override
    public void initView() {
        if (BaseApplication.ImmersionFlag) {
            tintManager.setTintAlpha(0);
        }
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        cid = SharedPreferencesUtils.getString(PasswordActivity.this,
                SharedPreferencesConstant.Shared_Login_Cid, "");
        shake = AnimationUtils.loadAnimation(PasswordActivity.this, R.anim.shake_x);
    }

    @Override
    public void initListener() {
        super.initListener();
        circular_immediately_login.setClickable(false);
        rl_password_back.setOnClickListener(this);
        circular_immediately_login.setIndeterminateProgressMode(true);
        circular_immediately_login.setOnClickListener(this);
        cb_password_is_show.setOnCheckedChangeListener(this);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String contents = s.toString();

                if (!"".equals(contents)) {
                    circular_immediately_login.setClickable(true);
                    circular_immediately_login.setTextColor(UIUtils.getColor(R.color.white));
                } else {
                    circular_immediately_login.setClickable(false);
                    circular_immediately_login.setTextColor(UIUtils.getColor(R.color.color_lucency_white));
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
            case R.id.rl_password_back://返回
                finish();
                break;
            case R.id.circular_immediately_login://立即登录
                circular_immediately_login.setProgress(50);
                Message m = new Message();
                mHandler.sendMessageDelayed(m, 1000);
                circular_immediately_login.setClickable(false);


//                //立即登录
//                password = et_password.getText().toString();
//                if (InputValidate.checkPassword(password)) {
//                    if ("忘记密码".equals(title)) {
//                        // 通过短信修改密码
//                        getHttpUtils2();
//                    } else {
//                        // 注册
//                        getHttpUtils();
//                    }
//                } else {
//                    UIUtils.showToastSafe("请输入6-18位字母或数字");
//                }
                break;

            default:
                break;
        }
    }

    public void circular_immediately_login_fail() {
        circular_immediately_login.setProgress(0);
        circular_immediately_login.startAnimation(shake);
        circular_immediately_login.setClickable(true);
    }


    /**
     * 修改密码接口
     */
    private void getHttpUtils2() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "modify_phone_password");
        params.addBodyParameter("phone", phoneNumber);
        params.addBodyParameter("password", password);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        circular_immediately_login_fail();

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                });
    }

    /**
     * 注册接口
     */
    @SuppressLint("HardwareIds")
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "register");
        params.addBodyParameter("username", phoneNumber);
        params.addBodyParameter("password", password);
        params.addBodyParameter("device_token", tm.getDeviceId());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        circular_immediately_login_fail();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                });
    }

    private void processData(String result) {
        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
                UserVerifyInfo.class);
        if (userVerifyInfo == null) {
            return;
        }
        if (userVerifyInfo.getCode().equals("1")) {
            CountControl.getInstance().registerSuccess(userVerifyInfo.getUid() + "", CountControl.Register_Type_Phone);
            // 注册或者修改密码成功后登陆
            getHttpUtils3(phoneNumber, password);
        } else {
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
            circular_immediately_login_fail();
        }
    }

    /**
     * 登录请求
     *
     * @param phoneNumber 手机号
     * @param password    密码
     */
    @SuppressLint("HardwareIds")
    private void getHttpUtils3(String phoneNumber, String password) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "login");
        params.addBodyParameter("username", phoneNumber);
        params.addBodyParameter("password", password);
        params.addBodyParameter("device_token", tm.getDeviceId());
        if (StringUtils.isNotNullString(cid)) {
            params.addBodyParameter("cid", cid);
        }

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        circular_immediately_login_fail();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData3(arg0.result);

                    }
                });

    }

    private void processData3(String result) {
        UserInfo use = GsonTools.changeGsonToBean(result, UserInfo.class);
        if (use == null) {
            return;
        }
        UserInfo.Data userInfo = use.getData();
        if (userInfo == null) {
            return;
        }
        CountControl.getInstance().loginSuccess(userInfo.getId());
        //写入登录成功
        SharedPreferencesUtils.saveBoolean(getApplicationContext(),
                getResources().getString(R.string.shared_isLogin), true);

        // 成功获取到用户信息，写入本地
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_id), userInfo.getId());

        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_username), phoneNumber);
        try {
            String mD5password = MD5.md5(password);
            SharedPreferencesUtils.saveString(getApplicationContext(),
                    getResources().getString(R.string.shared_password), mD5password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_third_source), "0");
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_org_auth),
                userInfo.getOrg_auth());
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_nickname),
                userInfo.getNickname());
        SharedPreferencesUtils.saveString(this,
                getResources().getString(R.string.shared_mobile), userInfo.getMobile());


        circular_immediately_login.setProgress(0);
        circular_immediately_login.setClickable(true);

        // 登录成功后关闭页面
        Intent intent = new Intent(this, PersonalCenterActivity.class);
        startActivity(intent);
    }

    //显示隐藏密码
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isChecked()) {
            et_password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            et_password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
        et_password.postInvalidate();
        // 切换后将EditText光标置于末尾
        CharSequence charSequence = et_password.getText();
        if (charSequence != null) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
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
        return getString(R.string.title_act_password);
    }

}
