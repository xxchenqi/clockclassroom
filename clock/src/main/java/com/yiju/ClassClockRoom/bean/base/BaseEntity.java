package com.yiju.ClassClockRoom.bean.base;

/**
 * 作者： 葛立平
 * 2016/2/26 14:54
 */
public class BaseEntity {
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获得的int类型的Code
     */
    public int getIntCode(){
        int intCode = 0;
        try{
            intCode = Integer.parseInt(code);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  intCode;
    }

    public BaseEntity(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseEntity() {
    }

}
