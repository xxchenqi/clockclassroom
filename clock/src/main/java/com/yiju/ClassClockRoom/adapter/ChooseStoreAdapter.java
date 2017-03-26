package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.ChooseStoreBean;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/7/5 13:43
 * ----------------------------------------
 */
public class ChooseStoreAdapter extends CommonBaseAdapter<ChooseStoreBean.DataEntity> {

    public ChooseStoreAdapter(Context context, List<ChooseStoreBean.DataEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, ChooseStoreBean.DataEntity dataEntity) {
        holder.setText(R.id.tv_item_store, dataEntity.getName())
                .setText(R.id.tv_item_store_address, dataEntity.getAddress())
                .setText(R.id.tv_item_store_distance, StringUtils.getDistanceStr(dataEntity.getLat_g(),dataEntity.getLng_g()));
        ImageView iv_item_store_choose = holder.getView(R.id.iv_item_store_choose);
        TextView tv_item_store = holder.getView(R.id.tv_item_store);


        Drawable drawable = UIUtils.getDrawable(R.drawable.chain);
        if(dataEntity.getSchool_type().equals("1")){
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_item_store.setCompoundDrawables(drawable, null, null, null);
                tv_item_store.setCompoundDrawablePadding(10);
            }
        }else {
            tv_item_store.setCompoundDrawables(null,null,null,null);
        }
        if (dataEntity.isFlag()) {
            iv_item_store_choose.setVisibility(View.VISIBLE);
        } else {
            iv_item_store_choose.setVisibility(View.GONE);
        }
    }
}
