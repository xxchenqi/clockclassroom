package com.yiju.ClassClockRoom.adapter;

import java.util.List;

import android.content.Context;

import com.yiju.ClassClockRoom.bean.Coupon.CouponDataEntity;
import com.yiju.ClassClockRoom.adapter.holder.BaseHolder;
import com.yiju.ClassClockRoom.adapter.holder.CouponHolder;

public class CouponAdapter extends MyBaseAdapter<CouponDataEntity> {

	public CouponAdapter(Context context, List<CouponDataEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	public BaseHolder<CouponDataEntity> getHolder() {
		return new CouponHolder(mContext);
	}

}
