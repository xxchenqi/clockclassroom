package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;

import java.util.List;

/**
 * --------------------------------------
 * <p/>
 * 注释:万能适配器基类
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-9 上午9:30:05
 * <p/>
 * --------------------------------------
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter {
    protected Context mContext;
    private List<T> mDatas;
    private LayoutInflater mInflater;
    private int layoutId;

    protected CommonBaseAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    public void updateDatas(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, layoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    protected abstract void convert(ViewHolder holder, T t);


}
