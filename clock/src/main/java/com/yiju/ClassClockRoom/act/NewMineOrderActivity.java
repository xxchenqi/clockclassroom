package com.yiju.ClassClockRoom.act;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.fragment.BaseFragment;
import com.yiju.ClassClockRoom.fragment.FragmentOrderClassRoom;
import com.yiju.ClassClockRoom.fragment.FragmentOrderCourse;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/6/14 09:44
 * ----------------------------------------
 */
public class NewMineOrderActivity extends FragmentActivity implements View.OnClickListener {
    //传参的状态
    public static final String STATUS = "status";
    //待支付的数量传参
    public static final String COUNT = "count";
    // 待支付
    public static final String STATUS_WAIT_PAY = "-1";
    // 已支付
    public static final String STATUS_USE = "1";
    // 已完成
    public static final String STATUS_FINISH = "99";
    // 全部订单
    public static final String STATUS_ALL = "ALL";

    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.tl_tab_order)
    private TabLayout tl_tab_order;

    @ViewInject(R.id.vp_viewpager_order)
    private ViewPager vp_viewpager_order;

    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mine_order);
        ViewUtils.inject(this);

        initData();
        initListener();
    }

    protected void initData() {
        status = getIntent().getStringExtra(STATUS);
        head_title.setText(UIUtils.getString(R.string.all_order));
        FragmentOrderPagerAdapter adapter = new FragmentOrderPagerAdapter(getSupportFragmentManager());
        vp_viewpager_order.setAdapter(adapter);
        tl_tab_order.setupWithViewPager(vp_viewpager_order);
    }

    public void initListener() {
        head_back_relative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
        }
    }

    class FragmentOrderPagerAdapter extends FragmentPagerAdapter {

        public FragmentOrderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return "课室";
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
                case 0: {//课室
                    BaseFragment fragment = new FragmentOrderClassRoom();
                    Bundle bundle = new Bundle();
                    bundle.putString("status", status);
                    fragment.setArguments(bundle);
                    return fragment;
                }
                case 1: {//课程
                    BaseFragment fragment = new FragmentOrderCourse();
                    Bundle bundle = new Bundle();
                    bundle.putString("status", status);
                    fragment.setArguments(bundle);
                    return fragment;
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
