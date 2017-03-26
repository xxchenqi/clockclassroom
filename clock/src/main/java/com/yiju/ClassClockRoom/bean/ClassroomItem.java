package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * --------------------------------------
 * 
 * 注释:教室详情ITEM对象
 * 
 * 
 * 
 * 作者: cq
 * 
 * 
 * 
 * 时间: 2015-12-17 下午2:01:02
 * 
 * --------------------------------------
 */
public class ClassroomItem implements Serializable {
	private String date;
	private List<Order3> list;
	private String status;
	private boolean cb_status = true;

	public boolean isCb_status() {
		return cb_status;
	}

	public void setCb_status(boolean cb_status) {
		this.cb_status = cb_status;
	}

	public String getDate() {
		return date;
	}

	public List<Order3> getList() {
		return list;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setList(List<Order3> list) {
		this.list = list;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ClassroomItem(String date, List<Order3> list, String status) {
		super();
		this.date = date;
		this.list = list;
		this.status = status;
	}

}
