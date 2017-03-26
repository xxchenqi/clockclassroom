package com.yiju.ClassClockRoom.adapter.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * --------------------------------------
 *
 * 注释:万能holder
 *
 *
 *
 * 作者: cq
 *
 *
 *
 * 时间: 2015-12-9 上午9:30:46
 *
 * --------------------------------------
 */
public class ViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private static boolean flag = false;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
					   int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (!flag) {
			if (convertView == null) {
				return new ViewHolder(context, parent, layoutId, position);
			} else {
				ViewHolder holder = (ViewHolder) convertView.getTag();
				holder.mPosition = position;
				return holder;
			}
		} else {
			return new ViewHolder(context, parent, layoutId, position);
		}

	}

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		ViewHolder.flag = flag;
	}

	public int getPosition() {
		return mPosition;
	}

	/**
	 * 通过viewId获取控件
	 *
	 * @param viewId i
	 * @param <T> t
	 * @return t
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {

		return mConvertView;
	}

	/**
	 * 设置TextView的值
	 *
	 * @param viewId i
	 * @param text s
	 * @return holder
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	public ViewHolder setTextAndColor(int viewId, String text, int color) {
		TextView tv = getView(viewId);
		tv.setText(text);
		tv.setTextColor(color);
		return this;
	}

	public ViewHolder setImageResource(int viewId, int resId) {
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}

	public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	// public ViewHolder setImageURI(int viewId,String uri ) {
	// ImageView view = getView(viewId);
	// view.setImageURI();
	// return this;
	// }

}
