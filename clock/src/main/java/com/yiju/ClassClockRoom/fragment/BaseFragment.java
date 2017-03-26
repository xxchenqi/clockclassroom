package com.yiju.ClassClockRoom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.callback.InitView;
import com.yiju.ClassClockRoom.util.net.ClassEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseFragment extends Fragment implements InitView {
    /**
     * 当前activity
     */
    Activity fatherActivity;
    public View currentView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fatherActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(setContentViewId(), null, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initIntent();
        initView();
        initData();
        initListener();
        return currentView;
    }

    protected abstract void initView();

    protected abstract void initData();

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
        onRefreshEvent(event);
    }

    // fragment之间的切换
    protected void changeFragment(Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        beginTransaction.replace(R.id.fl_container, fragment);
        beginTransaction.commit();
    }

    public abstract int setContentViewId();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
//        CountControl.getInstance().skipRuning(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        CountControl.getInstance().skipUnRuning(fatherActivity);
    }

    public abstract String getPageName();


}
