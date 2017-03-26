package com.yiju.ClassClockRoom.common.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.yiju.ClassClockRoom.BaseApplication;

public abstract class BaseDialog extends Dialog {
	public Context _last_context;
	public View dialog_layout;

	public BaseDialog(Context context) {
		super(context);
		_last_context = context;
		initView();
		initData();
		initListener();
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		_last_context = context;
		initView();
		initData();
		initListener();
	}

	public Context getLastContext() {
		return _last_context;
	}

	public boolean isContextChanged() {

		return _last_context == null
				|| !_last_context.equals(BaseApplication.getmForegroundActivity());
	}

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void initListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getWindow() != null){
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
			getWindow().setAttributes(
					params);
		}
	}
}
