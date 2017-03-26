package com.yiju.ClassClockRoom.util;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;

public class ProgressUtil {

	/**
	 * 开关进度条
	 * 
	 * @param isOpen
	 *            true开，false 关
	 */
	public static void progressMethod(View currentProgress, boolean isOpen) {
		if (currentProgress != null) {
			// if (isOpen) {
			// currentProgress.clearAnimation();
			// currentProgress.startAnimation(AnimationUtils.loadAnimation(
			// BaseApplication.mContext, R.anim.progress_round));
			// currentProgress.setVisibility(View.VISIBLE);
			// } else {
			// currentProgress.clearAnimation();
			// currentProgress.setVisibility(View.GONE);
			// }

			AnimationDrawable animationDrawable = (AnimationDrawable) currentProgress
					.getBackground();
			
			if (isOpen) {
				currentProgress.setVisibility(View.VISIBLE);
				animationDrawable.start();
			} else {
				animationDrawable.stop();
				currentProgress.setVisibility(View.GONE);

			}

		}
	}
}
