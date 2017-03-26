package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ejupay.sdk.EjuPayManager;
import com.ejupay.sdk.service.EjuPayResultCode;
import com.ejupay.sdk.service.IPayResult;
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
import com.yiju.ClassClockRoom.adapter.ShopCartAdapter;
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.bean.CartFive;
import com.yiju.ClassClockRoom.bean.CartSeven;
import com.yiju.ClassClockRoom.bean.CartSeven.DataEntity;
import com.yiju.ClassClockRoom.bean.ContactTelName;
import com.yiju.ClassClockRoom.bean.CouponUse;
import com.yiju.ClassClockRoom.bean.CreateOrderResult;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.OrderCancel;
import com.yiju.ClassClockRoom.bean.OrderStatus;
import com.yiju.ClassClockRoom.bean.OrderStatus.OrderDesc;
import com.yiju.ClassClockRoom.bean.ShopCart;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.control.FragmentFactory;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.LogUtil;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.ListViewForScrollView;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopCartActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "shopCart";

    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

    // 待支付订单布局隐藏/显示,设置点击事件
    @ViewInject(R.id.rl_need_gone)
    private RelativeLayout rl_need_gone;
    // 3笔订单待支付
    @ViewInject(R.id.tv_cart_all)
    private TextView tv_cart_all;
    // 倒计时
    @ViewInject(R.id.tv_cart_all_three)
    private TextView tv_cart_all_three;
    // 随待支付布局一块隐藏/显示
    @ViewInject(R.id.view)
    private View view;

    @ViewInject(R.id.list_cart)
    private ListViewForScrollView list_cart;

    // 订单备注，选填内容
    @ViewInject(R.id.et_request_no_must)
    private EditText et_request_no_must;

    // 联系方式，必填,设置点击事件
    @ViewInject(R.id.rl_name_tel)
    private RelativeLayout rl_name_tel;
    // 联系人姓名
    @ViewInject(R.id.tv_cart_order_name)
    private TextView tv_cart_order_name;
    // 电话
    @ViewInject(R.id.tv_cart_order_tel)
    private TextView tv_cart_order_tel;

    // 优惠券，设置点击事件
    @ViewInject(R.id.rl_coupon)
    private RelativeLayout rl_coupon;
    // 优惠金额例：-￥0.00
    @ViewInject(R.id.tv_coupon_price)
    private TextView tv_coupon_price;

    // 课室费用额例：￥0.00
    @ViewInject(R.id.tv_class_price)
    private TextView tv_class_price;

    // 应付金额例：￥0.00
    @ViewInject(R.id.tv_should_price)
    private TextView tv_should_price;

    // 支付宝，设置点击事件
    @ViewInject(R.id.rl_pay_way)
    private RelativeLayout rl_pay_way;
    // 支付宝，点击之后换图片
    @ViewInject(R.id.iv_pay_way)
    private ImageView iv_pay_way;

    // 微信，设置点击事件
    @ViewInject(R.id.rl_wxpay_way)
    private RelativeLayout rl_wxpay_way;
    // 微信，点击之后换图片
    @ViewInject(R.id.iv_wxpay_way)
    private ImageView iv_wxpay_way;

    // 全选,扩大点击区域
    @ViewInject(R.id.rl_cart_all)
    private RelativeLayout rl_cart_all;

    // 全选，设置点击事件，点击之后换图片
    @ViewInject(R.id.iv_cart_all)
    private ImageView iv_cart_all;

    // 合计 例：￥0.00
    @ViewInject(R.id.tv_cart_price)
    private TextView tv_cart_price;

    // 提交，设置点击事件，三个必选条件不满足灰色，满足橙色,点了上面删除的时候，按钮变为删除
    @ViewInject(R.id.rl_cart)
    private RelativeLayout rl_cart;
    // 提交，设置点击事件，三个必选条件不满足灰色，满足橙色,点了上面删除的时候，按钮变为删除
    @ViewInject(R.id.tv_cart)
    private TextView tv_cart;

    // 购物车为空时，隐藏该页面
    @ViewInject(R.id.sl)
    private ScrollView sl;
    // 购物车为空时，隐藏该页面
    @ViewInject(R.id.rl_empty_visible)
    private RelativeLayout rl_empty_visible;

    // 购物车为空时，显示该页面(没有待支付订单)
    @ViewInject(R.id.ll_empty_cart_nopay)
    private LinearLayout ll_empty_cart_nopay;

    // 购物车为空，前去预订
    @ViewInject(R.id.bt_empty_go_buy)
    private Button bt_empty_go_buy;

    // 购物车为空时，显示该页面(存在待支付订单)
    @ViewInject(R.id.ll_empty_cart_pay)
    private LinearLayout ll_empty_cart_pay;

    // 购物车为空，存在待支付订单，继续预订
    @ViewInject(R.id.bt_empty_go_on)
    private Button bt_empty_go_on;

    // 到店需另付押金(xx元)
    @ViewInject(R.id.deposit)
    private TextView deposit;

    private String uid;
    private String sid;
    private ShopCartAdapter adapter;
    // 定义一个变量来记录总价
    private float price;
    private List<Order2> mList = new ArrayList<>();
    private BaseApplication bb;
    // 定义一个变量来记录支付是否选择
    private boolean zfCheck = true;
    // 定义一个变量来记录微信支付是否选择
    private boolean wxCheck = true;
    private Map<String, Integer> order;
    private String coupon_id;
    private CartCommit conmmitInfo;
    private long time;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    time--;
                    if (time >= 0) {

                        tv_cart_all_three.setText(String.format(
                                UIUtils.getString(R.string.minute_second),
                                String.valueOf(time / (60)), String.valueOf(time % 60)));

                        handler.sendEmptyMessageDelayed(0, 1000);
                    } else {
                        rl_need_gone.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        ll_empty_cart_pay.setVisibility(View.GONE);
                    }
                    break;

                default:
                    break;
            }

        }
    };
    private String name;
    private int newIndex;
    private List<Order2> yesOrders = new ArrayList<>();
    private int position = 0;
    private ContactTelName contact;
    private String id;
    //传值给支付结果页
    private ArrayList<Object> order2s;

    @Override
    public int setContentViewId() {
        return R.layout.activity_show_cart;
    }

    /**
     * 初始化页面
     */
    @Override
    public void initView() {
        head_back_relative.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
        rl_cart_all.setOnClickListener(this);
        rl_cart.setOnClickListener(this);
        rl_pay_way.setOnClickListener(this);
//		rl_wxpay_way.setOnClickListener(this);
        rl_coupon.setOnClickListener(this);
        rl_name_tel.setOnClickListener(this);
        rl_need_gone.setOnClickListener(this);
        bt_empty_go_buy.setOnClickListener(this);
        bt_empty_go_on.setOnClickListener(this);
        deposit.setOnClickListener(this);
        list_cart.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 点击进入编辑页面
                Order2 info = mList.get(arg2);
                Intent intent = new Intent(ShopCartActivity.this, OrderEditDetailActivity.class);
                intent.putExtra("order2", info);
                intent.putExtra("position", arg2);
                startActivityForResult(intent, 0);
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        newIndex = getIntent().getIntExtra("newIndex", 1);
        getIntent().getStringExtra("device_str");
        uid = SharedPreferencesUtils.getString(this, "id", "");
        String actionBefore = "no_pay_count";
        refresh(actionBefore);
        String action = "show_cart";
        requestData(action, uid);
        bb = (BaseApplication) getApplicationContext();

        head_title.setText(UIUtils.getString(R.string.title_shop_car));
        head_right_text.setText(UIUtils.getString(R.string.delete));
    }

    private void refresh(String actionBefore) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", actionBefore);
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_COUPON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        LogUtil.d(TAG, "是否显示红色条目+++++++++++" + arg0.result);
                        OrderStatus info = GsonTools.changeGsonToBean(
                                arg0.result, OrderStatus.class);
                        if (null != info) {
                            if (info.getCode() == 1
                                    && info.getMsg().equals("ok")) {
                                OrderDesc dataInfo = info.getData();
                                if (dataInfo.getCount().equals("0")) {
                                    rl_need_gone.setVisibility(View.GONE);
                                    view.setVisibility(View.GONE);
                                    ll_empty_cart_pay.setVisibility(View.GONE);
                                } else {
                                    rl_need_gone.setVisibility(View.VISIBLE);
                                    view.setVisibility(View.VISIBLE);
                                    ll_empty_cart_nopay.setVisibility(View.GONE);
                                    tv_cart_all.setText(String.format(
                                            UIUtils.getString(R.string.sum_order_no_pay),
                                            dataInfo.getCount()));
                                    time = Long.valueOf(dataInfo
                                            .getExpire_time())
                                            - (long) dataInfo
                                            .getNowtime();
                                    handler.sendEmptyMessage(0);
                                }

                            }
                        }
                    }
                });
    }

    /**
     * 网络请求
     *
     * @param action 请求action
     * @param uid    用户id
     */
    private void requestData(String action, String uid) {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_RESERVATION, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        ProgressDialog.getInstance().dismiss();

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);
                        LogUtil.d(TAG, "+++++++++++++++++++++++++json"
                                + arg0.result);
                    }

                });
    }

    /**
     * 处理数据
     *
     * @param result 结果集
     */
    private void processData(String result) {
        ShopCart info = GsonTools.changeGsonToBean(result, ShopCart.class);
        LogUtil.d(TAG, "订单删除前、、、、、、、、、、、、、、、、、" + result);
        if (null != info) {
            if ("1".equals(info.getCode()) && info.getMsg().equals("ok")) {
                contact = info.getContact();
                id = contact.getId();
                if (!"".equals(contact.getName())) {
                    tv_cart_order_name.setText(contact.getName());
                    tv_cart_order_tel.setText(contact.getMobile());
                    tv_cart_order_tel.setTextColor(UIUtils
                            .getColor(R.color.color_gay_8f));
                }
                List<Order2> datas = info.getData();
                if (datas.size() > 0) {
                    sl.setVisibility(View.VISIBLE);
                    rl_empty_visible.setVisibility(View.VISIBLE);
                    ll_empty_cart_nopay.setVisibility(View.GONE);
                    ll_empty_cart_pay.setVisibility(View.GONE);
                    mList.clear();
                    mList.addAll(datas);
                    saveCount();
                    float allPrice = 0;

                    if (datas.size() > 0) {
                        if (adapter == null) {
                            if (newIndex == 1) {
                                int co = 0;
                                yesOrders.clear();
                                for (int i = 0; i < mList.size(); i++) {
                                    if (mList.get(i).getIs_valid().equals("1")) {
                                        mList.get(i).setCheck(true);
                                        yesOrders.add(mList.get(i));
                                        allPrice += Float.valueOf(mList.get(i)
                                                .getFee());
                                        co++;
                                    }
                                }
                                bb.setCount(co);
                                if (yesOrders.size() == 0) {
                                    iv_cart_all
                                            .setBackgroundResource(R.drawable.order_nonechoose_btn);
                                } else {
                                    iv_cart_all
                                            .setBackgroundResource(R.drawable.order_choose_btn);
                                }
                                bb.setPrice(allPrice);
                                tv_class_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), allPrice));
                                tv_should_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), allPrice));
                                tv_cart_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), allPrice));
//                                allPrice = 0;

                            } else if (newIndex == 0) {
                                int co = Integer.MAX_VALUE;
                                yesOrders.clear();
                                for (int i = 0; i < mList.size(); i++) {
                                    if (mList.get(i).getIs_valid().equals("1")) {
                                        yesOrders.add(mList.get(i));
                                    }
                                }
                                for (int i = 0; i < mList.size(); i++) {
                                    if (mList.get(i).getIs_valid().equals("1")) {
                                        co = i;
                                        break;
                                    }
                                }
                                if (co != Integer.MAX_VALUE) {
                                    bb.setCount(1);
                                    if (yesOrders.size() == 1) {
                                        iv_cart_all
                                                .setBackgroundResource(R.drawable.order_choose_btn);
                                    } else {
                                        bb.setCheck(false);
                                    }
                                    bb.setPrice(Float.valueOf(mList.get(co)
                                            .getFee()));
                                    tv_class_price.setText(String.format(
                                            UIUtils.getString(R.string.rmb_float_zero),
                                            Float.valueOf(mList.get(co).getFee())));
                                    tv_should_price.setText(String.format(
                                            UIUtils.getString(R.string.rmb_float_zero),
                                            Float.valueOf(mList.get(co).getFee())));
                                    tv_cart_price.setText(String.format(
                                            UIUtils.getString(R.string.rmb_float_zero),
                                            Float.valueOf(mList.get(co).getFee())));
                                    mList.get(co).setCheck(true);
                                }
                            }
                            adapter = new ShopCartAdapter(this, mList,
                                    iv_cart_all, tv_cart_price,
                                    tv_coupon_price, head_right_text,
                                    tv_class_price, tv_should_price);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        list_cart.setAdapter(adapter);
                    }
                } else {
                    jumpEmptyCart();
                }
            }
        }
        ProgressDialog.getInstance().dismiss();
    }

    /**
     * 跳转空购物车界面
     */
    private void jumpEmptyCart() {
        head_right_text.setVisibility(View.GONE);
        sl.setVisibility(View.GONE);
        rl_empty_visible.setVisibility(View.GONE);
        if (rl_need_gone.getVisibility() == View.VISIBLE) {
            // 存在待支付订单
            ll_empty_cart_nopay.setVisibility(View.GONE);
            if (sl.getVisibility() == View.GONE) {
                ll_empty_cart_pay.setVisibility(View.VISIBLE);
            }
        } else {
            // 没有待支付订单
            ll_empty_cart_nopay.setVisibility(View.VISIBLE);
            if (sl.getVisibility() == View.GONE && ll_empty_cart_nopay.getVisibility() == View.GONE) {
                ll_empty_cart_pay.setVisibility(View.VISIBLE);
            } else {
                ll_empty_cart_pay.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // 设置点击事件
        switch (v.getId()) {
            case R.id.head_back_relative:
//                backPress();
                onBackPressed();
                break;
            case R.id.head_right_text:
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                String delete = head_right_text.getText().toString();
                yesOrders.clear();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getIs_valid().equals("1")) {
                        yesOrders.add(mList.get(i));
                    }
                }
                if (delete.equals(UIUtils.getString(R.string.delete))) {
                    int c = 0;
                    float price = 0;
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).getCheck()) {
                            c++;
                            price += Float.valueOf(mList.get(i).getFee());
                        }
                    }
                    tv_class_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero),
                            price));
                    tv_should_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero),
                            price));
                    tv_cart_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero),
                            price));
                    bb.setCount(c);
                    if (c == 0) {
                        iv_cart_all
                                .setBackgroundResource(R.drawable.order_nonechoose_btn);
                    } else if (c == mList.size()) {
                        iv_cart_all
                                .setBackgroundResource(R.drawable.order_choose_btn);
                    } else {
                        iv_cart_all
                                .setBackgroundResource(R.drawable.order_nonechoose_btn);
                    }
                    head_right_text.setText(UIUtils.getString(R.string.label_finish));
                    tv_cart.setText(UIUtils.getString(R.string.delete));
                } else {
                    int c = 0;
                    float price = 0;
                    yesOrders.clear();
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).getIs_valid().equals("1")) {
                            yesOrders.add(mList.get(i));
                            if (mList.get(i).getCheck()) {
                                c++;
                                price += Float.valueOf(mList.get(i).getFee());
                            }
                        }
                    }
                    bb.setCount(c);
                    tv_class_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero),
                            price));
                    tv_should_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero),
                            price));
                    tv_cart_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero),
                            price));
                    if (c == 0) {
                        iv_cart_all
                                .setBackgroundResource(R.drawable.order_nonechoose_btn);
                    } else if (c == yesOrders.size()) {
                        iv_cart_all
                                .setBackgroundResource(R.drawable.order_choose_btn);
                    } else {
                        iv_cart_all
                                .setBackgroundResource(R.drawable.order_nonechoose_btn);
                    }
                    head_right_text.setText(UIUtils.getString(R.string.delete));
                    tv_cart.setText(UIUtils.getString(R.string.txt_commit));
                }
                tv_coupon_price.setText(UIUtils.getString(R.string.rmb_equals_negative_zero));
                adapter.notifyDataSetChanged();
                break;

            case R.id.rl_cart_all:
                if (adapter == null) {
                    UIUtils.showToastSafe(UIUtils.getString(R.string.toast_no_order));
                } else {
                    if (tv_cart.getText().toString().equals(UIUtils.getString(R.string.delete))) {
                        // 删除状态
                        adapter.notifyDataSetChanged();
                        int cCount = 0;
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getCheck()) {
                                cCount++;
                            }
                        }
                        if (cCount == mList.size()) {
                            bb.setCount(0);
                            iv_cart_all
                                    .setBackgroundResource(R.drawable.order_nonechoose_btn);
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).setCheck(false);
                                bb.getmOrder().put(
                                        String.valueOf(mList.get(i).getId())
                                                + "a"
                                                + String.valueOf(mList.get(i)
                                                .getSid()), 0);
                            }
                            tv_class_price.setText(UIUtils.getString(R.string.rmb_equals_zero));
                            tv_should_price.setText(UIUtils.getString(R.string.rmb_equals_zero));
                            tv_cart_price.setText(UIUtils.getString(R.string.rmb_equals_zero));
                            tv_coupon_price.setText(UIUtils.getString(R.string.rmb_equals_negative_zero));
                            bb.setPrice(0);
                        } else {
                            bb.setCount(mList.size());
                            iv_cart_all
                                    .setBackgroundResource(R.drawable.order_choose_btn);
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).setCheck(true);
                                bb.getmOrder().put(
                                        String.valueOf(mList.get(i).getId())
                                                + "a"
                                                + String.valueOf(mList.get(i)
                                                .getSid()), 1);
                                price += Float.valueOf(mList.get(i).getFee());
                            }
                            tv_class_price.setText(String.format(
                                    UIUtils.getString(R.string.rmb_float_zero),
                                    price));

                            tv_should_price.setText(String.format(
                                    UIUtils.getString(R.string.rmb_float_zero),
                                    price));
                            tv_cart_price.setText(String.format(
                                    UIUtils.getString(R.string.rmb_float_zero),
                                    price));
                            bb.setPrice(price);
                            price = 0;
                            changeAllPrice();
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // 提交状态
                        adapter.notifyDataSetChanged();
                        int cCount = 0;
                        yesOrders.clear();
                        ArrayList<Integer> index = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getIs_valid().equals("1")) {
                                yesOrders.add(mList.get(i));
                                index.add(i);
                            }
                        }
                        for (int i = 0; i < yesOrders.size(); i++) {
                            if (yesOrders.get(i).getCheck()) {
                                cCount++;
                            }
                        }
                        if (cCount == yesOrders.size()) {
                            bb.setCount(0);
                            iv_cart_all
                                    .setBackgroundResource(R.drawable.order_nonechoose_btn);
                            for (int i = 0; i < yesOrders.size(); i++) {
                                mList.get(index.get(i)).setCheck(false);
                                bb.getmOrder().put(
                                        String.valueOf(mList.get(index.get(i))
                                                .getId())
                                                + "a"
                                                + String.valueOf(mList.get(
                                                index.get(i)).getSid()), 0);
                            }
                            tv_class_price.setText(UIUtils.getString(R.string.rmb_equals_zero));
                            tv_should_price.setText(UIUtils.getString(R.string.rmb_equals_zero));
                            tv_cart_price.setText(UIUtils.getString(R.string.rmb_equals_zero));
                            tv_coupon_price.setText(UIUtils.getString(
                                    R.string.rmb_equals_negative_zero));
                            bb.setPrice(0);
                        } else {
                            bb.setCount(yesOrders.size());
                            iv_cart_all
                                    .setBackgroundResource(R.drawable.order_choose_btn);
                            for (int i = 0; i < yesOrders.size(); i++) {
                                mList.get(index.get(i)).setCheck(true);
                                bb.getmOrder().put(
                                        String.valueOf(mList.get(index.get(i))
                                                .getId())
                                                + "a"
                                                + String.valueOf(mList.get(
                                                index.get(i)).getSid()), 1);
                                price += Float.valueOf(mList.get(index.get(i))
                                        .getFee());
                            }
                            tv_class_price.setText(String.format(
                                    UIUtils.getString(R.string.rmb_float_zero),
                                    price));
                            tv_should_price.setText(String.format(
                                    UIUtils.getString(R.string.rmb_float_zero),
                                    price));
                            tv_cart_price.setText(String.format(
                                    UIUtils.getString(R.string.rmb_float_zero),
                                    price));
                            bb.setPrice(price);
                            price = 0;
                            changeAllPrice();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.rl_cart:
                String bt = tv_cart.getText().toString();
                if (bt.equals(UIUtils.getString(R.string.delete))) {
                    List<Integer> orders = haveOrder();
                    if (orders.size() > 0) {
                        CustomDialog customDialog = new CustomDialog(
                                ShopCartActivity.this,
                                UIUtils.getString(R.string.confirm),
                                "想想再说",
                                UIUtils.getString(R.string.dialog_msg_for_delete_classroom));
                        customDialog.setOnClickListener(new IOnClickListener() {
                            @Override
                            public void oncClick(boolean isOk) {
                                if (isOk) {
                                    String oid;
                                    String sid;
                                    List<String> idList = new ArrayList<>();
                                    StringBuilder o_sb = new StringBuilder();
                                    StringBuilder s_sb = new StringBuilder();
                                    if (mList == null) {
                                        return;
                                    }
                                    if (bb.getCount() == mList.size()) {
                                        for (int i = 0; i < mList.size(); i++) {
                                            idList.add(mList.get(i).getId() + "a"
                                                    + mList.get(i).getSid());
                                        }
                                        mList.clear();
                                    } else {
                                        List<Integer> indexArr = new ArrayList<>();
                                        for (int j = 0; j < mList.size(); j++) {
                                            if (mList.get(j).getCheck()) {
                                                idList.add(mList.get(j).getId()
                                                        + "a"
                                                        + mList.get(j).getSid());
                                                indexArr.add(j);
                                            }
                                        }
                                        if (indexArr.size() <= 0) {
                                            return;
                                        }
                                        int indexArrSize = indexArr.size();
                                        for (int i = 0; i < indexArrSize; i++) {
                                            int index = indexArr.get(i);
                                            mList.remove(index);
                                            i--;
                                            indexArrSize--;
                                        }
                                    }
                                    saveCount();
                                    if (idList.size() <= 0) {
                                        return;
                                    }
                                    for (int i = 0; i < idList.size(); i++) {
                                        String[] idSid = idList.get(i).split("a");
                                        String o_id = idSid[0];
                                        String s_id = idSid[1];
                                        if (i == idList.size() - 1) {
                                            o_sb.append(o_id);
                                            s_sb.append(s_id);
                                        } else {
                                            o_sb.append(o_id).append(",");
                                            s_sb.append(s_id).append(",");
                                        }
                                    }
                                    oid = o_sb.toString();
                                    sid = s_sb.toString();
                                    cancelOrder(oid, sid);
                                    bb.setCount(0);
                                    bb.setPrice(0);
                                    tv_class_price.setText(
                                            UIUtils.getString(R.string.rmb_equals_zero));
                                    tv_should_price.setText(
                                            UIUtils.getString(R.string.rmb_equals_zero));
                                    tv_cart_price.setText(
                                            UIUtils.getString(R.string.rmb_equals_zero));
                                    if (mList != null && mList.size() > 0) {
                                        for (int i = 0; i < mList.size(); i++) {
                                            mList.get(i).setCheck(false);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    bb.setPrice(0);
                                    changeAllPrice();
                                    if (mList != null && mList.size() == 0) {
                                        jumpEmptyCart();
                                    }
                                }
                            }
                        });
                    } else {
                        UIUtils.showToastSafe(getString(R.string.toast_select_delete_order));
                    }

                } else {
                    // 提交
                    List<Integer> orders = haveOrder();

                    if (orders.size() > 0) {
                        if (zfCheck) {
                            if (tv_cart_order_tel.getText().toString().equals(getString(R.string.mustfill))) {
                                showSmileToast(getString(R.string.toast_selecta_contact));
                                // UIUtils.showToastSafe("请选择联系方式");
                                telWay();
                            } else {
                                String remark = et_request_no_must.getText()
                                        .toString();
                                String contact_name = tv_cart_order_name.getText()
                                        .toString();
                                String contact_mobile = tv_cart_order_tel.getText()
                                        .toString();
                                if (tv_coupon_price.getText().toString()
                                        .equals("-￥0.00")) {
                                    coupon_id = "";
                                }
                                List<String> idList = new ArrayList<>();
                                StringBuilder o_sb = new StringBuilder();
                                StringBuilder index_sb = new StringBuilder();
                                List<Integer> indexList = new ArrayList<>();
                                idList.clear();
                                indexList.clear();
                                for (int j = 0; j < mList.size(); j++) {
                                    if (mList.get(j).getCheck()) {
                                        indexList.add(j);
                                        idList.add(mList.get(j).getId());
                                    }
                                }
                                for (int i = 0; i < idList.size(); i++) {
                                    String[] idSid = idList.get(i).split("a");
                                    String o_id = idSid[0];
                                    if (i == idList.size() - 1) {
                                        o_sb.append(o_id);
                                    } else {
                                        o_sb.append(o_id).append(",");
                                    }
                                }
                                for (int i = 0; i < indexList.size(); i++) {
                                    if (i == indexList.size() - 1) {
                                        index_sb.append(indexList.get(i));
                                    } else {
                                        index_sb.append(indexList.get(i)).append(",");
                                    }
                                }
                                name = mList.get(indexList.get(0)).getSname();
                                sid = mList.get(indexList.get(0)).getSid();

                                String order2_id = o_sb.toString();
                                String index = index_sb.toString();
                                order2s = new ArrayList<>();
                                //拼接数据传给支付结果页面
                                for (int i = 0; i < indexList.size(); i++) {
                                    //选中的角标
                                    Order2 orderDataEntity = mList.get(indexList.get(i));
                                    order2s.add(orderDataEntity);
                                }

                                commitCart(remark, contact_name, contact_mobile,
                                        coupon_id, order2_id, index);
                            }
                        } else {
                            if (tv_cart_order_tel.getText().toString().equals(getString(R.string.mustfill))) {
                                showSmileToast("客官，要选择您的支付、联系方式哦");
                                // UIUtils.showToastSafe("请选择订单、联系方式");
                            } else {
                                showSmileToast("请选择支付方式");
                            }
                        }
                    } else {
                        if (zfCheck) {
                            if (tv_cart_order_tel.getText().toString().equals("必填")) {
                                showSmileToast("客官，要选择您的订单、联系方式哦");
                                telWay();
                                // UIUtils.showToastSafe("请选择订单、联系方式");
                            } else {
                                showSmileToast("客官，要选择您的订单哦");
                                // UIUtils.showToastSafe("请选择订单");
                            }
                        } else {
                            if (tv_cart_order_tel.getText().toString().equals("必填")) {
                                showSmileToast("客官，要选择您的订单、支付、联系方式哦");
                                telWay();
                                // UIUtils.showToastSafe("请选择订单、联系方式");
                            } else {
                                showSmileToast("客官，要选择您的订单、支付方式哦");
                            }
                        }
                    }

                }
                break;
            case R.id.rl_pay_way:
                if (zfCheck) {
                    zfCheck = false;
                    iv_pay_way
                            .setBackgroundResource(R.drawable.order_nonechoose_btn);
                } else {
                    zfCheck = true;
                    iv_pay_way.setBackgroundResource(R.drawable.order_choose_btn);

                }
                break;
//		case R.id.rl_wxpay_way:
//				if (wxCheck) {
//					wxCheck = false;
//					iv_wxpay_way
//							.setBackgroundResource(R.drawable.order_nonechoose_btn);
//				} else {
//					wxCheck = true;
//					iv_wxpay_way.setBackgroundResource(R.drawable.order_choose_btn);
//
//				}
//				break;
            case R.id.rl_coupon:
                String order2_id;
                int cCount = 0;
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getCheck()) {
                        cCount++;
                    }
                }
                if (cCount == mList.size()) {
                    order2_id = getOrder2_id(mList);
                    jumpCoupon(order2_id, uid);
                } else {
                    List<String> idList = new ArrayList<>();
                    StringBuilder o_sb = new StringBuilder();
                    for (int j = 0; j < mList.size(); j++) {
                        if (mList.get(j).getCheck()) {
                            idList.add(mList.get(j).getId());
                        }
                    }
                    for (int i = 0; i < idList.size(); i++) {
                        String[] idSid = idList.get(i).split("a");
                        String o_id = idSid[0];
                        if (i == idList.size() - 1) {
                            o_sb.append(o_id);
                        } else {
                            o_sb.append(o_id).append(",");
                        }
                    }
                    order2_id = o_sb.toString();
                    jumpCoupon(order2_id, uid);
                }

                break;
            case R.id.rl_name_tel:
                Intent intent = new Intent(this, ContactShopCartActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 0);
                break;
            case R.id.rl_need_gone:
                Intent intentNopay = new Intent(this, MineOrderActivity.class);
                intentNopay.putExtra("status", "-1");
                startActivity(intentNopay);
                break;
            case R.id.bt_empty_go_buy:
                Intent intentIndex = new Intent(this, MainActivity.class);
                SharedPreferencesUtils.saveInt(this, "cartCount", 0);
                intentIndex.putExtra("backhome", "3");
                startActivity(intentIndex);
                break;
            case R.id.bt_empty_go_on:
                Intent intentNopayIndex = new Intent(this, MainActivity.class);
                SharedPreferencesUtils.saveInt(this, "cartCount", 1);
                intentNopayIndex.putExtra("backhome", "3");
                startActivity(intentNopayIndex);
                break;
            case R.id.deposit:
                Intent pledgeIntent = new Intent(this, Pledge_Activity.class);
//			StringBuffer sb = new StringBuffer();
//			if(null != mList &&mList.size() > 0){
//				for (int i = 0; i < mList.size(); i++) {
//					if(i == mList.size()-1){
//						sb.append(mList.get(i).getSid());
//					}else {
//						sb.append(mList.get(i).getSid()+",");
//					}
//				}
//			}
//			pledgeIntent.putExtra("sid", sb.toString());
                startActivity(pledgeIntent);
                break;
            default:
                break;
        }

    }

    private void backPress() {
        bb.setPrice(0);
        bb.getmOrder().clear();
        bb.setCount(0);
        if (newIndex == 1) {
            finish();
        } else {
            Intent intentPerson = new Intent(this, MainActivity.class);
            intentPerson.putExtra("backhome", "3");
            startActivity(intentPerson);
        }
    }

    /**
     * 滚动到联系人
     */
    private void telWay() {
        rl_name_tel.setBackgroundResource(R.drawable.name_tel_chang);
        sl.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private void showSmileToast(String s) {
        ImageSpan span = new ImageSpan(this, R.drawable.eye_icon);
        SpannableString ss = new SpannableString(s + "[smile]");
        ss.setSpan(span, s.length(), s.length() + "[smile]".length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        Toast.makeText(this, ss, Toast.LENGTH_SHORT).show();
    }

    private List<Integer> haveOrder() {
        List<Integer> oCount = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getCheck()) {
                oCount.add(i);
            }
        }
        return oCount;
    }

    /**
     * 提交数据
     *
     * @param remark         备注
     * @param contact_name   联系人姓名
     * @param contact_mobile 联系人电话
     * @param coupon_id2     优惠券id
     * @param order2_id      二级订单id
     * @param index          角标
     */
    private void commitCart(String remark, String contact_name,
                            String contact_mobile, String coupon_id2, String order2_id,
                            String index) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "commit_cart");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("remark", remark);
        params.addBodyParameter("contact_name", contact_name);
        params.addBodyParameter("contact_mobile", contact_mobile);
        params.addBodyParameter("coupon_id", coupon_id2);
        params.addBodyParameter("order2_ids", order2_id);
        params.addBodyParameter("index", index);
        LogUtil.d(TAG, "提交。。。。。。。。。。。。。。。。。。。。。。。。"
                + UrlUtils.SERVER_USER_COUPON + "action=" + "commit_cart&uid="
                + uid + "&remark=" + remark + "" + "&contact_name="
                + contact_name + "&contact_mobile=" + contact_mobile
                + "&coupon_id=" + coupon_id + "&order2_ids=" + order2_id
                + "&index=" + index);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_COMMIT_CART, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        JSONTokener jsonParser = new JSONTokener(arg0.result);
                        JSONObject json;
                        try {
                            json = (JSONObject) jsonParser.nextValue();
                            int code = json.getInt("code");
                            parseData(code, arg0.result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 根据code码处理不同的数据
     *
     * @param code   返回的响应码
     * @param result 返回的结果集
     */
    private void parseData(int code, String result) {
        switch (code) {
            case 1:
                conmmitInfo = GsonTools.changeGsonToBean(result, CartCommit.class);
                String allPrice = tv_cart_price.getText().toString().trim()
                        .substring(1);
                if (Float.valueOf(allPrice) == 0) {
                    jumpSucess();
                    break;
                }
                if (null != conmmitInfo) {
                    if (conmmitInfo.getMsg().equals("ok")) {
                        //判断之前是否初始化
                        EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
                            @Override
                            public void onSuccess() {
                                //调取易支付创建订单报文签名接口
                                getRequestCreateOrder(conmmitInfo.getTrade_id());
                            }
                        });
                        //支付宝支付相关处理
//                        getAliPaySign(conmmitInfo.getOrder1_id()+ "");
                    }
                }
                break;
            case 50025:
                // 数据提交失败，房间不足/设备不足
                CartFive fiveInfo = GsonTools.changeGsonToBean(result,
                        CartFive.class);
                if (null != fiveInfo) {
                    List<String> miss = fiveInfo.getMiss();
                    yesOrders.clear();
                    if (miss.size() > 0) {
                        float fee = 0;
                        for (int i = 0; i < miss.size(); i++) {
                            mList.get(Integer.valueOf(miss.get(i)))
                                    .setIs_valid("2");
                            mList.get(Integer.valueOf(miss.get(i))).setCheck(false);
                        }
                        for (int j = 0; j < mList.size(); j++) {
                            if (mList.get(j).getIs_valid().equals("1")) {
                                yesOrders.add(mList.get(j));
                                fee += Float.valueOf(mList.get(j).getFee());
                            }
                        }
                        bb.setPrice(fee);
                        bb.setCount(yesOrders.size());
                        tv_class_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), fee));
                        tv_should_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), fee));
                        tv_cart_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), fee));
                        if (yesOrders.size() == 0) {
                            iv_cart_all
                                    .setBackgroundResource(R.drawable.order_nonechoose_btn);
                        }
                        if (!tv_coupon_price.getText().toString().equals("-￥0.00")) {
                            useCoupon(order);
                        } else {
                            changeAllPrice();
                            // tv_cart_price.setText("¥" + bb.getPrice() + "0");
                        }
                        adapter.notifyDataSetChanged();
                        UIUtils.showToastSafe("存在库存不足的课室，请重新提交");
                    }
                }
                break;
            case 50027:
                // 数据提交失败，订单过期(将订单删除)
                final CartSeven sevenInfo = GsonTools.changeGsonToBean(result,
                        CartSeven.class);
                if (null != sevenInfo) {
//				if (sevenInfo.getMsg().equals("过期订单有过期作废订单，请删除")) {
                    // =======================================
                    CustomDialog customDialog = new CustomDialog(
                            ShopCartActivity.this,
                            UIUtils.getString(R.string.confirm),
                            "想想再说",
                            "存在已失效的课室，是否移除？");
                    customDialog.setOnClickListener(new IOnClickListener() {
                        @Override
                        public void oncClick(boolean isOk) {
                            if (isOk) {
                                float price1 = 0;
                                StringBuilder o_sb = new StringBuilder();
                                StringBuilder s_sb = new StringBuilder();
                                List<DataEntity> data = sevenInfo.getData();
                                for (int i = 0; i < data.size(); i++) {
                                    adapter.notifyDataSetChanged();
                                    String oid = data.get(i).getId();
                                    String sid = data.get(i).getSid();
                                    if (i == data.size() - 1) {
                                        o_sb.append(oid);
                                        s_sb.append(sid);
                                    } else {
                                        o_sb.append(oid).append(",");
                                        s_sb.append(sid).append(",");
                                    }
                                    mList.remove(mList.get(data.get(i)
                                            .getIndex()));
                                    saveCount();
                                }
                                cancelOrder(o_sb.toString(),
                                        s_sb.toString());
                                yesOrders.clear();
                                int cc = 0;
                                for (int i = 0; i < mList.size(); i++) {
                                    if (mList.get(i).getIs_valid().equals("1")) {
                                        yesOrders.add(mList.get(i));
                                        if (mList.get(i).getCheck()) {
                                            price1 += Float.valueOf(mList.get(
                                                    data.get(i).getIndex())
                                                    .getFee());
                                            cc++;
                                        }
                                    }
                                }
                                if (cc == yesOrders.size()) {
                                    iv_cart_all.setBackgroundResource
                                            (R.drawable.order_choose_btn);
                                } else {
                                    iv_cart_all.setBackgroundResource
                                            (R.drawable.order_nonechoose_btn);
                                }
                                bb.setCount(cc);
                                bb.setPrice(price1);
                                tv_class_price.setText(String.format(
                                        UIUtils.getString(R.string.rmb_float_zero),
                                        price1));
                                tv_should_price.setText(String.format(
                                        UIUtils.getString(R.string.rmb_float_zero),
                                        price1));
                                tv_cart_price.setText(String.format(
                                        UIUtils.getString(R.string.rmb_float_zero),
                                        price1));
                                tv_coupon_price.setText(
                                        UIUtils.getString(R.string.rmb_equals_negative_zero));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    // =======================================
//				}
                }

                break;

            case 8888:
                UIUtils.showToastSafe("服务器繁忙");
                break;
            default:
                break;
        }

    }

    /**
     * 请求 获取创建订单报文签名 接口
     */
    private void getRequestCreateOrder(String trade_id) {
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
                    ShopCartActivity.this,
                    createOrderResult.getBody(),
                    createOrderResult.getSign(),
                    new IPayResult() {
                        @Override
                        public void payResult(int code, String msg, String dataJson) {
                            if (code == EjuPayResultCode.PAY_SUCCESS_CODE.getCode()) {
                                //支付成功
                                jumpSucess();
                            } else if (code == EjuPayResultCode.PAY_FAIL_CODE.getCode()) {
                                //支付失败
                                jumpFail();
                            } else if (code == EjuPayResultCode.PAY_CANCEL_C0DE.getCode()) {
                                //支付取消
                                jumpFail();
                            }
                        }
                    }
            );
        }
    }
    /**
     * 获取AliPay密钥
     */
   /* private void getAliPaySign(String oid){
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "alipay");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("oid", oid);
        httpUtils.send(HttpMethod.POST,
                UrlUtils.SERVER_USER_COUPON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0,
                                          String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe("请求数据失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        LogUtil.d(TAG, "支付数据=========" + arg0.result);
                        if (arg0.result == null) {
                            UIUtils.showToastSafe(R.string.fail_data_request);
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    arg0.result);
                            if (jsonObject.getInt("code") == 1
                                    && jsonObject
                                    .getJSONObject("data") != null) {
                                String rsasign = jsonObject
                                        .getJSONObject("data")
                                        .getString("rsasign");
                                String prestr = jsonObject
                                        .getJSONObject("data")
                                        .getString("prestr");
                                String payInfo = prestr
                                        + "&sign=\"" + rsasign
                                        + "\"&"
                                        + "sign_type=\"RSA\"";
                                if (StringUtils
                                        .isNotNullString(payInfo)) {
                                                   *//* functionAliPay(payInfo);*//*
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }*/

    /**
     * 保存购物车订单的数量
     */
    private void saveCount() {
        SharedPreferencesUtils.saveInt(this, "cartCount", mList.size());
    }

    /**
     * 选择优惠券
     *
     * @param order2 二级订单id
     * @param u      用户id
     */
    private void jumpCoupon(String order2, String u) {
        Intent intent = new Intent(this, CouponActivity.class);
        intent.putExtra("order2_id", order2);
        intent.putExtra("uid", u);
        startActivityForResult(intent, 0);
    }

    /**
     * 将order2_id拼接
     *
     * @param mList 订单数据集合
     * @return 订单id拼接字符串
     */
    private String getOrder2_id(List<Order2> mList) {
        if (mList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mList.size(); i++) {
                String id = mList.get(i).getId();
                if (i == mList.size() - 1) {
                    sb.append(id);
                } else {
                    sb.append(id).append(",");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 取消订单
     *
     * @param oid 订单id
     * @param sid 学校id
     */
    private void cancelOrder(String oid, String sid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "cart_cancel");
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_RESERVATION,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        OrderCancel info = GsonTools.changeGsonToBean(
                                arg0.result, OrderCancel.class);
                        if (null != info) {
                            if (info.getCode() == 1
                                    && info.getMsg().equals(getString(R.string.toast_show_cancle_course))) {
                                UIUtils.showToastSafe(R.string.toast_order_have_been_deleted);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                });
    }

    /**
     * 处理联系人、优惠券等返回的数据
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        返回的intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:// 联系人
                id = data.getStringExtra("id");
                if (id != null) {
                    String name = data.getStringExtra("name");
                    String tel = data.getStringExtra("tel");
                    tv_cart_order_name.setText(name);
                    tv_cart_order_tel.setText(tel);
                    tv_cart_order_tel.setTextColor(UIUtils
                            .getColor(R.color.color_gay_8f));
                    rl_name_tel.setBackgroundColor(UIUtils.getColor(R.color.white));
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    tv_cart_order_name.setText(R.string.lable_contact);
                    tv_cart_order_tel.setText(R.string.mustfill);
                    tv_cart_order_tel.setTextColor(UIUtils
                            .getColor(R.color.app_theme_color));
                }

                break;
            case 2:// 优惠券
                String couponPrice = data.getStringExtra("couponPrice");
                coupon_id = data.getStringExtra("coupon_id");
                bb.setCouponID(coupon_id);
                tv_coupon_price.setText(String.format(UIUtils.getString(
                        R.string.negative_rmb_how_much), couponPrice));
                float price = bb.getPrice();
                float newPrice = price - Float.valueOf(couponPrice);
                tv_class_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), price));
                tv_should_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), newPrice));
                tv_cart_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), newPrice));
//                price = 0;
                if (adapter == null) {
                    UIUtils.showToastSafe(R.string.fail_network_request);
                } else {
                    adapter.notifyDataSetChanged();
                }

                break;

            case 3:
                int i = data.getIntExtra("position", Integer.MAX_VALUE);
                if (i != Integer.MAX_VALUE) {
                    cancelOrder(mList.get(i).getId(), mList.get(i).getSid());
                    mList.remove(i);
                    float ppp = 0;
                    int count = 0;
                    for (int j = 0; j < mList.size(); j++) {
                        if (mList.get(j).getCheck()) {
                            ppp += Float.valueOf(mList.get(j).getFee());
                            count++;
                        }
                    }
                    bb.setPrice(ppp);
                    bb.setCount(count);
                    tv_class_price.setText(
                            String.format(UIUtils.getString(R.string.rmb_float_zero), ppp));
                    tv_should_price.setText(
                            String.format(UIUtils.getString(R.string.rmb_float_zero), ppp));
                    tv_cart_price.setText(
                            String.format(UIUtils.getString(R.string.rmb_float_zero), ppp));
                    useCoupon(bb.getmOrder());
                    if (mList != null && mList.size() == 0) {
                        jumpEmptyCart();
                    }
                    adapter.notifyDataSetChanged();
                }

                break;

            case 4:
                int x = data.getIntExtra("position", Integer.MAX_VALUE);
                if (x != Integer.MAX_VALUE) {
                    requestNewData("show_cart", uid, x);
                }
                break;
            default:
                break;
        }
    }

    private void requestNewData(String action, String uid, final int x) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_RESERVATION, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe("请求数据失败");

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
//                        ShopCart info = GsonTools.changeGsonToBean(arg0.result, ShopCart.class);
//                        if (null != info) {
//                            if ((info.getCode() == 1) && info.getMsg().equals("ok")) {
//                                List<OrderDataEntity> datas = info.getData();
//                                mList.get(x).setDevice_free(datas.get(x).getDevice_free());
//                                adapter.notifyDataSetChanged();
//                            }
//
//                        }
                    }

                });
    }

    // 调用alipay
  /*  private void functionAliPay(final String orderInfo) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ShopCartActivity.this);
                // 返回结果
                final String resultData = alipay.pay(orderInfo);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (StringUtils.isNotNullString(resultData)) {
                            AlipayResult result = new AlipayResult(resultData);
                            String state = result.getResultStatus();
                            switch (state) {
                                case AlipayResult.AliPay_PayOK:
                                    // 成功处理
                                    jumpSucess();
                                    break;
                                case AlipayResult.AliPay_PayFail:
                                    jumpFail();
//                                    jumpSucess();
                                    break;
                                case AlipayResult.AliPay_Error:
                                case AlipayResult.AliPay_PayCancel:
                                    HttpUtils httpUtils = new HttpUtils();
                                    RequestParams params = new RequestParams();
                                    params.addBodyParameter("action",
                                            "order1_status");
                                    params.addBodyParameter("oid",
                                            conmmitInfo.getOrder1_id() + "");
                                    params.addBodyParameter("uid", uid);
                                    httpUtils.send(HttpMethod.POST,
                                            UrlUtils.SERVER_USER_COUPON, params,
                                            new RequestCallBack<String>() {

                                                @Override
                                                public void onFailure(
                                                        HttpException arg0,
                                                        String arg1) {
                                                    // 请求网络失败
                                                    UIUtils.showToastSafe("请求数据失败");

                                                }

                                                @Override
                                                public void onSuccess(
                                                        ResponseInfo<String> arg0) {
                                                    // 处理返回的数据
                                                    CouponUse isYes = GsonTools
                                                            .changeGsonToBean(
                                                                    arg0.result,
                                                                    CouponUse.class);
                                                    if (null != isYes) {
                                                        if (isYes.getCode() == 1
                                                                && isYes.getMsg()
                                                                .equals("ok")) {
                                                            Integer isInt = isYes
                                                                    .getData();
                                                            if (isInt == 1) {
                                                                jumpSucess();
                                                            } else {
                                                                jumpFail();
//                                                                jumpSucess();
                                                            }
                                                        }
                                                    }
                                                }

                                            });
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
    }*/

    /**
     * 跳转失败页面
     */
    private void jumpFail() {
        Intent intentFail = new Intent(ShopCartActivity.this,
                AliPay_PayFailActivity.class);

        intentFail.putExtra("fail", conmmitInfo);
        intentFail.putExtra("sid", sid);
        intentFail.putExtra("name", name);
        startActivity(intentFail);
    }

    /**
     * 跳转成功页面
     */
    private void jumpSucess() {
        Intent intentSucess = new Intent(ShopCartActivity.this,
                AliPay_PayOKActivity.class);
        intentSucess.putExtra("sucess", conmmitInfo);
        intentSucess.putExtra("sid", sid);
        intentSucess.putExtra("name", name);
        intentSucess.putExtra("order2", order2s);
        startActivity(intentSucess);
    }

    /**
     * 判断优惠券是否可用
     */
    private void useCoupon(Map<String, Integer> order) {
        String uid = SharedPreferencesUtils.getString(this, "id", null);
        String coupon_id = bb.getCouponID();
        List<String> idList = new ArrayList<>();
        StringBuilder o_sb = new StringBuilder();
        for (int j = 0; j < mList.size(); j++) {
            if (mList.get(j).getCheck()) {
                idList.add(mList.get(j).getId());
            }
        }
        if (idList.size() > 0) {
            for (int i = 0; i < idList.size(); i++) {
                String[] idSid = idList.get(i).split("a");
                String o_id = idSid[0];
                if (i == idList.size() - 1) {
                    o_sb.append(o_id);
                } else {
                    o_sb.append(o_id).append(",");
                }
            }
            String order2_id = o_sb.toString();
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("action", "coupon_available");
            params.addBodyParameter("order2_id", order2_id);
            params.addBodyParameter("coupon_id", coupon_id);
            params.addBodyParameter("uid", uid);
            httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_COUPON,
                    params, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            // 请求网络失败
                            UIUtils.showToastSafe("请求数据失败");

                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            // 处理返回的数据
                            processDataU(arg0.result);

                        }

                    });
        } else {
            tv_coupon_price.setText(
                    String.format(UIUtils.getString(R.string.rmb_float_zero), price));
        }
    }

    /**
     * 处理优惠券请求返回的数据
     *
     * @param result 结果集
     */
    private void processDataU(String result) {
        CouponUse couponInfo = GsonTools.changeGsonToBean(result,
                CouponUse.class);
        if (null != couponInfo) {
            if (couponInfo.getCode() == 1 && couponInfo.getMsg().equals("ok")) {
                tv_class_price.setText(String.format(
                        UIUtils.getString(R.string.rmb_float_zero),
                        bb.getPrice()));
                if (couponInfo.getData() == 1) {
                    // 优惠券可用
                    String cP = tv_coupon_price.getText().toString()
                            .substring(2);
                    float newPrice = bb.getPrice() - Float.valueOf(cP);
                    tv_should_price.setText(String.format(
                            UIUtils.getString(R.string.rmb_float_zero),
                            newPrice));
                    tv_cart_price.setText(String.format(
                            UIUtils.getString(R.string.rmb_float_zero),
                            newPrice));
                } else if (couponInfo.getData() == 0) {
                    // 优惠券不可用
                    tv_coupon_price.setText(UIUtils.getString(R.string.rmb_equals_negative_zero));
                    changeAllPrice();
                    // tv_cart_price.setText("¥" + bb.getPrice() + "0");
                }
            }
        }
    }

    /**
     * 控制总价格控件的数据
     */
    private void changeAllPrice() {
        if (bb.getPrice() <= 0) {
            bb.setPrice(0);
            tv_should_price.setText(UIUtils.getString(R.string.rmb_equals_zero));
            tv_cart_price.setText(UIUtils.getString(R.string.rmb_equals_zero));
        } else {
            tv_should_price.setText(String.format(
                    UIUtils.getString(R.string.rmb_float_zero),
                    bb.getPrice()));
            tv_cart_price.setText(String.format(
                    UIUtils.getString(R.string.rmb_float_zero),
                    bb.getPrice()));
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_shopcart);
    }


    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_INDEX);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            backPress();
            super.onBackPressed();
        }
    }
}
