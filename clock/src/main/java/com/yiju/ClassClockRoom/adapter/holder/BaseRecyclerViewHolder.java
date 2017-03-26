package com.yiju.ClassClockRoom.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/*
 * Created by wh on 2016/3/17.
 */
class BaseRecyclerViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    protected int position;
    protected View rootView;
    protected View.OnClickListener clickListener;
    private Object model;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
        setListeners();
    }


    public BaseRecyclerViewHolder(
            View view,
            View.OnClickListener clickListener
    ) {
        this(view);
        this.clickListener = clickListener;
    }

    protected void findViews(View view) {
        rootView = view;
    }

    protected void setListeners() {
    }

    public void updateView(int position) {
        this.position = position;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            v.setTag(getModel());
            clickListener.onClick(v);
        }
    }
}
