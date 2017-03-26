package com.yiju.ClassClockRoom.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/16 10:16
 * ----------------------------------------
 */
public class MineOrganizationPagerAdapter extends PagerAdapter {

    private List<ImageView> imageViews = new ArrayList<>();
    private VpClickListener vpClickListener;

    public MineOrganizationPagerAdapter(List<ImageView> imageViews, VpClickListener vpClickListener) {
        this.imageViews = imageViews;
        this.vpClickListener = vpClickListener;
    }

    public void setData(List<ImageView> views) {
        this.imageViews = views;
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (!(position >= getCount())) {
            container.removeView(imageViews.get(position));
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = imageViews.get(position);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpClickListener.vpClick();
            }
        });
        container.addView(v);
        return v;
    }

    public interface VpClickListener {
        void vpClick();
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }


}
