package com.yiju.ClassClockRoom.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Sandy on 2016/6/20/0020.
 */
public class CourseDetailPagerAdapter extends PagerAdapter {

    private List<ImageView> mLists;
    public CourseDetailPagerAdapter(ViewPager vpCourseDetail, List<ImageView> pics) {
        this.mLists = pics;
    }

    @Override
    public int getCount() {
//        return Integer.MAX_VALUE;
        return mLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        /*int currentPosition = position % mLists.size();
        if(currentPosition < 0){
            currentPosition = position+mLists.size();
        }
        ImageView view = mLists.get(currentPosition);*/
        ImageView view = mLists.get(position);
        ViewParent parent = view.getParent();
        if(null != parent){
            ViewGroup vp = (ViewGroup)parent;
            vp.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


}
