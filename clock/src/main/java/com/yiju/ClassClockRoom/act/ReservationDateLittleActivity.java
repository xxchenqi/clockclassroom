package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationDateLittleActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    // 预订失败跳转到此页面时显示
    @ViewInject(R.id.tv_has_date)
    private TextView tv_has_date;

    //日期
    @ViewInject(R.id.calendar_view_little)
    private CalendarPickerView calendar_view;

    @ViewInject(R.id.glide_pop)
    private View glide_pop;

    private List<Date> mDates = new ArrayList<>();//选中日期集合
    private Date beginDate;
    private Date endDate;
    private Calendar ca = Calendar.getInstance();
    private String fail;
    private String hasWeek;
    private List<Date> haveDates;
    private List<Date> selectedDates;
    private Set<Date> grayDates;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        glide_pop.setOnClickListener(this);
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int status = SharedPreferencesUtils.getInt(this,
                "first_into_reservationAct", 0);
        if (status == 0
                && (height == 800 || height == 854 || height == 1280 || height == 1920)) {
            glide_pop.setVisibility(View.VISIBLE);
        } else {
            glide_pop.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        head_title.setText(getString(R.string.reservation_date_little_title));
        Intent mReservationIntent = getIntent();
        if (null == mReservationIntent) {
            return;
        }
        mDates.clear();
        fail = mReservationIntent.getStringExtra("FAIL");
        hasWeek = mReservationIntent.getStringExtra("hasWeek");
        selectedDates = (ArrayList<Date>) mReservationIntent.
                getSerializableExtra("selectedDates");
        haveDates = (ArrayList<Date>) mReservationIntent.
                getSerializableExtra("haveDates");
        grayDates = (HashSet<Date>) mReservationIntent.
                getSerializableExtra("grayDates");
        if (null != selectedDates && selectedDates.size() > 0) {
            mDates.clear();
            mDates.addAll(selectedDates);
            List<Date> weekDates = new ArrayList<>();
            if (null != hasWeek && !hasWeek.equals("每周")) {
                String[] weeks = hasWeek.substring(2,hasWeek.length()).split("、");
                int counts = weeks.length;
                if (counts < 7) {
                    for (String week : weeks) {
                        int intWeek = formatWeek(week);
                        for (int j = 0; j < mDates.size(); j++) {
                            ca.setTime(mDates.get(j));
                            if (ca.get(Calendar.DAY_OF_WEEK) == intWeek) {
                                weekDates.add(mDates.get(j));
                            }
                        }
                    }
                    mDates.clear();
                    mDates.addAll(weekDates);
                }
            }
        }
        if(null != haveDates && haveDates.size() > 0){
            mDates.clear();
            mDates.addAll(haveDates);
        }
        Collections.sort(mDates);
        if (null != fail && "FAIL".equals(fail)) {
            //预订失败过来
            tv_has_date.setVisibility(View.VISIBLE);

        } else {
            //预订之前过来
            tv_has_date.setVisibility(View.GONE);
        }
        initCalendarDate();


        //======================================================
        calendar_view.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                if (beginDate.getTime() <= (date.getTime() + (23 * 60 * 60 * 1000 + 59 * 60 * 1000)) &&
                        endDate.getTime() >= date.getTime()) {
                    // 此范围内可选
                    if(null != grayDates && grayDates.size()>0 && grayDates.contains(date)){
                        return true;
                    }else {
                        return false;
                    }

                }else {
                    return true;
                }

            }
        });
        //======================================================
    }

    /**
     * 初始化日期显示
     */
    private void initCalendarDate() {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        long beginTime = mDates.get(0).getTime() / 1000;
        long endTime = mDates.get(mDates.size() - 1).getTime() / 1000;
        long currentTime = System.currentTimeMillis() / 1000;
        long sevenTime = 7 * 24 * 60 * 60;
        long ninetyTime = 90 * 24 * 60 * 60;
        if ((beginTime - currentTime) > sevenTime) {
            beginDate = new Date((beginTime - sevenTime) * 1000);
        } else {
            beginDate = new Date(currentTime * 1000);
        }
        calendar_view.setBeginDate(beginDate);
        if ((endTime + sevenTime) > (beginTime + ninetyTime)) {
            endDate = new Date((beginTime + ninetyTime) * 1000);
        } else {
            endDate = new Date((endTime + sevenTime) * 1000);
        }
        calendar_view.setEndDate(endDate);
        calendar_view.setDecorators(Collections
                .<CalendarCellDecorator>emptyList());
        calendar_view.init(new Date(), nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDates(mDates);
        if(null != grayDates && grayDates.size()>0){
            calendar_view.setOrangeDates(grayDates);
        }

    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_reservation_date_little);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation_date_little;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.glide_pop:
                SharedPreferencesUtils.saveInt(this,
                        "first_into_reservationAct", 1);
                glide_pop.setVisibility(View.GONE);
            default:
                break;
        }
    }

    /**
     * 将星期字符串转换成数字
     *
     * @param week 星期字符串
     * @return 星期数字
     */
    private int formatWeek(String week) {
        int szWeek = 0;
        switch (week) {
            case "一":
                szWeek = Calendar.MONDAY;
                break;
            case "二":
                szWeek = Calendar.TUESDAY;
                break;
            case "三":
                szWeek = Calendar.WEDNESDAY;
                break;
            case "四":
                szWeek = Calendar.THURSDAY;
                break;
            case "五":
                szWeek = Calendar.FRIDAY;
                break;
            case "六":
                szWeek = Calendar.SATURDAY;
                break;
            case "日":
                szWeek = Calendar.SUNDAY;
                break;
        }
        return szWeek;
    }

    /**
     * 返回数据
     */
    private void backReservation() {
        int DATE_LITTLE = 4;
        Intent intent = new Intent();
        List<Date> newDates = calendar_view.getSelectedDates();
        if (null != newDates && newDates.size() > 0) {
            if(null != grayDates && grayDates.size()>0){
                for (Date date:grayDates) {
                    if(newDates.contains(date)){
                        newDates.remove(date);
                    }
                }
            }
            intent.putExtra("haveDates", (Serializable) newDates);
            setResult(DATE_LITTLE, intent);
            finish();
        }else {
            UIUtils.showToastSafe("请至少选择一天");
        }
    }

    @Override
    public void onBackPressed() {
        backReservation();
    }

}
