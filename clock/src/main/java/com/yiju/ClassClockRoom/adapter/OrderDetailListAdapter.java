package com.yiju.ClassClockRoom.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.ClassroomArrangementActivity;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.bean.CommonMsgResult;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.List;

/**
 * 订单详情列表页适配器
 * Created by wh on 2016/5/13.
 */
public class OrderDetailListAdapter extends BaseAdapter {

    private List<Order2> order2s;

    public OrderDetailListAdapter(List<Order2> order2s) {
        this.order2s = order2s;
    }

    public void setOrder2s(List<Order2> order2s) {
        this.order2s = order2s;
    }

    @Override
    public int getCount() {
        return order2s.size();
    }

    @Override
    public Object getItem(int position) {
        return order2s.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(UIUtils.getContext(), R.layout.item_order_detail_list, null);
            viewHolder.tv_item_detail_list_sname = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_list_sname);
            viewHolder.tv_item_detail_list_use_desc = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_list_use_desc);
            viewHolder.tv_item_detail_list_room_count = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_list_room_count);
            viewHolder.tv_item_detail_list_date = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_list_date);
            viewHolder.tv_item_detail_list_repeat = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_list_repeat);
            viewHolder.tv_item_detail_list_time = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_list_time);
            viewHolder.bt_detail_list_classroom_arrangement = (Button)
                    convertView.findViewById(R.id.bt_detail_list_classroom_arrangement);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Order2 order2 = order2s.get(position);

        viewHolder.tv_item_detail_list_sname.setText(order2.getSname());
        viewHolder.tv_item_detail_list_use_desc.setText(order2.getType_desc());
        viewHolder.tv_item_detail_list_room_count.setText(
                String.format(
                        UIUtils.getString(R.string.multiply),
                        order2.getRoom_count()
                ));
        viewHolder.tv_item_detail_list_date.setText(
                String.format(
                        UIUtils.getString(R.string.to_symbol),
                        order2.getStart_date(),
                        order2.getEnd_date()
                ));
        viewHolder.tv_item_detail_list_repeat.setText(StringUtils.getRepeatWeek(order2.getRepeat()));
        viewHolder.tv_item_detail_list_time.setText(
                String.format(
                        UIUtils.getString(R.string.to_symbol),
                        StringUtils.changeTime(order2.getStart_time()),
                        StringUtils.changeTime(order2.getEnd_time())
                ));

        if (order2.getDevice_free() != null && order2.getDevice_free().size() > 0) {
            viewHolder.bt_detail_list_classroom_arrangement.setTextColor(UIUtils.getColor(R.color.color_black_33));
            viewHolder.bt_detail_list_classroom_arrangement.setBackgroundResource(R.drawable.background_gray_stroke);
        } else {
            viewHolder.bt_detail_list_classroom_arrangement.setTextColor(UIUtils.getColor(R.color.color_green_1e));
            viewHolder.bt_detail_list_classroom_arrangement.setBackgroundResource(R.drawable.background_green_1eb482_stroke);
        }
        //课室布置按钮
        viewHolder.bt_detail_list_classroom_arrangement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(UIUtils.getContext(), ClassroomArrangementActivity.class);
                intent.putExtra("order2", order2);
                BaseApplication.mForegroundActivity.startActivityForResult(intent, 1);*/
                getSystemTimeRequest(order2);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView tv_item_detail_list_sname;
        private TextView tv_item_detail_list_use_desc;
        private TextView tv_item_detail_list_room_count;
        private TextView tv_item_detail_list_date;
        private TextView tv_item_detail_list_repeat;
        private TextView tv_item_detail_list_time;
        private Button bt_detail_list_classroom_arrangement;

    }

    /**
     * 获取服务端当前时间请求
     *
     * @param o Order2
     */
    public void getSystemTimeRequest(final Order2 o) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_system_time");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result, o);
                    }
                }
        );
    }

    /**
     * 获取服务器系统时间返回处理
     *
     * @param result 返回值
     * @param o      Order2
     */
    private void processData(String result, Order2 o) {
        CommonMsgResult commonMsgResult = GsonTools.fromJson(result, CommonMsgResult.class);
        if ("1".equals(commonMsgResult.getCode())) {
            String sys_time = commonMsgResult.getData();//2015-10-15 15:21:36
            int compare_result = DateUtil.compareDate(sys_time, o.getEnd_date());
            if (compare_result >= 0) {
                //限定  不可点
                UIUtils.showToastSafe(UIUtils.getString(R.string.toast_edit_classroom));
            } else {
                //可点
                Intent i = new Intent(UIUtils.getContext(), ClassroomArrangementActivity.class);
                i.putExtra("order2", o);
                BaseApplication.mForegroundActivity.startActivityForResult(i, 1);
            }
        }
    }

}
