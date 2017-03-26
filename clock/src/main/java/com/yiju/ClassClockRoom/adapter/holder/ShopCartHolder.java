package com.yiju.ClassClockRoom.adapter.holder;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.CouponUse;
import com.yiju.ClassClockRoom.bean.DeviceEntity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.CustomDigitalClock;
import com.yiju.ClassClockRoom.widget.CustomDigitalClock.ClockListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopCartHolder extends BaseHolder<Order2> implements
        OnClickListener {

    private Context mContext;
    // 订单是否选中的状态
    private ImageView iv_item_cart_choose;
    // 订单的名字
    private TextView tv_item_cart_name;
    // 订单的图片
    private ImageView iv_item_cart_pic;
    // 订单的用途
    private TextView tv_item_cart_type;
    // 订单的日期
    private TextView tv_item_cart_date;
    // 订单的时间
    private TextView tv_item_cart_time;
    // 订单的星期
    private TextView tv_item_cart_week;
    // 订单的数量
    private TextView tv_item_cart_count;
    // 订单的收费设备(格式：收费设备:)
    private TextView tv_item_cart_contect_price;
    // 订单的免费设备(格式：免费设备:)
//    private TextView tv_item_cart_contect;
    // 订单的剩余时间(剩余5分钟的时候显示，否则隐藏)
    private RelativeLayout rl_item_cart_gone;
    // 剩余时间(格式:4分32秒)
    private CustomDigitalClock tv_item_last_time;
    // private TextView tv_item_last_time;
    // 订单的价格(格式:￥3245.34)
    private TextView tv_item_cart_price;
    // 订单状态(更换订单状态图片失效、订完)
    private ImageView iv_sale_no;
    // 定义一个变量来记录订单是否被选中
    private boolean isCheck = false;
    private ImageView mIv;
    private TextView mTv;
    private Order2 info;
    private List<Order2> mLists = new ArrayList<>();
    private float price;
    private BaseApplication bb;
    private TextView mPrice;
    private TextView mDelete;
    private String delete;
    private List<Order2> yesOrders;
    private TextView mClassPrice;
    private TextView mShould_price;

    private RelativeLayout rl_price_count;

    public ShopCartHolder(Context context, ImageView iv,
                          List<Order2> lists, TextView tv,
                          TextView price, TextView delete, TextView class_price, TextView should_price) {
        super(context);
        this.mContext = context;
        this.mIv = iv;
        this.mLists = lists;
        this.mTv = tv;
        this.mPrice = price;
        this.mDelete = delete;
        this.mClassPrice = class_price;
        this.mShould_price = should_price;
    }

    @Override
    public View initView(Context context) {
        // 加载布局
        View view = View.inflate(context, R.layout.item_cart, null);
        iv_item_cart_choose = (ImageView) view
                .findViewById(R.id.iv_item_cart_choose);
        tv_item_cart_name = (TextView) view
                .findViewById(R.id.tv_item_cart_name);
        iv_item_cart_pic = (ImageView) view.findViewById(R.id.iv_item_cart_pic);
        tv_item_cart_type = (TextView) view
                .findViewById(R.id.tv_item_cart_type);
        tv_item_cart_date = (TextView) view
                .findViewById(R.id.tv_item_cart_date);
        tv_item_cart_time = (TextView) view
                .findViewById(R.id.tv_item_cart_time);
        tv_item_cart_week = (TextView) view
                .findViewById(R.id.tv_item_cart_week);
        tv_item_cart_count = (TextView) view
                .findViewById(R.id.tv_item_cart_count);
        tv_item_cart_contect_price = (TextView) view
                .findViewById(R.id.tv_item_cart_contect_price);
//        tv_item_cart_contect = (TextView) view
//                .findViewById(R.id.tv_item_cart_contect);
        rl_item_cart_gone = (RelativeLayout) view
                .findViewById(R.id.rl_item_cart_gone);
        tv_item_last_time = (CustomDigitalClock) view
                .findViewById(R.id.tv_item_last_time);
        tv_item_cart_price = (TextView) view
                .findViewById(R.id.tv_item_cart_price);
        iv_sale_no = (ImageView) view.findViewById(R.id.iv_sale_no);
        rl_price_count = (RelativeLayout) view
                .findViewById(R.id.rl_price_count);

        iv_item_cart_choose.setOnClickListener(this);
        rl_price_count.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshView() {
        info = getData();
        delete = mDelete.getText().toString();
        yesOrders = new ArrayList<>();
        for (int i = 0; i < mLists.size(); i++) {
            if (mLists.get(i).getIs_valid().equals("1")) {
                yesOrders.add(mLists.get(i));
            }
        }
        if (null != info) {
            String is_valid = info.getIs_valid();
            switch (Integer.valueOf(is_valid)) {
                case 0:
                    //失效
                    iv_item_cart_choose.setEnabled(false);
                    iv_sale_no.setVisibility(View.VISIBLE);
                    iv_sale_no.setImageResource(R.drawable.lapse);
                    break;
                case 1:
                    //有效
                    iv_item_cart_choose.setEnabled(true);
                    iv_sale_no.setVisibility(View.GONE);
                    break;
                case 2:
                    //订完
                    iv_item_cart_choose.setEnabled(true);
                    iv_sale_no.setVisibility(View.VISIBLE);
                    iv_sale_no.setImageResource(R.drawable.booked_off);
                    iv_item_cart_choose.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
            long l = Long.valueOf(info.getExpire_time() + "000");
            if (0 < (l - System.currentTimeMillis())
                    && (l - System.currentTimeMillis()) < 5 * 60 * 1000) {
                rl_item_cart_gone.setVisibility(View.VISIBLE);
            } else {
                rl_item_cart_gone.setVisibility(View.INVISIBLE);
            }

            tv_item_last_time.setEndTime(l);
            tv_item_last_time.setClockListener(new ClockListener() {

                @Override
                public void timeEnd() {
                    // 订单时间到
                    UIUtils.showToastSafe("订单保存时间已到");
                    rl_item_cart_gone.setVisibility(View.INVISIBLE);
                }

                @Override
                public void remainFiveMinutes() {
                    // 订单还剩5分钟
                    UIUtils.showToastSafe("订单将为您保留5分钟，请及时提交");
                }
            });
            tv_item_cart_name.setText(info.getSname());
            Glide.with(UIUtils.getContext()).load(info.getPic_url())
                    .into(iv_item_cart_pic);
            if ("".equals(info.getUse_desc())) {
                tv_item_cart_type.setText(info.getType_desc());
            } else {
                tv_item_cart_type.setText(String.format(
                        UIUtils.getString(R.string.desc_and_type_content),
                        info.getType_desc(), info.getUse_desc()));
            }
            tv_item_cart_count.setText(String.format(UIUtils.getString(R.string.multiply),
                    info.getRoom_count()));

            tv_item_cart_date.setText(String.format(UIUtils.getString(R.string.to_symbol),
                    info.getStart_date(), info.getEnd_date()));

            String startTime = changeTime(info.getStart_time());
            String endTime = changeTime(info.getEnd_time());
            tv_item_cart_time.setText(String.format(
                    UIUtils.getString(R.string.to_symbol), startTime, endTime));
            String repeat = info.getRepeat();
            String week;
            if (repeat.equals("")) {
                week = "每天";
            } else {
                String[] weeks = repeat.split(",");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < weeks.length; i++) {
                    if (i == weeks.length - 1) {
                        sb.append(changWeek(Integer.valueOf(weeks[i])));
                    } else {
                        sb.append(changWeek(Integer.valueOf(weeks[i]))).append("、");
                    }
                }
                week = sb.toString();
            }
            tv_item_cart_week.setText(week);
//            tv_item_cart_count.setText(String.format(UIUtils.getString(R.string.multiply),
//                    info.getRoom_count()));
            List<DeviceEntity> device_noFree = info.getDevice_nofree();
            String deviceNoFree = changeDevice(device_noFree);
            if (deviceNoFree.equals("")) {
                deviceNoFree = "无";
            }
            tv_item_cart_contect_price.setText(
                    String.format(UIUtils.getString(R.string.charging_equipment), deviceNoFree));

//            List<DeviceEntity> device_free = info.getDevice_free();
//            String devicesFree = changeDevice(device_free);
//            if (devicesFree.equals("")) {
//                devicesFree = "无";
//            }
//            tv_item_cart_contect.setText(String.format(UIUtils.getString(R.string.free_equipment)
//                    , devicesFree));
            tv_item_cart_price.setText(String.format(UIUtils.getString(R.string.rmb_how_much),
                    info.getFee()));
            price = 0;
            bb = (BaseApplication) mContext.getApplicationContext();
            for (int i = 0; i < mLists.size(); i++) {
                price += Float.valueOf(mLists.get(i).getFee());
            }
            boolean check = info.getCheck();
            if (check) {
                if (!info.getIs_valid().equals("1")) {
                    if (delete.equals(UIUtils.getString(R.string.label_finish))) {
                        iv_item_cart_choose.setEnabled(true);
                        iv_item_cart_choose.setBackgroundResource(R.drawable.check_icon);
                    } else {
                        iv_item_cart_choose.setEnabled(false);
                        iv_item_cart_choose.setVisibility(View.GONE);
                        info.setCheck(false);
                    }
                } else {
                    iv_item_cart_choose.setEnabled(true);
                    iv_item_cart_choose.setBackgroundResource(R.drawable.check_icon);
                }
                isCheck = true;
                bb.setCheck(true);
            } else {
                if (!info.getIs_valid().equals("1")) {
                    if (delete.equals(UIUtils.getString(R.string.label_finish))) {
                        iv_item_cart_choose.setEnabled(true);
                    } else {
                        iv_item_cart_choose.setEnabled(false);
                        info.setCheck(false);
                    }
                } else {
                    iv_item_cart_choose.setEnabled(true);
                }
                isCheck = false;
                bb.setCheck(false);
            }

        }
    }

    private String changeDevice(List<DeviceEntity> device) {
        StringBuilder sb = new StringBuilder();
        if (device != null && device.size() > 0) {
            for (int i = 0; i < device.size(); i++) {
                if (i == device.size() - 1) {
//                    sb.append(device.get(i).getDevice_name()).append(device.get(i).getNum()).append(device.get(i).getUnit());
                    sb.append(device.get(i).getDevice_name()).append("X").append(device.get(i).getNum());
                } else {
//                    sb.append(device.get(i).getDevice_name()).append(device.get(i).getNum()).append(device.get(i).getUnit()).append(";");
                    sb.append(device.get(i).getDevice_name()).append("X").append(device.get(i).getNum()).append(";");
                }
            }
        }
        return sb.toString();
    }

    private String changWeek(int repeat) {
        String week = null;
        switch (repeat) {
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
        return week;
    }

    private String changeTime(String start_time) {
        String h;
        String m;
        if (start_time.length() < 4) {
            h = start_time.substring(0, 1);
            m = start_time.substring(1);
        } else {
            h = start_time.substring(0, 2);
            m = start_time.substring(2);
        }
        return h + ":" + m;
    }

    @Override
    public void onClick(View v) {
        // 处理点击事件
        switch (v.getId()) {
            case R.id.iv_item_cart_choose:
                if (delete.equals(UIUtils.getString(R.string.label_finish))) {
                    //删除状态
                    if (isCheck) {
                        isCheck = false;
                        info.setCheck(false);
                        int count = bb.getCount();
                        count--;
                        bb.setCount(count);
                        if (count == 0) {
                            mPrice.setText(UIUtils.getString(R.string.rmb_equals_negative_zero));
                        }
                        bb.setCheck(false);
                        float pp = bb.getPrice() - Float.valueOf(info.getFee());
                        bb.setPrice(pp);
                        Map<String, Integer> order = bb.getmOrder();
                        order.put(
                                String.valueOf(info.getId()) + "a"
                                        + String.valueOf(info.getSid()), 0);
                        if (!mPrice.getText().toString().equals(
                                UIUtils.getString(R.string.rmb_equals_negative_zero))) {
                            useCoupon(order);
                        } else {
                            mShould_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), pp));
                            mTv.setText(String.format(UIUtils.getString(R.string.rmb_float_zero), pp));
                            mClassPrice.setText(String.format(
                                    UIUtils.getString(R.string.rmb_float_zero), pp));
                        }

                    } else {
                        isCheck = true;
                        info.setCheck(true);
                        iv_item_cart_choose
                                .setBackgroundResource(R.drawable.check_icon);
                        int count = bb.getCount();
                        count++;
                        bb.setCount(count);
                        float pp = bb.getPrice() + Float.valueOf(info.getFee());
                        bb.setPrice(pp);
                        if (count == mLists.size()) {
                            mIv.setBackgroundResource(R.drawable.check_icon);
                            bb.setCheck(true);
                        } else {
                            bb.setCheck(false);
                        }
                        Map<String, Integer> order = bb.getmOrder();
                        order.put(
                                String.valueOf(info.getId()) + "a"
                                        + String.valueOf(info.getSid()), 1);
                        if (!mPrice.getText().toString().equals("-￥0.00")) {
                            String cP = mPrice.getText().toString().substring(2);
                            float newPrice = pp - Float.valueOf(cP);
                            mShould_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , newPrice));
                            mTv.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , newPrice));
                        } else {
                            mShould_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , pp));
                            mTv.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , pp));
                            mClassPrice.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , pp));

                        }
                    }
                } else {
                    //提交状态
                    if (isCheck) {
                        isCheck = false;
                        info.setCheck(false);
                        int count = bb.getCount();
                        count--;
                        bb.setCount(count);
                        if (count == 0) {
                            mPrice.setText(UIUtils.getString(R.string.rmb_equals_negative_zero));
                        }
                        bb.setCheck(false);
                        float pp = bb.getPrice() - Float.valueOf(info.getFee());
                        bb.setPrice(pp);
                        Map<String, Integer> order = bb.getmOrder();
                        order.put(
                                String.valueOf(info.getId()) + "a"
                                        + String.valueOf(info.getSid()), 0);
                        if (!mPrice.getText().toString().equals(
                                UIUtils.getString(R.string.rmb_equals_negative_zero))) {
                            useCoupon(order);
                        } else {
                            mShould_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , pp));
                            mTv.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , pp));
                            mClassPrice.setText(
                                    String.format(UIUtils.getString(R.string.rmb_float_zero)
                                            , pp));
                        }

                    } else {
                        isCheck = true;
                        info.setCheck(true);
                        iv_item_cart_choose
                                .setBackgroundResource(R.drawable.check_icon);
                        int count = bb.getCount();
                        count++;
                        bb.setCount(count);
                        float pp = bb.getPrice() + Float.valueOf(info.getFee());
                        bb.setPrice(pp);
                        if (count == yesOrders.size()) {
                            mIv.setBackgroundResource(R.drawable.check_icon);
//						bb.setCheck(true);
//					}else{
//						bb.setCheck(false);
                        }
                        Map<String, Integer> order = bb.getmOrder();
                        order.put(
                                String.valueOf(info.getId()) + "a"
                                        + String.valueOf(info.getSid()), 1);
                        if (!mPrice.getText().toString().equals("-￥0.00")) {
                            String cP = mPrice.getText().toString().substring(2);
                            float newPrice = pp - Float.valueOf(cP);
                            mShould_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , newPrice));
                            mTv.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , newPrice));
                        } else {
                            mShould_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , pp));
                            mTv.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                    , pp));
                        }
                        mClassPrice.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                                , pp));
                    }
                }


                break;
//            case R.id.rl_price_count:
//                if (info == null) {
//                    return;
//                }
//                Intent intent = new Intent(mContext,
//                        Common_Show_WebPage_Activity.class);
//                intent.putExtra(UIUtils.getString(R.string.redirect_open_url),
//                        UrlUtils.SERVER_WEB_LISTFREE + "oid2=" + info.getId());
//                intent.putExtra(UIUtils.getString(R.string.get_page_name),
//                        WebConstant.Expense_list_Page);
//                UIUtils.startActivity(intent);
//                break;
            default:
                break;
        }

    }

    private void useCoupon(Map<String, Integer> order) {
        // 判断优惠券是否可用
        String uid = SharedPreferencesUtils.getString(mContext, "id", null);
        String coupon_id = bb.getCouponID();
        List<String> idList = new ArrayList<>();
        StringBuilder o_sb = new StringBuilder();
        for (int j = 0; j < mLists.size(); j++) {
            if (mLists.get(j).getCheck()) {
                idList.add(mLists.get(j).getId());
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
        String order2_id = o_sb.toString();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "coupon_available");
        params.addBodyParameter("order2_id", order2_id);
        params.addBodyParameter("coupon_id", coupon_id);
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_COUPON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
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
        CouponUse couponInfo = GsonTools.changeGsonToBean(result,
                CouponUse.class);
        if (null != couponInfo) {
            if (couponInfo.getCode() == 1 && couponInfo.getMsg().equals("ok")) {
                if (couponInfo.getData() == 1) {
                    // 优惠券可用
                    String cP = mPrice.getText().toString().substring(2);
                    float newPrice = bb.getPrice() - Float.valueOf(cP);
                    mShould_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                            , newPrice));
                    mTv.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                            , newPrice));
                } else if (couponInfo.getData() == 0) {
                    // 优惠券不可用
                    mPrice.setText(UIUtils.getString(R.string.rmb_equals_negative_zero));
                    mShould_price.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                            , bb.getPrice()));
                    mTv.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                            , bb.getPrice()));
                }
                mClassPrice.setText(String.format(UIUtils.getString(R.string.rmb_float_zero)
                        , bb.getPrice()));

            }
        }
    }


}
