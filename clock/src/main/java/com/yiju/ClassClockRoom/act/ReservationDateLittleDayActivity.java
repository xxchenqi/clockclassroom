package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.DateLittleDayAdapter;
import com.yiju.ClassClockRoom.adapter.holder.DateLittleDayHolder;
import com.yiju.ClassClockRoom.bean.DayTimeAllData;
import com.yiju.ClassClockRoom.bean.DayTimeData;
import com.yiju.ClassClockRoom.bean.DeviceTypeFull;
import com.yiju.ClassClockRoom.bean.ReservationFail;
import com.yiju.ClassClockRoom.bean.ReservationFailTimeData;
import com.yiju.ClassClockRoom.bean.ReservationFailTimeData.DataAfterEntity;
import com.yiju.ClassClockRoom.bean.ReservationFailTimeData.DataPreEntity;
import com.yiju.ClassClockRoom.common.callback.ListItemClickTwoData;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class ReservationDateLittleDayActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

    @ViewInject(R.id.list_date_day)
    private ListView list_date_day;

    @ViewInject(R.id.tv_add_room)
    private TextView tv_add_room;
    //库存不足的页面需显示
    @ViewInject(R.id.tv_little_date_title)
    private TextView tv_little_date_title;

    @ViewInject(R.id.tv_little_date_advice)
    private TextView tv_little_date_advice;

    private List<DayTimeData> mList = new ArrayList<>();//数据源列表
    private List<DayTimeData> mDataList = new ArrayList<>();//改变后的数据列表
    private DateLittleDayAdapter dateLittleDayAdapter;
    private String room_name;
    private String time;
    private int maxCount;
    private String reservationTime;
    private int havePosition;
    private int room_count;
    private int roomFailCount = 0;
    private int roomMissCount = 0;
    private int deviceFailCount = 0;
    private int deviceUseCount = 0;
    private String title;
    private String sid;
    private String uid;
    private String room_id;
    private String start_time;
    private String end_time;
    private ReservationFail roomFail;
    private DeviceTypeFull deviceFail;
    private String room_start_time;
    private String room_end_time;
    private String chooseTime;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {

        head_back.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
        tv_add_room.setOnClickListener(this);
        tv_little_date_title.setOnClickListener(this);
        tv_little_date_advice.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        head_right_text.setText(getString(R.string.label_save));
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        String orange = intent.getStringExtra("ORANGE");
        title = intent.getStringExtra("title");
        DayTimeAllData dayTimeAllData = (DayTimeAllData) intent.getSerializableExtra("LISTDATA");
        DayTimeAllData hadChangeData = (DayTimeAllData) intent.getSerializableExtra("sendHaveList");
        if (null != dayTimeAllData) {
            if (null != dayTimeAllData.getList()) {
                mList = dayTimeAllData.getList();
                if (mList.size() > 0) {
                    mDataList.clear();
                    for (DayTimeData dayInfo : mList) {
                        DayTimeData newDayInfo = new DayTimeData();
                        newDayInfo.setDayTitle(dayInfo.getDayTitle());
                        newDayInfo.setFlag(dayInfo.isFlag());
                        newDayInfo.setTime(dayInfo.getTime());
                        mDataList.add(newDayInfo);
                    }
                }
            }

        }
        if (null != hadChangeData) {
            mList = hadChangeData.getList();
        }
        if (null != orange && orange.equals("ORANGE")) {
            tv_little_date_title.setVisibility(View.VISIBLE);
            tv_little_date_advice.setVisibility(View.VISIBLE);
            tv_add_room.setVisibility(View.GONE);
            tv_little_date_advice.setText("查看" + title.split("-")[1] + "月" + title.split("-")[2] + "日推荐预订时间");
        } else {
            tv_little_date_title.setVisibility(View.GONE);
            tv_little_date_advice.setVisibility(View.GONE);
            tv_add_room.setVisibility(View.VISIBLE);
            for (DayTimeData dayInfo : mList) {
                dayInfo.setFlag(false);
            }
        }
        room_name = intent.getStringExtra("room_name");
        sid = intent.getStringExtra("sid");
        uid = intent.getStringExtra("uid");
        room_id = intent.getStringExtra("room_id");
        start_time = intent.getStringExtra("start_time");
        end_time = intent.getStringExtra("end_time");
        room_start_time = intent.getStringExtra("room_start_time");
        room_end_time = intent.getStringExtra("room_end_time");
        time = intent.getStringExtra("time");
        room_count = intent.getIntExtra("room_count", 0);
        maxCount = intent.getIntExtra("maxCount", 0);
        head_title.setText(title.split("-")[1] + "月" + title.split("-")[2] + "日调整");
        showTimeList();
        roomFail = (ReservationFail) intent.
                getSerializableExtra("roomFail");
        deviceFail = (DeviceTypeFull) intent.
                getSerializableExtra("deviceFail");
        showFailList();
    }

    /**
     * 展示预订失败的日期对应的时间列表
     */
    private void showFailList() {
        if (null != roomFail) {
            tv_little_date_advice.setVisibility(View.VISIBLE);
            ReservationFail.DataEntity failData = roomFail.getData();
            if (null != failData) {
                List<ReservationFail.DataEntity.ArrEntity> failDates = failData.getArr();
                List<ReservationFail.DataEntity.ArrEntity> todayFailDates = new ArrayList<>();
                if (null != failDates && failDates.size() > 0) {
                    for (ReservationFail.DataEntity.ArrEntity arr : failDates) {
                        if (title.equals(arr.getDate().trim())) {
                            todayFailDates.add(arr);
                            roomMissCount += arr.getMiss_count();
                            roomFailCount += Integer.valueOf(arr.getRoom_count());
                        }
                    }
                    int roomUseCount = roomFailCount - roomMissCount;
                    for (ReservationFail.DataEntity.ArrEntity todayArr : todayFailDates) {
                        String start_time = StringUtils.changeTime(todayArr.getStart_time().trim());
                        String end_time = StringUtils.changeTime(todayArr.getEnd_time().trim());
                        if (roomMissCount != 0) {
                            String time = String.format(getString(R.string.to_symbol), start_time, end_time).replaceAll(" ", "");
                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(i).getTime().replaceAll(" ", "").equals(time)) {
                                    if (i < roomUseCount) {
                                        mList.get(i).setFlag(false);
                                    } else {
                                        mList.get(i).setFlag(true);
                                    }
                                }
                            }
                        }

                    }
                    if (null != dateLittleDayAdapter) {
                        dateLittleDayAdapter.notifyDataSetChanged();
                    }

                }
            }
            if (room_count == 1) {
                tv_little_date_title.setText(R.string.txt_suggest_change_date);
            } else if (room_count != 0) {
                tv_little_date_title.setText("当前阶段课室仅" + (roomFailCount - roomMissCount) + "间可用，" +
                        "请调整时间日期，或删除库存不足课室");
            }
        }

        if (null != deviceFail) {
            tv_little_date_advice.setVisibility(View.GONE);
            List<DeviceTypeFull.StockArrEntity> failDeviceData = deviceFail.getStock_arr();
            List<DeviceTypeFull.StockArrEntity> todayFailDeviceDates = new ArrayList<>();
            if (null != failDeviceData && failDeviceData.size() > 0) {
                for (DeviceTypeFull.StockArrEntity arr : failDeviceData) {
                    if (title.equals(arr.getDate().trim())) {
                        todayFailDeviceDates.add(arr);
                        deviceUseCount += arr.getStock_available();
                        deviceFailCount += Integer.valueOf(arr.getRoom_count());
                    }
                }
                for (DeviceTypeFull.StockArrEntity todayArr : todayFailDeviceDates) {
                    String start_time = StringUtils.changeTime(todayArr.getStart_time().trim());
                    String end_time = StringUtils.changeTime(todayArr.getEnd_time().trim());
                    String time = String.format(getString(R.string.to_symbol), start_time, end_time).replaceAll(" ", "");
                    if (deviceUseCount != deviceFailCount) {
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getTime().replaceAll(" ", "").equals(time)) {
                                if (i < deviceUseCount) {
                                    mList.get(i).setFlag(false);
                                } else {
                                    mList.get(i).setFlag(true);
                                }
                            }
                        }
                    }
                }
                if (null != dateLittleDayAdapter) {
                    dateLittleDayAdapter.notifyDataSetChanged();
                }

            }
            if (room_count == 1) {
                tv_little_date_title.setText(R.string.txt_suggest_change_date_for_device);
            } else if (room_count != 0) {
                tv_little_date_title.setText("当前时段设备仅" + deviceUseCount + "间课室可用，" +
                        "请调整时间日期，或删除设备库存不足课室");
            }
        }
    }

    /**
     * 展示课室
     */
    private void showTimeList() {
        if (null != mList) {
            if (null != dateLittleDayAdapter) {
                dateLittleDayAdapter.notifyDataSetChanged();
            } else {
                dateLittleDayAdapter = new DateLittleDayAdapter(this, mList, new ListItemClickTwoData() {
                    @Override
                    public void onClickItem(int position) {
                        jumpTimeChoosePage(position);
                    }

                    @Override
                    public void onDeleteItem(View v, int position) {
                        deleteCell(v, position);
                    }
                });
                list_date_day.setAdapter(dateLittleDayAdapter);
            }
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_reservation_date_little_day);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation_date_little_day;
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
            case R.id.tv_add_room:
                addRoom();
                break;
            case R.id.tv_little_date_title:
                addRoom();
                break;
            case R.id.tv_little_date_advice:
                jumpAdvicePage();
                break;
            default:
                break;
        }
    }

    /**
     * 跳往偏移时间页面
     */
    private void jumpAdvicePage() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "deviation_time");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("room_id", room_id);
        params.addBodyParameter("date", title);
        params.addBodyParameter("start_time", start_time);
        params.addBodyParameter("end_time", end_time);
        params.addBodyParameter("room_count", String.valueOf(room_count));


        httpUtils.send(HttpRequest.HttpMethod.POST,
                UrlUtils.SERVER_RESERVATION, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // 处理返回的数据
                        parseData(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }
                });
    }

    /**
     * 处理返回的偏移数据
     *
     * @param result json数据
     */
    private void parseData(String result) {
        if (null == result) {
            UIUtils.showToastSafe(title.split("-")[0] + "年" + title.split("-")[1] + "月" + title.split("-")[2] + "日暂无推荐时间");
            return;
        }
        ReservationFailTimeData reservationFailTimeData = GsonTools.changeGsonToBean(result, ReservationFailTimeData.class);
        if (null != reservationFailTimeData) {
            if (reservationFailTimeData.getCode() == 1) {
                DataPreEntity data_pre = reservationFailTimeData.getData_pre();
                DataAfterEntity data_after = reservationFailTimeData.getData_after();
                if (null != data_pre && null != data_after) {
                    if (data_pre.getFlag() == 0 && data_after.getFlag() == 0) {
                        UIUtils.showToastSafe(title.split("-")[0] + "年" + title.split("-")[1] + "月" + title.split("-")[2] + "日暂无推荐时间");
                    } else {
                        Intent intentTimeFail = new Intent(this, ReservationTimeFailActivity.class);
                        intentTimeFail.putExtra("TIMEFAIL", result);
                        intentTimeFail.putExtra("title", title);
                        startActivityForResult(intentTimeFail, 0);
                    }
                }
            }
        } else {
            UIUtils.showToastSafe(title.split("-")[0] + "年" + title.split("-")[1] + "月" + title.split("-")[2] + "日暂无推荐时间");
        }
    }

    /**
     * 增加课室
     */
    private void addRoom() {
        if (mList.size() < maxCount) {
            DayTimeData dayTimeData = new DayTimeData();
            dayTimeData.setDayTitle(room_name + " " + (mList.size() + 1));
            dayTimeData.setTime(time);
            dayTimeData.setFlag(false);
            mList.add(dayTimeData);
//            mList.add(room_name + "," + (mList.size() + 1) + "," + time);
            dateLittleDayAdapter.notifyDataSetChanged();
        }
        if (mList.size() == maxCount) {
            tv_add_room.setBackgroundColor(UIUtils.getColor(R.color.gray_dd));
        }
    }

    /**
     * 返回数据
     */
    private void backReservation() {
        int DATE_LITTLE_DAY = 5;

        Intent intent = new Intent();
        int count = 0;
        if (mDataList.size() != mList.size()) {
            DayTimeAllData changeDayTimeList = new DayTimeAllData();
            changeDayTimeList.setList(mList);
            intent.putExtra("changeDayTimeList", changeDayTimeList);
        } else {
            for (int i = 0; i < mList.size(); i++) {
                if ((mList.get(i).getTime()).equals(mDataList.get(i).getTime())) {
                    count++;
                }
            }
            if (count != mList.size()) {
                DayTimeAllData changeDayTimeList = new DayTimeAllData();
                changeDayTimeList.setList(mList);
                intent.putExtra("changeDayTimeList", changeDayTimeList);
            } else {
                SharedPreferencesUtils.saveString(this, title, "");
            }
        }
        if (mList.size() == 0) {
            intent.putExtra("EMPTY", "EMPTY");
        }
        intent.putExtra("haveTitle", title);
        setResult(DATE_LITTLE_DAY, intent);
        finish();
    }

    /**
     * 跳转时间选择页
     *
     * @param position 改变时间的位置
     */
    private void jumpTimeChoosePage(int position) {

        Intent intent = new Intent(this, ReservationTimeActivity.class);
//                intent.putExtra("TYPE", TIME);
        String time = mList.get(position).getTime();
        intent.putExtra("room_start_time", room_start_time);
        intent.putExtra("room_end_time", room_end_time);
        intent.putExtra("start_time", time.split(" ~ ")[0].replace(":", ""));
        intent.putExtra("end_time", time.split(" ~ ")[1].replace(":", ""));
        /*if (null != reservationTime) {
            intent.putExtra("reservationHaveTime", reservationTime);
        }*/
        intent.putExtra("position", position);
        if (null != reservationTime && havePosition == position) {
            intent.putExtra("reservationHaveTime", reservationTime);
        }
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int TIME_FAIL = 1;
        int TIME = 2;
        if (null != data) {
            if (resultCode == TIME) {
                reservationTime = data.getStringExtra("reservationTime");
                havePosition = data.getIntExtra("havePosition", Integer.MAX_VALUE);
                mList.get(havePosition).setTime(reservationTime);
                dateLittleDayAdapter.notifyDataSetChanged();
            } else if (resultCode == TIME_FAIL) {
                chooseTime = data.getStringExtra("chooseTime");
                if (chooseTime == null) {
                    return;
                }
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setTime(chooseTime);
                    mList.get(i).setFlag(false);
                }
                if (null != dateLittleDayAdapter) {
                    dateLittleDayAdapter.notifyDataSetChanged();
                }
            }
        }


    }

    /**
     * @param v     ListView中的Item
     * @param index 当前Item的索引
     */
    private void deleteCell(final View v, final int index) {
        AnimationListener al = new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mList.remove(index);
                for (int i = index; i < mList.size(); i++) {
                    DayTimeData dayTimeData = mList.get(i);
                    String[] nextItem = dayTimeData.getDayTitle().split(" ");
                    dayTimeData.setDayTitle(nextItem[0] + " " + (Integer.valueOf(nextItem[1]) - 1));
                    mList.get(i).setDayTitle(nextItem[0] + " " + (Integer.valueOf(nextItem[1]) - 1));
                    /*String nextItem = mList.get(i);
                    String[] nextItems = nextItem.split(",");
                    if(nextItems.length == 4 && nextItems[3].equals("t")){
                        mList.set(i, nextItems[0] + "," + (Integer.valueOf(nextItems[1]) - 1) + "," + nextItems[2]+",t");
                    }else {
                        mList.set(i, nextItems[0] + "," + (Integer.valueOf(nextItems[1]) - 1) + "," + nextItems[2]+",f");
                    }*/

                }

                DateLittleDayHolder vh = (DateLittleDayHolder) v.getTag();
                vh.needInflate = true;
                dateLittleDayAdapter.notifyDataSetChanged();
                tv_add_room.setBackgroundColor(UIUtils.getColor(R.color.app_theme_color));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };

        collapse(v, al);
    }

    /**
     * @param v  ListView的Item
     * @param al 设置好的Animation
     */
    private void collapse(final View v, AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al != null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(500);
        v.startAnimation(anim);
    }
}
