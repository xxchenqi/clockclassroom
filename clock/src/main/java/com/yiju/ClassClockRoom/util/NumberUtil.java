package com.yiju.ClassClockRoom.util;

import java.math.BigDecimal;

public class NumberUtil {
    /**
     * 小数点四舍五入
     *
     * @param str   数据
     * @param scale 位数
     * @return s
     */
    public static String getDecimal(String str, int scale) {
        if (scale < 0) {
            return "";
        }
        BigDecimal b = new BigDecimal(str);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue()
                + "";
    }

    public static String doubleTrans(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }
}
