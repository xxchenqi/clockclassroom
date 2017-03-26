package com.yiju.ClassClockRoom.adapter;

import android.content.Context;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.ClassroomItem;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Order3;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.MyListView;

import java.util.List;

public class ClassroomAdapter extends CommonBaseAdapter<ClassroomItem> {
    private Order2 order2;

    public ClassroomAdapter(Context context, List<ClassroomItem> datas,
                            int layoutId, Order2 order2) {
        super(context, datas, layoutId);
        this.order2 = order2;
    }

    @Override
    public void convert(ViewHolder holder, ClassroomItem classroomItem) {
        List<Order3> list = classroomItem.getList();
        if (list.size() != 0) {
            Order3 order3 = list.get(0);
            holder.setText(R.id.tv_item_classroom_date, order3.getDate())
                    .setText(R.id.tv_item_classroom_time,
                            String.format(
                                    UIUtils.getString(R.string.to_symbol),
                                    StringUtils.changeTime(order3.getStart_time()),
                                    StringUtils.changeTime(order3.getEnd_time())
                            ));

            String status = classroomItem.getStatus();
            if (!status.equals("back_order")) {
                MyListView lv = holder.getView(R.id.lv_item_classroom);
                ClassroomInnerAdapter adapter = new ClassroomInnerAdapter(
                        mContext, classroomItem.getList(),
                        R.layout.item_classroom1_inner,
                        classroomItem.getStatus(), order2);
                lv.setAdapter(adapter);
            }

        }

    }

}
