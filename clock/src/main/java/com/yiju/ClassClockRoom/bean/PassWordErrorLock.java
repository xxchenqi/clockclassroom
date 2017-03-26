package com.yiju.ClassClockRoom.bean;

import java.util.Date;

/**
 * 作者： 葛立平
 * 2016/1/25 11:29
 */
public class PassWordErrorLock {
    //累计错误次数锁定
    private final static int Param_Lock_Num = 5;
    //锁定持续时间
    private final static long Param_Lock_Time = 1000 * 60 * 60 * 24;
    //记录错误时间
    private long datetime;
    //记录错误次数
    private int errorCount;

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int count) {
        this.errorCount = count;
    }

    //添加错误次数
    public void addErrorCount(int num) {
        this.errorCount = errorCount + num;
    }

    //获得剩余次数
    public int getSurplusNum(){
        return  Param_Lock_Num - errorCount;
    }

    //获得剩余小时
    public int getSurplusTime(){
        long currentTime = new Date().getTime();
        long surplus = Param_Lock_Time - (currentTime - datetime);
        if (surplus > 0){
            float num = (float)surplus/(1000 * 60 * 60);
            return (int)Math.ceil(num);
        }else{
            return 0;
        }
    }

    public boolean isLock() {
        long currentTime = new Date().getTime();
        return errorCount >= Param_Lock_Num && currentTime - datetime < Param_Lock_Time;
    }
}
