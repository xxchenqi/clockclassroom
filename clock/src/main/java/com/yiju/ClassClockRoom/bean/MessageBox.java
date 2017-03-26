package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

public class MessageBox implements Serializable {

    /**
     * code : 1
     * data : [{"content":"n1已把你添加为成员","create_time":"1457945589","id":"1","is_read":"0","is_send":"1","type":"3","uid":"24"},{"content":"n1已把你添加为成员","create_time":"1457946294","id":"2","is_read":"0","is_send":"1","type":"3","uid":"24"},{"content":"您购物车里的课室将为您再保留5分钟，请您尽快提交订单并完成支付","create_time":"1457947261","id":"3","is_read":"0","is_send":"1","type":"1","uid":"24"}]
     * msg : ok
     */

    private Integer code;
    private String msg;
    /**
     * content : n1已把你添加为成员
     * create_time : 1457945589
     * id : 1
     * is_read : 0
     * is_send : 1
     * type : 3
     * uid : 24
     */

    private List<MessageData> data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<MessageData> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<MessageData> getData() {
        return data;
    }

    public static class MessageData implements Serializable {
        private String content;
        private String create_time;
        private String id;
        private String detail_id;
        private String is_read;
        private String is_send;
        private String big_type;
        private String type;

        public String getBig_type() {
            return big_type;
        }

        public void setBig_type(String big_type) {
            this.big_type = big_type;
        }

        private String uid;

        public String getDetail_id() {
            return detail_id;
        }

        public void setDetail_id(String detail_id) {
            this.detail_id = detail_id;
        }


        public void setContent(String content) {
            this.content = content;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public void setIs_send(String is_send) {
            this.is_send = is_send;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getContent() {
            return content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getId() {
            return id;
        }

        public String getIs_read() {
            return is_read;
        }

        public String getIs_send() {
            return is_send;
        }

        public String getType() {
            return type;
        }

        public String getUid() {
            return uid;
        }
    }
}
