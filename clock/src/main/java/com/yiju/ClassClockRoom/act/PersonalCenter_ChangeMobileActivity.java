package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.UserVerifyInfo;
import com.yiju.ClassClockRoom.control.FailCodeControl;
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
 * 注释: 更换手机第二部
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/1/21 15:46
 * ----------------------------------------
 */
public class PersonalCenter_ChangeMobileActivity extends BaseActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
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
     * 输入新手机号
     */
    @ViewInject(R.id.et_change_mobile_new_mobile)
    private EditText et_change_mobile_new_mobile;
    /**
     * 输入验证码
     */
    @ViewInject(R.id.et_change_mobile_verify)
    private EditText et_change_mobile_verify;
    /**
     * 获取验证码按钮
     */
    @ViewInject(R.id.btn_change_mobile_get_verify)
    private Button btn_change_mobile_get_verify;
    /**
     * 更换手机按钮
     */
    @ViewInject(R.id.btn_change_mobile_commit)
    private CircularProgressButton btn_change_mobile_commit;
    /**
     * 密码输入布局
     */
    @ViewInject(R.id.ly_change_mobile_password)
    private LinearLayout ly_change_mobile_password;
    /**
     * 密码输入框
     */
    @ViewInject(R.id.et_change_mobile_password)
    private EditText et_change_mobile_password;
    /**
     * 密码是否显示隐藏
     */
    @ViewInject(R.id.cb_change_mobile_password_isshow)
    private CheckBox cb_change_mobile_password_isshow;
    //分割线
    @ViewInject(R.id.v_divider)
    private View v_divider;
    //文案信息
    @ViewInject(R.id.tv_mobile)
    private TextView tv_mobile;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 验证码
     */
    private String verifyCode;
    /**
     * 计时类
     */
    private TimeCount timeCount;

    /**
     * 原手机号
     */
    private String originalPhoneNumber;
    /**
     * 请求类型
     */
    private int type;
    /**
     * 密码
     */
    private String password;

    /**
     * 三方qq信息
     */
    private String shared_third_qq;
    /**
     * 三方微信信息
     */
    private String shared_third_wechat;
    /**
     * 三方微博信息
     */
    private String shared_third_weibo;


    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_change_mobile;
    }

    @Override
    public void initView() {

//                手机用户-->有手机-->发送验证码-->验证验证码-->修改手机成功modify_phone type1
//                三方用户-->无手机-->跳third_bind_mobile,显示密码 type2
//                三方用户-->有手机-->跳third_bind_mobile,隐藏密码 type3

        originalPhoneNumber = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_mobile), "");
        shared_third_qq = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_qq), "");

        shared_third_wechat = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_wechat), "");

        shared_third_weibo = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_weibo), "");

        if (originalPhoneNumber != null && !"".equals(originalPhoneNumber)) {
            //有手机号
            //判断是否是第三方绑定,
            if ("".equals(shared_third_qq) && "".equals(shared_third_wechat) && "".equals(shared_third_weibo)) {
                //无绑定过三方账号
                type = 1;
            } else {
                //有绑定过三方账号
                type = 3;
            }
        } else {
            //无手机号,
            type = 2;
        }
//        type = getIntent().getIntExtra("type", 0);
//        originalPhoneNumber = getIntent().getStringExtra("mobile");
        if (originalPhoneNumber != null && !"".equals(originalPhoneNumber)) {
            head_title.setText(getResources().getString(R.string.person_modify_mobile));
            tv_mobile.setText(UIUtils.getString(R.string.change_mobile_content));
        } else {
            head_title.setText(R.string.title_binding_mobile_phone_number);
            tv_mobile.setText(UIUtils.getString(R.string.bind_mobile_content));
        }

        timeCount = new TimeCount(60000, 1000);
        head_back_relative.setOnClickListener(this);
        btn_change_mobile_get_verify.setOnClickListener(this);
        btn_change_mobile_commit.setOnClickListener(this);
        //更换按钮不可按
        btn_change_mobile_commit.setEnabled(false);
        cb_change_mobile_password_isshow.setOnCheckedChangeListener(this);

        //隐藏密码布局
        if (type != 2) {
            ly_change_mobile_password.setVisibility(View.GONE);
            v_divider.setVisibility(View.GONE);
        }

    }

    @Override
    public void initData() {
        et_change_mobile_verify.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                verifyCode = s.toString();
                if (s.length() > 0
                        && StringUtils.isNotNullString(et_change_mobile_new_mobile.getText().toString())) {
                    //更换按钮可以按
                    btn_change_mobile_commit.setEnabled(true);
                } else {
                    //更换按钮不可按
                    btn_change_mobile_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        et_change_mobile_new_mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s != null
                        && StringUtils.isNotNullString(et_change_mobile_verify.getText().toString())) {
                    btn_change_mobile_commit.setEnabled(true);
                }else {
                    btn_change_mobile_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        et_change_mobile_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s != null) {
                    password = s.toString();
                    btn_change_mobile_commit.setEnabled(true);
                }else {
                    btn_change_mobile_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

    }

    // 4个请求，获取验证码请求，核对验证码是否正确请求，修改手机请求,第三方绑定手机请求
    // 改成3个请求，取消验证短信
    /*
     * 获取验证码
     */
    private void getHttpUtils() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "sms_send");
        params.addBodyParameter("mobile", phoneNumber);
        params.addBodyParameter("type", "3");

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
        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
                UserVerifyInfo.class);
        if (userVerifyInfo == null) {
            return;
        }
        if (!"1".equals(userVerifyInfo.getCode())) {
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
        } else {
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
            timeCount.start();
        }
    }

    /**
     * 更换手机请求
     */
    private void getHttpUtils3() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "modifyPhone");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("phone", phoneNumber);
        params.addBodyParameter("code", verifyCode);
        params.addBodyParameter("url", UrlUtils.SERVER_USER_API);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());


        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
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
        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
                UserVerifyInfo.class);
        if (userVerifyInfo == null) {
            return;
        }
        if (userVerifyInfo.getCode().equals("1")) {
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
            SharedPreferencesUtils.saveString(this,
                    getResources().getString(R.string.shared_mobile),
                    phoneNumber);
            setResult(4);
            finish();
        } else {
            FailCodeControl.checkCode(userVerifyInfo.getCode());
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
        }

    }


    /**
     * 三方账号绑定手机号
     *
     * @param phoneNumber 手机号
     * @param verifyCode  验证码
     */
    private void getHttpUtils4(String phoneNumber, String verifyCode) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "third_bind_mobile");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", phoneNumber);
        params.addBodyParameter("code", verifyCode);
        params.addBodyParameter("type", "3");
        //有密码
        if (type == 2) {
            params.addBodyParameter("password", password);
        }
        params.addBodyParameter("url", UrlUtils.SERVER_USER_API);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
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
                });
    }

    private void processData4(String result) {
        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
                UserVerifyInfo.class);
        if (userVerifyInfo == null) {
            return;
        }
        if ("1".equals(userVerifyInfo.getCode())) {
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
            SharedPreferencesUtils.saveString(this,
                    getResources().getString(R.string.shared_mobile),
                    phoneNumber);
            String mD5password = "";
            try {
                mD5password = MD5.md5(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferencesUtils.saveString(getApplicationContext(),
                    getResources().getString(R.string.shared_password), mD5password);
            setResult(4);
            finish();
        } else {
            FailCodeControl.checkCode(userVerifyInfo.getCode());
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_068");
                finish();
                break;
            case R.id.btn_change_mobile_get_verify://获取验证码
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_069");
                phoneNumber = et_change_mobile_new_mobile.getText().toString();
                if (!phoneNumber.equals("") && phoneNumber.length() == 11 && InputValidate.checkedIsTelephone(phoneNumber)) {

                    getHttpUtils();

                } else {
                    UIUtils.showToastSafe(getString(R.string.toast_enter_a_correct_phone_number_format));
                }
                break;
            case R.id.btn_change_mobile_commit: //更换手机
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_070");
                phoneNumber = et_change_mobile_new_mobile.getText().toString();
                if (null != phoneNumber && !phoneNumber.equals("")
                        && phoneNumber.length() == 11
                        && InputValidate.checkedIsTelephone(phoneNumber)) {
//                手机用户-->有手机-->发送验证码-->验证验证码-->修改手机成功modify_phone type1
//                三方用户-->无手机-->跳third_bind_mobile,显示密码 type2
//                三方用户-->有手机-->跳third_bind_mobile,隐藏密码 type3
                    if (type == 1) {
                        //修改:取消验证短信步骤
                        getHttpUtils3();
                    } else if (type == 2) {
                        //判断密码
                        if (InputValidate.checkPassword(password)) {
                            getHttpUtils4(phoneNumber, verifyCode);
                        } else {
                            UIUtils.showToastSafe(getString(R.string.toast_input_rule_password));
                        }
                    } else if (type == 3) {
                        getHttpUtils4(phoneNumber, verifyCode);
                    }
                } else {
                    UIUtils.showToastSafe(R.string.toast_enter_a_correct_phone_number_format);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center_mobile_binding);
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.isChecked()) {
            et_change_mobile_password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            et_change_mobile_password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btn_change_mobile_get_verify.setText(R.string.txt_access_to_verify);
            btn_change_mobile_get_verify.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btn_change_mobile_get_verify.setClickable(false);
            btn_change_mobile_get_verify.setText(String.format(UIUtils.getString(R.string.second), String.valueOf(millisUntilFinished / 1000)));
        }
    }


}
