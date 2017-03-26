package com.yiju.ClassClockRoom.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.TimeWheelAdapter;
import com.yiju.ClassClockRoom.common.callback.ITroubleRunnable;
import com.yiju.ClassClockRoom.util.CommonUtil;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

public class TimeWheelDialog {
    private TextView trouble_cancel;
    private TextView trouble_ok;
    private Activity activity;
    private AlertDialog dialog;
    private WheelView wheelList_hour;
    private WheelView wheelList_min;
    private String[] hour_lists;
    private String[] min_lists;
//    private String[] min_one = new String[]{"00"};
    private String[] min_four = new String[]{"00", "15", "30", "45"};

    private String currentHour;
    private String currentMin;

    private ITroubleRunnable iTroubleRunnable;
    private TimeWheelAdapter hourTroubleWheelAdapter;
    private TimeWheelAdapter minTroubleWheelAdapter;
    private TextView tv_choose_begin_time;
    private String mTitle;
    private String mTime;
    private String mStartHour;
    private String mLastHour;
    private String mStartMinute;
    private Boolean mFlag;


    public void setITroubleRunnable(ITroubleRunnable iTroubleRunnable) {
        this.iTroubleRunnable = iTroubleRunnable;
    }

    public TimeWheelDialog(Activity activity,String startHour,String lastHour, String startMinute,
                           String title, String time, String[] hours, String[] minutes, boolean flag) {
        this.activity = activity;
        this.mTitle = title;
        this.mTime = time;
        this.hour_lists = hours;
        this.min_lists = minutes;
        this.mFlag = flag;
        this.mStartHour = startHour;
        this.mLastHour = lastHour;
        this.mStartMinute = startMinute;
        if(mFlag){
            if(!mTime.split(":")[0].equals(mLastHour)){
                StringBuilder sb = new StringBuilder();
                for (String minute : min_four) {
                    if (Integer.valueOf(minute) >= Integer.valueOf(mStartMinute)) {
                        sb.append(minute).append(" ");
                    }
                }
                if(sb.toString().trim().length() == 2){
                    min_lists = new String[]{sb.toString().trim()};
                }else {
                    min_lists = sb.toString().trim().split(" ");
                }
            }

        }
    }

    public void DrawLayout() {
        View v = LayoutInflater.from(activity).inflate(
                R.layout.time_wheel_layout, null);
        initView(v);
        initDate();
        initListeners();
        dialog = new AlertDialog.Builder(activity, R.style.dateDialogTheme)
                .create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM); // 设置dialog显示的位置
            window.setWindowAnimations(R.style.share_dialog_mystyle);
            window.setBackgroundDrawable(new BitmapDrawable());
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = CommonUtil.getScreenWidth();
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            dialog.setContentView(v, lp);
        }
    }

    private void initListeners() {
        trouble_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //确定
                hourTroubleWheelAdapter.notiFy();
                iTroubleRunnable.getTroubleTime(currentHour + ":" + currentMin);
                dialog.dismiss();
            }
        });
        trouble_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //取消
                dialog.dismiss();
            }
        });
    }

    private void initDate() {
        String[] hm = mTime.split(":");
        currentHour = hm[0];
        currentMin = hm[1];
        initHourWheel();
        initMinWheel();
        tv_choose_begin_time.setText(mTitle);
    }

    private void initHourWheel() {
        int currentIndex = 0;
        for (int i = 0; i < hour_lists.length; i++) {
            if (currentHour.equals(hour_lists[i])) {
                currentIndex = i;
            }
        }
        wheelList_hour.setWheelBackground(R.drawable.wheel_bg);
        wheelList_hour.setWheelForeground(R.drawable.wheel_val);
        wheelList_hour.setShadowColor(0xFFFFFFFF, 0x80FFFFFF, 0x00FFFFFF);
        hourTroubleWheelAdapter = new TimeWheelAdapter(
                activity, hour_lists);
        wheelList_hour.setViewAdapter(hourTroubleWheelAdapter);
        wheelList_hour.setCurrentItem(currentIndex);
        hourTroubleWheelAdapter.setCurrentIndex(currentIndex);
        wheelList_hour.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (hourTroubleWheelAdapter != null) {
                    hourTroubleWheelAdapter.setCurrentIndex(newValue);
                }
            }
        });
        wheelList_hour.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int index = wheel.getCurrentItem();
                currentHour = hour_lists[index];
                if (mFlag) {
                    if(currentHour.equals(mStartHour)){
                        StringBuilder sb = new StringBuilder();
                        for (String minute : min_four) {
                            if (Integer.valueOf(minute) >= Integer.valueOf(mStartMinute)) {
                                sb.append(minute).append(" ");
                            }
                        }
                        if(sb.toString().trim().length() == 2){
                            min_lists = new String[]{sb.toString().trim()};
                        }else {
                            min_lists = sb.toString().trim().split(" ");
                        }
                    }else if (currentHour.equals(mLastHour)) {
//                        min_lists = min_one;
//                        min_lists = new String[]{"00"};
                        StringBuilder sb = new StringBuilder();
                        for (String minute : min_four) {
                            if (Integer.valueOf(minute) <= Integer.valueOf(mStartMinute)) {
                                sb.append(minute).append(" ");
                            }
                        }
                        if(sb.toString().trim().length() == 2){
                            min_lists = new String[]{sb.toString().trim()};
                        }else {
                            min_lists = sb.toString().trim().split(" ");
                        }
                        if(!sb.toString().contains(currentMin)){
                            currentMin = "00";
                        }
                    } else {
                        min_lists = min_four;
                    }
                    initMinWheel();
                }
//					minTroubleWheelAdapter.notiFy();
            }
        });
    }


    private void initMinWheel() {
        int currentIndex = 0;
        for (int i = 0; i < min_lists.length; i++) {
            if (currentMin.equals(min_lists[i])) {
                currentIndex = i;
            }
        }
        wheelList_min.setWheelBackground(R.drawable.wheel_bg);
        wheelList_min.setWheelForeground(R.drawable.wheel_val);
        wheelList_min.setShadowColor(0xFFFFFFFF, 0x80FFFFFF, 0x00FFFFFF);
        minTroubleWheelAdapter = new TimeWheelAdapter(
                activity, min_lists);
        wheelList_min.setViewAdapter(minTroubleWheelAdapter);
        wheelList_min.setCurrentItem(currentIndex);
        minTroubleWheelAdapter.setCurrentIndex(currentIndex);
        wheelList_min.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (minTroubleWheelAdapter != null) {
                    minTroubleWheelAdapter.setCurrentIndex(newValue);
                }
            }
        });
        wheelList_min.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int index = wheel.getCurrentItem();
                currentMin = min_lists[index];
            }
        });
    }


    private void initView(View v) {
        wheelList_hour = (WheelView) v.findViewById(R.id.wv_trouble_hour);
        wheelList_min = (WheelView) v.findViewById(R.id.wv_trouble_min);

        trouble_cancel = (TextView) v.findViewById(R.id.trouble_cancel);
        trouble_ok = (TextView) v.findViewById(R.id.trouble_ok);

        tv_choose_begin_time = (TextView) v.findViewById(R.id.tv_choose_begin_time);
    }

    public boolean isShow() {
        return dialog.isShowing();
    }

}
