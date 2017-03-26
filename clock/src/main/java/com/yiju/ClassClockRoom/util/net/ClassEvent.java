package com.yiju.ClassClockRoom.util.net;

/**
 * 作者： 葛立平
 * 2016/2/29 18:20
 * 数据分发实体
 */
public class ClassEvent<T> {
    //分发消息类型
    public int type;
    //返回码
    public int code;
    //数据
    public T data;
    //是否只发送给当前页面
    public boolean pointCurrent;

    public ClassEvent(int type, T data) {
        this.type = type;
        this.data = data;
        this.pointCurrent = false;
    }

    public ClassEvent(int type, int code, T data) {
        this.type = type;
        this.code = code;
        this.data = data;
        this.pointCurrent = false;
    }

    public ClassEvent(int type, int code, T data,boolean isPointCurrent) {
        this.type = type;
        this.code = code;
        this.data = data;
        this.pointCurrent = isPointCurrent;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isPointCurrent() {
        return pointCurrent;
    }

    public void setPointCurrent(boolean pointCurrent) {
        this.pointCurrent = pointCurrent;
    }
}
