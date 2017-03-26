package com.yiju.ClassClockRoom.act;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseFragmentActivity;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.fragment.FragmentWatchlistCourse;
import com.yiju.ClassClockRoom.fragment.FragmentWatchlistTeacher;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * 我的关注
 * Created by wh on 2016/8/9.
 */
public class MineWatchlistActivity extends BaseFragmentActivity implements View.OnClickListener {
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.tl_tab_watchlist)
    private TabLayout tl_tab_watchlist;

    @ViewInject(R.id.vp_viewpager_watchlist)
    private ViewPager vp_viewpager_watchlist;

    @Override
    public String getPageName() {
        return getString(R.string.title_act_common_show_webpage_for_attention);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_watchlist);
        ViewUtils.inject(this);

        initData();
        initListener();
    }

    private void initData() {
        head_title.setText(UIUtils.getString(R.string.label_myAttention));
        FragmentWatchlistPagerAdapter adapter = new FragmentWatchlistPagerAdapter(getSupportFragmentManager());
        vp_viewpager_watchlist.setAdapter(adapter);
        tl_tab_watchlist.setupWithViewPager(vp_viewpager_watchlist);
    }

    private void initListener() {
        head_back_relative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this,3);
    }

    class FragmentWatchlistPagerAdapter extends FragmentPagerAdapter {

        public FragmentWatchlistPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return "老师";
                }
                case 1: {
                    return "课程";
                }
                default:
                    return "not found";
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {//老师
//                    BaseFragment fragment = new FragmentWatchlistTeacher();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("status", "0");
//                    fragment.setArguments(bundle);
                    return new FragmentWatchlistTeacher();
                }
                case 1: {//课程
//                    BaseFragment fragment = new FragmentWatchlistCourse();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("status", "1");
//                    fragment.setArguments(bundle);
                    return new FragmentWatchlistCourse();
                }
                default: {
                    break;
                }
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
