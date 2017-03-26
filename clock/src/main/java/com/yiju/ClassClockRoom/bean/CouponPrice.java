package com.yiju.ClassClockRoom.bean;
/**
 * 优惠券优惠金额
 * @author len
 *
 */
public class CouponPrice {

	private Integer code;
	private String msg;
	private String data;
	public String getData() {
		return data;
	}
	public void setData(String data) {
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
