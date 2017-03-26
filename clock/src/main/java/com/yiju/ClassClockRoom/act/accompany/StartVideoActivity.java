package com.yiju.ClassClockRoom.act.accompany;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.PlaySDK.IPlaySDK;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Login_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.AccompanyRead;
import com.yiju.ClassClockRoom.common.NetWebViewClient;
import com.yiju.ClassClockRoom.common.callback.OnClickOrientationListener;
import com.yiju.ClassClockRoom.common.constant.RequestCodeConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.share.ShareDialog;
import com.yiju.ClassClockRoom.fragment.AccompanyReadFragment;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.OrientationManager;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * 陪读视频播放页面
 *
 * @author geliping
 */
public class StartVideoActivity extends BaseActivity implements
        OnClickListener, OnClickOrientationListener {

    @ViewInject(R.id.head_title)
    private TextView head_title;//头部标题
    @ViewInject(R.id.head_back)
    private ImageView head_back;//返回图标
    @ViewInject(R.id.start_video_h_back)
    private ImageView start_video_h_back;//横屏返回图标
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;//分享按钮
    @ViewInject(R.id.play_sufaceview)
    private SurfaceView play_sufaceview;// 视频播放控件
    @ViewInject(R.id.play_video_bar)
    private ProgressBar play_video_bar;// 加载进度
    @ViewInject(R.id.play_default_bg)
    private ImageView play_default_bg_icon;// 默认播放背景图片
    @ViewInject(R.id.play_video_image)
    private ImageView play_video_image;// 播放视频大图标
    @ViewInject(R.id.play_video)
    private ImageView play_video;// 播放视频小图标
    @ViewInject(R.id.video_zoom)
    private ImageView video_zoom;// 视频缩放
    @ViewInject(R.id.video_name)
    private TextView video_name;// 视频名称
    @ViewInject(R.id.start_room_address_txt)
    private TextView start_room_address_txt;// 上课地址
    @ViewInject(R.id.web_referrer_teach)
    private WebView web_referrer_teach;// web网页
    @ViewInject(R.id.start_video_h_head_layout)
    private RelativeLayout _h_head_layout;// 横屏头部
    @ViewInject(R.id.start_video_v_head_layout)
    private LinearLayout _v_head_layout;// 竖屏幕头部
    @ViewInject(R.id.play_video_relayout)
    private RelativeLayout play_video_relayout;// 播放UI布局
    @ViewInject(R.id.play_video_operation_layout)
    private RelativeLayout play_video_operation_layout;// 播放菜单栏布局
    /**
     * 无WIFI显示界面
     */
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    /**
     * 刷新
     */
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;
    private WebSettings setting;
    private AccompanyRead accompanyRead;
    private boolean isLogin;

    // ===============================视频相关变量=========================

    private OrientationManager orientationManager;// 横竖屏管理
    private int currentScreenState;// 当前的横竖屏状态
    private static Return_Value_Info_t m_ReValue = new Return_Value_Info_t();
    private static long m_loginHandle = 0;
    private int fmRet;
    private int m_nPort = 0;
    private int mTimeOut = 30 * 1000;
    private int m_nSeq = 0;
    private byte[] m_szCameraId = null;
    private boolean isFirstLoad = false;
    private boolean isPlaying = false;
    private fMediaDataCallback fmCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int nType = 1;
        IDpsdkCore.DPSDK_Create(nType, m_ReValue);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_start_video_layout;
    }

    /**
     * 数据传递初始化
     */
    @Override
    public void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            accompanyRead = intent
                    .getParcelableExtra(AccompanyReadFragment.PARAM_STRING_ACCOMPANY_READ_INFO);
            if (accompanyRead != null) {
                m_szCameraId = accompanyRead.getVideo_id().getBytes();
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        setting = web_referrer_teach.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDefaultTextEncodingName("GBK");
        web_referrer_teach.setWebViewClient(new NetWebViewClient());
        orientationManager = new OrientationManager(StartVideoActivity.this);
        orientationManager.setIsAutoInduction(false);
    }

    /**
     * 控件监听初始化
     */
    @Override
    public void initListener() {
        head_back.setOnClickListener(this);
        start_video_h_back.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        play_video.setOnClickListener(this);
        play_video_image.setOnClickListener(this);
        video_zoom.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        orientationManager.setOnClickOrientationListener(this);
    }

    @Override
    public void initData() {
        head_title.setText(UIUtils.getString(R.string.accompany_read_online));
        RefreshWeb();
        if (accompanyRead != null) {
            video_name.setText(accompanyRead.getName());
            start_room_address_txt.setText(accompanyRead.getAddress());
            if (NetWorkUtils.getNetworkStatus(this)) {
                ly_wifi.setVisibility(View.GONE);
                new LoginVideoTask().execute();
            } else {
                ly_wifi.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_video:
            case R.id.play_video_image:
                if (isPlaying) { // 暂停
                    pauseVideo();
                } else { // 播放
                    playVideo();
                }
                break;
            case R.id.video_zoom:
                orientationManager.clickChange();
                break;
            case R.id.head_back:
                finishPage();
                break;
            case R.id.start_video_h_back:
                orientationManager.clickChange();
                break;
            case R.id.head_right_relative:
                if (accompanyRead != null) {
                    ShareDialog
                            .getInstance()
                            .setCurrent_Type(ShareDialog.Type_Share_Accompany_Video)
                            .setSchool_name(accompanyRead.getName())
                            .setStart_time(accompanyRead.getStartTimeStr())
                            .setEnd_time(accompanyRead.getEndTimeStr())
                            .setVideo_pas(accompanyRead.getVideo_password())
                            .setVid(accompanyRead.getVideo_id()).showDialog();
                }
                break;
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    new LoginVideoTask().execute();

                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    // ====================================视频相关============================================

    /**
     * 登录到视频连接
     */
    private class LoginVideoTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            if (m_loginHandle != 0) {
                IDpsdkCore.DPSDK_Logout(m_ReValue.nReturnValue, 30000);
                m_loginHandle = 0;
            }
            Login_Info_t loginInfo = new Login_Info_t();
            loginInfo.szIp = accompanyRead.getVideo_ip().getBytes();
            loginInfo.nPort = Integer.parseInt(accompanyRead.getVideo_port());
            loginInfo.szUsername = accompanyRead.getVideo_username().getBytes();
            loginInfo.szPassword = accompanyRead.getVideo_password().getBytes();
            loginInfo.nProtocol = 2;
            return IDpsdkCore.DPSDK_Login(m_ReValue.nReturnValue,
                    loginInfo, 30000);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0) {
                m_loginHandle = 1;
                IDpsdkCore.DPSDK_SetCompressType(m_ReValue.nReturnValue, 0);
                initPlayVideo();
            } else {
                UIUtils.showToastSafe(R.string.fail_load);
                m_loginHandle = 0;
            }
        }
    }

    /**
     * 初始化视频播放控件
     */
    private void initPlayVideo() {
        m_nPort = IPlaySDK.PLAYGetFreePort();
        SurfaceHolder holder = play_sufaceview.getHolder();
        holder.addCallback(new Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                IPlaySDK.InitSurface(m_nPort, play_sufaceview);
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                holder.setFixedSize(width, height);
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        fmCallback = new fMediaDataCallback() {

            @Override
            public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
                               byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {

                fmRet = IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
                if (!isFirstLoad) {
                    isFirstLoad = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            play_video_bar.setVisibility(View.GONE);
                        }
                    });
                    /*if (fmRet == 1) { // 解码成功
                    } else { // 解码失败
                    }*/
                }
            }
        };
    }

    /**
     * 视频播放
     */
    private void playVideo() {
        if (!StartRealPlay()) {
            return;
        }
        try {
            Return_Value_Info_t retVal = new Return_Value_Info_t();
            Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t();

            System.arraycopy(m_szCameraId, 0, getRealStreamInfo.szCameraId, 0,
                    m_szCameraId.length);
            getRealStreamInfo.nMediaType = 3;
            getRealStreamInfo.nRight = 1;
            getRealStreamInfo.nStreamType = 1;
            getRealStreamInfo.nTransType = 1;
            Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
            IDpsdkCore.DPSDK_GetChannelInfoById(getDPSDKHandle(), m_szCameraId,
                    ChannelInfo);
            int ret = IDpsdkCore.DPSDK_GetRealStream(getDPSDKHandle(), retVal,
                    getRealStreamInfo, fmCallback, mTimeOut);
            if (ret == 0) {
                setPlayVideoUI();
                m_nSeq = retVal.nReturnValue;
                isPlaying = true;
            } else {
                if (1000557 == ret) {
                    UIUtils.showToastSafe(R.string.video_play_nofund);
                }
                isPlaying = false;
                StopRealPlay();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 暂停播放
    private void pauseVideo() {
        int ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(getDPSDKHandle(),
                m_nSeq, mTimeOut);
        if (ret == 0) {// 关闭成功！
            isPlaying = false;
            isFirstLoad = false;
        } else {// 关闭失败！
            isPlaying = true;
            isFirstLoad = true;
        }
        StopRealPlay();
    }

    /**
     * 停止实时播放
     */
    private void StopRealPlay() {
        setStopVideoUI();
        try {
            IPlaySDK.PLAYStopSoundShare(m_nPort);
            IPlaySDK.PLAYStop(m_nPort);
            IPlaySDK.PLAYCloseStream(m_nPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始实时播放并返回状态
     */
    private boolean StartRealPlay() {
        if (play_sufaceview == null)
            return false;

        boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort, null, 0,
                1500 * 1024) != 0;
        if (bOpenRet) {
            boolean bPlayRet = IPlaySDK.PLAYPlay(m_nPort, play_sufaceview) != 0;
            if (bPlayRet) {
                boolean bSuccess = IPlaySDK.PLAYPlaySoundShare(m_nPort) != 0;
                if (!bSuccess) {
                    IPlaySDK.PLAYStop(m_nPort);
                    IPlaySDK.PLAYCloseStream(m_nPort);
                    return false;
                }
            } else {
                IPlaySDK.PLAYCloseStream(m_nPort);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 设置播放状态页面UI
     */
    private void setPlayVideoUI() {
        play_default_bg_icon.setVisibility(View.GONE);
        play_video_image.setVisibility(View.GONE);
        play_video_bar.setVisibility(View.VISIBLE);
        play_video.setImageResource(R.drawable.pause02);
    }

    /**
     * 设置暂停状态页面UI
     */
    private void setStopVideoUI() {
        play_video_image.setVisibility(View.VISIBLE);
        play_video_bar.setVisibility(View.GONE);
        play_video.setImageResource(R.drawable.play_v_icon);
    }

    private static int getDPSDKHandle() {
        if (m_loginHandle == 1)
            return m_ReValue.nReturnValue;
        else
            return 0;
    }

    /**
     * 退出视频
     */
    private void LogoutVideo() {
        if (m_loginHandle == 0) {
            return;
        }
        int nRet = IDpsdkCore.DPSDK_Logout(m_ReValue.nReturnValue, 30000);

        if (0 == nRet) {
            m_loginHandle = 0;
        }
    }

    // ========================================切换横竖屏相关======================================

    @Override
    public void landscape() {
        if (play_video_relayout != null) {
            currentScreenState = Configuration.ORIENTATION_LANDSCAPE;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            updateVideoView(true);
        }
    }

    @Override
    public void portrait() {
        if (play_video_relayout != null) {
            currentScreenState = Configuration.ORIENTATION_PORTRAIT;
            updateVideoView(false);
        }
    }

    /**
     * 根据横竖屏切换UI页面
     *
     * @param isFullscreen b
     */
    private void updateVideoView(boolean isFullscreen) {
        if (_v_head_layout == null || _h_head_layout == null
                || play_video_relayout == null
                || play_video_operation_layout == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (isFullscreen) {// 横屏
            _v_head_layout.setVisibility(View.GONE);
            _h_head_layout.setVisibility(View.VISIBLE);
            video_zoom.setImageResource(R.drawable.esc_exit);

            changeOperationLayoutHeight(play_video_relayout,
                    metrics.widthPixels, metrics.heightPixels);

            changeOperationLayoutHeight(play_video_operation_layout,
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                    UIUtils.getDimens(R.dimen.DIMEN_50DP));
        } else {// 竖屏
            _v_head_layout.setVisibility(View.VISIBLE);
            _h_head_layout.setVisibility(View.GONE);
            video_zoom.setImageResource(R.drawable.full_screen_view);

            changeOperationLayoutHeight(play_video_relayout,
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                    UIUtils.getDimens(R.dimen.DIMEN_208DP));

            changeOperationLayoutHeight(play_video_operation_layout,
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                    UIUtils.getDimens(R.dimen.DIMEN_34DP));
        }
    }

    /**
     * 修改控件ViewGroup的宽高
     */
    private void changeOperationLayoutHeight(View viewLayout, int width,
                                             int height) {
        if (viewLayout.getParent() instanceof RelativeLayout) {
            android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) viewLayout
                    .getLayoutParams();
            params.width = width;
            params.height = height;
            viewLayout.setLayoutParams(params);
        } else {
            android.widget.LinearLayout.LayoutParams params =
                    (android.widget.LinearLayout.LayoutParams) viewLayout.getLayoutParams();
            params.width = width;
            params.height = height;
            viewLayout.setLayoutParams(params);
        }
    }

    // ======================================web相关=======================================

    /**
     * 刷新web页
     */
    private void RefreshWeb() {
        isLogin = SharedPreferencesUtils.getBoolean(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_isLogin), false);
        String uid = "-1".equals(StringUtils.getUid()) ? "" : StringUtils.getUid();
        web_referrer_teach.loadUrl(
                String.format(
                        UrlUtils.H5_ACCOMPANY_TEACHER,
                        uid
                )
        );
    }

    // ======================================end=======================================

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (currentScreenState == Configuration.ORIENTATION_LANDSCAPE) {
                orientationManager.clickChange();
            } else {
                finishPage();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodeConstant.Web_Skip_Login
                && resultCode == RESULT_OK
                || requestCode == RequestCodeConstant.Web_Finish_Refresh
                && resultCode == RESULT_OK) {
            RefreshWeb();
        }
    }

    @Override
    public void onBackPressed() {
        finishPage();
    }

    //关闭页面
    private void finishPage() {
        pauseVideo();
        web_referrer_teach.removeAllViews();
        web_referrer_teach.destroy();
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this,0);
    }

    @Override
    protected void onDestroy() {
        LogoutVideo();
        super.onDestroy();
    }


    @Override
    public String getPageName() {
        return getString(R.string.title_act_startvideo);
    }

}
