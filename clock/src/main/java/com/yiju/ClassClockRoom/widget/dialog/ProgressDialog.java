package com.yiju.ClassClockRoom.widget.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.base.BaseDialog;
import com.yiju.ClassClockRoom.util.ProgressUtil;

public class ProgressDialog extends BaseDialog {
    private static ProgressDialog instance;
    private View view;
    private ImageView progress_image;
    private Activity currentActivity;

    private ProgressDialog(Activity context, int theme) {
        super(context, theme);
        currentActivity = context;
    }

    public synchronized static ProgressDialog getInstance() {
        if (instance == null || instance.isContextChanged()) {
            instance = new ProgressDialog(BaseApplication.getmForegroundActivity(),
                    R.style.CustomProgressDialog);
            instance.setCanceledOnTouchOutside(true);
            // instance.getWindow().getAttributes().gravity = Gravity.CENTER;
        }
        return instance;
    }

    @Override
    public void initView() {
        view = LayoutInflater.from(_last_context).inflate(
                R.layout.customprogressdialog, null);
        progress_image = (ImageView) view.findViewById(R.id.loadingImageView);
        setContentView(view);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {

    }

    @Override
    public void show() {
        if (instance != null && !isContextChanged() && !currentActivity.isFinishing()) {
            ProgressUtil.progressMethod(progress_image, true);
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (instance != null && !isContextChanged() && !currentActivity.isFinishing()) {
            ProgressUtil.progressMethod(progress_image, false);
            super.dismiss();
            instance = null;
        }
    }

}
