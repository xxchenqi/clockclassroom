package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.SchoolRight;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * Created by Sandy on 2016/9/5/0005.
 */
public class SchoolRightAdapter extends BaseAdapter{

    private int clickPosition = Integer.MAX_VALUE;
    public void setClickPosition(int clickPosition) {
        this.clickPosition = clickPosition;
    }

    private List<SchoolRight> mLists;
    ViewHolder holder;


    public SchoolRightAdapter(List<SchoolRight> list) {
        this.mLists = list;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public SchoolRight getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            holder = new ViewHolder();
            view = View.inflate(UIUtils.getContext(), R.layout.item_school_left, null);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_left_school = (TextView) view.findViewById(R.id.tv_left_school);

        if(clickPosition == position){
            holder.tv_left_school.setTextColor(UIUtils.getColor(R.color.app_theme_color));
        }else {
            holder.tv_left_school.setTextColor(UIUtils.getColor(R.color.order_black));
        }
        holder.tv_left_school.setText(mLists.get(position).getSchool_name());
        return view;
    }
    public class ViewHolder{
        private TextView tv_left_school;
    }
}
