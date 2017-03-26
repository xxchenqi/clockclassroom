package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.WatchlistCourseResult;
import com.yiju.ClassClockRoom.common.callback.ListItemClickTwoData;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:我的关注_课程适配器
 * <p/>
 * 作者: wh
 * <p/>
 * 时间: 2016/8/9
 * ----------------------------------------
 */
public class WatchlistCourseAdapter extends BaseAdapter {
    //数据源
    private List<WatchlistCourseResult.DataEntity> mList;
    private ListItemClickTwoData listener;

    public WatchlistCourseAdapter(List<WatchlistCourseResult.DataEntity> list,
                                  ListItemClickTwoData listItemClickTwoData) {
        this.mList = list;
        this.listener = listItemClickTwoData;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(UIUtils.getContext(), R.layout.item_watchlist_course, null);
            setViewHolder(view);
        } else if (((ViewHolder) convertView.getTag()).needInflate) {
            view = View.inflate(UIUtils.getContext(), R.layout.item_watchlist_course, null);
            setViewHolder(view);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        WatchlistCourseResult.DataEntity entity = mList.get(position);
        if (entity == null) {
            return null;
        }
        String pic = entity.getPic();
        Glide.with(UIUtils.getContext()).load(pic).into(holder.iv_course_pic);
        String status = entity.getCourse_status();
        if (StringUtils.isNotNullString(status)) {
            holder.tv_course_status.setText(status);
            if (UIUtils.getString(R.string.person_course_status_applying_single).equals(status)) {
                holder.tv_course_status.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            } else if (UIUtils.getString(R.string.person_course_status_cancel).equals(status)) {
                holder.tv_course_status.setTextColor(UIUtils.getColor(R.color.color_gay_99));
            } else if (UIUtils.getString(R.string.person_course_status_finish).equals(status)) {
                holder.tv_course_status.setTextColor(UIUtils.getColor(R.color.color_gay_f5));
            }
        }
        holder.tv_course_name.setText(entity.getCourse_name());
        holder.tv_single_price.setText(
                String.format(
                        UIUtils.getString(R.string.string_format_course_money),
                        entity.getSingle_price()
                ));
        holder.tv_count_class_time.setText(
                String.format(
                        UIUtils.getString(R.string.course_how_much_total),
                        entity.getCountclasstime()
                ));
        holder.tv_date.setText(
                String.format(
                        UIUtils.getString(R.string.to_symbol),
                        entity.getStart_date(),
                        entity.getEnd_date()
                ));
        holder.iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteItem(view, position);
            }
        });
        return view;
    }

    private void setViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.iv_course_pic = (ImageView) convertView.findViewById(R.id.iv_course_pic);
        holder.tv_course_status = (TextView) convertView.findViewById(R.id.tv_course_status);
        holder.tv_course_name = (TextView) convertView.findViewById(R.id.tv_course_name);
        holder.tv_single_price = (TextView) convertView.findViewById(R.id.tv_single_price);
        holder.tv_count_class_time = (TextView) convertView.findViewById(R.id.tv_count_class_time);
        holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
        holder.iv_collect = (ImageView) convertView.findViewById(R.id.iv_collect);
        holder.needInflate = false;
        convertView.setTag(holder);
    }

    public class ViewHolder {
        public ImageView iv_course_pic;
        public TextView tv_course_status;
        public TextView tv_course_name;
        public TextView tv_single_price;
        public TextView tv_count_class_time;
        public TextView tv_date;
        public ImageView iv_collect;
        public boolean needInflate;
    }
}
