package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.DataAlone;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

public class TimeFailAdapter extends BaseAdapter{

    private List<DataAlone> mDatas;
    private TextView tv_time_fail;
    private ImageView iv_time_fail;
    private int position = Integer.MAX_VALUE;

    public void setPosition(int position) {
        this.position = position;
    }

    public TimeFailAdapter(List<DataAlone> mLists) {
        this.mDatas = mLists;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = View.inflate(UIUtils.getContext(), R.layout.item_time_fail, null);
        tv_time_fail = (TextView) view.findViewById(R.id.tv_time_fail);
        iv_time_fail = (ImageView) view.findViewById(R.id.iv_time_fail);

        DataAlone data = mDatas.get(i);
        if(null != data){
            tv_time_fail.setText(data.getListdata());
            if(position == i){
                iv_time_fail.setVisibility(View.VISIBLE);
            }else{
                iv_time_fail.setVisibility(View.GONE);
            }
        }
        return view;
    }
}
