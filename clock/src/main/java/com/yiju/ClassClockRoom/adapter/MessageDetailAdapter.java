package com.yiju.ClassClockRoom.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.MessageBox;
import com.yiju.ClassClockRoom.adapter.holder.MessageDetailHolder;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

public class MessageDetailAdapter extends RecyclerView.Adapter<MessageDetailHolder> {

    private List<MessageBox.MessageData> mList;

    public MessageDetailAdapter(List<MessageBox.MessageData> mLists) {
        this.mList = mLists;
    }

    @Override
    public MessageDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MessageDetailHolder(
                View.inflate(UIUtils.getContext(),R.layout.item_message,null)
        );
    }

    @Override
    public void onBindViewHolder(MessageDetailHolder viewHolder, int position) {
        viewHolder.setModel(getItem(position));
        viewHolder.updateView(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public MessageBox.MessageData getItem(int position){
        return mList.get(position);
    }

}
