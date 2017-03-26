package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.Course_Person_Detail_RoomInfo;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * Created by Sandy on 2016/6/28/0028.
 */
public class PerCouTimeAdapter extends BaseAdapter {

    private List<Course_Person_Detail_RoomInfo> mList;
    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public PerCouTimeAdapter(List<Course_Person_Detail_RoomInfo> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        int maxCourse = 3;
        if (flag) {
            return mList.size();
        } else {
            if (mList.size() > maxCourse) {
                return maxCourse;
            } else {
                return mList.size();
            }
        }
    }

    @Override
    public Course_Person_Detail_RoomInfo getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = View.inflate(UIUtils.getContext(), R.layout.item_per_cou_time_normal,null);
        TextView tv_item_per_cou_time = (TextView) view.findViewById(R.id.tv_item_per_cou_time);
        TextView tv_item_per_cou_room = (TextView) view.findViewById(R.id.tv_item_per_cou_room);
        Course_Person_Detail_RoomInfo info = getItem(i);
        tv_item_per_cou_time.setText(info.getTime());
        tv_item_per_cou_room.setText(info.getRoom_no());
        return view;
    }

}
