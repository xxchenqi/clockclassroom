package com.yiju.ClassClockRoom.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.MoreStoreBean.MoreStoreEntity;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:更多门店适配器
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/9/5 10:33
 * ----------------------------------------
 */
public class MoreStoreAdapter extends CommonBaseAdapter<MoreStoreEntity> {

    public MoreStoreAdapter(Context context, List<MoreStoreEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, MoreStoreEntity moreStoreEntity) {
        Double distances = moreStoreEntity.getDistances();
        ImageView iv_more_store_pic = holder.getView(R.id.iv_more_store_pic);
        TextView tv_item_store = holder.getView(R.id.tv_item_store);
        if ("1".equals(moreStoreEntity.getSchool_type())) {
            Drawable drawable = UIUtils.getDrawable(R.drawable.ziying);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_item_store.setCompoundDrawables(drawable, null, null, null);
                tv_item_store.setCompoundDrawablePadding(10);
            }
        } else {
            tv_item_store.setCompoundDrawables(null, null, null, null);
        }
        holder.setText(R.id.tv_item_store, moreStoreEntity.getName())
                .setText(R.id.tv_item_subject, moreStoreEntity.getUse().replace(",", " "))
                .setText(R.id.tv_item_store_address, moreStoreEntity.getAddress())
                .setText(R.id.tv_item_distance, distances + "km");
        Glide.with(UIUtils.getContext())
                .load(moreStoreEntity.getPic_small())
                .error(R.drawable.clock_wait)
                .into(iv_more_store_pic);
    }
}
