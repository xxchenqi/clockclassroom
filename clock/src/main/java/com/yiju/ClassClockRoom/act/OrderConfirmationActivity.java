package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.AddInvoiceContactBean;
import com.yiju.ClassClockRoom.bean.AdjustmentData;
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.bean.ContactTelName;
import com.yiju.ClassClockRoom.bean.CreateOrderResult;
import com.yiju.ClassClockRoom.bean.DeviceEntity;
import com.yiju.ClassClockRoom.bean.InvoiceContacts;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.ShopCart;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

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
    //订单所有内容
    @ViewInject(R.id.sv_order_detail)
    private ScrollView sv_order_detail;
    //付款布局
    @ViewInject(R.id.rl_order_confirmation_affirm_pay)
    private LinearLayout rl_order_confirmation_affirm_pay;
    //店名
    @ViewInject(R.id.tv_item_detail_sname)
    private TextView tv_item_detail_sname;
    //用途
    @ViewInject(R.id.tv_item_detail_use_desc)
    private TextView tv_item_detail_use_desc;
    //数量
    @ViewInject(R.id.tv_item_detail_room_count)
    private TextView tv_item_detail_room_count;
    //日期
    @ViewInject(R.id.tv_item_detail_date)
    private TextView tv_item_detail_date;
    //星期
    @ViewInject(R.id.tv_item_detail_repeat)
    private TextView tv_item_detail_repeat;
    //时间
    @ViewInject(R.id.tv_item_detail_time)
    private TextView tv_item_detail_time;
    //可选设备列表
    @ViewInject(R.id.ll_no_free_device_add)
    private LinearLayout ll_no_free_device_add;
    //个性化调整
    @ViewInject(R.id.ll_up_down)
    private LinearLayout ll_up_down;
    //个性化调整区域块
    @ViewInject(R.id.ll_edit_individuation)
    private LinearLayout ll_edit_individuation;
    //取消课室
    @ViewInject(R.id.tv_cancel_classroom)
    private TextView tv_cancel_classroom;
    // 增加课室
    @ViewInject(R.id.tv_add_classroom)
    private TextView tv_add_classroom;
    //取消课室列表
    @ViewInject(R.id.ll_cancel_classroom)
    private LinearLayout ll_cancel_classroom;
    //增加课室列表
    @ViewInject(R.id.ll_add_classroom)
    private LinearLayout ll_add_classroom;
    //订单备注
    @ViewInject(R.id.et_remark)
    private EditText et_remark;
    //联系人名字
    @ViewInject(R.id.tv_order_detail_name)
    private TextView tv_order_detail_name;
    //联系人电话
    @ViewInject(R.id.tv_order_detail_mobile)
    private TextView tv_order_detail_mobile;
    //课程费用
    @ViewInject(R.id.tv_order_detail_money)
    private TextView tv_order_detail_money;
    //收费设备费用
    @ViewInject(R.id.tv_device_fee)
    private TextView tv_device_fee;
    //优惠金额
    @ViewInject(R.id.tv_privilege_price)
    private TextView tv_privilege_price;
    //待付金额
    @ViewInject(R.id.tv_ought_pay)
    private TextView tv_ought_pay;
    //合计金额
//    @ViewInject(R.id.tv_cart_price)
//    private TextView tv_cart_price;
    //必填文案
    @ViewInject(R.id.tv_not_null)
    private TextView tv_not_null;
    //优惠券
    @ViewInject(R.id.rl_coupon)
    private RelativeLayout rl_coupon;
    //提醒信息布局
    @ViewInject(R.id.lr_order_detail_remind)
    private LinearLayout lr_order_detail_remind;
    //优惠券内容
    @ViewInject(R.id.tv_coupon)
    private TextView tv_coupon;
    //联系人布局
    @ViewInject(R.id.rl_name_tel)
    private RelativeLayout rl_name_tel;
    //确认付款
    @ViewInject(R.id.btn_affirm_pay)
    private Button btn_affirm_pay;
    //可选设备布局
    @ViewInject(R.id.ll_optional_equipment)
    private LinearLayout ll_optional_equipment;
    //分割线
    @ViewInject(R.id.v_order_divider)
    private View v_order_divider;
    //旗舰店图片
    @ViewInject(R.id.iv_self_support)
    private ImageView iv_self_support;
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
    @ViewInject(R.id.tv_invoice)
    private TextView tv_invoice;
    //提醒电话
    @ViewInject(R.id.ll_lr_order_detail_remind_phone)
    private LinearLayout ll_lr_order_detail_remind_phone;
    //手机
    @ViewInject(R.id.tv_store_phone)
    private TextView tv_store_phone;
    //门店图片
    @ViewInject(R.id.iv_store_pic)
    private ImageView iv_store_pic;
    //优惠券金额显示
    @ViewInject(R.id.rl_coupon_money)
    private RelativeLayout rl_coupon_money;
    //设备费用
    @ViewInject(R.id.rl_device)
    private RelativeLayout rl_device;

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
    //uid
    private String invoice_type_save;
    private String uid;

    @Override
    public void initIntent() {
        super.initIntent();
        order2_id = getIntent().getStringExtra("order2_id");
    }

    @Override
    protected void initView() {
        uid = StringUtils.getUid();
        et_remark.setFilters(new InputFilter[]{CommonUtil.getTextLengthFilter(100)});
        order2s = new ArrayList<>();
        invoiceContacts = new ArrayList<>();
        invoice_type_save = SharedPreferencesUtils.getString(this,
                getResources().getString(R.string.shared_invoice_type) + uid, "0");
    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.order_affirm));
        getHttpUtils();
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
//        head_right_relative.setOnClickListener(this);
        rl_name_tel.setOnClickListener(this);
        rl_coupon.setOnClickListener(this);
        lr_order_detail_remind.setOnClickListener(this);
        btn_affirm_pay.setOnClickListener(this);
//        tv_item_detail_price.setOnClickListener(this);
        rl_invoice.setOnClickListener(this);
        tv_store_phone.setOnClickListener(this);
//        tv_item_detail_sname.setOnClickListener(this);
        tv_order_detail_money.setOnClickListener(this);
        tv_device_fee.setOnClickListener(this);
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
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("order2_id", order2_id);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_RESERVATION, params,
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
            dataEntity = orderConfirmationBean.getData().get(0);
            tv_item_detail_sname.setText(dataEntity.getSname());
            tv_item_detail_use_desc.setText(dataEntity.getType_desc() + "(最多容纳人数" + dataEntity.getMax_member() + "人)");
            tv_item_detail_room_count.setText(
                    String.format(
                            getString(R.string.format_multiply_rooms),
                            dataEntity.getRoom_count()
                    ));
            tv_item_detail_date.setText(replaceDate(dataEntity.getStart_date()) + " - " + replaceDate(dataEntity.getEnd_date()));
            tv_item_detail_use_desc.setText(dataEntity.getType_desc() + "(最多容纳" + dataEntity.getMax_member() + "人)");
            tv_item_detail_room_count.setText(
                    String.format(
                            getString(R.string.multiply),
                            dataEntity.getRoom_count()
                    ));
            tv_item_detail_date.setText(
                    String.format(
                            getString(R.string.to_symbol),
                            dataEntity.getStart_date(),
                            dataEntity.getEnd_date()
                    ));
            tv_item_detail_repeat.setText(getRepeatWeek(dataEntity.getRepeat()));
            tv_item_detail_time.setText(StringUtils.changeTime(dataEntity.getStart_time()) + " - "
                    + StringUtils.changeTime(dataEntity.getEnd_time()));
            //旗舰店显示设置
            if ("1".equals(dataEntity.getSchool_type())) {
                iv_self_support.setVisibility(View.VISIBLE);
                lr_order_detail_remind.setVisibility(View.VISIBLE);
                ll_lr_order_detail_remind_phone.setVisibility(View.GONE);
            } else {
                iv_self_support.setVisibility(View.GONE);
                lr_order_detail_remind.setVisibility(View.GONE);
                ll_lr_order_detail_remind_phone.setVisibility(View.VISIBLE);
                tv_store_phone.setText(dataEntity.getSchool_phone());
            }
            Glide.with(mContext).load(dataEntity.getPic_url()).error(R.drawable.clock_wait)
                    .into(iv_store_pic);
            tv_item_detail_time.setText(
                    String.format(
                            getString(R.string.to_symbol),
                            StringUtils.changeTime(dataEntity.getStart_time()),
                            StringUtils.changeTime(dataEntity.getEnd_time())
                    ));

            //个性化调整
            //取消课室和增加课室
            List<AdjustmentData> cancel_date = dataEntity.getRoom_adjust().getCancel_date();
            List<AdjustmentData> add_date = dataEntity.getRoom_adjust().getAdd_date();
            if (cancel_date != null && cancel_date.size() != 0) {
                //设置
                handleClassRoomCancel(ll_cancel_classroom, cancel_date);
            } else {
                //隐藏
                tv_cancel_classroom.setVisibility(View.GONE);
            }
            if (add_date != null && add_date.size() != 0) {
                //设置
                handleClassRoomCancel(ll_add_classroom, add_date);
            } else {
                //隐藏
                tv_add_classroom.setVisibility(View.GONE);
            }

            //如果增加和取消都没有都隐藏
            if (cancel_date != null && cancel_date.size() == 0 &&
                    add_date != null && add_date.size() == 0) {
                ll_up_down.setVisibility(View.GONE);
                ll_edit_individuation.setVisibility(View.GONE);
            }

            //可选设备
            List<DeviceEntity> device_no_free = dataEntity.getDevice_nofree();
            handleDeviceNoFree(ll_no_free_device_add, device_no_free);

            fee = dataEntity.getFee();
            Double courseFee = Double.valueOf(fee) - Double.valueOf(dataEntity.getDevice_fee());

            //课程费用
            tv_order_detail_money.setText(String.format(UIUtils.getString(
                    R.string.how_much_money), StringUtils.getDecimal(courseFee + "")));
            //设备费用
            if (!"0.00".equals(dataEntity.getDevice_fee())) {
                tv_device_fee.setText(String.format(UIUtils.getString(
                        R.string.how_much_money), StringUtils.getDecimal(dataEntity.getDevice_fee())));
            } else {
                rl_device.setVisibility(View.GONE);
            }
            //优惠金额
            tv_privilege_price.setText(String.format(getString(R.string.negative_how_much_money), "0.00"));
            //应付金额
            tv_ought_pay.setText(String.format(UIUtils.getString(
                    R.string.how_much_money), StringUtils.getDecimal(dataEntity.getFee())));

            sid = dataEntity.getSid();
            name = dataEntity.getSname();

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

            invoiceContacts.clear();
            order2s.clear();
            invoiceContacts.addAll(orderConfirmationBean.getInvoiceContactses());
            order2s.add(dataEntity);
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
                sv_order_detail.setVisibility(View.VISIBLE);
                request++;
            }
        } else {
            UIUtils.showToastSafe(orderConfirmationBean.getMsg());
        }
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

    /*
          *替换日期格式
  */
    private String replaceDate(String Date) {
        return Date.replaceAll("-", "/");
    }

    /*
   * 重复日期拼接
   */
    private String getRepeatWeek(String repeat) {
        // repeat: "2,3,4,5,6"
        if ("".equals(repeat)) {
            return "每天";
        } else {
            String[] repeats = repeat.split(",");
            StringBuilder sb = new StringBuilder();
            String week = "";

            for (int i = 0; i < repeats.length; i++) {
                Integer valueOf = Integer.valueOf(repeats[i]);

                switch (valueOf) {
                    case 1:
                        week = "周一";
                        break;
                    case 2:
                        week = "周二";
                        break;
                    case 3:
                        week = "周三";
                        break;
                    case 4:
                        week = "周四";
                        break;
                    case 5:
                        week = "周五";
                        break;
                    case 6:
                        week = "周六";
                        break;
                    case 7:
                        week = "周日";
                        break;
                    default:
                        break;
                }

                if (i == repeats.length - 1) {
                    sb.append(week);
                } else {
                    sb.append(week).append("、");
                }

            }
            return sb.toString();

        }
    }

    /**
     * 取消课室
     *
     * @param ll_parent   动态添加布局的父控件
     * @param cancel_date 取消课室的日期数据源
     */
    private void handleClassRoomCancel(LinearLayout ll_parent, List<AdjustmentData> cancel_date) {
        if (cancel_date != null && cancel_date.size() != 0) {
            for (int i = 0; i < cancel_date.size(); i++) {
                LinearLayout cancel_layout = (LinearLayout) LayoutInflater.from(
                        UIUtils.getContext()).inflate(
                        R.layout.item_adjustment_date, null);
                TextView tv_adjustment_date = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_date);
                TextView tv_adjustment_time = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_time);
                TextView tv_adjustment_count = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_count);
                AdjustmentData adjustmentData = cancel_date.get(i);
                tv_adjustment_date.setText(adjustmentData.getDate());
                tv_adjustment_time.setText(
                        String.format(
                                getString(R.string.to_symbol),
                                StringUtils.changeTime(adjustmentData.getStart_time()),
                                StringUtils.changeTime(adjustmentData.getEnd_time())
                        ));

//                tv_adjustment_count.setText("x" + adjustmentData.getRoom_count()+ "间");
//                        changeTime(adjustmentData.getStart_time())
//                                + " - " + changeTime(adjustmentData.getEnd_time())
//                );
                tv_adjustment_count.setText(
                        String.format(
                                getString(R.string.format_multiply_rooms),
                                adjustmentData.getRoom_count()
                        ));
                ll_parent.addView(cancel_layout, i);
            }
        }
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
            v_order_divider.setVisibility(View.GONE);
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
     */
    private void commitCart(String remark, String contact_name,
                            String contact_mobile, String coupon_id2, String order2_id,
                            String index) {
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
        params.addBodyParameter("pay_method", "6");// 6=易居支付 默认1=支付宝支付

        if (StringUtils.isNotNullString(invoice_contact_id)) {
            params.addBodyParameter("invoice_contact_id", invoice_contact_id);
            params.addBodyParameter("invoice_type", invoice_type + "");
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COMMIT_CART, params,
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
                        JSONObject person;
                        try {
                            person = (JSONObject) jsonParser.nextValue();
                            int code = person.getInt("code");
                            parseData(code, arg0.result);
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
     */
    private void parseData(int code, String result) {
        // 根据code码处理不同的数据
        conmmitInfo = GsonTools.changeGsonToBean(result, CartCommit.class);
        switch (code) {
            case 1:
                if (null != conmmitInfo) {
                    if (conmmitInfo.getMsg().equals("ok")) {
                        //"confirm_type": "2" 确认类型 0=无需确认 1=支付前确认 2=支付后确认 0,2跳转收银台 1跳转待确认
                        confirm_type = conmmitInfo.getConfirm_type();
                        if ("0".equals(confirm_type) || "2".equals(confirm_type)) {
                            //判断之前是否初始化
                            EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
                                @Override
                                public void onSuccess() {
                                    // 调取易支付创建订单报文签名接口
                                    getRequestCreateOrder(conmmitInfo.getTrade_id());
                                }
                            });
                        } else {
                            //提交成功结果页
                            jumpNewResult(1);
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
                            intentIndex.putExtra("backhome", "3");
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
                            intentIndex.putExtra("backhome", "3");
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
                                if ("0".equals(confirm_type)) {
                                    //0 无需确认
                                    jumpSucess();
                                } else {
                                    //2 支付后确认
                                    jumpNewResult(2);
                                }
                            } else if (code == EjuPayResultCode.PAY_FAIL_CODE.getCode()) {
                                //支付失败
                                if ("0".equals(confirm_type)) {
                                    //0 无需确认
                                    jumpFail();
                                } else {
                                    //2 支付后确认
                                    jumpNewResult(3);
                                }
                            } else if (code == EjuPayResultCode.PAY_CANCEL_C0DE.getCode()) {
                                //支付取消
                                if ("0".equals(confirm_type)) {
                                    //0 无需确认
                                    jumpFail();
                                } else {
                                    //2 支付后确认
                                    jumpNewResult(3);
                                }
                            }
                        }
                    }
            );
        }
    }

    /**
     * 失败跳转
     */
    private void jumpFail() {
        Intent intentFail = new Intent(OrderConfirmationActivity.this,
                AliPay_PayFailActivity.class);
        intentFail.putExtra("fail", conmmitInfo);
        intentFail.putExtra("sid", sid);
        intentFail.putExtra("name", name);
        startActivity(intentFail);
    }

    /**
     * 成功跳转
     */
    private void jumpSucess() {
        Intent intentSucess = new Intent(OrderConfirmationActivity.this,
                AliPay_PayOKActivity.class);
        intentSucess.putExtra("sucess", conmmitInfo);
        intentSucess.putExtra("sid", sid);
        intentSucess.putExtra("name", name);
        intentSucess.putExtra("order2", order2s);
        startActivity(intentSucess);
    }


    /**
     * type: 1 提交成功 2.支付成功 3.支付失败
     * 跳转到新的结果页
     */
    private void jumpNewResult(int type) {
        Intent intent = new Intent(OrderConfirmationActivity.this,
                NewPayResultActivity.class);
        intent.putExtra("conmmitInfo", conmmitInfo);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    /**
     * 取消支付处理
     */
   /* private void payCancel() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action",
                "order1_status");
        params.addBodyParameter("oid",
                conmmitInfo.getOrder1_id() + "");
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpRequest.HttpMethod.POST,
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
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        CouponUse isYes = GsonTools.changeGsonToBean(arg0.result, CouponUse.class);
                        if (null != isYes) {
                            if (isYes.getCode() == 1 && isYes.getMsg().equals("ok")) {
                                Integer isInt = isYes.getData();
                                if (isInt == 1) {
                                    jumpSucess();
                                } else {
                                    jumpFail();
                                }
                            }
                        }
                    }

                }
        );
    }*/
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
                    rl_coupon_money.setVisibility(View.VISIBLE);
                    String batch_name = data.getStringExtra("batch_name");
                    tv_coupon.setText(batch_name);
                    //优惠券id
                    coupon_id = data.getStringExtra("coupon_id");
                    //设置优惠金额
                    tv_privilege_price.setText(String.format(UIUtils.getString(
                            R.string.negative_rmb_how_much), couponPrice));
                    Double sumFee = Double.valueOf(fee) - Double.valueOf(couponPrice);
                    //设置应付金额
                    tv_ought_pay.setText(String.format(UIUtils.getString(
                            R.string.how_much_money), StringUtils.getDecimal(sumFee + "")));
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
                tv_invoice.setText("");
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
                onBackPressed();
                break;
            case R.id.head_right_relative://添加购物车
                CustomDialog customDialog = new CustomDialog(
                        OrderConfirmationActivity.this,
                        UIUtils.getString(R.string.dialog_show_goon_reservation),
                        UIUtils.getString(R.string.dialog_show_complete_pay),
                        UIUtils.getString(R.string.dialog_show_reservation_complete));
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            Intent intentIndex = new Intent(OrderConfirmationActivity.this, MainActivity.class);
                            intentIndex.putExtra("backhome", "3");
                            startActivity(intentIndex);
                        } else {
                            Intent intentIndex = new Intent(OrderConfirmationActivity.this, MainActivity.class);
                            intentIndex.putExtra("backhome_jump_shopcart", "3");
                            startActivity(intentIndex);
                        }
                    }
                });
                break;
            case R.id.rl_name_tel://联系人
                Intent intent = new Intent(this, ContactShopCartActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 0);
                break;
            case R.id.rl_coupon://优惠券
                Intent intentCoupon = new Intent(this, CouponActivity.class);
                //2级订单id
                intentCoupon.putExtra("order2_id", order2_id);
                intentCoupon.putExtra("uid", StringUtils.getUid());
                startActivityForResult(intentCoupon, 0);
                break;
            case R.id.lr_order_detail_remind://提示
                Intent pledgeIntent = new Intent(this, Pledge_Activity.class);
                startActivity(pledgeIntent);
                break;
            case R.id.btn_affirm_pay://确认付款
                String mobile = tv_order_detail_mobile.getText().toString();
                if (!"".equals(mobile)) {
                    commitCart(et_remark.getText().toString(),
                            tv_order_detail_name.getText().toString(),
                            tv_order_detail_mobile.getText().toString(),
                            coupon_id, order2_id, "0");
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
                Intent intent_invoice = new Intent(this, WriteInvoiceInformationActivity.class);
                intent_invoice.putExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_INFO, invoiceContacts);
                intent_invoice.putExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_ID, invoice_contact_id);//未开过发票的话是要传进去的
                intent_invoice.putExtra(WriteInvoiceInformationActivity.EXTRA_INVOICE_LAST_TYPE, last_invoice_type);
                startActivityForResult(intent_invoice, 0);
                break;
            case R.id.tv_store_phone://门店电话
                CustomDialog customDialog_phone = new CustomDialog(
                        this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        tv_store_phone.getText().toString()
                );
                customDialog_phone.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            if (!PermissionsChecker.checkPermission(PermissionsChecker.CALL_PHONE_PERMISSIONS)) {
                                // 跳转系统电话
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                        .parse("tel:" + tv_store_phone.getText().toString()));
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
                break;
            case R.id.tv_order_detail_money://课程费用
                Intent intent_rooms = new Intent(this, Common_Show_WebPage_Activity.class);
                // url
                intent_rooms.putExtra(UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_STORE_PRICE + "oid2=" + dataEntity.getId());
                intent_rooms.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "课室费用明细");
                startActivity(intent_rooms);
                break;
            case R.id.tv_device_fee://设备费用
                Intent intent_device = new Intent(this, Common_Show_WebPage_Activity.class);
                // url
                intent_device.putExtra(UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_DEVICE_PRICE + "oid2=" + dataEntity.getId());
                intent_device.putExtra(Common_Show_WebPage_Activity.Param_String_Title, "收费设备费用明细");
                startActivity(intent_device);
                break;
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
        }
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
