package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.accompany.AccompanyReadStatusActivity;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.MessageAdapter;
import com.yiju.ClassClockRoom.bean.MessageDetialData;
import com.yiju.ClassClockRoom.control.FailCodeControl;
import com.yiju.ClassClockRoom.control.SchemeControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageDetialActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.tv_no_message)
    private TextView tv_no_message;


    @ViewInject(R.id.lv_message)
    private PullToRefreshListView lv_message;
    private MessageAdapter messageAdapter;
    private List<MessageDetialData.MessageInfo> mLists = new ArrayList<>();
    private int big_type;
    private final int TYPE_ORDER = 1;//订单消息
    private final int TYPE_MECH = 2;//系统提醒
    private final int TYPE_PEIDU = 3;//陪读提醒
    //下拉刷新标志，默认为下来刷新
    private boolean flag_down = true;
    private int limit = 0;
    private View footView;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        footView = View.inflate(this, R.layout.include_no_more, null);
        lv_message.setMode(PullToRefreshBase.Mode.BOTH);
        lv_message.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = true;
                limit = 0;
                getData();
                lv_message.setMode(PullToRefreshBase.Mode.BOTH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = false;
                limit += 5;

                getData();
            }
        });
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int type = Integer.valueOf(mLists.get(i - 1).getType());
                switch (type) {
                    case 1://待支付订单
                    case 7://退单申请(我的订单)
                    case 10://订单成立(已支付页面)
                    case 11://订单消费提醒
                    case 12://订单消费完毕
                    case 18://支付前确认通过
                    case 19://支付前确认不通过
                    case 20://支付后确认通过
                    case 21://支付后确认不通过
                    case 22://电子发票开票成功
                        Intent intentOrder;
                        if ("3".equals(mLists.get(i - 1).getOrder_type())) {
                            intentOrder = new Intent(UIUtils.getContext(), CourseOrderDetailActivity.class);
                        } else {
                            intentOrder = new Intent(UIUtils.getContext(), OrderDetailActivity.class);
                        }
                        intentOrder.putExtra("oid", mLists.get(i - 1).getDetail_id());
                        UIUtils.startActivity(intentOrder);
                        break;
                    case 2://购物车(已经没有购物车了)
                        break;
                    case 4://机构加人
                    case 5://机构修改权限
                    case 6://机构审核
                    case 9://老师退出机构(我的机构)
                        break;
                    case 3://陪读
                        String content = mLists.get(i - 1).getContent().split("：")[1].split("，")[0].trim();
                        Intent intent = new Intent();
                        intent.setClass(UIUtils.getContext(), AccompanyReadStatusActivity.class);
                        intent.putExtra(SchemeControl.PASSWORD, content);
                        UIUtils.startActivity(intent);
                        break;

                    case 8://机构移除老师(个人中心)
                        jumpMessageDetial("person", new Intent(UIUtils.getContext(), PersonalCenterActivity.class));
                        break;
                    case 13://机构审核失败
                        break;
                    case 14://课程审核通过
                    case 15://课程审核不通过
                    case 16://老师取消课程
                    case 17://系统取消课程
                        break;
                    case 23://审核通过
                    case 24://审核不通过
                        break;

                }
            }
        });
    }

    private void jumpMessageDetial(String s, Intent intent) {

        if (null != s) {
            if ("read".equals(s)) {
                intent.putExtra("read", s);
            } else if ("person".equals(s)) {
                intent.putExtra("person", s);
            }/*else if("jigou".equals(s)){
                intent.putExtra("jigou", s);
            }else if("cart".equals(s)){
                UIUtils.startActivity(intent);
            }*/ else if ("fail".equals(s)) {
            } else if ("course".equals(s)) {
                intent.putExtra("course", s);
            } else {
                intent.putExtra("status", s);
            }
        }
        UIUtils.startActivity(intent);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        big_type = getIntent().getIntExtra("big_type", 0);
        String title = null;
        switch (big_type) {
            case TYPE_ORDER:
                // 订单
                title = getString(R.string.order_mess);
                break;
            case TYPE_MECH:
                // 系统
                title = getString(R.string.mech_mess);
                break;
            case TYPE_PEIDU:
                // 陪读
                title = getString(R.string.peidu_mess);
                break;
        }
        head_title.setText(title);
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        int limit_end = 5;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "message_box");
        params.addBodyParameter("uid", StringUtils.getUid());
        params.addBodyParameter("big_type", String.valueOf(big_type));
        params.addBodyParameter("limit", limit + "," + limit_end);
        params.addBodyParameter("url", UrlUtils.SERVER_API_COMMON);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
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
     * 处理返回的数据
     *
     * @param result 结果集
     */
    private void processData(String result) {
        if (null != result) {
            MessageDetialData messageDetialData = GsonTools.changeGsonToBean(result, MessageDetialData.class);
            if (null != messageDetialData) {
                if (messageDetialData.getCode() == 1) {
                    List<MessageDetialData.MessageInfo> infos = messageDetialData.getData();
                    if (null != infos && infos.size() > 0) {
                        mLists.addAll(infos);
                        footView.setVisibility(View.GONE);
                    } else {
                        if (limit != 0) {
                            footView.setVisibility(View.VISIBLE);
                        }
                        if (!flag_down) {
                            lv_message.onRefreshComplete();
                            lv_message.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                    }
                    if (null != mLists && mLists.size() > 0) {
                        lv_message.setVisibility(View.VISIBLE);
                        tv_no_message.setVisibility(View.GONE);
                        lv_message.onRefreshComplete();
                        if (null != messageAdapter) {
                            messageAdapter.notifyDataSetChanged();
                        } else {
                            ListView refreshableView = lv_message.getRefreshableView();
                            FrameLayout footerLayoutHolder = new FrameLayout(UIUtils.getContext());
                            footerLayoutHolder.addView(footView);
                            refreshableView.addFooterView(footerLayoutHolder);
                            messageAdapter = new MessageAdapter(mLists);
                            lv_message.setAdapter(messageAdapter);
                        }
                    } else {
                        lv_message.setVisibility(View.GONE);
                        tv_no_message.setVisibility(View.VISIBLE);
                    }
                } else {
                    FailCodeControl.checkCode(messageDetialData.getCode());
                    lv_message.setVisibility(View.GONE);
                    tv_no_message.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public String getPageName() {
        if (big_type == TYPE_ORDER) {
            return getString(R.string.title_act_my_message_order);
        } else if (big_type == TYPE_MECH) {
            return getString(R.string.title_act_my_message_mech);
        } else if (big_type == TYPE_PEIDU) {
            return getString(R.string.title_act_my_message_peidu);
        }
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_message_detial;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                finish();
                break;
        }
    }
}
