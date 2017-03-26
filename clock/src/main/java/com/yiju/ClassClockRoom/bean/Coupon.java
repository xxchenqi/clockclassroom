package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * 购物车优惠券 on 2015/12/10/0010.
 */
public class Coupon {

    /**
     * code : 1
     * msg : ok
     * data : [{"id":"34","batch_name":"mh大套间满送类无窗","type":"2","sid":"10","is_batch":"1","code":null,"num":"3","get_way":null,"ctype":null,"duration":"1","start_date":null,"end_date":null,"full":null,"discount":null,"room_type":"2","desc":"","create_time":"1449571191","sname":"闵行中心店"},{"id":"35","batch_name":"mh大套间上午","type":"2","sid":"10","is_batch":"1","code":null,"num":"3","get_way":null,"ctype":null,"duration":"1","start_date":null,"end_date":null,"full":null,"discount":null,"room_type":"1","desc":"","create_time":"1449571322","sname":"闵行中心店"},{"id":"29","batch_name":"zb满送类","type":"2","sid":"0","is_batch":"1","code":null,"num":"10","get_way":null,"ctype":null,"duration":"1","start_date":null,"end_date":null,"full":null,"discount":null,"room_type":"1","desc":"","create_time":"1449567996","sname":null},{"id":"36","batch_name":"mh中间满送类","type":"2","sid":"0","is_batch":"1","code":null,"num":"10","get_way":null,"ctype":null,"duration":"1","start_date":null,"end_date":null,"full":null,"discount":null,"room_type":"2","desc":"","create_time":"1449571677","sname":null}]
     */

    private Integer code;
    private String msg;
    /**
     * id : 34
     * batch_name : mh大套间满送类无窗
     * type : 2
     * sid : 10
     * is_batch : 1
     * code : null
     * num : 3
     * get_way : null
     * ctype : null
     * duration : 1
     * start_date : null
     * end_date : null
     * full : null
     * discount : null
     * room_type : 2
     * desc :
     * create_time : 1449571191
     * sname : 闵行中心店
     */

    private List<CouponDataEntity> data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<CouponDataEntity> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<CouponDataEntity> getData() {
        return data;
    }

    public static class CouponDataEntity {
        private String id;
        private String batch_name;
        private String type;
        private String sid;
        private String is_batch;
        private String code;
        private String num;
        private String get_way;
        private String ctype;
        private String duration;
        private String start_date;
        private String end_date;
        private String full;
        private String discount;
        private String room_type;
        private String desc;
        private String create_time;
        private String sname;
        private String abs_date;
        private boolean check;

        public boolean isCheck() {
			return check;
		}

		public void setCheck(boolean check) {
			this.check = check;
		}

		public String getAbs_date() {
			return abs_date;
		}

		public void setAbs_date(String abs_date) {
			this.abs_date = abs_date;
		}

		public void setId(String id) {
            this.id = id;
        }

        public void setBatch_name(String batch_name) {
            this.batch_name = batch_name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setIs_batch(String is_batch) {
            this.is_batch = is_batch;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public void setGet_way(String get_way) {
            this.get_way = get_way;
        }

        public void setCtype(String ctype) {
            this.ctype = ctype;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public void setFull(String full) {
            this.full = full;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getId() {
            return id;
        }

        public String getBatch_name() {
            return batch_name;
        }

        public String getType() {
            return type;
        }

        public String getSid() {
            return sid;
        }

        public String getIs_batch() {
            return is_batch;
        }

        public String getCode() {
            return code;
        }

        public String getNum() {
            return num;
        }

        public String getGet_way() {
            return get_way;
        }

        public String getCtype() {
            return ctype;
        }

        public String getDuration() {
            return duration;
        }

        public String getStart_date() {
            return start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public String getFull() {
            return full;
        }

        public String getDiscount() {
            return discount;
        }

        public String getRoom_type() {
            return room_type;
        }

        public String getDesc() {
            return desc;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getSname() {
            return sname;
        }
    }
}
