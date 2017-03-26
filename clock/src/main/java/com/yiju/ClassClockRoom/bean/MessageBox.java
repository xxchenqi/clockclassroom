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

    private List<MessageTypeData> data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<MessageTypeData> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<MessageTypeData> getData() {
        return data;
    }

    public static class MessageTypeData implements Serializable {
        private String name;
        private String noread_count;
        private String create_time;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        private String recent_message;
        private String big_type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNoread_count() {
            return noread_count;
        }

        public void setNoread_count(String noread_count) {
            this.noread_count = noread_count;
        }

        public String getRecent_message() {
            return recent_message;
        }

        public void setRecent_message(String recent_message) {
            this.recent_message = recent_message;
        }

        public String getBig_type() {
            return big_type;
        }

        public void setBig_type(String big_type) {
            this.big_type = big_type;
        }
    }
}
