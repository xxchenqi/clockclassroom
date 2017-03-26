package com.yiju.ClassClockRoom.adapter;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yiju.ClassClockRoom.adapter.holder.BaseHolder;

abstract class MyBaseAdapter<T> extends BaseAdapter {

	Context mContext;
	private List<T> mDatas;
	private BaseHolder<T> holder;

	MyBaseAdapter(Context context, List<T> mDatas) {
		mContext = context;
		setData(mDatas);
	}
	
	private void setData(List<T> mDatas) {
		this.mDatas = mDatas;
	}
	
	public List<T> getData() {
		return mDatas;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView != null) {
			holder = (BaseHolder) convertView.getTag();
		} else {
			holder = getHolder();
		}
		
		holder.setData(mDatas.get(position));
		
		return holder.getRootView();
	}

	protected abstract BaseHolder<T> getHolder();

}
