package com.yiju.ClassClockRoom.bean;


import java.util.List;

/**
 * 购物车提交失败50025 2015/12/11/0011.
 */
public class CartFive {

    /**
     * code : 50025
     * miss_device : [""]
     * miss_room : []
     * msg : 过期订单房间不足或投影仪不足
     */

    private Integer code;
    private String msg;
    private List<String> miss;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

	public List<String> getMiss() {
		return miss;
	}

	public void setMiss(List<String> miss) {
		this.miss = miss;
	}
}
