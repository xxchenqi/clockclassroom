package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * --------------------------------------
 * 
 * 注释:联系人列表返回信息
 * 
 * 
 * 
 * 作者: cq
 * 
 * 
 * 
 * 时间: 2015-12-10 上午10:17:14
 * 
 * --------------------------------------
 */
public class ContactBean {
	private String code;
	private String msg;
	private ArrayList<Data> data;
	
	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public ArrayList<Data> getData() {
		return data;
	}

	public class Data implements Serializable{
		private String id;
		private String uid;
		private String name;
		private String mobile;
		private String create_time;
		private String isdefault;
		private boolean check;
		public boolean isCheck() {
			return check;
		}

		public void setCheck(boolean check) {
			this.check = check;
		}

		public String getId() {
			return id;
		}

		public String getUid() {
			return uid;
		}

		public String getName() {
			return name;
		}

		public String getMobile() {
			return mobile;
		}

		public String getCreate_time() {
			return create_time;
		}

		public String getIsdefault() {
			return isdefault;
		}

	}

}
