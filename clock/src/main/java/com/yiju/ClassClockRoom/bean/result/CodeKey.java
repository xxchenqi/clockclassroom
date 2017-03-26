package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;

/**
 * 图片服务信息类
 */
public class CodeKey extends BaseEntity{
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data {
		private String file_category;
		private String permit_code;
		private String key;

		public String getFile_category() {
			return file_category;
		}

		public String getPermit_code() {
			return permit_code;
		}

		public String getKey() {
			return key;
		}

		public void setFile_category(String file_category) {
			this.file_category = file_category;
		}

		public void setPermit_code(String permit_code) {
			this.permit_code = permit_code;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}
}
