package com.yiju.ClassClockRoom.act.accompany;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.LoginActivity;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.AccompanyRead;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.constant.RequestCodeConstant;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.fragment.AccompanyReadFragment;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class StayStartVideoActivity extends BaseActivity implements
        OnClickListener {
    public static final String Param_String_PageType = "param_string_page_type";
    public static final String Param_String_WiFi = "param_string_wifi";
    public static final int Param_Int_stay = 0;// 等待开始
    public static final int Param_Int_end = 1;// 结束
    public static final int Param_Int_start = 2;//正在播放
    private int currentPageType = Param_Int_stay;
    private boolean isWiFi;

    @ViewInject(R.id.head_title)
    private TextView head_title;// 标题
    @ViewInject(R.id.head_back)
    private ImageView head_back;// 返回
    @ViewInject(R.id.stay_start_video_hint)
    private RelativeLayout video_hint;// 提示条
    @ViewInject(R.id.stay_start_video_hint_close)// 关闭提示条
    private ImageView hint_close;
    @ViewInject(R.id.stay_start_video_time)
    private TextView video_time;// 视频播放时间
    @ViewInject(R.id.stay_start_video_remind)
    private RelativeLayout video_remind;// 提醒设置
    @ViewInject(R.id.stay_start_video_remind_text)
    private TextView remind_text;// 提醒设置文字
    @ViewInject(R.id.stay_start_video_remind_switch)
    private SwitchCompat video_remind_switch;// 提醒设置切换按钮
    @ViewInject(R.id.stay_start_video_change_room)
    private RelativeLayout change_room;// 切换房间

    private AccompanyRead accompanyRead;
    private boolean isLogin;

    @Override
    public void initIntent() {
        super.initIntent();
        accompanyRead = getIntent().getParcelableExtra(
                AccompanyReadFragment.PARAM_STRING_ACCOMPANY_READ_INFO);
        currentPageType = getIntent().getIntExtra(Param_String_PageType,
                Param_Int_stay);
        isWiFi = getIntent().getBooleanExtra(Param_String_WiFi, true);
        refreshUserInfo();
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_stay_start_video_layout;
    }

    @Override
    public void initView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                video_hint.setVisibility(View.GONE);
            }
        }, 3000);
    }

    // 刷新User登录信息
    private void refreshUserInfo() {
        isLogin = SharedPreferencesUtils.getBoolean(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_isLogin), false);
    }

    // 注册是事件
    @Override
    public void initListener() {
        head_back.setOnClickListener(this);
        hint_close.setOnClickListener(this);
        change_room.setOnClickListener(this);
        video_remind_switch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    return true;
                }
                if (isLogin && !"-1".equals(StringUtils.getUid())) {
                    HttpClassRoomApi.getInstance().askRemind(StringUtils.getUid(), accompanyRead);// 提醒设置
                } else {
                    skipLogin();//跳转登陆
                }
                return true;
            }
        });
    }

    @Override
    public void initData() {
        if (isWiFi) {
            UIUtils.showToastSafe(R.string.accompany_staystart_wifi);
        }
        head_title.setText(UIUtils.getString(R.string.accompany_read_online));
        if (accompanyRead != null) {
            if (currentPageType == Param_Int_stay) {//视频还未开始
                video_remind.setVisibility(View.VISIBLE);
                change_room.setVisibility(View.GONE);
                video_time.setText(String.format(UIUtils.getString(R.string.course_will_start),
                        accompanyRead.getStart_DateTime_change()));
                refreshRemind();
            } else if (currentPageType == Param_Int_end) {//视频结束
                video_remind.setVisibility(View.GONE);
                change_room.setVisibility(View.VISIBLE);
                video_time.setText(String.format(UIUtils.getString(R.string.course_over),
                        accompanyRead.getEnd_DateTime_change()));
            } else if (currentPageType == Param_Int_start) {//WIFI未连接
                video_remind.setVisibility(View.GONE);
                change_room.setVisibility(View.GONE);
                video_time.setText(String.format(UIUtils.getString(R.string.course_start),
                        accompanyRead.getStart_DateTime_change()));
                CustomDialog customDialog = new CustomDialog(
                        StayStartVideoActivity.this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        UIUtils.getString(R.string.accompany_dialog_wifi));
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            Intent intent_wifi = new Intent(UIUtils.getContext(),
                                    Common_Show_WebPage_Activity.class);
                            intent_wifi.putExtra(UIUtils.getString(R.string.get_page_name),
                                    WebConstant.WiFi_Page);
                            startActivity(intent_wifi);
                        }
                    }
                });
                /*VideoWifiDialog.getInstance().setContent(getString(R.string.accompany_dialog_wifi))
                        .setonClickListener(new IOnClickListener() {
                            @Override
                            public void oncClick(boolean isOk) {
                                if (isOk) {
                                    Intent intent_wifi = new Intent(UIUtils.getContext(),
                                            Common_Show_WebPage_Activity.class);
                                    intent_wifi.putExtra(UIUtils.getString(R.string.get_page_name),
                                            WebConstant.WiFi_Page);
                                    startActivity(intent_wifi);
                                }
                            }
                        }).DrawLayout();*/
            }
        }
    }

    // 刷新提醒
    private void refreshRemind() {
        if (isLogin) {
            video_remind_switch.setClickable(true);
            if (accompanyRead.getHasRemember() == 0) {
                remind_text.setText(UIUtils
                        .getString(R.string.accompany_add_remind));
                video_remind_switch.setChecked(false);
            } else {
                remind_text.setText(UIUtils
                        .getString(R.string.accompany_cancel_remind));
                video_remind_switch.setChecked(true);
            }
        } else {
            remind_text.setText(UIUtils
                    .getString(R.string.accompany_set_remind));
            video_remind_switch.setChecked(false);
            video_remind_switch.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:// 返回
                onBackPressed();
                break;
            case R.id.stay_start_video_hint_close:// 关闭提示条
                video_hint.setVisibility(View.GONE);
                break;
            case R.id.stay_start_video_change_room:// 切换房间
                onBackPressed();
                break;
        }
    }

    /**
     * 跳转登陆页
     */
    private void skipLogin() {
        Intent intent = new Intent();
        intent.putExtra(UIUtils.getString(R.string.login_pre_page),
                UIUtils.getString(R.string.from_page_find));
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(UIUtils.getContext(), LoginActivity.class);
        BaseApplication.getmForegroundActivity()
                .startActivityForResult(intent,
                        RequestCodeConstant.StayStart_Video_Skip_Login);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClassRoomEvent(ClassEvent<AccompanyRead> event) {

    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        super.onRefreshEvent(event);
        if (event.getType() == DataManager.Video_Data) {
            this.accompanyRead = (AccompanyRead) event.getData();
            refreshRemind();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodeConstant.StayStart_Video_Skip_Login
                && resultCode == RESULT_OK) {
            refreshUserInfo();
            String pwd = SharedPreferencesUtils.getString(
                    BaseApplication.getmForegroundActivity(),
                    UIUtils.getString(R.string.shared_accompany_read_pwd), "");
            if ("-1".equals(StringUtils.getUid())) {
                return;
            }
            HttpClassRoomApi.getInstance().getVideoState(pwd, StringUtils.getUid(), false);// 登录房间请求数据
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_accompanyread_staystartvideo);
    }
}
