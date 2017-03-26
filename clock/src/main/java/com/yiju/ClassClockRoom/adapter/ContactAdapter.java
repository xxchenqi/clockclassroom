package com.yiju.ClassClockRoom.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.ContactBean.Data;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;

public class ContactAdapter extends CommonBaseAdapter<Data> {

	public ContactAdapter(Context context, List<Data> datas, int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, Data data) {
		String isdefault = data.getIsdefault();

		if ("1".equals(isdefault)) {
			holder.setText(R.id.tv_contact_name, data.getName())
					.setText(R.id.tv_contact_phone, data.getMobile());
		} else {
			holder.setText(R.id.tv_contact_name, data.getName())
					.setText(R.id.tv_contact_phone, data.getMobile());
			holder.getView(R.id.tv_contact_default).setVisibility(View.GONE);
		}

	}

}
