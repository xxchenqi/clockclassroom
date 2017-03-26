package com.yiju.ClassClockRoom.act.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.bugtags.library.Bugtags;
import com.lidroid.xutils.ViewUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.callback.InitView;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.LockScreenControl;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends Activity implements InitView {

    public Context mContext;
    protected SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityControlManager.getInstance().pushOneActivity(this);
        initIntent();
        super.onCreate(savedInstanceState);
        setContentView(setContentViewId());
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
        BaseApplication.setmForegroundActivity(this);
        mContext = this;
        EventBus.getDefault().register(this);
        ViewUtils.inject(this);
        initView();
        initListener();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    public abstract String getPageName();

    public abstract int setContentViewId();

    @Override
    public void initIntent() {

    }

    @Override
    public void initListener() {

    }


    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void baseRefreshEvent(ClassEvent<Object> event) {
        if (event.isPointCurrent()
                && !CommonUtil.isTopActivity(getClass().getName(), UIUtils.getContext())) {
            return;
        }
        onRefreshEvent(event);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        CountControl.getInstance().skipRuning(this);
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
        //umeng session统计
        MobclickAgent.onPageStart(getLocalClassName());//页面统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ProgressDialog.getInstance().dismiss();
        if (BaseApplication.BugTagsFlag) {
            Bugtags.onPause(this);
        }
        //umeng session统计
        MobclickAgent.onPageEnd(getLocalClassName());//页面统计
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (BaseApplication.BugTagsFlag) {
            Bugtags.onDispatchTouchEvent(this, ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        CountControl.getInstance().skipUnRuning(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LockScreenControl.getInstance().unRegisterLockReceiver(this);
        ActivityControlManager.getInstance().popOneActivity(this);
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 0);
    }
}
