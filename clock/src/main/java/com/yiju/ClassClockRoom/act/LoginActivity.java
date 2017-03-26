package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.remind.RemindSetActivity;
import com.yiju.ClassClockRoom.bean.LoginBean;
import com.yiju.ClassClockRoom.bean.MessageBox;
import com.yiju.ClassClockRoom.bean.WorkingPayResult;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.result.LoginResult;
import com.yiju.ClassClockRoom.bean.result.UserInfo;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.control.OtherLoginControl;
import com.yiju.ClassClockRoom.control.SchemeControl;
import com.yiju.ClassClockRoom.fragment.PersonalCenterFragment;
import com.yiju.ClassClockRoom.receiver.PushClockReceiver;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.InputValidate;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.ClearEditText;
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
public class LoginActivity extends BaseActivity implements OnClickListener {

    //退出按钮
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    //账号密码登录
    @ViewInject(R.id.tv_password_login)
    private TextView tv_password_login;
    //登录按钮
    @ViewInject(R.id.circular_button_login)
    private CircularProgressButton circular_button_login;
    //用户名输入框
    @ViewInject(R.id.et_username)
    private ClearEditText et_username;
    //注册验证
    @ViewInject(R.id.et_register_verify)
    private EditText et_register_verify;
    //发送验证码
    @ViewInject(R.id.btn_get_verify)
    private Button btn_get_verify;
    //qq登录
    @ViewInject(R.id.login_qq_img)
    private ImageView login_qq_img;
    //微信登录
    @ViewInject(R.id.login_wechat_img)
    private ImageView login_wechat_img;
    //新浪登录
    @ViewInject(R.id.login_sina_img)
    private ImageView login_sina_img;
    //火焰喷射
    @ViewInject(R.id.fire_work)
    private FireworkView fire_work;
    //电话管理
    private TelephonyManager tm;
    //友盟
    private UMShareAPI mShareAPI;
    //动画
    private Animation shake;
    //用户名
    private String username;
    //验证码
    private String verify_number = "";
    //路径
    private String path;
    //消息箱
    private MessageBox messageBox;
    //消息箱类型的传值
    private int type;
    //计时器
    private TimeCount timeCount;


    //按钮点击1秒后处理
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            username = et_username.getText().toString().replaceAll(" ", "");
            validateLocalLogin();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean is_login = intent.getBooleanExtra(ExtraControl.EXTRA_IS_LOGIN, false);
        if (is_login) {
            setResult(RESULT_OK);
            onBackPressed();
        }
    }

    @Override
    public void initIntent() {
        super.initIntent();
        path = getIntent().getStringExtra(SchemeControl.PATH);
        type = getIntent().getIntExtra("big_type", 1);
        messageBox = (MessageBox) getIntent().getSerializableExtra("messageBox");
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
        //动画初始化
        shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake_x);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        circular_button_login.setIndeterminateProgressMode(true);
//        fire_work.bindEditText(et_username);
        timeCount = new TimeCount(60000, 1000);
    }

    @Override
    public void initListener() {
        super.initListener();
        tv_password_login.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        login_qq_img.setOnClickListener(this);
        login_wechat_img.setOnClickListener(this);
        login_sina_img.setOnClickListener(this);
        btn_get_verify.setOnClickListener(this);
        circular_button_login.setOnClickListener(this);
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
                if (contents.length() == 13 && verify_number.length() == 4) {
                    circular_button_login.setEnabled(true);
                } else {
                    circular_button_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_register_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                verify_number = charSequence.toString();

                if (et_username.getText().length() == 13 && verify_number.length() == 4) {
                    circular_button_login.setEnabled(true);
                } else {
                    circular_button_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3300_001");
                onBackPressed();
                break;
            case R.id.tv_password_login://账号密码登录
                MobclickAgent.onEvent(UIUtils.getContext(), "v3300_004");
                startActivity(new Intent(this, PasswordLoginActivity.class));
                break;
            case R.id.circular_button_login://登录
                MobclickAgent.onEvent(UIUtils.getContext(), "v3300_003");
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
            case R.id.login_qq_img://qq登录
                MobclickAgent.onEvent(UIUtils.getContext(), "v3300_006");
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
                    OtherLoginControl.login(LoginActivity.this, mShareAPI,
                            SHARE_MEDIA.QQ);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            case R.id.login_wechat_img:///微信登录
                MobclickAgent.onEvent(UIUtils.getContext(), "v3300_007");
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
                    OtherLoginControl.login(LoginActivity.this, mShareAPI,
                            SHARE_MEDIA.WEIXIN);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            case R.id.login_sina_img://新浪登录
                MobclickAgent.onEvent(UIUtils.getContext(), "v3300_008");
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_PHONE_STATE_PERMISSIONS)) {
                    OtherLoginControl.login(LoginActivity.this, mShareAPI,
                            SHARE_MEDIA.SINA);
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_PHONE_STATE_PERMISSIONS);
                }
                break;
            case R.id.btn_get_verify://获取验证码
                MobclickAgent.onEvent(UIUtils.getContext(), "v3300_002");
                username = et_username.getText().toString().replaceAll(" ", "");
                if (StringUtils.isNotNullString(username) && username.length() == 11 &&
                        InputValidate.checkedIsTelephone(username)) {
                    timeCount.start();
                    getSendVerify(username);
                } else {
                    UIUtils.showToastSafe(R.string.toast_enter_a_correct_phone_number_format);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param phoneNumber 手机号
     */
    private void getSendVerify(String phoneNumber) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", phoneNumber);

        httpUtils.send(HttpMethod.POST, UrlUtils.JAVA_SEND_VERIFY, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        BaseEntity baseEntity = GsonTools.changeGsonToBean(arg0.result, BaseEntity.class);
                        if (baseEntity == null) {
                            return;
                        }
                        if (!"1".equals(baseEntity.getCode())) {
                            UIUtils.showLongToastSafe(baseEntity.getMsg());
                        }
                    }
                });
    }


    /**
     * 登录判断
     */
    private void validateLocalLogin() {
        if (!InputValidate.checkedIsTelephone(username)) {// 不是电话
            UIUtils.showToastSafe(getResources().getString(
                    R.string.label_login_beUse));
            circular_button_login.setProgress(0);
            circular_button_login.startAnimation(shake);
            circular_button_login.setEnabled(true);
        } else {
            if (StringUtils.isNullString(PushClockReceiver.cid)) {
                PushClockReceiver.cid = SharedPreferencesUtils.getString(
                        this,
                        SharedPreferencesConstant.Shared_Login_Cid, "");
            }
            if (StringUtils.isNullString(SplashActivity.youYunUDid)) {
                SplashActivity.youYunUDid = SharedPreferencesUtils.getString(
                        this,
                        SharedPreferencesConstant.Shared_Login_Udid, "");
            }
            getHttpVerifyLogin(PushClockReceiver.cid, SplashActivity.youYunUDid);
        }

    }

    private void getHttpVerifyLogin(String cid, String udid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", username);
        params.addBodyParameter("code", verify_number);
        params.addBodyParameter("device_token", tm.getDeviceId());
        if (StringUtils.isNotNullString(cid)) {
            params.addBodyParameter("cid", cid);//个推推送码
        } else {
            params.addBodyParameter("cid", "");//个推推送码
        }
        if (StringUtils.isNotNullString(udid)) {
            params.addBodyParameter("udid", udid);//游云推送码
        } else {
            params.addBodyParameter("udid", "");//游云推送码
        }

        httpUtils.send(HttpMethod.POST, UrlUtils.JAVA_VERIFY_LOGIN, params,
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
                        circular_button_login.setProgress(0);
                        circular_button_login.startAnimation(shake);
                        circular_button_login.setEnabled(true);
                        processData(arg0.result);
                    }
                });
    }


    private void processData(String result) {
        LoginBean loginBean = GsonTools.changeGsonToBean(result, LoginBean.class);
        if (loginBean == null) {
            return;
        }
        if ("0".equals(loginBean.getCode())) {
            //umeng账号统计
            MobclickAgent.onProfileSignIn(loginBean.getObj().getUserId());
            // Login success
            parseUserInfo(loginBean.getObj());
            //获取支付所需的工作密钥
            getWorkingKey(loginBean.getObj().getUserId());
            // 登陆成功返回()
            setResult(RESULT_OK);

            //浏览器跳转过来需要,例:浏览器跳转个人信息->未登录->登录->登录完成->请求用户信息->请求完成->跳转个人信息
            if (UIUtils.getString(R.string.scheme_personal_information_path).equals(path)) {//个人信息
                getHttpUtils();
            } else if (UIUtils.getString(R.string.scheme_mine_order_wait_pay_path).equals(path)) {//我的订单-待支付
                getHttpUtils();
            } else if (UIUtils.getString(R.string.scheme_personal_teacher_information_path).equals(path)) {//个人老师资料
                getHttpUtils();
            } else {
                //设置过密码,未设置过密码
                if (!"1".equals(loginBean.getObj().getExistpassword())) {
                    startActivity(new Intent(this, SetPasswordActivity.class));
                }
                onBackPressed();
            }
        } else {
            circular_button_login.setProgress(0);
            circular_button_login.startAnimation(shake);
            circular_button_login.setEnabled(true);
            UIUtils.showToastSafe(loginBean.getMsg());
        }
    }

    /**
     * 完成解析后处理
     */
    private void parseUserInfo(LoginResult loginResult) {
        CountControl.getInstance().loginSuccess(loginResult.getUserId());
        //写入登录成功
        SharedPreferencesUtils.saveBoolean(this,
                getResources().getString(R.string.shared_isLogin), true);
        // 成功获取到用户信息，写入本地
        SharedPreferencesUtils.saveString(this,
                getResources().getString(R.string.shared_id), loginResult.getUserId());
        SharedPreferencesUtils.saveString(this,
                getResources().getString(R.string.shared_session_id), loginResult.getSessionId());
        SharedPreferencesUtils.saveString(this,
                getResources().getString(R.string.shared_mobile), loginResult.getMobile());
        SharedPreferencesUtils.saveString(this,
                getResources().getString(R.string.shared_existpassword),
                loginResult.getExistpassword());
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
                        WorkingPayResult workingPayResult = GsonTools.changeGsonToBean(arg0.result,
                                WorkingPayResult.class);
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
            } else {
                ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
            }

        } else {
            //请求失败
            UIUtils.showLongToastSafe(userInfo.getMsg());
            ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
        }
    }

    /**
     * 计时器类
     */
    class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
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

}
