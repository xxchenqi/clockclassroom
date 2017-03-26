package com.yiju.ClassClockRoom.control;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;

public class TroubleControl {
	private static TroubleControl sInstance;

	/**
	 * 实例化
	 * 
	 * @return sInstance
	 */
	public static TroubleControl getInstance() {
		if (sInstance == null) {
			sInstance = new TroubleControl();
		}
		return sInstance;
	}

	private String[] hour_lists;
	private String[] min_lists;
	private String[] continue_lists;

	private String local_trouble_btime;
	private String local_trouble_etime;

	private String currentHour;
	private String currentMin;
	private String currentContinue;

	// 初始化当前的时间
	public void initLocalTime() {
		if (hour_lists == null) {
			getHourLists();
		}
		if (min_lists == null) {
			getMinLists();
		}
		if (continue_lists == null) {
			getContinueLists();
		}

		local_trouble_btime = SharedPreferencesUtils.getString(
				BaseApplication.getmForegroundActivity(),
				BaseApplication.getmForegroundActivity().getResources()
						.getString(R.string.shared_trouble_btime),
				hour_lists[22] + min_lists[0]);
		if (local_trouble_btime.length() > 2) {
			currentHour = local_trouble_btime.substring(0,
					local_trouble_btime.length() - 2);
			currentMin = local_trouble_btime.substring(
					local_trouble_btime.length() - 2,
					local_trouble_btime.length());
		} else {
			currentHour = "0";
			currentMin = "0";
		}

		local_trouble_etime = SharedPreferencesUtils.getString(
				BaseApplication.getmForegroundActivity(),
				BaseApplication.getmForegroundActivity().getResources()
						.getString(R.string.shared_trouble_etime),
				getEndTime(currentHour, continue_lists[10]) + min_lists[0]);

		currentContinue = getInterval(currentHour, local_trouble_etime);
	}

	// 设置显示类容
	public String setContentVisible(String currentHour, String currentMin,
			String currentContinue) {
		if (StringUtils.isNullString(currentContinue)
				|| StringUtils.isNullString(currentHour)
				|| StringUtils.isNullString(currentMin)) {
			currentHour = this.currentHour;
			currentMin = this.currentMin;
			currentContinue = this.currentContinue;
		}
		switch (currentContinue) {
			case "0":
				return "全天";
			case "24":
				return "无免打扰时间";
			default:
				if (Integer.parseInt(currentHour)
						+ Integer.parseInt(currentContinue) >= 24) {

					return "每日" + currentHour + ":" + currentMin + "-次日"
							+ getEndTime(currentHour, currentContinue) + ":"
							+ currentMin;
				} else {
					return "每日" + currentHour + ":" + currentMin + "-"
							+ getEndTime(currentHour, currentContinue) + ":"
							+ currentMin;
				}
		}
	}

	// 获得小时数组
	public String[] getHourLists() {
		if (hour_lists == null) {
			hour_lists = new String[24];
			for (int h = 0; h <= 23; h++) {
				hour_lists[h] = numFormatTime(h);
			}
		}
		return hour_lists;
	}

	// 获得分钟数组
	public String[] getMinLists() {
		if (min_lists == null) {
			min_lists = new String[60];
			for (int m = 0; m <= 59; m++) {
				min_lists[m] = numFormatTime(m);
			}
		}
		return min_lists;
	}

	// 获得间距小时数组
	public String[] getContinueLists() {
		if (continue_lists == null) {
			continue_lists = new String[25];
			for (int i = 0; i <= 24; i++) {
				continue_lists[i] = i + "";
			}
		}
		return continue_lists;
	}

	public String getLocal_trouble_btime() {
		return StringUtils.isNullString(local_trouble_btime) ? "0"
				: local_trouble_btime;
	}

	public String getLocal_trouble_etime() {
		return StringUtils.isNullString(local_trouble_etime) ? "0"
				: local_trouble_etime;
	}

	public String getCurrentHour() {
		return StringUtils.isNullString(currentHour) ? "0" : currentHour;
	}

	public String getCurrentMin() {
		return StringUtils.isNullString(currentMin) ? "0" : currentMin;
	}

	public String getCurrentContinue() {
		return StringUtils.isNullString(currentContinue) ? "0"
				: currentContinue;
	}

	// 数字转时间
	private String numFormatTime(int time) {
		if (time < 10) {
			return "0" + time;
		}
		return time + "";
	}

	/**
	 * 计算间距后结束小时
	 * 
	 * @param start
	 *            起始小时
	 * @param interval
	 *            间隔时间
	 * @return 结束小时
	 */
	private String getEndTime(String start, String interval) {
		int startTime = Integer.parseInt(start);
		int intervalTime = Integer.parseInt(interval);
		if (startTime + intervalTime < 24) {
			return (startTime + intervalTime) + "";
		} else {
			return (startTime + intervalTime - 24) + "";
		}
	}

	/**
	 * 计算间距时间
	 * 
	 * @param startHour
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @return 间距时间
	 */
	private String getInterval(String startHour, String endTime) {
		int sHour = Integer.parseInt(startHour);
		int eHour;
		if (endTime.length() > 2) {
			eHour = Integer
					.parseInt(endTime.substring(0, endTime.length() - 2));
		} else {
			eHour = 0;
		}

		if (eHour > sHour) {
			return eHour - sHour + "";
		} else {
			return (24 - sHour) + eHour + "";
		}
	}
}
