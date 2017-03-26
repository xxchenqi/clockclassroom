package com.yiju.ClassClockRoom.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.ContactBean.Data;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;

public class ContactAdapter extends CommonBaseAdapter<Data> {
	private int color;

	public ContactAdapter(Context context, List<Data> datas, int layoutId) {
		super(context, datas, layoutId);
		color = mContext.getResources().getColor(R.color.color_gay_8f);
	}

	@Override
	public void convert(ViewHolder holder, Data data) {
		String isdefault = data.getIsdefault();

		if ("1".equals(isdefault)) {
			holder.setText(R.id.tv_contact_name, data.getName()).setText(
					R.id.tv_contact_phone, data.getMobile());
		} else {
			holder.setTextAndColor(R.id.tv_contact_name, data.getName(), color)
					.setTextAndColor(R.id.tv_contact_phone, data.getMobile(),
							color);
			holder.getView(R.id.tv_contact_default).setVisibility(View.GONE);
			holder.getView(R.id.iv_contact_choose).setVisibility(View.GONE);

		}

	}

}
