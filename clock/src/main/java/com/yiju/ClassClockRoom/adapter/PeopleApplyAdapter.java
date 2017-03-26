package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.People_Apply_Courselist;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * Created by Sandy on 2016/6/27/0027.
 */
public class PeopleApplyAdapter extends BaseAdapter {

    private List<People_Apply_Courselist> mLists;
    ViewHolder holder;

    public PeopleApplyAdapter(List<People_Apply_Courselist> lists) {
        this.mLists = lists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public People_Apply_Courselist getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(UIUtils.getContext(), R.layout.item_person_course_people_apply, null);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        assignViews(view);
        People_Apply_Courselist info = getItem(i);
        if (null != info) {
            Glide.with(UIUtils.getContext()).load(info.getPic()).into(holder.iv_item_people_apply_pic);
            holder.tv_item_people_apply_nickname.setText(info.getNickname());
            holder.tv_item_people_apply_name.setText(info.getContactname());
            holder.tv_item_people_apply_tel.setText(info.getMobile());
            holder.tv_item_people_apply_time.setText(info.getDaterange());
            holder.tv_item_people_apply_all_hour.setText(
                    String.format(
                            UIUtils.getString(R.string.total_hour_short_text),
                            info.getTotaltime() + ""
                    ));
            holder.tv_item_people_apply_price.setText(info.getTotalfee() + "");
        }
        return view;
    }

    private void assignViews(View v) {//已报名: 5/12

        holder.iv_item_people_apply_pic = (ImageView) v.findViewById(R.id.iv_item_people_apply_pic);
        holder.tv_item_people_apply_nickname = (TextView) v.findViewById(R.id.tv_item_people_apply_nickname);
        holder.tv_item_people_apply_name = (TextView) v.findViewById(R.id.tv_item_people_apply_name);
        holder.tv_item_people_apply_tel = (TextView) v.findViewById(R.id.tv_item_people_apply_tel);
        holder.tv_item_people_apply_time = (TextView) v.findViewById(R.id.tv_item_people_apply_time);
        holder.tv_item_people_apply_all_hour = (TextView) v.findViewById(R.id.tv_item_people_apply_all_hour);
        holder.tv_item_people_apply_price = (TextView) v.findViewById(R.id.tv_item_people_apply_price);

    }

    class ViewHolder {
        private ImageView iv_item_people_apply_pic;
        private TextView tv_item_people_apply_nickname;
        private TextView tv_item_people_apply_name;
        private TextView tv_item_people_apply_tel;
        private TextView tv_item_people_apply_time;
        private TextView tv_item_people_apply_all_hour;
        private TextView tvItemPersonCourseStatus;
        private TextView tv_item_people_apply_price;
    }
}
