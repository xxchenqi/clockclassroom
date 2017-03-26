package com.yiju.ClassClockRoom.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.StringUtils;

/**
 * 作者： 葛立平
 * 视频wifi设置提示框
 * 2016/1/21 18:32
 */
public class VideoWifiDialog implements View.OnClickListener {
    private static VideoWifiDialog instance;
    private Activity mActivity;
    private String mContent;

    private TextView video_wifi_name;
    private TextView video_wifi_cancel;
    private TextView video_wifi_ok;

    private IOnClickListener mListener;

    private AlertDialog dialog;

    private VideoWifiDialog(Activity activity) {
        this.mActivity = activity;
    }


    public static VideoWifiDialog getInstance() {
        synchronized (VideoWifiDialog.class) {
            if (instance == null || instance.mActivity == null || instance.mActivity != BaseApplication.getmForegroundActivity()) {
                instance = new VideoWifiDialog(
                        BaseApplication.getmForegroundActivity());
            }
            return instance;
        }
    }

    public void DrawLayout() {
        View v = LayoutInflater.from(mActivity).inflate(
                R.layout.dialog_video_wifi_layout, null);
        initView(v);
        initDate();
        initListeners();
        dialog = new AlertDialog.Builder(mActivity, R.style.myDialogTheme)
                .create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER); // 设置dialog显示的位置
            window.setBackgroundDrawable(new BitmapDrawable());
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = CommonUtil.getScreenWidth();
            lp.gravity = Gravity.CENTER;
            dialog.setContentView(v, lp);
        }
    }

    private void initListeners() {
        video_wifi_cancel.setOnClickListener(this);
        video_wifi_ok.setOnClickListener(this);
    }

    private void initDate() {
        video_wifi_name.setText(StringUtils.formatNullString(mContent));
    }

    public VideoWifiDialog setContent(String content) {
        mContent = content;
        return instance;
    }

    private void initView(View view) {
        video_wifi_name = (TextView) view.findViewById(R.id.video_wifi_name);
        video_wifi_cancel = (TextView) view.findViewById(R.id.video_wifi_cancel);
        video_wifi_ok = (TextView) view.findViewById(R.id.video_wifi_ok);
        video_wifi_name.setText(StringUtils.formatNullString(mContent));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_wifi_cancel:
                mListener.oncClick(false);
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.video_wifi_ok:
                mListener.oncClick(true);
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
        }
    }

    public VideoWifiDialog setonClickListener(IOnClickListener listener) {
        this.mListener = listener;
        return instance;
    }
}
