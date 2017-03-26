package com.yiju.ClassClockRoom.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yiju.ClassClockRoom.R;

/**
 * --------------------------------------
 * 
 * 注释:自定义toast样式
 * 
 * 
 * 
 * 作者: cq
 * 
 * 
 * 
 * 时间: 2015-12-22 下午5:43:19
 * 
 * --------------------------------------
 */
class ToastUtil extends Toast {
	private static Toast mToast;

	public ToastUtil(Context context) {
		super(context);
	}

	public static Toast makeText(Context context, CharSequence text,
			int duration) {
		Toast result = new Toast(context);
		// 获取LayoutInflater对象
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 由layout文件创建一个View对象
		View layout = inflater.inflate(R.layout.toast_ly, null);
		// 实例化ImageView和TextView对象
		TextView textView = (TextView) layout.findViewById(R.id.message);
		// 这里我为了给大家展示就使用这个方面既能显示无图也能显示带图的toast
		textView.setText(text);
		result.setView(layout);
		result.setDuration(duration);

		return result;
	}

	public static void showToast(Context context, String content) {
		mToast = ToastUtil.makeText(context, content, Toast.LENGTH_LONG);
		mToast.show();
	}

}