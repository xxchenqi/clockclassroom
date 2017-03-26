package com.yiju.ClassClockRoom.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Room;
import com.yiju.ClassClockRoom.bean.TeacherInfoBean;
import com.yiju.ClassClockRoom.fragment.StoreFragment;

import java.io.Serializable;
import java.util.List;

/**
 * 首页适配器
 * Created by wh on 2016/3/3.
 */
public class NewIndexAdapter extends FragmentPagerAdapter {

    //    private List<Fragment> fragments;
    private List<Room> mRoomLists;
    private List<CourseInfo> course_recommend;//推荐课程
    private List<TeacherInfoBean> teacher_recommend;//推荐老师
    private Order2 info;
    private ITagPosRunnable iTagPosRunnable;

    public interface ITagPosRunnable {
        void savePos(int position);
    }

    public NewIndexAdapter(FragmentManager fm, List<Room> mLists, Order2 info,
                           ITagPosRunnable iTagPosRunnable,
                           List<CourseInfo> course_recommendList,
                           List<TeacherInfoBean> teacher_recommendList) {
        super(fm);
//        cleanFragment(fm);
        this.mRoomLists = mLists;
        this.course_recommend = course_recommendList;
        this.teacher_recommend = teacher_recommendList;
        this.info = info;
        this.iTagPosRunnable = iTagPosRunnable;
    }

    @Override
    public Fragment getItem(int position) {
        position = (mRoomLists.size() + position % mRoomLists.size()) % mRoomLists.size();
        Room room = mRoomLists.get(position);
        Fragment fragment = new StoreFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Room", room);
        bundle.putSerializable("course_recommend", (Serializable) course_recommend);
        bundle.putSerializable("teacher_recommend", (Serializable) teacher_recommend);
        if (info != null) {
            if (info.getSid().equals(room.getId())) {
                iTagPosRunnable.savePos(position);
                bundle.putSerializable("info", info);
            }
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mRoomLists.size();
    }

    public void cleanFragment(FragmentManager fm) {
        if (fm != null) {
            if (fm.getFragments() == null) {
                return;
            }
            FragmentTransaction ft = fm.beginTransaction();
            if (ft != null) {
                for (Fragment f : fm.getFragments()) {
                    if (f != null) {
                        ft.remove(f);
                    }
                }
                ft.commitAllowingStateLoss();
                fm.executePendingTransactions();
            }
        }
        notifyDataSetChanged();
    }
}
