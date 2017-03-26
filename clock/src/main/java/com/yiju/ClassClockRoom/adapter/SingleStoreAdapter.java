package com.yiju.ClassClockRoom.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.IndexDetailActivity;
import com.yiju.ClassClockRoom.act.ReservationActivity;
import com.yiju.ClassClockRoom.bean.Room;
import com.yiju.ClassClockRoom.bean.RoomTypeInfo;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * 单一门店课室列表适配器
 * Created by wh on 2016/9/5.
 */
public class SingleStoreAdapter extends BaseAdapter {
    //数据源
    private List<RoomTypeInfo> typeInfos;
    private Room room;

    public SingleStoreAdapter(Room mRoom) {
        this.room = mRoom;
        this.typeInfos = mRoom.getRoom_type();
    }

    @Override
    public int getCount() {
        return typeInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return typeInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(UIUtils.getContext(), R.layout.item_room_child_type_new, null);
            holder.iv_item_room_pic = (ImageView)
                    convertView.findViewById(R.id.iv_item_room_pic);
            holder.tv_class_tag = (TextView)
                    convertView.findViewById(R.id.tv_class_tag);
            holder.tv_desc = (TextView)
                    convertView.findViewById(R.id.tv_desc);
            holder.tv_day_price = (TextView)
                    convertView.findViewById(R.id.tv_day_price);
            holder.tv_week_price = (TextView)
                    convertView.findViewById(R.id.tv_week_price);
            holder.tv_reserve = (TextView)
                    convertView.findViewById(R.id.tv_reserve);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RoomTypeInfo info = typeInfos.get(position);
        if (holder != null) {
            String picSmall = info.getPic_small();
            if (StringUtils.isNotNullString(picSmall)) {
                Glide.with(UIUtils.getContext()).load(picSmall).into(holder.iv_item_room_pic);
            }
            holder.tv_class_tag.setText(info.getDesc());
            if (StringUtils.isNotNullString(info.getDesc_app())) {
                holder.tv_desc.setText(info.getDesc_app());
                holder.tv_desc.setVisibility(View.VISIBLE);
            } else {

                holder.tv_desc.setVisibility(View.GONE);
            }
            String dayP = info.getPrice_weekday();
            String wendP = info.getPrice_weekend();
            holder.tv_day_price.setText(
                    String.format(
                            UIUtils.getString(R.string.rmb_how_much),
                            dayP.substring(0, dayP.indexOf(".")))
            );
            holder.tv_week_price.setText(
                    String.format(
                            UIUtils.getString(R.string.rmb_how_much),
                            wendP.substring(0, wendP.indexOf(".")))
            );
            // 课室图片点击
            holder.iv_item_room_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UIUtils.getContext(), IndexDetailActivity.class);
                    intent.putExtra("sid", room.getId());
                    intent.putExtra("sname", room.getName());
                    intent.putExtra("type_desc", info.getDesc());
                    intent.putExtra("typeid", info.getId());
                    intent.putExtra("room_start_time", room.getStart_time());
                    intent.putExtra("room_end_time", room.getEnd_time());
                    intent.putExtra("room_name", info.getDesc());
                    intent.putExtra("can_schedule", room.getCan_schedule());
                    intent.putExtra("instruction", room.getInstruction());
                    intent.putExtra("confirm_type", room.getConfirm_type());
                    BaseApplication.getmForegroundActivity().startActivity(intent);
                }
            });
            //点击预订按钮
            holder.tv_reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UIUtils.getContext(), ReservationActivity.class);
                    intent.putExtra("sid", room.getId());
                    intent.putExtra("name", room.getName());//name: "测试徐汇虹梅商务大厦旗舰店",
                    intent.putExtra("type_id", info.getId());
                    intent.putExtra("room_start_time", room.getStart_time());
                    intent.putExtra("room_end_time", room.getEnd_time());
                    intent.putExtra("room_name", info.getDesc());//desc: "大间课室(有窗)",
                    intent.putExtra("instruction", room.getInstruction());
                    intent.putExtra("confirm_type", room.getConfirm_type());
                    BaseApplication.getmForegroundActivity().startActivity(intent);
                }
            });

            //can_schedule判断 =1是可预订 =0是不可预订
            if ("1".equals(room.getCan_schedule())) {
                holder.tv_reserve.setText(R.string.reserve_space);
                holder.tv_reserve.setBackgroundResource(R.drawable.background_green_1eb482_radius_3);
                holder.tv_reserve.setClickable(true);
            } else {
                holder.tv_reserve.setText(R.string.coming_reserve_space);
                holder.tv_reserve.setBackgroundResource(R.drawable.background_gray_cccccc_radius_3);
                holder.tv_reserve.setClickable(false);
            }

        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_item_room_pic;
        private TextView tv_class_tag;
        private TextView tv_desc;
        private TextView tv_day_price;
        private TextView tv_week_price;
        private TextView tv_reserve;
    }
}
