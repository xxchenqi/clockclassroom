package com.yiju.ClassClockRoom.act.search;

import android.content.Context;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.CommonBaseAdapter;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.util.StringUtils;

import java.util.List;

/**
 * 作者： 葛立平
 * 2016/3/3 14:45
 */
public class SearchAdapter extends CommonBaseAdapter<String> {
    public SearchAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, String s) {
        holder.setText(R.id.tv_search_note, StringUtils.formatNullString(s));
    }
}
