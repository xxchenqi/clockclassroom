package com.yiju.ClassClockRoom.adapter;

import android.content.Context;

import com.yiju.ClassClockRoom.bean.ReservationBean.ReservationDevice;
import com.yiju.ClassClockRoom.adapter.holder.BaseHolder;
import com.yiju.ClassClockRoom.adapter.holder.DeviceTypeHolder;

import java.util.List;

public class DeviceTypeAdapter extends MyBaseAdapter<ReservationDevice>{
    public DeviceTypeAdapter(Context context, List<ReservationDevice> mDatas) {
        super(context, mDatas);
    }

    @Override
    public BaseHolder<ReservationDevice> getHolder() {
        return new DeviceTypeHolder(mContext);
    }

}
