package com.yiju.ClassClockRoom.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.StudentType;
import com.yiju.ClassClockRoom.util.UIUtils;

public class GVHolder extends BaseHolder<StudentType> {

    private View item_v;
    private TextView item_student_name;

    public GVHolder(Context context) {
        super(context);
    }

    @Override
    public View initView(Context context) {
        View view = View.inflate(context, R.layout.item_student_type, null);
        item_student_name = (TextView) view.findViewById(R.id.item_student_name);
        item_v = view.findViewById(R.id.item_v);
        return view;
    }

    @Override
    public void refreshView() {
        StudentType data = getData();
        if (null != data) {
            item_student_name.setText(data.getType_name());
            if (data.getIs_default().equals("1")) {
                item_student_name.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            } else {
                item_student_name.setTextColor(UIUtils.getColor(R.color.color_gay_8f));
            }
        }
    }
}
