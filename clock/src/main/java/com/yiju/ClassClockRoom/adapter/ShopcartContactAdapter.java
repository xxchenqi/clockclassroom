package com.yiju.ClassClockRoom.adapter;

import java.util.List;

import android.content.Context;

import com.yiju.ClassClockRoom.bean.ContactBean.Data;
import com.yiju.ClassClockRoom.adapter.holder.BaseHolder;
import com.yiju.ClassClockRoom.adapter.holder.ShopcartContactHolder;

public class ShopcartContactAdapter extends MyBaseAdapter<Data> {

	public ShopcartContactAdapter(Context context, List<Data> mDatas) {
		super(context, mDatas);
	}

	@Override
	public BaseHolder<Data> getHolder() {
		return new ShopcartContactHolder(mContext);
	}

}
