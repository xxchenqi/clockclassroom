package com.yiju.ClassClockRoom.act.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.bugtags.library.Bugtags;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.control.LockScreenControl;
import com.yiju.ClassClockRoom.fragment.BaseFragment;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

public abstract class BaseFragmentActivity extends FragmentActivity {

    protected SystemBarTintManager tintManager;

    public abstract String getPageName();

    protected boolean isRuning;

    @Override
    protected void onCreate(Bundle arg0) {
        ActivityControlManager.getInstance().pushOneActivity(this);
        super.onCreate(arg0);
        BaseApplication.setmForegroundActivity(this);
        if (BaseApplication.ImmersionFlag) {
            tintManager = new SystemBarTintManager(this);
            //透明状态栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置状态栏颜色默认为绿色
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setTintColor(UIUtils.getColor(R.color.app_theme_color));
        }
    }

    public BaseFragment getCurrentFragment() {
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        CountControl.getInstance().skipRuning(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BaseApplication.setmForegroundActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LockScreenControl.getInstance().registerLockReceiver(this);
        if (BaseApplication.BugTagsFlag) {
            Bugtags.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ProgressDialog.getInstance().dismiss();
        if (BaseApplication.BugTagsFlag) {
            Bugtags.onPause(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (BaseApplication.BugTagsFlag) {
            Bugtags.onDispatchTouchEvent(this, ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LockScreenControl.getInstance().unRegisterLockReceiver(this);
        ActivityControlManager.getInstance().popOneActivity(this);
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 0);
    }

}
