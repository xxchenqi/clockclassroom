package com.yiju.ClassClockRoom.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.WatchlistTeacherResult;
import com.yiju.ClassClockRoom.common.callback.ListItemClickTwoData;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:我的关注_老师适配器
 * <p/>
 * 作者: wh
 * <p/>
 * 时间: 2016/8/9
 * ----------------------------------------
 */
public class WatchlistTeacherAdapter extends BaseAdapter {
    private List<WatchlistTeacherResult.DataEntity> mList;
    private ListItemClickTwoData listener;

    public WatchlistTeacherAdapter(List<WatchlistTeacherResult.DataEntity> list,
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(UIUtils.getContext(), R.layout.item_watchlist_teacher, null);
            setViewHolder(view);
        } else if (((ViewHolder) convertView.getTag()).needInflate) {
            view = View.inflate(UIUtils.getContext(), R.layout.item_watchlist_teacher, null);
            setViewHolder(view);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();
        //课程
        WatchlistTeacherResult.DataEntity entity = mList.get(position);
        if (entity != null) {
            if ("1".equals(entity.getShow_teacher())) {//展示
                holder.ll_show_teacher_detail.setVisibility(View.VISIBLE);
                holder.tv_show_tips.setVisibility(View.GONE);
                Glide.with(UIUtils.getContext()).load(entity.getAvatar()).into(holder.iv_item_teacher_pic);
            } else if ("0".equals(entity.getShow_teacher())) {//隐藏
                holder.ll_show_teacher_detail.setVisibility(View.GONE);
                holder.tv_show_tips.setVisibility(View.VISIBLE);
                holder.iv_item_teacher_pic.setBackground(UIUtils.getDrawable(R.drawable.clock_wait));
            }
            String sex = entity.getSex();
            if (StringUtils.isNotNullString(sex)) {
                if ("0".equals(sex)) {//未填
                    holder.iv_sex.setVisibility(View.GONE);
                } else if ("1".equals(sex)) {//男
                    holder.iv_sex.setVisibility(View.VISIBLE);
                    holder.iv_sex.setBackgroundResource(R.drawable.male);
                } else if ("2".equals(sex)) {//女
                    holder.iv_sex.setVisibility(View.VISIBLE);
                    holder.iv_sex.setBackgroundResource(R.drawable.female);
                }
            }
            holder.tv_teacher_name.setText(entity.getReal_name());
            //设置标签
            if (StringUtils.isNotNullString(entity.getTag())) {
                holder.tv_tags.setText(entity.getTag());
                holder.tv_tags.setVisibility(View.VISIBLE);
            } else {
                holder.tv_tags.setVisibility(View.GONE);
            }
            holder.tv_org_name.setText(entity.getOrg_name());
            holder.tv_course_info.setText(entity.getCourse_name());
            holder.iv_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteItem(view, position);
                }
            });
        }
        return view;
    }

    private void setViewHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.iv_item_teacher_pic = (ImageView) convertView.findViewById(R.id.iv_item_teacher_pic);
        holder.ll_show_teacher_detail = (LinearLayout) convertView.findViewById(R.id.ll_show_teacher_detail);
        holder.tv_show_tips = (TextView) convertView.findViewById(R.id.tv_show_tips);
        holder.iv_sex = (ImageView) convertView.findViewById(R.id.iv_sex);
        holder.tv_teacher_name = (TextView) convertView.findViewById(R.id.tv_teacher_name);
        holder.tv_tags = (TextView) convertView.findViewById(R.id.tv_tags);
        holder.tv_org_name = (TextView) convertView.findViewById(R.id.tv_org_name);
        holder.tv_course_info = (TextView) convertView.findViewById(R.id.tv_course_info);
        holder.iv_collect = (ImageView) convertView.findViewById(R.id.iv_collect);
        holder.needInflate = false;
        convertView.setTag(holder);
    }

    public class ViewHolder {
        public ImageView iv_item_teacher_pic;
        public TextView tv_teacher_name;
        public LinearLayout ll_show_teacher_detail;
        public TextView tv_show_tips;
        public ImageView iv_sex;
        public TextView tv_tags;
        public TextView tv_org_name;
        public TextView tv_course_info;
        public ImageView iv_collect;
        public boolean needInflate;
    }
}
