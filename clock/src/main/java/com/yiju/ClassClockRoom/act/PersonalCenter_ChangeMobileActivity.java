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
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.UserVerifyInfo;
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

    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_change_mobile;
    }

    @Override
    public void initView() {

        type = getIntent().getIntExtra("type", 0);
        originalPhoneNumber = getIntent().getStringExtra("mobile");
        if (originalPhoneNumber != null && !"".equals(originalPhoneNumber)) {
            head_title.setText(getResources().getString(R.string.person_modify_mobile));
        } else {
            head_title.setText(R.string.title_binding_mobile_phone_number);
            tv_mobile.setVisibility(View.GONE);
        }

        timeCount = new TimeCount(60000, 1000);
        head_back_relative.setOnClickListener(this);
        btn_change_mobile_get_verify.setOnClickListener(this);
        btn_change_mobile_commit.setOnClickListener(this);
        //更换按钮不可按
        btn_change_mobile_commit.setClickable(false);
//        btn_change_mobile_commit
//                .setBackgroundResource(R.drawable.regist_noclick_btn);
        btn_change_mobile_commit.setTextColor(UIUtils.getColor(R.color.color_lucency_white));
        cb_change_mobile_password_isshow.setOnCheckedChangeListener(this);

        //隐藏密码布局
        if (type != 2) {
            ly_change_mobile_password.setVisibility(View.GONE);
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
                if (s.length() == 4) {
                    //更换按钮可以按
                    btn_change_mobile_commit.setClickable(true);
//                    btn_change_mobile_commit.setBackgroundResource(R.drawable.background_green_1eb482_radius_5);
                    btn_change_mobile_commit.setTextColor(UIUtils.getColor(R.color.white));
                } else {
                    //更换按钮不可按
                    btn_change_mobile_commit.setClickable(false);
//                    btn_change_mobile_commit
//                            .setBackgroundResource(R.drawable.regist_noclick_btn);
                    btn_change_mobile_commit.setTextColor(UIUtils.getColor(R.color.color_lucency_white));
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
                if (s != null) {
                    phoneNumber = s.toString();
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


//    /*
//     * 验证短信
//	 */
//    private void getHttpUtils2(String verify_code) {
//        HttpUtils httpUtils = new HttpUtils();
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("action", "verify_code");
//        params.addBodyParameter("username", phoneNumber);
//        params.addBodyParameter("code", verify_code);
//        params.addBodyParameter("type", "3");
//
//        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
//                new RequestCallBack<String>() {
//
//                    @Override
//                    public void onFailure(HttpException arg0, String arg1) {
//                        UIUtils.showToastSafe(R.string.fail_network_request);
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> arg0) {
//                        // 处理返回的数据
//                        processData2(arg0.result);
//                    }
//                });
//    }
//
//    private void processData2(String result) {
//        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
//                UserVerifyInfo.class);
//        if (userVerifyInfo.getCode().equals("1")) {
//            getHttpUtils3(phoneNumber);
//
//        } else {
//            UIUtils.showToastSafe("验证码不正确");
//        }
//
//    }


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
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("phone", phoneNumber);
        params.addBodyParameter("code", verifyCode);

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
        params.addBodyParameter("uname", StringUtils.getUsername());
        try {
            params.addBodyParameter("pwd", MD5.md5(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.addBodyParameter("code", verifyCode);
        params.addBodyParameter("type", "3");
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        //有密码
        if (type == 2) {
            params.addBodyParameter("password", password);
        }


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
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                finish();
                break;
            case R.id.btn_change_mobile_get_verify://获取验证码
                phoneNumber = et_change_mobile_new_mobile.getText().toString();
                if (!phoneNumber.equals("") && phoneNumber.length() == 11 && InputValidate.checkedIsTelephone(phoneNumber)) {

                    getHttpUtils();

                } else {
                    UIUtils.showToastSafe(getString(R.string.toast_enter_a_correct_phone_number_format));
                }
                break;
            case R.id.btn_change_mobile_commit: //更换手机
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
