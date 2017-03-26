package com.yiju.ClassClockRoom.bean;

public class UserVerifyInfo {
	private String code;
	private String msg;
	private int verify_code;
	private int uid;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(int verify_code) {
		this.verify_code = verify_code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
