package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.ReservationDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationDateActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

    //日期
    @ViewInject(R.id.calendar_view)
    private CalendarPickerView calendar_view;

    private int clickCount = 0;
    private List<Date> mDates = new ArrayList<>();
    private Date beginDate;
    private Date endDate;
    private Date nowDate;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        head_title.setText(getString(R.string.reservation_date));
        head_right_text.setText(getString(R.string.label_save));
        Intent mReservationIntent = getIntent();
        Order2 info;
        if (null != mReservationIntent) {
            info = (Order2) mReservationIntent.getSerializableExtra("info");
        }else{
            return;
        }
        mDates.clear();
        ReservationDate reservationDate = (ReservationDate) mReservationIntent.
                getSerializableExtra("reservationHaveDate");
        if (null != reservationDate) {
            List<Date> reservationDates = reservationDate.getDate();
            if (null != reservationDates && reservationDates.size() > 0) {
                mDates.add(reservationDates.get(0));
                mDates.add(reservationDates.get(reservationDates.size() - 1));
            }
        } else if (null == info) {
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DATE, 1);
            String start_date = ca.get(Calendar.YEAR) + "-"
                    + (ca.get(Calendar.MONTH) + 1) + "-"
                    + ca.get(Calendar.DAY_OF_MONTH);

            ca.setTime(new Date());
            ca.add(Calendar.DATE, 2);
            String end_date = ca.get(Calendar.YEAR) + "-"
                    + (ca.get(Calendar.MONTH) + 1) + "-"
                    + ca.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                mDates.add(format.parse(start_date));
                mDates.add(format.parse(end_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            mDates.addAll(getNewDeadDates(info.getStart_date(), info.getEnd_date()));
        }
        Calendar nextYear = Calendar.getInstance();
        nowDate = new Date();
        // 去掉时分秒
        nextYear.setTime(nowDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            nowDate = format.parse(nextYear.get(Calendar.YEAR) + "-" +
                    (nextYear.get(Calendar.MONTH) + 1)+ "-"
                    + nextYear.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        beginDate = nowDate;
        calendar_view.setBeginDate(beginDate);
        nextYear.add(Calendar.YEAR, 1);

        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        List<Date> dates = new ArrayList<>();

        endDate = nextYear.getTime();
        calendar_view.setEndDate(endDate);
        dates.add(mDates.get(0));
        dates.add(mDates.get(mDates.size() - 1));
        calendar_view.setDecorators(Collections
                .<CalendarCellDecorator>emptyList());
        calendar_view.init(new Date(), nextYear.getTime()) // 设置显示的年月
                .inMode(CalendarPickerView.SelectionMode.RANGE) // 设置多选
                .withSelectedDates(dates);
        int s = dates.get(0).getDate();
        int sm = dates.get(0).getMonth();
        int sy = dates.get(0).getYear();
        int e = dates.get(1).getDate();
        int em = dates.get(1).getMonth();
        int ey = dates.get(1).getYear();
        calendar_view.setSPposition(s);
        calendar_view.setEPposition(e);
        calendar_view.setSMPposition(sm);
        calendar_view.setEMPposition(em);
        calendar_view.setSYPposition(sy);
        calendar_view.setEYPposition(ey);
        calendar_view.smoothScrollToPosition(0);
        calendarSelectListener();
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_reservation_date);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation_date;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.head_right_text:
                backReservation();
                break;
            default:
                break;
        }
    }

    /**
     * 返回数据
     */
    private void backReservation() {
        int DATE = 0;
        Intent intent = new Intent();
        ReservationDate reservationDate = new ReservationDate();
        List<Date> selectedDates = calendar_view.getSelectedDates();
        reservationDate.setDate(selectedDates);
        intent.putExtra("reservationDate", reservationDate);
        setResult(DATE, intent);
        finish();
    }
    /**
     * 日历选择监听
     */
    private void calendarSelectListener() {

        calendar_view.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
            @Override
            public boolean isDateSelectable(Date date) {
                return nowDate.getTime() <= (date.getTime()) &&
                        endDate.getTime() >= date.getTime();
            }
        });
        final List<Date> ms = new ArrayList<>();
        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {

            @Override
            public void onDateUnselected(Date date) {
            }

            @Override
            public void onDateSelected(Date date) {
                // 选择日期时
                clickCount++;
                if (clickCount == 1) {
                    int i = date.getDate();
                    int im = date.getMonth();
                    int iy = date.getYear();
                    ms.clear();
                    ms.add(date);
                    calendar_view.setSPposition(i);
                    calendar_view.setSMPposition(im);
                    calendar_view.setSYPposition(iy);
                    calendar_view.setEPposition(Integer.MAX_VALUE);
                    calendar_view.setEMPposition(Integer.MAX_VALUE);
                    calendar_view.setEYPposition(Integer.MAX_VALUE);
                    beginDate = date;
                    long nowTime = date.getTime() / 1000;
                    long ninetyTime = 90 * 24 * 60 * 60;
                    endDate = new Date((nowTime + ninetyTime) * 1000);
                    calendar_view.setEndDate(endDate);
                } else if (clickCount == 2) {
                    clickCount = 0;
                    int i = date.getDate();
                    int im = date.getMonth();
                    int iy = date.getYear();
                    int fd = ms.get(0).getDate();
                    int fm = ms.get(0).getMonth();
                    int fy = ms.get(0).getYear();
                    if (fy == iy) {
                        //第一次年等于第二次年
                        if (fm == im) {
                            //月相等
                            if (fd > i) {
                                //第一次天大
                                changeStart(ms, date, i, im, iy);
                            } else {
                                //第一次天小
                                changeEnd();
                            }
                        } else if (fm > im) {
                            //一次月大
                            changeStart(ms, date, i, im, iy);
                        } else {
                            //一次月小
                            changeEnd();
                        }
                    } else if (fy > iy) {
                        //第一次年大于第二次年
                        changeStart(ms, date, i, im, iy);
                    } else {
                        //第一次年小于第二次
                        changeEnd();
                    }
                }
                calendar_view.validateAndUpdate();
            }

            private void changeEnd() {
                List<Date> ls = calendar_view.getSelectedDates();
                int x = ls.get(ls.size() - 1).getDate();
                int xm = ls.get(ls.size() - 1).getMonth();
                int xy = ls.get(ls.size() - 1).getYear();
                calendar_view.setEPposition(x);
                calendar_view.setEMPposition(xm);
                calendar_view.setEYPposition(xy);
            }

            private void changeStart(final List<Date> ms, Date date, int i,
                                     int im, int iy) {
                ms.clear();
                ms.add(date);
                clickCount = 1;
                calendar_view.setSPposition(i);
                calendar_view.setSMPposition(im);
                calendar_view.setSYPposition(iy);
                calendar_view.setEPposition(Integer.MAX_VALUE);
                calendar_view.setEMPposition(Integer.MAX_VALUE);
                calendar_view.setEYPposition(Integer.MAX_VALUE);
            }
        });
    }
    /**
     * 生成新的日期区间
     *
     * @param sd 开始日期字符串
     * @param ed 结束日期字符串
     * @return 返回新的日期区间
     */
    private List<Date> getNewDeadDates(String sd, String ed) {
        List<Date> changeDeadDates = new ArrayList<>();
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
}
