package com.yiju.ClassClockRoom.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yiju.ClassClockRoom.bean.ClassroomArrangeData;

import java.util.ArrayList;
import java.util.List;

/**
 * 课室用途选择 viewpagerAdapter
 * Created by wh on 2016/5/16.
 */
public class ClassRoomTypePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<ClassroomArrangeData> classroom_typeData;
    private List<Fragment> fragments;

    public ClassRoomTypePagerAdapter(
            FragmentManager fm,
            List<Fragment> fragments,
            ArrayList<ClassroomArrangeData> classroom_typeData) {
        super(fm);
        this.fragments = fragments;
        this.classroom_typeData = classroom_typeData;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return classroom_typeData.get(position).getPuse_name();//页卡标题
    }
}
