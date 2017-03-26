package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.ReservationDate;
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

    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

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

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
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
        head_right_text.setText(getString(R.string.label_save));
        Intent mReservationIntent = getIntent();
        Order2 info;
        if (null != mReservationIntent) {
            info = (Order2) mReservationIntent.getSerializableExtra("info");
        } else {
            return;
        }
        mDates.clear();
        ArrayList<Integer> reservationHaveWeekList = mReservationIntent.getIntegerArrayListExtra("reservationHaveWeek");
        ReservationDate reservationDate = (ReservationDate) mReservationIntent.
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
        if (null != reservationDate) {
            List<Date> reservationDates = reservationDate.getDate();
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
                        showChoose((mDates.get(i).getDay()) + 1);
                    }
                }
                showChooseView(tv_reservation_every, iv_reservation_every, rl_reservation_every);
                if (null != reservationHaveWeekList && reservationHaveWeekList.size() > 0) {
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
                }
            }
        } else if (null != info) {
            Set<Date> newDeadDates = getNewDeadDates(info.getStart_date(), info.getEnd_date());
            mDates.addAll(newDeadDates);
            String repeat = info.getRepeat();
            if (!("").equals(repeat)) {
                /*for (int i = 0; i < newDeadDates.size(); i++) {
                    showChoose(newDeadDates.get(i).getDay() + 1);
                }*/
                for (Date date : newDeadDates) {
                    showChoose(date.getDay() + 1);
                }
                tv_reservation_every.setTextColor(getResources().getColor(R.color.order_divider_view));
                iv_reservation_every.setVisibility(View.GONE);
                for (ImageView iv : mImageViews) {
                    backColor(iv);
                }
                for (Integer in : mWeeks) {
                    if (in == 0) {
                        mTextViews.get(6).setTextColor(
                                getResources().getColor(R.color.order_black));
                        mImageViews.get(6).setVisibility(View.VISIBLE);
                        mImageViews.get(6).setEnabled(true);
                    } else {
                        mTextViews.get(in - 1).setTextColor(
                                getResources().getColor(R.color.order_black));
                        mImageViews.get(in - 1).setVisibility(View.VISIBLE);
                        mImageViews.get(in - 1).setEnabled(true);
                    }
                }
                if (repeat.contains(",")) {
                    String[] repeats = repeat.split(",");
                    for (String repeat1 : repeats) {
                        if ("7".equals(repeat1)) {
                            showChoose(1);
                        } else {
                            showChoose(Integer.valueOf(repeat1) + 1);
                        }
                    }
                } else {
                    if (repeat.equals("7")) {
                        showChoose(0);
                    } else {
                        showChoose(Integer.valueOf(repeat) + 1);
                    }
                }
                tv_reservation_every.setTextColor(getResources().getColor(R.color.order_black));
                if (repeat.length() == 1 && repeat.length() == newDeadDates.size()) {
                    iv_reservation_every.setVisibility(View.VISIBLE);
                    numInfoCount = 1;
                } else if (repeat.length() >= 1) {
                    String[] repeats = repeat.split(",");
                    numInfoCount = repeats.length;
                    if (repeats.length == newDeadDates.size()) {
                        iv_reservation_every.setVisibility(View.VISIBLE);
                    } else {
                        iv_reservation_every.setVisibility(View.GONE);
                    }
                }/* else {
                    iv_reservation_every.setVisibility(View.GONE);
                }*/


            /*} else {
                for (int i = 0; i < newDeadDates.size(); i++) {
                    showChoose(newDeadDates.get(i).getDay() + 1);
                }
                tv_reservation_every.setTextColor(getResources().getColor(R.color.order_divider_view));
                iv_reservation_every.setVisibility(View.GONE);*/
            }else {
                for (Date date : newDeadDates) {
                    showChoose(date.getDay() + 1);
                }
                tv_reservation_every.setTextColor(getResources().getColor(R.color.order_black));
                iv_reservation_every.setVisibility(View.VISIBLE);
                numInfoCount = getDeadWeeks(mDates).size();
            }
            iv_reservation_every.setEnabled(true);
            rl_reservation_every.setEnabled(true);
        }
        mWeeks = getDeadWeeks(mDates);
        if (null != reservationHaveWeekList && reservationHaveWeekList.size() > 0) {
            numCount = reservationHaveWeekList.size();
        } else if (null != info) {
            numCount = numInfoCount;
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
                finish();
                break;
            case R.id.head_right_text:
                backReservation();
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
                }
                break;
            case R.id.rl_reservation_mon:
                chooseWeek(iv_reservation_mon);
                break;
            case R.id.rl_reservation_tue:
                chooseWeek(iv_reservation_tue);
                break;
            case R.id.rl_reservation_wed:
                chooseWeek(iv_reservation_wed);
                break;
            case R.id.rl_reservation_thu:
                chooseWeek(iv_reservation_thu);
                break;
            case R.id.rl_reservation_fri:
                chooseWeek(iv_reservation_fri);
                break;
            case R.id.rl_reservation_sat:
                chooseWeek(iv_reservation_sat);
                break;
            case R.id.rl_reservation_sun:
                chooseWeek(iv_reservation_sun);
                break;
            default:
                break;
        }
    }

    /**
     * 星期状态
     *
     * @param iv 星期是否被选中
     */
    private void chooseWeek(ImageView iv) {

        if (iv.getVisibility() == View.VISIBLE) {
            iv.setVisibility(View.GONE);
            iv_reservation_every.setVisibility(View.GONE);
            numCount--;

        } else {
            iv.setVisibility(View.VISIBLE);
            numCount++;
            if (numCount == mWeeks.size()) {
                iv_reservation_every.setVisibility(View.VISIBLE);
            }
        }
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
        switch (i - 1) {
            case 0:
                showChooseView(tv_reservation_sun, iv_reservation_sun, rl_reservation_sun);
                break;
            case 1:
                showChooseView(tv_reservation_mon, iv_reservation_mon, rl_reservation_mon);
                break;
            case 2:
                showChooseView(tv_reservation_tue, iv_reservation_tue, rl_reservation_tue);
                break;
            case 3:
                showChooseView(tv_reservation_wed, iv_reservation_wed, rl_reservation_wed);
                break;
            case 4:
                showChooseView(tv_reservation_thu, iv_reservation_thu, rl_reservation_thu);
                break;
            case 5:
                showChooseView(tv_reservation_fri, iv_reservation_fri, rl_reservation_fri);
                break;
            case 6:
                showChooseView(tv_reservation_sat, iv_reservation_sat, rl_reservation_sat);
                break;

            default:
                break;
        }
    }

    /**
     * 展示选中的星期
     *
     * @param text 展示可选的星期
     */
    private void showChooseView(TextView text, ImageView iv, RelativeLayout rl) {
        text.setTextColor(getResources().getColor(R.color.order_black));
        iv.setVisibility(View.VISIBLE);
        rl.setEnabled(true);
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
}
