package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class TroubleWheelAdapter extends AbstractWheelTextAdapter {
    private String[] datas;

    private int currentIndex;
    private String endTxt;

    public TroubleWheelAdapter(Context context) {
        super(context);
    }

    public TroubleWheelAdapter(Context context, String[] datas) {
        super(context, R.layout.item_trouble_wheel_layout);
        this.datas = datas;
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        View view = super.getItem(index, convertView, parent);
        TextView textView = (TextView) view
                .findViewById(R.id.item_trouble_text);
        String sum_data = datas[index] + getEndStr();
        textView.setText(sum_data);
        if (currentIndex == index) {
            textView.setTextColor(UIUtils.getColor(R.color.app_theme_color));
        } else {
            textView.setTextColor(UIUtils.getColor(R.color.color_gay_99));
        }
        return view;
    }

    @Override
    public int getItemsCount() {
        return datas.length;
    }

    @Override
    protected CharSequence getItemText(int index) {
        return datas[index];
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
        notifyDataChangedEvent();
    }

    public void setEndStr(String endTxt) {
        this.endTxt = endTxt;
    }

    private String getEndStr() {
        return StringUtils.formatNullString(endTxt);
    }
}
