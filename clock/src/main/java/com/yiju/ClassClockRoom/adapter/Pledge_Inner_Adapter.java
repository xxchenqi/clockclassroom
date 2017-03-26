package com.yiju.ClassClockRoom.adapter;

import android.content.Context;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.PledgeBean;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/1/25 16:10
 * ----------------------------------------
*/
public class Pledge_Inner_Adapter extends CommonBaseAdapter<PledgeBean.Data.Data2> {

    public Pledge_Inner_Adapter(Context context, List<PledgeBean.Data.Data2> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, PledgeBean.Data.Data2 data2) {
        holder.setText(R.id.tv_item_pledge_room, data2.getDesc()).setText(R.id.tv_item_pledge_price, "¥" + data2.getDeposit());
    }
}
