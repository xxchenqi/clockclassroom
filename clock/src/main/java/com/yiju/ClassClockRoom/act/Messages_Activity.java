package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.yiju.ClassClockRoom.bean.MessageBox;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.BadgeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Messages_Activity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.rl_order)
    private RelativeLayout rl_order;

    @ViewInject(R.id.iv_order)
    private ImageView iv_order;

    @ViewInject(R.id.tv_order_one)
    private TextView tv_order_one;

    @ViewInject(R.id.tv_order_time)
    private TextView tv_order_time;

    @ViewInject(R.id.rl_mech)
    private RelativeLayout rl_mech;

    @ViewInject(R.id.iv_mech)
    private ImageView iv_mech;

    @ViewInject(R.id.tv_mech_one)
    private TextView tv_mech_one;

    @ViewInject(R.id.tv_mech_time)
    private TextView tv_mech_time;

    @ViewInject(R.id.rl_peidu)
    private RelativeLayout rl_peidu;
    @ViewInject(R.id.iv_peidu)
    private ImageView iv_peidu;
    @ViewInject(R.id.tv_peidu_one)
    private TextView tv_peidu_one;
    @ViewInject(R.id.tv_peidu_time)
    private TextView tv_peidu_time;
    private BadgeView badgeView_order;
    private BadgeView badgeView_mech;
    private BadgeView badgeView_peidu;
    private MessageBox messageBox;
    private String uid;

    private final int TYPE_ORDER = 1;//订单消息
    private final int TYPE_MECH = 2;//系统提醒
    private final int TYPE_PEIDU = 3;//陪读提醒

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        rl_order.setOnClickListener(this);
        rl_mech.setOnClickListener(this);
        rl_peidu.setOnClickListener(this);
        badgeView_order = new BadgeView(this, iv_order);
        badgeView_mech = new BadgeView(this, iv_mech);
        badgeView_peidu = new BadgeView(this, iv_peidu);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        head_title.setText(getResources().getText(R.string.my_message));
        uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                "id", null);
        sendHttp(uid, "message_box");

    }

    /**
     * 请求数据
     *
     * @param uid    用户id
     * @param action 可变参数数组
     */
    private void sendHttp(String uid, String... action) {
        final int length = action.length;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action[0]);
        params.addBodyParameter("uid", uid);
        if (length == 1) {
            params.addBodyParameter("username", StringUtils.getUsername());
            params.addBodyParameter("password", StringUtils.getPassword());
            params.addBodyParameter("third_source", StringUtils.getThirdSource());
        } else if (length == 2) {
            params.addBodyParameter("big_type", action[1]);
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result, length);
                    }
                });
    }

    /**
     * 解析网络返回的数据
     *
     * @param result 服务器返回的结果集
     * @param length 根据sendHttp中参数action的长度进行不同的处理
     */
    private void processData(String result, int length) {
        switch (length) {
            case 1:
                messageBox = GsonTools.changeGsonToBean(result, MessageBox.class);
                if (null != messageBox) {
                    if (messageBox.getCode() == 1) {
                        List<MessageBox.MessageData> data = messageBox.getData();
                        ArrayList<MessageBox.MessageData> mOrders = new ArrayList<>();
                        ArrayList<MessageBox.MessageData> mMechs = new ArrayList<>();
                        ArrayList<MessageBox.MessageData> mPeiDus = new ArrayList<>();
                        if (null != data && data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                switch (data.get(i).getBig_type()) {
                                    case "" + TYPE_ORDER:
                                        mOrders.add(data.get(i));
                                        break;
                                    case "" + TYPE_MECH:
                                        mMechs.add(data.get(i));
                                        break;
                                    case "" + TYPE_PEIDU:
                                        mPeiDus.add(data.get(i));
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (mOrders.size() > 0) {
                                showMessage(tv_order_one, tv_order_time, mOrders, badgeView_order);
                            }
                            if (mMechs.size() > 0) {
                                showMessage(tv_mech_one, tv_mech_time, mMechs, badgeView_mech);
                            }
                            if (mPeiDus.size() > 0) {
                                showMessage(tv_peidu_one, tv_peidu_time, mPeiDus, badgeView_peidu);
                            }
                        }
                    }
                }
                break;
            case 2://已读
                break;
        }


    }

    /**
     * 消息展示
     *
     * @param tv        消息内容
     * @param time      时间
     * @param lists     消息数据集合
     * @param badgeView 右上角红色提醒
     */
    private void showMessage(TextView tv, TextView time, ArrayList<MessageBox.MessageData> lists,
                             BadgeView badgeView) {
        Calendar ca = Calendar.getInstance();
        Date today = ca.getTime();
        int count = 0;
        for (int i = 0; i < lists.size(); i++) {
            if ("0".equals(lists.get(i).getIs_read())) {
                count++;
            }
        }
        if (count == 0) {
            badgeView.hide();
        } else {
            if (count > 99) {
                badgeView.setText(getResources().getString(R.string.jiujiujia));
            } else {
                badgeView.setText(count + "");
            }
            badgeView.setTextSize(10f);
            badgeView.setBadgeMargin(1);
            badgeView.show();
        }
        if ("".equals(lists.get(0).getContent())) {
            tv.setText(R.string.no_message);
        } else {
            tv.setText(lists.get(0).getContent());
        }
        long O_time = Long.valueOf(lists.get(0).getCreate_time());
        Date date = new Date(O_time * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String format = formatter.format(date);
        if (date.getYear() == today.getYear()) {
            if (date.getDate() == today.getDate()) {
                // 今天
                String minutes;
                if (date.getMinutes() < 10) {
                    minutes = "0" + date.getMinutes();
                } else {
                    minutes = date.getMinutes() + "";
                }
                String s = String.format(getResources().getString(R.string.colon), date.getHours() + "",
                        minutes);
                time.setText(s);
            } else if (today.getDate() - date.getDate() == 1) {
                time.setText(getResources().getString(R.string.yestoday));
            } else {
                time.setText(format.substring(5));
            }
        } else {
            time.setText(format);
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_message);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_messages;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.rl_order://订单消息
                jumpDetial(TYPE_ORDER, badgeView_order);
                break;
            case R.id.rl_mech://系统提醒
                jumpDetial(TYPE_MECH, badgeView_mech);
                break;
            case R.id.rl_peidu://陪读提醒
                jumpDetial(TYPE_PEIDU, badgeView_peidu);
                break;
        }
    }

    /**
     * 根据类型跳往消息列表页面
     *
     * @param i         消息类型
     * @param badgeView 右上角红色提醒
     */
    private void jumpDetial(int i, BadgeView badgeView) {
        badgeView.hide();
        Intent intent = new Intent(this, MessageDetialActivity.class);
        if (null != messageBox) {
            intent.putExtra("big_type", i);
            intent.putExtra("messageBox", messageBox);
            sendHttp(uid, "message_read", "" + i);
            startActivity(intent);
        }
    }
}
