package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
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

    public OrderCourseAdapter(Context context, List<OrderCourseData> datas, int layoutId,
                              OrderClickListener orderClickListener) {
        super(context, datas, layoutId);
        this.orderClickListener = orderClickListener;
    }

    public interface OrderClickListener {
        void orderClick(View view, int position);
    }

    @Override
    public void convert(final ViewHolder holder, final OrderCourseData data) {
        //课程状态
        TextView tv_course_status = holder.getView(R.id.tv_course_status);
        //门店名
        TextView tv_store_name = holder.getView(R.id.tv_store_name);
        //按钮左
        Button btn_mine_order_left = holder.getView(R.id.btn_mine_order_left);
        //按钮右
        Button btn_mine_order_right = holder.getView(R.id.btn_mine_order_right);
        //按钮布局
        RelativeLayout rl_mine_order = holder.getView(R.id.rl_mine_order);
        //倒计时时间
        TextView tv_order_need_time = holder.getView(R.id.tv_order_need_time);
        //课程图片
        ImageView iv_item_course_pic = holder.getView(R.id.iv_item_course_pic);
        //旗舰店标志
        ImageView iv_self_support = holder.getView(R.id.iv_self_support);
        //课程名
        TextView tv_item_course_name = holder.getView(R.id.tv_item_course_name);
        //课程价格
        TextView tv_course_price = holder.getView(R.id.tv_course_price);
        //课程时间
        TextView tv_item_course_date = holder.getView(R.id.tv_item_course_date);


        if ("1".equals(data.getSchool_type())) {
            //school_type 1代表自营
            iv_self_support.setVisibility(View.VISIBLE);
        } else {
            iv_self_support.setVisibility(View.GONE);
        }
        //课程图片
        Glide.with(mContext).load(data.getPic()).into(iv_item_course_pic);
        //课程名
        tv_item_course_name.setText(data.getCourse_name());
        //课程时间
        tv_item_course_date.setText(data.getCourse_str());
        //课程价格
        tv_course_price.setText(String.format(UIUtils.getString(R.string.total_money), data.getReal_fee()));
        //门店名
        tv_store_name.setText(data.getSname());
        /**
         * 订单状态    状态码返回码
         * 待支付      0
         * 报名成功    1
         * 已取消     100
         * 已关闭     102
         */
        String status = data.getStatus();
        if ("0".equals(status)) { //待支付
            tv_course_status.setText(UIUtils.getString(R.string.status_zero));
            int t = data.getTime();//倒计时
            /**
             * 当倒计时结束时:
             * 状态：待支付-->已关闭
             * 按钮文字：关闭订单-->删除订单
             */
            if (t >= 0) {
                tv_order_need_time.setText(String.format(
                        UIUtils.getString(R.string.minute_second_need), t / (60), t % 60));
                rl_mine_order.setVisibility(View.VISIBLE);
                btn_mine_order_left.setVisibility(View.VISIBLE);
                btn_mine_order_right.setVisibility(View.VISIBLE);
                btn_mine_order_right.setText(UIUtils.getString(R.string.order_immediate_pay));
                btn_mine_order_left.setText(UIUtils.getString(R.string.order_close));
                btn_mine_order_right.setBackgroundResource(R.drawable.background_green_1eb482_stroke);
                btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            } else {//隐藏
                tv_order_need_time.setVisibility(View.GONE);
                tv_course_status.setText(UIUtils.getString(R.string.status_close));
                rl_mine_order.setVisibility(View.VISIBLE);
                btn_mine_order_right.setVisibility(View.VISIBLE);
                btn_mine_order_left.setVisibility(View.GONE);
                btn_mine_order_right.setText(UIUtils.getString(R.string.order_delete));
                btn_mine_order_right.setBackgroundResource(R.drawable.background_gray_stroke);
                btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.order_black));
            }
        } else if ("1".equals(status)) {//报名成功
            tv_course_status.setText(UIUtils.getString(R.string.sign_up_success));
            rl_mine_order.setVisibility(View.GONE);
        } else if ("100".equals(status)) {// 已取消
            tv_course_status.setText(mContext.getResources().getString(
                    R.string.status_cancel));
            rl_mine_order.setVisibility(View.GONE);
        } else if ("102".equals(status)) { //已关闭
            tv_course_status.setText(UIUtils.getString(R.string.status_close));
            rl_mine_order.setVisibility(View.VISIBLE);
            btn_mine_order_right.setVisibility(View.VISIBLE);
            btn_mine_order_left.setVisibility(View.GONE);
            btn_mine_order_right.setText(UIUtils.getString(R.string.order_delete));
            btn_mine_order_right.setBackgroundResource(R.drawable.background_gray_stroke);
            btn_mine_order_right.setTextColor(UIUtils.getColor(R.color.order_black));
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

    }
}
