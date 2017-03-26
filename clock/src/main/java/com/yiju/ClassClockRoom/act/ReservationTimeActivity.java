package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.TimeListAdapter;
import com.yiju.ClassClockRoom.common.callback.ITroubleRunnable;
import com.yiju.ClassClockRoom.common.callback.ListItemClickTwoData;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.dialog.TimeWheelDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.yiju.ClassClockRoom.R.string.reservation_start_time;

public class ReservationTimeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.tv_reservation_start_time)
    private TextView tv_reservation_start_time;

    @ViewInject(R.id.tv_reservation_end_time)
    private TextView tv_reservation_end_time;

    @ViewInject(R.id.tv_add_time)
    private TextView tv_add_time;

    @ViewInject(R.id.list_time)
    private ListView list_time;
    private String[] minutes = new String[]{"00", "15", "30", "45"};
    private String[] hours;
    private String[] hours_end;
    private String room_start_time;
    private String room_end_time;
    private List<String> mList = new ArrayList<>();
    private TimeListAdapter adapter;
    private int maxCount = 6;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        tv_reservation_start_time.setOnClickListener(this);
        tv_reservation_end_time.setOnClickListener(this);
        tv_add_time.setOnClickListener(this);
        tv_add_time.setEnabled(false);

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        head_title.setText(getString(R.string.reservation_choose_time));
        Intent mReservationIntent = getIntent();
        if (null == mReservationIntent) {
            return;
        }
        room_start_time = mReservationIntent.getStringExtra("room_start_time");
        room_end_time = mReservationIntent.getStringExtra("room_end_time");
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
        ArrayList<String> reservationHaveTimes = (ArrayList<String>) mReservationIntent.getSerializableExtra("reservationList");
        if (null != reservationHaveTimes && reservationHaveTimes.size() > 0) {
            mList.clear();
            mList.addAll(reservationHaveTimes);
        }

        String reservationHaveTime = mReservationIntent.getStringExtra("reservationHaveTime");
        if (null != reservationHaveTime) {
            String[] times = reservationHaveTime.split(" ~ ");
            tv_reservation_start_time.setText(times[0]);
            tv_reservation_end_time.setText(times[1]);
        } else {
            tv_reservation_start_time.setText(UIUtils.getString(reservation_start_time));
            tv_reservation_end_time.setText(UIUtils.getString(R.string.reservation_end_time));
        }

        adapter = new TimeListAdapter(this, mList, new ListItemClickTwoData() {
            @Override
            public void onClickItem(int position) {

            }

            @Override
            public void onDeleteItem(View v, int position) {
                deleteCell(v, position);
            }
        });
        list_time.setAdapter(adapter);


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
                onBackPressed();
                break;
            // 选择开始时间
            case R.id.tv_reservation_start_time:
                showBegin();
                break;
            // 选择结束时间
            case R.id.tv_reservation_end_time:
                if (UIUtils.getString(R.string.reservation_start_time).equals(tv_reservation_start_time.getText().toString())) {
                    UIUtils.showLongToastSafe("请选择开始时间");
                } else {
                    showEnd();
                }
                break;
            // 增加时间段
            case R.id.tv_add_time:
                if (mList.size() != maxCount) {
                    //是否有冲突,默认无冲突
                    boolean flag = false;
                    //选择的开始日期
                    String current_start_time = tv_reservation_start_time.getText().toString();
                    //选择的结束日期
                    String current_end_time = tv_reservation_end_time.getText().toString();
                    //选的日期和现有的日期遍历,存在交叉(冲突)时间flag 就变成false，跳出循环
                    for (int i = 0; i < mList.size(); i++) {
                        String[] split = mList.get(i).split("~");
                        Boolean overLaps = DateUtil.isOverLaps(current_start_time, current_end_time, split[0], split[1], "HH:mm");
                        if (overLaps) {
                            //有冲突
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        //有冲突
                        UIUtils.showLongToastSafe("使用时段不可交叉，请修改");
                        return;
                    }
                    String reservationTime = String.format(
                            getString(R.string.to_symbol),
                            tv_reservation_start_time.getText().toString(),
                            tv_reservation_end_time.getText().toString()
                    );
                    mList.add(reservationTime);
                    adapter.notifyDataSetChanged();
                    tv_reservation_start_time.setText(UIUtils.getString(reservation_start_time));
                    tv_reservation_end_time.setText(UIUtils.getString(R.string.reservation_end_time));
                    tv_reservation_start_time.setTextColor(UIUtils.getColor(R.color.color_gay_99));
                    tv_reservation_end_time.setTextColor(UIUtils.getColor(R.color.color_gay_99));
                    tv_add_time.setTextColor(UIUtils.getColor(R.color.color_gay_99));
                    tv_add_time.setEnabled(false);
                } else {
                    UIUtils.showToastSafe("已达到增加上限");
                }
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
        intent.putExtra("reservationList", (Serializable) mList);
        setResult(TIME, intent);
        finish();
    }

    /**
     * 显示开始时间
     */
    private void showBegin() {

        String startTime = tv_reservation_start_time.getText().toString();
        if (startTime.equals(UIUtils.getString(reservation_start_time))) {
            startTime = StringUtils.changeTime(room_start_time);
        }
//        String beginH = startTime.split(":")[0];
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
                tv_reservation_start_time.setText(time.length() == 4 ? ("0" + time) : time);
                tv_reservation_end_time.setText(String.format(UIUtils.getString(R.string.colon),
                        String.valueOf(h + 1).length() == 1 ? ("0" + (h + 1)) : String.valueOf(h + 1), hms[1]));
                tv_add_time.setEnabled(true);
                tv_add_time.setTextColor(UIUtils.getColor(R.color.color_green_30AA44));
                tv_reservation_start_time.setTextColor(UIUtils.getColor(R.color.color_black_33));
                tv_reservation_end_time.setTextColor(UIUtils.getColor(R.color.color_black_33));

            }
        });
        bDialog.DrawLayout();
    }

    /**
     * 显示结束时间
     */
    private void showEnd() {
        String begin = tv_reservation_start_time.getText().toString();
        if (begin.equals(UIUtils.getString(reservation_start_time))) {
            begin = StringUtils.changeTime(room_start_time);
        }
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
        if (endTime.equals(UIUtils.getString(R.string.reservation_end_time))) {
            endTime = StringUtils.changeTime(room_end_time);
        }
        TimeWheelDialog bDialog = new TimeWheelDialog(
                this, hours[0], hours[hours.length - 1], StringUtils.changeTime(room_start_time).split(":")[1],
                getString(R.string.select_end_time), endTime, newHours,
                new String[]{beginM}, false);
        bDialog.setITroubleRunnable(new ITroubleRunnable() {
            @Override
            public void getTroubleTime(String time) {
                tv_reservation_end_time.setTextColor(UIUtils.getColor(R.color.color_black_33));
                tv_reservation_end_time.setText(time.length() == 4 ? ("0" + time) : time);
                if (tv_reservation_start_time.getText().toString().equals(UIUtils.getString(R.string.reservation_choose))) {
                    tv_add_time.setEnabled(false);
                    tv_add_time.setTextColor(UIUtils.getColor(R.color.color_gay_99));
                } else {
                    tv_add_time.setEnabled(true);
                    tv_add_time.setTextColor(UIUtils.getColor(R.color.color_green_30AA44));
                    tv_reservation_start_time.setTextColor(UIUtils.getColor(R.color.color_black_33));
                    tv_reservation_end_time.setTextColor(UIUtils.getColor(R.color.color_black_33));
                }
            }
        });
        bDialog.DrawLayout();
    }

    /**
     * @param v     ListView中的Item
     * @param index 当前Item的索引
     */
    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mList.remove(index);
                adapter.notifyDataSetChanged();
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
    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                v.requestLayout();
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

    @Override
    public void onBackPressed() {
        backReservation();
        super.onBackPressed();
    }
}
