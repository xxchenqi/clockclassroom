package com.yiju.ClassClockRoom.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * Created by Sandy on 2016/6/23/0023.
 */
public class PersonMineCourseDetailAdapter extends BaseAdapter{

    private List<String> mUrls;
    public PersonMineCourseDetailAdapter(List<String> urls) {
        this.mUrls = urls;
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public String getItem(int i) {
        return mUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = View.inflate(UIUtils.getContext(), R.layout.item_member_detail,null);
        }
        ImageView iv_item_member = (ImageView) view.findViewById(R.id.iv_item_member);
        String url = getItem(i);
        Glide.with(UIUtils.getContext()).load(url).into(iv_item_member);
        return view;
    }
}
