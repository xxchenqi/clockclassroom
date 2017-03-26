package com.yiju.ClassClockRoom.bean;

/**
 * --------------------------------------
 * 
 * 注释:版本信息
 * 
 * 
 * 
 * 作者: cq
 * 
 * 
 * 
 * 时间: 2015-12-24 下午4:46:34
 * 
 * --------------------------------------
 */
public class VersionBean {
	private String code;
	private String msg;
	private Data data;
	private int update;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Data getData() {
		return data;
	}

	public int getUpdate() {
		return update;
	}

	public class Data {
		private String id;
		private String system_id;
		private String version;
		private String create_time;
		private String must_update;
		private String desc;
		private String url;

		public String getId() {
			return id;
		}

		public String getSystem_id() {
			return system_id;
		}

		public String getVersion() {
			return version;
		}

		public String getCreate_time() {
			return create_time;
		}

		public String getMust_update() {
			return must_update;
		}

		public String getDesc() {
			return desc;
		}

		public String getUrl() {
			return url;
		}

	}

}
