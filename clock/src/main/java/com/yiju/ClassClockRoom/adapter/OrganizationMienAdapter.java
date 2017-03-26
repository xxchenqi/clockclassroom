package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.MineOrganizationBean;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/17 17:43
 * ----------------------------------------
 */
public class OrganizationMienAdapter extends CommonBaseAdapter<MineOrganizationBean.DataEntity.MienEntity> {
    private String org_auth;
    private int mScreenWidth;

    public OrganizationMienAdapter(Context context, List<MineOrganizationBean.DataEntity.MienEntity> datas, int layoutId, int screenWidth) {
        super(context, datas, layoutId);
        this.mScreenWidth = screenWidth;
        org_auth = SharedPreferencesUtils.getString(context,
                context.getResources().getString(R.string.shared_org_auth), "-1");
    }

    @Override
    protected void convert(ViewHolder holder, MineOrganizationBean.DataEntity.MienEntity mienEntity) {
        ImageView iv_item_member = holder.getView(R.id.iv_item_member);
//        ViewGroup.LayoutParams layoutParams = iv_item_member.getLayoutParams();
//        layoutParams.width = (mScreenWidth - UIUtils.dip2px(44)) / 2;//一屏显示两列
//        iv_item_member.setLayoutParams(layoutParams);
        if ("2".equals(org_auth)) {
            if (holder.getPosition() == getCount() - 1) {
                iv_item_member.setBackgroundResource(R.drawable.add_picture_btn);
            } else {
                Glide.with(mContext).load(mienEntity.getPic()).into(iv_item_member);
            }
        } else {
            Glide.with(mContext).load(mienEntity.getPic()).into(iv_item_member);

        }

    }
}
