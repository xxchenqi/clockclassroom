package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.umeng.analytics.MobclickAgent;
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
//    private String hasWeek;
    private List<Date> haveDates;
    private List<Date> selectedDates;
    private List<Date> beDates;
    private Set<Date> grayDates;
    private long beginTime;
    private long endTime;

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
//        hasWeek = mReservationIntent.getStringExtra("hasWeek");
        beDates = (ArrayList<Date>) mReservationIntent.
                getSerializableExtra("BEDates");
        selectedDates = (ArrayList<Date>) mReservationIntent.
                getSerializableExtra("selectedDates");
        haveDates = (ArrayList<Date>) mReservationIntent.
                getSerializableExtra("haveDates");
        grayDates = (HashSet<Date>) mReservationIntent.
                getSerializableExtra("grayDates");
        if (null != selectedDates && selectedDates.size() > 0) {
            mDates.clear();
            mDates.addAll(selectedDates);
            beginTime = beDates.get(0).getTime() / 1000;
            endTime = beDates.get(beDates.size() - 1).getTime() / 1000;
        }
        if(null != haveDates && haveDates.size() > 0){
            mDates.clear();
            mDates.addAll(haveDates);
        }
        if (null != fail && "FAIL".equals(fail)) {
            //预订失败过来
            tv_has_date.setVisibility(View.VISIBLE);
            if(null != grayDates && grayDates.size()>0){
                for (Date d:grayDates) {
                    if(mDates.contains(d)){
                        mDates.remove(d);
                    }
                }
            }

        } else {
            //预订之前过来
            tv_has_date.setVisibility(View.GONE);
        }
        Collections.sort(mDates);
        initCalendarDate();


        //======================================================
        calendar_view.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                if (beginDate.getTime() <= (date.getTime() + (23 * 60 * 60 * 1000 + 59 * 60 * 1000)) &&
                        endDate.getTime() >= date.getTime()) {
                    // 此范围内可选
                    return null != grayDates && grayDates.size() > 0 && grayDates.contains(date);

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
//        long beginTime = mDates.get(0).getTime() / 1000;
//        long endTime = mDates.get(mDates.size() - 1).getTime() / 1000;
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
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_252");
                onBackPressed();
                break;
            case R.id.glide_pop:
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_251");
                SharedPreferencesUtils.saveInt(this,
                        "first_into_reservationAct", 1);
                glide_pop.setVisibility(View.GONE);
            default:
                break;
        }
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
