package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.widget.CheckBox;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.ClassroomItem;
import com.yiju.ClassClockRoom.bean.Order3;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;

import java.util.List;

public class BackOrderAdapter extends CommonBaseAdapter<ClassroomItem> {

	public BackOrderAdapter(Context context, List<ClassroomItem> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, ClassroomItem classroomItem) {
		List<Order3> list = classroomItem.getList();
		Order3 order3 = list.get(0);

		holder.setText(R.id.tv_item_backorder_date, order3.getDate()).setText(
				R.id.tv_item_backorder_time,
				changeTime(order3.getStart_time()) + "-"
						+ changeTime(order3.getEnd_time()));
		
		CheckBox cb = holder.getView(R.id.cb_item_backorder_choose);
		cb.setChecked(classroomItem.isCb_status());
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
