package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ejupay.sdk.EjuPayManager;
import com.ejupay.sdk.service.EjuPayResultCode;
import com.ejupay.sdk.service.IPayResult;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.CourseOrderDetailBean;
import com.yiju.ClassClockRoom.bean.CreateOrderResult;
import com.yiju.ClassClockRoom.bean.OrderCourseResult;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.MD5;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ----------------------------------------
 * 注释:课程订单详情
 * <p/>
 * 作者: sandy
 * <p/>
 * 时间: on 2016/6/24 13:55
 * ----------------------------------------
 */
public class CourseOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //订单超时提示布局
    @ViewInject(R.id.tv_tip)
    private TextView tv_tip;
    //订单编号
    @ViewInject(R.id.tv_order_detail_id)
    private TextView tv_order_detail_id;
    //订单状态
    @ViewInject(R.id.tv_order_detail_status)
    private TextView tv_order_detail_status;
    //下单时间
    @ViewInject(R.id.tv_order_time)
    private TextView tv_order_time;
    //门店
    @ViewInject(R.id.ll_course_detial_store)
    private LinearLayout ll_course_detial_store;
    //旗舰店图标
    @ViewInject(R.id.iv_item_detail_type)
    private ImageView iv_item_detail_type;
    //门店
    @ViewInject(R.id.tv_course_address)
    private TextView tv_course_address;
    //课程点击区域
    @ViewInject(R.id.ll_course)
    private LinearLayout ll_course;
    //课程图
    @ViewInject(R.id.iv_course_pic)
    private ImageView iv_course_pic;
    //课程名称
    @ViewInject(R.id.tv_course_name)
    private TextView tv_course_name;
    //课程时间
    @ViewInject(R.id.tv_course_desc)
    private TextView tv_course_desc;
    //联系方式
    @ViewInject(R.id.tv_contact_name)
    private TextView tv_contact_name;
    //订单费用
    @ViewInject(R.id.ll_order_total)
    private LinearLayout ll_order_total;
    //课程费用
    @ViewInject(R.id.tv_course_price_total)
    private TextView tv_course_price_total;
    //实付
    @ViewInject(R.id.tv_course_price_should)
    private TextView tv_course_price_should;
    //左下角金额
    @ViewInject(R.id.tv_pay_price)
    private TextView tv_pay_price;
    //订单费用
    @ViewInject(R.id.ll_pay_way)
    private RelativeLayout ll_pay_way;
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

    private boolean closeFlag = false;

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
                        tv_tip.setText("请在" + time / (60) + "分" + time % 60 + "秒内完成付款，订单超时自动关闭");
                    } else {
                        changeCloseStatus();
                        closeFlag = true;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private String uid;
    private int time;
    private String oid;
    private int expireTime;
    private int serverTime;
    private String sname;
    private String course_id;
    private CourseOrderDetailBean.DataEntity data;
    private String pay_method;

    @Override
    public void initIntent() {
        super.initIntent();
        oid = getIntent().getStringExtra("oid");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean refresh = intent.getBooleanExtra(ExtraControl.EXTRA_REFRESH_FLAG, false);
        if (refresh) {
            if (handler != null) {
                handler.removeMessages(0);
            }
            getHttpUtils();
        }
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.order_detail));
        uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                "id", null);
        getHttpUtils();
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        tv_order_detail_status.setOnClickListener(this);
        rl_order_detail_pay.setOnClickListener(this);
        ll_course_detial_store.setOnClickListener(this);
        ll_course.setOnClickListener(this);
        tv_pay_order.setOnClickListener(this);
        tv_close_order.setOnClickListener(this);
        tv_change_order.setOnClickListener(this);

    }


    /**
     * 课程订单详情请求
     */
    private void getHttpUtils() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_order_course_detail");
        params.addBodyParameter("id", oid);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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

    private void processData(String result) {
        CourseOrderDetailBean courseOrderDetailBean = GsonTools.changeGsonToBean(result,
                CourseOrderDetailBean.class);
        if (courseOrderDetailBean == null) {
            return;
        }
        if ("1".equals(courseOrderDetailBean.getCode())) {
            data = courseOrderDetailBean.getData();
            if (!StringUtils.isNullString(data.getFee())) {
                tv_course_price_total.setText(String.format(
                        UIUtils.getString(R.string.how_much_money), data.getFee()));
            }
            if (!StringUtils.isNullString(data.getReal_fee())) {
                tv_course_price_should.setText(String.format(
                        UIUtils.getString(R.string.how_much_money), data.getReal_fee()));
            }
            pay_method = data.getPay_method();
            switch (Integer.valueOf(data.getStatus())) {
                case 0://未支付
                    tv_tip.setVisibility(View.VISIBLE);
                    ll_order_total.setVisibility(View.GONE);
                    ll_pay_way.setVisibility(View.GONE);
                    rl_order_detail_pay.setVisibility(View.VISIBLE);
                    tv_close_order.setVisibility(View.VISIBLE);
                    tv_pay_order.setVisibility(View.VISIBLE);
                    tv_change_order.setVisibility(View.GONE);
                    tv_pay_price.setVisibility(View.VISIBLE);
                    tv_order_detail_status.setText(UIUtils.getString(R.string.label_wait_pay));
                    tv_pay_price.setText(String.format(
                            UIUtils.getString(R.string.how_much_money), data.getReal_fee()));
                    if (data.getExpire_time() != null) {
                        expireTime = Integer.valueOf(data.getExpire_time());
                    }
                    if (courseOrderDetailBean.getServer_time() != null) {
                        serverTime = Integer.valueOf(courseOrderDetailBean.getServer_time());
                    }
                    // 开启倒计时
                    time = expireTime - serverTime;
                    if (time >= 0) {
                        tv_tip.setText("请在" + time / (60) + "分" + time % 60 + "秒内完成付款，订单超时自动关闭");
                        handler.sendEmptyMessage(0);
                    } else {
                        changeCloseStatus();
                        closeFlag = true;
                    }
                    break;
                case 1://报名成功
                    tv_tip.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_pay_way.setVisibility(View.VISIBLE);
                    rl_order_detail_pay.setVisibility(View.GONE);
                    tv_pay_price.setVisibility(View.GONE);
                    tv_order_detail_status.setText(UIUtils.getString(R.string.sign_up_success));
                    break;
                case 100://已取消
                    tv_tip.setVisibility(View.GONE);
                    ll_order_total.setVisibility(View.VISIBLE);
                    ll_pay_way.setVisibility(View.VISIBLE);
                    rl_order_detail_pay.setVisibility(View.GONE);
                    tv_pay_price.setVisibility(View.GONE);
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_cancel));
                    break;
                case 102://已关闭
                    changeCloseStatus();
                    break;
            }

            tv_order_detail_id.setText(String.format(UIUtils.getString(R.string.order_num), data.getId()));
            tv_order_time.setText(
                    String.format(
                            getString(R.string.txt_order_create_time),
                            second2Date(data.getCreate_time())
                    ));
            if (!StringUtils.isNullString(data.getSchool_type())) {
                if ("1".equals(data.getSchool_type())) {
                    iv_item_detail_type.setVisibility(View.VISIBLE);
                } else {
                    iv_item_detail_type.setVisibility(View.GONE);
                }
            }
            course_id = data.getCourse_id();
            sname = data.getSname();
            if (!StringUtils.isNullString(sname)) {
                tv_course_address.setText(sname);
            }
            if (!StringUtils.isNullString(data.getPic())) {
                Glide.with(this).load(data.getPic()).into(iv_course_pic);
            }
            if (!StringUtils.isNullString(data.getCourse_name())) {
                tv_course_name.setText(data.getCourse_name());
            }
            if (!StringUtils.isNullString(data.getCourse_str())) {
                tv_course_desc.setText(data.getCourse_str());
            }
            if (!StringUtils.isNullString(data.getContact_name()) && !StringUtils.isNullString(data.getContact_mobile())) {
                tv_contact_name.setText(data.getContact_name() + " " + data.getContact_mobile());
            }
        } else {
            UIUtils.showToastSafe(courseOrderDetailBean.getMsg());
        }
    }

    private void changeCloseStatus() {
        tv_tip.setVisibility(View.GONE);
        ll_order_total.setVisibility(View.GONE);
        ll_pay_way.setVisibility(View.GONE);
        rl_order_detail_pay.setVisibility(View.VISIBLE);
        tv_order_detail_status.setText(UIUtils.getString(R.string.status_close));
        tv_close_order.setVisibility(View.GONE);
        tv_pay_order.setVisibility(View.GONE);
        tv_change_order.setVisibility(View.VISIBLE);
        tv_pay_price.setVisibility(View.VISIBLE);
        tv_pay_price.setText(String.format(
                UIUtils.getString(R.string.how_much_money), data.getReal_fee()));
    }

    @Override
    public String getPageName() {
        return UIUtils.getString(R.string.person_course_order_detial_page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_course_order_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_131");
                onBackPressed();
                break;
            case R.id.ll_course_detial_store://门店
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_132");
                Intent intent = new Intent(UIUtils.getContext(), StoreDetailActivity.class);
                intent.putExtra(ExtraControl.EXTRA_STORE_ID, data.getSid());
                UIUtils.startActivity(intent);
                break;
            case R.id.ll_course://课程
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_133");
                Intent intentCourse;
                if (data == null) {
                    return;
                }
                if ("1".equals(data.getCtype())) {
                    //体验课
                    intentCourse = new Intent(UIUtils.getContext(), ExperienceCourseDetailActivity.class);
                } else {
                    //正式课
                    intentCourse = new Intent(UIUtils.getContext(), FormalCourseDetailActivity.class);
                }
                intentCourse.putExtra(ExtraControl.EXTRA_COURSE_ID, course_id);
                UIUtils.startActivity(intentCourse);
                break;
            case R.id.tv_pay_order://立即支付
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_136");
                if ("6".equals(pay_method)) {
                    getRequestCreateOrder();
                } else {
                    //切换支付方式
                    changePayWayH5();
                }
                break;
            case R.id.tv_close_order://删除订单和取消订单操作
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_135");
                cancelOrder();
                break;
            //删除订单
            case R.id.tv_change_order:
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_134");
                deleteOrder();
                break;
        }
    }

    private void changePayWayH5() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "update_pay_method");
        params.addBodyParameter("order_id", oid);
        params.addBodyParameter("pay_method", "6");
        try {
            String sign = MD5.md5(oid + "|6");
            params.addBodyParameter("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                        JSONObject json;
                        try {
                            json = new JSONObject(arg0.result);
                            String code = json.getString("code");
                            if ("1".equals(code)) {
                                getRequestCreateOrder();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 请求 获取创建订单报文签名 接口
     */
    private void getRequestCreateOrder() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "create_order");
        params.addBodyParameter("trade_id", data.getTrade_id());
        params.addBodyParameter("terminalType", "SDK");
        if ("1".equals(data.getCtype())) {
            //体验
            params.addBodyParameter("businessId", "23");
        } else {
            //正式
            params.addBodyParameter("businessId", "22");
        }
        params.addBodyParameter("cid", "1");

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
                    this,
                    createOrderResult.getBody(),
                    createOrderResult.getSign(),
                    new IPayResult() {
                        @Override
                        public void payResult(int code, String msg, String dataJson) {
                            Intent intent = new Intent(CourseOrderDetailActivity.this, SignUpResultActivity.class);
                            //课程名
                            intent.putExtra(ExtraControl.EXTRA_COURSE_NAME, data.getCourse_name());
                            intent.putExtra(ExtraControl.EXTRA_ENTRANCE, SignUpResultActivity.ENTRANCE_DETAIL);
                            //订单id
                            intent.putExtra(ExtraControl.EXTRA_ORDER_ONE_ID, data.getId());
                            if ("1".equals(data.getCtype())) {//课次
                                intent.putExtra(ExtraControl.EXTRA_CLASS_TIMES, data.getCourse_str());
                            }
                            if (code == EjuPayResultCode.PAY_SUCCESS_CODE.getCode()) {
                                //支付成功
                                intent.putExtra(ExtraControl.EXTRA_PAY_STATUS, ExtraControl.EXTRA_PAY_SUCCESS);
                                startActivity(intent);
                            } else if (code == EjuPayResultCode.PAY_FAIL_CODE.getCode()) {
                                //支付失败
                                intent.putExtra(ExtraControl.EXTRA_PAY_STATUS, ExtraControl.EXTRA_PAY_FAIL);
                                startActivity(intent);
                            } else if (code == EjuPayResultCode.PAY_CANCEL_C0DE.getCode()) {
                                //支付取消
                                intent.putExtra(ExtraControl.EXTRA_PAY_STATUS, ExtraControl.EXTRA_PAY_FAIL);
                                startActivity(intent);
                            }
                        }
                    }
            );
        }
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        CustomDialog customDialog = new CustomDialog(
                CourseOrderDetailActivity.this,
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

    // 请求关闭课程订单
    private void getHttpUtils2() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "order_cancel");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("oid", oid);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
            // 修改状态
            tv_order_detail_status.setText(UIUtils.getString(R.string.status_close));
            changeCloseStatus();
            closeFlag = true;
            UIUtils.showToastSafe(UIUtils.getString(R.string.toast_show_close_success));
            CourseOrderDetailActivity.this.setResult(RESULT_OK);
            finish();

        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }


    /**
     * 删除订单
     */
    private void deleteOrder() {
        CustomDialog customDialog = new CustomDialog(
                CourseOrderDetailActivity.this,
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

    /**
     * 删除请求
     */
    private void getHttpUtils3() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "order_delete");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("oid", oid);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
            UIUtils.showToastSafe(UIUtils.getString(R.string.toast_show_delete_success));
            CourseOrderDetailActivity.this.setResult(RESULT_OK);
            finish();
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
            UIUtils.showToastSafe(getString(R.string.toast_permission_call_phone));
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String second2Date(String time) {
        Date today = new Date(Long.valueOf(time + "000"));
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(today);
    }

    @Override
    public void onBackPressed() {
        if (closeFlag) {
            CourseOrderDetailActivity.this.setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeMessages(0);
        }
        super.onDestroy();
    }
}
