package com.yiju.ClassClockRoom.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘管理
 * 
 * @author geliping
 * 
 */
public class KeyBoardManager {
	/**
	 * 关闭键盘
	 */
	public static void closeKeyBoard(Activity activity) {
		if (activity.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				View view = activity.getWindow().peekDecorView();
				if (view != null) {
					InputMethodManager inputManger = (InputMethodManager) activity
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManger.hideSoftInputFromWindow(view.getWindowToken(),
							0);
				}
			}
		}
	}

	//打开软键盘
	public static void openKeyBoard(Activity activity){
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 打开输入法
	 */
	public static void openKeyBoard(Activity activity, View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 接受软键盘输入的编辑文本或其它视图
		inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 关闭输入法
	 */
	public static void closeInput(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	/**
	 * 关闭输入法
	 */
	public static void closeInput(Activity activity, ViewGroup view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 若返回true，则表示输入法打开
	 * 
	 * @return b
	 */
	public static boolean isInputType(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}

}
