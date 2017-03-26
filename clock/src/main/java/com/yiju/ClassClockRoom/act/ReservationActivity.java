package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.AdjustmentData;
import com.yiju.ClassClockRoom.bean.DayTimeData;
import com.yiju.ClassClockRoom.bean.DeviceTypeFull;
import com.yiju.ClassClockRoom.bean.DeviceTypeFull.StockArrEntity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.ReservationBean;
import com.yiju.ClassClockRoom.bean.ReservationDate;
import com.yiju.ClassClockRoom.bean.ReservationFail;
import com.yiju.ClassClockRoom.bean.ReservationFail.DataEntity.ArrEntity;
import com.yiju.ClassClockRoom.bean.RoomAdjustEntity;
import com.yiju.ClassClockRoom.bean.RoomInfo;
import com.yiju.ClassClockRoom.bean.RoomInfo.RoomInEntity;
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

/**
 * ============================================================
 * <p/>
 * 作      者  : Sandy
 * <p/>
 * 日      期  : 2016/5/3/0005 09:55
 * <p/>
 * 描      述  :
 * <p/>
 * ============================================================
 */
public class ReservationActivity extends BaseActivity implements
        OnClickListener {

    final private int DATE = 0;
    final private int WEEK = 1;
    final private int TIME = 2;
    final private int DEVICE = 3;
    final private int DATE_LITTLE = 4;
    // 标题
    @ViewInject(R.id.tv_reservation_school)
    private TextView tv_reservation_school;

    @ViewInject(R.id.tv_reservation_login)
    private TextView tv_reservation_login;

    @ViewInject(R.id.iv_back_reservation)
    private ImageView iv_back_reservation;

    // 选择日期
    @ViewInject(R.id.rl_date)
    private RelativeLayout rl_date;

    @ViewInject(R.id.tv_date)
    private TextView tv_date;

    // 选择星期
    @ViewInject(R.id.rl_week)
    private RelativeLayout rl_week;

    @ViewInject(R.id.tv_week)
    private TextView tv_week;

    // 选择时间
    @ViewInject(R.id.rl_time)
    private RelativeLayout rl_time;

    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    //个别日期调整
    @ViewInject(R.id.rl_reservation_little)
    private RelativeLayout rl_reservation_little;

    // 课室数量（不能小于1）
    @ViewInject(R.id.iv_room_reduce)
    private ImageView iv_room_reduce;
    @ViewInject(R.id.iv_room_add)
    private ImageView iv_room_add;
    @ViewInject(R.id.et_room_count)
    private EditText et_room_count;

    // 可选设备
    @ViewInject(R.id.rl_device)
    private RelativeLayout rl_device;

    @ViewInject(R.id.tv_device)
    private TextView tv_device;

    // 立即支付
    @ViewInject(R.id.tv_reservation)
    private TextView tv_reservation;

    //布局
    @ViewInject(R.id.ll_reservation)
    private LinearLayout ll_reservation;
    //描述信息
    @ViewInject(R.id.tv_reservation_describe_all)
    private TextView tv_reservation_describe_all;
    //显示全部
    @ViewInject(R.id.tv_reservation_describe)
    private TextView tv_reservation_describe;
    private boolean is_open = false;

    // 房间数量上限
    private int maxRoomCount;
    private List<String> rooms;
    private Order2 info;
    private List<Date> selectedDates;
    private String room_start_time;
    private String room_end_time;
    private String reservationTime;
    private ArrayList<Integer> reservationWeekList;
    private ReservationBean reservationBean;
    private boolean flag = true;
    private ReservationBean reservationHaveBean;
    private String uid;
    private String sid;
    private List<ReservationBean.ReservationDevice> haveDevice;
    private String room_name;
    private Set<Date> blueDateLists;
    private Set<Date> orangeDates = new HashSet<>();
    private Calendar ca = Calendar.getInstance();
    private ReservationDate blueHaveDate;
    private String quest_start_date;
    private String quest_end_date;
    private String request_begin_time;
    private String request_end_time;
    private String room_id;
    private String repeat;
    private String jsonDevice;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String roomInfoJsonStr;
    private Map<String, List<DayTimeData>> colorMap;
    private int lineCount;
    private String instruction;
    private String confirm_type;

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation;
    }

    /**
     * 初始化页面
     */
    @Override
    public void initView() {
        iv_back_reservation.setOnClickListener(this);
        tv_reservation_login.setOnClickListener(this);
        rl_date.setOnClickListener(this);
        rl_week.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        rl_reservation_little.setOnClickListener(this);
        rl_device.setOnClickListener(this);
        tv_reservation.setOnClickListener(this);
        iv_room_reduce.setOnClickListener(this);
        iv_room_add.setOnClickListener(this);
        iv_room_reduce.setEnabled(true);
        iv_room_add.setEnabled(true);
        tv_reservation_describe_all.setOnClickListener(this);
//        tv_reservation.setEnabled(true);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        Intent intent = getIntent();
        String name;
        String type_id;
        sid = intent.getStringExtra("sid");
        name = intent.getStringExtra("name");
        type_id = intent.getStringExtra("type_id");
        room_start_time = intent.getStringExtra("room_start_time");
        room_end_time = intent.getStringExtra("room_end_time");
        room_name = intent.getStringExtra("room_name");
        instruction = intent.getStringExtra("instruction");
        confirm_type = intent.getStringExtra("confirm_type");
        getRoomDeviceCount(sid, type_id);
        tv_reservation_school.setText(name);

        info = (Order2) intent.getSerializableExtra("info");

        if (StringUtils.isNotNullString(instruction)) {
            String str;
            if (!"0".equals(confirm_type)) {
                str = "课室预订订单将在订单确认通过后生效\r\n" + instruction;
            } else {
                str = instruction;
            }
            tv_reservation_describe.setText(str);
            tv_reservation_describe.post(new Runnable() {
                @Override
                public void run() {
                    lineCount = tv_reservation_describe.getLineCount();
                    if (lineCount > 3) {
                        tv_reservation_describe.setMaxLines(3);
                    } else {
                        tv_reservation_describe_all.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            if (!"0".equals(confirm_type)) {
                tv_reservation_describe.setText(R.string.txt_reservation_describe);
                tv_reservation_describe_all.setVisibility(View.GONE);
            } else {
                ll_reservation.setVisibility(View.GONE);
            }
        }


        if (null != info) {
            backData(info.getStart_date(), info.getEnd_date(),
                    info.getStart_time(), info.getEnd_time());
        } else {
            Calendar ca = Calendar.getInstance();// 获取当前的时间
            ca.add(Calendar.DATE, 1);// 获取第二天的时间
            String start_date = getDateString(ca);
            String weekBegin = getWeek(ca.get(Calendar.DAY_OF_WEEK));
            ca.setTime(new Date());
            ca.add(Calendar.DATE, 2);// 获取第三天的时间
            String end_date = getDateString(ca);
            String weekEnd = getWeek(ca.get(Calendar.DAY_OF_WEEK));
            tv_date.setText(
                    String.format(
                            getString(R.string.to_symbol),
                            start_date,
                            end_date
                    ));
            tv_week.setText(weekBegin + " " + weekEnd);
            showFormatTime(room_start_time, room_end_time);
        }
        et_room_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!flag) {
                    return;
                }
                String counts = charSequence.toString();
                if (!"".equals(counts)) {
                    if (Integer.valueOf(counts) > 1) {
                        iv_room_reduce.setEnabled(true);
                        iv_room_add.setEnabled(true);
                        iv_room_reduce.setImageResource(R.drawable.order_reduce_btn_click);
                        if (Integer.valueOf(counts) == maxRoomCount) {
                            iv_room_add.setImageResource(R.drawable.order_add_btn_noclick);
                        } else {
                            iv_room_add.setImageResource(R.drawable.order_add_btn_click);
                        }
                    }
                } else {
                    et_room_count.setText("1");
                    iv_room_reduce.setImageResource(R.drawable.order_reduce_btn_noclick);
                }
                if ("0".equals(counts)) {
                    flag = false;
                    et_room_count.setText("1");
                    flag = true;
                } else {
                    if (null != rooms && rooms.size() > 0) {
                        if (!"".equals(counts)) {
                            if (Integer.valueOf(counts) > rooms.size()) {
                                flag = false;
                                et_room_count.setText(String.valueOf(rooms.size()));
                                flag = true;
                            } else if (Integer.valueOf(counts) < 1) {
                                flag = false;
                                et_room_count.setText(String.format(UIUtils.getString(R.string.one), rooms.size()));
                                flag = true;
                            } else {
                                flag = false;
                                et_room_count.setText(counts);
                                flag = true;
                            }
                        }
                    }
                }
                Selection.setSelection(et_room_count.getText(), et_room_count.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * 转换日期字符串
     *
     * @param ca 日期
     * @return 20xx/xx/xx
     */
    private String getDateString(Calendar ca) {
        return ca.get(Calendar.YEAR) + "年"
                + (String.valueOf(ca.get(Calendar.MONTH) + 1).length() == 1 ?
                ("0" + String.valueOf(ca.get(Calendar.MONTH) + 1)) : String.valueOf(ca.get(Calendar.MONTH) + 1)) + "月"
                + (String.valueOf(ca.get(Calendar.DAY_OF_MONTH)).length() == 1 ?
                ("0" + String.valueOf(ca.get(Calendar.DAY_OF_MONTH))) : String.valueOf(ca.get(Calendar.DAY_OF_MONTH))) + "日";
    }

    /**
     * 获得课室和设备数量上限
     *
     * @param sid     学校id
     * @param type_id 课室类型id
     */
    private void getRoomDeviceCount(String sid, String type_id) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "type_rooms");
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("type", type_id);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_RESERVATION,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);
                    }
                });
    }

    /**
     * 解析结果集获得房间上限
     *
     * @param result 服务器返回的结果集
     */
    private void processData(String result) {

        reservationBean = GsonTools.changeGsonToBean(result, ReservationBean.class);

        if (null != reservationBean) {
            if (reservationBean.getCode() == 1) {
                rooms = reservationBean.getData();
                if (null != rooms && rooms.size() > 0) {
                    maxRoomCount = rooms.size();
                    int count = Integer.valueOf(et_room_count.getText().toString());
                    if (count == maxRoomCount) {
                        iv_room_add.setImageResource(R.drawable.order_add_btn_noclick);
                    } else {
                        iv_room_add.setImageResource(R.drawable.order_add_btn_click);
                    }
                }
            }
        } else {
            UIUtils.showToastSafe(R.string.fail_data_request);
        }
    }

    @Override
    public void onClick(View v) {
        // 处理点击事件
        switch (v.getId()) {
            case R.id.iv_back_reservation:
                onBackPressed();
                break;
            case R.id.rl_date:
                // 选择日历
                showReservationActivity(DATE);
                break;
            case R.id.rl_week:
                // 选择星期
                showReservationActivity(WEEK);
                break;
            case R.id.rl_time:
                // 选择时间
                showReservationActivity(TIME);
                break;
            case R.id.rl_reservation_little:
                // 个别日期调整
                showReservationActivity(DATE_LITTLE);
                break;
            case R.id.iv_room_reduce:
                // 减掉
                reduceCount(iv_room_reduce, iv_room_add, et_room_count);
                clearDateLittle();
                break;
            case R.id.iv_room_add:
                // 增加
                addCount(iv_room_reduce, iv_room_add, et_room_count, maxRoomCount);
                clearDateLittle();
                break;
            case R.id.rl_device:
                // 选择设备
                showReservationActivity(DEVICE);
                break;
            case R.id.tv_reservation:
                // 立即支付
                if ("".equals(et_room_count.getText().toString())) {
                    UIUtils.showToastSafe(getString(R.string.toast_select_one_classroom_at_least));
                    return;
                }
                ProgressDialog.getInstance().show();
                payRightNow();
                break;
            case R.id.tv_reservation_login:
                // 登录
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.tv_reservation_describe_all:
                if (is_open) {
                    //收起
                    is_open = false;
                    tv_reservation_describe_all.setText(R.string.course_detial_more_intro);
                    tv_reservation_describe.setMaxLines(3);
                } else {
                    //展开
                    is_open = true;
                    tv_reservation_describe_all.setText(R.string.course_detial_more_intro_pull);
                    tv_reservation_describe.setMaxLines(Integer.MAX_VALUE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示当期页面数据
     *
     * @param start_date 开始日期
     * @param end_date   结束日期
     * @param start_time 开始时间
     * @param end_time   结束时间
     */
    private void backData(String start_date, String end_date, String start_time, String end_time) {
        String[] start_dates = start_date.split("-");
        String[] end_dates = end_date.split("-");
        tv_date.setText(
                String.format(
                        getString(R.string.to_symbol),
                        start_dates[0] + "-" + start_dates[1] + "-" + start_dates[2],
                        end_dates[0] + "-" + end_dates[1] + "-" + end_dates[2]
                ));
//        tv_week.setText(getInfoWeek(start_date) + " " + getInfoWeek(end_date));
//        showFormatTime(start_time, end_time);
        String repeat = info.getRepeat();
        if (null != repeat && !"".equals(repeat)) {
            if (repeat.length() != 1) {
                String[] repeats = repeat.split(",");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < repeats.length; i++) {
                    if (i == repeats.length - 1) {
                        sb.append(formatNewWeek(repeats[i]));
                    } else {
                        sb.append(formatNewWeek(repeats[i])).append(" ");
                    }
                }
                tv_week.setText(sb.toString());
            } else {
                tv_week.setText(formatNewWeek(repeat));
            }
        } else if ("".equals(repeat)) {
            Set<Date> newDeadDates = getNewDeadDates(info.getStart_date(), info.getEnd_date());
            ArrayList<Date> mDates = new ArrayList<>();
            mDates.addAll(newDeadDates);
            Collections.sort(mDates);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mDates.size(); i++) {
                ca.setTime(mDates.get(i));
                String week = getWeek(ca.get(Calendar.DAY_OF_WEEK));
                if (i == mDates.size() - 1) {
                    sb.append(week);
                } else {
                    sb.append(week).append(" ");
                }
            }
            tv_week.setText(sb.toString());
        }
        tv_time.setText(
                String.format(
                        getString(R.string.to_symbol),
                        StringUtils.changeTime(start_time),
                        StringUtils.changeTime(end_time)
                ));
        et_room_count.setText(info.getRoom_count());
        if (Integer.valueOf(info.getRoom_count()) != 0) {
            iv_room_reduce.setImageResource(R.drawable.order_reduce_btn_click);
            iv_room_reduce.setEnabled(true);
        } else {
            iv_room_reduce.setImageResource(R.drawable.order_reduce_btn_noclick);
            iv_room_reduce.setEnabled(false);
        }
        iv_room_add.setEnabled(true);

    }

    /**
     * 根据日期获得星期
     *
     * @param info_date 日期
     * @return 星期
     */
    private String getInfoWeek(String info_date) {
        try {
            Date date = format.parse(info_date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return getWeek(calendar.get(Calendar.DAY_OF_WEEK));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 显示标准的时间格式
     *
     * @param start_time 开始时间
     * @param end_time   结束时间
     */
    private void showFormatTime(String start_time, String end_time) {
        String bh;
        String bm;
        if (start_time.length() != 4) {
            bh = "0" + start_time.substring(0, 1);
            bm = start_time.substring(1);
        } else {
            bh = start_time.substring(0, 2);
            bm = start_time.substring(2);
        }
        String eh;
        String em;
        if (end_time.length() != 4) {
            eh = "0" + end_time.substring(0, 1);
            em = end_time.substring(1);
        } else {
            eh = end_time.substring(0, 2);
            em = end_time.substring(2);
        }
        tv_time.setText(
                String.format(
                        getString(R.string.to_symbol),
                        String.format(UIUtils.getString(R.string.colon), bh, bm),
                        String.format(UIUtils.getString(R.string.colon), eh, em)
                ));
    }

    /**
     * 根据日期中阿拉伯数字转化为自定义星期
     *
     * @param i 日期中的天
     * @return 星期字符串
     */
    private String getWeek(int i) {
        String week = null;
        switch (i - 1) {
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
     * 增加数量
     *
     * @param ivR      减少按钮
     * @param ivA      增加按钮
     * @param et       显示文本框
     * @param maxCount 上限
     */
    private void addCount(ImageView ivR, ImageView ivA, EditText et, int maxCount) {
        if (maxCount > 0) {
            int count = Integer.valueOf(et.getText().toString());
            if (count + 1 >= maxCount) {
                ivA.setEnabled(false);
                ivA.setImageResource(R.drawable.order_add_btn_noclick);
            } else {
                ivA.setEnabled(true);
                ivA.setImageResource(R.drawable.order_add_btn_click);
            }
            if (count == maxCount) {
                et.setText(String.valueOf(count));
                ivA.setEnabled(false);
                if (count == 1) {
                    ivR.setImageResource(R.drawable.order_reduce_btn_noclick);
                } else {
                    ivR.setImageResource(R.drawable.order_reduce_btn_click);
                }
                return;
            } else {
                et.setText(String.valueOf(count + 1));
                ivR.setImageResource(R.drawable.order_reduce_btn_click);
            }
        }
        // 将光标置到数字后
        Selection.setSelection(et_room_count.getText(), et_room_count.length());
    }

    /**
     * 减少数量
     *
     * @param ivR 减少按钮
     * @param ivA 增加按钮
     * @param et  显示文本框
     */
    private void reduceCount(ImageView ivR, ImageView ivA, EditText et) {
        ivA.setEnabled(true);
        ivA.setImageResource(R.drawable.order_add_btn_click);
        int count = Integer.valueOf(et.getText().toString());
        count--;
        if (count > 1) {
            ivR.setImageResource(R.drawable.order_reduce_btn_click);
            et.setText(String.valueOf(count));
        } else {
            ivR.setImageResource(R.drawable.order_reduce_btn_noclick);
            et.setText("1");
        }

        // 将光标置到数字后
        Selection.setSelection(et_room_count.getText(), et_room_count.length());
    }

    @Override
    public void onBackPressed() {
        SharedPreferencesUtils.saveInt(this,
                "first_into_reservationTwoAct", 1);
        clearDateLittle();
        super.onBackPressed();
    }

    /**
     * 根据状态携带数据跳转页面
     *
     * @param what 页面替代符
     */
    private void showReservationActivity(int what) {
        Intent intent = null;
        ReservationDate reservationDate = new ReservationDate();
        switch (what) {
            case DATE:
                intent = new Intent(this, ReservationDateActivity.class);
//                intent.putExtra("TYPE", DATE);
                if (null != selectedDates && selectedDates.size() > 0) {
                    reservationDate.setDate(selectedDates);
                    intent.putExtra("reservationHaveDate", reservationDate);
                } else if (null != info) {
                    intent.putExtra("info", info);
                }
                break;
            case WEEK:
                intent = new Intent(this, ReservationWeekActivity.class);
//                intent.putExtra("TYPE", WEEK);
                if (null != selectedDates
                        && selectedDates.size() > 0) {
                    reservationDate.setDate(selectedDates);
                    intent.putExtra("reservationHaveDate", reservationDate);
                } else if (null != info) {
                    intent.putExtra("info", info);
                } else {
                    Calendar ca = Calendar.getInstance();
                    ca.add(Calendar.DATE, 1);
                    List<Date> ls = new ArrayList<>();
                    ls.add(ca.getTime());
                    ca.setTime(new Date());
                    ca.add(Calendar.DATE, 2);
                    ls.add(ca.getTime());
                    reservationDate.setDate(ls);
                    intent.putExtra("reservationHaveDate", reservationDate);
                }
                if (null != reservationWeekList) {
                    intent.putIntegerArrayListExtra("reservationHaveWeek", reservationWeekList);
                }
                break;
            case TIME:
                intent = new Intent(this, ReservationTimeActivity.class);
//                intent.putExtra("TYPE", TIME);
                intent.putExtra("room_start_time", room_start_time);
                intent.putExtra("room_end_time", room_end_time);
                intent.putExtra("start_time", room_start_time);
                intent.putExtra("end_time", room_end_time);
                if (null != reservationTime) {
                    intent.putExtra("reservationHaveTime", reservationTime);
                }
                break;
            case DEVICE:
                intent = new Intent(this, ReservationDeviceActivity.class);
//                intent.putExtra("TYPE", DEVICE);
                if (null != reservationBean) {
                    intent.putExtra("reservationBean", reservationBean);
                }
                if (null != reservationHaveBean) {
                    intent.putExtra("reservationHadBean", reservationHaveBean);
                }
                if (null != info) {
                    intent.putExtra("info", info);
                }
                break;
            case DATE_LITTLE:
                if (null == rooms) {
                    UIUtils.showToastSafe(getString(R.string.toast_network_is_a_little_slow));
                    return;
                }
                intent = new Intent(this, ReservationDateLittleActivity.class);
//                intent.putExtra("TYPE", DATE_LITTLE);
                if (null != selectedDates && selectedDates.size() > 0) {
                    reservationDate.setDate(selectedDates);
                    intent.putExtra("reservationHaveDate", reservationDate);
                } else if (null != info) {
                    intent.putExtra("info", info);
                }
                intent.putExtra("hasWeek", tv_week.getText().toString());
                intent.putExtra("room_name", room_name);
                intent.putExtra("time", tv_time.getText().toString());
                intent.putExtra("room_start_time", room_start_time);
                intent.putExtra("room_end_time", room_end_time);
                if ("".equals(et_room_count.getText().toString())) {
                    UIUtils.showToastSafe(R.string.toast_select_one_classroom_at_least);
                    return;
                }
                intent.putExtra("room_count", Integer.valueOf(et_room_count.getText().toString()));
                intent.putExtra("maxCount", rooms.size());
                if (null != blueHaveDate) {
                    intent.putExtra("blueHaveDates", blueHaveDate);
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
     * 处理选择的日期时间
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            switch (resultCode) {
                case DATE:
                    ReservationDate reservationDate = (ReservationDate)
                            data.getSerializableExtra("reservationDate");
                    if (null != reservationDate) {
                        selectedDates = reservationDate.getDate();
                        if (null != selectedDates && selectedDates.size() > 0) {
                            showDateContent();
                            clearDateLittle();
                            info = null;
                        }
                    }

                    break;
                case WEEK:
                    reservationWeekList = data.getIntegerArrayListExtra("reservationWeek");
                    if (null != reservationWeekList && reservationWeekList.size() > 0) {
                        showWeekContent(reservationWeekList);
                        clearDateLittle();
                        info = null;
                    }
                    break;
                case TIME:
                    reservationTime = data.getStringExtra("reservationTime");
                    tv_time.setText(reservationTime);
                    clearDateLittle();
                    info = null;
                    break;
                case DEVICE:
                    reservationHaveBean = (ReservationBean) data.getSerializableExtra("reservationHaveBean");
                    showDeviceContent();
                    info = null;
                    break;
                case DATE_LITTLE:
                    blueHaveDate = (ReservationDate)
                            data.getSerializableExtra("blueHaveDate");
                    if (null != blueHaveDate) {
                        blueDateLists = blueHaveDate.getbDate();
                        colorMap = blueHaveDate.getMap();
                        if (null != colorMap && colorMap.size() > 0) {
                            info = null;
                        }
                    }

                default:
                    break;
            }
        }
    }

    /**
     * 显示用户选择的日期
     */
    private void showDateContent() {
        ca.setTime(selectedDates.get(0));
        String start_date = getDateString(ca);
        ca.setTime(selectedDates.get(selectedDates.size() - 1));
        String end_date = getDateString(ca);
        tv_date.setText(
                String.format(
                        getString(R.string.to_symbol),
                        start_date,
                        end_date
                ));
        StringBuilder sb_week = new StringBuilder();
        if (selectedDates.size() >= 7) {
            sb_week.append("周一 周二 周三 周四 周五 周六 周日");
        } else if (selectedDates.size() < 7) {
            for (int i = 0; i < selectedDates.size(); i++) {
                if (i == selectedDates.size()) {
                    sb_week.append(getNewWeek((selectedDates.get(i).getDay())));
                }
                sb_week.append(getNewWeek((selectedDates.get(i).getDay()))).append(" ");
            }
        }
        tv_week.setText(sb_week.toString());
        if (null != reservationWeekList) {
            reservationWeekList.clear();
        }
        if (null != info) {
            info = null;
        }
        for (int i = 0; i < selectedDates.size(); i++) {
            Date date = selectedDates.get(i);
            ca.setTime(date);
            String title = getTitleString(ca);
            SharedPreferencesUtils.saveString(this, title, "");
        }

    }

    /**
     * 清空用户操作
     */
    private void clearDateLittle() {
        if (null != blueDateLists && blueDateLists.size() > 0) {

            for (Date date : blueDateLists) {
                ca.setTime(date);
                String title = getTitleString(ca);
                SharedPreferencesUtils.saveString(this, title, "");
            }
            blueDateLists.clear();
            blueHaveDate = null;
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
            if (i == list.size()) {
                sbWeek.append(getNewWeek(list.get(i)));
            } else {
                sbWeek.append(getNewWeek(list.get(i))).append(" ");
            }
        }
        tv_week.setText(sbWeek.toString());
    }

    /**
     * 显示用户选择的设备
     */
    private void showDeviceContent() {
        int count = 0;
        haveDevice = reservationHaveBean.getDevice();
        if (null != haveDevice && haveDevice.size() > 0) {
            StringBuilder sbDevice = new StringBuilder();
            for (int i = 0; i < haveDevice.size(); i++) {
                if (haveDevice.get(i).getCount() == 0) {
                    count++;
                } else {
                    if (i == haveDevice.size()) {
                        sbDevice.append(haveDevice.get(i).getName()).append("X").append(haveDevice.get(i).getCount());
                    } else {
                        sbDevice.append(haveDevice.get(i).getName()).append("X").append(haveDevice.get(i).getCount()).append(" ");
                    }
                }
            }
            if (count != haveDevice.size()) {
                tv_device.setText(sbDevice.toString());
            } else {
                tv_device.setText(getString(R.string.reservation_device_title));
            }
        }
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
     * 立即支付
     */
    private void payRightNow() {

        if (null == uid) {
            // 如果用户没有登录，跳转到登录页面
            Intent intentLogin = new Intent(this, LoginActivity.class);
            startActivity(intentLogin);
        } else {
//            tv_reservation.setEnabled(false);
            String[] dates = tv_date.getText().toString().split(" ~ ");
            quest_start_date = dates[0].replace("年", "-").replace("月", "-").replace("日", "");
            quest_end_date = dates[1].replace("年", "-").replace("月", "-").replace("日", "");
            String time = tv_time.getText().toString();
            request_begin_time = time.split(" ~ ")[0].replace(":", "");
            request_end_time = time.split(" ~ ")[1].replace(":", "");
            String room_count = et_room_count.getText().toString();
            StringBuilder sb = new StringBuilder();
            if (null != rooms && rooms.size() > 0) {
                for (int i = 0; i < rooms.size(); i++) {
                    String id = rooms.get(i);
                    if (i == (rooms.size() - 1)) {
                        sb.append(id);
                    } else {
                        sb.append(id).append(",");
                    }
                }
            }
            room_id = sb.toString();
            repeat = getRepeatString();
            Gson gson = new Gson();
            jsonDevice = "";
            if (null != haveDevice && haveDevice.size() > 0) {
                Map<String, Integer> map = new HashMap<>();
                for (int i = 0; i < haveDevice.size(); i++) {
                    if (haveDevice.get(i).getCount() != 0) {
                        map.put(haveDevice.get(i).getId(), haveDevice.get(i).getCount());
                    }
                }
                if (map.size() != 0) {
                    jsonDevice = gson.toJson(map);
                }
            }
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
            params.addBodyParameter("room_count", room_count);
            if (null != jsonDevice && !"".equals(jsonDevice)) {
                params.addBodyParameter("device_str", jsonDevice);
            }
            List<RoomInfo> roomInfos = new ArrayList<>();
            roomInfoJsonStr = "";

            if (null != info) {
                colorMap = new HashMap<>();
                Set<Date> mSets = getNewDeadDates(info.getStart_date(), info.getEnd_date());
                List<Date> mDates = new ArrayList<>();
                mDates.clear();
                mDates.addAll(mSets);
                Collections.sort(mDates);
                List<Date> mInfoDates = new ArrayList<>();
                List<Date> weekDates = new ArrayList<>();
                String hasWeek = tv_week.getText().toString();
                String[] weeks = hasWeek.split(" ");
                int counts = weeks.length;
                if (counts < 7) {
                    for (String week : weeks) {
                        int intWeek = formatWeek(week);
                        for (Date date : mDates) {
                            ca.setTime(date);
                            int currentCaWeek = ca.get(Calendar.DAY_OF_WEEK);
                            if (currentCaWeek == intWeek) {
                                weekDates.add(date);
                            }
                        }
                        /*for (int j = 0; j < mDates.size(); j++) {
                            ca.setTime(mDates.get(j));
                            if (ca.get(Calendar.DAY_OF_WEEK) == intWeek) {
                                weekDates.add(mDates.get(j));
                            }
                        }*/
                    }
                    mDates.clear();
                    mDates.addAll(weekDates);
                    mInfoDates.clear();
                    mInfoDates.addAll(mDates);
                }
                RoomAdjustEntity infoData = info.getRoom_adjust();
                List<AdjustmentData> add_date = infoData.getAdd_date();
                List<AdjustmentData> cancel_date = infoData.getCancel_date();
                if (null != add_date && add_date.size() > 0) {

                    for (AdjustmentData addInfo : add_date) {
                        Date addDate = null;
                        try {
                            addDate = format.parse(addInfo.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (!mDates.contains(addDate)) {
                            mDates.add(addDate);
                        }
                    }
                }
                if (null != cancel_date && cancel_date.size() > 0) {

                    for (AdjustmentData cancelInfo : cancel_date) {
                        Date cancelDate = null;
                        try {
                            cancelDate = format.parse(cancelInfo.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (mDates.contains(cancelDate) && room_count.equals(cancelInfo.getRoom_count())) {
                            mDates.remove(cancelDate);
                        }
                    }
                }
                List<DayTimeData> bornLists = new ArrayList<>();
                for (int i = 0; i < Integer.valueOf(room_count); i++) {
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
                            Date cancelDate = null;
                            try {
                                cancelDate = format.parse(cancelInfo.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (null != cancelDate && mDates.get(i).toString().equals(cancelDate.toString())) {
                                int cancelCount = Integer.valueOf(cancelInfo.getRoom_count());
                                for (int j = 0; j < (bornLists.size() - cancelCount); j++) {
                                    lastDayTimeDatas.add(bornLists.get(j));
                                }
                            }
                        }
                        ca.setTime(mDates.get(i));
                        String d_title = ca.get(Calendar.YEAR) + "-" +
                                (String.valueOf(ca.get(Calendar.MONTH) + 1).length() == 1 ?
                                        ("0" + String.valueOf(ca.get(Calendar.MONTH) + 1)) : String.valueOf(ca.get(Calendar.MONTH) + 1))
                                + "-" + ca.get(Calendar.DAY_OF_MONTH);
                        if (lastDayTimeDatas.size() > 0) {
                            colorMap.put(d_title, lastDayTimeDatas);
                        } else {
                            colorMap.put(d_title, bornLists);
                        }
                    }
                }
                if (null != add_date && add_date.size() > 0) {
                    for (int i = 0; i < mDates.size(); i++) {
                        ArrayList<AdjustmentData> sameDays = new ArrayList<>();
                        sameDays.clear();
                        for (AdjustmentData addInfo : add_date) {
                            Date addDate = null;
                            try {
                                addDate = format.parse(addInfo.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (null != addDate && addDate.toString().equals(mDates.get(i).toString())) {
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
                            String d_title = ca.get(Calendar.YEAR) + "-" +
                                    (String.valueOf(ca.get(Calendar.MONTH) + 1).length() == 1 ?
                                            ("0" + String.valueOf(ca.get(Calendar.MONTH) + 1)) : String.valueOf(ca.get(Calendar.MONTH) + 1))
                                    + "-" + ca.get(Calendar.DAY_OF_MONTH);
                            if (mInfoDates.contains(mDates.get(i))) {
                                colorMap.put(d_title, bornLists);
                            } else if (noDayTimeDatas.size() > 0) {
                                colorMap.put(d_title, noDayTimeDatas);
                            }
                        }
                    }
                }
            }
            if (null != colorMap && colorMap.size() > 0) {
                int count = 0;
                for (Map.Entry<String, List<DayTimeData>> colorSet : colorMap.entrySet()) {
                    List<RoomInEntity> roomInEntities = new ArrayList<>();
                    RoomInfo roomInfo = new RoomInfo();
                    String title_date = colorSet.getKey();
                    roomInfo.setDate(title_date);
                    List<DayTimeData> timeLists = colorSet.getValue();
                    if (null != timeLists && timeLists.size() > 0) {
                        for (DayTimeData timeInfo : timeLists) {
                            RoomInEntity roomInEntity = new RoomInEntity();
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
                if (count == colorMap.size()) {
                    ProgressDialog.getInstance().dismiss();
                    UIUtils.showToastSafe(getString(R.string.toast_select_one_day_at_least));
                    return;
                }
                roomInfoJsonStr = gson.toJson(roomInfos);
                params.addBodyParameter("room_info", roomInfoJsonStr);
            }/*else if(null != colorMap && colorMap.size() == 0){
                UIUtils.showToastSafe("请至少选择一天");
            }*/

            Date date = null;
            try {
                date = format.parse(quest_start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!checkDate(date, time, tv_week.getText().toString())) {
                return;
            }
            httpUtils.send(HttpMethod.POST,
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
                            ReservationActivity.this,
                            OrderConfirmationActivity.class);
                    intent.putExtra("order2_id", json.getString("order2_id"));
                    startActivity(intent);
//                    clearDateLittle();
//                    tv_reservation.setEnabled(true);
                    break;
                case 40003:
                    // 参数不全
                    break;
                case 40044:
                    // 该用户已被加入黑名单
                    break;
                case 40050:
                    // 房间库存不足(跳转偏移界面)
                    ReservationFail mInfo = GsonTools
                            .changeGsonToBean(
                                    result,
                                    ReservationFail.class);
                    if (null != mInfo
                            && mInfo.getCode() == 40050
                            && (mInfo.getMsg())
                            .equals(getString(R.string.txt_lack_of_room_inventory))) {
                        int count = 0;
                        List<ArrEntity> arrEntities = mInfo.getData().getArr();
                        for (int i = 0; i < arrEntities.size(); i++) {
                            count += arrEntities.get(i).getMiss_count();
                        }
                        String s = count + " 间课室库存不足可进行个别日期调整或重新预订";
                        showRoomDialog(s, mInfo, null);
                    }

                    break;
                case 40051:
                    // 收费设备库存不足
                    DeviceTypeFull deviceTypeFull =
                            GsonTools.changeGsonToBean(result, DeviceTypeFull.class);
                    if (deviceTypeFull == null) {
                        return;
                    }
                    //================================
                    /*List<StockArrEntity> stock_arr = deviceTypeFull.getStock_arr();
                    HashSet<String> arrs = new HashSet<>();
                    for (int i = 0; i < stock_arr.size(); i++) {
                        arrs.add(stock_arr.get(i).getDate());
                    }
                    String s = arrs.size() + " 间课室的设备库存不足可进行个别日期调整或重新预订";*/
                    //================================
                    List<DeviceTypeFull.StockArrEntity> failDeviceData = deviceTypeFull.getStock_arr();
                    int deviceUseCount = 0;
                    int deviceFailCount = 0;
                    if (null != failDeviceData && failDeviceData.size() > 0) {
                        for (DeviceTypeFull.StockArrEntity arr : failDeviceData) {
                            deviceUseCount += arr.getStock_available();
                            deviceFailCount += Integer.valueOf(arr.getRoom_count());
                        }
                    }
                    String s = (deviceFailCount - deviceUseCount) + " 间课室的设备库存不足可进行个别日期调整或重新预订";
                    showRoomDialog(s, null, deviceTypeFull);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 星期请求参数转换
     *
     * @return 拼接字符串
     */
    private String getRepeatString() {
        StringBuilder sb_week = new StringBuilder();
        String[] weeks = tv_week.getText().toString().split(" ");
        for (int i = 0; i < weeks.length; i++) {
            if (i == weeks.length - 1) {
                sb_week.append(formatRequestWeek(weeks[i]));
            } else {
                sb_week.append(formatRequestWeek(weeks[i])).append(",");
            }
        }
        return sb_week.toString();
    }

    /**
     * 将星期字符串转换为数字字符串
     *
     * @param week 星期字符串
     * @return 星期对应数字字符串
     */
    private String formatNewWeek(String week) {
        String szWeek = null;
        switch (week) {
            case "1":
                szWeek = "周一";
                break;
            case "2":
                szWeek = "周二";
                break;
            case "3":
                szWeek = "周三";
                break;
            case "4":
                szWeek = "周四";
                break;
            case "5":
                szWeek = "周五";
                break;
            case "6":
                szWeek = "周六";
                break;
            case "7":
                szWeek = "周日";
                break;
        }
        return szWeek;
    }

    /**
     * 将星期字符串转换为数字字符串
     *
     * @param week 星期字符串
     * @return 星期对应数字字符串
     */
    private String formatRequestWeek(String week) {
        String szWeek = null;
        switch (week) {
            case "周一":
                szWeek = "1";
                break;
            case "周二":
                szWeek = "2";
                break;
            case "周三":
                szWeek = "3";
                break;
            case "周四":
                szWeek = "4";
                break;
            case "周五":
                szWeek = "5";
                break;
            case "周六":
                szWeek = "6";
                break;
            case "周日":
                szWeek = "7";
                break;
        }
        return szWeek;
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
        String title = getTitleString(ca);
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
            if (null != colorMap && colorMap.size() > 0) {
                if (colorMap.containsKey(title)) {
                    List<DayTimeData> dayTimeDatas = colorMap.get(title);
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

    /**
     * 展示时间冲突对话框
     */
    private void showTimeDialog() {
        UIUtils.showToastSafe(getString(R.string.toast_course_use_start_time));
        ProgressDialog.getInstance().dismiss();
    }

    /**
     * 展示预订失败对话框
     *
     * @param s          对话框标题
     * @param roomFail   课室不足数据
     * @param deviceFail 设备不足数据
     */
    private void showRoomDialog(String s, final ReservationFail roomFail, final DeviceTypeFull deviceFail) {
        CustomDialog customDialog = new CustomDialog(
                ReservationActivity.this,
                "个别日期调整",
                "重新预订",
                s);
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    jumpTitlePage(roomFail, deviceFail);
                }
            }
        });
    }

    /**
     * 跳往个别日期调整
     */
    private void jumpTitlePage(ReservationFail roomFail, DeviceTypeFull deviceFail) {
        Intent intent = new Intent(this, ReservationDateLittleActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("sid", sid);
        intent.putExtra("room_id", room_id);
        intent.putExtra("start_date", quest_start_date);
        intent.putExtra("end_date", quest_end_date);
        intent.putExtra("repeat", repeat);
        intent.putExtra("start_time", request_begin_time);
        intent.putExtra("end_time", request_end_time);
        intent.putExtra("room_start_time", room_start_time);
        intent.putExtra("room_end_time", room_end_time);
        intent.putExtra("device_str", jsonDevice);
        intent.putExtra("hasWeek", tv_week.getText().toString());
        intent.putExtra("room_name", room_name);
        intent.putExtra("time", tv_time.getText().toString());
        intent.putExtra("FAIL", "FAIL");
        intent.putExtra("room_count", Integer.valueOf(et_room_count.getText().toString()));
        intent.putExtra("maxCount", rooms.size());
        if (null != roomInfoJsonStr && !"".equals(roomInfoJsonStr)) {
            intent.putExtra("roomInfoJsonStr", roomInfoJsonStr);
        }
        ReservationDate reservationDate = new ReservationDate();
        refreshData(roomFail, deviceFail);
        if (null != selectedDates && selectedDates.size() > 0) {
            ArrayList<Date> copySelectedDates = new ArrayList<>();
            copySelectedDates.clear();
            copySelectedDates.addAll(selectedDates);
            List<Date> weekDates = new ArrayList<>();
            String hasWeek = tv_week.getText().toString();
            String[] weeks = hasWeek.split(" ");
            int counts = weeks.length;
            if (counts < 7) {
                for (String week : weeks) {
                    int intWeek = formatWeek(week);
                    for (int j = 0; j < copySelectedDates.size(); j++) {
                        ca.setTime(copySelectedDates.get(j));
                        int currentCaWeek = ca.get(Calendar.DAY_OF_WEEK);
                        if (currentCaWeek == intWeek) {
                            weekDates.add(copySelectedDates.get(j));
                        }
                    }
                }
                copySelectedDates.clear();
                copySelectedDates.addAll(weekDates);
            }
            if (null != blueHaveDate) {
                ArrayList<Date> mDates = new ArrayList<>();
                Set<Date> mSetDates = new HashSet<>();
                for (Date date : copySelectedDates) {
                    if (!orangeDates.contains(date)) {
                        mSetDates.add(date);
                    }
                }
                if (null != blueDateLists && blueDateLists.size() > 0) {
                    for (Date date : blueDateLists) {
                        if (!orangeDates.contains(date)) {
                            mSetDates.add(date);
                        }
                    }
                }
                mDates.clear();
                mDates.addAll(mSetDates);
                Collections.sort(mDates);
                reservationDate.setDate(mDates);
            } else {
                reservationDate.setDate(copySelectedDates);
            }
            SharedPreferencesUtils.saveBoolean(this, "oneFlag", true);
            intent.putExtra("reservationHaveDate", reservationDate);
        } else {
            ArrayList<Date> mDates = new ArrayList<>();
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
            try {
                mDates.add(format.parse(start_date));
                mDates.add(format.parse(end_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (null != blueHaveDate) {
                if (null != blueDateLists && blueDateLists.size() > 0) {
                    for (Date date : blueDateLists) {
                        if (!orangeDates.contains(date) && !mDates.contains(date)) {
                            mDates.add(date);
                        }
                    }
                }
                Collections.sort(mDates);
                reservationDate.setDate(mDates);
            }
            Collections.sort(mDates);
            reservationDate.setDate(mDates);
            intent.putExtra("reservationHaveDate", reservationDate);
        }

        if (null != roomFail) {
            intent.putExtra("roomFail", roomFail);
            intent.putExtra("DEVICE", jsonDevice);
        } else if (null != deviceFail) {
            intent.putExtra("deviceFail", deviceFail);
        }
        if (null != blueHaveDate) {
            intent.putExtra("blueHaveDates", blueHaveDate);
        }
        clearDateLittle();
        startActivity(intent);
    }

    /**
     * 刷新预订失败的数据
     *
     * @param roomFail   课室不足的数据
     * @param deviceFail 设备不足的数据
     */
    private void refreshData(ReservationFail roomFail, DeviceTypeFull deviceFail) {
        if (null != roomFail) {
            orangeDates.clear();
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
        }
        if (null != deviceFail) {
            orangeDates.clear();
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
        }
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
     * 是否显示登录
     */
    @Override
    protected void onResume() {
        super.onResume();
        uid = SharedPreferencesUtils.getString(this, "id", null);
        if (null != uid) {
            tv_reservation_login.setVisibility(View.GONE);
        } else {
            tv_reservation_login.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获得0000-00-00格式的日期
     *
     * @param calendar 日期对象
     * @return 需求格式的日期字符串
     */
    private String getTitleString(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "-" +
                (String.valueOf(calendar.get(Calendar.MONTH) + 1).length() == 1 ?
                        ("0" + String.valueOf(calendar.get(Calendar.MONTH) + 1)) : String.valueOf(calendar.get(Calendar.MONTH) + 1))
                + "-" + (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length() == 1 ?
                ("0" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))) : String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_reservation);
    }

}
