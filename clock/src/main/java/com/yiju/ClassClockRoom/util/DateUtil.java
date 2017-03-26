package com.yiju.ClassClockRoom.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /*
     * 按照固定格式转换时间
     * @param date
     * @param oldPattern yyyy-MM-dd HH:mm:ss
     * @param newPattern yyyy年MM月dd HH:mm:ss
     * @return
     */
    public static String StringPattern(String date, String oldPattern,
                                       String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern, Locale.getDefault()); // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern, Locale.getDefault()); // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date); // 将给定的字符串中的日期提取出来
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
        }
        return sdf2.format(d);
    }

    /*
     * 得到当前时间格式自己填写
     *
     * @param format 格式
     * @return
     */
    public static String getCurrentDate(String format) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 比较日期大小
     *
     * @param s1 sys_time
     * @param s2 end_date
     * @return result 等于0：c1=c2  小于0：c1 < c2  大与0：c1 > c2
     */
    public static int compareDate(String s1, String s2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("---时间格式不正确");
        }
//        int result = c1.compareTo(c2);
//        if (result == 0)
//            System.out.println("---c1相等c2");
//        else if (result < 0)
//            System.out.println("---c1小于c2");
//        else
//            System.out.println("---c1大于c2");
        return c1.compareTo(c2);
    }

    public static String second2Date(String time) {
        Date today = new Date(Long.valueOf(time + "000"));
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return f.format(today);
    }
    /**
     * 判断两个时间段是否有重叠
     *
     * @param beginDate1
     *            时间段1 开始时间
     * @param endDate1
     *            时间段1 结束时间
     * @param beginDate2
     *            时间段2 开始时间
     * @param endDate2
     *            时间段2 结束时间
     * @param dateformat
     *            时间格式
     * @return true 存在重叠 false 不重叠
     */

    public static Boolean isOverLaps(String beginDate1, String endDate1,
                                     String beginDate2, String endDate2, String dateformat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1b = sdf.parse(beginDate1);
            Date date1e = sdf.parse(endDate1);
            Date date2b = sdf.parse(beginDate2);
            Date date2e = sdf.parse(endDate2);
            if (date2b.compareTo(date1e) * date2e.compareTo(date1b) > 0)
                return false;
            else {// 判断是否是时间交接点:08:00-12:00 12:00-15:00
                if (date2b.equals(date1e) || date2e.equals(date1b)) {
                    return false;
                } else
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
