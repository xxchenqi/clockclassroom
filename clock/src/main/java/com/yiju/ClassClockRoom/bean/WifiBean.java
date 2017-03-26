package com.yiju.ClassClockRoom.bean;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/1/28 15:08
 * ----------------------------------------
 */
public class WifiBean {

    /**
     * code : 1
     * msg : ok
     * data : {"id":"2","url":"http://check.51shizhong.com/wx_check.html"}
     */

    private String code;
    private String msg;
    /**
     * id : 2
     * url : http://check.51shizhong.com/wx_check.html
     */

    private DataEntity data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String url;

        public void setId(String id) {
            this.id = id;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }
    }
}
