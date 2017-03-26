package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.AdjustmentData;
import com.yiju.ClassClockRoom.bean.DayTimeAllData;
import com.yiju.ClassClockRoom.bean.DayTimeData;
import com.yiju.ClassClockRoom.bean.DeviceTypeFull;
import com.yiju.ClassClockRoom.bean.DeviceTypeFull.StockArrEntity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.ReservationDate;
import com.yiju.ClassClockRoom.bean.ReservationFail;
import com.yiju.ClassClockRoom.bean.ReservationFail.DataEntity.ArrEntity;
import com.yiju.ClassClockRoom.bean.RoomAdjustEntity;
import com.yiju.ClassClockRoom.bean.RoomInfo;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ReservationDateLittleActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

    // 预订失败跳转到此页面时显示
    @ViewInject(R.id.tv_has_date)
    private TextView tv_has_date;

    @ViewInject(R.id.tv_right_now_reservation)
    private TextView tv_right_now_reservation;

    //日期
    @ViewInject(R.id.calendar_view_little)
    private CalendarPickerView calendar_view;

    @ViewInject(R.id.glide_pop)
    private View glide_pop;

    private List<Date> mDates = new ArrayList<>();//绿色日期集合
    private List<Date> mInfoDates = new ArrayList<>();//购物车数据回显日期集合
    private List<Date> mCopyDates = new ArrayList<>();//绿色日期集合备份
    private List<Date> mOneDatas = new ArrayList<>();//第一次进入个别日期调整日期集合
    private List<Date> mCopyOrangeDates = new ArrayList<>();//橙色日期集合
    private List<Date> mHightDates = new ArrayList<>();//未过滤星期的日期集合
    private Set<Date> blueDates = new HashSet<>();//蓝色日期集合
    private Set<Date> orangeDates = new HashSet<>();//橙色日期集合
    private List<DayTimeData> haveList;//用户已经操作过的列表
    private List<DayTimeData> bornLists;//初始化的列表
    private HashMap<String, List<DayTimeData>> allDayTimeMaps = new HashMap<>();//所有日期的时间选择页的数据
    private Map<String, List<DayTimeData>> maps;//预订失败后回显所有日期的时间列表的数据源
    private Map<String, List<DayTimeData>> requestMaps;//进行请求时当前所有日期的时间选择数据
    private List<AdjustmentData> add_date;//购物车返回的增加的课室数据
    private List<AdjustmentData> cancel_date;//购物车返回的取消的课室数据
    private Order2 info;
    private String room_name;
    private int room_count;
    private String time;
    private int maxCount;
    private String haveTitle;
    private Date beginDate;
    private Date endDate;
    private Calendar ca = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String uid;
    private String sid;
    private String room_id;
    private String quest_start_date;
    private String quest_end_date;
    private String repeat;
    private String request_begin_time;
    private String request_end_time;
    private String jsonDevice;
    private String title;
    private ReservationFail roomFail;
    private DeviceTypeFull deviceFail;
    private String fail;
    private String roomInfoJsonStr;
    private String hasWeek;
    private String room_start_time;
    private String room_end_time;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
        tv_right_now_reservation.setOnClickListener(this);
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
//        tv_right_now_reservation.setEnabled(true);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        head_title.setText(getString(R.string.reservation_date_little_title));
        head_right_text.setText(getString(R.string.label_save));
        Intent mReservationIntent = getIntent();
        if (null == mReservationIntent) {
            return;
        }
        info = (Order2) mReservationIntent.getSerializableExtra("info");
        mDates.clear();
        uid = mReservationIntent.getStringExtra("uid");
        sid = mReservationIntent.getStringExtra("sid");
        room_id = mReservationIntent.getStringExtra("room_id");
        quest_start_date = mReservationIntent.getStringExtra("start_date");
        quest_end_date = mReservationIntent.getStringExtra("end_date");
        repeat = mReservationIntent.getStringExtra("repeat");
        request_begin_time = mReservationIntent.getStringExtra("start_time");
        request_end_time = mReservationIntent.getStringExtra("end_time");
        room_start_time = mReservationIntent.getStringExtra("room_start_time");
        room_end_time = mReservationIntent.getStringExtra("room_end_time");
        jsonDevice = mReservationIntent.getStringExtra("device_str");
        fail = mReservationIntent.getStringExtra("FAIL");
        hasWeek = mReservationIntent.getStringExtra("hasWeek");
        room_name = mReservationIntent.getStringExtra("room_name");
        time = mReservationIntent.getStringExtra("time");
        roomInfoJsonStr = mReservationIntent.getStringExtra("roomInfoJsonStr");
        room_count = mReservationIntent.getIntExtra("room_count", 0);
        maxCount = mReservationIntent.getIntExtra("maxCount", 0);
        ReservationDate reservationDate = (ReservationDate) mReservationIntent.
                getSerializableExtra("reservationHaveDate");
        roomFail = (ReservationFail) mReservationIntent.
                getSerializableExtra("roomFail");
        deviceFail = (DeviceTypeFull) mReservationIntent.
                getSerializableExtra("deviceFail");
        if (null != fail && "FAIL".equals(fail)) {
            //预订失败过来
            tv_has_date.setVisibility(View.VISIBLE);
            tv_right_now_reservation.setVisibility(View.VISIBLE);
            head_right_text.setVisibility(View.GONE);
        } else {
            //预订之前过来
            tv_has_date.setVisibility(View.GONE);
            tv_right_now_reservation.setVisibility(View.GONE);
            head_right_text.setVisibility(View.VISIBLE);
        }
        ReservationDate blueHaveDate = (ReservationDate) mReservationIntent.
                getSerializableExtra("blueHaveDates");
        refreshData(roomFail, deviceFail);
        if (null != blueHaveDate) {
            blueDates.clear();
//            allDayTimeMaps.clear();
            Set<Date> blueDateHaveLists = blueHaveDate.getbDate();
            if (null != blueDateHaveLists && blueDateHaveLists.size() > 0) {
                blueDates.addAll(blueDateHaveLists);
            }
            maps = blueHaveDate.getMap();
            /*if(null != maps && maps.size()>0){
                allDayTimeMaps.putAll(maps);
            }*/
        }
        if (null != reservationDate) {
            List<Date> reservationDates = reservationDate.getDate();
            if (null != reservationDates && reservationDates.size() > 0) {
                mDates.addAll(reservationDates);
            }
        } else if (null == info) {
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DATE, 1);
            String start_date = ca.get(Calendar.YEAR) + "-"
                    + (ca.get(Calendar.MONTH) + 1) + "-"
                    + ca.get(Calendar.DAY_OF_MONTH);
            if (null == blueHaveDate) {
                SharedPreferencesUtils.saveString(this, start_date, "");
            }

            ca.setTime(new Date());
            ca.add(Calendar.DATE, 2);
            String end_date = ca.get(Calendar.YEAR) + "-"
                    + (ca.get(Calendar.MONTH) + 1) + "-"
                    + ca.get(Calendar.DAY_OF_MONTH);
            if (null == blueHaveDate) {
                SharedPreferencesUtils.saveString(this, end_date, "");
            }

            try {
                mDates.add(format.parse(start_date));
                mDates.add(format.parse(end_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            mDates.addAll(getNewDeadDates(info.getStart_date(), info.getEnd_date()));
        }
        /*if(null == mDates || mDates.size() == 0){
            mHightDates.clear();
            mHightDates.addAll(orangeDates);
            Collections.sort(mHightDates);
        }else {
            mHightDates.clear();
            mHightDates.addAll(mDates);
        }*/
        mHightDates.clear();
        mHightDates.addAll(mDates);
        mHightDates.addAll(orangeDates);
        Collections.sort(mHightDates);
        if (null == orangeDates || orangeDates.size() == 0) {
            List<Date> weekDates = new ArrayList<>();
            if (null != hasWeek) {
                String[] weeks = hasWeek.split(" ");
                int counts = weeks.length;
                if (counts < 7) {
                    for (String week : weeks) {
                        int intWeek = formatWeek(week);
                        for (int j = 0; j < mDates.size(); j++) {
                            ca.setTime(mDates.get(j));
                            int currentCa = ca.get(Calendar.DAY_OF_WEEK);
                            if (currentCa == intWeek) {
                                weekDates.add(mDates.get(j));
                            }
                        }
                    }
                    mDates.clear();
                    mDates.addAll(weekDates);
                }
            }
        }
        mCopyDates.clear();
        mCopyDates.addAll(mDates);
        mInfoDates.clear();
        mInfoDates.addAll(mDates);
        getOneData();
        if (null != info) {
            RoomAdjustEntity infoData = info.getRoom_adjust();
            add_date = infoData.getAdd_date();
            cancel_date = infoData.getCancel_date();
            if (null != add_date && add_date.size() > 0) {

                for (AdjustmentData addInfo : add_date) {
                    Date date = null;
                    try {
                        date = format.parse(addInfo.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (!mDates.contains(date)) {
                        mDates.add(date);
                    }
                }
                mCopyDates.clear();
                mCopyDates.addAll(mDates);
            }
            if (null != cancel_date && cancel_date.size() > 0) {

                for (AdjustmentData cancelInfo : cancel_date) {
                    Date date = null;
                    try {
                        date = format.parse(cancelInfo.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (mDates.contains(date) && room_count == Integer.valueOf(cancelInfo.getRoom_count())) {
                        mDates.remove(date);
                    }
                }
                mCopyDates.clear();
                mCopyDates.addAll(mDates);
            }
        }
        if (null == maps) {
            maps = new HashMap<>();
            requestMaps = new HashMap<>();
            if (null != info) {
                bornLists = new ArrayList<>();
                for (int i = 0; i < room_count; i++) {
                    DayTimeData dayTimeData = new DayTimeData();
                    dayTimeData.setDayTitle(room_name + " " + (i + 1));
                    dayTimeData.setTime(time);
                    dayTimeData.setFlag(false);
                    bornLists.add(dayTimeData);
                }
                if (null != cancel_date && cancel_date.size() > 0) {
                    for (int i = 0; i < mDates.size(); i++) {
                        ArrayList<DayTimeData> lastDayTimeDatas = new ArrayList<>();
                        lastDayTimeDatas.clear();
                        for (AdjustmentData cancelInfo : cancel_date) {
                            Date date = null;
                            try {
                                date = format.parse(cancelInfo.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (null != date && mDates.get(i).toString().equals(date.toString())) {
                                int cancelCount = Integer.valueOf(cancelInfo.getRoom_count());
                                for (int j = 0; j < (bornLists.size() - cancelCount); j++) {
                                    lastDayTimeDatas.add(bornLists.get(j));
                                }
                            }
                        }
                        ca.setTime(mDates.get(i));
                        String d_title = getTitleString(ca);
                        if (lastDayTimeDatas.size() > 0) {
                            requestMaps.put(d_title, lastDayTimeDatas);
                        } else {
                            requestMaps.put(d_title, bornLists);
                        }
                    }
                }
                if (null != add_date && add_date.size() > 0) {
                    for (int i = 0; i < mDates.size(); i++) {
                        ArrayList<AdjustmentData> sameDays = new ArrayList<>();
                        sameDays.clear();
                        for (AdjustmentData addInfo : add_date) {
                            Date date = null;
                            try {
                                date = format.parse(addInfo.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (null != date && date.toString().equals(mDates.get(i).toString())) {
                                sameDays.add(addInfo);
                            }
                        }
                        if (sameDays.size() > 0) {
                            ArrayList<DayTimeData> noDayTimeDatas = new ArrayList<>();
                            int count = 0;
                            for (AdjustmentData sameInfo : sameDays) {
                                if (mInfoDates.contains(mDates.get(i))) {
                                    DayTimeData dayTimeData = new DayTimeData();
                                    dayTimeData.setDayTitle(room_name + " " + bornLists.size());
                                    dayTimeData.setTime(
                                            String.format(
                                                    getString(R.string.to_symbol),
                                                    StringUtils.changeTime(sameInfo.getStart_time()),
                                                    StringUtils.changeTime(sameInfo.getEnd_time())
                                            ));
                                    dayTimeData.setFlag(false);
                                    bornLists.add(dayTimeData);
                                } else {
                                    count++;
                                    DayTimeData dayTimeData = new DayTimeData();
                                    dayTimeData.setDayTitle(room_name + " " + count);
                                    dayTimeData.setTime(
                                            String.format(
                                                    getString(R.string.to_symbol),
                                                    StringUtils.changeTime(sameInfo.getStart_time()),
                                                    StringUtils.changeTime(sameInfo.getEnd_time())
                                            ));
                                    dayTimeData.setFlag(false);
                                    noDayTimeDatas.add(dayTimeData);
                                }
                            }
                            ca.setTime(mDates.get(i));
                            String d_title = getTitleString(ca);
                            if (mInfoDates.contains(mDates.get(i))) {
                                requestMaps.put(d_title, bornLists);
                            } else if (noDayTimeDatas.size() > 0) {
                                requestMaps.put(d_title, noDayTimeDatas);
                            }
                        }
                    }
                }
            } else {
                for (Date d : mDates) {
                    ca.setTime(d);
                    String d_title = getTitleString(ca);
                    List<DayTimeData> greenLists = new ArrayList<>();
                    for (int i = 0; i < room_count; i++) {
                        DayTimeData dayTimeData = new DayTimeData();
                        dayTimeData.setDayTitle(room_name + " " + (i + 1));
                        dayTimeData.setTime(time);
                        dayTimeData.setFlag(false);
                        greenLists.add(dayTimeData);
                    }
                    maps.put(d_title, greenLists);
                }
                requestMaps.clear();
                requestMaps.putAll(maps);
            }
        } else {
            requestMaps = new HashMap<>();
            requestMaps.clear();
            requestMaps.putAll(maps);
        }
        if (null != blueDates && blueDates.size() > 0) {
            initCalendarDate(blueDates, null);
        } else {
            initCalendarDate(null, null);
        }
        if (null != orangeDates && orangeDates.size() > 0) {
            blueDates.clear();
            initCalendarDate(null, orangeDates);
        }
        //======================================================
        calendar_view.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                if (beginDate.getTime() <= (date.getTime() + (23 * 60 * 60 * 1000 + 59 * 60 * 1000)) &&
                        endDate.getTime() >= date.getTime()) {
                    jumpDayPage(date);
                }
                return true;
            }
        });
        //======================================================
    }

    /**
     * 获得第一次进入此页面的一手数据
     */
    private void getOneData() {
        boolean oneFlag = SharedPreferencesUtils.getBoolean(this, "oneFlag", true);
        if (oneFlag) {
            mOneDatas.clear();
            mOneDatas.addAll(mDates);
            ReservationDate oneData = new ReservationDate();
            SharedPreferencesUtils.saveBoolean(this, "oneFlag", false);
            oneData.setDate(mOneDatas);
            Gson gson = new Gson();
            SharedPreferencesUtils.saveString(this, "oneData", gson.toJson(oneData));
        } else {
            String oneDataIson = SharedPreferencesUtils.getString(this, "oneData", null);
            if (null != oneDataIson) {
                ReservationDate oneData = GsonTools.fromJson(oneDataIson, ReservationDate.class);
                mOneDatas = oneData.getDate();
            }
        }
    }

    /**
     * 获得橙色数据
     */
    private void refreshData(ReservationFail roomFail, DeviceTypeFull deviceFail) {
        if (null != roomFail) {
            orangeDates.clear();
            mCopyOrangeDates.clear();
            List<ArrEntity> roomFails = roomFail.getData().getArr();
            if (null != roomFails && roomFails.size() > 0) {
                Date date;
                for (int i = 0; i < roomFails.size(); i++) {
                    try {
                        if (roomFails.get(i).getMiss_count() != 0) {
                            date = format.parse(roomFails.get(i).getDate());
                            orangeDates.add(date);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
//            reFreashWeekData();
            mCopyOrangeDates.addAll(orangeDates);

        }
        if (null != deviceFail) {
            orangeDates.clear();
            mCopyOrangeDates.clear();
            List<StockArrEntity> deviceFails = deviceFail.getStock_arr();
            if (null != deviceFails && deviceFails.size() > 0) {
                HashSet<String> deviceArr = new HashSet<>();
                Date date;
                for (int i = 0; i < deviceFails.size(); i++) {
                    deviceArr.add(deviceFails.get(i).getDate());
                }
                if (deviceArr.size() > 0) {
                    for (String dateStr : deviceArr) {
                        try {
                            date = format.parse(dateStr);
                            if (!orangeDates.contains(date)) {
                                orangeDates.add(date);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
//            reFreashWeekData();
            mCopyOrangeDates.addAll(orangeDates);

        }
    }

    //======================================================

    /**
     * 初始化日期显示
     */
    private void initCalendarDate(Set<Date> bDates, Set<Date> oDates) {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        long beginTime = mHightDates.get(0).getTime() / 1000;
        long endTime = mHightDates.get(mHightDates.size() - 1).getTime() / 1000;
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
        if (null != bDates && bDates.size() > 0) {
            calendar_view.setBlueDates(bDates);
        }
        if (null != oDates && oDates.size() > 0) {
            calendar_view.setOrangeDates(oDates);
        }


    }
    //======================================================

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
            case R.id.head_right_text:
                backReservation();
                break;
            case R.id.tv_right_now_reservation:
                ProgressDialog.getInstance().dismiss();
                payRightNow();
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
     * 生成新的日期区间
     *
     * @param sd 开始日期字符串
     * @param ed 结束日期字符串
     * @return 返回新的日期区间
     */
    private List<Date> getNewDeadDates(String sd, String ed) {
        List<Date> changeDeadDates = new ArrayList<>();
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
     * 将星期字符串转换成数字
     *
     * @param week 星期字符串
     * @return 星期数字
     */
    private int formatWeek(String week) {
        int szWeek = 0;
        switch (week) {
            case "周一":
                szWeek = Calendar.MONDAY;
                break;
            case "周二":
                szWeek = Calendar.TUESDAY;
                break;
            case "周三":
                szWeek = Calendar.WEDNESDAY;
                break;
            case "周四":
                szWeek = Calendar.THURSDAY;
                break;
            case "周五":
                szWeek = Calendar.FRIDAY;
                break;
            case "周六":
                szWeek = Calendar.SATURDAY;
                break;
            case "周日":
                szWeek = Calendar.SUNDAY;
                break;
        }
        return szWeek;
    }

    /**
     * 跳转个别日期时间调整页面
     *
     * @param date 选择的当天
     */
    private void jumpDayPage(Date date) {

        ca.setTime(date);
        title = getTitleString(ca);
        Intent intent = new Intent(this, ReservationDateLittleDayActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("sid", sid);
        intent.putExtra("room_id", room_id);
        intent.putExtra("start_time", request_begin_time);
        intent.putExtra("end_time", request_end_time);
        intent.putExtra("maxCount", maxCount);
        intent.putExtra("room_count", room_count);
        intent.putExtra("room_name", room_name);
        intent.putExtra("time", time);
        intent.putExtra("title", title);
        intent.putExtra("room_start_time", room_start_time);
        intent.putExtra("room_end_time", room_end_time);
        if (null != fail && "FAIL".equals(fail)) {
            for (Map.Entry<String, List<DayTimeData>> failMaps : maps.entrySet()) {
                String key = failMaps.getKey();
                List<DayTimeData> value = failMaps.getValue();
                Date failDate = null;
                if (null != key) {
                    try {
                        failDate = format.parse(key);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (null != failDate && failDate.toString().equals(date.toString())) {
                    if (mCopyOrangeDates.contains(failDate) && mCopyOrangeDates.contains(date)) {
                        if (orangeDates.contains(failDate)) {
                            intent.putExtra("ORANGE", "ORANGE");
                            if (null != roomFail) {
                                intent.putExtra("roomFail", roomFail);
                            } else if (null != deviceFail) {
                                intent.putExtra("deviceFail", deviceFail);
                            }
                        }
                        bornFailDayList(intent, value);
                    } else if (mCopyDates.contains(failDate) && mCopyDates.contains(date)) {
                        bornFailDayList(intent, value);
                    }/*else if(mCopyOrangeDates.contains(date) || mCopyDates.contains(date)){
                        backData(intent);
                    }*/
                }
                backData(intent);
            }
        } else {
            if (null != info) {
                if (mCopyDates.contains(date)) {
                    bornInfoDayList(intent, date);
                }
                backData(intent);
            } else {
                if (mCopyDates.contains(date)) {
                    bornDayList(intent);
                }
                backData(intent);
            }

        }

        //=========================================================================================
        startActivityForResult(intent, 0);
    }

    /**
     * 购物车返回生成数据集合
     *
     * @param intent 将数据放在intent中
     */
    private void bornInfoDayList(Intent intent, Date chooseDate) {

        DayTimeAllData dayTimeAllData = new DayTimeAllData();
        bornLists = new ArrayList<>();
        for (int i = 0; i < room_count; i++) {
            DayTimeData dayTimeData = new DayTimeData();
            dayTimeData.setDayTitle(room_name + " " + (i + 1));
            dayTimeData.setTime(time);
            dayTimeData.setFlag(false);
            bornLists.add(dayTimeData);
        }
        if (null != cancel_date && cancel_date.size() > 0) {
            ArrayList<DayTimeData> lastDayTimeDatas = new ArrayList<>();
            lastDayTimeDatas.clear();
            for (AdjustmentData cancelInfo : cancel_date) {
                Date date = null;
                try {
                    date = format.parse(cancelInfo.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (null != date && date.toString().equals(chooseDate.toString())) {
                    int cancelCount = Integer.valueOf(cancelInfo.getRoom_count());
                    for (int i = 0; i < (bornLists.size() - cancelCount); i++) {
                        lastDayTimeDatas.add(bornLists.get(i));
                    }
                }
            }
            if (lastDayTimeDatas.size() > 0) {
                bornLists.clear();
                bornLists.addAll(lastDayTimeDatas);
            }

        }
        if (null != add_date && add_date.size() > 0) {

            ArrayList<AdjustmentData> sameDays = new ArrayList<>();
            sameDays.clear();
            for (AdjustmentData addInfo : add_date) {
                Date date = null;
                try {
                    date = format.parse(addInfo.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (null != date && date.toString().equals(chooseDate.toString())) {
                    sameDays.add(addInfo);
                }
            }
            if (sameDays.size() > 0) {
                ArrayList<DayTimeData> noDayTimeDatas = new ArrayList<>();
                int i = 1;
                for (AdjustmentData sameInfo : sameDays) {
                    if (mInfoDates.contains(chooseDate)) {
                        DayTimeData dayTimeData = new DayTimeData();
                        dayTimeData.setDayTitle(room_name + " " + bornLists.size());
                        dayTimeData.setTime(
                                String.format(
                                        getString(R.string.to_symbol),
                                        StringUtils.changeTime(sameInfo.getStart_time()),
                                        StringUtils.changeTime(sameInfo.getEnd_time())
                                ));
                        dayTimeData.setFlag(false);
                        bornLists.add(dayTimeData);
                    } else {
                        DayTimeData dayTimeData = new DayTimeData();
                        dayTimeData.setDayTitle(room_name + " " + i);
                        dayTimeData.setTime(
                                String.format(
                                        getString(R.string.to_symbol),
                                        StringUtils.changeTime(sameInfo.getStart_time()),
                                        StringUtils.changeTime(sameInfo.getEnd_time())
                                ));
                        dayTimeData.setFlag(false);
                        noDayTimeDatas.add(dayTimeData);
                    }
                }
                if (noDayTimeDatas.size() > 0) {
                    bornLists.clear();
                    bornLists.addAll(noDayTimeDatas);
                }
            }
        }

        dayTimeAllData.setList(bornLists);
        intent.putExtra("LISTDATA", dayTimeAllData);
    }

    /**
     * 为失败的数据的每天生成数据集合
     *
     * @param intent 将数据放在intent中
     * @param value  带回的当做基础数据
     */
    private void bornFailDayList(Intent intent, List<DayTimeData> value) {
        bornLists = value;
        DayTimeAllData failDayTimeAllData = new DayTimeAllData();
        failDayTimeAllData.setList(value);
        intent.putExtra("LISTDATA", failDayTimeAllData);
    }

    /**
     * 为每一天生成数据集合
     *
     * @param intent 将数据放在intent中
     */
    private void bornDayList(Intent intent) {

        DayTimeAllData dayTimeAllData = new DayTimeAllData();
        bornLists = new ArrayList<>();
        for (int i = 0; i < room_count; i++) {
            DayTimeData dayTimeData = new DayTimeData();
            dayTimeData.setDayTitle(room_name + " " + (i + 1));
            dayTimeData.setTime(time);
            dayTimeData.setFlag(false);
            bornLists.add(dayTimeData);
        }
        dayTimeAllData.setList(bornLists);
        intent.putExtra("LISTDATA", dayTimeAllData);
    }

    /**
     * 显示用户已经改变的数据
     *
     * @param intent 将数据放在intent中
     */
    private void backData(Intent intent) {
        DayTimeAllData changeDayTimeAllData = new DayTimeAllData();
        String gsonList = SharedPreferencesUtils.getString(this, title, null);
        if (null != haveTitle && title.equals(haveTitle)) {
            if (null != haveList && haveList.size() >= 0) {
                changeDayTimeAllData.setList(haveList);
                intent.putExtra("sendHaveList", changeDayTimeAllData);
            }
        } else if ("EMPTY".equals(gsonList)) {
            changeDayTimeAllData.setList(new ArrayList<DayTimeData>());
            intent.putExtra("sendHaveList", changeDayTimeAllData);
        } else if (!"".equals(gsonList)) {
            DayTimeAllData dayTimeAllData = GsonTools.fromJson(gsonList, DayTimeAllData.class);
            intent.putExtra("sendHaveList", dayTimeAllData);
        }
    }


    /**
     * 处理选择的日期时间
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int DATE_LITTLE_DAY = 5;
        if (resultCode == DATE_LITTLE_DAY) {
            if (null != data) {
                haveTitle = data.getStringExtra("haveTitle");
                String empty = data.getStringExtra("EMPTY");
                DayTimeAllData changeDayTimeList = (DayTimeAllData) data.getSerializableExtra("changeDayTimeList");
                Date date = null;
                if (null != haveTitle) {
                    try {
                        date = format.parse(haveTitle);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (null != changeDayTimeList) {
                    haveList = changeDayTimeList.getList();
                    if (null != haveList && haveList.size() > 0) {
                        Gson gson = new Gson();
                        String json = gson.toJson(changeDayTimeList);
                        SharedPreferencesUtils.saveString(this, haveTitle, json);
                        allDayTimeMaps.put(title, haveList);
                    } else if (null != haveList && haveList.size() == 0) {
                        SharedPreferencesUtils.saveString(this, haveTitle, "EMPTY");
                        allDayTimeMaps.put(title, null);
                    }
                    if (orangeDates.contains(date)) {
                        orangeDates.remove(date);
                    } else if (mDates.contains(date)) {
                        mDates.remove(date);
                    }
                    blueDates.add(date);
                    roomInfoJsonStr = null;
                } else {
                    haveList = null;
                    if (mCopyOrangeDates.contains(date) && !orangeDates.contains(date)) {
                        orangeDates.add(date);
                    }
                    if (mCopyDates.contains(date) && !mDates.contains(date)) {
                        mDates.add(date);
                    }
                    if (blueDates.contains(date)) {
                        blueDates.remove(date);
                    }
                    if (null != empty && "EMPTY".equals(empty)) {
                        if (allDayTimeMaps.containsKey(title)) {
                            allDayTimeMaps.remove(title);
                        }
                    } else {
                        allDayTimeMaps.put(title, bornLists);
                    }
                }
                initCalendarDate(blueDates, orangeDates);
            }
            if (null != allDayTimeMaps && allDayTimeMaps.size() > 0) {
                for (Map.Entry<String, List<DayTimeData>> colorSet : allDayTimeMaps.entrySet()) {
                    /*String key = colorSet.getKey();
                    List<DayTimeData> value = colorSet.getValue();
                    if (null != key && null != value && value.size() > 0) {
                        requestMaps.put(key, value);
                    }*/
                    if (null == requestMaps) {
                        requestMaps = new HashMap<>();
                    }
                    requestMaps.put(colorSet.getKey(), colorSet.getValue());
                }
            }
            ReservationDate oneData = new ReservationDate();
            for (Date date : blueDates) {
                if (!mOneDatas.contains(date)) {
                    mOneDatas.add(date);
                }
            }
            oneData.setDate(mOneDatas);
            Gson gson = new Gson();
            SharedPreferencesUtils.saveString(this, "oneData", gson.toJson(oneData));
        }

    }

    /**
     * 立即支付
     */
    private void payRightNow() {
//        tv_right_now_reservation.setEnabled(false);
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "schedule_step2");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("room_id", room_id);
        params.addBodyParameter("start_date", quest_start_date);
        params.addBodyParameter("end_date", quest_end_date);
        params.addBodyParameter("repeat", repeat);
        params.addBodyParameter("start_time", request_begin_time);
        params.addBodyParameter("end_time", request_end_time);
        params.addBodyParameter("room_count", String.valueOf(room_count));
        if (null != jsonDevice && !"".equals(jsonDevice)) {
            params.addBodyParameter("device_str", jsonDevice);
        }

        List<RoomInfo> roomInfos = new ArrayList<>();
        if (null != roomInfoJsonStr && !"".equals(roomInfoJsonStr)) {
            params.addBodyParameter("room_info", roomInfoJsonStr);
        } else {
            if (null != requestMaps && requestMaps.size() > 0) {
                int count = 0;
                for (Map.Entry<String, List<DayTimeData>> colorSet : requestMaps.entrySet()) {
                    List<RoomInfo.RoomInEntity> roomInEntities = new ArrayList<>();
                    RoomInfo roomInfo = new RoomInfo();
                    String title_date = colorSet.getKey();
                    roomInfo.setDate(title_date);
                    List<DayTimeData> timeLists = colorSet.getValue();
                    if (null != timeLists && timeLists.size() > 0) {
                        for (DayTimeData timeInfo : timeLists) {
                            RoomInfo.RoomInEntity roomInEntity = new RoomInfo.RoomInEntity();
                            String roomTime = timeInfo.getTime();
                            roomInEntity.setStart_time(Integer.valueOf(roomTime.split(" ~ ")[0].replace(":", "")));
                            roomInEntity.setEnd_time(Integer.valueOf(roomTime.split(" ~ ")[1].replace(":", "")));
                            roomInEntities.add(roomInEntity);
                        }
                        if (roomInEntities.size() > 0) {
                            roomInfo.setRoom_in(roomInEntities);
                            roomInfos.add(roomInfo);
                        }
                    } else {
                        count++;
                    }
                }
                if (count == requestMaps.size()) {
                    ProgressDialog.getInstance().dismiss();
                    UIUtils.showToastSafe(R.string.toast_select_one_day_at_least);
                    return;
                }
                Gson gson = new Gson();
                roomInfoJsonStr = gson.toJson(roomInfos);
                params.addBodyParameter("room_info", roomInfoJsonStr);
            }
        }
        Date date = null;
        try {
            date = format.parse(quest_start_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!checkDate(date, time, hasWeek)) {
            return;
        }
        httpUtils.send(HttpRequest.HttpMethod.POST,
                UrlUtils.SERVER_RESERVATION, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0,
                                          String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        parseData(arg0.result);
                        ProgressDialog.getInstance().dismiss();
                    }
                });
    }

    /**
     * 解析预订结果
     *
     * @param result 预订结果
     */
    private void parseData(String result) {
        JSONObject json;
        try {
            json = new JSONObject(result);
            String code = json.getString("code");
            switch (Integer.valueOf(code)) {
                case 1:
                    // 预订成功跳转下一步
                    Intent intent = new Intent(
                            ReservationDateLittleActivity.this,
                            OrderConfirmationActivity.class);
                    intent.putExtra("order2_id", json.getString("order2_id"));
//                    tv_right_now_reservation.setEnabled(true);
//                    clearDateLittle();
                    startActivity(intent);
                    break;
                case 40003:
                    // 参数不全
                    break;
                case 40044:
                    // 该用户已被加入黑名单
                    break;
                case 40050:
                    // 房间库存不足(跳转偏移界面)
                    ReservationFail roomFail = GsonTools
                            .changeGsonToBean(
                                    result,
                                    ReservationFail.class);
                    if (null != roomFail
                            && roomFail.getCode() == 40050
                            && (roomFail.getMsg())
                            .equals(getString(R.string.txt_lack_of_room_inventory))) {
                        int count = 0;
                        List<ArrEntity> arrEntities = roomFail.getData().getArr();
                        for (int i = 0; i < arrEntities.size(); i++) {
                            count += arrEntities.get(i).getMiss_count();
                        }
                        String s = count + " 间课室库存不足可进行个别日期调整或重新预订";
                        showRoomDialog(s, roomFail, null);
                    }

                    break;
                case 40051:
                    // 收费设备库存不足
                    DeviceTypeFull deviceFail = GsonTools.changeGsonToBean(result, DeviceTypeFull.class);
                    if (deviceFail == null) {
                        return;
                    }
                    /*List<StockArrEntity> stock_arr = deviceFail.getStock_arr();
                    HashSet<String> arrs = new HashSet<>();
                    for (int i = 0; i < stock_arr.size(); i++) {
                        arrs.add(stock_arr.get(i).getDate());
                    }
                    String s = arrs.size() + " 间课室的设备库存不足可进行个别日期调整或重新预订";*/
                    List<DeviceTypeFull.StockArrEntity> failDeviceData = deviceFail.getStock_arr();
                    int deviceUseCount = 0;
                    int deviceFailCount = 0;
                    if (null != failDeviceData && failDeviceData.size() > 0) {
                        for (DeviceTypeFull.StockArrEntity arr : failDeviceData) {
                            deviceUseCount += arr.getStock_available();
                            deviceFailCount += Integer.valueOf(arr.getRoom_count());
                        }
                    }
                    String s = (deviceFailCount - deviceUseCount) + " 间课室的设备库存不足可进行个别日期调整或重新预订";
                    showRoomDialog(s, null, deviceFail);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showRoomDialog(String s, final ReservationFail roomFailInfo, final DeviceTypeFull deviceFailInfo) {
        CustomDialog customDialog = new CustomDialog(
                ReservationDateLittleActivity.this,
                s);
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    refreshData(roomFailInfo, deviceFailInfo);
                    for (int i = 0; i < mOneDatas.size(); i++) {
                        if (orangeDates.contains(mOneDatas.get(i)) && mDates.contains(mOneDatas.get(i))) {
                            mDates.remove(mOneDatas.get(i));
                        } else if (!mDates.contains(mOneDatas.get(i))) {
                            mDates.add(mOneDatas.get(i));
                        }
                    }
                    roomFail = roomFailInfo;
                    deviceFail = deviceFailInfo;
                    blueDates.clear();

                    if (null != requestMaps && requestMaps.size() > 0) {
                        maps.clear();
                        maps.putAll(requestMaps);
                    }
                    initCalendarDate(null, orangeDates);
                }
            }
        });
    }

    /**
     * 检验是否包含今天，时间是否合法
     *
     * @param day  选中当天的而日期对象
     * @param time 选中当前的时间字符串
     * @param week 选中当前的星期字符串
     * @return 合法(true), 不合法(false)
     */
    private boolean checkDate(Date day, String time, String week) {
        Calendar ca = Calendar.getInstance();// 获取当前的时间
        Date today = ca.getTime();
        //当个别日期调整过以后
        String title = ca.get(Calendar.YEAR) + "-" +
                (String.valueOf(ca.get(Calendar.MONTH) + 1).length() == 1 ?
                        ("0" + String.valueOf(ca.get(Calendar.MONTH) + 1)) : String.valueOf(ca.get(Calendar.MONTH) + 1))
                + "-" + (String.valueOf(ca.get(Calendar.DAY_OF_MONTH)).length() == 1 ?
                ("0" + String.valueOf(ca.get(Calendar.DAY_OF_MONTH))) : String.valueOf(ca.get(Calendar.DAY_OF_MONTH)));
        String[] times = time.split(":");
        String checkTime = ca.get(Calendar.YEAR) + "-"
                + (ca.get(Calendar.MONTH) + 1) + "-"
                + ca.get(Calendar.DAY_OF_MONTH) + "-" + times[0] + "-"
                + times[1];
        try {
            ca.setTime(new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.getDefault())
                    .parse(checkTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ct = ca.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        if (today.getYear() == day.getYear() && today.getMonth() == day.getMonth()
                && today.getDate() == day.getDate()) {
            if (null != requestMaps && requestMaps.size() > 0) {
                if (requestMaps.containsKey(title)) {
                    List<DayTimeData> dayTimeDatas = requestMaps.get(title);
                    if (null != dayTimeDatas && dayTimeDatas.size() > 0) {
                        int count = 0;
                        for (DayTimeData timeInfo : dayTimeDatas) {
                            try {
                                ca.setTime(new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.getDefault())
                                        .parse(title + "-" + (timeInfo.getTime()).split(" ~ ")[0].replace(":", "-")));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long dayTime = ca.getTimeInMillis();
                            if (currentTime < dayTime && (dayTime - currentTime) > 30 * 60 * 1000) {
                                count++;
                            }
                        }
                        if (count == dayTimeDatas.size()) {
                            return true;
                        } else {
                            showTimeDialog();
                            return false;
                        }
                    }
                }
            }
            if (week.contains(getNewWeek(day.getDay()))) {
                if (currentTime < ct && (ct - currentTime) > 30 * 60 * 1000) {
                    return true;
                } else {
                    showTimeDialog();
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void showTimeDialog() {
        UIUtils.showToastSafe(getString(R.string.toast_course_start_time));
        ProgressDialog.getInstance().dismiss();
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
     * 清空用户操作
     */
    private void clearDateLittle() {
        if (null != blueDates && blueDates.size() > 0) {
            for (Date date : blueDates) {
                ca.setTime(date);
                String title = getTitleString(ca);
                SharedPreferencesUtils.saveString(this, title, "");
            }
            blueDates.clear();
        }
        if (null != mDates && mDates.size() > 0) {
            for (Date date : mDates) {
                ca.setTime(date);
                String title = getTitleString(ca);
                SharedPreferencesUtils.saveString(this, title, "");
            }
            mDates.clear();
        }
        if (null != orangeDates && orangeDates.size() > 0) {
            for (Date date : orangeDates) {
                ca.setTime(date);
                String title = getTitleString(ca);
                SharedPreferencesUtils.saveString(this, title, "");
            }
            orangeDates.clear();
        }

    }

    /**
     * 获得0000-00-00格式的日期
     *
     * @param calendar c
     * @return s
     */
    private String getTitleString(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "-" +
                (String.valueOf(calendar.get(Calendar.MONTH) + 1).length() == 1 ?
                        ("0" + String.valueOf(calendar.get(Calendar.MONTH) + 1)) : String.valueOf(calendar.get(Calendar.MONTH) + 1))
                + "-" + (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length() == 1 ?
                ("0" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))) : String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    /**
     * 返回数据
     */
    private void backReservation() {
        /*ReservationDate oneData = new ReservationDate();
        for (Date date:blueDates) {
            if(!mOneDatas.contains(date)){
                mOneDatas.add(date);
            }
        }
        oneData.setDate(mOneDatas);
        Gson gson = new Gson();
        SharedPreferencesUtils.saveString(this,"oneData",gson.toJson(oneData));*/
        int DATE_LITTLE = 4;
        Intent intent = new Intent();
        ReservationDate saveBlueHaveDate = new ReservationDate();
        if (null != blueDates && blueDates.size() > 0) {
            saveBlueHaveDate.setbDate(blueDates);
        }
        if (null != requestMaps && requestMaps.size() > 0) {
            saveBlueHaveDate.setMap(requestMaps);
        }
        intent.putExtra("blueHaveDate", saveBlueHaveDate);
        setResult(DATE_LITTLE, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferencesUtils.saveBoolean(this, "oneFlag", true);
        if (null != blueDates && blueDates.size() > 0) {
            clearDateLittle();
        }
        if (null != fail) {
            clearDateLittle();
        }
        finish();
    }

}
