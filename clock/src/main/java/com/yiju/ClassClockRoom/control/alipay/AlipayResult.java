package com.yiju.ClassClockRoom.control.alipay;

import java.util.HashMap;
import java.util.Map;

public class AlipayResult {
	
	public static final String AliPay_PayOK = "9000";
	public static final String AliPay_PayFail = "4006";
	public static final String AliPay_PayCancel = "6001";
	public static final String AliPay_Error = "6002";
	
	private static final Map<String, String> sResultStatus ;

	private String mResult;
	
	private static String srcTemp = null;
		

	/**
	 * @param result 返回结果String
	 */
	public AlipayResult(String result) {
		this.mResult = result;
		srcTemp = mResult.replace("{", "").replace("}", "");
	}

	static {
		sResultStatus = new HashMap<>();
		sResultStatus.put(AliPay_PayOK, "订单支付成功");
		sResultStatus.put("4000", "订单支付失败");
		sResultStatus.put("4001", "订单参数错误");
		sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
		sResultStatus.put("4004", "该用户已解除绑定");
		sResultStatus.put("4005", "绑定失败或没有绑定");
		sResultStatus.put(AliPay_PayFail, "订单充值失败");
		sResultStatus.put("4010", "重新绑定账户");
		sResultStatus.put("6000", "支付服务正在进行升级操作");
		sResultStatus.put(AliPay_PayCancel, "用户中途取消");
		sResultStatus.put("6002", "网络连接出错");
		sResultStatus.put("7001", "网页充值失败");
		sResultStatus.put("8000", "正在处理中");
	}

	/**
	 * 返回状态类容
	 * @return ""
	 */
	public  String getMemo() {
		return getContent(srcTemp, "memo=", ";result");
	}
	
	/**
	 * 返回状态编号
	 * @return ""
	 */
	public String getResultStatus(){
		return getContent(srcTemp, "resultStatus=", ";memo");
	}

	
	/**
	 * 返回HashMap 内容 状态
	 * @return ""
	 */
	public String getHashMapV(){
		String hashMapView;
		String rs = getResultStatus();
		if (sResultStatus.containsKey(rs)) {
			hashMapView = sResultStatus.get(rs);
		} else {
			hashMapView = "其他错误";
		}
		return hashMapView;
	}
	
	private  String getContent(String src, String startTag, String endTag) {
		String content = src;
		int start = src.indexOf(startTag);
		start += startTag.length();

		try {
			if (endTag != null) {
				int end = src.indexOf(endTag);
				content = src.substring(start, end);
			} else {
				content = src.substring(start);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}	
}
