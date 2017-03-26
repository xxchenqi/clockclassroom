package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.adapter.holder.BaseHolder;
import com.yiju.ClassClockRoom.adapter.holder.ShopCartHolder;

import java.util.ArrayList;
import java.util.List;

public class ShopCartAdapter extends MyBaseAdapter<Order2> {

    private List<Order2> mLists = new ArrayList<>();
    private ImageView iv;
    private TextView tv;
    private TextView price;
    private TextView delete;
    private TextView class_price;
    private TextView should_price;

    public ShopCartAdapter(Context context, List<Order2> mDatas, ImageView iv_cart_all,
                           TextView tv_cart_price, TextView tv_coupon_price,
                           TextView tv_cart_delete, TextView tv_class_price,TextView tv_should_price) {
        super(context, mDatas);
        this.mLists = mDatas;
        this.iv = iv_cart_all;
        this.tv = tv_cart_price;
        this.price = tv_coupon_price;
        this.delete = tv_cart_delete;
        this.class_price = tv_class_price;
        this.should_price = tv_should_price;
    }

    @Override
    public BaseHolder<Order2> getHolder() {
        return new ShopCartHolder(mContext, iv, mLists, tv, price, delete, class_price,should_price);
    }


}
