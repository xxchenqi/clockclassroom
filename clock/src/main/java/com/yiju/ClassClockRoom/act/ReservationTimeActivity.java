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
import com.yiju.ClassClockRoom.common.callback.ITroubleRunnable;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.dialog.TimeWheelDialog;

import java.util.ArrayList;

public class ReservationTimeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

    @ViewInject(R.id.rl_reservation_start_time)
    private RelativeLayout rl_reservation_start_time;

    @ViewInject(R.id.rl_reservation_end_time)
    private RelativeLayout rl_reservation_end_time;

    @ViewInject(R.id.tv_reservation_start_time)
    private TextView tv_reservation_start_time;

    @ViewInject(R.id.tv_reservation_end_time)
    private TextView tv_reservation_end_time;
    private String[] minutes = new String[]{"00", "15", "30", "45"};
    private int position;
    private String[] hours;
    private String[] hours_end;
    private String room_start_time;
    private String room_end_time;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
        rl_reservation_start_time.setOnClickListener(this);
        rl_reservation_end_time.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        head_title.setText(getString(R.string.reservation_time));
        head_right_text.setText(getString(R.string.label_save));
        Intent mReservationIntent = getIntent();
        if (null == mReservationIntent) {
            return;
        }
//        Order2 info = (Order2) mReservationIntent.getSerializableExtra("info");
        room_start_time = mReservationIntent.getStringExtra("room_start_time");
        room_end_time = mReservationIntent.getStringExtra("room_end_time");
        String start_time = mReservationIntent.getStringExtra("start_time");
        String end_time = mReservationIntent.getStringExtra("end_time");
        position = mReservationIntent.getIntExtra("position", Integer.MAX_VALUE);
        String reservationHaveTime = mReservationIntent.getStringExtra("reservationHaveTime");
        if (null != reservationHaveTime) {
            String[] times = reservationHaveTime.split(" ~ ");
            tv_reservation_start_time.setText(times[0]);
            tv_reservation_end_time.setText(times[1]);
        } else {
            tv_reservation_start_time.setText(StringUtils.changeTime(start_time));
            tv_reservation_end_time.setText(StringUtils.changeTime(end_time));
        }
        int startHour = Integer.valueOf(StringUtils.changeTime(room_start_time).split(":")[0]);
        int endHour = Integer.valueOf(StringUtils.changeTime(room_end_time).split(":")[0]);
        ArrayList<String> hoursList = new ArrayList<>();
        ArrayList<String> hoursEndList = new ArrayList<>();
        for (int i = 0; i < endHour - startHour; i++) {
            hoursList.add(String.valueOf(startHour + i));
        }
        hoursEndList.clear();
        hoursEndList.addAll(hoursList);
        hoursEndList.remove(0);
        hoursEndList.add(String.valueOf(endHour));
        hours = hoursList.toArray(new String[hoursList.size()]);
        hours_end = hoursEndList.toArray(new String[hoursEndList.size()]);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_reservation_time);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation_time;
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
            // 选择开始时间
            case R.id.rl_reservation_start_time:
                showBegin();
                break;
            // 选择结束时间
            case R.id.rl_reservation_end_time:
                showEnd();
                break;
            default:
                break;
        }
    }

    /**
     * 返回数据
     */
    private void backReservation() {
        int TIME = 2;
        Intent intent = new Intent();
        String reservationTime = String.format(
                getString(R.string.to_symbol),
                tv_reservation_start_time.getText().toString(),
                tv_reservation_end_time.getText().toString()
        );
        intent.putExtra("reservationTime", reservationTime);
        intent.putExtra("havePosition", position);
        setResult(TIME, intent);
        finish();
    }

    /**
     * 显示开始时间
     */
    private void showBegin() {

        String startTime = tv_reservation_start_time.getText().toString();
        String beginH = startTime.split(":")[0];
        String beginM = StringUtils.changeTime(room_start_time).split(":")[1];
        if (hours[hours.length - 1].equals(beginH)) {
//            minutes = new String[]{"00"};
            StringBuilder sb = new StringBuilder();
            for (String minute : minutes) {
                if (Integer.valueOf(minute) <= Integer.valueOf(beginM)) {
                    sb.append(minute).append(" ");
                }
            }
            if (sb.toString().trim().length() == 2) {
                minutes = new String[]{sb.toString().trim()};
            } else {
                minutes = sb.toString().trim().split(" ");
            }
        }
        TimeWheelDialog bDialog = new TimeWheelDialog(
                this, hours[0], hours[hours.length - 1], beginM, getString(R.string.select_start_time), startTime, hours,
                minutes, true);
        bDialog.setITroubleRunnable(new ITroubleRunnable() {
            @Override
            public void getTroubleTime(String time) {
                String[] hms = time.split(":");
                int h = Integer.valueOf(hms[0]);
//                int lastStartHour = Integer.valueOf(hours[hours.length-1]);
//                if (h < lastStartHour) {
                tv_reservation_start_time.setText(time.length() == 4 ? ("0" + time) : time);
                tv_reservation_end_time.setText(String.format(UIUtils.getString(R.string.colon),
                        String.valueOf(h + 1).length() == 1 ? ("0" + (h + 1)) : String.valueOf(h + 1), hms[1]));
                /*} else if (h == lastStartHour) {
//                    UIUtils.showToastSafe("开始时间最晚为20:00");
                    tv_reservation_start_time.setText(String.format(UIUtils.getString(R.string.colon_minute), h));
                    *//*tv_reservation_end_time.setText(String.format(UIUtils.getString(R.string.colon_minute),
                            String.valueOf(h+1).length() == 1? "0"+(h + 1) : (h+1)));*//*
                }*/
            }
        });
        bDialog.DrawLayout();
    }

    /**
     * 显示结束时间
     */
    private void showEnd() {
        String begin = tv_reservation_start_time.getText().toString();
        String beginH = begin.split(":")[0];
        String beginM = begin.split(":")[1];
        String[] newHours;
        if (beginH.equals(hours[hours.length - 1])) {
            newHours = new String[]{hours_end[hours_end.length - 1]};
        } else {
            StringBuilder sb = new StringBuilder();
            if (beginM.equals("00")) {
                for (String aHours_end : hours_end) {
                    if (Integer.valueOf(aHours_end) > Integer.valueOf(beginH)) {
                        sb.append(aHours_end).append(" ");
                    }
                }
            } else {
                for (String hour : hours) {
                    if (Integer.valueOf(hour) > Integer.valueOf(beginH)) {
                        sb.append(hour).append(" ");
                    }
                }
            }
            newHours = sb.toString().trim().split(" ");
        }
        String endTime = tv_reservation_end_time.getText().toString();
        TimeWheelDialog bDialog = new TimeWheelDialog(
                this, hours[0], hours[hours.length - 1], StringUtils.changeTime(room_start_time).split(":")[1],
                getString(R.string.select_end_time), endTime, newHours,
                new String[]{beginM}, false);
        bDialog.setITroubleRunnable(new ITroubleRunnable() {
            @Override
            public void getTroubleTime(String time) {
                tv_reservation_end_time.setText(time.length() == 4 ? ("0" + time) : time);
            }
        });
        bDialog.DrawLayout();
    }
}
