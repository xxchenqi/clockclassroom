package com.yiju.ClassClockRoom.control;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.yiju.ClassClockRoom.receiver.LockReceiver;

public class LockScreenControl {
	private static LockScreenControl sInstance;

	private LockReceiver lockReceiver;

	private LockScreenControl() {
		lockReceiver = new LockReceiver();
	}

	/**
	 * 实例化
	 *
	 * @return sInstance
	 */
	public static LockScreenControl getInstance() {
		if (sInstance == null) {
			sInstance = new LockScreenControl();
		}
		return sInstance;
	}

	public void registerLockReceiver(Context context) {
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		context.registerReceiver(lockReceiver, filter);
	}

	public void unRegisterLockReceiver(Context context) {
		context.unregisterReceiver(lockReceiver);
	}
}
