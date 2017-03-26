package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.SchoolLeft;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:更多门店筛选适配器
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/9/5 15:12
 * ----------------------------------------
 */
public class MoreStoreFiltrateAdapter extends CommonBaseAdapter<SchoolLeft> {
    public MoreStoreFiltrateAdapter(Context context, List<SchoolLeft> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, SchoolLeft schoolLeft) {
        TextView tv_item_more_store_area = holder.getView(R.id.tv_item_more_store_area);
        holder.setText(R.id.tv_item_more_store_area, schoolLeft.getDist_name());
        if (schoolLeft.isFlag()) {
            tv_item_more_store_area.setTextColor(UIUtils.getColor(R.color.color_green_1e));
        } else {
            tv_item_more_store_area.setTextColor(UIUtils.getColor(R.color.black));
        }
    }
}
