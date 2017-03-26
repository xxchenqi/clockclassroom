package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.SchoolLeft;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * Created by Sandy on 2016/9/5/0005.
 */
public class SchoolLeftAdapter extends BaseAdapter{

    private int clickPosition = Integer.MAX_VALUE;
    ViewHolder holder;

    public void setClickPosition(int clickPosition) {
        this.clickPosition = clickPosition;
    }

    private List<SchoolLeft> mLists;

    public SchoolLeftAdapter(List<SchoolLeft> mListLefts) {
        this.mLists = mListLefts;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public SchoolLeft getItem(int i) {
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
        holder.rl_left_school = (RelativeLayout) view.findViewById(R.id.rl_left_school);

        if(clickPosition == position){
            holder.rl_left_school.setBackgroundColor(UIUtils.getColor(R.color.gray_f5f5f5));
            holder.tv_left_school.setTextColor(UIUtils.getColor(R.color.app_theme_color));
        }else {
            holder.rl_left_school.setBackgroundColor(UIUtils.getColor(R.color.white));
            holder.tv_left_school.setTextColor(UIUtils.getColor(R.color.order_black));
        }
        holder.tv_left_school.setText(mLists.get(position).getDist_name());
        return view;
    }

    public class ViewHolder{
        private TextView tv_left_school;
        private RelativeLayout rl_left_school;
    }
}
