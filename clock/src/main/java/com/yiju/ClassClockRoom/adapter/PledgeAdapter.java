package com.yiju.ClassClockRoom.adapter;

import android.content.Context;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.PledgeBean;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/1/25 16:08
 * ----------------------------------------
 */
public class PledgeAdapter extends CommonBaseAdapter<PledgeBean.Data> {

    public PledgeAdapter(Context context, List<PledgeBean.Data> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, PledgeBean.Data data) {
        holder.setText(R.id.tv_item_pledge_title, data.getName());

        ListViewForScrollView lv = holder.getView(R.id.lv_item_pledge_title);
        ArrayList<PledgeBean.Data.Data2> data2 = data.getData();

        lv.setAdapter(new Pledge_Inner_Adapter(mContext, data2, R.layout.item_pledge));


    }
}
