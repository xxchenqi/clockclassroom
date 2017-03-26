package com.yiju.ClassClockRoom.adapter;

import android.content.Context;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.WifiStoreResult;

import java.util.List;

/**
 * 可用wifi门店列表适配器
 * Created by wh on 2016/11/9.
 */
public class AvailableWifiStoreAdapter extends CommonBaseAdapter<WifiStoreResult.DataEntity> {

    public AvailableWifiStoreAdapter(Context context, List<WifiStoreResult.DataEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, WifiStoreResult.DataEntity dataEntity) {
        holder.setText(R.id.tv_wifi_store_name, dataEntity.getName());
    }
}
