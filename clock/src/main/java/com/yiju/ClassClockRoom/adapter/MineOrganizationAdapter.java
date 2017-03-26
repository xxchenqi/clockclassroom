package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.MemberBean;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.util.StringUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/14 16:53
 * ----------------------------------------
 */
public class MineOrganizationAdapter extends CommonBaseAdapter<MemberBean.DataEntity> {


    public MineOrganizationAdapter(Context context, List<MemberBean.DataEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(ViewHolder holder, MemberBean.DataEntity dataEntity) {
        ViewHolder.setFlag(true);
        ImageView iv_item_mine_organization = holder.getView(R.id.iv_item_mine_organization);
        TextView tv_teacher_data = holder.getView(R.id.tv_teacher_data);
        if (StringUtils.isNotNullString(dataEntity.getAvatar())) {
            Glide.with(mContext).load(dataEntity.getAvatar()).into(iv_item_mine_organization);
        }
        if ("0".equals(dataEntity.getFullteacherinfo())) {//0：不全 1：全
            tv_teacher_data.setVisibility(View.VISIBLE);
        } else {
            tv_teacher_data.setVisibility(View.GONE);
        }

        holder.setText(R.id.tv_item_mine_organization_teacher, dataEntity.getReal_name());

        ImageView iv_item_mine_organization_eye = holder.getView(R.id.iv_item_mine_organization_eye);
        if ("0".equals(dataEntity.getShow_teacher())) {
            iv_item_mine_organization_eye.setBackgroundResource(R.drawable.icon_closeeye);
        } else {
            iv_item_mine_organization_eye.setBackgroundResource(R.drawable.icon_openeye);
        }
        LinearLayout ll_public = holder.getView(R.id.ll_public);
        if ("2".equals(dataEntity.getOrg_auth())) {
            ll_public.setVisibility(View.VISIBLE);
        } else {
            ll_public.setVisibility(View.GONE);
        }
    }
}
