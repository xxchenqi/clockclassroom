package com.yiju.ClassClockRoom.bean;
/**
 * 优惠券是否可用
 * @author len
 *
 */
public class CouponUse {

	private Integer code;
	private String msg;
	private Integer data;
	public Integer getData() {
		return data;
	}
	public void setData(Integer data) {
		this.data = data;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
