package com.yiju.ClassClockRoom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.OrderCourseData;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * 课程订单_适配器
 * Created by wh on 2016/6/22.
 */
public class OrderCourseAdapter extends CommonBaseAdapter<OrderCourseData> {
    private OrderClickListener orderClickListener;
    private boolean cbShow;
    private boolean cbChoose = true;

    public OrderCourseAdapter(Context context, List<OrderCourseData> datas, int layoutId,
                              OrderClickListener orderClickListener, boolean cbShow) {
        super(context, datas, layoutId);
        this.orderClickListener = orderClickListener;
        this.cbShow = cbShow;
    }

    public interface OrderClickListener {
        void orderClick(View view, int position);

        void cbClick(boolean isChecked, String real_fee);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void convert(final ViewHolder holder, final OrderCourseData data) {
        RelativeLayout rl_mine_order = holder.getView(R.id.rl_mine_order);
        TextView tv_item_course_id = holder.getView(R.id.tv_item_course_id);
        TextView tv_course_status = holder.getView(R.id.tv_course_status);
        TextView tv_order_need_time = holder.getView(R.id.tv_order_need_time);
        ImageView iv_item_course_pic = holder.getView(R.id.iv_item_course_pic);
        TextView tv_item_course_name = holder.getView(R.id.tv_item_course_name);
        TextView tv_item_course_address = holder.getView(R.id.tv_item_course_address);
        TextView tv_item_course_date = holder.getView(R.id.tv_item_course_date);
        TextView tv_total_hour = holder.getView(R.id.tv_total_hour);
        TextView tv_course_price = holder.getView(R.id.tv_course_price);
        Button btn_mine_order_left = holder.getView(R.id.btn_mine_order_left);
        Button btn_mine_order_right = holder.getView(R.id.btn_mine_order_right);
        CheckBox cb_order_choose = holder.getView(R.id.cb_order_choose);

        tv_item_course_id.setText(
                String.format(
                        UIUtils.getString(R.string.order_id),
                        data.getOrder_course_id() + ""
                )
        );
        Glide.with(mContext).load(data.getPic()).into(iv_item_course_pic);
        tv_item_course_name.setText(data.getCourse_name());
        tv_item_course_address.setText(data.getSchool_name());
        tv_item_course_date.setText(
                String.format(
                        UIUtils.getString(R.string.to_symbol),
                        data.getStart_date(),
                        data.getEnd_date()
                )
        );
        tv_total_hour.setText(
                String.format(
                        UIUtils.getString(R.string.course_how_much_total),
                        data.getCourse_num() + ""
                )
        );
        tv_course_price.setText(
                String.format(
                        UIUtils.getString(R.string.total_money),
                        data.getReal_fee()
                )
        );

        String status = data.getStatus();
        if ("0".equals(status)) { //待支付
            tv_course_status.setText(mContext.getResources().getString(
                    R.string.status_zero));
            int t = data.getTime();//倒计时
            /**
             * 当倒计时结束时:
             * 状态：待支付-->已关闭
             * 按钮文字：关闭订单-->删除订单
             */
            if (t >= 0) {
                tv_order_need_time.setText(
                        String.format(
                                UIUtils.getString(R.string.minute_second_need),
                                t / (60),
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
                tv_order_need_time.setVisibility(View.GONE);
                tv_course_status.setText(mContext.getResources().getString(
                        R.string.status_close));
                rl_mine_order.setVisibility(View.VISIBLE);
                btn_mine_order_right.setVisibility(View.VISIBLE);
                btn_mine_order_left.setVisibility(View.GONE);
                btn_mine_order_right.setText(UIUtils.getString(R.string.order_delete));
                btn_mine_order_right.setBackgroundResource(R.drawable.background_gray_stroke);
                btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.order_black));
            }
        } else if ("1".equals(status)) {//进行中
            tv_course_status.setText(mContext.getResources().getString(
                    R.string.status_ing));
            rl_mine_order.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.GONE);
            btn_mine_order_left.setVisibility(View.GONE);
        } else if ("2".equals(status) || "4".equals(status)) { //已关闭
            tv_course_status.setText(mContext.getResources().getString(
                    R.string.status_close));
            rl_mine_order.setVisibility(View.VISIBLE);
            btn_mine_order_right.setVisibility(View.VISIBLE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setText(UIUtils.getString(R.string.order_delete));
            btn_mine_order_right.setBackgroundResource(R.drawable.background_gray_stroke);
            btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.order_black));
        } else if ("6".equals(status)) {// 订单超时
            tv_course_status.setText(mContext.getResources().getString(
                    R.string.status_overtime));
            rl_mine_order.setVisibility(View.GONE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.GONE);
        } else if ("101".equals(status)) {//已完成
            tv_course_status.setText(mContext.getResources().getString(
                    R.string.status_finish));
            rl_mine_order.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.GONE);
            btn_mine_order_left.setVisibility(View.GONE);
        } else if ("100".equals(status) || "110".equals(status)) {// 已取消
            tv_course_status.setText(mContext.getResources().getString(
                    R.string.status_cancel));
            rl_mine_order.setVisibility(View.GONE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setVisibility(View.GONE);
        }

        if (!"0".equals(status)) {
            tv_order_need_time.setVisibility(View.GONE);
        } else {
            int t = data.getTime();
            if (t >= 0) {
                tv_order_need_time.setVisibility(View.VISIBLE);
            } else {
                tv_order_need_time.setVisibility(View.GONE);
            }

        }
        if (cbShow) {
            cb_order_choose.setVisibility(View.GONE);
        }
        cbChoose = false;
        cb_order_choose.setChecked(data.isCbChoose());
        cbChoose = true;
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
        cb_order_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbChoose) {
                    data.setCbChoose(isChecked);
                    orderClickListener.cbClick(isChecked, data.getReal_fee());
                }
            }
        });

    }


}
