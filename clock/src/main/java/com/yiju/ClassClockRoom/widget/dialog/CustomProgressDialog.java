package com.yiju.ClassClockRoom.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.BaseApplication;

public class CustomProgressDialog extends Dialog {
    private static Context currentContext = null;
    private static CustomProgressDialog customProgressDialog = null;

    public CustomProgressDialog(Context context) {
        super(context);
        currentContext = context;
    }

    private CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        currentContext = context;
    }

    public static CustomProgressDialog createDialog() {
        if (customProgressDialog == null || currentContext == null ||
                !currentContext.equals(BaseApplication.getmForegroundActivity())) {
            customProgressDialog = new CustomProgressDialog(BaseApplication.getmForegroundActivity(),
                    R.style.CustomProgressDialog);
            customProgressDialog.setContentView(R.layout.customprogressdialog);
            if (customProgressDialog.getWindow() != null) {
                customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            }
        }
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }

        ImageView imageView = (ImageView) customProgressDialog
                .findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        animationDrawable.start();
    }

    public CustomProgressDialog setTitle(String strTitle) {
        return customProgressDialog;
    }

}
