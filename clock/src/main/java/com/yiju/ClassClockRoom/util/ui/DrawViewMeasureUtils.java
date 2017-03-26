package com.yiju.ClassClockRoom.util.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DrawViewMeasureUtils {
	// 修改Listview的高度
	public static void setListViewHeightBasedOnChildren(ListView listView,
			int attHeight) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ attHeight;
		listView.setLayoutParams(params);
	}

	// 修改Listview的高度
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// 修改Listview的高度
	public static void setListViewHeightBasedOnChildren(
			ExpandableListView listView, int position) {
		ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View groupItem;
			if (i == position) {
				groupItem = listAdapter.getGroupView(i, true, null, listView);
				View listItem = listAdapter.getChildView(i, 0, false, null,
						listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			} else {
				groupItem = listAdapter.getGroupView(i, false, null, listView);
			}
			groupItem.measure(0, 0);
			totalHeight += groupItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		if (position == -1) {
			params.height = totalHeight
					+ ((listAdapter.getGroupCount() - 1) * listView
							.getDividerHeight());
		} else {
			params.height = totalHeight
					+ ((listAdapter.getGroupCount() + 1) * listView
							.getDividerHeight());
		}
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

//	public static int measureIndexOffset(ListView listView,int position){
//		int result = 0;
//		int index = listView.getFirstVisiblePosition();
//		if (position  >= index){
//			View v = listView.getChildAt(position - index);
//			int top = (v == null) ? 0 : v.getTop();
//			int height = 0;
//			if(v != null){
//				ImageView iv_image = (ImageView) v.findViewById(R.id.iv_image);
//				if (iv_image == null){
//					return result;
//				}
//				height = iv_image.getHeight();
//			}
//			//-(height -(v.getHeight() - Math.abs(top)))
//			result = top + (v != null ? v.getHeight() : 0) -height;
//		}
//		return result;
//	}
}
