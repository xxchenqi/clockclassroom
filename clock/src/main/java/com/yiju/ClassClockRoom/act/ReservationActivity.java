package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
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
import com.yiju.ClassClockRoom.bean.DeviceTypeFull;
import com.yiju.ClassClockRoom.bean.DeviceTypeFull.StockArrEntity;
import com.yiju.ClassClockRoom.bean.ReservationBean;
import com.yiju.ClassClockRoom.bean.ReservationBean.ReservationDevice;
import com.yiju.ClassClockRoom.bean.ReservationFail;
import com.yiju.ClassClockRoom.bean.ReservationFail.DataEntity.ArrEntity;
import com.yiju.ClassClockRoom.bean.RoomInfo;
import com.yiju.ClassClockRoom.bean.RoomInfo.RoomInEntity;
import com.yiju.ClassClockRoom.bean.StoreDetailClassRoom;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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
 * <p>
 * 作      者  : Sandy
 * <p>
 * 日      期  : 2016/5/3/0005 09:55
 * <p>
 * 描      述  :
 * <p>
 * ============================================================
 */
public class ReservationActivity extends BaseActivity implements
        OnClickListener {

    final private int DATE = 0;
    final private int WEEK = 1;
    final private int TIME = 2;
    final private int DEVICE = 3;
    final private int DATE_LITTLE = 4;
    final private int STORE = 5;
    // 标题
    @ViewInject(R.id.tv_reservation_school)
    private TextView tv_reservation_school;

    @ViewInject(R.id.iv_reservation_call)
    private ImageView iv_reservation_call;

    // 背景图
    @ViewInject(R.id.iv_store_back)
    private ImageView iv_store_back;

    // 选择课室类型
    @ViewInject(R.id.rl_store)
    private RelativeLayout rl_store;

    // 课室类型
    @ViewInject(R.id.tv_room_type)
    private TextView tv_room_type;

    // 课室类型提示
    @ViewInject(R.id.tv_store_tip)
    private TextView tv_store_tip;

    // 价格
    @ViewInject(R.id.ll_price)
    private LinearLayout ll_price;

    // 平日价格
    @ViewInject(R.id.tv_day_price)
    private TextView tv_day_price;

    // 周末价格
    @ViewInject(R.id.tv_week_price)
    private TextView tv_week_price;

    @ViewInject(R.id.iv_back_reservation)
    private ImageView iv_back_reservation;

    // 预订单日
    @ViewInject(R.id.rl_sigle)
    private RelativeLayout rl_sigle;

    @ViewInject(R.id.iv_sigle)
    private ImageView iv_sigle;

    // 预订多日
    @ViewInject(R.id.rl_more)
    private RelativeLayout rl_more;

    @ViewInject(R.id.iv_more)
    private ImageView iv_more;

    // 选择日期
    @ViewInject(R.id.rl_date)
    private RelativeLayout rl_date;

    @ViewInject(R.id.tv_date_one)
    private TextView tv_date_one;

    @ViewInject(R.id.tv_date_two)
    private TextView tv_date_two;

    // 选择时间
    @ViewInject(R.id.rl_time)
    private RelativeLayout rl_time;

    @ViewInject(R.id.tv_time_one)
    private TextView tv_time_one;

    @ViewInject(R.id.tv_time_two)
    private TextView tv_time_two;

    // 选择星期
    @ViewInject(R.id.rl_week)
    private RelativeLayout rl_week;

    @ViewInject(R.id.tv_week)
    private TextView tv_week;

    //个别日期调整
    @ViewInject(R.id.rl_reservation_little)
    private RelativeLayout rl_reservation_little;

    @ViewInject(R.id.tv_reservation_little)
    private TextView tv_reservation_little;

    // 课室数量（不能小于1）
    @ViewInject(R.id.rl_room_count)
    private RelativeLayout rl_room_count;

    @ViewInject(R.id.rl_room_reduce)
    private RelativeLayout rl_room_reduce;

    @ViewInject(R.id.rl_room_add)
    private RelativeLayout rl_room_add;

    @ViewInject(R.id.iv_room_reduce)
    private ImageView iv_room_reduce;

    @ViewInject(R.id.iv_room_add)
    private ImageView iv_room_add;

    @ViewInject(R.id.et_room_count)
    private EditText et_room_count;

    // 可选设备
    @ViewInject(R.id.rl_device)
    private RelativeLayout rl_device;

    @ViewInject(R.id.tv_reservation_device)
    private TextView tv_reservation_device;

    // 立即支付
    @ViewInject(R.id.tv_reservation)
    private TextView tv_reservation;

    // 预订提示
    @ViewInject(R.id.ll_reservation)
    private RelativeLayout ll_reservation;
    //描述信息
    @ViewInject(R.id.tv_reservation_describe)
    private TextView tv_reservation_describe;


    // 房间数量上限
    private int maxRoomCount;
    private List<String> rooms;
    private List<Date> selectedDates;
    private List<Date> copySingleDates = new ArrayList<>();
    private List<Date> copyMoreDates = new ArrayList<>();
    private List<Date> copyMoreOriginalDates = new ArrayList<>();
    private String room_start_time;
    private String room_end_time;
    private ArrayList<Integer> reservationWeekList;
    private ReservationBean reservationBean;
    private boolean flag = true;
    private List<ReservationDevice> haveDevices;
    private String uid;
    private Calendar ca = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private boolean reservation_flag = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;
    private ArrayList<String> timeLists;
    private List<Date> haveDates;
    private String sid;
    private int index;
    private List<StoreDetailClassRoom> classRooms;
    private String tel;
    private List<ReservationDevice> devices;
    private int lineCount;
    private String instruction;
    private String confirm_type;
    private String school_type;
    private Boolean littleFlag = false;

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation2;
    }

    /**
     * 初始化页面
     */
    @Override
    public void initView() {
        iv_back_reservation.setOnClickListener(this);
        iv_reservation_call.setOnClickListener(this);
        rl_date.setOnClickListener(this);
        rl_week.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        rl_reservation_little.setOnClickListener(this);
        rl_device.setOnClickListener(this);
        tv_reservation.setOnClickListener(this);
        rl_room_reduce.setOnClickListener(this);
        rl_room_add.setOnClickListener(this);
        rl_room_reduce.setEnabled(true);
        rl_room_add.setEnabled(true);
        rl_sigle.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        rl_store.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        String sname = intent.getStringExtra("sname");
        String type_id = intent.getStringExtra("type_id");
        String store_pic = intent.getStringExtra("store_pic");
        index = intent.getIntExtra("index", Integer.MAX_VALUE);
        room_start_time = intent.getStringExtra("room_start_time");
        room_end_time = intent.getStringExtra("room_end_time");
        tel = intent.getStringExtra(ExtraControl.EXTRA_TEL);
        instruction = intent.getStringExtra("instruction");
        confirm_type = intent.getStringExtra("confirm_type");
        school_type = intent.getStringExtra(ExtraControl.EXTRA_SCHOOL_TYPE);
        if (StringUtils.isNotNullString(instruction)) {
            String str;
            if (!"0".equals(confirm_type)) {
                str = "课室预订订单将在订单确认通过后生效\r\n" + instruction;
            } else {
                str = instruction;
            }
            tv_reservation_describe.setText(str);
        } else {
            if (!"0".equals(confirm_type)) {
                tv_reservation_describe.setText(R.string.txt_reservation_describe);
            } else {
                ll_reservation.setVisibility(View.GONE);
            }
        }
        if (StringUtils.isNotNullString(sname)) {
            tv_reservation_school.setText(sname);
            tv_reservation_school.setTextColor(UIUtils.getColor(R.color.white));
        }
        if (StringUtils.isNotNullString(store_pic)) {
            Glide.with(this).load(store_pic).into(iv_store_back);
        }
        classRooms = (List<StoreDetailClassRoom>) intent.getSerializableExtra("class_room");
        if (StringUtils.isNullString(type_id)) {
            //未选择课室类型
            rl_room_count.setVisibility(View.GONE);
            rl_device.setVisibility(View.GONE);
            ll_price.setVisibility(View.GONE);
            tv_store_tip.setVisibility(View.VISIBLE);
        } else {
            //已选择课室类型
            setStore(type_id, classRooms.get(index));
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
                        rl_room_reduce.setEnabled(true);
                        rl_room_add.setEnabled(true);
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

    private void setStore(String type_id, StoreDetailClassRoom info) {
        getRoomDeviceCount(sid, type_id);
        rl_room_count.setVisibility(View.VISIBLE);
        rl_device.setVisibility(View.VISIBLE);
        ll_price.setVisibility(View.VISIBLE);
        tv_store_tip.setVisibility(View.GONE);
        tv_room_type.setText(info.getDesc());
        tv_day_price.setText(String.format(UIUtils.getString(R.string.txt_format_price), info.getPrice_weekday()));
        tv_week_price.setText(String.format(UIUtils.getString(R.string.txt_format_price), info.getPrice_weekend()));
        if (StringUtils.isNotNullString(info.getPic_small())) {
            Glide.with(this).load(info.getPic_small()).into(iv_store_back);
        }
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
                devices = reservationBean.getDevice();
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
            case R.id.rl_store:
                // 选择课室
                showReservationActivity(STORE);
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
            case R.id.rl_room_reduce:
                // 减掉
                reduceCount(iv_room_reduce, iv_room_add, et_room_count);
                break;
            case R.id.rl_room_add:
                // 增加
                addCount(iv_room_reduce, iv_room_add, et_room_count, maxRoomCount);
                break;
            case R.id.rl_device:
                // 选择设备
                showReservationActivity(DEVICE);
                break;
            case R.id.tv_reservation:
                if (rl_room_count.getVisibility() == View.GONE) {
                    UIUtils.showToastSafe(getString(R.string.toast_select_one_classroom_at_least));
                    return;
                }
                String date_one = tv_date_one.getText().toString();
                if (StringUtils.isNullString(date_one) || UIUtils.getString(R.string.reservation_choose).equals(date_one)) {
                    UIUtils.showToastSafe(getString(R.string.toast_show_select_date));
                    return;
                }
                String time_one = tv_time_one.getText().toString();
                if (StringUtils.isNullString(time_one) || UIUtils.getString(R.string.reservation_choose).equals(time_one)) {
                    UIUtils.showToastSafe(getString(R.string.toast_show_select_time));
                    return;
                }
                String week = tv_week.getText().toString();
                if (StringUtils.isNullString(week)) {
                    UIUtils.showToastSafe(getString(R.string.toast_show_select_week));
                    return;
                }
                boolean isLogin = SharedPreferencesUtils.getBoolean(UIUtils.getContext(),
                        getResources().getString(R.string.shared_isLogin), false);
                if (!isLogin) {
                    Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                String mobile = SharedPreferencesUtils.getString(this, UIUtils.getString(R.string.shared_mobile), null);
                if (StringUtils.isNullString(mobile)) {
                    // 弹出电话呼叫窗口
                    CustomDialog customDialog = new CustomDialog(
                            ReservationActivity.this,
                            UIUtils.getString(R.string.confirm),
                            UIUtils.getString(R.string.label_cancel),
                            "绑定手机号码的帐号方可进行预订或充值，前去绑定");
                    customDialog.setOnClickListener(new IOnClickListener() {
                        @Override
                        public void oncClick(boolean isOk) {
                            if (isOk) {
                                startActivity(new Intent(ReservationActivity.this, PersonalCenter_ChangeMobileActivity.class));
                            }
                        }
                    });

                } else {
                    ProgressDialog.getInstance().show();
                    payRightNow();
                }
                break;
            case R.id.iv_reservation_call:
                if (StringUtils.isNullString(tel)) {
                    return;
                }
                // 弹出电话呼叫窗口
                CustomDialog customDialog = new CustomDialog(
                        ReservationActivity.this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        tel);
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            if (!PermissionsChecker.checkPermission(PermissionsChecker.CALL_PHONE_PERMISSIONS)) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + tel));
                                startActivity(intent);
                            } else {
                                PermissionsChecker.requestPermissions(
                                        ReservationActivity.this,
                                        PermissionsChecker.CALL_PHONE_PERMISSIONS
                                );
                            }
                        }
                    }
                });

                break;
            case R.id.rl_sigle:
                rl_sigle.setEnabled(false);
                rl_more.setEnabled(true);
                iv_sigle.setVisibility(View.VISIBLE);
                iv_more.setVisibility(View.GONE);
                rl_week.setVisibility(View.GONE);
                rl_reservation_little.setVisibility(View.GONE);
                tv_date_two.setVisibility(View.GONE);
                reservation_flag = true;
                // 切换时保存数据
                if (copySingleDates.size() > 0) {
                    ca.setTime(copySingleDates.get(0));
                    tv_date_one.setText(getDateString(ca));
                } else {
                    tv_date_one.setText(UIUtils.getString(R.string.reservation_choose));
                }
                break;

            case R.id.rl_more:
                rl_sigle.setEnabled(true);
                rl_more.setEnabled(false);
                iv_sigle.setVisibility(View.GONE);
                iv_more.setVisibility(View.VISIBLE);
                rl_reservation_little.setVisibility(View.GONE);
                reservation_flag = false;
                // 切换时保存数据
                if (copyMoreDates.size() > 0) {
                    tv_date_two.setVisibility(View.VISIBLE);
                    rl_reservation_little.setVisibility(View.VISIBLE);
                    ca.setTime(copyMoreDates.get(0));
                    tv_date_one.setText(getDateString(ca));
                    ca.setTime(copyMoreDates.get(copyMoreDates.size() - 1));
                    tv_date_two.setText(getDateString(ca));
                    rl_week.setVisibility(View.VISIBLE);
                } else {
                    tv_date_one.setText(UIUtils.getString(R.string.reservation_choose));
                    tv_date_two.setVisibility(View.GONE);
                    rl_week.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
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
                rl_room_add.setEnabled(false);
                ivA.setImageResource(R.drawable.order_add_btn_noclick);
            } else {
                rl_room_add.setEnabled(true);
                ivA.setImageResource(R.drawable.order_add_btn_click);
            }
            if (count == maxCount) {
                et.setText(String.valueOf(count));
                rl_room_add.setEnabled(false);
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
        rl_room_add.setEnabled(true);
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
        super.onBackPressed();
    }

    /**
     * 根据状态携带数据跳转页面
     *
     * @param what 页面替代符
     */
    private void showReservationActivity(int what) {
        Intent intent = null;
        switch (what) {
            case STORE:
                intent = new Intent(this, ReservationStoreActivity.class);
                if (index != Integer.MAX_VALUE) {
                    intent.putExtra("index", index);
                }
                if (null != classRooms && classRooms.size() > 0) {
                    intent.putExtra("class_room", (Serializable) classRooms);
                }
                break;
            case DATE:
                intent = new Intent(this, ReservationDateActivity.class);
                if (reservation_flag) {
                    intent.putExtra("reservation_flag", true);
                    if (null != copySingleDates && copySingleDates.size() > 0) {
                        intent.putExtra("reservationHaveDate", (Serializable) copySingleDates);
                    }
                } else {
                    intent.putExtra("reservation_flag", false);
                    if (null != copyMoreOriginalDates && copyMoreOriginalDates.size() > 0) {
                        intent.putExtra("reservationHaveDate", (Serializable) copyMoreOriginalDates);
                    }
                }
                break;
            case WEEK:
                intent = new Intent(this, ReservationWeekActivity.class);
                if (null != copyMoreDates
                        && copyMoreDates.size() > 0) {
                    intent.putExtra("reservationHaveDate", (Serializable) copyMoreDates);
                }
                if (null != reservationWeekList && reservationWeekList.size() > 0) {
                    intent.putIntegerArrayListExtra("reservationHaveWeek", reservationWeekList);
                }
                break;
            case TIME:
                intent = new Intent(this, ReservationTimeActivity.class);
                intent.putExtra("room_start_time", room_start_time);
                intent.putExtra("room_end_time", room_end_time);
                if (null != timeLists && timeLists.size() > 0) {
                    intent.putExtra("reservationList", timeLists);
                }
                break;
            case DEVICE:
                intent = new Intent(this, ReservationDeviceActivity.class);
                if (null != devices && devices.size() > 0) {
                    intent.putExtra("reservationDevices", (Serializable) devices);
                }
                if (null != haveDevices && haveDevices.size() > 0) {
                    intent.putExtra("haveDevices", (Serializable) haveDevices);
                }
                break;
            case DATE_LITTLE:
                intent = new Intent(this, ReservationDateLittleActivity.class);
                if (null != copyMoreDates && copyMoreDates.size() > 0) {
                    intent.putExtra("selectedDates", (Serializable) copyMoreDates);
                }
                intent.putExtra("hasWeek", tv_week.getText().toString());
                if (null != haveDates && haveDates.size() > 0) {
                    intent.putExtra("haveDates", (Serializable) haveDates);
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
                case STORE:
                    index = data.getIntExtra("haveIndex", Integer.MAX_VALUE);
                    if (index != Integer.MAX_VALUE) {
                        setStore(String.valueOf(classRooms.get(index).getId()), classRooms.get(index));
                        if (StringUtils.isNotNullString(classRooms.get(index).getPic_small())) {
                            Glide.with(this).load(classRooms.get(index).getPic_small()).into(iv_store_back);
                        }
                    }

                    break;
                case DATE:
                    selectedDates = (ArrayList<Date>) data.getSerializableExtra("reservationDate");
                    if (null != selectedDates && selectedDates.size() > 0) {
                        if (reservation_flag) {
                            copySingleDates.clear();
                            copySingleDates.addAll(selectedDates);
                            showDateContent();
                        } else {
                            copyMoreDates.clear();
                            copyMoreDates.addAll(selectedDates);
                            copyMoreOriginalDates.clear();
                            copyMoreOriginalDates.addAll(selectedDates);
                            showDateContent();
                        }
                    }
                    break;
                case WEEK:
                    reservationWeekList = data.getIntegerArrayListExtra("reservationWeek");
                    if (null != reservationWeekList && reservationWeekList.size() > 0) {
                        showWeekContent(reservationWeekList);
                    }
                    break;
                case TIME:
                    timeLists = (ArrayList<String>) data.getSerializableExtra("reservationList");
                    if (null != timeLists && timeLists.size() > 0) {
//                        tv_time_one.setText(timeLists.get(0).split("!")[1]);
                        tv_time_one.setText(timeLists.get(0) + "等");
                        int count = 0;
                        for (String s : timeLists) {
//                            String[] times = s.split("!");
//                            String[] time = times[1].split(" ~ ");
                            String[] time = s.split(" ~ ");
                            count += Integer.valueOf(time[1].split(":")[0]) - Integer.valueOf(time[0].split(":")[0]);
                        }
                        tv_time_two.setVisibility(View.VISIBLE);
                        tv_time_two.setText("共 " + count + " 个小时");
                    } else {
                        tv_time_one.setText(UIUtils.getString(R.string.reservation_choose));
                        tv_time_two.setVisibility(View.GONE);
                    }

                    break;
                case DEVICE:
                    haveDevices = (List<ReservationDevice>) data.getSerializableExtra("haveDevices");
                    showDeviceContent();
                    break;
                case DATE_LITTLE:
                    haveDates = (List<Date>) data.getSerializableExtra("haveDates");
                    if (null != haveDates && haveDates.size() > 0) {
                        littleFlag = true;
                        int count = 0;
                        for (Date d : haveDates) {
                            if (!copyMoreDates.contains(d)) {
                                count++;
                            }
                        }
                        for (Date d : copyMoreDates) {
                            if (!haveDates.contains(d)) {
                                count++;
                            }
                        }
                        if (count == 0) {
                            tv_reservation_little.setText(UIUtils.getString(R.string.reservation_choose));
                        } else {
                            tv_reservation_little.setText("已调整 " + count + " 天");
                        }
                    } else {
                        littleFlag = false;
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

        rl_reservation_little.setVisibility(View.VISIBLE);
        ca.setTime(selectedDates.get(0));
        String start_date = getDateString(ca);
        ca.setTime(selectedDates.get(selectedDates.size() - 1));
        String end_date = getDateString(ca);
        if (reservation_flag) {
            tv_date_two.setVisibility(View.GONE);
            tv_date_one.setText(start_date);
            rl_reservation_little.setVisibility(View.GONE);
        } else {
            tv_date_two.setVisibility(View.VISIBLE);
            rl_reservation_little.setVisibility(View.VISIBLE);
            tv_date_one.setText(start_date);
            tv_date_two.setText(end_date);
            rl_week.setVisibility(View.VISIBLE);
        }

        StringBuilder sb_week = new StringBuilder();
        sb_week.append("每周");
        if (selectedDates.size() < 7) {
            ArrayList<Integer> weekInts = new ArrayList<>();
            for (int i = 0; i < selectedDates.size(); i++) {
                weekInts.add(selectedDates.get(i).getDay());
            }
            Collections.sort(weekInts);
            for (int i = 0; i < weekInts.size(); i++) {
                if (i == weekInts.size() - 1) {
                    sb_week.append(getNewWeek((weekInts.get(i))));
                } else {
                    sb_week.append(getNewWeek((weekInts.get(i)))).append("、");
                }
            }
        }
        tv_week.setText(sb_week.toString());
        if (null != reservationWeekList && reservationWeekList.size() > 0) {
            reservationWeekList.clear();
        }
        clearLittleData();
    }

    private void clearLittleData() {
        if (null != haveDates && haveDates.size() > 0) {
            haveDates.clear();
            tv_reservation_little.setText(UIUtils.getString(R.string.reservation_choose));
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
        sbWeek.append("每周");
        if (list.size() != 7) {
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    sbWeek.append(getNewWeek(list.get(i)));
                } else {
                    sbWeek.append(getNewWeek(list.get(i))).append("、");
                }
            }

        }
        tv_week.setText(sbWeek.toString());
        String weekStr = sbWeek.toString();
        copyMoreDates.clear();
        copyMoreDates.addAll(copyMoreOriginalDates);
        deleteWeek(copyMoreDates, weekStr);
        clearLittleData();
    }

    /**
     * 显示用户选择的设备
     */
    private void showDeviceContent() {
        int count = 0;
        if (null != haveDevices && haveDevices.size() > 0) {
            for (int i = 0; i < haveDevices.size(); i++) {
                if (haveDevices.get(i).getCount() != 0) {
                    count++;
                }
            }
        }
        if (count != 0) {
            tv_reservation_device.setText(haveDevices.get(0).getName() + "等 " + count + " 项");
        } else {
            tv_reservation_device.setText(UIUtils.getString(R.string.reservation_choose));
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
                week = "日";
                break;
            case 1:
                week = "一";
                break;
            case 2:
                week = "二";
                break;
            case 3:
                week = "三";
                break;
            case 4:
                week = "四";
                break;
            case 5:
                week = "五";
                break;
            case 6:
                week = "六";
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
            String quest_start_date = tv_date_one.getText().toString().replace("年", "-").replace("月", "-").replace("日", "");
            String quest_end_date;
            if (reservation_flag) {
                quest_end_date = quest_start_date;
            } else {
                quest_end_date = tv_date_two.getText().toString().replace("年", "-").replace("月", "-").replace("日", "");
            }
//            String time = tv_time_one.getText().toString();
            String request_begin_time = getMinTime();
            String request_end_time = getMaxTime();
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
            String room_id = sb.toString();
            String repeat = getRepeatString();
            if (repeat.equals("null")) {
                repeat = "";
            }
            Gson gson = new Gson();
            String jsonDevice = "";
            if (null != haveDevices && haveDevices.size() > 0) {
                Map<String, Integer> map = new HashMap<>();
                for (int i = 0; i < haveDevices.size(); i++) {
                    if (haveDevices.get(i).getCount() != 0) {
                        map.put(haveDevices.get(i).getId(), haveDevices.get(i).getCount());
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
            String roomInfoJsonStr;
            List<Date> mDates = new ArrayList<>();
            if (reservation_flag) {
                mDates.clear();
                mDates.addAll(copySingleDates);
            } else {
                mDates.clear();
                mDates.addAll(copyMoreDates);
                if (null != haveDates && haveDates.size() > 0) {
                    mDates.clear();
                    mDates.addAll(haveDates);
                }
            }
            String weekDays = tv_week.getText().toString();
            if (!littleFlag) {
                deleteWeek(mDates, weekDays);
            }
            for (Date d : mDates) {
                ca.setTime(d);
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.setDate(getDateString(ca).replace("年", "-").replace("月", "-").replace("日", ""));
                List<RoomInEntity> roomInEntities = new ArrayList<>();
                for (String t : timeLists) {
//                    String st = t.split("!")[1].split(" ~ ")[0].replace(":", "");
//                    String et = t.split("!")[1].split(" ~ ")[1].replace(":", "");
                    String st = t.split(" ~ ")[0].replace(":", "");
                    String et = t.split(" ~ ")[1].replace(":", "");
                    RoomInEntity roomInEntity = new RoomInEntity();
                    roomInEntity.setStart_time(st);
                    roomInEntity.setEnd_time(et);
                    roomInEntities.add(roomInEntity);
                }
                roomInfo.setRoom_in(roomInEntities);
                roomInfos.add(roomInfo);
            }
            roomInfoJsonStr = gson.toJson(roomInfos);
            params.addBodyParameter("room_info", roomInfoJsonStr);
            Date date = null;
            try {
                date = format.parse(quest_start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!checkDate(date, StringUtils.changeTime(getMinTime()), tv_week.getText().toString().substring(2))) {
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

    private void deleteWeek(List<Date> dates, String weekDays) {
        List<Date> weekDates = new ArrayList<>();
        if (!weekDays.equals("每周")) {
            String[] weeks = weekDays.substring(2, weekDays.length()).split("、");
            int counts = weeks.length;
            if (counts < 7) {
                for (String week : weeks) {
                    int intWeek = formatWeek(week);
                    for (int j = 0; j < dates.size(); j++) {
                        ca.setTime(dates.get(j));
                        if (ca.get(Calendar.DAY_OF_WEEK) == intWeek) {
                            weekDates.add(dates.get(j));
                        }
                    }
                }
                dates.clear();
                dates.addAll(weekDates);
            }
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
     * 获取时间最小值
     *
     * @return 最小时间字符串
     */
    private String getMinTime() {

        int minTime = 0;
        for (int i = 0; i < timeLists.size(); i++) {
//            int startTime = Integer.valueOf(timeLists.get(i).split("!")[1].split(" ~ ")[0].replace(":", ""));
            int startTime = Integer.valueOf(timeLists.get(i).split(" ~ ")[0].replace(":", ""));
            if (i == 0) {
                minTime = startTime;
            } else if (minTime > startTime) {
                minTime = startTime;
            }
        }
        return String.valueOf(minTime);
    }


    /**
     * 获取时间最大值
     *
     * @return 最大时间字符串
     */
    private String getMaxTime() {

        int maxTime = 0;
        for (int i = 0; i < timeLists.size(); i++) {
//            int endTime = Integer.valueOf(timeLists.get(i).split("!")[1].split(" ~ ")[1].replace(":", ""));
            int endTime = Integer.valueOf(timeLists.get(i).split(" ~ ")[1].replace(":", ""));
            if (i == 0) {
                maxTime = endTime;
            } else if (maxTime < endTime) {
                maxTime = endTime;
            }
        }
        return String.valueOf(maxTime);
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
                    intent.putExtra(ExtraControl.EXTRA_SCHOOL_TYPE, school_type);
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
                        Set<Date> noFullDates = getNoFullRoomDate(arrEntities);
                        if (!reservation_flag) {
                            showRoomDialog(s, noFullDates);
                        } else {
                            UIUtils.showToastSafe("课室库存不足，请选择其他时间");
                            noFullDates.clear();
                        }
                    }

                    break;
                case 40051:
                    // 收费设备库存不足
                    DeviceTypeFull deviceTypeFull =
                            GsonTools.changeGsonToBean(result, DeviceTypeFull.class);
                    if (deviceTypeFull == null) {
                        return;
                    }
                    List<StockArrEntity> failDeviceData = deviceTypeFull.getStock_arr();
                    int deviceUseCount = 0;
                    int deviceFailCount = 0;
                    if (null != failDeviceData && failDeviceData.size() > 0) {
                        for (StockArrEntity arr : failDeviceData) {
                            deviceUseCount += arr.getStock_available();
                            deviceFailCount += arr.getRoom_count();
                        }
                    }
                    String s = (deviceFailCount - deviceUseCount) + " 间课室的设备库存不足可进行个别日期调整或重新预订";
                    Set<Date> noFullDates = getNoFullDeviceDate(failDeviceData);
                    if (!reservation_flag) {
                        showRoomDialog(s, noFullDates);
                    } else {
                        UIUtils.showToastSafe("设备库存不足");
                        noFullDates.clear();
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得房间不足的日期集合
     *
     * @param list 所有日期
     * @return 不可选择日期
     */
    private Set<Date> getNoFullRoomDate(List<ArrEntity> list) {
        Date date;
        Set<Date> grayDates = new HashSet<>();
        for (ArrEntity info : list) {
            if (info.getMiss_count() != 0) {
                try {
                    date = format.parse(info.getDate());
                    grayDates.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return grayDates;
    }

    /**
     * 获得设备不足的日期集合
     *
     * @param list 所有日期
     * @return 不可选择日期
     */
    private Set<Date> getNoFullDeviceDate(List<StockArrEntity> list) {
        Date date;
        Set<Date> grayDates = new HashSet<>();
        for (StockArrEntity info : list) {
            if ((info.getRoom_count() - info.getStock_available()) != 0) {
                try {
                    date = format.parse(info.getDate());
                    grayDates.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return grayDates;
    }

    /**
     * 星期请求参数转换
     *
     * @return 拼接字符串
     */
    private String getRepeatString() {
        StringBuilder sb_week = new StringBuilder();
        String weekStr = tv_week.getText().toString();
        String[] weeks = weekStr.substring(2, weekStr.length()).split("、");
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
    private String formatRequestWeek(String week) {
        String szWeek = null;
        switch (week) {
            case "一":
                szWeek = "1";
                break;
            case "二":
                szWeek = "2";
                break;
            case "三":
                szWeek = "3";
                break;
            case "四":
                szWeek = "4";
                break;
            case "五":
                szWeek = "5";
                break;
            case "六":
                szWeek = "6";
                break;
            case "日":
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
            if (week.contains(getNewWeek(day.getDay()))) {
                if (currentTime < ct && (ct - currentTime) > 30 * 60 * 1000) {
                    return true;
                } else {
                    showTimeDialog();
                    return false;
                }
            } else if(week.equals("")){
                showTimeDialog();
                return false;
            } else{
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
     * @param s           对话框标题
     * @param noFullDates 课室不足数据
     */
    private void showRoomDialog(String s, final Set<Date> noFullDates) {
        CustomDialog customDialog = new CustomDialog(
                ReservationActivity.this,
                "个别日期调整",
                "重新预订",
                s);
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
//                    jumpTitlePage(roomFail, deviceFail);
                    Intent intent = new Intent(UIUtils.getContext(), ReservationDateLittleActivity.class);
                    if (null != haveDates && haveDates.size() > 0) {
                        intent.putExtra("haveDates", (Serializable) haveDates);
                    }
                    if (null != copyMoreDates && copyMoreDates.size() > 0) {
                        intent.putExtra("selectedDates", (Serializable) copyMoreDates);
                    }
                    intent.putExtra("grayDates", (Serializable) noFullDates);
                    intent.putExtra("FAIL", "FAIL");
                    startActivityForResult(intent, DATE_LITTLE);
                }
            }
        });
    }


    /**
     * 是否显示登录
     */
    @Override
    protected void onResume() {
        super.onResume();
        uid = SharedPreferencesUtils.getString(this, "id", null);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
            UIUtils.showToastSafe(getString(R.string.toast_permission_call_phone));
        }
    }
}
