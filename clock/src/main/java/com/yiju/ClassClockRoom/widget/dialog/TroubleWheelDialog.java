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

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.TroubleWheelAdapter;
import com.yiju.ClassClockRoom.common.callback.ITroubleRunnable;
import com.yiju.ClassClockRoom.control.TroubleControl;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

public class TroubleWheelDialog {
    private TextView trouble_content;
    private TextView trouble_ok;
    private Activity activity;
    private AlertDialog dialog;
    private WheelView wheelList_hour;
    private WheelView wheelList_min;
    private WheelView wheelList_continue;
    private String[] hour_lists;
    private String[] min_lists;
    private String[] continue_lists;

    private String currentHour;
    private String currentMin;
    private String currentContinue;

    private String localTrouble_btime;
    private String localTrouble_etime;

    private ITroubleRunnable iTroubleRunnable;

    public void setITroubleRunnable(ITroubleRunnable iTroubleRunnable) {
        this.iTroubleRunnable = iTroubleRunnable;
    }

    public TroubleWheelDialog(Activity activity) {
        this.activity = activity;
    }

    public void DrawLayout() {
        View v = LayoutInflater.from(activity).inflate(
                R.layout.dialog_trouble_wheel_layout, null);
        initView(v);
        initdate();
        initListeners();
        dialog = new AlertDialog.Builder(activity, R.style.myDialogTheme)
                .create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM); // 设置dialog显示的位置
            window.setWindowAnimations(R.style.share_dialog_mystyle);
            window.setBackgroundDrawable(new BitmapDrawable());
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = CommonUtil.getScreenWidth();
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            dialog.setContentView(v, lp);
        }
    }

    private void initListeners() {
        trouble_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (localTrouble_btime.equals(currentHour + currentMin)
                        && localTrouble_etime.equals(getEndTime(currentHour,
                        currentContinue) + currentMin)) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    return;
                }
                postTrouble();
            }
        });
    }

    private void initdate() {
        TroubleControl.getInstance().initLocalTime();
        hour_lists = TroubleControl.getInstance().getHourLists();
        min_lists = TroubleControl.getInstance().getMinLists();
        continue_lists = TroubleControl.getInstance().getContinueLists();

        localTrouble_btime = TroubleControl.getInstance()
                .getLocal_trouble_btime();
        localTrouble_etime = TroubleControl.getInstance()
                .getLocal_trouble_etime();

        currentHour = TroubleControl.getInstance().getCurrentHour();
        currentMin = TroubleControl.getInstance().getCurrentMin();
        currentContinue = TroubleControl.getInstance().getCurrentContinue();

        trouble_content.setText(TroubleControl.getInstance().setContentVisible(
                currentHour, currentMin, currentContinue));

        initWheel(wheelList_hour, hour_lists);
        initWheel(wheelList_min, min_lists);
        initWheel(wheelList_continue, continue_lists);
    }

    private void initWheel(WheelView wheelView, String[] lists) {
        int currentIndex = 0;
        if (wheelView == wheelList_hour) {
            currentIndex = Integer.parseInt(currentHour);
        } else if (wheelView == wheelList_min) {
            currentIndex = Integer.parseInt(currentMin);
        } else if (wheelView == wheelList_continue) {
            currentIndex = Integer.parseInt(currentContinue);
        }
        wheelView.setWheelBackground(R.drawable.wheel_bg);
        wheelView.setWheelForeground(R.drawable.wheel_val);
        wheelView.setShadowColor(0xFFFFFFFF, 0x80FFFFFF, 0x00FFFFFF);

        final TroubleWheelAdapter troubleWheelAdapter = new TroubleWheelAdapter(
                activity, lists);
        if (wheelView == wheelList_continue) {
            troubleWheelAdapter.setEndStr("小时");
        }
        wheelView.setViewAdapter(troubleWheelAdapter);
        wheelView.setCurrentItem(currentIndex);
        troubleWheelAdapter.setCurrentIndex(currentIndex);
        wheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                troubleWheelAdapter.setCurrentIndex(newValue);
            }
        });
        wheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int index = wheel.getCurrentItem();
                if (wheel == wheelList_hour) {
                    currentHour = hour_lists[index];
                } else if (wheel == wheelList_min) {
                    currentMin = min_lists[index];
                } else if (wheel == wheelList_continue) {
                    currentContinue = continue_lists[index];
                }
                trouble_content.setText(TroubleControl.getInstance().setContentVisible(
                        currentHour, currentMin, currentContinue));
            }
        });
    }


    private void initView(View v) {
        wheelList_hour = (WheelView) v.findViewById(R.id.wv_trouble_hour);
        wheelList_min = (WheelView) v.findViewById(R.id.wv_trouble_min);
        wheelList_continue = (WheelView) v
                .findViewById(R.id.wv_trouble_continue);

        trouble_content = (TextView) v.findViewById(R.id.trouble_content);
        trouble_ok = (TextView) v.findViewById(R.id.trouble_ok);
    }

    public boolean isShow() {
        return dialog.isShowing();
    }

    /**
     * 计算间距后结束小时
     *
     * @param start    起始小时
     * @param interval 间隔时间
     * @return 结束小时
     */
    private String getEndTime(String start, String interval) {
        int startTime = Integer.parseInt(start);
        int intervalTime = Integer.parseInt(interval);
        if (startTime + intervalTime < 24) {
            return (startTime + intervalTime) + "";
        } else {
            return (startTime + intervalTime - 24) + "";
        }
    }

    private void postTrouble() {
        final String startTime = currentHour + currentMin;
        final String etime = getEndTime(currentHour, currentContinue)
                + currentMin;

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "set_trouble_time");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("trouble_btime", startTime);
        params.addBodyParameter("trouble_etime", etime);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.remind_set_fail);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(arg0.result);
                            if (jsonObject.getInt("code") == 1) {
                                SharedPreferencesUtils.saveString(
                                        activity,
                                        activity.getResources().getString(
                                                R.string.shared_trouble_btime),
                                        startTime);

                                SharedPreferencesUtils.saveString(
                                        activity,
                                        activity.getResources().getString(
                                                R.string.shared_trouble_etime),
                                        etime);
                                if (iTroubleRunnable != null) {
                                    iTroubleRunnable
                                            .getTroubleTime(TroubleControl.getInstance().setContentVisible(
                                                    currentHour, currentMin, currentContinue));
                                }
                                UIUtils.showToastSafe(R.string.remind_set_success);
                                dialog.dismiss();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        UIUtils.showToastSafe(R.string.remind_set_fail);
                    }
                });
    }
}
