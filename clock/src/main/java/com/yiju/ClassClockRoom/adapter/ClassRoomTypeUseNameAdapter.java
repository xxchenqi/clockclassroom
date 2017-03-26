package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.ClassroomArrangeData;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * classroomType use_name adapter
 * Created by wh on 2016/5/16.
 */
public class ClassRoomTypeUseNameAdapter extends BaseAdapter {

    private List<ClassroomArrangeData.UseEntity> use;

    public ClassRoomTypeUseNameAdapter(List<ClassroomArrangeData.UseEntity> use) {
        this.use = use;
    }

    @Override
    public int getCount() {
        return use.size();
    }

    @Override
    public Object getItem(int position) {
        return use.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(UIUtils.getContext(), R.layout.item_classroomtypeusename, null);
            viewHolder.tv_use_name = (TextView) convertView.findViewById(R.id.tv_use_name);
            viewHolder.iv_icon_select = (ImageView) convertView.findViewById(R.id.iv_icon_select);
            viewHolder.view_line = convertView.findViewById(R.id.view_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_use_name.setText(use.get(position).getUse_name());
        if (use.get(position).isSelect()) {
            viewHolder.iv_icon_select.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_icon_select.setVisibility(View.GONE);
        }

        if (position == use.size() - 1) {
            viewHolder.view_line.setVisibility(View.GONE);
        } else {
            viewHolder.view_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_use_name;
        private ImageView iv_icon_select;
        private View view_line;
    }
}
