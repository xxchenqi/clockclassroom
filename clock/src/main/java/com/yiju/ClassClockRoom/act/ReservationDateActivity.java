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

import java.io.Serializable;
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

    //日期
    @ViewInject(R.id.calendar_view)
    private CalendarPickerView calendar_view;

    private int clickCount = 0;
    private List<Date> mDates = new ArrayList<>();
    private List<Date> oldDates = new ArrayList<>();
    private Date beginDate;
    private Date endDate;
    private Date nowDate;
    private boolean reservation_flag;
    private Date singleDate;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        Intent mReservationIntent = getIntent();
        if (null != mReservationIntent) {
            List<Date> reservationDates = (ArrayList<Date>) mReservationIntent.
                    getSerializableExtra("reservationHaveDate");
            reservation_flag = mReservationIntent.getBooleanExtra("reservation_flag", true);
            if(reservation_flag){
                if (null != reservationDates && reservationDates.size() > 0) {
                    singleDate = reservationDates.get(0);
                }
            }
            if (null != reservationDates && reservationDates.size() > 0) {
                mDates.clear();
                mDates.add(reservationDates.get(0));
                mDates.add(reservationDates.get(reservationDates.size() - 1));
            }
        }
        Calendar nextYear = Calendar.getInstance();
        nowDate = new Date();
        // 去掉时分秒
        nextYear.setTime(nowDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            nowDate = format.parse(nextYear.get(Calendar.YEAR) + "-" +
                    (nextYear.get(Calendar.MONTH) + 1) + "-"
                    + nextYear.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        beginDate = nowDate;
        calendar_view.setBeginDate(beginDate);
        nextYear.add(Calendar.YEAR, 1);

        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        endDate = nextYear.getTime();
        calendar_view.setEndDate(endDate);
        calendar_view.setDecorators(Collections
                .<CalendarCellDecorator>emptyList());
        if (reservation_flag) {
            head_title.setText(getString(R.string.reservation_choose_single_date));
            initSingleCalendar(nextYear);
        } else {
            head_title.setText(getString(R.string.reservation_choose_more_date));
            initMoreCalendar(nextYear);
        }
        calendar_view.smoothScrollToPosition(0);
    }

    /**
     * 选择单天
     * @param nextYear 标记
     */
    private void initSingleCalendar(Calendar nextYear) {
        if(null != singleDate){
            calendar_view.init(new Date(), nextYear.getTime())
                    .inMode(CalendarPickerView.SelectionMode.SINGLE)
                    .withSelectedDate(singleDate);
        }else {
            calendar_view.init(new Date(), nextYear.getTime())
                    .inMode(CalendarPickerView.SelectionMode.SINGLE);
        }

    }

    /**
     * 选择多天             标记
     *
     * @param nextYear     最大日期
     */
    private void initMoreCalendar(Calendar nextYear) {
        if(null != mDates && mDates.size() >0 &&mDates.get(0).getTime() < System.currentTimeMillis()){
            calendar_view.init(new Date(), nextYear.getTime()) // 设置显示的年月
                    .inMode(CalendarPickerView.SelectionMode.RANGE); // 设置多选
            calendar_view.setOldDates(mDates);
            oldDates.clear();
            oldDates.addAll(mDates);
        }else {
            calendar_view.init(new Date(), nextYear.getTime()) // 设置显示的年月
                    .inMode(CalendarPickerView.SelectionMode.RANGE) // 设置多选
                    .withSelectedDates(mDates);
            if(null != mDates && mDates.size() >0){
                int s = mDates.get(0).getDate();
                int sm = mDates.get(0).getMonth();
                int sy = mDates.get(0).getYear();
                int e = mDates.get(mDates.size()-1).getDate();
                int em = mDates.get(mDates.size()-1).getMonth();
                int ey = mDates.get(mDates.size()-1).getYear();
                calendar_view.setSPposition(s);
                calendar_view.setEPposition(e);
                calendar_view.setSMPposition(sm);
                calendar_view.setEMPposition(em);
                calendar_view.setSYPposition(sy);
                calendar_view.setEYPposition(ey);
            }
        }
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
        switch (v.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backReservation();
        super.onBackPressed();
    }

    /**
     * 返回数据
     */
    private void backReservation() {
        int DATE = 0;
        Intent intent = new Intent();
        if(null != oldDates && oldDates.size() > 0){
            intent.putExtra("reservationDate", (Serializable) oldDates);
        }else {
            List<Date> selectedDates = calendar_view.getSelectedDates();
            intent.putExtra("reservationDate", (Serializable) selectedDates);
        }
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
                calendar_view.setOldDates(null);
                oldDates.clear();
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
}
