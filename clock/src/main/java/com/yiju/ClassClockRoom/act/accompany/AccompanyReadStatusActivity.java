package com.yiju.ClassClockRoom.act.accompany;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.AccompanyRead;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.callback.INetWorkRunnable;
import com.yiju.ClassClockRoom.common.constant.ParamConstant;
import com.yiju.ClassClockRoom.control.SchemeControl;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/11/10 16:55
 * ----------------------------------------
 */
public class AccompanyReadStatusActivity extends BaseActivity {
    // 传递陪读数据参数
    public static final String PARAM_STRING_ACCOMPANY_READ_INFO = "param_string_accompany_read_info";
    //返回
    @ViewInject(R.id.rl_accompany_back)
    private RelativeLayout rl_accompany_back;
    //刷新
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;
    //无wifi
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    //密码
    private String password;

    @Override
    public void initIntent() {
        super.initIntent();
        password = getIntent().getStringExtra(SchemeControl.PASSWORD);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        //请求
        if (NetWorkUtils.getNetworkStatus(this)) {
            ly_wifi.setVisibility(View.GONE);
            if (StringUtils.isNotNullString(password)) {
                HttpClassRoomApi.getInstance().getVideoState(password, StringUtils.getUid(), true);
            } else {
                UIUtils.showLongToastSafe("密码不能为空,请返回重来");
            }
        } else {
            ly_wifi.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        rl_accompany_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_no_wifi_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }


    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        if (event.getType() == DataManager.Video_Data) {
            if (ParamConstant.RESULT_CODE_VIDEO_PWD_ERROR == event.getCode()) {
                UIUtils.showLongToastSafe("密码错误");//密码错误
            } else {
                AccompanyRead bean = (AccompanyRead) event.getData();
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
                        if (NetWorkUtils.isWiFiActive(AccompanyReadStatusActivity.this) && connect) {
                            // 开始上课
                            Intent intent = new Intent(AccompanyReadStatusActivity.this, StartVideoActivity.class);
                            intent.putExtra(PARAM_STRING_ACCOMPANY_READ_INFO, bean);
                            startActivity(intent);
                            finish();
                        } else {
                            skipStayPage(bean, false, StayStartVideoActivity.Param_Int_start);
                        }
                    }
                });
    }


    /**
     * 跳转到还未开始授课
     */
    private void skipStayPage(AccompanyRead accompanyRead, boolean isWifi,
                              int value) {
        // 未开始上课
        Intent intent = new Intent(this, StayStartVideoActivity.class);
        intent.putExtra(PARAM_STRING_ACCOMPANY_READ_INFO, accompanyRead);
        if (!isWifi) {
            intent.putExtra(StayStartVideoActivity.Param_String_WiFi, false);
        }
        intent.putExtra(StayStartVideoActivity.Param_String_PageType, value);
        startActivity(intent);
        finish();
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_accompany_read_status;
    }
}
