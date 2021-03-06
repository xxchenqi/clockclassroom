package com.yiju.ClassClockRoom.act.remind;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.RemindAccompanyBean;
import com.yiju.ClassClockRoom.control.AccompanyRemindControl;
import com.yiju.ClassClockRoom.control.FailCodeControl;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 陪读提醒时间设置
 *
 * @author geliping
 */
public class RemindAccompanyActivity extends BaseActivity implements
        OnClickListener {
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //    @ViewInject(R.id.cb_voideremind_open_switch)
//    private SwitchCompat open_switch;// 预约提醒
    @ViewInject(R.id.remind_accompany_list)
    private ListView remind_accompany_list;// 时间列表
    private RemindAccompanyAdapter accompanyAdapter;

    private ArrayList<RemindAccompanyBean> arrayList;
    private String remerber;// 提前多久提醒
    //    private String RemindAccompanyValue;// 在线陪读是否推送参数

    @Override
    public int setContentViewId() {
        return R.layout.activity_remind_accompany_layout;
    }

    @Override
    public void initView() {
        head_title.setText(UIUtils.getString(R.string.remind_accompany));
    }

    @Override
    public void initListener() {
        head_back_relative.setOnClickListener(this);
    }

    @Override
    public void initData() {
        remerber = SharedPreferencesUtils.getString(
                RemindAccompanyActivity.this,
                getResources().getString(R.string.shared_remerber),
                AccompanyRemindControl.lists.get(1));

        arrayList = new ArrayList<>();
        for (String str : AccompanyRemindControl.lists) {
            RemindAccompanyBean accompanyBean = new RemindAccompanyBean();
            accompanyBean.setName(AccompanyRemindControl.NumFormatStr(Integer
                    .valueOf(str)));
            accompanyBean.setTime(str);
            if (remerber.equals(str)) {
                accompanyBean.setIscheck(true);
            } else {
                accompanyBean.setIscheck(false);
            }
            arrayList.add(accompanyBean);
        }
        accompanyAdapter = new RemindAccompanyAdapter(
                RemindAccompanyActivity.this, arrayList,
                R.layout.item_remind_accompany_layout,
                new RemindAccompanyAdapter.IAccompanyRunnableSelectResult() {
                    @Override
                    public void selectResult() {
                        backActivity();
                    }
                }
        );
        remind_accompany_list.setAdapter(accompanyAdapter);

    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_remind_accompany);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
//                backActivity();
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_181");
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            backActivity();
            finish();
            return true;
        } else {
            return false;
        }
    }

    // 退出Activity
    private void backActivity() {
        if (remerber.equals(accompanyAdapter.getCurrentTime())) {
            finish();
        } else {
            // 修改提醒值
            postSetRemindValue(accompanyAdapter.getCurrentTime());
        }
    }

    /**
     * 设置提醒值
     */
    private void postSetRemindValue(final String time) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "modifyTime");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("time", time);
        params.addBodyParameter("url", UrlUtils.SERVER_USER_API);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());

        httpUtils.send(HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        finish();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            JSONObject jsonObject = new JSONObject(arg0.result);
                            int code = jsonObject.getInt("code");
                            if (code == 1) {
                                SharedPreferencesUtils.saveString(
                                        RemindAccompanyActivity.this,
                                        getResources().getString(
                                                R.string.shared_remerber), time);
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                FailCodeControl.checkCode(code);
                                UIUtils.showToastSafe(R.string.remind_set_fail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
}
