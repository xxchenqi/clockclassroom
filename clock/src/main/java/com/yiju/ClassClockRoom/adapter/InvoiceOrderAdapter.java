package com.yiju.ClassClockRoom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.MineOrderData;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.MyListView;

import java.util.List;

/**
 * --------------------------------------
 * <p/>
 * 注释:发票适配器
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-9 上午9:32:40
 * <p/>
 * --------------------------------------
 */
public class InvoiceOrderAdapter extends CommonBaseAdapter<MineOrderData> {

    private CheckChangeListener changeListener;

    public InvoiceOrderAdapter(Context context, List<MineOrderData> datas, int layoutId,
                               CheckChangeListener changeListener) {
        super(context, datas, layoutId);
        this.changeListener = changeListener;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void convert(final ViewHolder holder, final MineOrderData data) {

        holder.setText(R.id.tv_mineorder_id, "订单编号:" + data.getId()).setText(
                R.id.tv_mineorder_price, "¥" + data.getReal_fee());
        TextView tv_status = holder.getView(R.id.tv_mineorder_status);

        TextView tv_time = holder.getView(R.id.tv_order_need_time);
        TextView tv_need = holder.getView(R.id.tv_order_need);
        TextView tv_content = holder.getView(R.id.tv_order_need_content);
        CheckBox cb = holder.getView(R.id.cb_item_invoice_choose);

        String status = data.getStatus();
        if ("0".equals(status)) {

            tv_status.setText(mContext.getResources().getString(
                    R.string.status_zero));
            tv_status.setTextColor(mContext.getResources()
                    .getColor(R.color.red));

            int t = data.getTime();

            if (t > 0) {
                tv_time.setText(String.format(UIUtils.getString(R.string.minute_second), t / (60), t % 60));
            } else {
                tv_time.setText(UIUtils.getString(R.string.wee_hours));
            }

        } else if ("1".equals(status)) {//进行中
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_ing));
            tv_status.setTextColor(mContext.getResources().getColor(
                    R.color.blue));

        } else if ("2".equals(status)) {//已关闭
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_close));
            tv_status.setTextColor(mContext.getResources().getColor(
                    R.color.blue));
        }
//		else if ("3".equals(status)) {
//			tv_status.setText(mContext.getResources().getString(
//					R.string.status_three));
//			tv_status.setTextColor(mContext.getResources().getColor(
//					R.color.blue));
//		}
        else if ("4".equals(status)) {//已关闭
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_close));
            tv_status.setTextColor(mContext.getResources().getColor(
                    R.color.blue));
        }
//		else if ("5".equals(status)) {
//			tv_status.setText(mContext.getResources().getString(
//					R.string.status_five));
//			tv_status.setTextColor(mContext.getResources().getColor(
//					R.color.blue));
//		} else if ("6".equals(status)) {
//			tv_status.setText(mContext.getResources().getString(
//					R.string.status_six));
//			tv_status.setTextColor(mContext.getResources().getColor(
//					R.color.blue));
//		}
        else if ("101".equals(status)) {//已完成
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_finish));
        } else if ("100".equals(status)) {//已取消
            tv_status.setText(mContext.getResources().getString(
                    R.string.status_cancel));
        }

        if (!"0".equals(status)) {
            tv_time.setVisibility(View.GONE);
            tv_need.setVisibility(View.GONE);
            tv_content.setVisibility(View.GONE);
        } else {
            tv_time.setVisibility(View.VISIBLE);
            tv_need.setVisibility(View.VISIBLE);
            tv_content.setVisibility(View.VISIBLE);
        }

        // 内部listview处理
        MyListView lv_mineorder_inner = holder.getView(R.id.lv_mineorder_inner);
        lv_mineorder_inner.setAdapter(new MineOrderInnerAdapter(mContext, data
                .getOrder2(), R.layout.item_mineorder_inner));

        cb.setChecked(data.isChoose());

        cb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeListener.check(v, holder.getPosition());
            }
        });
    }

    public interface CheckChangeListener {
        void check(View view, int position);
    }

}
