package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.LoginBean;
import com.yiju.ClassClockRoom.bean.UserVerifyInfo;
import com.yiju.ClassClockRoom.bean.WorkingPayResult;
import com.yiju.ClassClockRoom.bean.result.LoginResult;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.InputValidate;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.ClearEditText;
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
public class PasswordActivity extends BaseActivity implements OnClickListener {
    //退出按钮
    @ViewInject(R.id.rl_password_back)
    private RelativeLayout rl_password_back;
    //密码输入框
    @ViewInject(R.id.et_password)
    private ClearEditText et_password;
    //确认密码输入框
    @ViewInject(R.id.et_password_again)
    private ClearEditText et_password_again;
    //立即登录按钮
    @ViewInject(R.id.circular_immediately_login)
    private CircularProgressButton circular_immediately_login;
    //手机号
    private String phoneNumber;
    //密码
    private String password = "";
    //确认密码
    private String password_again = "";
    //标题
    private String title;
    //手机管理
    private TelephonyManager tm;
    //cid
    private String cid;
    //游云id
    private String youYunUDid;
    //动画
    private Animation shake;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //立即登录
            if (InputValidate.checkPassword(password)) {
                if (getString(R.string.label_forget_password).equals(title)) {
                    // 通过短信修改密码
                    getHttpUtils2();
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
        youYunUDid = SharedPreferencesUtils.getString(PasswordActivity.this,
                SharedPreferencesConstant.Shared_Login_Udid, "");
        shake = AnimationUtils.loadAnimation(PasswordActivity.this, R.anim.shake_x);
    }

    @Override
    public void initListener() {
        super.initListener();
        rl_password_back.setOnClickListener(this);
        circular_immediately_login.setIndeterminateProgressMode(true);
        circular_immediately_login.setOnClickListener(this);
        circular_immediately_login.setEnabled(false);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
                if (password.length() >= 6 && password.length() <= 18
                        && password_again.length() >= 6 && password_again.length() <= 18) {
                    circular_immediately_login.setEnabled(true);
                } else {
                    circular_immediately_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_password_again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password_again = s.toString();
                if (password.length() >= 6 && password.length() <= 18
                        && password_again.length() >= 6 && password_again.length() <= 18) {
                    circular_immediately_login.setEnabled(true);
                } else {
                    circular_immediately_login.setEnabled(false);
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
                if (!password.equals(password_again)) {
                    UIUtils.showToastSafe("两次密码输入不一致!");
                    return;
                }
                circular_immediately_login.setProgress(50);
                Message m = new Message();
                mHandler.sendMessageDelayed(m, 1000);
                circular_immediately_login.setClickable(false);
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
            params.addBodyParameter("cid", cid);//个推推送码
        }
        if (StringUtils.isNotNullString(youYunUDid)) {
            params.addBodyParameter("udid", youYunUDid);//游云推送码
        }

        httpUtils.send(HttpMethod.POST, UrlUtils.JAVA_LOGIN, params,
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
        LoginBean loginBean = GsonTools.changeGsonToBean(result, LoginBean.class);
        if (loginBean == null) {
            return;
        }
        if ("0".equals(loginBean.getCode())) {
            //友盟账号统计
            MobclickAgent.onProfileSignIn(loginBean.getObj().getUserId());
            // Login success
            parseUserInfo(loginBean.getObj());
            //获取支付所需的工作密钥
            getWorkingKey(loginBean.getObj().getUserId());
            // 登陆成功返回()
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(ExtraControl.EXTRA_IS_LOGIN, true);
            startActivity(intent);
        } else {
            UIUtils.showToastSafe(loginBean.getMsg());
        }
        circular_immediately_login.setProgress(0);
        circular_immediately_login.setClickable(true);
    }

    /**
     * 完成解析后处理
     */
    private void parseUserInfo(LoginResult loginResult) {
        //统计
        CountControl.getInstance().loginSuccess(loginResult.getUserId());
        //写入登录成功
        SharedPreferencesUtils.saveBoolean(getApplicationContext(),
                getResources().getString(R.string.shared_isLogin), true);
        // 成功获取到用户信息，写入本地
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_id), loginResult.getUserId());
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_session_id), loginResult.getSessionId());
        SharedPreferencesUtils.saveString(this,
                getResources().getString(R.string.shared_existpassword),
                "1");
    }

    /**
     * 请求工作密钥api
     *
     * @param uid 用户id
     */
    private void getWorkingKey(String uid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getWorkingKey");
        params.addBodyParameter("uid", uid);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_PAY, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        WorkingPayResult workingPayResult = GsonTools.changeGsonToBean(arg0.result, WorkingPayResult.class);
                        if (workingPayResult == null) {
                            return;
                        }
                        if ("1".equals(workingPayResult.getCode())) {
                            //存储易支付初始化所需的工作密钥等参数
                            EjuPaySDKUtil.saveEjuPaySdkParam(workingPayResult);
                            //易支付初始化
                            EjuPaySDKUtil.initEjuPaySDK(null);
                        }
                    }
                });
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
