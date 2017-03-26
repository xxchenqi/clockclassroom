package com.yiju.ClassClockRoom.adapter;

import java.util.List;

import android.content.Context;

import com.yiju.ClassClockRoom.bean.ReservationThree.Desc;
import com.yiju.ClassClockRoom.adapter.holder.BaseHolder;
import com.yiju.ClassClockRoom.adapter.holder.RoomDeviceHolder;

public class RoomDeviceAdapter extends MyBaseAdapter<Desc> {

	private Context mContext;
	private List<Desc> mLists;
	private int roomCount;
	
	public RoomDeviceAdapter(Context context, List<Desc> mDatas, int roomCount) {
		super(context, mDatas);
		this.mContext = context;
		this.mLists = mDatas;
		this.roomCount = roomCount;
	}

	@Override
	public BaseHolder<Desc> getHolder() {
		return new RoomDeviceHolder(mContext, roomCount);
	}

}
