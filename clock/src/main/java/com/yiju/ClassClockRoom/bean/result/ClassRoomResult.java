package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.ClassRoomData;

public class ClassRoomResult {
    private Integer code;
    private String msg;
    private ClassRoomData data;

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

    public ClassRoomData getData() {
        return data;
    }

    public void setData(ClassRoomData data) {
        this.data = data;
    }
}
