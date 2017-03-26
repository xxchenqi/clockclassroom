package com.yiju.ClassClockRoom.act.search;

import android.content.Context;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.CommonBaseAdapter;
import com.yiju.ClassClockRoom.bean.HotSearch;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.util.StringUtils;

import java.util.List;

/**
 * 作者： 葛立平
 * 2016/3/9 11:32
 */
public class SearchHotAdapter extends CommonBaseAdapter<HotSearch> {
    public SearchHotAdapter(Context context, List<HotSearch> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, HotSearch hotSearch) {
        holder.setText(R.id.tv_search_note, StringUtils.formatNullString(hotSearch.getWord()));
    }

}
