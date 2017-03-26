package com.yiju.ClassClockRoom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.MineOrderData;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * --------------------------------------
 * <p/>
 * 注释:我的订单适配器
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq , wh
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-9 上午9:32:40
 * <p/>
 * --------------------------------------
 */
public class MineOrderAdapter extends CommonBaseAdapter<MineOrderData> {
    private OrderClickListener orderClickListener;
    private boolean cbShow;
    private boolean cbChoose = true;

    public MineOrderAdapter(Context context, List<MineOrderData> datas, int layoutId,
                            OrderClickListener orderClickListener, boolean cbShow) {
        super(context, datas, layoutId);
        this.orderClickListener = orderClickListener;
//        this.cbShow = cbShow;
    }


    public interface OrderClickListener {
        void orderClick(View view, int position);

        void cbClick(boolean isChecked, String real_fee);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void convert(final ViewHolder holder, final MineOrderData data) {
        if (data.getOrder2() == null) {
            return;
        }
        Order2 order2 = data.getOrder2().get(0);
        if (order2 == null) {
            return;
        }
        RelativeLayout rl_mine_order = holder.getView(R.id.rl_mine_order);
        Button btn_mine_order_left = holder.getView(R.id.btn_mine_order_left);
        Button btn_mine_order_right = holder.getView(R.id.btn_mine_order_right);
//        CheckBox cb_order_choose = holder.getView(R.id.cb_order_choose);
        TextView tv_status = holder.getView(R.id.tv_mineorder_status);//待支付
        TextView tv_time = holder.getView(R.id.tv_order_need_time);//0分0秒 内须提交
        ImageView iv_self_support = holder.getView(R.id.iv_self_support);//自营门店图标
        ImageView iv_item_mineorder_pic = holder.getView(R.id.iv_item_mineorder_pic);

//        if (cbShow) {
//            cb_order_choose.setVisibility(View.VISIBLE);
//        }
//        cbChoose = false;
//        cb_order_choose.setChecked(data.isCbChoose());
//        cbChoose = true;

        btn_mine_order_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.orderClick(v, holder.getPosition());
            }
        });
        btn_mine_order_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.orderClick(v, holder.getPosition());
            }
        });
        holder.setText(R.id.tv_item_mineorder_use_desc, order2.getType_desc())
                .setText(R.id.tv_item_mineorder_room_count,
                        String.format(UIUtils.getString(R.string.max_member_text),
                                order2.getMax_member()
                        ))
                .setText(R.id.tv_item_mineorder_date,
                        String.format(
                                UIUtils.getString(R.string.to_symbol),
                                order2.getStart_date(),
                                order2.getEnd_date()
                        ))
                .setText(R.id.tv_total_hour,
                        String.format(
                                UIUtils.getString(R.string.total_hour_text),
                                order2.getTotal_hour()
                        ))
                .setText(R.id.tv_mineorder_price,
                        String.format(UIUtils.getString(R.string.total_money),
                                data.getReal_fee()
                        ))
                .setText(R.id.tv_store_name, order2.getSname());

        if ("".equals(order2.getPic_url())) {
            iv_item_mineorder_pic.setImageResource(R.drawable.clock_wait);
        } else {
            Glide.with(mContext).load(order2.getPic_url()).error(R.drawable.clock_wait)
                    .into(iv_item_mineorder_pic);
        }

        if ("1".equals(order2.getSchool_type())) {
            //school_type 1代表自营
            iv_self_support.setVisibility(View.VISIBLE);
        } else {
            iv_self_support.setVisibility(View.GONE);
        }

        String status = data.getStatus();
        if ("0".equals(status) || "9".equals(status)) { //待支付
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_zero));
            int t = data.getTime();//倒计时
            /**
             * 当倒计时结束时:
             * 状态：待支付-->已关闭
             * 按钮文字：关闭订单-->删除订单
             */
            if (t >= 0) {
                tv_time.setText(
                        String.format(
                                UIUtils.getString(R.string.minute_second_need_hour),
                                t / 60 / 60,
                                t / 60 % 60,
                                t % 60
                        ));
                rl_mine_order.setVisibility(View.VISIBLE);
                btn_mine_order_left.setVisibility(View.VISIBLE);
                btn_mine_order_right.setVisibility(View.VISIBLE);
                btn_mine_order_right.setText(UIUtils.getString(R.string.order_immediate_pay));
                btn_mine_order_left.setText(UIUtils.getString(R.string.order_close));
                btn_mine_order_right.setBackgroundResource(R.drawable.background_green_1eb482_stroke);
                btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            } else {//隐藏
                tv_time.setVisibility(View.GONE);
                tv_status.setText(mContext.getResources().getString(
                        R.string.status_close));
                rl_mine_order.setVisibility(View.VISIBLE);
                btn_mine_order_right.setVisibility(View.VISIBLE);
                btn_mine_order_left.setVisibility(View.GONE);
                btn_mine_order_right.setText(UIUtils.getString(R.string.order_delete));
                btn_mine_order_right.setBackgroundResource(R.drawable.background_gray_stroke);
                btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.order_black));
            }
        } else if ("1".equals(status)) {//进行中
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_ing));
            rl_mine_order.setVisibility(View.VISIBLE);
            btn_mine_order_right.setVisibility(View.VISIBLE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setText(UIUtils.getString(R.string.order_classroom_arrangement));
            btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            btn_mine_order_right.setBackgroundResource(R.drawable.background_green_1eb482_stroke);
        } else if ("2".equals(status) || "4".equals(status) || "8".equals(status)) { //已关闭
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_close));
            rl_mine_order.setVisibility(View.VISIBLE);
            btn_mine_order_right.setVisibility(View.VISIBLE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setText(UIUtils.getString(R.string.order_delete));
            btn_mine_order_right.setBackgroundResource(R.drawable.background_gray_stroke);
            btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.order_black));
        } else if ("6".equals(status)) {// 订单超时
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_overtime));
            rl_mine_order.setVisibility(View.GONE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.GONE);
        } else if ("101".equals(status)) {//已完成
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_finish));
            rl_mine_order.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.GONE);
            btn_mine_order_left.setVisibility(View.GONE);
        } else if ("100".equals(status) || "11".equals(status) || "12".equals(status)) {// 已取消
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_cancel));
            rl_mine_order.setVisibility(View.GONE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.GONE);
        } else if ("7".equals(status)) {//支付前待确认
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_affirm));
            rl_mine_order.setVisibility(View.VISIBLE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.VISIBLE);
            btn_mine_order_right.setText(UIUtils.getString(R.string.order_close));
            btn_mine_order_right.setBackgroundResource(R.drawable.background_gray_stroke);
            btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.order_black));
        } else if ("10".equals(status)) {//支付后待确认
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_affirm));
            rl_mine_order.setVisibility(View.GONE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.GONE);
        }

        if (!"0".equals(status) && !"9".equals(status)) {
            if ("2".equals(status) || "4".equals(status) || "8".equals(status)) {
                if (data.isStatusFlag()) {
                    tv_time.setVisibility(View.VISIBLE);
                    tv_time.setText(R.string.txt_have_expired);
                } else {
                    tv_time.setVisibility(View.GONE);
                }
            } else {
                tv_time.setVisibility(View.GONE);
            }
        } else {
            int t = data.getTime();
            if (t >= 0) {
                tv_time.setVisibility(View.VISIBLE);
            } else {
                tv_time.setVisibility(View.GONE);
            }
        }

//        cb_order_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (cbChoose) {
//                    data.setCbChoose(isChecked);
//                    orderClickListener.cbClick(isChecked, data.getReal_fee());
//                }
//            }
//        });

    }
}
