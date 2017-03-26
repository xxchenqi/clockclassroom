package com.yiju.ClassClockRoom.act.remind;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.common.callback.ITroubleRunnable;
import com.yiju.ClassClockRoom.common.constant.RequestCodeConstant;
import com.yiju.ClassClockRoom.control.AccompanyRemindControl;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.FailCodeControl;
import com.yiju.ClassClockRoom.control.TroubleControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.TroubleWheelDialog;

/**
 * 提醒设置
 *
 * @author geliping 、wh
 */
public class RemindSetActivity extends BaseActivity implements OnClickListener {

    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.remind_order_switch)
    private SwitchCompat remind_order_switch;// 订单提醒开关

    @ViewInject(R.id.remind_system_switch)
    private SwitchCompat remind_system_switch;// 系统提醒开关

    @ViewInject(R.id.remind_accompany_read_switch)
    private SwitchCompat remind_accompany_read_switch;//陪读提醒开关

    @ViewInject(R.id.remind_time_layout)
    private RelativeLayout remind_time_layout;// 提醒时间

    @ViewInject(R.id.remind_time_text)
    private TextView remind_time_text;

    @ViewInject(R.id.quiet_hours_layout)
    private RelativeLayout quiet_hours_layout;// 免打扰

    @ViewInject(R.id.quiet_hours_text)
    private TextView quiet_hours_text;

    // 是否推送
    public static final String Type_Remind_True = "1";
    public static final String Type_Remind_False = "0";


    private String remindPeiduValue;// 在线陪读是否推送参数
    private String remindOrderValue;// 预订超时是否推送参数
    private String remindSysValue;// 支付超时是否推送参数
    private String remerber;// 提前多久提醒
    private TroubleWheelDialog wv_trouble;

    @Override
    public int setContentViewId() {
        return R.layout.activity_remind_set_layout;
    }

    @Override
    public void initView() {
        head_title.setText(UIUtils.getString(R.string.remind_setting));
    }

    @Override
    public void initData() {
        refreshAccompany();

        remindOrderValue = SharedPreferencesUtils.getString(
                RemindSetActivity.this,
                getResources().getString(R.string.shared_is_order_remerber),
                "");
        remindSysValue = SharedPreferencesUtils.getString(
                RemindSetActivity.this,
                getResources().getString(R.string.shared_is_sys_remerber),
                "");
        if (remindOrderValue.equals(Type_Remind_True)) {
            remind_order_switch.setChecked(true);
        } else {
            remind_order_switch.setChecked(false);
        }
        if (remindSysValue.equals(Type_Remind_True)) {
            remind_system_switch.setChecked(true);
        } else {
            remind_system_switch.setChecked(false);
        }
        if (remindPeiduValue.equals(Type_Remind_True)) {
            remind_accompany_read_switch.setChecked(true);
        } else {
            remind_accompany_read_switch.setChecked(false);
        }

        initNoDisturb();

    }

    // 刷新陪读
    private void refreshAccompany() {
        remerber = SharedPreferencesUtils.getString(RemindSetActivity.this,
                getResources().getString(R.string.shared_remerber),
                AccompanyRemindControl.lists.get(1));
        remindPeiduValue = SharedPreferencesUtils.getString(
                RemindSetActivity.this,
                getResources().getString(R.string.shared_is_remerber),
                "");
        if (remindPeiduValue.equals(Type_Remind_True)) {
            remind_time_text.setText(AccompanyRemindControl
                    .NumFormatStr(Integer.valueOf(remerber)));
        } else {
            remind_time_text.setText(UIUtils
                    .getString(R.string.remind_closed));
        }
    }

    // 初始化免打扰时间
    private void initNoDisturb() {
        TroubleControl.getInstance().initLocalTime();
        quiet_hours_text.setText(TroubleControl.getInstance()
                .setContentVisible(null, null, null));
    }

    @Override
    public void initListener() {
        head_back_relative.setOnClickListener(this);
        remind_time_layout.setOnClickListener(this);
        quiet_hours_layout.setOnClickListener(this);
        //陪读提醒开关
        remind_accompany_read_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (isChecked) {
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_177");
                    setRemindRequest("is_remerber", Type_Remind_True);
                } else {
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_178");
                    setRemindRequest("is_remerber", Type_Remind_False);
                }
            }
        });
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_remind_set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_176");
                backActivity();
                break;
            case R.id.remind_time_layout:
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_179");
                Intent intent = new Intent(RemindSetActivity.this,
                        RemindAccompanyActivity.class);
                startActivityForResult(intent,
                        RequestCodeConstant.Remind_Skip_SetAccompany);
                break;
            case R.id.quiet_hours_layout:
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_180");
                wv_trouble = new TroubleWheelDialog(RemindSetActivity.this);
                wv_trouble.setITroubleRunnable(new ITroubleRunnable() {
                    @Override
                    public void getTroubleTime(String time) {
                        quiet_hours_text.setText(time);
                    }
                });
                wv_trouble.DrawLayout();
                break;
        }
    }

    /**
     * 编辑开关请求
     *
     * @param is_remerber 1 推送  0 不推送
     */
    private void setRemindRequest(final String action, final String is_remerber) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("is_remerber", is_remerber);
        params.addBodyParameter("url", UrlUtils.SERVER_USER_API);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());


        httpUtils.send(HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.remind_set_fail);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        CommonResultBean resultData = GsonTools.fromJson(arg0.result, CommonResultBean.class);
                        if ("1".equals(resultData.getCode())) {
                            if ("is_order_remerber".equals(action)) {//订单提醒
                                SharedPreferencesUtils.saveString(
                                        RemindSetActivity.this,
                                        UIUtils.getString(R.string.shared_is_order_remerber),
                                        is_remerber);
                            } else if ("is_sys_remerber".equals(action)) {//系统提醒
                                SharedPreferencesUtils.saveString(
                                        RemindSetActivity.this,
                                        UIUtils.getString(R.string.shared_is_sys_remerber),
                                        is_remerber);
                            } else if ("is_remerber".equals(action)) {//陪读提醒
                                SharedPreferencesUtils.saveString(
                                        RemindSetActivity.this,
                                        UIUtils.getString(R.string.shared_is_remerber),
                                        is_remerber);
                                refreshAccompany();
                            }
                        }
                        FailCodeControl.checkCode(resultData.getCode());
                        UIUtils.showToastSafe(resultData.getMsg());

                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodeConstant.Remind_Skip_SetAccompany) {
            refreshAccompany();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backActivity();
            return true;
        } else {
            return false;
        }
    }

    private void backActivity() {
        setResult(RESULT_OK);
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
    }

    @Override
    public void onBackPressed() {
        backActivity();
    }
}
