package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.remind.RemindSetActivity;
import com.yiju.ClassClockRoom.bean.MessageBox;
import com.yiju.ClassClockRoom.bean.WorkingPayResult;
import com.yiju.ClassClockRoom.bean.result.UserInfo;
import com.yiju.ClassClockRoom.bean.result.UserInfo.Data;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.control.OtherLoginControl;
import com.yiju.ClassClockRoom.control.SchemeControl;
import com.yiju.ClassClockRoom.fragment.PersonalCenterFragment;
import com.yiju.ClassClockRoom.receiver.PushClockReceiver;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.InputValidate;
import com.yiju.ClassClockRoom.util.MD5;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.FireworkView;
import com.yiju.ClassClockRoom.widget.circular.CircularProgressButton;


/**
 * ----------------------------------------
 * 注释: 登录
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/6/6 14:18
 * ----------------------------------------
 */
public class LoginActivity extends BaseActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {

    /**
     * 退出按钮
     */
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    /**
     * 注册按钮
     */
    @ViewInject(R.id.tv_register)
    private TextView tv_register;
    /**
     * 登录按钮
     */
    @ViewInject(R.id.circular_button_login)
    private CircularProgressButton circular_button_login;
    /**
     * 用户名输入框
     */
    @ViewInject(R.id.et_username)
    private EditText et_username;
    /**
     * 密码输入框
     */
    @ViewInject(R.id.et_password)
    private EditText et_password;
    /**
     * 忘记密码
     */
    @ViewInject(R.id.tv_forgotpassword)
    private TextView tv_forgotpassword;
    /**
     * qq登录
     */
    @ViewInject(R.id.login_qq_img)
    private ImageView login_qq_img;
    /**
     * 微信登录
     */
    @ViewInject(R.id.login_wechat_img)
    private ImageView login_wechat_img;
    /**
     * 新浪登录
     */
    @ViewInject(R.id.login_sina_img)
    private ImageView login_sina_img;
    /**
     * 电话号码删除按钮
     */
    @ViewInject(R.id.iv_login_delete)
    private ImageView iv_login_delete;
    /**
     * 密码显示
     */
    @ViewInject(R.id.cb_password_is_show)
    private CheckBox cb_password_is_show;
    @ViewInject(R.id.fire_work)
    private FireworkView fire_work;
    //吐司
    private Toast msg;
    //是否登录
    private boolean isLogin = false;
    //电话管理
    private TelephonyManager tm;
    //友盟
    private UMShareAPI mShareAPI;
    //动画
    private Animation shake;

    private String username;
    private String password;
    //路径
    private String path;
    //消息箱
    private MessageBox messageBox;
    private int type;

    //按钮点击1秒后处理
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            username = et_username.getText().toString().replaceAll(" ", "");
            password = et_password.getText().toString();
            validateLocalLogin();
            super.handleMessage(msg);
        }

    };
    private String teacher_uid;
    private String show_teacher;
    private String org_auth;
    private String mobile;
    private String title;
    private boolean course_flag;

    @Override
    public void initIntent() {
        super.initIntent();
        path = getIntent().getStringExtra(SchemeControl.PATH);
        type = getIntent().getIntExtra("big_type", 1);
        messageBox = (MessageBox) getIntent().getSerializableExtra("messageBox");
        teacher_uid = getIntent().getStringExtra(MemberDetailActivity.ACTION_UID);
        show_teacher = getIntent().getStringExtra(MemberDetailActivity.ACTION_SHOW_TEACHER);
        org_auth = getIntent().getStringExtra(MemberDetailActivity.ACTION_ORG_AUTH);
        title = getIntent().getStringExtra(MemberDetailActivity.ACTION_TITLE);
        mobile = getIntent().getStringExtra(MemberDetailActivity.ACTION_MOBILE);
        course_flag = getIntent().getBooleanExtra(MemberDetailActivity.ACTION_COURSE_FLAG, false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化友盟
        mShareAPI = UMShareAPI.get(this);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        if (BaseApplication.ImmersionFlag) {
            tintManager.setTintAlpha(0);
        }
        //动画初始化
        shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake_x);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        circular_button_login.setEnabled(false);
        fire_work.bindEditText(et_username);
    }

    @Override
    public void initListener() {
        super.initListener();
        circular_button_login.setOnClickListener(this);
        circular_button_login.setIndeterminateProgressMode(true);
        tv_register.setOnClickListener(this);
        iv_login_delete.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        tv_forgotpassword.setOnClickListener(this);
        login_qq_img.setOnClickListener(this);
        login_wechat_img.setOnClickListener(this);
        login_sina_img.setOnClickListener(this);
        cb_password_is_show.setOnCheckedChangeListener(this);
//        textview_other_login.setOnClickListener(this);
        circular_button_login.setEnabled(false);
        //以334显示
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String contents = s.toString();
                int length = contents.length();
                if (!"".equals(contents)) {
                    iv_login_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_login_delete.setVisibility(View.GONE);
                }
                if (length == 4) {
                    if (contents.substring(3).equals(" ")) {
                        contents = contents.substring(0, 3);
                        et_username.setText(contents);
                        et_username.setSelection(contents.length());
                    } else {
                        contents = contents.substring(0, 3) + " " + contents.substring(3);
                        et_username.setText(contents);
                        et_username.setSelection(contents.length());
                    }
                } else if (length == 9) {
                    if (contents.substring(8).equals(" ")) { // -
                        contents = contents.substring(0, 8);
                        et_username.setText(contents);
                        et_username.setSelection(contents.length());
                    } else {// +
                        contents = contents.substring(0, 8) + " " + contents.substring(8);
                        et_username.setText(contents);
                        et_username.setSelection(contents.length());
                    }
                }
                if (contents.replaceAll(" ", "").length() == 11
                        && et_password.getText().toString().replaceAll(" ", "").length() >= 6) {
                    circular_button_login.setEnabled(true);
                } else {
                    circular_button_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (StringUtils.isNullString(et_username.getText().toString().trim())) {
                        UIUtils.showToastSafe("请先输入手机号");
                        et_username.setFocusable(true);
                        return;
                    }
                    if (et_username.getText().toString().replaceAll(" ", "").length() != 11) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.label_login_beUse));
                        return;
                    }
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6 && s.length() <= 18) {
                    circular_button_login.setEnabled(true);
                } else {
                    circular_button_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void initData() {
//        ViewTreeObserver vto = other_login_layout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                other_login_layout.getViewTreeObserver()
//                        .removeGlobalOnLayoutListener(this);
//                //获取other_login_height控件的高度
//                other_login_height = other_login_layout.getHeight();
//                //移动
//                ObjectAnimator
//                        .ofFloat(other_login_layout, "translationY",
//                                (int) (other_login_height * 0.54))
//                        .setDuration(0).start();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back://返回
                onBackPressed();
                break;
            case R.id.circular_button_login://登录
                //1秒后在请求
                //是否有权限
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
                    circular_button_login.setProgress(50);
                    Message m = new Message();
                    mHandler.sendMessageDelayed(m, 1000);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            case R.id.tv_register://注册
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
                    Intent register = new Intent(this, RegisterActivity.class);
                    register.putExtra("title", getString(R.string.account_register));//标题
                    startActivity(register);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            case R.id.tv_forgotpassword://忘记密码
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
                    Intent forgotpassword = new Intent(this, RegisterActivity.class);
                    forgotpassword.putExtra("title", getString(R.string.forget_password));
                    startActivity(forgotpassword);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            case R.id.iv_login_delete://删除
                et_username.setText("");
                break;
//            case R.id.ibtn_login_icon:
//            case R.id.textview_other_login:
//                // 第三登录弹出
//                if (!other_on_off) {
//                    //还原
//                    ObjectAnimator.ofFloat(other_login_layout, "translationY", 0)
//                            .setDuration(400).start();
//                    other_on_off = true;
//                } else {
//                    //移下去
//                    ObjectAnimator
//                            .ofFloat(other_login_layout, "translationY",
//                                    (int) (other_login_height * 0.54))
//                            .setDuration(400).start();
//                    other_on_off = false;
//                }
////                setOtherLoginBtnBg();
//                break;
            case R.id.login_qq_img://qq登录
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
//                setOtherLoginIcon(Param_Other_QQ);
                    OtherLoginControl.login(LoginActivity.this, mShareAPI,
                            SHARE_MEDIA.QQ);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            case R.id.login_wechat_img:///微信登录
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
//                setOtherLoginIcon(Param_Other_Wechat);
                    OtherLoginControl.login(LoginActivity.this, mShareAPI,
                            SHARE_MEDIA.WEIXIN);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            case R.id.login_sina_img://新浪登录
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
//                setOtherLoginIcon(Param_Other_Sina);
                    OtherLoginControl.login(LoginActivity.this, mShareAPI,
                            SHARE_MEDIA.SINA);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 登录判断
     */
    private void validateLocalLogin() {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            msg = Toast.makeText(getApplicationContext(), getResources()
                            .getString(R.string.label_login_hasContent),
                    Toast.LENGTH_SHORT);
            msg.show();
            circular_button_login.setProgress(0);
            circular_button_login.startAnimation(shake);
            circular_button_login.setEnabled(true);
            return;
        }

        // if (!InputValidate.checkedIsEmail(userName)) {// 不是Email
        if (!InputValidate.checkedIsTelephone(username)) {// 不是电话
            UIUtils.showToastSafe(getResources().getString(
                    R.string.label_login_beUse));
            circular_button_login.setProgress(0);
            circular_button_login.startAnimation(shake);
            circular_button_login.setEnabled(true);
        } else {
            if (!InputValidate.checkPassword(password)) {
                UIUtils.showToastSafe(getResources().getString(
                        R.string.label_login_bePassword));
                circular_button_login.setProgress(0);
                circular_button_login.startAnimation(shake);
                circular_button_login.setEnabled(true);
            } else {
                if (StringUtils.isNullString(PushClockReceiver.cid)) {
                    PushClockReceiver.cid = SharedPreferencesUtils.getString(
                            LoginActivity.this,
                            SharedPreferencesConstant.Shared_Login_Cid, "");
                }
                if (StringUtils.isNullString(SplashActivity.youYunUDid)) {
                    SplashActivity.youYunUDid = SharedPreferencesUtils.getString(
                            LoginActivity.this,
                            SharedPreferencesConstant.Shared_Login_Udid, "");
                }
                circular_button_login.setProgress(0);
                circular_button_login.startAnimation(shake);
                circular_button_login.setEnabled(true);

                requestLogin(PushClockReceiver.cid, SplashActivity.youYunUDid);
            }
        }

    }

    /**
     * 登录请求
     *
     * @param cid 设备识别码
     */
    @SuppressLint("HardwareIds")
    private void requestLogin(String cid, String udid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "login");
        params.addBodyParameter("username", username);
        params.addBodyParameter("password", password);
        params.addBodyParameter("device_token", tm.getDeviceId());
        if (StringUtils.isNotNullString(cid)) {
            params.addBodyParameter("cid", cid);//个推推送码
        }else{
            params.addBodyParameter("cid", "");//个推推送码
        }
        if (StringUtils.isNotNullString(udid)) {
            params.addBodyParameter("udid", udid);//游云推送码
        }else{
            params.addBodyParameter("udid", "");//游云推送码
        }

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(getResources().getString(R.string.network_anomaly));
                        circular_button_login.setProgress(0);
                        circular_button_login.startAnimation(shake);
                        circular_button_login.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);
                    }
                });
    }

    private void processData(String result) {
        UserInfo userInfo = GsonTools.fromJson(result, UserInfo.class);
        if(userInfo==null){
            return;
        }
        if ("1".equals(userInfo.getCode())) {
            //umeng账号统计
            MobclickAgent.onProfileSignIn(userInfo.getData().getId());
            circular_button_login.setProgress(0);
            circular_button_login.setEnabled(true);
            // Login success
            praseUserInfo(userInfo.getData());
            // 登陆成功返回()
            setResult(RESULT_OK);
            if (path != null) {//浏览器跳转过来需要,例:浏览器跳转个人信息->未登录->登录->登录完成->请求用户信息->请求完成->跳转个人信息
                if (path.equals(UIUtils.getString(R.string.scheme_personal_information_path))) {//个人信息
                    getHttpUtils();
                } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_wait_pay_path))) {//我的订单-待支付
                    getHttpUtils();
                } else if (path.equals(UIUtils.getString(R.string.scheme_personal_teacher_information_path))) {//个人老师资料
                    getHttpUtils();
                } else {
                    onBackPressed();
                }
            } else {
                onBackPressed();
            }
        } else {
            circular_button_login.setProgress(0);
            circular_button_login.startAnimation(shake);
            circular_button_login.setEnabled(true);
            UIUtils.showToastSafe(userInfo.getMsg());
        }
    }

    /**
     * 完成解析后处理
     *
     * @param userInfo 用户信息
     */
    private void praseUserInfo(Data userInfo) {
        this.isLogin = true;
        CountControl.getInstance().loginSuccess(userInfo.getId());
        //写入登录成功
        SharedPreferencesUtils.saveBoolean(getApplicationContext(),
                getResources().getString(R.string.shared_isLogin), isLogin);

        //登录状态写入到数据库就可以。
        //其他信息不需要再这边写入到数据库，在个人中心里会去请求getinfo去写入，因为登录获取到的用户信息不对。(PS:绑定账号)

        // 成功获取到用户信息，写入本地
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_id), userInfo.getId());
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_username), username);
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
        SharedPreferencesUtils.saveString(getApplicationContext(),
                getResources().getString(R.string.shared_avatar),
                userInfo.getAvatar());
        SharedPreferencesUtils.saveString(this,
                getResources().getString(R.string.shared_mobile), userInfo.getMobile());
        //获取支付所需的工作密钥

        getWorkingKey(userInfo.getId());

    }

    //请求工作密钥api
    private void getWorkingKey(String uid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getWorkingKey");
        params.addBodyParameter("uid", uid);
//        params.addBodyParameter("expire_second", "");//密钥过期秒数，可不传，默认7天

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_API_PAY, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForWorkingKey(arg0.result);
                    }
                });
    }

    private void processDataForWorkingKey(String result) {
        WorkingPayResult workingPayResult = GsonTools.changeGsonToBean(result, WorkingPayResult.class);
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


    /**
     * 第三方登录按钮文字背景
     */
//    private void setOtherLoginBtnBg() {
//        if (other_on_off) {
//            //还原
////            textview_other_login.setTextColor(UIUtils.getColor(R.color.orange));
////            ibtn_login_icon.setImageResource(R.drawable.third_party_login);
//        } else {
//            //移下
////            textview_other_login.setTextColor(UIUtils
////                    .getColor(R.color.color_gay_99));
////            ibtn_login_icon.setImageResource(R.drawable.third_party_login_gray);
//        }
//    }

    /**
     * 第三方登录背景
     */
//    private void setOtherLoginIcon(int index) {
//        login_qq_img.setImageResource(R.drawable.qq_icon);
//        login_wechat_img.setImageResource(R.drawable.wechat_icon_gray);
//        login_sina_img.setImageResource(R.drawable.sina_icon);
//        switch (index) {
//            case Param_Other_QQ:
//                login_qq_img.setImageResource(R.drawable.qq_icon_orange);
//                break;
//            case Param_Other_Wechat:
//                login_wechat_img.setImageResource(R.drawable.wechat_icon);
//                break;
//            case Param_Other_Sina:
//                login_sina_img.setImageResource(R.drawable.sina_icon_orange);
//                break;
//            default:
//                break;
//        }
//    }
    @Override
    public String getPageName() {
        return getString(R.string.title_act_login);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if ("-1".equals(StringUtils.getUid())) {
            ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
        } else {
            //个人信息和待支付路径不能在此判断.(例:直接返回)
            if (StringUtils.isNotNullString(path)) {
                if (path.equals(UIUtils.getString(R.string.scheme_mine_message_path))) {//我的消息
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, Messages_Activity.class);
                } else if (path.equals(UIUtils.getString(R.string.scheme_order_message_path)) ||//订单消息
                        path.equals(UIUtils.getString(R.string.scheme_system_message_path)) ||//系统消息
                        path.equals(UIUtils.getString(R.string.scheme_accompany_read_remind_path))) {//陪读提醒
                    Intent intent = new Intent(this, MessageDetialActivity.class);
                    intent.putExtra("messageBox", messageBox);
                    intent.putExtra("big_type", type);
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, intent);
                } else if (path.equals(UIUtils.getString(R.string.scheme_mine_organization_path))) {//我的机构
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, MineOrganizationActivity.class);
                } else if (path.equals(UIUtils.getString(R.string.scheme_mine_course_path))) {//我的课程
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, PersonMineCourseActivity.class);
                } else if (path.equals(UIUtils.getString(R.string.scheme_publish_course_path))) {//发布课程
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, PublishActivity.class);
                } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_all_path))) {//我的订单-全部订单
                    Intent intent = new Intent(this, MineOrderActivity.class);
                    intent.putExtra(MineOrderActivity.STATUS, MineOrderActivity.STATUS_ALL);
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, intent);
                } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_underway_path))) {//我的订单-进行中
                    Intent intent = new Intent(this, MineOrderActivity.class);
                    intent.putExtra(MineOrderActivity.STATUS, MineOrderActivity.STATUS_USE);
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, intent);
                } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_finish_path))) {//我的订单-已完成
                    Intent intent = new Intent(this, MineOrderActivity.class);
                    intent.putExtra(MineOrderActivity.STATUS, MineOrderActivity.STATUS_FINISH);
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, intent);
                } else if (path.equals(UIUtils.getString(R.string.scheme_mine_discount_coupon_path))) {
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, PersonalCenter_CouponListActivity.class);
                } else if (path.equals(UIUtils.getString(R.string.scheme_mine_attention_path))) {//我的关注
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, MineWatchlistActivity.class);
                } else if (path.equals(UIUtils.getString(R.string.scheme_remind_set_path))) {//提醒设置
                    ActivityControlManager.getInstance().finishCurrentAndOpenOther(this, RemindSetActivity.class);
                } else {//默认
                    ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
                }
            } else {
                ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
            }
        }
    }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
            UIUtils.showToastSafe(getString(R.string.toast_permission_read_phone_state));
        }
    }

    /**
     * 个人信息请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getInfo");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showLongToastSafe("个人信息获取失败");
                        //请求失败回到个人信息
                        ActivityControlManager.getInstance().finishCurrentAndOpenHome(LoginActivity.this, 3);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataInfo(arg0.result);
                    }
                });
    }


    private void processDataInfo(String result) {
        UserInfo userInfo = GsonTools.changeGsonToBean(result, UserInfo.class);
        if (userInfo == null) {
            return;
        }
        if ("1".equals(userInfo.getCode())) {
            UserInfo.Data data = userInfo.getData();
            PersonalCenterFragment.parseUserInfo(data, userInfo);
            Intent intent = new Intent();

            if (path.equals(UIUtils.getString(R.string.scheme_personal_information_path))) {//个人信息页
                intent.setClass(this, PersonalCenter_InformationActivity.class);
                startActivity(intent);
                finish();
            } else if (path.equals(UIUtils.getString(R.string.scheme_mine_order_wait_pay_path))) {//待支付
                intent.setClass(this, MineOrderActivity.class);
                intent.putExtra(MineOrderActivity.STATUS, MineOrderActivity.STATUS_WAIT_PAY);
                intent.putExtra(MineOrderActivity.COUNT, userInfo.getAlert_order().getCount());
                startActivity(intent);
                finish();
            } else if (path.equals(UIUtils.getString(R.string.scheme_personal_teacher_information_path))) {//个人老师资料
                intent.setClass(this, MemberDetailActivity.class);
                intent.putExtra(MemberDetailActivity.ACTION_UID, teacher_uid);
                intent.putExtra(MemberDetailActivity.ACTION_SHOW_TEACHER, show_teacher);
                intent.putExtra(MemberDetailActivity.ACTION_ORG_AUTH, org_auth);
                intent.putExtra(MemberDetailActivity.ACTION_MOBILE, mobile);
                intent.putExtra(MemberDetailActivity.ACTION_TITLE, title);
                startActivity(intent);
                finish();
            } else {
                ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
            }

        } else {
            //请求失败
            UIUtils.showLongToastSafe(userInfo.getMsg());
            ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
        }
    }


}
