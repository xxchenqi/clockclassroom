package com.yiju.ClassClockRoom.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.EditContactActivity;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.bean.ContactBean.Data;
import com.yiju.ClassClockRoom.util.UIUtils;

public class ShopcartContactHolder extends BaseHolder<Data> {

	private ImageView iv_contact_choose;
	private TextView tv_contact_name;
	private TextView tv_contact_default;
	private TextView tv_contact_phone;
	private ImageView iv_item_shopcart_change;
	private Data info;

	public ShopcartContactHolder(Context context) {
		super(context);
	}

	@Override
	public View initView(final Context context) {
		// 加载布局
		View view = View.inflate(context, R.layout.item_shopcart_contact, null);
		iv_contact_choose = (ImageView) view
				.findViewById(R.id.iv_contact_choose);
		tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
		tv_contact_default = (TextView) view
				.findViewById(R.id.tv_contact_default);
		tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);
		iv_item_shopcart_change = (ImageView) view
				.findViewById(R.id.iv_item_shopcart_change);
		iv_item_shopcart_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 修改联系人
				Intent intent = new Intent(context, EditContactActivity.class);
				intent.putExtra("data", info);
				intent.putExtra("flag", "edit");
				BaseApplication.getmForegroundActivity()
						.startActivityForResult(intent, 0);

			}
		});
		return view;
	}

	@Override
	public void refreshView() {
		info = getData();
		if (null != info) {
//			String check = SharedPreferencesUtils.getString(UIUtils.getContext(), "check", "");
			
			/*
			 * if(info.isCheck()){ }else{
			 * iv_contact_choose.setBackgroundResource
			 * (R.drawable.order_nonechoose_btn); }
			 */
			if (info.getIsdefault().equals("1")) {
				tv_contact_name.setText(info.getName());
				tv_contact_phone.setText(info.getMobile());
				tv_contact_default.setVisibility(View.VISIBLE);
			} else {
				tv_contact_name.setText(info.getName());
				tv_contact_name.setTextColor(UIUtils
						.getColor(R.color.color_gay_8f));
				tv_contact_phone.setText(info.getMobile());
				tv_contact_phone.setTextColor(UIUtils
						.getColor(R.color.color_gay_8f));
				tv_contact_default.setVisibility(View.GONE);
			}
			if (info.isCheck()) {
				iv_contact_choose
						.setBackgroundResource(R.drawable.order_choose_btn);
				tv_contact_name.setTextColor(UIUtils.getColor(R.color.app_theme_color));
				tv_contact_phone.setTextColor(UIUtils.getColor(R.color.app_theme_color));
			} else {
				iv_contact_choose
						.setBackgroundResource(R.drawable.order_nonechoose_btn);
				tv_contact_name.setTextColor(UIUtils.getColor(R.color.color_gay_8f));
				tv_contact_phone.setTextColor(UIUtils.getColor(R.color.color_gay_8f));
			}
		}
	}

}
