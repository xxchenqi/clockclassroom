package com.yiju.ClassClockRoom.control;

import java.util.Arrays;
import java.util.List;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.util.UIUtils;

public class AccompanyRemindControl {
	public static final List<String> lists = Arrays.asList(UIUtils
			.getStringArray(R.array.remind_accompany_array_num));

	public static String NumFormatStr(int time) {
		if (time == Integer.parseInt(lists.get(0))) {
			return "视频开始时提醒";
		} else if (time > Integer.parseInt(lists.get(0))
				&& time <= Integer.parseInt(lists.get(1))) {
			return "提前5分钟";
		} else if (time > Integer.parseInt(lists.get(1))
				&& time <= Integer.parseInt(lists.get(2))) {
			return "提前10分钟";
		} else if (time > Integer.parseInt(lists.get(2))
				&& time <= Integer.parseInt(lists.get(3))) {
			return "提前15分钟";
		} else if (time > Integer.parseInt(lists.get(3))
				&& time <= Integer.parseInt(lists.get(4))) {
			return "提前1小时";
		} else if (time > Integer.parseInt(lists.get(4))
				&& time <= Integer.parseInt(lists.get(5))) {
			return "提前2小时";
		} else if (time > Integer.parseInt(lists.get(5))
				&& time <= Integer.parseInt(lists.get(6))) {
			return "提前1天";
		} else if (time > Integer.parseInt(lists.get(6))
				&& time <= Integer.parseInt(lists.get(7))) {
			return "提前2天";
		} else if (time > Integer.parseInt(lists.get(7))
				&& time <= Integer.parseInt(lists.get(8))) {
			return "提前1周";
		} else {
			return "提前1周以上";
		}
	}
}
