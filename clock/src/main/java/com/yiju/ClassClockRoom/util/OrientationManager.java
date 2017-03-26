package com.yiju.ClassClockRoom.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.OrientationEventListener;

import com.yiju.ClassClockRoom.common.callback.OnClickOrientationListener;

/**
 * 横竖屏管理
 */
public class OrientationManager {
	private Activity activity;

	public OrientationManager(Activity activity) {
		this.activity = activity;
	}

	private OrientationEventListener mOrientationListener; // 屏幕方向改变监听器
	private boolean mIsLand = false; // 是否是横屏
	private boolean mClick = false; // 是否点击
	private boolean mClickLand = true; // 点击进入横屏
	private boolean mClickPort = true; // 点击进入竖屏
//	private boolean isPlaying = false;// 是否正在播放
	private boolean isAutoInduction = true; //是否自动感应横竖屏
	
	private OnClickOrientationListener onClickOrientationListener;

	/**
	 * 开启监听器
	 */
	private void startListener() {
		mOrientationListener = new OrientationEventListener(activity) {
			@Override
			public void onOrientationChanged(int rotation) {
				if(!isAutoInduction){
					return;
				}
//				if (!isPlaying) {
//					return;
//				}
				// 设置竖屏
				if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
					if (mClick) {
						if (mIsLand && !mClickLand) {
							return;
						} else {
							mClickPort = true;
							mClick = false;
							mIsLand = false;
						}
					} else {
						if (mIsLand) {
							activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
							if (onClickOrientationListener != null) {
								onClickOrientationListener.portrait();
							}
							mIsLand = false;
							mClick = false;
						}
					}
				}
				// 设置横屏
				else if (((rotation >= 230) && (rotation <= 310))) {
					if (mClick) {
						if (!mIsLand && !mClickPort) {
							return;
						} else {
							mClickLand = true;
							mClick = false;
							mIsLand = true;
						}
					} else {
						if (!mIsLand) {
							activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
							if (onClickOrientationListener != null) {
								onClickOrientationListener.landscape();
							}
							mIsLand = true;
							mClick = false;
						}
					}
				}
			}
		};
		mOrientationListener.enable();
	}

	public void setOnClickOrientationListener(
			OnClickOrientationListener onClickOrientationListener) {
		startListener();
		this.onClickOrientationListener = onClickOrientationListener;
	}

	/**
	 * 切换
	 */
	public void clickChange() {
		if (!mIsLand) {
			setLand();
		} else {
			setPortrait();
		}
	}

//	public void setIsPlaying(boolean isPlaying) {
//		this.isPlaying = isPlaying;
//	}
	
	/**
	 * 设置是否自动切换横竖屏
	 * @param isAutoInduction b
	 */
	public void setIsAutoInduction(boolean isAutoInduction) {
		this.isAutoInduction = isAutoInduction;
	}

	public boolean ismIsLand() {
		return mIsLand;
	}

	private void setLand() {
		mClick = true;
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		if (onClickOrientationListener != null) {
			onClickOrientationListener.landscape();
		}
		mIsLand = true;
		mClickLand = false;
	}

	private void setPortrait() {
		mClick = true;
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (onClickOrientationListener != null) {
			onClickOrientationListener.portrait();
		}
		mIsLand = false;
		mClickPort = false;
	}
}
