package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.DayTimeData;
import com.yiju.ClassClockRoom.common.callback.ListItemClickTwoData;
import com.yiju.ClassClockRoom.adapter.holder.DateLittleDayHolder;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

public class DateLittleDayAdapter extends BaseAdapter {

    private Context context;
    private List<DayTimeData> mList;
    private ListItemClickTwoData listener;

    public DateLittleDayAdapter(Context context, List<DayTimeData> list, ListItemClickTwoData listItemClickTwoData) {
        this.context = context;
        this.mList = list;
        this.listener = listItemClickTwoData;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        DateLittleDayHolder vh;

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_date_little_day, null);
            setViewHolder(view);
        } else if (((DateLittleDayHolder) convertView.getTag()).needInflate) {
            view = View.inflate(context, R.layout.item_date_little_day, null);
            setViewHolder(view);
        } else {
            view = convertView;
        }

        vh = (DateLittleDayHolder) view.getTag();
        DayTimeData data = mList.get(position);

        vh.tv_item_little_time.setText(data.getTime());
        vh.tv_item_little_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteItem(view, position);
            }
        });
        vh.rl_item_little_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(position);
            }
        });
        if (data.isFlag()) {
            vh.tv_item_little_type.setText(
                    String.format(
                            UIUtils.getString(R.string.txt_insufficient_inventory),
                            data.getDayTitle()
                    ));
            vh.tv_item_little_type.setTextColor(UIUtils.getColor(R.color.orange));
        } else {
            vh.tv_item_little_type.setText(data.getDayTitle());
            vh.tv_item_little_type.setTextColor(UIUtils.getColor(R.color.order_black));
        }
        //        vh.tv_item_little_type.setText(data.getDayTitle());
        /*String[] datas = data.split(",");
        vh.tv_item_little_type.setText(datas[0] + " " + datas[1]);
        vh.tv_item_little_time.setText(datas[2]);*/

        /*if(datas.length == 4){
            if(datas[3].equals("t")){
                vh.tv_item_little_type.setText(datas[0] + " " + datas[1]+" 库存不足" );
                vh.tv_item_little_type.setTextColor(UIUtils.getColor(R.color.orange));
            }else{
                vh.tv_item_little_type.setText(datas[0] + " " + datas[1] );
                vh.tv_item_little_type.setTextColor(UIUtils.getColor(R.color.order_black));
            }
        }*/
        return view;
    }

    private void setViewHolder(View view) {
        DateLittleDayHolder vh = new DateLittleDayHolder();
        vh.tv_item_little_type = (TextView) view.findViewById(R.id.tv_item_little_type);
        vh.tv_item_little_delete = (TextView) view.findViewById(R.id.tv_item_little_delete);
        vh.rl_item_little_time = (RelativeLayout) view.findViewById(R.id.rl_item_little_time);
        vh.tv_item_little_time = (TextView) view.findViewById(R.id.tv_item_little_time);
        vh.needInflate = false;
        view.setTag(vh);
    }
}
