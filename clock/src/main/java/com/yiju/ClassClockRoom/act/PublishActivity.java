package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.ChooseStoreBean;
import com.yiju.ClassClockRoom.bean.Course_DataInfo;
import com.yiju.ClassClockRoom.bean.Course_Edit_Info;
import com.yiju.ClassClockRoom.bean.Course_Person_Detail;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sandy on 2016/6/30/0030.
 */
public class PublishActivity extends BaseActivity implements View.OnClickListener {

    // 标题
    @ViewInject(R.id.iv_back_reservation)
    private ImageView iv_back_reservation;

    @ViewInject(R.id.rl_course_address)
    private RelativeLayout rl_course_address;

    @ViewInject(R.id.tv_course_address)
    private TextView tv_course_address;

    @ViewInject(R.id.rl_course_date)
    private RelativeLayout rl_course_date;

    @ViewInject(R.id.tv_course_date)
    private TextView tv_course_date;

    @ViewInject(R.id.rl_course_week)
    private RelativeLayout rl_course_week;

    @ViewInject(R.id.tv_course_week)
    private TextView tv_course_week;

    @ViewInject(R.id.rl_course_time)
    private RelativeLayout rl_course_time;

    @ViewInject(R.id.tv_course_time)
    private TextView tv_course_time;

    @ViewInject(R.id.tv_publish)
    private TextView tv_publish;

    final private int DATE = 0;
    final private int WEEK = 1;
    final private int TIME = 2;
    final private int ADDRESS = 4;
    final private int PUBLISH = 5;

    private List<Date> selectedDates;
    private ArrayList<Integer> reservationWeekList;
    private String reservationTime;
    //门店id
    private String sid;
    private Course_Edit_Info course_detail;
    private String start_time;
    private String end_time;
    private Calendar ca;
    private Course_DataInfo course_list_data;
    private Course_Person_Detail course_detail_data;

    @Override
    protected void initView() {
        tv_course_address.setVisibility(View.GONE);
        tv_course_date.setVisibility(View.GONE);
        tv_course_week.setVisibility(View.GONE);
        tv_course_time.setVisibility(View.GONE);
        iv_back_reservation.setOnClickListener(this);
        rl_course_address.setOnClickListener(this);
        rl_course_date.setOnClickListener(this);
        rl_course_week.setOnClickListener(this);
        rl_course_time.setOnClickListener(this);
        tv_publish.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            course_detail = (Course_Edit_Info) intent.getSerializableExtra("COURSE_DETAIL");
            course_list_data = (Course_DataInfo) intent.getSerializableExtra("COURSE_LIST_DATA");
            course_detail_data = (Course_Person_Detail) intent.getSerializableExtra("COURSE_DETAIL_DATA");
            ca = Calendar.getInstance();
            if (null != course_detail) {
                showEditPage();
            }
        }
    }

    /**
     * 编辑带来数据
     */
    private void showEditPage() {
        sid = course_detail.getSid();
        tv_course_address.setVisibility(View.VISIBLE);
        tv_course_date.setVisibility(View.VISIBLE);
        tv_course_week.setVisibility(View.VISIBLE);
        tv_course_time.setVisibility(View.VISIBLE);
        tv_course_address.setText(course_detail.getAddress());
        selectedDates = getNewDeadDates(course_detail.getStart_date(), course_detail.getEnd_date());
        showDateContent();
        String[] repeats = course_detail.getRepeat().split(",");
        reservationWeekList = new ArrayList<>();
        for (String str : repeats) {
            if (Integer.valueOf(str) == 7) {
                reservationWeekList.add(0);
            } else {
                reservationWeekList.add(Integer.valueOf(str));
            }
        }
        showWeekContent(reservationWeekList);
        showFormatTime(String.valueOf(course_detail.getStart_time()), String.valueOf(course_detail.getEnd_time()));
        reservationTime = tv_course_time.getText().toString();
        start_time = course_detail.getSchool_start_time();
        end_time = course_detail.getSchool_end_time();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_reservation:
                onBackPressed();
                break;
            case R.id.rl_course_address:
                showReservationActivity(ADDRESS);
                break;
            case R.id.rl_course_date:
                showReservationActivity(DATE);
                break;
            case R.id.rl_course_week:
                showReservationActivity(WEEK);
                break;
            case R.id.rl_course_time:
                showReservationActivity(TIME);
                break;
            case R.id.tv_publish://下一步
                showReservationActivity(PUBLISH);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
    }

    /**
     * 根据状态携带数据跳转页面
     *
     * @param what 页面替代符
     */
    private void showReservationActivity(int what) {
        Intent intent = null;
//        ReservationDate reservationDate = new ReservationDate();
        switch (what) {
            case ADDRESS:
                intent = new Intent(this, ChooseStoreActivity.class);
                intent.putExtra(ChooseStoreActivity.ACTION_SID, sid);
                break;
            case DATE:
                if (getFlag()) {
                    UIUtils.showToastSafe(R.string.course_reservation_address);
                    return;
                }
                intent = new Intent(this, ReservationDateActivity.class);
                intent.putExtra("reservation_flag", false);
                if (null != selectedDates && selectedDates.size() > 0) {
                    intent.putExtra("reservationHaveDate", (Serializable) selectedDates);
                }
                break;
            case WEEK:
                if (getFlag()) {
                    UIUtils.showToastSafe(R.string.course_reservation_address);
                    return;
                }
                intent = new Intent(this, ReservationWeekActivity.class);
                if (null != selectedDates
                        && selectedDates.size() > 0) {
                    intent.putExtra("reservationHaveDate", (Serializable) selectedDates);
                } else {
                    UIUtils.showToastSafe("请先选择上课的日期范围");
                    return;
                }
                if (null != reservationWeekList) {
                    intent.putIntegerArrayListExtra("reservationHaveWeek", reservationWeekList);
                }
                break;
            case TIME:
                if (getFlag()) {
                    UIUtils.showToastSafe(R.string.course_reservation_address);
                    return;
                }
                intent = new Intent(this, PublishTimeActivity.class);
                intent.putExtra("room_start_time", start_time);
                intent.putExtra("room_end_time", end_time);
                if (! StringUtils.isNullString(reservationTime)) {
                    intent.putExtra("reservationHaveTime", reservationTime);
                }
                break;
            case PUBLISH:
                if (getFlag()) {
                    UIUtils.showToastSafe(R.string.course_reservation_address);
                    return;
                }
                if(tv_course_date.getText().toString().equals(UIUtils.getString(R.string.reservation_date_title))) {
                    UIUtils.showToastSafe(R.string.toast_reservation_date_title);
                    return;
                }
                if(tv_course_week.getText().toString().equals(UIUtils.getString(R.string.reservation_week_title))) {
                    UIUtils.showToastSafe(R.string.toast_reservation_week_title);
                    return;
                }
                if(tv_course_time.getText().toString().equals(UIUtils.getString(R.string.reservation_time_title))) {
                    UIUtils.showToastSafe(R.string.toast_reservation_time_title);
                    return;
                }
                intent = new Intent(this, PublishCourseSecondActivity.class);
                intent.putExtra(ChooseStoreActivity.ACTION_SID, sid);
                intent.putExtra("DATE", tv_course_date.getText().toString());
                intent.putExtra("WEEK", tv_course_week.getText().toString());
                intent.putExtra("TIME", tv_course_time.getText().toString());

                if (null != course_list_data) {
                    intent.putExtra("COURSE_LIST_DATA", course_list_data);
                }
                if (null != course_detail_data) {
                    intent.putExtra("COURSE_DETAIL_DATA", course_detail_data);
                }
                break;
            default:
                break;
        }
        if (null != intent) {
            startActivityForResult(intent, 0);
        }
    }

    /**
     * 判断是否选择了门店
     *
     * @return 选择是否
     */
    private boolean getFlag() {

        return tv_course_address.getVisibility() != View.VISIBLE;
    }

    /**
     * 处理选择的日期时间
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            switch (resultCode) {
                case ADDRESS:
                    tv_course_address.setVisibility(View.VISIBLE);
                    ChooseStoreBean.DataEntity info = (ChooseStoreBean.DataEntity) data.getSerializableExtra(ChooseStoreActivity.ACTION_SID);
                    start_time = info.getStart_time();
                    end_time = info.getEnd_time();
                    tv_course_address.setText(info.getName());
                    sid = info.getSid();
                    showPage();
                    break;
                case DATE:
                    selectedDates = (ArrayList<Date>) data.getSerializableExtra("reservationDate");
                    if(null != selectedDates && selectedDates.size()>0){
                        course_detail = null;
                        showDateContent();
                    }
                    break;
                case WEEK:
                    reservationWeekList = data.getIntegerArrayListExtra("reservationWeek");
                    if (null != reservationWeekList && reservationWeekList.size() > 0) {
                        showWeekContent(reservationWeekList);
                    }
                    break;

                case TIME:
                    reservationTime = data.getStringExtra("reservationTime");
                    if(! StringUtils.isNullString(reservationTime)){
                        tv_course_time.setText(reservationTime);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void showPage() {
        // 获取当前的时间
        ca = Calendar.getInstance();
        tv_course_date.setVisibility(View.VISIBLE);
        tv_course_week.setVisibility(View.VISIBLE);
        tv_course_time.setVisibility(View.VISIBLE);

        if (null != course_detail) {
            showEditPage();
        }
    }

    /**
     * 转换日期字符串
     *
     * @param ca 日期
     * @return 20xx/xx/xx
     */
    private String getDateString(Calendar ca) {
        return ca.get(Calendar.YEAR) + "-"
                + (String.valueOf(ca.get(Calendar.MONTH) + 1).length() == 1 ?
                ("0" + String.valueOf(ca.get(Calendar.MONTH) + 1)) : String.valueOf(ca.get(Calendar.MONTH) + 1)) + "-"
                + (String.valueOf(ca.get(Calendar.DAY_OF_MONTH)).length() == 1 ?
                ("0" + String.valueOf(ca.get(Calendar.DAY_OF_MONTH))) : String.valueOf(ca.get(Calendar.DAY_OF_MONTH)));
    }

    /**
     * 显示用户选择的日期
     */
    private void showDateContent() {

        ca.setTime(selectedDates.get(0));
        String start_date = getDateString(ca);
        ca.setTime(selectedDates.get(selectedDates.size() - 1));
        String end_date = getDateString(ca);
        tv_course_date.setText(String.format(getString(R.string.to_symbol), start_date, end_date));
        StringBuilder sb_week = new StringBuilder();
        if (selectedDates.size() >= 7) {
            sb_week.append("周一 周二 周三 周四 周五 周六 周日");
        } else if (selectedDates.size() < 7) {
            ArrayList<Integer> weekInts = new ArrayList<>();
            for (int i = 0; i < selectedDates.size(); i++) {
                weekInts.add(selectedDates.get(i).getDay());
            }
            Collections.sort(weekInts);
            for (int i = 0; i < weekInts.size(); i++) {
                if (i == weekInts.size() - 1) {
                    sb_week.append(getNewWeek((weekInts.get(i))));
                } else {
                    sb_week.append(getNewWeek((weekInts.get(i)))).append(" ");
                }
            }
        }
        tv_course_week.setText(sb_week.toString());
        if(null != reservationWeekList && reservationWeekList.size() >0){
            reservationWeekList.clear();
        }
    }

    /**
     * 显示用户选择的星期
     *
     * @param list 保存星期的集合
     */
    private void showWeekContent(ArrayList<Integer> list) {

        Collections.sort(list);
        StringBuilder sbWeek = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sbWeek.append(getNewWeek(list.get(i)));
            } else {
                sbWeek.append(getNewWeek(list.get(i))).append(" ");
            }
        }
        tv_course_week.setText(sbWeek.toString());
    }

    /**
     * 根据日历选择框返回的对象中日期对象，返回星期字符串
     *
     * @param date 日期中的天
     * @return 星期字符串
     */
    private String getNewWeek(int date) {
        String week = null;
        switch (date) {
            case 0:
                week = "周日";
                break;
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;
            default:
                break;
        }
        return week;
    }


    /**
     * 显示标准的时间格式
     *
     * @param start_time 开始时间
     * @param end_time   结束时间
     */
    private void showFormatTime(String start_time, String end_time) {
        tv_course_time.setText(
                String.format(
                        getString(R.string.to_symbol),
                        StringUtils.changeTime(start_time),
                        StringUtils.changeTime(end_time)
                ));
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_course_reservation);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_course_reservation;
    }
}
