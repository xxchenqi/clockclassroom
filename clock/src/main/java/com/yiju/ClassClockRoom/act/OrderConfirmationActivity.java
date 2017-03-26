package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ejupay.sdk.EjuPayManager;
import com.ejupay.sdk.service.EjuPayResultCode;
import com.ejupay.sdk.service.IEjuPayResult;
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
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.AddInvoiceContactBean;
import com.yiju.ClassClockRoom.bean.AdjustmentData;
import com.yiju.ClassClockRoom.bean.AvailablePrice;
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.bean.ContactTelName;
import com.yiju.ClassClockRoom.bean.CreateOrderResult;
import com.yiju.ClassClockRoom.bean.DeviceEntity;
import com.yiju.ClassClockRoom.bean.InvoiceContacts;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.ShopCart;
import com.yiju.ClassClockRoom.bean.TimeGroup;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.callback.PayWayOnClickListener;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.control.FailCodeControl;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.LogUtil;
import com.yiju.ClassClockRoom.util.PayWayUtil;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringFormatUtil;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ----------------------------------------
 * 注释: 订单确认页
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/5/9 15:27
 * ----------------------------------------
 */
public class OrderConfirmationActivity extends BaseActivity implements View.OnClickListener {
    //退出按钮
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //客服
    @ViewInject(R.id.head_right_image)
    private ImageView head_right_image;
    //客服点击
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //订单所有内容
    @ViewInject(R.id.sv_order_detail)
    private ScrollView sv_order_detail;

    //付款布局
    @ViewInject(R.id.rl_order_confirmation_affirm_pay)
    private RelativeLayout rl_order_confirmation_affirm_pay;

    //联系人布局
    @ViewInject(R.id.rl_name_tel)
    private RelativeLayout rl_name_tel;
    //联系人名字
    @ViewInject(R.id.tv_order_detail_name)
    private TextView tv_order_detail_name;
    //联系人电话
    @ViewInject(R.id.tv_order_detail_mobile)
    private TextView tv_order_detail_mobile;
    //联系人必填文案
    @ViewInject(R.id.tv_not_null)
    private TextView tv_not_null;

    //旗舰店图片
    @ViewInject(R.id.iv_self_support)
    private ImageView iv_self_support;
    //店名
    @ViewInject(R.id.tv_item_detail_sname)
    private TextView tv_item_detail_sname;
    //地址
    @ViewInject(R.id.tv_store_address)
    private TextView tv_store_address;
    //用途(中间课室)
    @ViewInject(R.id.tv_item_detail_use_desc)
    private TextView tv_item_detail_use_desc;
    //数量
    @ViewInject(R.id.tv_item_detail_room_count)
    private TextView tv_item_detail_room_count;
    //平日价格
    @ViewInject(R.id.tv_day_price)
    private TextView tv_day_price;
    //周末价格
    @ViewInject(R.id.tv_week_price)
    private TextView tv_week_price;
    //门店图片
    @ViewInject(R.id.iv_store_pic)
    private ImageView iv_store_pic;
    //订单信息
    @ViewInject(R.id.tv_order_info)
    private TextView tv_order_info;
    //使用日期
    @ViewInject(R.id.tv_use_date)
    private TextView tv_use_date;
    //循环方式
    @ViewInject(R.id.tv_use_week)
    private TextView tv_use_week;
    //使用天数
    @ViewInject(R.id.tv_use_day)
    private TextView tv_use_day;
    //使用时段
    @ViewInject(R.id.tv_use_time)
    private TextView tv_use_time;


    //个别日期调整区域块
    @ViewInject(R.id.ll_edit_individuation)
    private LinearLayout ll_edit_individuation;
    //取消课室
    @ViewInject(R.id.tv_cancel_classroom)
    private TextView tv_cancel_classroom;
    // 增加课室
    @ViewInject(R.id.tv_add_classroom)
    private TextView tv_add_classroom;
    // 增加日期
    @ViewInject(R.id.tv_add_classroom_date)
    private TextView tv_add_classroom_date;
    // 减少日期
    @ViewInject(R.id.tv_cancel_classroom_date)
    private TextView tv_cancel_classroom_date;
    //取消课室列表
//    @ViewInject(R.id.ll_cancel_classroom)
//    private LinearLayout ll_cancel_classroom;
//    //增加课室列表
//    @ViewInject(R.id.ll_add_classroom)
//    private LinearLayout ll_add_classroom;

    //收费项目
    @ViewInject(R.id.ll_no_free_device_add)
    private LinearLayout ll_no_free_device_add;
    //收费项目布局
    @ViewInject(R.id.ll_optional_equipment)
    private LinearLayout ll_optional_equipment;
    //课室小计
    @ViewInject(R.id.rl_class_fee)
    private RelativeLayout rl_class_fee;
    //收费项目
    @ViewInject(R.id.rl_charge)
    private RelativeLayout rl_charge;


    //订单备注
    @ViewInject(R.id.et_remark)
    private EditText et_remark;
    //优惠券
    @ViewInject(R.id.rl_coupon)
    private RelativeLayout rl_coupon;
    //提醒信息布局
    @ViewInject(R.id.lr_order_detail_remind)
    private LinearLayout lr_order_detail_remind;
    //优惠券内容
    @ViewInject(R.id.tv_coupon)
    private TextView tv_coupon;

    //确认付款
    @ViewInject(R.id.btn_affirm_pay)
    private Button btn_affirm_pay;

    //发票
    @ViewInject(R.id.rl_invoice)
    private RelativeLayout rl_invoice;
    //发票信息
    @ViewInject(R.id.ll_invoice_info)
    private LinearLayout ll_invoice_info;
    //发票类型
    @ViewInject(R.id.tv_invoice_type)
    private TextView tv_invoice_type;
    //发票抬头
    @ViewInject(R.id.tv_invoice_company_name)
    private TextView tv_invoice_company_name;
    //发票描述
    @ViewInject(R.id.tv_invoice_company_xmmc)
    private TextView tv_invoice_company_xmmc;
    //发票箭头
//    @ViewInject(R.id.tv_invoice)
//    private TextView tv_invoice;
    //余额支付提示
    @ViewInject(R.id.tv_order_conf_tips)
    private TextView tv_order_conf_tips;
    //总价
    @ViewInject(R.id.tv_sum_price)
    private TextView tv_sum_price;
    //收费项目
    @ViewInject(R.id.tv_device_price)
    private TextView tv_device_price;
    //课室费用
    @ViewInject(R.id.tv_course_price)
    private TextView tv_course_price;
    //房间数量
    @ViewInject(R.id.tv_sum_room)
    private TextView tv_sum_room;

    //传到支付界面的bean
    private CartCommit conmmitInfo;
    //2级订单ID
    private String order2_id;
    //优惠券id
    private String coupon_id = "";
    //订单费用
    private String fee;
    //传到支付结果页面的学校id
    private String sid;
    //传到支付结果页面的学校sname
    private String name;
    //联系人的id
    private String id;
    //传给支付结果页的bean
    private ArrayList<Order2> order2s;
    //确认类型"confirm_type": "2" 确认类型 0=无需确认 1=支付前确认 2=支付后确认 0,2跳转收银台 1跳转待确认
    private String confirm_type;
    //发票联系人
    private ArrayList<InvoiceContacts> invoiceContacts;
    //发票id
    private String invoice_contact_id;
    //发票类型
    private int invoice_type;
    //2级订单
    private Order2 dataEntity;
    //最后一次选择过的发票类型
    private String last_invoice_type;
    //请求次数
    private int request = 0;
    private String invoice_type_save;
    private String uid;
    //电话
    private String school_phone;
    private PopupWindow mPopupWindow;
    private String balance;
    private String school_type;
    private String store_tel;
    private String c_type;

    @Override
    public void initIntent() {
        super.initIntent();
        order2_id = getIntent().getStringExtra("order2_id");
    }

    @Override
    protected void initView() {
        uid = StringUtils.getUid();
        et_remark.setFilters(new InputFilter[]{CommonUtil.getTextLengthFilter(100)});
        et_remark.setSingleLine(false);
        et_remark.setHorizontallyScrolling(false);
        order2s = new ArrayList<>();
        invoiceContacts = new ArrayList<>();
        invoice_type_save = SharedPreferencesUtils.getString(this,
                getResources().getString(R.string.shared_invoice_type) + uid, "0");
    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.order_affirm));
        head_right_image.setImageResource(R.drawable.service);
        getHttpUtils();
        getBalanceHttpUtils();
    }

    /**
     * 获取余额
     */
    private void getBalanceHttpUtils() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "user_amount_remain");
        params.addBodyParameter("uid", StringUtils.getUid());
        params.addBodyParameter("url", UrlUtils.SERVER_USER_API);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
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
                            }else{
                                FailCodeControl.checkCode(code);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        rl_name_tel.setOnClickListener(this);
        rl_coupon.setOnClickListener(this);
        lr_order_detail_remind.setOnClickListener(this);
        btn_affirm_pay.setOnClickListener(this);
        rl_invoice.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        rl_class_fee.setOnClickListener(this);
        rl_charge.setOnClickListener(this);
        tv_order_conf_tips.setOnClickListener(this);
        et_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_236");
            }
        });
    }

    /**
     * 订单确认请求(购物车请求)
     */
    private void getHttpUtils() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "show_cart");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order2_id", order2_id);
        params.addBodyParameter("room_adjust_new", "1");
        params.addBodyParameter("url", UrlUtils.SERVER_RESERVATION);
        params.addBodyParameter("sessionId", StringUtils.getSessionId());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.JAVA_PROXY, params,
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
                        processData(arg0.result);
                    }
                });
    }

    private void processData(String result) {

        ShopCart orderConfirmationBean = GsonTools.changeGsonToBean(result,
                ShopCart.class);
        if (orderConfirmationBean == null) {
            return;
        }

        if ("1".equals(orderConfirmationBean.getCode())) {
            if (orderConfirmationBean.getData() == null ||
                    orderConfirmationBean.getData().size() == 0) {
                return;
            }

            rl_order_confirmation_affirm_pay.setVisibility(View.VISIBLE);
            sv_order_detail.setVisibility(View.VISIBLE);
            dataEntity = orderConfirmationBean.getData().get(0);
            c_type = dataEntity.getConfirm_type();
            //联系人和手机设置
            ContactTelName contact = orderConfirmationBean.getContact();
            if (contact != null) {
                id = contact.getId();
                if (StringUtils.isNotNullString(id)) {
                    tv_order_detail_name.setText(contact.getName());
                    tv_order_detail_mobile.setText(contact.getMobile());
                    tv_not_null.setVisibility(View.GONE);
                }
            }
            school_phone = dataEntity.getSchool_phone();
            school_type = dataEntity.getSchool_type();
            //课室图片
            Glide.with(mContext).load(dataEntity.getPic_url()).error(R.drawable.bg_placeholder_4_3)
                    .into(iv_store_pic);
            //门店名字
            tv_item_detail_sname.setText(dataEntity.getSname());
            //门店地址
            tv_store_address.setText(dataEntity.getSaddress());
            //门店类型
            tv_item_detail_use_desc.setText(dataEntity.getType_desc() + "(最多容纳" + dataEntity.getMax_member() + "人)");
            //房间数量
            tv_item_detail_room_count.setText(
                    String.format(getString(R.string.format_multiply_rooms), dataEntity.getRoom_count()));
            //平日价格
            AvailablePrice availablePrice = dataEntity.getAvailable_price().get(0);
            tv_day_price.setText("¥" + availablePrice.getPrice_weekday());
            //周末价格
            tv_week_price.setText("¥" + availablePrice.getPrice_weekend());
            //房间数量
            tv_sum_room.setText("共" + dataEntity.getRoom_count() + "间,合计:");

            //旗舰店显示设置
            if ("1".equals(dataEntity.getSchool_type())) {
                //是旗舰店
                iv_self_support.setVisibility(View.VISIBLE);//显示旗舰店
                lr_order_detail_remind.setVisibility(View.VISIBLE);//显示温馨提示
                tv_order_conf_tips.setText(UIUtils.getString(R.string.txt_school_invoice));
                tv_order_conf_tips.setEnabled(false);
                rl_invoice.setVisibility(View.VISIBLE);
                ll_invoice_info.setVisibility(View.VISIBLE);
            } else {
                //非旗舰店
                iv_self_support.setVisibility(View.GONE);//隐藏旗舰店
                lr_order_detail_remind.setVisibility(View.GONE);//隐藏温馨提示
                if (StringUtils.isNullString(dataEntity.getSchool_phone())) {
                    store_tel = UIUtils.getString(R.string.txt_phone_number);
                } else {
                    store_tel = dataEntity.getSchool_phone();
                }
                String wholeStr = String.format(UIUtils.getString(R.string.format_order_phone_tip), store_tel);
                StringFormatUtil spanStr = new StringFormatUtil(this, wholeStr,
                        store_tel, R.color.blue, new StringFormatUtil.IOnClickListener() {
                    @Override
                    public void click() {
                        callPhone(store_tel);
                    }
                }).fillColor();
                tv_order_conf_tips.setHighlightColor(UIUtils.getColor(android.R.color.transparent));
                tv_order_conf_tips.setText(spanStr.getResult());
                tv_order_conf_tips.setEnabled(true);
                rl_invoice.setVisibility(View.GONE);
                ll_invoice_info.setVisibility(View.GONE);
            }

            //周末
            int sum_weekend = 0;
            //非周末
            int no_sum_weekend = 0;
            JSONArray array;
            try {
                array = new JSONArray(dataEntity.getRoom_info());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String date = jsonObject.getString("date");
                    if (StringUtils.isWeekend(date)) {
                        sum_weekend++;
                    } else {
                        no_sum_weekend++;
                    }
                }
            } catch (JSONException e) {
                return;
            }

            if (no_sum_weekend != 0 && sum_weekend != 0) {
                tv_use_day.setText("使用天数：" + (sum_weekend + no_sum_weekend) + "天"
                        + "(平日" + no_sum_weekend + "天,周末" + sum_weekend + "天)");
            } else if (no_sum_weekend == 0 && sum_weekend != 0) {
                tv_use_day.setText("使用天数：" + (sum_weekend + no_sum_weekend) + "天"
                        + "(周末" + sum_weekend + "天)");
            } else if (no_sum_weekend != 0 && sum_weekend == 0) {
                tv_use_day.setText("使用天数：" + (sum_weekend + no_sum_weekend) + "天"
                        + "(平日" + no_sum_weekend + "天)");
            }
            //订单信息
            if (!dataEntity.getStart_date().equals(dataEntity.getEnd_date())) {
                tv_order_info.setText("订单信息(多天)");
                tv_use_date.setText("使用日期：" + dataEntity.getStart_date() + "~" + dataEntity.getEnd_date());
                tv_use_week.setVisibility(View.VISIBLE);
                String repeat = dataEntity.getRepeat();
                if (StringUtils.isNullString(repeat)) {
                    tv_use_week.setText("循环方式：每天");
                } else {
                    tv_use_week.setVisibility(View.VISIBLE);
                    String[] repeats = repeat.split(",");
                    String week = getStrWeek(repeats);
                    tv_use_week.setText("循环方式：" + week);
                }
            } else {
                tv_order_info.setText("订单信息(单天)");
                tv_use_date.setText("使用日期：" + dataEntity.getStart_date());
                tv_use_week.setVisibility(View.GONE);
            }

            List<TimeGroup> time_group = dataEntity.getTime_group();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < time_group.size(); i++) {
                if (i == time_group.size() - 1) {
                    sb.append(String.format(
                            UIUtils.getString(R.string.to_symbol),
                            StringUtils.changeTime(time_group.get(i).getStart_time()),
                            StringUtils.changeTime(time_group.get(i).getEnd_time())
                    ));
                } else {
                    sb.append(String.format(
                            UIUtils.getString(R.string.to_symbol),
                            StringUtils.changeTime(time_group.get(i).getStart_time()),
                            StringUtils.changeTime(time_group.get(i).getEnd_time()))).append(",");
                }
            }

            tv_use_time.setText("使用时段：" + sb.toString());

            //取消课室和增加课室
            List<AdjustmentData> cancel_date = dataEntity.getRoom_adjust().getCancel_date();
            List<AdjustmentData> add_date = dataEntity.getRoom_adjust().getAdd_date();
            //取消
            List<AdjustmentData> newCancel_date = getNewCancel_date(cancel_date);
            if (newCancel_date != null && newCancel_date.size() != 0) {
                //设置
                StringBuilder addBuilder = getDateBuilder(newCancel_date);
                tv_cancel_classroom_date.setText(addBuilder.toString());
            } else {
                //隐藏
                tv_cancel_classroom.setVisibility(View.GONE);
            }
            //增加
            List<AdjustmentData> newCancel_date1 = getNewCancel_date(add_date);
            if (newCancel_date1 != null && newCancel_date1.size() != 0) {
                //设置
                StringBuilder addBuilder = getDateBuilder(newCancel_date1);
                tv_add_classroom_date.setText(addBuilder.toString());

            } else {
                //隐藏
                tv_add_classroom.setVisibility(View.GONE);
            }
            //如果增加和取消都没有都隐藏
            if (cancel_date != null && cancel_date.size() == 0 &&
                    add_date != null && add_date.size() == 0) {
                ll_edit_individuation.setVisibility(View.GONE);
            }


            //收费项目
            List<DeviceEntity> device_no_free = dataEntity.getDevice_nofree();
            handleDeviceNoFree(ll_no_free_device_add, device_no_free);
            //总费用
            fee = dataEntity.getFee();
            tv_sum_price.setText(String.format(UIUtils.getString(R.string.how_much_money), StringUtils.getDecimal(fee)));
            //收费费用
            tv_device_price.setText(String.format(UIUtils.getString(
                    R.string.how_much_money), StringUtils.getDecimal(dataEntity.getDevice_fee())));
            //课室费用
            Double class_fee = Double.valueOf(fee) - Double.valueOf(dataEntity.getDevice_fee());

            tv_course_price.setText(String.format(UIUtils.getString(
                    R.string.how_much_money), StringUtils.getDecimal(class_fee + "")));

            //门店id
            sid = dataEntity.getSid();
            //门店名字
            name = dataEntity.getSname();
            //发票联系人清空
            invoiceContacts.clear();
            //添加到发票联系人
            invoiceContacts.addAll(orderConfirmationBean.getInvoiceContactses());
            order2s.clear();
            order2s.add(dataEntity);

            //发票逻辑,用户未开过发票
            if (request == 0) {//是第一次请求需要这些判断
                last_invoice_type = orderConfirmationBean.getLast_invoice_type();
                if (invoiceContacts.size() == 0) {
                    //未开过发票，请求新增发票接口
                    getHttpUtils_new_contact();
                } else {
                    // 取出最近保存过的发票类型
                    if (!"0".equals(invoice_type_save)) {
                        last_invoice_type = invoice_type_save;
                    } else {
                        if ("0".equals(last_invoice_type)) {//如果最近提交的发票类型是0强制改成2
                            last_invoice_type = "2";
                        }
                    }

                    //1.普通 2.电子 3.专用
                    //获取发票信息的时候 1专用 2普通/电子
                    for (int i = 0; i < invoiceContacts.size(); i++) {
                        String typeid = invoiceContacts.get(i).getTypeid();
                        String id_contact = invoiceContacts.get(i).getId();
                        if ("1".equals(typeid)) {
                            //专用
                            if ("3".equals(last_invoice_type)) {
                                tv_invoice_type.setText(R.string.invoice_special);
                                tv_invoice_company_name.setText(invoiceContacts.get(i).getMc());
                                invoice_contact_id = id_contact;
                                invoice_type = 3;
                            }
                        } else {
                            //普通和电子
                            if ("1".equals(last_invoice_type)) {
                                tv_invoice_type.setText(R.string.invoice_paper);
                                tv_invoice_company_name.setText(invoiceContacts.get(i).getMc());
                                invoice_type = 1;
                            } else if ("2".equals(last_invoice_type)) {
                                tv_invoice_type.setText(R.string.invoice_electron);
                                tv_invoice_company_name.setText(invoiceContacts.get(i).getMc());
                                invoice_type = 2;
                            }
                            invoice_contact_id = id_contact;
                        }
                    }
                }

                request++;
            }
        } else {
            FailCodeControl.checkCode(orderConfirmationBean.getCode());
            UIUtils.showToastSafe(orderConfirmationBean.getMsg());
        }
    }

    @NonNull
    private StringBuilder getDateBuilder(List<AdjustmentData> add_date) {
        StringBuilder addBuilder = new StringBuilder();
        for (int i = 0; i < add_date.size(); i++) {
            if (i == add_date.size() - 1) {
                addBuilder.append(add_date.get(i).getDate());
            } else {
                addBuilder.append(add_date.get(i).getDate()).append("、");
            }
        }
        return addBuilder;
    }

    private String getStrWeek(String[] repeats) {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < repeats.length; i++) {
            if (i == (repeats.length - 1)) {
                stringBuilder.append(getWeekDay(Integer.valueOf(repeats[i])));
            } else {
                stringBuilder.append(getWeekDay(Integer.valueOf(repeats[i]))).append("、");
            }
        }
        return stringBuilder.toString();
    }

    private String getWeekDay(Integer integer) {

        String weekStr = null;
        switch (integer) {
            case 1:
                weekStr = "周一";
                break;
            case 2:
                weekStr = "周二";
                break;
            case 3:
                weekStr = "周三";
                break;
            case 4:
                weekStr = "周四";
                break;
            case 5:
                weekStr = "周五";
                break;
            case 6:
                weekStr = "周六";
                break;
            case 7:
                weekStr = "周日";
                break;
        }
        return weekStr;
    }


    /**
     * 新增发票请求
     */
    private void getHttpUtils_new_contact() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "add_invoice_contact");
        params.addBodyParameter("uid", StringUtils.getUid());
        params.addBodyParameter("typeid", "2");
        params.addBodyParameter("mc", getString(R.string.invoice_personal));

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData_new_contact(arg0.result);
                    }
                });
    }

    private void processData_new_contact(String result) {
        AddInvoiceContactBean addInvoiceContactBean = GsonTools.changeGsonToBean(result,
                AddInvoiceContactBean.class);
        if (addInvoiceContactBean == null) {
            return;
        }
        if ("1".equals(addInvoiceContactBean.getCode())) {
            sv_order_detail.setVisibility(View.VISIBLE);
            invoice_contact_id = addInvoiceContactBean.getInvoice_contact_id() + "";
            invoice_type = 2;
            // 保存最近保存过的发票类型
            SharedPreferencesUtils.saveString(this,
                    getResources().getString(R.string.shared_invoice_type) + StringUtils.getUid(),
                    "2");
        } else {
            UIUtils.showLongToastSafe(addInvoiceContactBean.getMsg());
        }


    }


    @Override
    public String getPageName() {
        return getString(R.string.title_act_order_confirmation);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_order_confirmation;
    }

    private List<AdjustmentData> getNewCancel_date(List<AdjustmentData> cancel_date) {
        List<AdjustmentData> newCancel_date = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (AdjustmentData adjustmentData : cancel_date) {
            if (adjustmentData == null) {
                continue;
            }
            String date = adjustmentData.getDate();
            if (StringUtils.isNotNullString(date)) {
                if (!set.contains(date)) {
                    set.add(date);
                    newCancel_date.add(adjustmentData);
                }
            }
        }
        set.clear();
        return newCancel_date;
    }


    /**
     * 收费设备_可选设备
     *
     * @param ll_parent      动态添加布局的父控件
     * @param device_no_free 收费设备数据源
     */
    private void handleDeviceNoFree(LinearLayout ll_parent, List<DeviceEntity> device_no_free) {
        if (device_no_free != null && device_no_free.size() > 0) {
            for (int i = 0; i < device_no_free.size(); i++) {
                LinearLayout layout = (LinearLayout) LayoutInflater.from(
                        UIUtils.getContext()).inflate(
                        R.layout.item_device, null);
                TextView tv_name = (TextView) layout.findViewById(R.id.tv_name);
                TextView tv_count = (TextView) layout.findViewById(R.id.tv_count);
                DeviceEntity dev = device_no_free.get(i);
                tv_name.setText(dev.getDevice_name());
                tv_count.setText(
                        String.format(
                                UIUtils.getString(R.string.multiply),
                                dev.getNum()
                        ));
                ll_parent.addView(layout, i);
            }
        } else {
            ll_optional_equipment.setVisibility(View.GONE);
        }
    }

    /**
     * 提交购物车请求
     *
     * @param remark         备注
     * @param contact_name   联系人名字
     * @param contact_mobile 联系人电话
     * @param coupon_id2     优惠券id
     * @param order2_id      二级订单id
     * @param index          角标默认为0(第一个)
     * @param flag           false为余额支付
     */
    private void commitCart(String remark, String contact_name,
                            String contact_mobile, String coupon_id2, String order2_id,
                            String index, final boolean flag) {
        ProgressDialog.getInstance().show();
        // 提交数据
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "commit_cart");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("remark", remark);
        params.addBodyParameter("contact_name", contact_name);
        params.addBodyParameter("contact_mobile", contact_mobile);
        params.addBodyParameter("coupon_id", coupon_id2);
        params.addBodyParameter("order2_ids", order2_id);
        params.addBodyParameter("index", index);
        if (flag) {
            params.addBodyParameter("pay_method", "6");// 6=易居支付 默认1=支付宝支付
        } else {
            params.addBodyParameter("pay_method", "5");
        }


        if (StringUtils.isNotNullString(invoice_contact_id)) {
            params.addBodyParameter("invoice_contact_id", invoice_contact_id);
            params.addBodyParameter("invoice_type", invoice_type + "");
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        JSONTokener jsonParser = new JSONTokener(arg0.result);
                        JSONObject person;
                        try {
                            person = (JSONObject) jsonParser.nextValue();
                            int code = person.getInt("code");
                            parseData(code, arg0.result, flag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 购物车请求后的解析
     *
     * @param code   返回状态码
     * @param result 输出结果
     * @param flag
     */
    private void parseData(int code, String result, boolean flag) {
        // 根据code码处理不同的数据
        conmmitInfo = GsonTools.changeGsonToBean(result, CartCommit.class);
        switch (code) {
            case 1:
                if (null != conmmitInfo) {
                    if (conmmitInfo.getMsg().equals("ok")) {
                        //"confirm_type": "2" 确认类型 0=无需确认 1=支付前确认 2=支付后确认 0,2跳转收银台 1跳转待确认
                        ProgressDialog.getInstance().dismiss();
                        confirm_type = conmmitInfo.getConfirm_type();
                        if ("0".equals(confirm_type) || "2".equals(confirm_type)) {
                            if (flag) {
                                //判断之前是否初始化
                                EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
                                    @Override
                                    public void onSuccess() {
                                        // 调取易支付创建订单报文签名接口
                                        getRequestCreateOrder(conmmitInfo.getTrade_id());
                                    }
                                });
                            } else {
                                checkPassword();
                            }

                        } else {
                            //提交成功结果页
                            jumpResult(ClassroomPayResultActivity.TYPE_COMMIT_SUCCESS,
                                    ClassroomPayResultActivity.ENTRANCE_AFFIRM,
                                    conmmitInfo.getRoom_count() + "",
                                    conmmitInfo.getType_desc(),
                                    conmmitInfo.getOrder1_id() + "");
                        }
                        //支付宝支付相关处理
//                        getAliPaySign();

                    }
                }
                break;
            case 50025:
                // 数据提交失败，房间不足/设备不足
                CustomDialog customDialog = new CustomDialog(
                        OrderConfirmationActivity.this,
                        "重新预订",
                        "返回首页",
                        "页面停留时间过长,课室库存已被占用,请重新预订");
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            finish();
                        } else {
                            Intent intentIndex = new Intent(OrderConfirmationActivity.this, MainActivity.class);
                            intentIndex.putExtra(MainActivity.Param_Start_Fragment, 0);
                            startActivity(intentIndex);
                        }
                    }
                });

                break;
            case 50027:
                // 数据提交失败，订单过期(将订单删除)
                CustomDialog customDialog2 = new CustomDialog(
                        OrderConfirmationActivity.this,
                        "重新预订",
                        "返回首页",
                        "页面停留时间过长,当前时间离课室开始时间不足30分钟,请重新预订");
                customDialog2.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            finish();
                        } else {
                            Intent intentIndex = new Intent(OrderConfirmationActivity.this, MainActivity.class);
                            intentIndex.putExtra(MainActivity.Param_Start_Fragment, 0);
                            startActivity(intentIndex);
                        }
                    }
                });
                break;
            case 50011:
                UIUtils.showToastSafe(conmmitInfo.getMsg());
                break;
            case 8888:
                UIUtils.showToastSafe(getString(R.string.toast_server_busy));
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
                    OrderConfirmationActivity.this,
                    createOrderResult.getBody(),
                    createOrderResult.getSign(),
                    new IPayResult() {
                        @Override
                        public void payResult(int code, String msg, String dataJson) {
                            if (code == EjuPayResultCode.PAY_SUCCESS_CODE.getCode()) {
                                //支付成功
//                                if ("0".equals(confirm_type)) {
//                                    //0 无需确认
//                                } else {
//                                    //2 支付后确认
//                                }
                                jumpResult(ClassroomPayResultActivity.TYPE_PAY_SUCCESS,
                                        ClassroomPayResultActivity.ENTRANCE_AFFIRM,
                                        conmmitInfo.getRoom_count() + "",
                                        conmmitInfo.getType_desc(),
                                        conmmitInfo.getOrder1_id() + "");
                            } else if (code == EjuPayResultCode.PAY_FAIL_CODE.getCode()) {
                                //支付失败
//                                if ("0".equals(confirm_type)) {
//                                    //0 无需确认
//                                } else {
//                                    //2 支付后确认
//                                }
                                jumpResult(ClassroomPayResultActivity.TYPE_PAY_FAIL,
                                        ClassroomPayResultActivity.ENTRANCE_AFFIRM,
                                        conmmitInfo.getRoom_count() + "",
                                        conmmitInfo.getType_desc(),
                                        conmmitInfo.getOrder1_id() + "");
                            } else if (code == EjuPayResultCode.PAY_CANCEL_C0DE.getCode()) {
                                //支付取消
//                                if ("0".equals(confirm_type)) {
//                                    //0 无需确认
//                                } else {
//                                    //2 支付后确认
//                                }
                                jumpResult(ClassroomPayResultActivity.TYPE_PAY_FAIL,
                                        ClassroomPayResultActivity.ENTRANCE_AFFIRM,
                                        conmmitInfo.getRoom_count() + "",
                                        conmmitInfo.getType_desc(),
                                        conmmitInfo.getOrder1_id() + "");
                            }
                        }
                    }
            );
        }
    }

    /**
     * 跳转结果页
     *
     * @param type       1.支付失败,2.支付成功,3.提价成功
     * @param entrance   1.订单确认页 ,2.列表,详情
     * @param room_count 房间数量
     * @param type_desc  迷你间等
     * @param oid        订单id
     */
    private void jumpResult(int type, int entrance, String room_count, String type_desc, String oid) {
        Intent intent = new Intent(this, ClassroomPayResultActivity.class);
        intent.putExtra(ExtraControl.EXTRA_TYPE, type);
        intent.putExtra(ExtraControl.EXTRA_ENTRANCE, entrance);
        intent.putExtra(ExtraControl.EXTRA_ROOM_COUNT, room_count);
        intent.putExtra(ExtraControl.EXTRA_TYPE_DESC, type_desc);
        intent.putExtra(ExtraControl.EXTRA_ORDER_ID, oid);
        startActivity(intent);
    }

    /**
     * 跳转余额支付
     */
    private void jumpPayPage() {
        String currentFee = tv_sum_price.getText().toString().substring(1);
        Intent intent = new Intent(this, PayPasswordActivity.class);
        intent.putExtra(ExtraControl.EXTRA_ROOM_COUNT, conmmitInfo.getRoom_count() + "");
        intent.putExtra(ExtraControl.EXTRA_TYPE_DESC, conmmitInfo.getType_desc());
        intent.putExtra(ExtraControl.EXTRA_ORDER_ID, conmmitInfo.getOrder1_id() + "");
        intent.putExtra(ExtraControl.EXTRA_CONFIRM_TYPE, conmmitInfo.getConfirm_type());
        intent.putExtra(ExtraControl.EXTRA_ENTRANCE, ClassroomPayResultActivity.ENTRANCE_AFFIRM);
        intent.putExtra(ExtraControl.EXTRA_BALANCE, currentFee);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:// 联系人
                //联系人的位置
                id = data.getStringExtra("id");
                if (id != null) {
                    String name = data.getStringExtra("name");
                    String tel = data.getStringExtra("tel");
                    tv_order_detail_name.setText(name);
                    tv_order_detail_mobile.setText(tel);
                    tv_not_null.setVisibility(View.GONE);
                } else {
                    tv_order_detail_name.setText("");
                    tv_order_detail_mobile.setText("");
                    tv_not_null.setVisibility(View.VISIBLE);
                }
                break;
            case 2:// 优惠券
                //优惠券金额
                String couponPrice = data.getStringExtra("couponPrice");
                if (!"0.00".equals(couponPrice)) {
                    String batch_name = data.getStringExtra("batch_name");
                    //设置优惠券文案
                    tv_coupon.setText(batch_name);
                    //优惠券id
                    coupon_id = data.getStringExtra("coupon_id");
                    //应付金额
                    Double sumFee = Double.valueOf(fee) - Double.valueOf(couponPrice);
                    tv_sum_price.setText(String.format(UIUtils.getString(R.string.how_much_money), sumFee));
                }
                break;
            case 3://发票
                getHttpUtils();
                invoice_type = data.getIntExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_TYPE, 0);
                String head = data.getStringExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_HEAD);
                invoice_contact_id = data.getStringExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_ID);
                last_invoice_type = invoice_type + "";
                switch (invoice_type) {
                    case 1:
                        tv_invoice_type.setText(R.string.invoice_paper);
                        break;
                    case 2:
                        tv_invoice_type.setText(R.string.invoice_electron);
                        break;
                    case 3:
                        tv_invoice_type.setText(R.string.invoice_special);
                        break;
                }
                tv_invoice_company_name.setText(head);
                tv_invoice_company_xmmc.setText(R.string.invoice_rental_fee);
//                tv_invoice.setText("");
                ll_invoice_info.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_242");
                onBackPressed();
                break;
            case R.id.tv_order_conf_tips:
                callPhone(store_tel);
                break;
            case R.id.head_right_relative://客服热线
                //旗舰店显示设置
                if ("1".equals(dataEntity.getSchool_type())) {
                    //是旗舰店
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_240");
                } else {
                    //非旗舰店
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_241");
                }
                // 弹出电话呼叫窗口
                if (StringUtils.isNullString(school_phone)) {
                    school_phone = UIUtils.getString(R.string.txt_phone_number);
                }
                callPhone(school_phone);
                break;
            case R.id.rl_name_tel://联系人
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_226");
                Intent intent = new Intent(this, ContactShopCartActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 0);
                break;
            case R.id.rl_coupon://优惠券
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_238");
                Intent intentCoupon = new Intent(this, CouponActivity.class);
                //2级订单id
                intentCoupon.putExtra("order2_id", order2_id);
                intentCoupon.putExtra("uid", StringUtils.getUid());
                startActivityForResult(intentCoupon, 0);
                break;
            case R.id.lr_order_detail_remind://提示
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_239");
                Intent pledgeIntent = new Intent(this, Pledge_Activity.class);
                startActivity(pledgeIntent);
                break;
            case R.id.btn_affirm_pay://确认付款
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_243");
                String mobile = tv_order_detail_mobile.getText().toString();
                if (StringUtils.isNotNullString(mobile)) {
                    if ("1".equals(c_type)) {
                        commitCart(et_remark.getText().toString(),
                                tv_order_detail_name.getText().toString(),
                                tv_order_detail_mobile.getText().toString(),
                                coupon_id, order2_id, "0", false);
                    } else {
                        if ("1".equals(school_type)) {
                            String currentFee = tv_sum_price.getText().toString().substring(1);
                            mPopupWindow = PayWayUtil.getPopu(OrderConfirmationActivity.this.getWindow(),
                                    OrderConfirmationActivity.this,
                                    R.layout.activity_order_confirmation,
                                    balance, currentFee, new PayWayOnClickListener() {
                                        @Override
                                        public void onBalanceClick() {
                                            MobclickAgent.onEvent(UIUtils.getContext(), "v3200_245");
                                            commitCart(et_remark.getText().toString(),
                                                    tv_order_detail_name.getText().toString(),
                                                    tv_order_detail_mobile.getText().toString(),
                                                    coupon_id, order2_id, "0", false);
                                        }

                                        @Override
                                        public void onOtherClick() {
                                            MobclickAgent.onEvent(UIUtils.getContext(), "v3200_244");
                                            mPopupWindow.dismiss();
                                            commitCart(et_remark.getText().toString(),
                                                    tv_order_detail_name.getText().toString(),
                                                    tv_order_detail_mobile.getText().toString(),
                                                    coupon_id, order2_id, "0", true);
                                        }
                                    });
                        } else {
                            commitCart(et_remark.getText().toString(),
                                    tv_order_detail_name.getText().toString(),
                                    tv_order_detail_mobile.getText().toString(),
                                    coupon_id, order2_id, "0", true);
                        }
                    }
                } else {
                    showSmileToast(getString(R.string.toast_select_contact));
                }
                break;
//            case R.id.tv_item_detail_price://费用清单
//                Intent intent_price = new Intent(
//                        this,
//                        Common_Show_WebPage_Activity.class);
//                intent_price.putExtra(
//                        UIUtils.getString(R.string.redirect_open_url),
//                        UrlUtils.SERVER_WEB_LISTFREE + "oid2=" + order2_id);
//                intent_price.putExtra(
//                        UIUtils.getString(R.string.get_page_name),
//                        WebConstant.Expense_list_Page);
//                UIUtils.startActivity(intent_price);
//                break;
            case R.id.rl_invoice://发票
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_237");
                Intent intent_invoice = new Intent(this, WriteInvoiceInformationActivity.class);
                intent_invoice.putExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_INFO, invoiceContacts);
                intent_invoice.putExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_ID, invoice_contact_id);//未开过发票的话是要传进去的
                intent_invoice.putExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_LAST_TYPE, last_invoice_type);
                startActivityForResult(intent_invoice, 0);
                break;
//            case tv_order_detail_money://课程费用
//                Intent intent_rooms = new Intent(this, Common_Show_WebPage_Activity.class);
//                // url
//                intent_rooms.putExtra(UIUtils.getString(R.string.redirect_open_url),
//                        UrlUtils.SERVER_WEB_STORE_PRICE + "oid2=" + dataEntity.getId());
//                intent_rooms.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "课室费用明细");
//                startActivity(intent_rooms);
//                break;
//            case tv_device_fee://设备费用
//                Intent intent_device = new Intent(this, Common_Show_WebPage_Activity.class);
//                // url
//                intent_device.putExtra(UIUtils.getString(R.string.redirect_open_url),
//                        UrlUtils.SERVER_WEB_DEVICE_PRICE + "oid2=" + dataEntity.getId());
//                intent_device.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "收费设备费用明细");
//                startActivity(intent_device);
//                break;
            case R.id.tv_item_detail_sname://跳转门店详情
                /*Intent intent_store = new Intent(UIUtils.getContext(),
                        IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sid", dataEntity.getSid());
                bundle.putString("name", dataEntity.getSname());
                bundle.putString("address", dataEntity.getSaddress());
                bundle.putString("tags", dataEntity.getSchool_tags());
                intent_store.putExtras(bundle);
                startActivity(intent_store);*/
                break;
            case R.id.rl_class_fee://课室费用
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_235");
                Intent intent_rooms = new Intent(this, Common_Show_WebPage_Activity.class);
                // url
                intent_rooms.putExtra(UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_STORE_PRICE + "oid2=" + dataEntity.getId());
                intent_rooms.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "课室费用明细");
                startActivity(intent_rooms);
                break;
            case R.id.rl_charge://收费项目
                Intent intent_device = new Intent(this, Common_Show_WebPage_Activity.class);
                // url
                intent_device.putExtra(UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_DEVICE_PRICE + "oid2=" + dataEntity.getId());
                intent_device.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "收费设备费用明细");
                startActivity(intent_device);
                break;
        }
    }

    private void callPhone(final String store_tel) {
        CustomDialog customDialog_phone = new CustomDialog(
                OrderConfirmationActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                store_tel
        );
        customDialog_phone.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    if (!PermissionsChecker.checkPermission(PermissionsChecker.CALL_PHONE_PERMISSIONS)) {
                        // 跳转系统电话
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:" + store_tel));
                        startActivity(intent);
                    } else {
                        PermissionsChecker.requestPermissions(
                                OrderConfirmationActivity.this,
                                PermissionsChecker.CALL_PHONE_PERMISSIONS
                        );
                    }
                }
            }
        });
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


    /**
     * 打印带图的吐司
     *
     * @param s 文字信息
     */
    private void showSmileToast(String s) {
        ImageSpan span = new ImageSpan(this, R.drawable.eye_icon);
        SpannableString ss = new SpannableString(s + "[smile]");
        ss.setSpan(span, s.length(), s.length() + "[smile]".length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        Toast.makeText(this, ss, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        CustomDialog customDialog = new CustomDialog(
                OrderConfirmationActivity.this,
                UIUtils.getString(R.string.dialog_show_means),
                UIUtils.getString(R.string.dialog_show_think_again),
                UIUtils.getString(R.string.dialog_show_give_up_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    cancelOrder(order2_id, sid);
                    finish();
                }
            }
        });
    }

    /**
     * 取消订单请求
     *
     * @param oid 订单id
     * @param sid 学校id
     */
    private void cancelOrder(String oid, String sid) {
        // 取消订单
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "cart_cancel");
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("sid", sid);
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_RESERVATION,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                    }

                });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
