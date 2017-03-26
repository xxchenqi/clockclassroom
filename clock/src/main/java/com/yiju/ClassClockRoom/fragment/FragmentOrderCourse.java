package com.yiju.ClassClockRoom.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ejupay.sdk.EjuPayManager;
import com.ejupay.sdk.service.EjuPayResultCode;
import com.ejupay.sdk.service.IPayResult;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.AliPay_PayFailActivity;
import com.yiju.ClassClockRoom.act.AliPay_PayOKActivity;
import com.yiju.ClassClockRoom.act.CourseOrderDetailActivity;
import com.yiju.ClassClockRoom.act.MainActivity;
import com.yiju.ClassClockRoom.adapter.OrderCourseAdapter;
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.bean.CreateOrderResult;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.OrderCourseData;
import com.yiju.ClassClockRoom.bean.OrderCourseResult;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程订单列表_碎片
 * Created by wh on 2016/6/22.
 * <p/>
 * 订单状态    状态码
 * 待支付      0
 * 进行中      1
 * 已完成      101
 * 订单超时    6
 * 已关闭      4
 * 已取消      100(学生取消) 110(老师取消)
 */
public class FragmentOrderCourse extends BaseFragment
        implements OrderCourseAdapter.OrderClickListener,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        CompoundButton.OnCheckedChangeListener {

    //list
    private PullToRefreshListView lv_order;
    //无订单布局
    private RelativeLayout rl_no_order;
    //随便逛逛按钮
    private Button btn_stroll;
    //无WIFI显示界面
    private RelativeLayout ly_wifi;
    //刷新
    private Button btn_no_wifi_refresh;
    //全选
    private CheckBox cb_check_all;
    //合并支付布局
    private RelativeLayout rl_merge_pay;
    //总价
    private TextView tv_total_price;
    //合并支付
    private Button btn_merge_pay;
    //参数limit开始
    private int limit = 0;
    //参数limit数量
    private int limit_end = 5;
    //服务器时间
    private int serverTime;
    //适配器
    private OrderCourseAdapter mineOrderAdapter;
    //数据源
    private ArrayList<OrderCourseData> data;

    //刷新的标记为，默认为下拉刷新
    private boolean is_down_refresh = true;
    //是否正在滑动
    private boolean isScrolling = false;
    //第一次加载标志位
    private boolean isFirst = true;
    //回调的标志位
    private boolean result_flag = false;
    //参数status,状态码
    private String status;
    //全选按钮的标记
    private boolean cbFlag = true;

    //当前价格
    private double currentPrice;
    //总共价格
    private double sumPrice;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    //是否需要继续倒计时
                    boolean isNeedCountTime = false;
                    //遍历list里有没有需要倒计时的
                    for (int index = 0; index < data.size(); index++) {
                        OrderCourseData d = data.get(index);
                        //未支付的状态
                        if ("0".equals(d.getStatus())) {
                            //获取时间戳(秒)
                            int time = d.getTime();
                            if (time > 0) {
                                // 设置flag要刷新界面
                                isNeedCountTime = true;
                                //时间-1秒
                                d.setTime(time - 1);
                            } else if (time == 0) {
                                // 时间如果=0，请求刷新界面
                                d.setTime(time - 1);
                                //强制设置已关闭
                                d.setStatus("4");
//                                getHttpUtils();
                            }
                        }
                    }

                    //滑动的时候不去刷新，防止卡顿
                    if (!isScrolling) {
                        mineOrderAdapter.notifyDataSetChanged();
                    }
                    //是否需要刷新
                    if (isNeedCountTime) {
                        // 继续倒计时刷新页面
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }

                    break;
            }

        }

    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int setContentViewId() {
        return R.layout.fragment_order_course;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        Bundle arguments = getArguments();
        status = arguments.getString("status");
    }

    @Override
    protected void initView() {
        lv_order = (PullToRefreshListView) currentView.findViewById(R.id.lv_order);
        rl_no_order = (RelativeLayout) currentView.findViewById(R.id.rl_no_order);
        btn_stroll = (Button) currentView.findViewById(R.id.btn_stroll);
        ly_wifi = (RelativeLayout) currentView.findViewById(R.id.ly_wifi);
        btn_no_wifi_refresh = (Button) currentView.findViewById(R.id.btn_no_wifi_refresh);
        cb_check_all = (CheckBox) currentView.findViewById(R.id.cb_check_all);
        rl_merge_pay = (RelativeLayout) currentView.findViewById(R.id.rl_merge_pay);
        tv_total_price = (TextView) currentView.findViewById(R.id.tv_total_price);
        btn_merge_pay = (Button) currentView.findViewById(R.id.btn_merge_pay);

        data = new ArrayList<>();
        if ("-1".equals(status)) {
            mineOrderAdapter = new OrderCourseAdapter(getActivity(), data,
                    R.layout.item_order_course, this, true);
        } else {
            mineOrderAdapter = new OrderCourseAdapter(getActivity(), data,
                    R.layout.item_order_course, this, false);
        }
    }

    @Override
    protected void initData() {
        lv_order.setAdapter(mineOrderAdapter);
        //请求
        if (NetWorkUtils.getNetworkStatus(getActivity())) {
            ly_wifi.setVisibility(View.GONE);
            lv_order.setVisibility(View.VISIBLE);
            getHttpUtils();
        } else {
            ly_wifi.setVisibility(View.VISIBLE);
            lv_order.setVisibility(View.GONE);
        }

        //滑动监听
        lv_order.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrolling = !(scrollState == SCROLL_STATE_IDLE
                        || scrollState == SCROLL_STATE_TOUCH_SCROLL);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        //刷新设置
        lv_order.setMode(PullToRefreshBase.Mode.BOTH);
        lv_order.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                is_down_refresh = true;
                limit = 0;
                limit_end = 5;
                getHttpUtils();
                //如果数据全部已请求完后，lv设置只能下拉刷新，重新下拉刷新后再去打开上拉加载
                lv_order.setMode(PullToRefreshBase.Mode.BOTH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                is_down_refresh = false;
                limit += 5;
                limit_end = 5;
                getHttpUtils();
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        btn_stroll.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        lv_order.setOnItemClickListener(this);
        btn_merge_pay.setOnClickListener(this);
        cb_check_all.setOnCheckedChangeListener(this);
    }

    /**
     * 订单列表请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_order_course_list");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("limit", limit + "," + limit_end);
        if (!"all".equals(status)) {
            params.addBodyParameter("status", status);
        }
        // params.addBodyParameter("invoice_flag", invoice_flag);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
//                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                });
    }

    private void processData(String result) {
        //请求前停止handler
        handler.removeMessages(1);
        OrderCourseResult courseResult = GsonTools.changeGsonToBean(result,
                OrderCourseResult.class);
        if (courseResult == null) {
            return;
        }

        serverTime = courseResult.getServertime();

        if ("1".equals(courseResult.getCode())) {

            if (is_down_refresh) {
                data.clear();
                sumPrice = 0;
            }
            List<OrderCourseData> courseDatas = courseResult.getData();
            if (courseDatas == null) {
                return;
            }
            if (courseDatas.size() > 0) {
                currentPrice = 0;
                cbFlag = false;
                cb_check_all.setChecked(false);
                cbFlag = true;

                if ("-1".equals(status)) {
                    rl_merge_pay.setVisibility(View.GONE);
                }
                for (int i = 0; i < courseDatas.size(); i++) {
                    OrderCourseData courseData = courseDatas.get(i);
                    int time = (Integer.valueOf(courseData.getExpire_time()) - serverTime);
                    courseData.setTime(time);
                    sumPrice += Double.valueOf(courseData.getReal_fee());
                }

                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setCbChoose(false);
                }
                tv_total_price.setText(
                        String.format(
                                UIUtils.getString(R.string.how_much_money),
                                currentPrice
                        ));
                this.data.addAll(courseDatas);
                mineOrderAdapter.notifyDataSetChanged();
                handler.sendEmptyMessage(1);
                isFirst = false;
            } else {
                //如果数量为0就显示无订单界面
                if (is_down_refresh) {
                    lv_order.setVisibility(View.GONE);
                    rl_no_order.setVisibility(View.VISIBLE);
                } else {
                    handler.sendEmptyMessage(1);
                    lv_order.onRefreshComplete();
                    //数据如果我为0则只设置上拉刷新
                    lv_order.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        } else {
            //参数不全
            UIUtils.showToastSafe(courseResult.getMsg());
        }

        //完成刷新
        lv_order.onRefreshComplete();

        if (result_flag) {
            limit = limit_end;
            result_flag = false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stroll:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("backhome", "backhome");
                startActivity(intent);
                break;
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(getActivity())) {
                    ly_wifi.setVisibility(View.GONE);
                    lv_order.setVisibility(View.VISIBLE);
                    getHttpUtils();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    lv_order.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_merge_pay:
                UIUtils.showToastSafe("合并支付");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderCourseData d = data.get(position - 1);
        Intent intent = new Intent(getActivity(), CourseOrderDetailActivity.class);
        intent.putExtra("oid", d.getOrder_course_id() + "");
        startActivityForResult(intent, 0);
    }

    @Override
    public void orderClick(View view, int position) {
        String text = ((Button) view).getText().toString();
        String id = data.get(position).getOrder_course_id() + "";

        switch (view.getId()) {
            case R.id.btn_mine_order_right:
//                if (UIUtils.getString(R.string.order_immediate_pay).equals(text)) {
//                    getHttpUtils4(id, data.get(position).getOrder2());
//                } else
                if (UIUtils.getString(R.string.order_delete).equals(text)) {
                    deleteOrder(id);
                }
//                else if (UIUtils.getString(R.string.order_classroom_arrangement).equals(text)) {
                    //跳转到课室选择列表页
//                    ArrayList<Order2> order2 = data.get(position).getOrder2();
//                    if (order2.size() > 1) {
//                        Intent intentOrder = new Intent(getActivity(), OrderDetailListActivity.class);
//                        intentOrder.putExtra("oid", data.get(position).getId() + "");
//                        startActivity(intentOrder);
//                    } else {
//                        Intent intentOrder = new Intent(getActivity(), ClassroomArrangementActivity.class);
//                        intentOrder.putExtra("order2", order2.get(0));
//                        startActivityForResult(intentOrder, 1);
//                    }
//                }
//                else if ("开具发票".equals(text)) {
//                    Intent intent = new Intent(this, InvoiceInformationActivity.class);
//                    intent.putExtra("oid", id);
//                    startActivityForResult(intent, 0);
//                }
                else if (UIUtils.getString(R.string.order_close).equals(text)) {
                    cancelOrder(id);
                }
                break;
            case R.id.btn_mine_order_left:
                if (UIUtils.getString(R.string.order_close).equals(text)) {
                    cancelOrder(id);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (cbFlag) {
            if (isChecked) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setCbChoose(true);
                }
                currentPrice = sumPrice;
            } else {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setCbChoose(false);
                }
                currentPrice = 0;
            }
            tv_total_price.setText(String.format(getString(R.string.how_much_money), currentPrice + ""));
            mineOrderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void cbClick(boolean isChecked, String real_fee) {
        cbFlag = false;
        if (isChecked) {
            int size = 0;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).isCbChoose()) {
                    size++;
                } else {
                    break;
                }
            }
            if (size == data.size()) {
                cb_check_all.setChecked(true);
            }

            currentPrice += Double.valueOf(real_fee);

        } else {
            cb_check_all.setChecked(false);
            currentPrice -= Double.valueOf(real_fee);
        }
        cbFlag = true;
        tv_total_price.setText(String.format(getString(R.string.how_much_money), currentPrice + ""));

    }


    /**
     * 取消订单
     */
    private void cancelOrder(final String oid) {
        CustomDialog customDialog = new CustomDialog(
                getActivity(),
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_close_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    getHttpUtils2(oid);
                }
            }
        });
    }

    // 请求关闭课程订单
    private void getHttpUtils2(String oid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "close_course_order");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order1_id", oid);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData2(arg0.result);

                    }
                });
    }

    private void processData2(String result) {
        OrderCourseResult mineOrder = GsonTools.changeGsonToBean(result,
                OrderCourseResult.class);
        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            //刷新
            if (NetWorkUtils.getNetworkStatus(getActivity())) {
                ly_wifi.setVisibility(View.GONE);
                lv_order.setVisibility(View.VISIBLE);
                //标记为设置true，数据clear完在去加载
                is_down_refresh = true;
                if (limit == 0) {
                    limit_end = 5;
                } else {
                    limit_end = limit;
                }
                limit = 0;
                result_flag = true;
                getHttpUtils();
            } else {
                ly_wifi.setVisibility(View.VISIBLE);
                lv_order.setVisibility(View.GONE);
            }
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }


    /**
     * 删除订单
     */
    private void deleteOrder(final String oid) {
        CustomDialog customDialog = new CustomDialog(
                getActivity(),
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_delete_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {// 请求删除
                    getHttpUtils3(oid);
                }
            }
        });
    }

    /**
     * 删除请求
     *
     * @param oid 订单id
     */
    private void getHttpUtils3(String oid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "delete_course_order");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order1_id", oid);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData3(arg0.result);

                    }
                });
    }

    private void processData3(String result) {
        OrderCourseResult mineOrder = GsonTools.changeGsonToBean(result,
                OrderCourseResult.class);
        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            //刷新
            if (NetWorkUtils.getNetworkStatus(getActivity())) {
                ly_wifi.setVisibility(View.GONE);
                lv_order.setVisibility(View.VISIBLE);
                //标记为设置true，数据clear完在去加载
                is_down_refresh = true;
                if (limit == 0) {
                    limit_end = 5;
                } else {
                    limit_end = limit;
                }
                limit = 0;
                result_flag = true;
                getHttpUtils();
            } else {
                ly_wifi.setVisibility(View.VISIBLE);
                lv_order.setVisibility(View.GONE);
            }
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }
    }


    /**
     * 订单详情请求
     *
     * @param oid     订单id
     * @param order2s 2级订单
     */
    private void getHttpUtils4(final String oid, final ArrayList<Order2> order2s) {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "order_detail");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("level", "1");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData4(arg0.result, oid, order2s);
                    }
                });
    }

    private void processData4(String result, String oid, ArrayList<Order2> order2s) {
        // <!-- 0未支付 1已支付 2过期未支付 3已支付退款 4已取消 5支付失败 status是状态 -->
        ProgressDialog.getInstance().dismiss();
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }

        if ("1".equals(mineOrder.getCode())) {
            ArrayList<Order2> order2 = mineOrder.getData().get(0).getOrder2();
            if (order2.size() > 0) {
                //设置要传到支付结果的bean
                CartCommit commitInfo = new CartCommit();
                commitInfo.setUse_desc(order2.get(0).getUse_desc());
                commitInfo.setType_desc(order2.get(0).getType_desc());
                commitInfo.setRoom_count(Integer.valueOf(order2.get(0)
                        .getRoom_count()));
                commitInfo.setOrder1_id(Integer.valueOf(mineOrder.getData().get(0).getId()));
                String sid = order2.get(0).getSid();
                String name = order2.get(0).getSname();
                parseData(oid, commitInfo, sid, name, order2s);
            }
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }
    }

    /**
     * 支付宝支付
     */
    private void parseData(String oid, final CartCommit commitInfo, final String sid, final String name, final ArrayList<Order2> order2s) {
        //判断之前是否初始化
        EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
            @Override
            public void onSuccess() {
                // 调取易支付创建订单报文签名接口
                getRequestCreateOrder(commitInfo, sid, name, order2s);
            }
        });
        //支付宝支付相关处理
//        getAliPaySign(oid);
    }

    /**
     * 请求 获取创建订单报文签名 接口
     */
    private void getRequestCreateOrder(final CartCommit commitInfo, final String sid,
                                       final String name, final ArrayList<Order2> order2s) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "create_order");
        params.addBodyParameter("trade_id", commitInfo.getTrade_id());
        params.addBodyParameter("terminalType", "SDK");
//        params.addBodyParameter("is_admin", "0");// 是否后台支付 默认0 ,可不传
        httpUtils.send(HttpRequest.HttpMethod.POST,
                UrlUtils.SERVER_API_PAY, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe("请求数据失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        processDataForCreateOrder(arg0.result, commitInfo, sid, name, order2s);
                    }
                }
        );
    }

    private void processDataForCreateOrder(
            String result,
            final CartCommit commitInfo,
            final String sid,
            final String name,
            final ArrayList<Order2> order2s
    ) {
        CreateOrderResult createOrderResult = GsonTools.changeGsonToBean(
                result,
                CreateOrderResult.class
        );
        if (createOrderResult == null) {
            return;
        }
        if ("1".equals(createOrderResult.getCode())) {
            //调取EjuPay支付方法
            EjuPayManager.getInstance().startSelectPay(
                    getActivity(),
                    createOrderResult.getBody(),
                    createOrderResult.getSign(),
                    new IPayResult() {
                        @Override
                        public void payResult(int code, String msg, String dataJson) {
                            if (code == EjuPayResultCode.PAY_SUCCESS_CODE.getCode()) {
                                //支付成功
                                Intent intentSucess = new Intent(
                                        getActivity(),
                                        AliPay_PayOKActivity.class);
                                intentSucess.putExtra("sucess", commitInfo);
                                intentSucess.putExtra("sid", sid);
                                intentSucess.putExtra("order2", order2s);
                                intentSucess.putExtra("name", name);
                                startActivity(intentSucess);
                            } else if (code == EjuPayResultCode.PAY_FAIL_CODE.getCode()) {
                                //支付失败
                                Intent intentFail = new Intent(
                                        getActivity(),
                                        AliPay_PayFailActivity.class);
                                intentFail.putExtra("fail", commitInfo);
                                intentFail.putExtra("sid", sid);
                                intentFail.putExtra("name", name);
                                startActivity(intentFail);
                            } else if (code == EjuPayResultCode.PAY_CANCEL_C0DE.getCode()) {
                                //支付取消
                                Intent intentFail = new Intent(
                                        getActivity(),
                                        AliPay_PayFailActivity.class);
                                intentFail.putExtra("fail", commitInfo);
                                intentFail.putExtra("sid", sid);
                                intentFail.putExtra("name", name);
                                startActivity(intentFail);
                            }
                        }
                    }
            );
        }
    }

    /**
     * 获取AliPay密钥
     */
  /*  private void getAliPaySign(String oid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "alipay");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("oid", oid);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_COUPON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe("请求数据失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if (arg0.result == null) {
                            UIUtils.showToastSafe(R.string.fail_data_request);
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(arg0.result);
                            if (jsonObject.getInt("code") == 1
                                    && jsonObject.getJSONObject("data") != null) {
                                String rsasign = jsonObject.getJSONObject(
                                        "data").getString("rsasign");
                                String prestr = jsonObject
                                        .getJSONObject("data").getString(
                                                "prestr");
                                String payInfo = prestr + "&sign=\"" + rsasign
                                        + "\"&" + "sign_type=\"RSA\"";
                                if (StringUtils.isNotNullString(payInfo)) {
                                    *//*functionAliPay(payInfo, conmmitInfo, sid, name, order2s);*//*
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }*/


    // 调用alipay
    /*private void functionAliPay(final String payInfo, final CartCommit conmmitInfo, final String sid, final String name, final ArrayList<Order2> order2s) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                // 返回结果
                final String resultData = alipay.pay(payInfo);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (StringUtils.isNotNullString(resultData)) {
                            AlipayResult result = new AlipayResult(resultData);
                            String state = result.getResultStatus();
                            switch (state) {
                                case AlipayResult.AliPay_PayOK:
                                    // 成功处理
                                    Intent intentSucess = new Intent(
                                            getActivity(),
                                            AliPay_PayOKActivity.class);
                                    intentSucess.putExtra("sucess", conmmitInfo);
                                    intentSucess.putExtra("sid", sid);
                                    intentSucess.putExtra("order2", order2s);
                                    intentSucess.putExtra("name", name);
                                    startActivity(intentSucess);
                                    break;
                                case AlipayResult.AliPay_PayFail:
                                case AlipayResult.AliPay_PayCancel:
                                    Intent intentFail = new Intent(
                                            getActivity(),
                                            AliPay_PayFailActivity.class);
                                    intentFail.putExtra("fail", conmmitInfo);
                                    intentFail.putExtra("sid", sid);
                                    intentFail.putExtra("name", name);
                                    startActivity(intentFail);

                                    break;
                                default:
                                    UIUtils.showToastSafe(result.getHashMapV()
                                            + "code:" + result.getResultStatus());
                                    break;
                            }
                        } else {
                            UIUtils.showToastSafe("启动支付宝失败");
                        }
                    }
                });

            }
        }).start();
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (NetWorkUtils.getNetworkStatus(getActivity())) {
                ly_wifi.setVisibility(View.GONE);
                lv_order.setVisibility(View.VISIBLE);
                //标记为设置true，数据clear完在去加载
                is_down_refresh = true;
                if (limit == 0) {
                    limit_end = 5;
                } else {
                    limit_end = limit;
                }
                limit = 0;
                result_flag = true;
                getHttpUtils();
            } else {
                ly_wifi.setVisibility(View.VISIBLE);
                lv_order.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }


    @Override
    public String getPageName() {
        return null;
    }
}
