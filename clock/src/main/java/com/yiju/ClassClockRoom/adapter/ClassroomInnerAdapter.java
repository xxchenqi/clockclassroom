package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Order3;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.control.share.ShareDialog;
import com.yiju.ClassClockRoom.util.StringUtils;

import java.util.List;

public class ClassroomInnerAdapter extends CommonBaseAdapter<Order3> {

    private String status;
    private Order2 order2;

    public ClassroomInnerAdapter(Context context, List<Order3> datas,
                                 int layoutId, String status, Order2 order2) {
        super(context, datas, layoutId);
        this.status = status;
        this.order2 = order2;
    }

    @Override
    public void convert(ViewHolder holder, final Order3 o3) {
        holder.setText(R.id.tv_item_classroom_class, "教室:" + o3.getRoom_no());
        ImageView iv = holder.getView(R.id.iv_item_classroom_share);

        if ("no_finish".equals(status)) {
            if (StringUtils.isNotNullString(o3.getVideo_pas())) {
                holder.setText(R.id.tv_item_classroom_password,
                        "密码:" + o3.getVideo_pas());
            }
            iv.setImageResource(R.drawable.new_share);
            iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ShareDialog
                            .getInstance()
                            .setCurrent_Type(ShareDialog.Type_Share_Accompany_Key)
                            .setSchool_name(order2.getSname())
                            .setStart_time(changeTime(o3.getStart_time()))
                            .setEnd_time(changeTime(o3.getEnd_time()))
                            .setVideo_pas(o3.getVideo_pas())
                            .setVid(o3.getId())
                            .setRoom_no(o3.getRoom_no())
                            .setDate(o3.getDate())
                            .setVisiblePwd_Layout(true)
                            .showDialog();
                }
            });
        } else {
            holder.setText(R.id.tv_item_classroom_password, "密码:****");
            iv.setImageResource(R.drawable.share_gray);
        }

        if (o3.isCourse_flag()) {
            holder.setText(R.id.tv_item_classroom_password, "已绑定课程");
        }


    }

    /*
     * 时间转换
     */
    private String changeTime(String start_time) {
        String h;
        String m;
        if (start_time.length() < 4) {
            h = "0" + start_time.substring(0, 1);
            m = start_time.substring(1);
        } else {
            h = start_time.substring(0, 2);
            m = start_time.substring(2);
        }
        return h + ":" + m;
    }

}
