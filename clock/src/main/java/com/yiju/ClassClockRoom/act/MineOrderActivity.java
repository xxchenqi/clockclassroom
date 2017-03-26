package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.MineOrderAdapter;
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.bean.CommonMsgResult;
import com.yiju.ClassClockRoom.bean.CreateOrderResult;
import com.yiju.ClassClockRoom.bean.MineOrderData;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.control.FragmentFactory;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * --------------------------------------
 * 注释:我的订单
 * 作者: cq
 * 时间: 2015-12-9 上午9:09:27
 * <p>
 * 订单状态    状态码
 * 待支付      0/9
 * 进行中      1
 * 已完成      101
 * 订单超时    6
 * 已关闭      2/4/8
 * 已取消      100/11/12
 * 待确认      7/10
 * <p>
 * --------------------------------------
 */
public class MineOrderActivity extends BaseActivity implements OnClickListener,
        OnItemClickListener, MineOrderAdapter.OrderClickListener {
    //传参的状态
    public static final String STATUS = "status";
    //待支付的数量传参
    public static final String COUNT = "count";
    // 待支付
    public static final String STATUS_WAIT_PAY = "-1";
    // 已支付
    public static final String STATUS_USE = "1";
    // 已完成
    public static final String STATUS_FINISH = "99";
    // 全部订单
    public static final String STATUS_ALL = "ALL";
    //成功的状态码
    private String successfulCode = "1";
    /**
     * 退出按钮
     */
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    @ViewInject(R.id.head_title)
    private TextView head_title;
    /**
     * listview
     */
    @ViewInject(R.id.lv_mineorder)
    private PullToRefreshListView lv_mineorder;
    /**
     * 无订单布局
     */
    @ViewInject(R.id.rl_mine_order3)
    private RelativeLayout rl_mine_order3;
    /**
     * 随便逛逛按钮
     */
    @ViewInject(R.id.btn_mineorder_stroll)
    private Button btn_mineorder_stroll;
    /**
     * 无WIFI显示界面
     */
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    /**
     * 刷新
     */
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;
    /**
     * 参数limit开始
     */
    private int limit = 0;
    /**
     * 参数limit数量
     */
    private int limit_end = 5;
    /**
     * 服务器时间
     */
    private int serverTime;
    /**
     * 我的订单适配器
     */
    private MineOrderAdapter mineOrderAdapter;
    /**
     * listview数据源
     */
    private ArrayList<MineOrderData> data;
    /**
     * 刷新的标记为，默认为下拉刷新
     */
    private boolean is_down_refresh = true;
    /**
     * 是否正在滑动
     */
    private boolean isScrolling = false;
    /**
     * 第一次加载标志位
     */
    private boolean isFirst = true;
    /**
     * 回调的标志位
     */
    private boolean result_flag = false;
    /**
     * 参数status,状态码
     */
    private String status;
    /**
     * 支付订单的数量
     */
    private String count;
    //批量开具发票
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //批量开具发票布局
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;

    private String order1_id;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    long timeStart = System.currentTimeMillis();
                    //是否需要继续倒计时
                    boolean isNeedCountTime = false;
                    //遍历list里有没有需要倒计时的
                    for (int index = 0; index < data.size(); index++) {
                        MineOrderData d = data.get(index);
                        //未支付的状态
                        if ("0".equals(d.getStatus()) || "9".equals(d.getStatus())) {

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
                                d.setStatusFlag(true);
//                                getHttpUtils();
                            }
                        }
                    }

                    //滑动的时候不去刷新，防止卡顿
                    if (!isScrolling) {
                        mineOrderAdapter.notifyDataSetChanged();
                    }
                    long timeEnd = System.currentTimeMillis();
                    long timeResult = timeEnd - timeStart;
                    //是否需要刷新
                    if (isNeedCountTime) {
                        // 继续倒计时刷新页面
                        handler.sendEmptyMessageDelayed(1, 1000 - timeResult);
                    }

                    break;
            }

        }

    };


    @Override
    public int setContentViewId() {
        return R.layout.activity_mineorder;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        status = getIntent().getStringExtra(STATUS);
        count = getIntent().getStringExtra(COUNT);
    }

    @Override
    public void initView() {
        data = new ArrayList<>();
        mineOrderAdapter = new MineOrderAdapter(this, data,
                R.layout.item_mineorder, this, false);
    }

    @Override
    public void initListener() {
        super.initListener();
        lv_mineorder.setOnItemClickListener(this);
        head_back_relative.setOnClickListener(this);
        btn_mineorder_stroll.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
    }

    @Override
    public void initData() {
//        head_right_text.setText(UIUtils.getString(R.string.order_draw_up_invoices));
        lv_mineorder.setAdapter(mineOrderAdapter);

        //标题设置
        if (STATUS_ALL.equals(status)) {
            head_title.setText(UIUtils.getString(R.string.all_order));
        } else if (STATUS_FINISH.equals(status)) {
            head_title.setText(UIUtils.getString(R.string.finish_order));
        } else if (STATUS_USE.equals(status)) {
            head_title.setText(UIUtils.getString(R.string.pay_order));
        } else if (STATUS_WAIT_PAY.equals(status)) {
            if (count != null && !"".equals(count) && !"0".equals(count)) {
                head_title.setText(String.format(UIUtils.getString(R.string.wait_pay_sum), count));
            } else {
                head_title.setText(UIUtils.getString(R.string.wait_pay_order));
            }
        }

        //请求
        if (NetWorkUtils.getNetworkStatus(MineOrderActivity.this)) {
            ly_wifi.setVisibility(View.GONE);
            lv_mineorder.setVisibility(View.VISIBLE);
            getHttpUtils();
        } else {
            ly_wifi.setVisibility(View.VISIBLE);
            lv_mineorder.setVisibility(View.GONE);
        }

        //滑动监听
        lv_mineorder.setOnScrollListener(new OnScrollListener() {

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
        lv_mineorder.setMode(PullToRefreshBase.Mode.BOTH);
        lv_mineorder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                is_down_refresh = true;
                limit = 0;
                limit_end = 5;
                getHttpUtils();
                //如果数据全部已请求完后，lv设置只能下拉刷新，重新下拉刷新后再去打开上拉加载
                lv_mineorder.setMode(PullToRefreshBase.Mode.BOTH);
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

    /**
     * 订单列表请求
     */
    private void getHttpUtils() {
        if (isFirst) {
            ProgressDialog.getInstance().show();
        }
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "order_list");
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

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                }
        );
    }

    private void processData(String result) {
        //请求前停止handler
        handler.removeMessages(1);
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }
        serverTime = mineOrder.getTime();

        if (successfulCode.equals(mineOrder.getCode())) {
            if (is_down_refresh) {
                data.clear();
            }
            ArrayList<MineOrderData> data2 = mineOrder.getData();
            if (data2.size() > 0) {
                for (int i = 0; i < data2.size(); i++) {
                    MineOrderData data3 = data2.get(i);
                    int time = (Integer.valueOf(data3.getExpire_time()) - serverTime);
                    data3.setTime(time);
                }

                this.data.addAll(data2);
                mineOrderAdapter.notifyDataSetChanged();
                handler.sendEmptyMessage(1);
                isFirst = false;
            } else {
                //如果数量为0就显示无订单界面
                if (is_down_refresh) {
                    lv_mineorder.setVisibility(View.GONE);
                    rl_mine_order3.setVisibility(View.VISIBLE);
                } else {
                    handler.sendEmptyMessage(1);
                    lv_mineorder.onRefreshComplete();
                    //数据如果我为0则只设置上拉刷新
                    lv_mineorder.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            //重新请求后标题需要重新设置
            if ("-1".equals(status)) {
                String count = this.data.size() + "";
                if (!"".equals(count) && !"0".equals(count)) {
                    head_title.setText(String.format(UIUtils.getString(R.string.wait_pay_sum), count));
                } else {
                    head_title.setText(R.string.wait_pay_order);
                }

            }

        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

        //完成刷新
        lv_mineorder.onRefreshComplete();

        if (result_flag) {
            limit = limit_end;
            result_flag = false;
        }

        ProgressDialog.getInstance().dismiss();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                //返回
                onBackPressed();
                break;
            case R.id.btn_mineorder_stroll:
                // 随便逛逛
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("backhome", "backhome");
                startActivity(intent);
                break;
            case R.id.btn_no_wifi_refresh:
                //刷新
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    lv_mineorder.setVisibility(View.VISIBLE);
                    getHttpUtils();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    lv_mineorder.setVisibility(View.GONE);
                }
                break;
            case R.id.head_right_relative:
//                //批量开具发票
//                UIUtils.showToastSafe("批量开具发票");
//                Intent invoiceOrderIntent = new Intent(this, InvoiceOrderActivity.class);
//                startActivity(invoiceOrderIntent);

                // 跳转至如何开具发票页h5
//                Intent i = new Intent(UIUtils.getContext(),
//                        Common_Show_WebPage_Activity.class);
//                i.putExtra(UIUtils.getString(R.string.get_page_name),
//                        WebConstant.Draw_up_invoices_Page);
//                startActivity(i);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // <!-- 0未支付 1已支付 2过期未支付 3已支付退款 4已取消 5支付失败 status是状态 -->
        MineOrderData d = data.get(position - 1);
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("oid", d.getId());
        startActivityForResult(intent, 0);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //取消订单或者删除订单的操作
        if (resultCode == RESULT_OK || resultCode == ClassroomArrangementActivity.RESULT_CODE_FROM_CLASSROOM_ARRANGEMENT_ACT) {
            if (NetWorkUtils.getNetworkStatus(this)) {
                ly_wifi.setVisibility(View.GONE);
                lv_mineorder.setVisibility(View.VISIBLE);
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
                lv_mineorder.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_MY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public String getPageName() {
        if (STATUS_FINISH.equals(status)) {
            return getString(R.string.title_act_done_order);
        } else if (STATUS_USE.equals(status)) {
            return getString(R.string.title_act_haspay_order);
        } else if (STATUS_WAIT_PAY.equals(status)) {
            return getString(R.string.title_act_nopay_order);
        } else {
            return getString(R.string.title_act_personal_center_my_order);
        }

    }


    /**
     * 取消订单
     */
    private void cancelOrder(final String oid) {
        CustomDialog customDialog = new CustomDialog(
                MineOrderActivity.this,
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

    // 请求取消
    private void getHttpUtils2(String oid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "order_cancel");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("oid", oid);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            //刷新
            if (NetWorkUtils.getNetworkStatus(this)) {
                ly_wifi.setVisibility(View.GONE);
                lv_mineorder.setVisibility(View.VISIBLE);
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
                lv_mineorder.setVisibility(View.GONE);
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
                MineOrderActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_delete_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    // 请求删除
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
        params.addBodyParameter("action", "order_delete");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("oid", oid);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            //刷新
            if (NetWorkUtils.getNetworkStatus(this)) {
                ly_wifi.setVisibility(View.GONE);
                lv_mineorder.setVisibility(View.VISIBLE);
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
                lv_mineorder.setVisibility(View.GONE);
            }
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }

//    /**
//     * 订单详情请求
//     *
//     * @param oid     订单id
//     * @param order2s 2级订单
//     */
//    private void getHttpUtils4(final String oid, final ArrayList<Order2> order2s) {
//        ProgressDialog.getInstance().show();
//        HttpUtils httpUtils = new HttpUtils();
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("action", "order_detail");
//        if (!"-1".equals(StringUtils.getUid())) {
//            params.addBodyParameter("uid", StringUtils.getUid());
//        }
//        params.addBodyParameter("oid", oid);
//        params.addBodyParameter("level", "1");
//
//        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
//                new RequestCallBack<String>() {
//                    @Override
//                    public void onFailure(HttpException arg0, String arg1) {
//                        UIUtils.showToastSafe(R.string.fail_network_request);
//                        ProgressDialog.getInstance().dismiss();
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> arg0) {
//                        ProgressDialog.getInstance().dismiss();
//                        // 处理返回的数据
//                        processData4(arg0.result, oid, order2s);
//                    }
//                });
//    }
//
//    private void processData4(String result, String oid, ArrayList<Order2> order2s) {
//        // <!-- 0未支付 1已支付 2过期未支付 3已支付退款 4已取消 5支付失败 status是状态 -->
//        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
//                MineOrder.class);
//        if (mineOrder == null) {
//            return;
//        }
//
//        if ("1".equals(mineOrder.getCode())) {
//            ArrayList<Order2> order2 = mineOrder.getData().get(0).getOrder2();
//            if (order2.size() > 0) {
//                //设置要传到支付结果的bean
//                CartCommit conmmitInfo = new CartCommit();
//                conmmitInfo.setTrade_id(mineOrder.getData().get(0).getTrade_id());
//                conmmitInfo.setUse_desc(order2.get(0).getUse_desc());
//                conmmitInfo.setType_desc(order2.get(0).getType_desc());
//                conmmitInfo.setRoom_count(Integer.valueOf(order2.get(0)
//                        .getRoom_count()));
//                conmmitInfo.setOrder1_id(Integer.valueOf(mineOrder.getData().get(0).getId()));
//                String sid = order2.get(0).getSid();
//                String name = order2.get(0).getSname();
//                parseData(oid, conmmitInfo, sid, name, order2s, mineOrder.getData().get(0).getConfirm_type());
//            }
//        } else {
//            UIUtils.showToastSafe(mineOrder.getMsg());
//        }
//    }

    /**
     * 支付宝支付
     */
    private void parseData(final CartCommit conmmitInfo, final String sid, final String name, final ArrayList<Order2> order2s, final String confirm_type) {
        //判断之前是否初始化
        EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
            @Override
            public void onSuccess() {
                //调取易支付创建订单报文签名接口
                getRequestCreateOrder(conmmitInfo.getTrade_id(), conmmitInfo, sid, name, order2s, confirm_type);
            }
        });
        //支付宝支付相关处理
//        getAliPaySign(oid);
    }

    /**
     * 请求 获取创建订单报文签名 接口
     */
    private void getRequestCreateOrder(String trade_id, final CartCommit conmmitInfo,
                                       final String sid, final String name,
                                       final ArrayList<Order2> order2s, final String confirm_type) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "create_order");
        params.addBodyParameter("trade_id", trade_id);
        params.addBodyParameter("terminalType", "SDK");
//        params.addBodyParameter("is_admin", "0");// 是否后台支付 默认0 ,可不传
        httpUtils.send(HttpRequest.HttpMethod.POST,
                UrlUtils.SERVER_API_PAY, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        processDataForCreateOrder(arg0.result, conmmitInfo, sid, name, order2s, confirm_type);
                    }
                }
        );
    }

    private void processDataForCreateOrder(String result, final CartCommit conmmitInfo,
                                           final String sid, final String name,
                                           final ArrayList<Order2> order2s, final String confirm_type) {
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
                    MineOrderActivity.this,
                    createOrderResult.getBody(),
                    createOrderResult.getSign(),
                    new IPayResult() {
                        @Override
                        public void payResult(int code, String msg, String dataJson) {
                            if (code == EjuPayResultCode.PAY_SUCCESS_CODE.getCode()) {
                                //支付成功
                                // 成功处理
                                if ("0".equals(confirm_type) || "1".equals(confirm_type)) {
                                    //无需确认和支付前确认,可以布置课室
                                    jumpSucess(conmmitInfo, sid, name, order2s);
                                } else {
                                    //支付后确认,不可以布置课室
                                    jumpNewResult(2, conmmitInfo);
                                }
                            } else if (code == EjuPayResultCode.PAY_FAIL_CODE.getCode()) {
                                //支付失败
                                Intent intentFail = new Intent(
                                        MineOrderActivity.this,
                                        AliPay_PayFailActivity.class);
                                intentFail.putExtra("fail", conmmitInfo);
                                intentFail.putExtra("sid", sid);
                                intentFail.putExtra("name", name);
                                startActivity(intentFail);
                            } else if (code == EjuPayResultCode.PAY_CANCEL_C0DE.getCode()) {
                                //支付取消
                                Intent intentFail = new Intent(
                                        MineOrderActivity.this,
                                        AliPay_PayFailActivity.class);
                                intentFail.putExtra("fail", conmmitInfo);
                                intentFail.putExtra("sid", sid);
                                intentFail.putExtra("name", name);
                                startActivity(intentFail);
                            }
                        }
                    }
            );
        } else {
            UIUtils.showToastSafe(getString(R.string.toast_parameter_is_not_complete));
        }
    }

    /**
     * type: 1 提交成功 2.支付成功 3.支付失败
     * 跳转到新的结果页
     */
    private void jumpNewResult(int type, CartCommit conmmitInfo) {
        Intent intent = new Intent(this,
                NewPayResultActivity.class);
        intent.putExtra("conmmitInfo", conmmitInfo);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    /**
     * 成功跳转
     */
    private void jumpSucess(CartCommit conmmitInfo, String sid, String name, ArrayList<Order2> order2s) {
        Intent intentSucess = new Intent(this,
                AliPay_PayOKActivity.class);
        intentSucess.putExtra("sucess", conmmitInfo);
        intentSucess.putExtra("sid", sid);
        intentSucess.putExtra("name", name);
        intentSucess.putExtra("order2", order2s);
        startActivity(intentSucess);
    }

    /**
     * 获取AliPay密钥
     */
    /*private void getAliPaySign(String oid){
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "alipay");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("oid", oid);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_COUPON, params,
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
                }
        );
    }*/

    // 调用alipay
    /*private void functionAliPay(final String payInfo, final CartCommit conmmitInfo, final String sid, final String name, final ArrayList<Order2> order2s) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MineOrderActivity.this);
                // 返回结果
                final String resultData = alipay.pay(payInfo);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (StringUtils.isNotNullString(resultData)) {
                            AlipayResult result = new AlipayResult(resultData);
                            String state = result.getResultStatus();
                            switch (state) {
                                case AlipayResult.AliPay_PayOK:
                                    // 成功处理
                                    Intent intentSucess = new Intent(
                                            MineOrderActivity.this,
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
                                            MineOrderActivity.this,
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
    //订单列表的，取消订单，删除订单等按钮
    @Override
    public void orderClick(View view, int position) {
        String text = ((Button) view).getText().toString();
        String id = data.get(position).getId();//o1的ID

        switch (view.getId()) {
            case R.id.btn_mine_order_right:
                if (UIUtils.getString(R.string.order_immediate_pay).equals(text)) {

                    MineOrderData o1 = data.get(position);//1级
                    Order2 o2 = o1.getOrder2().get(0);//2级
//                    int coupon_id = Integer.valueOf(o2.getCoupon_id());//优惠券id

                    CartCommit conmmitInfo = new CartCommit();
                    conmmitInfo.setTrade_id(o1.getTrade_id());
                    conmmitInfo.setUse_desc(o2.getUse_desc());
                    conmmitInfo.setType_desc(o2.getType_desc());
                    conmmitInfo.setRoom_count(Integer.valueOf(o2.getRoom_count()));
                    conmmitInfo.setOrder1_id(Integer.valueOf(id));
                    String sid = o2.getSid();
                    String name = o2.getSname();
                    String confirm_type = o1.getConfirm_type();
                    //修改无论有优惠券还是无优惠券都检测
                    checkCoupon(id, conmmitInfo, sid, name, o1.getOrder2(), confirm_type, position);
//                    if (coupon_id > 0 && coupon_id < 999999999) {
//                        //检查优惠券是否失效
//                    } else {
//                        //没用优惠券直接去支付
//                        parseData(conmmitInfo, sid, name, o1.getOrder2(), confirm_type);
//                    }

                } else if (UIUtils.getString(R.string.order_delete).equals(text)) {
                    deleteOrder(id);
                } else if (UIUtils.getString(R.string.order_classroom_arrangement).equals(text)) {
                    //跳转到课室选择列表页
                    ArrayList<Order2> order2 = data.get(position).getOrder2();
                    if (order2.size() > 1) {
                        Intent intentOrder = new Intent(this, OrderDetailListActivity.class);
                        intentOrder.putExtra("oid", data.get(position).getId() + "");
                        startActivity(intentOrder);
                    } else {
                        // 先判断订单日期是否大于今天
                        getSystemTimeRequest(order2.get(0));
                        /*Intent intentOrder = new Intent(this, ClassroomArrangementActivity.class);
                        intentOrder.putExtra("order2", order2.get(0));
                        startActivityForResult(intentOrder, 1);*/
                    }

                }
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

    /**
     * 检测优惠券
     * @param id 订单id
     * @param conmmitInfo 传到支付结果页的bean
     * @param sid 门店id
     * @param name 门店名字
     * @param order2 布置课室
     * @param confirm_type 确认类型
     * @param position 点击列表的位置
     */
    private void checkCoupon(final String id, final CartCommit conmmitInfo, final String sid, final String name, final ArrayList<Order2> order2, final String confirm_type, final int position) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "verify_order_coupon");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order1_id", id);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        CartCommit cartCommit = GsonTools.changeGsonToBean(arg0.result, CartCommit.class);
                        if (cartCommit == null) {
                            return;
                        }
                        if (cartCommit.getCode() == 50050) {//优惠券无效
                            CustomDialog customDialog = new CustomDialog(
                                    MineOrderActivity.this,
                                    UIUtils.getString(R.string.confirm),
                                    UIUtils.getString(R.string.label_cancel),
                                    UIUtils.getString(R.string.dialog_show_coupon_use));
                            customDialog.setOnClickListener(new IOnClickListener() {
                                @Override
                                public void oncClick(boolean isOk) {
                                    if (isOk) {
                                        cancelCoupon(id, position);
                                    }
                                }
                            });
                        } else if (cartCommit.getCode() == 1) {
                            //去支付
                            if (!StringUtils.isNullString(cartCommit.getTrade_id())) {
                                data.get(position).setTrade_id(cartCommit.getTrade_id());
                            }
                            parseData(conmmitInfo, sid, name, order2, confirm_type);
                        }else{
                            UIUtils.showLongToastSafe(cartCommit.getMsg());
                        }
                    }
                });
    }

    /**
     * 删除无效优惠券接口
     * @param id 订单id
     * @param position pos
     */
    private void cancelCoupon(String id, final int position) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "return_order_coupon");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order1_id", id);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        JSONTokener jsonParser = new JSONTokener(arg0.result);
                        JSONObject json;
                        int code = Integer.MAX_VALUE;
                        String tid = null;
                        try {
                            json = (JSONObject) jsonParser.nextValue();
                            code = json.getInt("code");
                            tid = json.getString("trade_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code == 1) {
                            UIUtils.showLongToastSafe(getString(R.string.toast_deleted_coupons));
                            data.get(position).setTrade_id(tid);
                            data.get(position).setReal_fee(data.get(position).getFee());
                            data.get(position).getOrder2().get(0).setCoupon_id("0");
                            mineOrderAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    /**
     * 获取服务端当前时间请求
     *
     * @param o Order2
     */
    public void getSystemTimeRequest(final Order2 o) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_system_time");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataSystemTime(arg0.result, o);
                    }
                }
        );
    }

    private void processDataSystemTime(String result, Order2 o) {
        CommonMsgResult commonMsgResult = GsonTools.fromJson(result, CommonMsgResult.class);
        if ("1".equals(commonMsgResult.getCode())) {
            String sys_time = commonMsgResult.getData();//2015-10-15 15:21:36
            int compare_result = DateUtil.compareDate(sys_time, o.getEnd_date());
            if (compare_result >= 0) {
                //限定  不可点
                UIUtils.showToastSafe(UIUtils.getString(R.string.toast_edit_classroom));
            } else {
                //可点
                Intent i = new Intent(UIUtils.getContext(), ClassroomArrangementActivity.class);
                i.putExtra("order2", o);
                BaseApplication.getmForegroundActivity().startActivityForResult(i, 1);
            }
        }
    }

    @Override
    public void cbClick(boolean isChecked, String rr) {

    }
}
