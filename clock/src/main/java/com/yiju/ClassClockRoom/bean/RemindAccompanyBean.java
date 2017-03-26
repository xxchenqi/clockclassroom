package com.yiju.ClassClockRoom.bean;
/**
 * 陪读提醒列表相关
 * @author geliping
 *
 */
public class RemindAccompanyBean {
	private String time;
	private String name;
	private boolean ischeck;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIscheck() {
		return ischeck;
	}
	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
}
