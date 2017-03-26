package com.yiju.ClassClockRoom.util.sdcard;


import com.yiju.ClassClockRoom.util.CommonUtil;

class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "clock/files/";
		} else {
			return CommonUtil.getRootFilePath() + "clock/files";
		}
	}
}
