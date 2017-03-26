package com.yiju.ClassClockRoom.adapter.holder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.Coupon.CouponDataEntity;
import com.yiju.ClassClockRoom.util.UIUtils;

public class CouponHolder extends BaseHolder<CouponDataEntity> implements OnClickListener {

    //整体布局，选中时改变背景
    private RelativeLayout rl_item_coupon;
    //满送券	类型为(type:2)显示
    private TextView tv_item_coupon_mansong;
    //满减券	类型为(type:1)显示
    private LinearLayout ll_item_coupon_discount;
    //满减券状态下，减掉的金额(格式：30)
    private TextView tv_item_coupon_price;
    //满减券状态下，满足的金额才能使用(格式：满60可用)
    private TextView tv_item_coupon_full;
    //优惠券的名字
    private TextView tv_item_coupon_name;
    //优惠券的描述（当超过18个字符，后面的"详情"显示）
    private TextView tv_item_coupon_desc;
    //当优惠券的描述大于18个字符时 显示该条目
    private FrameLayout fl_item_desc;
    //优惠券描述为空时隐藏
    private RelativeLayout rl_item_coupon_desc;
    //优惠券的开始-结束日期
    private TextView tv_item_coupon_date;
    //优惠券的剩余使用时间
    private TextView tv_item_coupon_lasttime;
    private String desc;
    private View view;
    private Context mContext;

    public CouponHolder(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View initView(Context context) {
        view = View.inflate(context, R.layout.item_coupon, null);
        rl_item_coupon = (RelativeLayout) view.findViewById(R.id.rl_item_coupon);
        tv_item_coupon_mansong = (TextView) view.findViewById(R.id.tv_item_coupon_mansong);
        ll_item_coupon_discount = (LinearLayout) view.findViewById(R.id.ll_item_coupon_discount);
        tv_item_coupon_price = (TextView) view.findViewById(R.id.tv_item_coupon_price);
        tv_item_coupon_full = (TextView) view.findViewById(R.id.tv_item_coupon_full);
        tv_item_coupon_name = (TextView) view.findViewById(R.id.tv_item_coupon_name);
        tv_item_coupon_desc = (TextView) view.findViewById(R.id.tv_item_coupon_desc);
        fl_item_desc = (FrameLayout) view.findViewById(R.id.fl_item_desc);
        rl_item_coupon_desc = (RelativeLayout) view.findViewById(R.id.rl_item_coupon_desc);
        tv_item_coupon_date = (TextView) view.findViewById(R.id.tv_item_coupon_date);
        tv_item_coupon_lasttime = (TextView) view.findViewById(R.id.tv_item_coupon_lasttime);
        fl_item_desc.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshView() {
        // 加载数据，刷新布局
        CouponDataEntity info = getData();
        String type = info.getType();
        if (type.equals("1")) {//满送券
            ll_item_coupon_discount.setVisibility(View.VISIBLE);
            tv_item_coupon_mansong.setVisibility(View.INVISIBLE);
            tv_item_coupon_price.setText(info.getDiscount().split("\\.")[0]);
            tv_item_coupon_full.setText(String.format(UIUtils.getString(R.string.satisfy_how_much_usable), info.getFull().split("\\.")[0]));
        } else if (type.equals("2")) {//满减券
            tv_item_coupon_mansong.setVisibility(View.VISIBLE);
            ll_item_coupon_discount.setVisibility(View.INVISIBLE);
        }
        tv_item_coupon_name.setText(info.getBatch_name());
        desc = info.getDesc();
        if (null == desc) {
            rl_item_coupon_desc.setVisibility(View.GONE);
        } else if (desc.length() > 18) {
            rl_item_coupon_desc.setVisibility(View.VISIBLE);
            fl_item_desc.setVisibility(View.VISIBLE);
            tv_item_coupon_desc.setText(String.format(UIUtils.getString(R.string.apostrophe), desc.substring(0, 8)));
        } else {
            rl_item_coupon_desc.setVisibility(View.VISIBLE);
            fl_item_desc.setVisibility(View.GONE);
            tv_item_coupon_desc.setText(desc);
        }
        String start_date = info.getStart_date();
        if (start_date == null) {
            tv_item_coupon_date.setVisibility(View.GONE);
            tv_item_coupon_lasttime.setVisibility(View.GONE);
        } else {
            tv_item_coupon_date.setVisibility(View.VISIBLE);
            tv_item_coupon_date.setText(String.format(UIUtils.getString(R.string.to), start_date, info.getEnd_date()));
        }
        String abs_date = info.getAbs_date();
        if (abs_date == null) {
            tv_item_coupon_lasttime.setVisibility(View.GONE);
        } else {
            tv_item_coupon_lasttime.setVisibility(View.VISIBLE);
            tv_item_coupon_lasttime.setText(String.format(UIUtils.getString(R.string.overdue),abs_date));
        }
        if (info.isCheck()) {
            rl_item_coupon.setBackgroundResource(R.drawable.order_coupon_orange_border);
            info.setCheck(false);
        } else {
            rl_item_coupon.setBackgroundResource(R.drawable.order_coupon_grey_border);
        }
    }

    @Override
    public void onClick(View v) {
        // 优惠券详情点击事件
        switch (v.getId()) {
            case R.id.fl_item_desc:
                View vv = View.inflate(mContext, R.layout.popupwindow_coupon_desc, null);
                final PopupWindow pop = new PopupWindow(vv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                pop.setOutsideTouchable(true);
                ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.color_lucency));
                pop.setBackgroundDrawable(dw);
                pop.setTouchInterceptor(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // 点击消失
                        pop.dismiss();
                        return false;
                    }
                });
                TextView tv_desc = (TextView) vv.findViewById(R.id.tv_desc);
                tv_desc.setText(desc);
                pop.showAtLocation(view, 0, 0, 0);
                break;

            default:
                break;
        }
    }

}
