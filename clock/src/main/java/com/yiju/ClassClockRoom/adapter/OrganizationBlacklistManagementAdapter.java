package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.OrganizationBlacklistBean;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/14 14:17
 * ----------------------------------------
 */
public class OrganizationBlacklistManagementAdapter extends CommonBaseAdapter<OrganizationBlacklistBean.DataEntity> {

    private ListItemClickHelp itemClickHelp;
    public OrganizationBlacklistManagementAdapter(
            Context context,
            List<OrganizationBlacklistBean.DataEntity> datas,
            int layoutId,
            ListItemClickHelp i
    ) {
        super(context, datas, layoutId);
        this.itemClickHelp = i;
    }

    @Override
    protected void convert(final ViewHolder holder, final OrganizationBlacklistBean.DataEntity dataEntity) {
        holder.setText(R.id.tv_item_blacklist_name, dataEntity.getName());
        ImageView iv_item_blacklist = holder.getView(R.id.iv_item_blacklist);
        Glide.with(mContext).load(dataEntity.getLogo()).into(iv_item_blacklist);
        holder.getView(R.id.tv_tv_item_blacklist_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickHelp.onClickItem(holder.getPosition());
            }
        });
    }
}
