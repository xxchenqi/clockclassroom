package com.yiju.ClassClockRoom.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.accompany.StartVideoActivity;
import com.yiju.ClassClockRoom.act.accompany.StayStartVideoActivity;
import com.yiju.ClassClockRoom.bean.AccompanyRead;
import com.yiju.ClassClockRoom.bean.PassWordErrorLock;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.common.constant.ParamConstant;
import com.yiju.ClassClockRoom.common.callback.INetWorkRunnable;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.KeyBoardManager;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;

import java.util.Date;

/**
 * 陪读碎片
 *
 * @author geliping
 */
public class AccompanyReadFragment extends BaseFragment {
    // 传递陪读数据参数
    public static final String PARAM_STRING_ACCOMPANY_READ_INFO = "param_string_accompany_read_info";

    // 切换控件
    private ViewFlipper accompany_read_flipper;
    // ========================陪读登录相关========================//
    //返回
    private RelativeLayout rl_accompany_back;
    //标题
    private TextView tv_accompany_title;
    // 密码输入框
    private EditText accompany_password_edit;
    // 确认按钮
    private TextView accompany_affirm;
    // 错误提示
    private TextView accompany_error_hint;
    // ========================陪读帮助相关========================//
    // 陪读帮助返回
    private RelativeLayout head_back_relative;
    // 陪读帮助
    private ImageView accompany_help;
    // 帮助页标题
    private TextView head_title;
    private PassWordErrorLock lockBean;

    private boolean isHelpPage;//是否是帮助页

    @Override
    public int setContentViewId() {
        return R.layout.fragment_accompany_read_layout;
    }

    @Override
    public void initListener() {
        accompany_help.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 切换到帮助页面
                accompany_read_flipper.setInAnimation(changeAnimation(1.0f,
                        0.0f));
                accompany_read_flipper.setOutAnimation(changeAnimation(0.0f,
                        -1.0f));
                accompany_read_flipper.showNext();
                isHelpPage = true;
            }
        });

        rl_accompany_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                fatherActivity.finish();
                fatherActivity.onBackPressed();
            }
        });
        head_back_relative.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 切换到登录房间页面
                changeToAccompany();
            }
        });
        accompany_affirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String pwd = accompany_password_edit.getText().toString();
                if (StringUtils.isNullString(pwd)) {//密码不为空
                    UIUtils.showToastSafe(R.string.label_hint_password);
                    return;
                }
                if (lockBean != null && lockBean.isLock()) {//是否超过密码输入次数
                    return;
                }
                if("-1".equals(StringUtils.getUid())){
                    return;
                }
                HttpClassRoomApi.getInstance().getVideoState(pwd, StringUtils.getUid(), true);
            }
        });
        accompany_read_flipper.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardManager.closeKeyBoard(fatherActivity);
                return false;
            }
        });
    }


    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        if (event.getType() == DataManager.Video_Data) {
            if (ParamConstant.RESULT_CODE_VIDEO_PWD_ERROR == event.getCode()) {
                ErrorMethod();//密码错误
            } else {
                AccompanyRead bean = (AccompanyRead) event.getData();
                SharedPreferencesUtils.saveString(fatherActivity, SharedPreferencesConstant.Shared_PassWord_Error_Lock, "");
                switch (event.getCode()) {
                    case ParamConstant.RESULT_CODE_SUCCESS://成功获取数据
                        videoStartMethod(bean);
                        break;
                    case ParamConstant.RESULT_CODE_VIDEO_END://视频结束
                        skipStayPage(bean, true, StayStartVideoActivity.Param_Int_end);
                        break;
                    case ParamConstant.RESULT_CODE_VIDEO_STAY_START://视频还未开始
                        skipStayPage(bean, true, StayStartVideoActivity.Param_Int_stay);
                        break;
                }
            }
        }
    }

    /**
     * 视频正在播放处理
     */
    private void videoStartMethod(final AccompanyRead bean) {
        NetWorkUtils.checkURL(
                bean.getVideo_ip(),
                new INetWorkRunnable() {
                    @Override
                    public void isconnection(
                            boolean connect) {
                        if (NetWorkUtils.isWiFiActive(fatherActivity) && connect) {
                            // 开始上课
                            Intent intent = new Intent(fatherActivity,
                                    StartVideoActivity.class);
                            intent.putExtra(PARAM_STRING_ACCOMPANY_READ_INFO, bean);
                            fatherActivity.startActivity(intent);
                        } else {
                            skipStayPage(bean, false, StayStartVideoActivity.Param_Int_start);
                        }
                    }
                });
    }

    //错误处理
    private void ErrorMethod() {
        String lockData = SharedPreferencesUtils.getString(fatherActivity,
                SharedPreferencesConstant.Shared_PassWord_Error_Lock, "");
        if (StringUtils.isNullString(lockData)) {
            lockBean = new PassWordErrorLock();
        } else {
            lockBean = GsonTools.fromJson(lockData, PassWordErrorLock.class);
        }
        if (lockBean != null) {
            lockBean.addErrorCount(1);
            lockBean.setDatetime(new Date().getTime());
            SharedPreferencesUtils.saveString(fatherActivity,
                    SharedPreferencesConstant.Shared_PassWord_Error_Lock,
                    GsonTools.createGsonString(lockBean));
            if (lockBean.isLock()) {
                accompany_error_hint.setText(String.format(
                                UIUtils.getString(R.string.password_fail),
                                lockBean.getSurplusTime())
                );
            } else {
                accompany_error_hint.setText(String.format(
                        UIUtils.getString(R.string.password_chance),
                        lockBean.getSurplusNum()));
            }
        }
    }

    @Override
    public void initData() {
        tv_accompany_title.setText(UIUtils.getString(R.string.accompany_read_online));
        head_title.setText(UIUtils.getString(R.string.accompany_help_page));
        //获得输错密码次数
        String lockData = SharedPreferencesUtils.getString(fatherActivity,
                SharedPreferencesConstant.Shared_PassWord_Error_Lock, "");
        lockBean = GsonTools.fromJson(lockData, PassWordErrorLock.class);
        if (lockBean != null) {
            if (lockBean.isLock()) {
                accompany_error_hint.setText(String.format(
                        UIUtils.getString(R.string.password_fail),
                        lockBean.getSurplusTime()));
            }
        }
        accompany_password_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!"".equals(s.toString())){
                    accompany_affirm.setTextColor(UIUtils.getColor(R.color.white));
                    accompany_affirm.setEnabled(true);
                }else {
                    accompany_affirm.setTextColor(UIUtils.getColor(R.color.color_lucency_white));
                    accompany_affirm.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        accompany_password_edit.setText(SharedPreferencesUtils.getString(
                fatherActivity,
                UIUtils.getString(R.string.shared_accompany_read_pwd), ""));
    }

    @Override
    public void initView() {
        accompany_read_flipper = (ViewFlipper) currentView
                .findViewById(R.id.accompany_read_flipper);
        accompany_help = (ImageView) currentView
                .findViewById(R.id.accompany_help);
        rl_accompany_back = (RelativeLayout) currentView
                .findViewById(R.id.rl_accompany_back);
        tv_accompany_title = (TextView) currentView
                .findViewById(R.id.tv_accompany_title);
        accompany_password_edit = (EditText) currentView
                .findViewById(R.id.accompany_password_edit);
        accompany_affirm = (TextView) currentView
                .findViewById(R.id.accompany_affirm);
        accompany_error_hint = (TextView) currentView
                .findViewById(R.id.accompany_error_hint);
        head_back_relative = (RelativeLayout) currentView
                .findViewById(R.id.head_back_relative);
        head_title = (TextView) currentView.findViewById(R.id.head_title);
        accompany_affirm.setTextColor(UIUtils.getColor(R.color.color_lucency_white));
        accompany_affirm.setEnabled(false);
    }

    /**
     * 动画切换页面效果
     */
    private Animation changeAnimation(float xFrom, float xto) {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, xFrom, Animation.RELATIVE_TO_SELF,
                xto, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        return animation;
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_fragment_accompanyread);
    }

    /**
     * 跳转到还未开始授课
     */
    private void skipStayPage(AccompanyRead accompanyRead, boolean isWifi,
                              int value) {
        // 未开始上课
        Intent intent = new Intent(fatherActivity, StayStartVideoActivity.class);
        intent.putExtra(PARAM_STRING_ACCOMPANY_READ_INFO, accompanyRead);
        if (!isWifi) {
            intent.putExtra(StayStartVideoActivity.Param_String_WiFi, false);
        }
        intent.putExtra(StayStartVideoActivity.Param_String_PageType, value);
        fatherActivity.startActivity(intent);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && isHelpPage) {
            // 切换到登录房间页面
            changeToAccompany();
            return true;
        }
        return false;
    }

    // 切换到登录房间页面
    private void changeToAccompany() {
        accompany_read_flipper.setInAnimation(changeAnimation(-1.0f,
                0.0f));
        accompany_read_flipper.setOutAnimation(changeAnimation(0.0f,
                1.0f));
        accompany_read_flipper.showPrevious();
        isHelpPage = false;
    }

}
