package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: wh
 * <p/>
 * 时间: on 2016/7/6 18:06
 * ----------------------------------------
 */
public class CourseStyleAdapter extends BaseAdapter {

    private int mScreenWidth;
    private AddClickListener listener;
    private List<String> urls = new ArrayList<>();

    public CourseStyleAdapter(List<String> datas, int mScreenWidth, AddClickListener listener) {
        this.urls = datas;
        this.mScreenWidth = mScreenWidth;
        this.listener = listener;
    }

    public void setData(List<String> datas) {
        this.urls = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        convertView = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(UIUtils.getContext(), R.layout.item_member_detail, null);
            viewHolder.iv_item_member = (RoundImageView) convertView.findViewById(R.id.iv_item_member);

            ViewGroup.LayoutParams layoutParams = viewHolder.iv_item_member.getLayoutParams();
            layoutParams.width = (mScreenWidth - UIUtils.dip2px(44)) / 2;//一屏显示两列
            viewHolder.iv_item_member.setLayoutParams(layoutParams);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String url = urls.get(position);

        if (!"add".equals(url)) {
            Glide.with(UIUtils.getContext()).load(url).into(viewHolder.iv_item_member);
        } else {
            viewHolder.iv_item_member.setBackgroundResource(R.drawable.add_picture_btn);
        }
        viewHolder.iv_item_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("add".equals(url)) {
                    listener.addClick();
                } else {
                    listener.checkClick(position);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private RoundImageView iv_item_member;
    }

    public interface AddClickListener {
        void addClick();

        void checkClick(int pos);
    }


}
