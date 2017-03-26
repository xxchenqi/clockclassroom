package com.yiju.ClassClockRoom.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
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
        ImageView iv_item_index_store_pic = holder.getView(R.id.iv_item_index_store_pic);
        TextView tv_item_index_store_name = holder.getView(R.id.tv_item_index_store_name);
        TextView tv_item_index_store_tag = holder.getView(R.id.tv_item_index_store_tag);
        TextView tv_item_index_store_address = holder.getView(R.id.tv_item_index_store_address);
        TextView tv_item_index_store_distance = holder.getView(R.id.tv_item_index_store_distance);
        TextView tv_item_index_store_booking = holder.getView(R.id.tv_item_index_store_booking);

        //can_schedule判断 =1是可预订 =0是不可预订
        if ("1".equals(moreStoreEntity.getCan_schedule())) {
            tv_item_index_store_booking.setVisibility(View.GONE);
        } else {
            tv_item_index_store_booking.setVisibility(View.VISIBLE);
        }
        if ("1".equals(moreStoreEntity.getSchool_type())) {
            Drawable drawable = UIUtils.getDrawable(R.drawable.chain);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_item_index_store_name.setCompoundDrawables(drawable, null, null, null);
                tv_item_index_store_name.setCompoundDrawablePadding(UIUtils.getDimens(R.dimen.DIMEN_5DP));
            }
        } else {
            tv_item_index_store_name.setCompoundDrawables(null, null, null, null);
        }
        tv_item_index_store_name.setText(moreStoreEntity.getName());
        tv_item_index_store_tag.setText(moreStoreEntity.getUse().replace(",", " "));
        tv_item_index_store_address.setText(
                String.format(
                        UIUtils.getString(R.string.txt_address_before),
                        moreStoreEntity.getAddress()
                ));
        if (moreStoreEntity.getDistances() >= 1) {
            tv_item_index_store_distance.setText(
                    String.format(
                            UIUtils.getString(R.string.txt_more_store_diatance),
                            moreStoreEntity.getDistances()
                    ));
        } else {
            tv_item_index_store_distance.setText(
                    String.format(
                            UIUtils.getString(R.string.txt_more_store_distance_m),
                            moreStoreEntity.getDistances() * 1000
                    ));
        }
        Glide.with(UIUtils.getContext())
                .load(moreStoreEntity.getPic_small())
                .error(R.drawable.clock_wait)
                .into(iv_item_index_store_pic);
    }
}
