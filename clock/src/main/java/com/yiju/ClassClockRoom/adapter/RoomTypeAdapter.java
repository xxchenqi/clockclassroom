package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.RoomTypeInfo;

import java.util.List;


public class RoomTypeAdapter extends BaseAdapter {

    private List<RoomTypeInfo> mLists = null;
    private Context mContext;
    private Holder holder;
    private String sid;
    private int selectPosition = Integer.MAX_VALUE;
    private ItemClickHelp mListener;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public RoomTypeAdapter(Context context,
                           List<RoomTypeInfo> lists, String s, ItemClickHelp l) {
        this.mContext = context;
        this.mLists = lists;
        this.sid = s;
        this.mListener = l;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(mContext, R.layout.item_room_type, null);
            holder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.tv_item_desk = (TextView) convertView.findViewById(R.id.tv_item_desk);
            holder.tv_item_day_price = (TextView) convertView.findViewById(R.id.tv_item_day_price);
            holder.tv_item_week_price = (TextView) convertView.findViewById(R.id.tv_item_week_price);
            holder.iv_item_roompic = (ImageView) convertView.findViewById(R.id.iv_item_roompic);
            holder.iv_item_choose = (ImageView) convertView.findViewById(R.id.iv_item_choose);
            holder.tv_item_aere = (TextView) convertView.findViewById(R.id.tv_item_aere);
            holder.rl_item_choose = (RelativeLayout) convertView.findViewById(R.id.rl_item_choose);
            holder.iv_room_desc = (ImageView) convertView.findViewById(R.id.iv_room_desc);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final RoomTypeInfo info = mLists.get(position);
        holder.tv_item_name.setText(info.getDesc());
        holder.tv_item_desk.setText(info.getDesc_app());
        holder.tv_item_day_price.setText(info.getPrice_weekday());
        holder.tv_item_week_price.setText(info.getPrice_weekend());
        String area = info.getArea();
        if ("-1".equals(area)) {
            holder.tv_item_aere.setVisibility(View.GONE);
        } else {
            holder.tv_item_aere.setVisibility(View.VISIBLE);
            holder.tv_item_aere.setText(info.getArea());
        }
        holder.rl_item_choose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 点击事件
                mListener.onCheckPosition(position);
            }
        });
        holder.iv_room_desc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 点击事件
                mListener.onClickPosition(position);
            }
        });
        if (!("").equals(info.getPic_small())) {
            Glide.with(mContext).load(info.getPic_small()).into(holder.iv_item_roompic);
        } else {
            holder.iv_item_roompic.setImageResource(R.drawable.bg_placeholder_4_3);
        }
        if (selectPosition == position) {
            holder.iv_item_choose.setImageResource(R.drawable.check_icon);
//			UIUtils.startActivity(mIntent);
        }
        return convertView;
    }

    static class Holder {
        TextView tv_item_name;
        TextView tv_item_day_price;
        TextView tv_item_week_price;
        TextView tv_item_desk;
        ImageView iv_item_roompic;
        ImageView iv_item_choose;
        //		LinearLayout ll_item_detail;
        TextView tv_item_aere;
        RelativeLayout rl_item_choose;
        ImageView iv_room_desc;
    }

    public interface ItemClickHelp {
        void onClickPosition(int position);

        void onCheckPosition(int position);
    }
}
