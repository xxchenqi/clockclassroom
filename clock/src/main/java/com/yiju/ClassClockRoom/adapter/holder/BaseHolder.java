package com.yiju.ClassClockRoom.adapter.holder;


import android.content.Context;
import android.view.View;

public abstract class BaseHolder<T> {

	private View mView;
	private T mData;
	BaseHolder(Context context) {
		mView = initView(context);
		mView.setTag(this);
	}

	public View getRootView() {
		return mView;
	}
	
	/**
	 * 设置数据
	 * 
	 * @param data t
	 */
	public void setData(T data) {
		this.mData = data;
		refreshView();
	}
	/**
	 * 返回数据
	 * 
	 * @return mData
	 */
	T getData() {
		return mData;
	}

	
	/**
	 * 初始化ViewHolder
	 * @return v
	 */
	protected abstract View initView(Context context);
	
	
	protected abstract void refreshView();
}
