package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * 预订第三个页面 2015/12/2/0002.
 */
public class ReservationThree {


    /**
     * code : 1
     * msg : ok
     * data : [{"id":"8","name":"投影仪","fee":"5.00","unit":"小时","max":20},{"id":"6","name":"桌椅","fee":"0.00","unit":" ","max":"10"},{"id":"4","name":"50人家长休息室","fee":"0.00","unit":" ","max":null},{"id":"2","name":"收发文件","fee":"0.00","unit":" ","max":null},{"id":"12","name":"桌子","fee":"0.00","unit":" ","max":null},{"id":"9","name":"音箱","fee":"0.00","unit":"小时","max":null},{"id":"7","name":"打印/复印","fee":"0.00","unit":"张","max":null},{"id":"5","name":"白板（含白板笔）","fee":"0.00","unit":" ","max":"10"},{"id":"3","name":"在线视频","fee":"0.00","unit":" ","max":null},{"id":"1","name":"免费wifi","fee":"0.00","unit":" ","max":null},{"id":"13","name":"椅子","fee":"0.00","unit":" ","max":null},{"id":"10","name":"话筒","fee":"0.00","unit":"小时","max":null}]
     */

    private Integer code;
    private String msg;
    /**
     * id : 8
     * name : 投影仪
     * fee : 5.00
     * unit : 小时
     * max : 20
     */

    private List<Desc> data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<Desc> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<Desc> getData() {
        return data;
    }

    public static class Desc {
        private String id;
        private String name;
        private String fee;
        private String unit;
        private int max;
        private int recommend;
        private int currentCount = 1;
        
        
        public int getCurrentCount() {
			return currentCount;
		}

		public void setCurrentCount(int currentCount) {
			this.currentCount = currentCount;
		}

		public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getFee() {
            return fee;
        }

        public String getUnit() {
            return unit;
        }

        public int getMax() {
            return max;
        }

		public int getRecommend() {
			return recommend;
		}

		public void setRecommend(int recommend) {
			this.recommend = recommend;
		}
        
    }
}
