package com.yiju.ClassClockRoom.bean;

import com.yiju.ClassClockRoom.util.StringUtils;

/** 未读消息bean
 * Created by admin on 2016/3/28.
 */
public class MessageBoxNoRead {
    private String code;
    private String noread_count;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNoread_count() {
        return StringUtils.isNotNullString(noread_count)? noread_count : "0";
    }

    public void setNoread_count(String noread_count) {
        this.noread_count = noread_count;
    }
}
