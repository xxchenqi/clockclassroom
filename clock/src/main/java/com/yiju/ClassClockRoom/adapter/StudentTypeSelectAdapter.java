package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.StudentType;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * 学生类型选择适配器
 */
public class StudentTypeSelectAdapter extends BaseAdapter {

    private List<StudentType> mLists;
    private LayoutInflater mInflater;
    private String current_student_type;

    public StudentTypeSelectAdapter(Context con, List<StudentType> lists, String current_student_type) {
        mInflater = LayoutInflater.from(con);
        this.mLists = lists;
        this.current_student_type = current_student_type;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_student_type_select, null);
            holder.name = (TextView) convertView.findViewById(R.id.item_student_name);
            holder.v = convertView.findViewById(R.id.item_v);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StudentType info = mLists.get(position);
        holder.name.setText(info.getType_desc());
        if (StringUtils.isNotNullString(current_student_type)) {
            if (current_student_type.equals(info.getId())) {
                holder.name.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            } else {
                holder.name.setTextColor(UIUtils.getColor(R.color.color_gay_8f));
            }
        } else {
            if (info.getIs_default().equals("1")) {
                holder.name.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            } else {
                holder.name.setTextColor(UIUtils.getColor(R.color.color_gay_8f));
            }
        }
        if (position == mLists.size() - 1) {
            holder.v.setVisibility(View.GONE);
        } else {
            holder.v.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView name;
        private View v;
    }
}