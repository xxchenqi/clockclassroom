package com.yiju.ClassClockRoom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiju.ClassClockRoom.R;

public class FragmentSlide extends BaseFragment {

	public static final String LAYOUT_ID = "layout_id";
	
	private LayoutInflater inflater;
	private ViewGroup container;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (currentView != null) {
			ViewGroup parent = (ViewGroup) currentView.getParent();
			if (parent != null) {
				parent.removeView(currentView);
			}
			return currentView;
		}

		this.inflater = inflater;
		this.container = container;
		currentView = inflater.inflate(getLayoutId(), container, false);
		return currentView;
	}


	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {

	}

	@Override
	public int setContentViewId() {
		return getLayoutId();
	}


	@Override
	public String getPageName() {
		return "";
	}

	private int getLayoutId() {
		Bundle bundle = getArguments();
		if (bundle == null) {
			return R.layout.guide01;
		}
		return bundle.getInt(LAYOUT_ID);
	}
}
