package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.SchoolInfo;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:筛选器适配器
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/6/16 16:49
 * ----------------------------------------
 */
public class FiltrateAdapter extends CommonBaseAdapter<SchoolInfo> {

    public FiltrateAdapter(Context context, List<SchoolInfo> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, SchoolInfo schoolInfo) {
        TextView tv_item_filtrate = holder.getView(R.id.tv_item_filtrate);
        holder.setText(R.id.tv_item_filtrate, schoolInfo.getName());
        if (schoolInfo.isFlag()) {
            tv_item_filtrate.setTextColor(UIUtils.getColor(R.color.color_green_1e));
            tv_item_filtrate.setBackgroundResource(R.drawable.background_green_1eb482_stroke_radius_6);
        } else {
            tv_item_filtrate.setTextColor(UIUtils.getColor(R.color.black66));
            tv_item_filtrate.setBackgroundResource(R.drawable.background_gray_stroke_radius_6);
        }
    }
}
