package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ejupay.sdk.EjuPayManager;
import com.ejupay.sdk.service.EjuPayResultCode;
import com.ejupay.sdk.service.IEjuPayResult;
import com.ejupay.sdk.service.IPayResult;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.adapter.OrderDetailAdapter;
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.bean.CreateOrderResult;
import com.yiju.ClassClockRoom.bean.MineOrderData;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.callback.PayWayOnClickListener;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.LogUtil;
import com.yiju.ClassClockRoom.util.MD5;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.PayWayUtil;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.StringFormatUtil;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * --------------------------------------
 * 注释:订单详情
 * 作者: cq
 * 时间: 2015-12-11 下午6:11:37
 * <p/>
 * 订单状态    状态码        右上角按钮文案        界面是否显示明细与课室布置
 * 待支付      0            关闭订单
 * 进行中      1            如何开具发票          是
 * 已完成      101          如何开具发票          是
 * 订单超时    6
 * 已关闭      2/4          删除订单
 * 已取消      100                               是
 * <p/>
 * --------------------------------------
 */
public class OrderDetailActivity extends BaseActivity implements
        OnClickListener {
    /**
     * 无网
     */
    @ViewInject(R.id.wifi)
    private RelativeLayout wifi;
    /**
     * 无网刷新按钮
     */
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;
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
     * 取消订单按钮
     */
    /*@ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;*/
    /**
     * 取消订单文案
     */
    /*@ViewInject(R.id.head_right_text)
    private TextView head_right_text;*/
    /**
     * 绿色提示
     */
    @ViewInject(R.id.ll_title_green)
    private LinearLayout ll_title_green;
    /**
     * 绿色提示一
     */
    @ViewInject(R.id.tv_title_one)
    private TextView tv_title_one;
    /**
     * 绿色提示二
     */
    @ViewInject(R.id.tv_title_two)
    private TextView tv_title_two;
    /**
     * 订单超时提示
     */
    @ViewInject(R.id.ll_order_overtime)
    private LinearLayout ll_order_overtime;
    /**
     * 客服电话
     */
    @ViewInject(R.id.tv_phone_number)
    private TextView tv_phone_number;

    /**
     * 订单所有内容
     */
    @ViewInject(R.id.sv_order_detail)
    private ScrollView sv_order_detail;
    /**
     * 订单标题栏
     */
    @ViewInject(R.id.rl_order_detail1)
    private RelativeLayout rl_order_detail1;
    /**
     * 订单编号
     */
    @ViewInject(R.id.tv_order_detail_id)
    private TextView tv_order_detail_id;
    /**
     * 订单状态
     */
    @ViewInject(R.id.tv_order_detail_status)
    private TextView tv_order_detail_status;
    /**
     * 订单内容
     */
    @ViewInject(R.id.lv_order_detail_lv)
    private ListView lv_order_detail_lv;
    /**
     * 订单备注
     */
    @ViewInject(R.id.ll_order_detail_remark)
    private LinearLayout ll_order_detail_remark;
    @ViewInject(R.id.tv_remark)
    private TextView tv_remark;
    /**
     * 订单名字
     */
    @ViewInject(R.id.tv_order_detail_name)
    private TextView tv_order_detail_name;
    /**
     * 订单电话
     */
    @ViewInject(R.id.tv_order_detail_mobile)
    private TextView tv_order_detail_mobile;
    /**
     * 订单总计总模块
     */
    @ViewInject(R.id.ll_order_total)
    private LinearLayout ll_order_total;
    /**
     * 订单优惠金额行
     */
    @ViewInject(R.id.rl_privilege_price)
    private RelativeLayout rl_privilege_price;
    /**
     * 订单优惠金额
     */
    @ViewInject(R.id.tv_privilege_price)
    private TextView tv_privilege_price;
    /**
     * 订单优惠内容
     */
    @ViewInject(R.id.tv_privilege_content)
    private TextView tv_privilege_content;
    /**
     * 退款布局
     */
    @ViewInject(R.id.rl_reimburse)
    private RelativeLayout rl_reimburse;
    /**
     * 退款金额
     */
    @ViewInject(R.id.tv_rl_reimburse_prices)
    private TextView tv_rl_reimburse_prices;
    /**
     * 应付金额
     */
    @ViewInject(R.id.tv_ought_pay)
    private TextView tv_ought_pay;
    /**
     * 提醒信息
     */
    @ViewInject(R.id.tv_order_detail_remind)
    private TextView tv_order_detail_remind;
    /**
     * 提醒信息布局
     */
    @ViewInject(R.id.lr_order_detail_remind)
    private LinearLayout lr_order_detail_remind;
    /**
     * 下单时间
     */
    @ViewInject(R.id.tv_order_time)
    private TextView tv_order_time;
    /**
     * 优惠券
     */
    @ViewInject(R.id.ll_order_detail_coupon)
    private LinearLayout ll_order_detail_coupon;
    /**
     * 优惠券金额
     */
    @ViewInject(R.id.tv_coupon)
    private TextView tv_coupon;
    /**
     * 收费设备金额
     */
    @ViewInject(R.id.tv_order_device_money)
    private TextView tv_order_device_money;
    /**
     * 立即支付布局
     */
    @ViewInject(R.id.rl_order_detail_pay)
    private RelativeLayout rl_order_detail_pay;
    /**
     * 立即支付
     */
    @ViewInject(R.id.tv_pay_order)
    private TextView tv_pay_order;
    /**
     * 关闭订单
     */
    @ViewInject(R.id.tv_close_order)
    private TextView tv_close_order;
    /**
     * 删除订单
     */
    @ViewInject(R.id.tv_change_order)
    private TextView tv_change_order;
    /**
     * 支付方式布局
     */
    @ViewInject(R.id.ll_detail_pay)
    private LinearLayout ll_detail_pay;
    /**
     * 支付方式文字
     */
    @ViewInject(R.id.tv_order_alipay)
    private TextView tv_order_alipay;
    /**
     * 支付方式icon
     */
    @ViewInject(R.id.iv_order_alipay)
    private ImageView iv_order_alipay;
    /**
     * 空页面
     */
    @ViewInject(R.id.iv_order_delete)
    private TextView iv_order_delete;
    /**
     * 整体页面
     */
    @ViewInject(R.id.ll_show)
    private LinearLayout ll_show;
    /**
     * 待付/实付
     */
    @ViewInject(R.id.tv_pay_type_name)
    private TextView tv_pay_type_name;

    //发票相关
    /**
     * 发票信息栏
     */
    @ViewInject(R.id.ll_order_invoice)
    private LinearLayout ll_order_invoice;
    /**
     * 发票类型
     */
    @ViewInject(R.id.tv_invoice_type)
    private TextView tv_invoice_type;
    /**
     * 发票公司
     */
    @ViewInject(R.id.tv_invoice_mc)
    private TextView tv_invoice_mc;
    /**
     * 发票项目
     */
    @ViewInject(R.id.tv_invoice_xmmc)
    private TextView tv_invoice_xmmc;
    /**
     * 电子版
     */
    @ViewInject(R.id.rl_electronic_invoice)
    private RelativeLayout rl_electronic_invoice;
    /**
     * 纸质版
     */
    @ViewInject(R.id.rl_paper_invoice)
    private RelativeLayout rl_paper_invoice;
    /**
     * 纸质版发票提醒
     */
    @ViewInject(R.id.tv_paper_desc)
    private TextView tv_paper_desc;

    /**
     * 支付选择的集合
     */
    private List<CheckBox> cbs;
    /**
     * 当前选择的支付
     */
    private int currentNum;
    /**
     * 订单编号(传进来的参数)
     */
    private String oid;
    /**
     * 费用明细
     */
    @ViewInject(R.id.rl_price_detail)
    private RelativeLayout rl_price_detail;
    /**
     * 课室费用
     */
    @ViewInject(R.id.tv_order_detail_money)
    private TextView tv_order_detail_money;
    /**
     * 收费设备
     */
    @ViewInject(R.id.rl_device)
    private RelativeLayout rl_device;
    //提示信息
    @ViewInject(R.id.tv_order_detail_conf_tips)
    private TextView tv_order_detail_conf_tips;
    /**
     * lv适配器
     */
    private OrderDetailAdapter orderDetailAdapter;
    /**
     * lv数据源
     */
    private ArrayList<Order2> datas;
    /**
     * 服务器时间
     */
    private int serverTime;
    /**
     * 延期时间
     */
    private int expireTime;
    /**
     * 倒计时时间 ( time = expireTime - serverTime;)
     */
    private int time;
    /**
     * 解析后的bean
     */
    private MineOrderData data;
    /**
     * 传到支付界面的bean
     */
    private CartCommit conmmitInfo;
    /**
     * 学校id
     */
    private String sid;
    private String name;
    /**
     * 当前状态
     */
    private Integer currentStatus;
    private Integer invoiceType = Integer.MAX_VALUE;
    private String Md5Key = "Eju_classroom_20150827";
    //确认类型
    private String confirm_type;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    //每秒的倒计时时间-1
                    time--;
                    /**
                     * 当倒计时结束时:
                     * 状态：待支付-->已关闭
                     * 按钮文字：关闭订单-->删除订单
                     */
                    if (time >= 0) {
                        handler.sendEmptyMessageDelayed(0, 1000);
//                        tv_count_down.setText("[" + time / (60) + ":" + time % 60 + "]");
                        tv_title_one.setText("请在" + time / 60 / 60 + "时" + time / (60) % 60 + "分" + time % 60 + "秒内完成支付，超时订单自动关闭");
                    } else {
                        //计算后的结果小于0，强制改成已取消状态
                        rl_order_detail_pay.setVisibility(View.VISIBLE);
                        tv_pay_order.setVisibility(View.GONE);
                        tv_close_order.setVisibility(View.GONE);
                        tv_change_order.setVisibility(View.VISIBLE);
                        tv_order_detail_conf_tips.setVisibility(View.GONE);
                        tv_order_detail_status.setText(UIUtils
                                .getString(R.string.status_close));
                        tv_order_detail_status.setTextColor(getResources().getColor(R.color.color_blue_1e));
                    }
                    break;
                case 2:
                    time--;
                    if (time >= 0) {
                        handler.sendEmptyMessageDelayed(2, 1000);
                        tv_title_one.setText("1.请在" + time / 60 / 60 + "时" + time / (60) % 60 + "分" + time % 60 + "秒内完成支付，超时订单自动关闭");
                    } else {
                        rl_order_detail_pay.setVisibility(View.VISIBLE);
                        tv_pay_order.setVisibility(View.GONE);
                        tv_close_order.setVisibility(View.GONE);
                        tv_change_order.setVisibility(View.VISIBLE);
                        tv_order_detail_conf_tips.setVisibility(View.GONE);
                        tv_order_detail_status.setText(UIUtils
                                .getString(R.string.status_close));
                        tv_order_detail_status.setTextColor(getResources().getColor(R.color.color_blue_1e));
                    }
                default:
                    break;
            }
        }
    };
    private Order2 order2Info;
    private HttpHandler<File> download;
    private int coupon_id;
    private String url;
    private String balance;
    private String fee;
    private PopupWindow mPopupWindow;
    private String school_type;
    private ArrayList<Order2> order2;
    private String pay_method;
    private String store_tel;

    @Override
    public int setContentViewId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.order_detail));
        ll_show.setVisibility(View.GONE);
        head_back_relative.setOnClickListener(this);
        tv_order_detail_status.setOnClickListener(this);
        tv_order_detail_money.setOnClickListener(this);
        tv_rl_reimburse_prices.setOnClickListener(this);
//        rl_price_detail.setOnClickListener(this);
        tv_order_device_money.setOnClickListener(this);
        tv_pay_order.setOnClickListener(this);
        tv_close_order.setOnClickListener(this);
        tv_change_order.setOnClickListener(this);
        lr_order_detail_remind.setOnClickListener(this);
        tv_phone_number.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        rl_electronic_invoice.setOnClickListener(this);
        tv_order_detail_conf_tips.setOnClickListener(this);

        Intent intent = getIntent();
        oid = intent.getStringExtra("oid");

        if (null != oid && oid.equals("0")) {
            ll_show.setVisibility(View.GONE);
            iv_order_delete.setVisibility(View.VISIBLE);
            return;
        }
        datas = new ArrayList<>();
        cbs = new ArrayList<>();

    }

    @Override
    public void initData() {
        // 订单优惠内容默认为隐藏
        tv_privilege_content.setVisibility(View.GONE);
        // 提醒信息还未做
        tv_order_detail_remind.setVisibility(View.VISIBLE);

        if (NetWorkUtils.getNetworkStatus(this)) {
            wifi.setVisibility(View.GONE);
            getBalanceHttpUtils();
            getHttpUtils();
        } else {
            wifi.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取余额
     */
    private void getBalanceHttpUtils() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "user_amount_remain");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        ProgressDialog.getInstance().dismiss();
                        JSONObject json;
                        try {
                            json = new JSONObject(arg0.result);
                            String code = json.getString("code");
                            if ("1".equals(code)) {
                                balance = json.getString("data");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 订单详情接口请求
     */
    private void getHttpUtils() {
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
        params.addBodyParameter("room_adjust_new", "1");

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        ProgressDialog.getInstance().dismiss();
                        wifi.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        wifi.setVisibility(View.GONE);
                        ll_show.setVisibility(View.VISIBLE);
                        processData(arg0.result);
                    }
                });
    }

    private void processData(String result) {
        // <!-- 0待支付 1进行中 2已关闭  4已关闭 6订单超时 100已完成 101已取消 status是状态 -->
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            ProgressDialog.getInstance().dismiss();
            return;
        }
        serverTime = mineOrder.getTime();

        if ("1".equals(mineOrder.getCode())) {
            // 显示或者隐藏退款(2次判断)
            if (mineOrder.getData() == null) {
                ProgressDialog.getInstance().dismiss();
                return;
            }
            data = mineOrder.getData().get(0);
            confirm_type = data.getConfirm_type();
            order2 = data.getOrder2();
            if (order2 == null || order2.size() == 0) {
                ProgressDialog.getInstance().dismiss();
                return;
            }
            order2Info = order2.get(0);
            school_type = order2Info.getSchool_type();
            if (StringUtils.isNotNullString(data.getRefund_fee_total())
                    && !"0".equals(data.getRefund_fee_total())) {
                rl_reimburse.setVisibility(View.VISIBLE);
                tv_rl_reimburse_prices.setText(
                        String.format(
                                UIUtils.getString(R.string.how_much_money),
                                StringUtils.getDecimal(data.getRefund_fee_total())
                        ));
            } else {
                rl_reimburse.setVisibility(View.GONE);
            }
            coupon_id = Integer.valueOf(data.getCoupon_id());
            if (data.getExpire_time() != null) {
                expireTime = Integer.valueOf(data.getExpire_time());
            }
            //设置订单编号
            tv_order_detail_id.setText(String.format(UIUtils.getString(R.string.order_num), data.getId()));
            tv_coupon.setText(data.getBatch_name());
            if (StringUtils.isNullString(data.getSchool_phone())) {
                store_tel = UIUtils.getString(R.string.txt_phone_number);
            } else {
                store_tel = data.getSchool_phone();
            }
            String wholeStr = String.format(UIUtils.getString(R.string.format_order_phone_tip), store_tel);
            StringFormatUtil spanStr = new StringFormatUtil(this, wholeStr,
                    store_tel, R.color.blue, new StringFormatUtil.IOnClickListener() {
                @Override
                public void click() {
                }
            }).fillColor();
            tv_order_detail_conf_tips.setHighlightColor(UIUtils.getColor(android.R.color.transparent));
            tv_order_detail_conf_tips.setText(spanStr.getResult());
            pay_method = data.getPay_method();
            if (!StringUtils.isNullString(pay_method)) {
                if (pay_method.equals("1")) {
                    tv_order_alipay.setText(UIUtils.getString(R.string.order_alipay));
                    iv_order_alipay.setVisibility(View.VISIBLE);
                    tv_order_alipay.setVisibility(View.GONE);
                } else if (pay_method.equals("5")) {
                    tv_order_alipay.setText(UIUtils.getString(R.string.reservation_pay_balance));
                    tv_order_alipay.setVisibility(View.VISIBLE);
                } else if (pay_method.equals("6")) {
                    tv_order_alipay.setText(UIUtils.getString(R.string.order_online));
                    iv_order_alipay.setVisibility(View.GONE);
                    tv_order_alipay.setVisibility(View.VISIBLE);
                } else {
                    tv_order_alipay.setText(UIUtils.getString(R.string.reservation_pay_other));
                }
            }
            //获取当前状态
            if (data.getStatus() != null) {
                currentStatus = Integer.valueOf(data.getStatus());
            }
            // 一系列状态的显示和隐藏
            /**
             返回状态  代表含义                 APP中显示
             0         未支付                   待支付
             1         已支付                   进行中

             2         过期未支付               已关闭
             4         已取消                   已关闭
             8         支付前确认不通过         已关闭

             6         已支付订单超时或关闭，   订单超时
             等待后台客服联系用户

             7         支付前待确认             待确认
             10        支付后待确认             待确认

             9         支付后待确认（支付前）   待支付

             11        支付后确认不通过         已取消
             100       退款                     已取消

             101       订单完成                 已完成

             */
            switch (currentStatus) {
                case 0://待支付
                    ll_title_green.setVisibility(View.VISIBLE);
                    tv_title_one.setVisibility(View.VISIBLE);
                    tv_title_two.setVisibility(View.GONE);
                    rl_order_detail_pay.setVisibility(View.VISIBLE);
                    tv_close_order.setVisibility(View.VISIBLE);
                    tv_pay_order.setVisibility(View.VISIBLE);
                    tv_change_order.setVisibility(View.GONE);
                    ll_detail_pay.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_order_detail_coupon.setVisibility(View.VISIBLE);
                    checkSchool();
                    break;
                case 9://支付后待确认（支付前）   待支付
//                    head_right_text.setText(UIUtils.getString(R.string.order_close));
//                    head_right_relative.setVisibility(View.VISIBLE);
                    ll_title_green.setVisibility(View.VISIBLE);
                    tv_title_one.setVisibility(View.VISIBLE);
                    tv_title_two.setVisibility(View.VISIBLE);
                    rl_order_detail_pay.setVisibility(View.VISIBLE);
                    tv_close_order.setVisibility(View.VISIBLE);
                    tv_pay_order.setVisibility(View.VISIBLE);
                    tv_change_order.setVisibility(View.GONE);
                    ll_detail_pay.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_order_detail_coupon.setVisibility(View.VISIBLE);
                    checkSchool();
                    break;
                case 1://进行中
                    ll_title_green.setVisibility(View.GONE);
                    rl_order_detail_pay.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_detail_pay.setVisibility(View.VISIBLE);
                    ll_order_detail_coupon.setVisibility(View.GONE);
                    checkSchool();
                    break;
                case 2://已关闭
                case 4://已关闭
                case 8://支付前确认不通过         已关闭
                    tv_order_detail_conf_tips.setVisibility(View.GONE);
                    ll_title_green.setVisibility(View.GONE);
                    rl_order_detail_pay.setVisibility(View.VISIBLE);
                    tv_close_order.setVisibility(View.GONE);
                    tv_pay_order.setVisibility(View.GONE);
                    tv_change_order.setVisibility(View.VISIBLE);
                    tv_change_order.setText(UIUtils.getString(R.string.order_delete));
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_detail_pay.setVisibility(View.GONE);
                    ll_order_detail_coupon.setVisibility(View.VISIBLE);
                    break;
                case 6://订单超时
                    ll_title_green.setVisibility(View.GONE);
                    rl_order_detail_pay.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.VISIBLE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_detail_pay.setVisibility(View.VISIBLE);
                    ll_order_detail_coupon.setVisibility(View.GONE);
                    checkSchool();
                    break;
                case 101://已完成
                    ll_title_green.setVisibility(View.GONE);
                    rl_order_detail_pay.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_detail_pay.setVisibility(View.VISIBLE);
                    ll_order_detail_coupon.setVisibility(View.GONE);
                    checkSchool();
                    break;
                case 11://已取消
                case 12://已取消
                case 100://已取消
                    ll_title_green.setVisibility(View.GONE);
                    rl_order_detail_pay.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_detail_pay.setVisibility(View.VISIBLE);
                    ll_order_detail_coupon.setVisibility(View.GONE);
                    tv_order_detail_conf_tips.setVisibility(View.GONE);
                    break;
                case 7://支付前待确认             待确认
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_title_green.setVisibility(View.VISIBLE);
                    tv_title_one.setVisibility(View.VISIBLE);
                    tv_title_two.setVisibility(View.GONE);
                    tv_title_one.setText(UIUtils.getString(R.string.order_sure_pay));
                    rl_order_detail_pay.setVisibility(View.VISIBLE);
                    tv_pay_order.setVisibility(View.GONE);
                    tv_close_order.setVisibility(View.GONE);
                    tv_change_order.setVisibility(View.VISIBLE);
                    tv_change_order.setText(UIUtils.getString(R.string.order_close));
                    ll_detail_pay.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_order_detail_coupon.setVisibility(View.VISIBLE);
                    checkSchool();
                    break;
                case 10://支付后待确认             待确认
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_title_green.setVisibility(View.VISIBLE);
                    tv_title_one.setVisibility(View.VISIBLE);
                    tv_title_two.setVisibility(View.GONE);
                    tv_title_one.setText(UIUtils.getString(R.string.order_pay_sure));
                    rl_order_detail_pay.setVisibility(View.VISIBLE);
                    tv_pay_order.setVisibility(View.GONE);
                    tv_close_order.setVisibility(View.GONE);
                    tv_change_order.setVisibility(View.VISIBLE);
                    tv_change_order.setText(UIUtils.getString(R.string.order_cancel));
                    ll_detail_pay.setVisibility(View.VISIBLE);
                    ll_order_overtime.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_order_detail_coupon.setVisibility(View.GONE);
                    checkSchool();
                    break;
                case 3://已删除
                    ll_show.setVisibility(View.GONE);
                    iv_order_delete.setVisibility(View.VISIBLE);
                    checkSchool();
                    break;
                default:
                    break;
            }
            //设置当前状态的文案
            switch (currentStatus) {
                case 0://待支付
                    // 未支付状态要改成时间
                    tv_pay_type_name.setText(UIUtils
                            .getString(R.string.order_wait_pay));
                    tv_order_detail_status.setText(UIUtils
                            .getString(R.string.status_zero));
                    tv_order_detail_status.setTextColor(getResources().getColor(
                            R.color.color_blue_1e));

                    // 开启倒计时
                    time = expireTime - serverTime;
                    if (time >= 0) {
//                        tv_count_down.setText("[" + time / (60) + ":" + time % 60 + "]");
                        tv_title_one.setText("请在" + time / (60) + "分" + time % 60 + "秒内完成支付，超时订单自动关闭");
                        handler.sendEmptyMessage(0);
                    } else {
                        rl_order_detail_pay.setVisibility(View.VISIBLE);
                        tv_pay_order.setVisibility(View.GONE);
                        tv_close_order.setVisibility(View.GONE);
                        tv_change_order.setVisibility(View.VISIBLE);
                        tv_order_detail_status.setText(UIUtils
                                .getString(R.string.status_close));
                        tv_order_detail_status.setTextColor(getResources().getColor(R.color.color_blue_1e));
                    }
                    break;
                case 9://支付后待确认（支付前）   待支付

                    tv_order_detail_status.setText(UIUtils
                            .getString(R.string.status_zero));
                    tv_order_detail_status.setTextColor(getResources().getColor(
                            R.color.color_blue_1e));

                    // 开启倒计时
                    time = expireTime - serverTime;
                    if (time >= 0) {
//                        tv_count_down.setText("[" + time / (60) + ":" + time % 60 + "]");
                        tv_title_one.setText("1.请在" + time / (60) + "分" + time % 60 + "秒内完成支付，超时订单自动关闭");
                        handler.sendEmptyMessage(2);
                    } else {
                        rl_order_detail_pay.setVisibility(View.VISIBLE);
                        tv_pay_order.setVisibility(View.GONE);
                        tv_close_order.setVisibility(View.GONE);
                        tv_change_order.setVisibility(View.VISIBLE);
                        tv_order_detail_status.setText(UIUtils
                                .getString(R.string.status_close));
                        tv_order_detail_status.setTextColor(getResources().getColor(R.color.color_blue_1e));
                    }
                    tv_pay_type_name.setText(UIUtils
                            .getString(R.string.order_wait_pay));
                    break;
                case 1://进行中
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_ing));
                    tv_pay_type_name.setText(UIUtils
                            .getString(R.string.order_ought_pay));
                    break;
                case 2://已关闭
                case 4://已关闭
                case 8://已关闭
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_close));
                    tv_pay_type_name.setText(UIUtils
                            .getString(R.string.order_ought_pay));
                    break;
                case 6://订单超时
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_overtime));
                    tv_pay_type_name.setText(UIUtils
                            .getString(R.string.order_ought_pay));
                    break;
                case 101://已完成
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_finish));
                    tv_pay_type_name.setText(UIUtils
                            .getString(R.string.order_ought_pay));
                    break;
                case 11://已取消
                case 12://已取消
                case 100://已取消
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_cancel));
                    tv_pay_type_name.setText(UIUtils
                            .getString(R.string.order_ought_pay));
                    break;
                case 7://待确认
                case 10://待确认
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_affirm));
                    tv_pay_type_name.setText(UIUtils
                            .getString(R.string.order_ought_pay));
                    break;
                default:
                    break;
            }

            if (StringUtils.isNullString(data.getInvoice_type())) {
                ll_order_invoice.setVisibility(View.GONE);
            } else {
                invoiceType = Integer.valueOf(data.getInvoice_type());
            }
            String invoice_rule = data.getInvoice_rule();
            if (!StringUtils.isNullString(invoice_rule)) {
                tv_paper_desc.setText(invoice_rule);
            }
            url = data.getPdf_url();
            switch (invoiceType) {
                case 0://无发票
                    ll_order_invoice.setVisibility(View.GONE);
                    break;
                case 1://普通发票
                    setInvoiceData(UIUtils.getString(R.string.invoice_paper));
                    rl_electronic_invoice.setVisibility(View.GONE);
                    rl_paper_invoice.setVisibility(View.VISIBLE);
                    break;
                case 2://电子发票
                    setInvoiceData(UIUtils.getString(R.string.invoice_electronic));
                    if (StringUtils.isNullString(url)) {
                        rl_electronic_invoice.setVisibility(View.GONE);
                    } else {
                        rl_electronic_invoice.setVisibility(View.VISIBLE);
                    }
                    rl_paper_invoice.setVisibility(View.GONE);
                    break;
                case 3://专用发票
                    setInvoiceData(UIUtils.getString(R.string.invoice_special));
                    rl_electronic_invoice.setVisibility(View.GONE);
                    rl_paper_invoice.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            if ("1".equals(school_type)) {
                ll_order_invoice.setVisibility(View.VISIBLE);
            } else {
                ll_order_invoice.setVisibility(View.GONE);
            }
            if (StringUtils.isNullString(data.getRemark())) {
                ll_order_detail_remark.setVisibility(View.GONE);
            } else {
                ll_order_detail_remark.setVisibility(View.VISIBLE);
                tv_remark.setText(data.getRemark());
            }

            tv_order_detail_name.setText(data.getContact_name());
            tv_order_detail_mobile.setText(data.getContact_mobile());
            tv_order_detail_money.setText(
                    String.format(UIUtils.getString(R.string.how_much_money),
                            StringUtils.getDecimal((Double.valueOf(order2.get(0).getFee()) -
                                    (Double.valueOf(order2.get(0).getDevice_fee()))) + ""))
            );
            if (Double.valueOf(order2.get(0).getDevice_fee()) == 0) {
                rl_device.setVisibility(View.GONE);
            } else {
                rl_device.setVisibility(View.VISIBLE);
                tv_order_device_money.setText(String.format(UIUtils.getString(R.string.how_much_money),
                        StringUtils.getDecimal(order2.get(0).getDevice_fee())));
            }
            // 计算出优惠金额
            double couponfee = Double.valueOf(data.getFee())
                    - Double.valueOf(data.getReal_fee());
            if (couponfee > 0) {
                tv_privilege_price.setText(
                        String.format(UIUtils.getString(R.string.negative_how_much_money),
                                StringUtils.getDecimal(couponfee + "")));
            } else if (couponfee == 0) {
                rl_privilege_price.setVisibility(View.GONE);
                ll_order_detail_coupon.setVisibility(View.GONE);
            } else {
                tv_privilege_price.setText(
                        String.format(UIUtils.getString(R.string.negative_how_much_money),
                                StringUtils.getDecimal(Math.abs(couponfee) + "")));
            }
            //设置应付金额
            fee = data.getReal_fee();
            tv_ought_pay.setText(
                    String.format(
                            UIUtils.getString(R.string.how_much_money),
                            StringUtils.getDecimal(fee))
            );
            tv_order_time.setText(
                    String.format(
                            getString(R.string.txt_order_create_time),
                            second2Date(data.getCreate_time())
                    ));
            datas.clear();
            datas.addAll(data.getOrder2());

            if (datas == null || datas.size() == 0) {
                ProgressDialog.getInstance().dismiss();
                return;
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Long time = serverTime * 1000L;
            String current_date = format.format(time);

            if (orderDetailAdapter == null) {
                String coupon = "0";
                if (StringUtils.isNotNullString(data.getCoupon_id())) {
                    coupon = data.getCoupon_id();
                }
                orderDetailAdapter = new OrderDetailAdapter(this, current_date, datas,
                        currentStatus, coupon);
                lv_order_detail_lv.setAdapter(orderDetailAdapter);
            } else {
                orderDetailAdapter.setDatas(datas, currentStatus);
                orderDetailAdapter.notifyDataSetChanged();
            }

            rl_order_detail1.setVisibility(View.VISIBLE);
            sv_order_detail.setVisibility(View.VISIBLE);

            if (order2.size() > 0) {
                //设置要传到支付结果的bean
                conmmitInfo = new CartCommit();
                conmmitInfo.setTrade_id(data.getTrade_id());
                conmmitInfo.setUse_desc(order2.get(0).getUse_desc());
                conmmitInfo.setType_desc(order2.get(0).getType_desc());
                conmmitInfo.setRoom_count(Integer.valueOf(order2.get(0)
                        .getRoom_count()));
                conmmitInfo.setOrder1_id(Integer.valueOf(data.getId()));
                sid = order2.get(0).getSid();
                name = order2.get(0).getSname();
            }
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }
        ProgressDialog.getInstance().dismiss();
    }

    private void checkSchool() {
        if ("1".equals(school_type)) {
            tv_order_detail_conf_tips.setVisibility(View.GONE);
        } else {
            tv_order_detail_conf_tips.setVisibility(View.VISIBLE);
        }
    }

    private void setInvoiceData(String string) {
        ll_order_invoice.setVisibility(View.VISIBLE);
        tv_invoice_type.setText(string);
        tv_invoice_mc.setText(data.getMc());
        tv_invoice_xmmc.setText(data.getInvoice_xmmc());
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, PersonalCenterActivity.class);
//            intent.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_MY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                onBackPressed();
                break;
            //关闭订单
            case R.id.tv_close_order://删除订单和取消订单操作
                cancelOrder();
                break;
            //删除订单
            case R.id.tv_change_order:
                if (UIUtils.getString(R.string.order_close).equals(tv_change_order.getText().toString().trim())) {
                    cancelOrder();
                } else if (UIUtils.getString(R.string.order_delete).equals(tv_change_order.getText().toString().trim())) {
                    deleteOrder();
                } else {
                    payAfterCancel();
                }
                break;
            case R.id.tv_order_detail_status://订单状态
                Intent intent = new Intent(this, Common_Show_WebPage_Activity.class);
                intent.putExtra(UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.ORDER_LOG + "oid=" + oid);
                intent.putExtra(UIUtils.getString(R.string.get_page_name),
                        WebConstant.Order_Log_Page);
                startActivity(intent);
                break;
//            case R.id.tv_order_detail_money:
            // Intent intent2 = new Intent(this,
            // Common_Show_WebPage_Activity.class);
            // intent2.putExtra(UIUtils.getString(R.string.redirect_open_url),
            // UrlUtils.CLASSROOM_PRICE + "oid=" + oid + "&uid=" + uid);
            // intent2.putExtra(UIUtils.getString(R.string.get_page_name),
            // WebConstant.Classroom_Page);
            // startActivity(intent2);
//                break;
            case R.id.tv_rl_reimburse_prices://退款费用
                String uid = "-1".equals(StringUtils.getUid()) ? "" : StringUtils.getUid();
                Intent intent_web = new Intent(this,
                        Common_Show_WebPage_Activity.class);
                intent_web.putExtra(UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_REFUNDAMOUNT + "oid=" + oid + "&uid=" + uid);
                intent_web.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                        UIUtils.getString(R.string.txt_fees_refunds));
                startActivity(intent_web);
                break;

            case R.id.lr_order_detail_remind://押金详情
                Intent pledgeIntent = new Intent(this, Pledge_Activity.class);
                startActivity(pledgeIntent);
                break;
            case R.id.tv_pay_order:

                if ("1".equals(school_type)) {
                    mPopupWindow = PayWayUtil.getPopu(OrderDetailActivity.this.getWindow(),
                            OrderDetailActivity.this,
                            R.layout.activity_order_detail,
                            balance, fee, new PayWayOnClickListener() {
                                @Override
                                public void onBalanceClick() {
                                    mPopupWindow.dismiss();
                                    checkCoupon(false);
                                }

                                @Override
                                public void onOtherClick() {
                                    mPopupWindow.dismiss();
                                    if (StringUtils.isNullString(pay_method)) {
                                        changePayWay();
                                    } else if ("5".equals(pay_method)) {
                                        changePayWay();
                                    } else {
                                        checkCoupon(true);
                                    }
                                }
                            });
                } else {
                    checkCoupon(true);
                }

                break;
            case R.id.tv_order_detail_conf_tips:
                callPhone(store_tel);
                break;
            case R.id.tv_phone_number://客服电话
                // 弹出电话呼叫窗口
                callPhone(UIUtils.getString(R.string.txt_phone_number));
                break;
            /*case R.id.rl_price_detail:
                Intent intent_price = new Intent(
                        this,
                        Common_Show_WebPage_Activity.class);
                intent_price.putExtra(
                        UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_LISTFREE + "oid2=" + order2Info.getId());
                intent_price.putExtra(
                        UIUtils.getString(R.string.get_page_name),
                        WebConstant.Expense_list_Page);
                UIUtils.startActivity(intent_price);
                break;*/
            case R.id.tv_order_detail_money://课室费用
                Intent intent_store = new Intent(
                        this,
                        Common_Show_WebPage_Activity.class);
                intent_store.putExtra(
                        UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_STORE_PRICE + "oid2=" + order2Info.getId());
                intent_store.putExtra(
                        UIUtils.getString(R.string.get_page_name),
                        WebConstant.Expense_list_Page);
                intent_store.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "课室费用明细");
                UIUtils.startActivity(intent_store);
                break;
            case R.id.tv_order_device_money://收费设备费用
                Intent intent_device = new Intent(
                        this,
                        Common_Show_WebPage_Activity.class);
                intent_device.putExtra(
                        UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_DEVICE_PRICE + "oid2=" + order2Info.getId());
                intent_device.putExtra(
                        UIUtils.getString(R.string.get_page_name),
                        WebConstant.Expense_list_Page);
                intent_device.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "收费设备费用明细");
                UIUtils.startActivity(intent_device);
                break;
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    wifi.setVisibility(View.GONE);
                    getHttpUtils();
                } else {
                    wifi.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_electronic_invoice:
                //下载发票
                downLoadInvoice();
                break;
            default:
                break;
        }
    }

    public void callPhone(final String tel) {
        CustomDialog customDialog_phone = new CustomDialog(
                OrderDetailActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                tel
        );
        customDialog_phone.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    if (!PermissionsChecker.checkPermission(PermissionsChecker.CALL_PHONE_PERMISSIONS)) {
                        // 跳转系统电话
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:" + tel));
                        startActivity(intent);
                    } else {
                        PermissionsChecker.requestPermissions(
                                OrderDetailActivity.this,
                                PermissionsChecker.CALL_PHONE_PERMISSIONS
                        );
                    }
                }
            }
        });
    }

    private void commitOrder() {
        //提交订单
        switch (currentNum) {
            case 0:
                //支付宝支付
                parseData();
                break;
            case 1:
                //微信支付
                UIUtils.showToastSafe(UIUtils.getString(R.string.txt_weixin_pay));
                break;
        }
    }

    private void checkCoupon(final boolean flag) {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "verify_order_coupon");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order1_id", data.getId());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ProgressDialog.getInstance().dismiss();
                        // 处理返回的数据
                        JSONTokener jsonParser = new JSONTokener(arg0.result);
                        JSONObject json;
                        int code = Integer.MAX_VALUE;
                        try {
                            json = (JSONObject) jsonParser.nextValue();
                            code = json.getInt("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code == 50050) {
                            CustomDialog customDialog = new CustomDialog(
                                    OrderDetailActivity.this,
                                    UIUtils.getString(R.string.confirm),
                                    UIUtils.getString(R.string.label_cancel),
                                    UIUtils.getString(R.string.dialog_show_coupon_use));
                            customDialog.setOnClickListener(new IOnClickListener() {
                                @Override
                                public void oncClick(boolean isOk) {
                                    if (isOk) {
                                        cancelCoupon();
                                    }
                                }
                            });
                        } else if (code == 1) {
                            JSONTokener jsonParser2 = new JSONTokener(arg0.result);
                            JSONObject json2;
                            String trade_id = null;
                            try {
                                json2 = (JSONObject) jsonParser2.nextValue();
                                trade_id = json2.getString("trade_id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (flag) {
                                if (!StringUtils.isNullString(trade_id)) {
                                    conmmitInfo.setTrade_id(trade_id);
                                }
                                commitOrder();
                            } else {
                                checkPassword();
                            }
                        }
                    }
                });
    }

    /**
     * 切换支付方式
     */
    private void changePayWay() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "switch_paymethod");
        params.addBodyParameter("order1_id", oid);
        params.addBodyParameter("uid", StringUtils.getUid());
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("pay_method", "6");
        httpUtils.send(HttpRequest.HttpMethod.POST,
                UrlUtils.SERVER_USER_COUPON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject json;
                        try {
                            json = new JSONObject(arg0.result);
                            String code = json.getString("code");
                            String trade_id = json.getString("trade_id");
                            if ("1".equals(code)) {
                                conmmitInfo.setTrade_id(trade_id);
                                checkCoupon(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void cancelCoupon() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "return_order_coupon");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order1_id", data.getId());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        ProgressDialog.getInstance().dismiss();
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
                            ProgressDialog.getInstance().dismiss();
                            coupon_id = 0;
                            rl_privilege_price.setVisibility(View.GONE);
                            tv_ought_pay.setText(String.format(
                                    UIUtils.getString(R.string.how_much_money),
                                    data.getFee()
                            ));
                            if (!StringUtils.isNullString(tid)) {
                                conmmitInfo.setTrade_id(tid);
                            }
                        }
                    }
                });
    }

    /**
     * 下载发票至本地
     */
    private void downLoadInvoice() {
        String currentDate = DateUtil.getCurrentDate("yyyy-MM-dd");
        String target = Environment.getExternalStorageDirectory()
                + "/clock/" + "时钟教室发票(" + data.getId() + ")_" + currentDate.replace("-", "") + ".pdf";

        File file = new File(target);
        if (file.exists()) {
            file.delete();
        }
        HttpUtils http = new HttpUtils();

        // 让ProgressDialog显示
        download = http.download(url, target, true,
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> arg0) {
                        UIUtils.showToastSafe("已保存到./clock 文件夹下");
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(arg0.getExceptionCode() + ":" + arg1);

                    }
                });
//        }
    }

    private void check(int num) {
        for (int i = 0; i < cbs.size(); i++) {
            if (i == num) {
                cbs.get(i).setChecked(true);
            } else {
                cbs.get(i).setChecked(false);
            }
        }
    }

    /**
     * 支付宝支付
     */
    private void parseData() {
        //判断之前是否初始化
        EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
            @Override
            public void onSuccess() {
                //调取易支付创建订单报文签名接口
                getRequestCreateOrder();
            }
        });
        //支付宝支付相关处理
//        getAliPaySign();
    }

    /**
     * 请求 获取创建订单报文签名 接口
     */
    private void getRequestCreateOrder() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "create_order");
        params.addBodyParameter("trade_id", conmmitInfo.getTrade_id());
        params.addBodyParameter("terminalType", "SDK");
        if (StringUtils.isNotNullString(school_type)) {
            if (school_type.equals("1")) {
                params.addBodyParameter("businessId", "23");
            } else if (school_type.equals("2")) {
                params.addBodyParameter("businessId", "22");
            }
            params.addBodyParameter("cid", "0");
        }
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
                        processDataForCreateOrder(arg0.result);
                    }
                }
        );
    }

    private void processDataForCreateOrder(String result) {
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
                    OrderDetailActivity.this,
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
                                    Intent intentSucess = new Intent(
                                            OrderDetailActivity.this,
                                            AliPay_PayOKActivity.class);
                                    intentSucess.putExtra("sucess", conmmitInfo);
                                    intentSucess.putExtra("sid", sid);
                                    intentSucess.putExtra("name", name);
                                    intentSucess.putExtra("order2", datas);
                                    startActivity(intentSucess);
                                } else {
                                    //支付后确认,不可以布置课室
                                    Intent intent = new Intent(OrderDetailActivity.this,
                                            NewPayResultActivity.class);
                                    intent.putExtra("conmmitInfo", conmmitInfo);
                                    intent.putExtra("type", 2);
                                    startActivity(intent);
                                }

                            } else if (code == EjuPayResultCode.PAY_FAIL_CODE.getCode()) {
                                //支付失败
                                Intent intentFail = new Intent(
                                        OrderDetailActivity.this,
                                        AliPay_PayFailActivity.class);
                                intentFail.putExtra("fail", conmmitInfo);
                                intentFail.putExtra("sid", sid);
                                intentFail.putExtra("name", name);
                                startActivity(intentFail);
                            } else if (code == EjuPayResultCode.PAY_CANCEL_C0DE.getCode()) {
                                //支付取消
                                Intent intentFail = new Intent(
                                        OrderDetailActivity.this,
                                        AliPay_PayFailActivity.class);
                                intentFail.putExtra("fail", conmmitInfo);
                                intentFail.putExtra("sid", sid);
                                intentFail.putExtra("name", name);
                                startActivity(intentFail);
                            }
                        }
                    }
            );
        }
    }

    private void checkPassword() {
        EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
            @Override
            public void onSuccess() {
                // 调取易支付创建订单报文签名接口
                if (EjuPayManager.getInstance().isUserSetPwd()) {
                    //跳转输入密码页面
                    ProgressDialog.getInstance().dismiss();
                    mPopupWindow.dismiss();
                    jumpPayPage();

                } else {
                    //跳转设置支付密码页面
//                    EjuPayManager.getInstance().startChangePassWord(UIUtils.getContext());
                    ProgressDialog.getInstance().dismiss();
                    EjuPayManager.getInstance().setEjuPayResult(new IEjuPayResult() {
                        @Override
                        public void callResult(int code, String msg, String dataJson) {
                            if (code == EjuPayResultCode.SetPassWord_SUCCESS_CODE.getCode()) {
                                LogUtil.d("test", "支付密码设置成功");
                                jumpPayPage();
                            } else if (code == EjuPayResultCode.SetPassWord_FAIL_CODE.getCode()) {
                                LogUtil.d("test", "支付密码设置失败");
                            }

                        }
                    });

                }
            }
        });
    }

    private void jumpPayPage() {
        Intent intent = new Intent(OrderDetailActivity.this, PayPasswordActivity.class);
        intent.putExtra("conmmitInfo", conmmitInfo);
        intent.putExtra("sid", sid);
        intent.putExtra("name", name);
        intent.putExtra("order2", order2);
        startActivity(intent);
    }

    @SuppressLint("SimpleDateFormat")
    private String second2Date(String time) {
        Date today = new Date(Long.valueOf(time + "000"));
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(today);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeMessages(0);
        }
        if (isTaskRoot()) {
            Intent intent = new Intent(this, PersonalCenterActivity.class);
//            intent.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_MY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onDestroy();
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        CustomDialog customDialog = new CustomDialog(
                OrderDetailActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_close_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    getHttpUtils2();
                }
            }
        });
    }

    // 请求取消
    private void getHttpUtils2() {
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
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
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
            // 修改状态
            tv_order_detail_status.setText(UIUtils.getString(R.string.status_close));
            // 隐藏取消订单
            ll_title_green.setVisibility(View.GONE);
            rl_order_detail_pay.setVisibility(View.VISIBLE);
            tv_close_order.setVisibility(View.GONE);
            tv_pay_order.setVisibility(View.GONE);
            tv_change_order.setVisibility(View.VISIBLE);
            ll_order_overtime.setVisibility(View.GONE);
            ll_order_total.setVisibility(View.GONE);
            ll_detail_pay.setVisibility(View.GONE);
            ll_order_detail_coupon.setVisibility(View.VISIBLE);

            CustomDialog customDialog = new CustomDialog(
                    OrderDetailActivity.this,
                    mineOrder.getMsg());
            customDialog.setOnClickListener(new IOnClickListener() {
                @Override
                public void oncClick(boolean isOk) {
                    if (isOk) {
                        OrderDetailActivity.this.setResult(RESULT_OK);
                        finish();
                    }
                }
            });
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }

    /**
     * 支付后确认的取消订单
     */
    private void payAfterCancel() {
        CustomDialog customDialog = new CustomDialog(
                OrderDetailActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_cancel_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    // 请求删除
                    getHttpUtils4();
                }
            }
        });
    }

    /**
     * 删除订单
     */
    private void deleteOrder() {
        CustomDialog customDialog = new CustomDialog(
                OrderDetailActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_delete_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    // 请求删除
                    getHttpUtils3();
                }
            }
        });
    }

    private void getHttpUtils3() {
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
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
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
            CustomDialog customDialog = new CustomDialog(
                    OrderDetailActivity.this,
                    mineOrder.getMsg());
            customDialog.setOnClickListener(new IOnClickListener() {
                @Override
                public void oncClick(boolean isOk) {
                    if (isOk) {
                        OrderDetailActivity.this.setResult(RESULT_OK);
                        finish();
                    }
                }
            });
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }

    private void getHttpUtils4() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "unconfirm_after_pay");
        params.addBodyParameter("id", oid);
        params.addBodyParameter("is_app", "1");
        String md5id = null;
        try {
            md5id = MD5.md5(oid + "|" + Md5Key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isNullString(md5id)) {
            params.addBodyParameter("md5id", md5id);
        }
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData4(arg0.result);

                    }
                });
    }

    private void processData4(String result) {
        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject json;
        int code = Integer.MAX_VALUE;
        try {
            json = (JSONObject) jsonParser.nextValue();
            code = json.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (code == 1) {
            CustomDialog customDialog = new CustomDialog(
                    OrderDetailActivity.this,
                    "取消成功");
            customDialog.setOnClickListener(new IOnClickListener() {
                @Override
                public void oncClick(boolean isOk) {
                    if (isOk) {
                        OrderDetailActivity.this.setResult(RESULT_OK);
                        finish();
                    }
                }
            });
        } else {
            UIUtils.showToastSafe(getString(R.string.toast_cancel_failed));
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_order_detail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getHttpUtilsResult();
        }
        if (resultCode == ClassroomArrangementActivity.RESULT_CODE_FROM_CLASSROOM_ARRANGEMENT_ACT) {
            getHttpUtils();
        }
    }

    //修改设备后，重新刷新数据
    private void getHttpUtilsResult() {
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

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        ProgressDialog.getInstance().dismiss();

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataResult(arg0.result);

                    }
                });
    }

    private void processDataResult(String result) {
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);

        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {

            datas.clear();
            datas.addAll(mineOrder.getData().get(0).getOrder2());
            orderDetailAdapter.notifyDataSetChanged();
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
//        } else {
            UIUtils.showToastSafe(getString(R.string.toast_permission_call_phone));
        }
    }

}
