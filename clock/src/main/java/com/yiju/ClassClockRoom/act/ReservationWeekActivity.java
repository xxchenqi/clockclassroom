package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ReservationWeekActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    // "平时 4 天，周末 0 天"
    @ViewInject(R.id.tv_all_count_day)
    private TextView tv_all_count_day;
    @ViewInject(R.id.tv_all_count_week)
    private TextView tv_all_count_week;

    @ViewInject(R.id.rl_reservation_every)
    private RelativeLayout rl_reservation_every;
    @ViewInject(R.id.rl_reservation_mon)
    private RelativeLayout rl_reservation_mon;
    @ViewInject(R.id.rl_reservation_tue)
    private RelativeLayout rl_reservation_tue;
    @ViewInject(R.id.rl_reservation_wed)
    private RelativeLayout rl_reservation_wed;
    @ViewInject(R.id.rl_reservation_thu)
    private RelativeLayout rl_reservation_thu;
    @ViewInject(R.id.rl_reservation_fri)
    private RelativeLayout rl_reservation_fri;
    @ViewInject(R.id.rl_reservation_sat)
    private RelativeLayout rl_reservation_sat;
    @ViewInject(R.id.rl_reservation_sun)
    private RelativeLayout rl_reservation_sun;

    @ViewInject(R.id.iv_reservation_every)
    private ImageView iv_reservation_every;
    @ViewInject(R.id.iv_reservation_mon)
    private ImageView iv_reservation_mon;
    @ViewInject(R.id.iv_reservation_tue)
    private ImageView iv_reservation_tue;
    @ViewInject(R.id.iv_reservation_wed)
    private ImageView iv_reservation_wed;
    @ViewInject(R.id.iv_reservation_thu)
    private ImageView iv_reservation_thu;
    @ViewInject(R.id.iv_reservation_fri)
    private ImageView iv_reservation_fri;
    @ViewInject(R.id.iv_reservation_sat)
    private ImageView iv_reservation_sat;
    @ViewInject(R.id.iv_reservation_sun)
    private ImageView iv_reservation_sun;

    @ViewInject(R.id.tv_reservation_every)
    private TextView tv_reservation_every;
    @ViewInject(R.id.tv_reservation_mon)
    private TextView tv_reservation_mon;
    @ViewInject(R.id.tv_reservation_tue)
    private TextView tv_reservation_tue;
    @ViewInject(R.id.tv_reservation_wed)
    private TextView tv_reservation_wed;
    @ViewInject(R.id.tv_reservation_thu)
    private TextView tv_reservation_thu;
    @ViewInject(R.id.tv_reservation_fri)
    private TextView tv_reservation_fri;
    @ViewInject(R.id.tv_reservation_sat)
    private TextView tv_reservation_sat;
    @ViewInject(R.id.tv_reservation_sun)
    private TextView tv_reservation_sun;

    private int numCount = 0;// 定义一个变量，来记录总的选中天数
    private int numInfoCount = 0;// 定义一个变量，来记录总的选中天数
    private List<Date> mDates = new ArrayList<>();
    private Set<Integer> mWeeks = new HashSet<>();
    private List<TextView> mTextViews = new ArrayList<>();
    private List<ImageView> mImageViews = new ArrayList<>();
    private Calendar calendar;
    private int allCount;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        rl_reservation_every.setOnClickListener(this);
        rl_reservation_mon.setOnClickListener(this);
        rl_reservation_tue.setOnClickListener(this);
        rl_reservation_wed.setOnClickListener(this);
        rl_reservation_thu.setOnClickListener(this);
        rl_reservation_fri.setOnClickListener(this);
        rl_reservation_sat.setOnClickListener(this);
        rl_reservation_sun.setOnClickListener(this);
        rl_reservation_every.setEnabled(false);
        rl_reservation_mon.setEnabled(false);
        rl_reservation_tue.setEnabled(false);
        rl_reservation_wed.setEnabled(false);
        rl_reservation_thu.setEnabled(false);
        rl_reservation_fri.setEnabled(false);
        rl_reservation_sat.setEnabled(false);
        rl_reservation_sun.setEnabled(false);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        head_title.setText(getString(R.string.reservation_week));
        calendar = Calendar.getInstance();
        Intent mReservationIntent = getIntent();
        if (null == mReservationIntent) {
            return;
        }
        mDates.clear();
        ArrayList<Integer> reservationHaveWeekList = mReservationIntent.getIntegerArrayListExtra("reservationHaveWeek");
        List<Date> reservationDates = (ArrayList<Date>) mReservationIntent.
                getSerializableExtra("reservationHaveDate");
        mTextViews.add(tv_reservation_sun);
        mTextViews.add(tv_reservation_mon);
        mTextViews.add(tv_reservation_tue);
        mTextViews.add(tv_reservation_wed);
        mTextViews.add(tv_reservation_thu);
        mTextViews.add(tv_reservation_fri);
        mTextViews.add(tv_reservation_sat);
        mImageViews.add(iv_reservation_sun);
        mImageViews.add(iv_reservation_mon);
        mImageViews.add(iv_reservation_tue);
        mImageViews.add(iv_reservation_wed);
        mImageViews.add(iv_reservation_thu);
        mImageViews.add(iv_reservation_fri);
        mImageViews.add(iv_reservation_sat);
        if (null != reservationDates && reservationDates.size() > 0) {
            mDates.addAll(reservationDates);
            mWeeks = getDeadWeeks(mDates);
            if (mDates.size() >= 7) {
                showChoose(1);
                showChoose(2);
                showChoose(3);
                showChoose(4);
                showChoose(5);
                showChoose(6);
                showChoose(7);
            } else if (mDates.size() < 7) {
                for (int i = 0; i < mDates.size(); i++) {
                    showChoose((mDates.get(i).getDay()+1));
                }
            }
            showChooseView(tv_reservation_every, iv_reservation_every, rl_reservation_every, Integer.MAX_VALUE);
            if (null != reservationHaveWeekList && reservationHaveWeekList.size() > 0) {
                allCount = 0;
                if (reservationHaveWeekList.size() == mWeeks.size()) {
                    iv_reservation_every.setVisibility(View.VISIBLE);
                } else {
                    iv_reservation_every.setVisibility(View.GONE);
                }
                for (int j = 0; j < mImageViews.size(); j++) {
                    backColor(mImageViews.get(j));
                }
                for (int i = 0; i < reservationHaveWeekList.size(); i++) {
                    showChoose(reservationHaveWeekList.get(i) + 1);
                }
                if (reservationHaveWeekList.contains(Calendar.SUNDAY - 1) && !reservationHaveWeekList.contains(Calendar.SATURDAY - 1)) {
                    setDayTip(Calendar.SUNDAY);
                } else if (reservationHaveWeekList.contains(Calendar.SATURDAY - 1) && !reservationHaveWeekList.contains(Calendar.SUNDAY - 1)) {
                    setDayTip(Calendar.SATURDAY);
                } else if (reservationHaveWeekList.contains(Calendar.SATURDAY - 1) && reservationHaveWeekList.contains(Calendar.SUNDAY - 1)) {
                    setDayTip();
                    allCount = mDates.size();
                } else {
//                    changeTextColor("平时 " + allCount + " 天,周末 0 天",
//                            String.valueOf(allCount), String.valueOf(0));
                    setCountText(allCount + "", "0");
                }
            } else {
                setDayTip();
                allCount = mDates.size();
            }
        }
        mWeeks = getDeadWeeks(mDates);
        if (null != reservationHaveWeekList && reservationHaveWeekList.size() > 0) {
            numCount = reservationHaveWeekList.size();
        } else {
            numCount = mWeeks.size();
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_reservation_week);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation_week;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.rl_reservation_every:
                // 每天的点击事件，点击之后将所有星期置为未选中状态
                if (iv_reservation_every.getVisibility() == View.VISIBLE) {
                    backColor(iv_reservation_every);
                    backColor(iv_reservation_mon);
                    backColor(iv_reservation_tue);
                    backColor(iv_reservation_wed);
                    backColor(iv_reservation_thu);
                    backColor(iv_reservation_fri);
                    backColor(iv_reservation_sat);
                    backColor(iv_reservation_sun);
                    numCount = 0;
//                    changeTextColor("平时 0 天,周末 0 天", "0", "0");
                    setCountText("0", "0");
                    allCount = 0;
                } else {
                    // 那么将所有的星期全部置为选中状态
                    changeText(iv_reservation_every, tv_reservation_every);
                    changeText(iv_reservation_mon, tv_reservation_mon);
                    changeText(iv_reservation_tue, tv_reservation_tue);
                    changeText(iv_reservation_wed, tv_reservation_wed);
                    changeText(iv_reservation_thu, tv_reservation_thu);
                    changeText(iv_reservation_fri, tv_reservation_fri);
                    changeText(iv_reservation_sat, tv_reservation_sat);
                    changeText(iv_reservation_sun, tv_reservation_sun);
                    numCount = mWeeks.size();
                    allCount = mDates.size();
                    setDayTip();
                }
                break;
            case R.id.rl_reservation_mon:
                chooseWeek(iv_reservation_mon, Calendar.MONDAY);
                break;
            case R.id.rl_reservation_tue:
                chooseWeek(iv_reservation_tue, Calendar.TUESDAY);
                break;
            case R.id.rl_reservation_wed:
                chooseWeek(iv_reservation_wed, Calendar.WEDNESDAY);
                break;
            case R.id.rl_reservation_thu:
                chooseWeek(iv_reservation_thu, Calendar.THURSDAY);
                break;
            case R.id.rl_reservation_fri:
                chooseWeek(iv_reservation_fri, Calendar.FRIDAY);
                break;
            case R.id.rl_reservation_sat:
                chooseWeek(iv_reservation_sat, Calendar.SATURDAY);
                break;
            case R.id.rl_reservation_sun:
                chooseWeek(iv_reservation_sun, Calendar.SUNDAY);
                break;
            default:
                break;
        }
    }

    private void setDayTip() {
        int count = getDays(Calendar.SUNDAY) + getDays(Calendar.SATURDAY);
//        changeTextColor("平时 " + (allCount - count) + " 天,周末 " + (count) + " 天",
//                String.valueOf(allCount - count), String.valueOf(count));


        setCountText(allCount - count + "", count + "");
    }


    private int getDays(int day) {
        int count = 0;
        for (Date d : mDates) {
            calendar.setTime(d);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            if (week == day) {
                count += 1;
            }
        }
        return count;
    }

   /* private void changeTextColor(String wholeStr, String greenStr1, String greenStr2) {
        StringFormatUtil spanStr = new StringFormatUtil(this, wholeStr,
                greenStr1, greenStr2, R.color.app_theme_color).fillColor(true);
        tv_all_count.setText(spanStr.getResult());
    }*/

    private void setCountText(String dayCount, String weekCount) {
        tv_all_count_day.setText(dayCount);
        tv_all_count_week.setText(weekCount);
    }

    /**
     * 星期状态
     *
     * @param iv   星期是否被选中
     * @param week 星期几
     */
    private void chooseWeek(ImageView iv, int week) {

        if (iv.getVisibility() == View.VISIBLE) {
            iv.setVisibility(View.GONE);
            iv_reservation_every.setVisibility(View.GONE);
            numCount--;
            int chooseDays = getDays(week);
            allCount = allCount - chooseDays;
            setDayTip(week, true);
        } else {
            iv.setVisibility(View.VISIBLE);
            numCount++;
            if (numCount == mWeeks.size()) {
                iv_reservation_every.setVisibility(View.VISIBLE);
            }
            int chooseDays = getDays(week);
            allCount = allCount + chooseDays;
            setDayTip(week, false);
        }

    }

    private void setDayTip(int week, boolean flag) {
        int zhouMo = 0;
        int pingshi = 0;
        if (flag) {
            if (week == Calendar.SUNDAY && iv_reservation_sat.getVisibility() == View.VISIBLE) {
                zhouMo = getDays(Calendar.SATURDAY);
            } else if (week == Calendar.SUNDAY && iv_reservation_sat.getVisibility() == View.GONE) {
                zhouMo = 0;
            } else if (week == Calendar.SATURDAY && iv_reservation_sun.getVisibility() == View.VISIBLE) {
                zhouMo = getDays(Calendar.SUNDAY);
            } else if (week == Calendar.SATURDAY && iv_reservation_sun.getVisibility() == View.GONE) {
                zhouMo = 0;
            } else if(iv_reservation_sat.getVisibility() == View.GONE && iv_reservation_sun.getVisibility() == View.GONE){
                zhouMo = 0;
            } else if(iv_reservation_sat.getVisibility() == View.VISIBLE && iv_reservation_sun.getVisibility() == View.GONE){
                zhouMo = getDays(Calendar.SATURDAY);
            }else if(iv_reservation_sat.getVisibility() == View.GONE && iv_reservation_sun.getVisibility() == View.VISIBLE){
                zhouMo = getDays(Calendar.SUNDAY);
            } else{
                zhouMo = getDays(Calendar.SATURDAY) + getDays(Calendar.SUNDAY);
            }
        } else {
            if (week == Calendar.SATURDAY && iv_reservation_sat.getVisibility() == View.VISIBLE &&
                    iv_reservation_sun.getVisibility() == View.GONE) {
                zhouMo = getDays(Calendar.SATURDAY);
            } else if (week == Calendar.SUNDAY && iv_reservation_sun.getVisibility() == View.VISIBLE &&
                    iv_reservation_sat.getVisibility() == View.GONE) {
                zhouMo = getDays(Calendar.SUNDAY);
            } else if(iv_reservation_sat.getVisibility() == View.GONE && iv_reservation_sun.getVisibility() == View.GONE){
                zhouMo = 0;
            } else if(iv_reservation_sat.getVisibility() == View.VISIBLE && iv_reservation_sun.getVisibility() == View.GONE){
                zhouMo = getDays(Calendar.SATURDAY);
            } else if(iv_reservation_sat.getVisibility() == View.GONE && iv_reservation_sun.getVisibility() == View.VISIBLE){
                zhouMo = getDays(Calendar.SUNDAY);
            } else {
                zhouMo = getDays(Calendar.SATURDAY) + getDays(Calendar.SUNDAY);
            }
        }
        pingshi = allCount - zhouMo;
//        changeTextColor("平时 " + pingshi + " 天,周末 " + zhouMo + " 天",
//                String.valueOf(pingshi), String.valueOf(zhouMo));
        setCountText(pingshi + "", zhouMo + "");
    }

    private void setDayTip(int week) {
        int zhouMo = 0;
        int pingshi = 0;
        zhouMo = getDays(week);
        pingshi = allCount - zhouMo;
//        changeTextColor("平时 " + pingshi + " 天,周末 " + zhouMo + " 天",
//                String.valueOf(pingshi), String.valueOf(zhouMo));
        setCountText(pingshi + "", zhouMo + "");
    }

    /**
     * 计算日期区内包含的星期
     *
     * @param lists 选择的日期区间
     * @return 去除重复的星期
     */
    private Set<Integer> getDeadWeeks(List<Date> lists) {
        Set<Integer> weeks = new HashSet<>();
        for (int i = 0; i < lists.size(); i++) {
            weeks.add(lists.get(i).getDay());
        }
        return weeks;
    }

    /**
     * 设置默认选中的星期，将界面变为选中状态
     *
     * @param i 日期中的天
     */
    private void showChoose(int i) {
        switch (i) {
            case 1:
                showChooseView(tv_reservation_sun, iv_reservation_sun, rl_reservation_sun, i);
                break;
            case 2:
                showChooseView(tv_reservation_mon, iv_reservation_mon, rl_reservation_mon, i);
                break;
            case 3:
                showChooseView(tv_reservation_tue, iv_reservation_tue, rl_reservation_tue, i);
                break;
            case 4:
                showChooseView(tv_reservation_wed, iv_reservation_wed, rl_reservation_wed, i);
                break;
            case 5:
                showChooseView(tv_reservation_thu, iv_reservation_thu, rl_reservation_thu, i);
                break;
            case 6:
                showChooseView(tv_reservation_fri, iv_reservation_fri, rl_reservation_fri, i);
                break;
            case 7:
                showChooseView(tv_reservation_sat, iv_reservation_sat, rl_reservation_sat, i);
                break;

            default:
                break;
        }
    }

    /**
     * 展示选中的星期
     *
     * @param text 展示可选的星期
     * @param week 星期
     */
    private void showChooseView(TextView text, ImageView iv, RelativeLayout rl, int week) {
        text.setTextColor(getResources().getColor(R.color.order_black));
        iv.setVisibility(View.VISIBLE);
        rl.setEnabled(true);
        if (week != Integer.MAX_VALUE) {
            allCount += getDays(week);
        }
    }

    /**
     * 星期的不选择状态
     *
     * @param iv 不选择星期
     */
    private void backColor(ImageView iv) {

        iv.setVisibility(View.GONE);
    }

    /**
     * 生成新的日期区间
     *
     * @param sd 开始日期字符串
     * @param ed 结束日期字符串
     * @return 返回新的日期区间
     */
    private Set<Date> getNewDeadDates(String sd, String ed) {
        Set<Date> changeDeadDates = new HashSet<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date bDate;
        Date eDate;
        try {
            bDate = format.parse(sd);
            eDate = format.parse(ed);
            changeDeadDates.add(bDate);
            for (int i = 0; i < getGapCount(sd, ed) - 1; i++) {
                long newB = bDate.getTime() + (24 * 60 * 60 * 1000 * (i + 1));
                Date date = new Date(newB);
                changeDeadDates.add(date);
            }
            changeDeadDates.add(eDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return changeDeadDates;

    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return 天数
     */
    private static int getGapCount(String start, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date startDate;
        Date endDate;
        try {
            startDate = format.parse(start);
            endDate = format.parse(end);
            Calendar fromCalendar = Calendar.getInstance();
            fromCalendar.setTime(startDate);
            fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
            fromCalendar.set(Calendar.MINUTE, 0);
            fromCalendar.set(Calendar.SECOND, 0);
            fromCalendar.set(Calendar.MILLISECOND, 0);

            Calendar toCalendar = Calendar.getInstance();
            toCalendar.setTime(endDate);
            toCalendar.set(Calendar.HOUR_OF_DAY, 0);
            toCalendar.set(Calendar.MINUTE, 0);
            toCalendar.set(Calendar.SECOND, 0);
            toCalendar.set(Calendar.MILLISECOND, 0);

            return (int) ((toCalendar.getTime().getTime() - fromCalendar
                    .getTime().getTime()) / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 星期的选中状态
     *
     * @param iv 显示星期被选中
     */
    private void changeText(ImageView iv, TextView tv) {
        if (tv.getCurrentTextColor() == getResources().getColor(R.color.order_black)) {
            iv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 返回数据
     */
    private void backReservation() {
        int WEEK = 1;
        Intent intent = new Intent();
        ArrayList<Integer> reservationWeekList = new ArrayList<>();
        for (int i = 0; i < mImageViews.size(); i++) {
            if (mImageViews.get(i).getVisibility() == View.VISIBLE) {
                reservationWeekList.add(i);
            }
        }
        if (reservationWeekList.size() > 0) {
            intent.putIntegerArrayListExtra("reservationWeek", reservationWeekList);
            setResult(WEEK, intent);
            finish();
        } else {
            UIUtils.showToastSafe(R.string.toast_select_one_day_at_least);
        }
    }

    @Override
    public void onBackPressed() {
        backReservation();
    }
}
