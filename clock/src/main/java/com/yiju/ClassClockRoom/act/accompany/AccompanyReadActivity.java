package com.yiju.ClassClockRoom.act.accompany;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseFragmentActivity;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.fragment.BaseFragment;
import com.yiju.ClassClockRoom.control.FragmentFactory;

/**
 * Created by geLiPing on 2016/8/10.
 */
public class AccompanyReadActivity extends BaseFragmentActivity{
    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accompany_read);
        changeFragment();
    }


    private void changeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        currentFragment = FragmentFactory.createFragment(FragmentFactory.TAB_VIDEO);
        beginTransaction.replace(R.id.fl_container, currentFragment);
        beginTransaction.commit();
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
    }
}
