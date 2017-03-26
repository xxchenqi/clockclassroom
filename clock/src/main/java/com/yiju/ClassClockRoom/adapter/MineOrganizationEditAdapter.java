package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.MemberBean;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
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
public class MineOrganizationEditAdapter extends CommonBaseAdapter<MemberBean.DataEntity> {

    private ListItemClickHelp l;

    public MineOrganizationEditAdapter(
            Context context,
            List<MemberBean.DataEntity> datas,
            int layoutId,
            ListItemClickHelp listItemClickHelp) {
        super(context, datas, layoutId);
        this.l = listItemClickHelp;
    }

    @Override
    protected void convert(final ViewHolder holder, MemberBean.DataEntity dataEntity) {
        ViewHolder.setFlag(true);
        RelativeLayout rl_select_teacher = holder.getView(R.id.rl_select_teacher);
        ImageView iv_select_teacher = holder.getView(R.id.iv_select_teacher);
        ImageView iv_item_mine_organization = holder.getView(R.id.iv_item_mine_organization);
        TextView tv_item_mine_organization_teacher = holder.getView(R.id.tv_item_mine_organization_teacher);
        ImageView iv_item_mine_organization_eye = holder.getView(R.id.iv_item_mine_organization_eye);
        LinearLayout ll_public = holder.getView(R.id.ll_public);
        TextView tv_teacher_data = holder.getView(R.id.tv_teacher_data);

        if (dataEntity == null) {
            return;
        }
        if (StringUtils.isNotNullString(dataEntity.getAvatar())) {
            Glide.with(mContext).load(dataEntity.getAvatar()).into(iv_item_mine_organization);
        }
        tv_item_mine_organization_teacher.setText(dataEntity.getReal_name());

        if ("0".equals(dataEntity.getShow_teacher())) {
            iv_item_mine_organization_eye.setBackgroundResource(R.drawable.icon_closeeye);
        } else {
            iv_item_mine_organization_eye.setBackgroundResource(R.drawable.icon_openeye);
        }
        if ("0".equals(dataEntity.getFullteacherinfo())) {//0：不全 1：全
            tv_teacher_data.setVisibility(View.VISIBLE);
        } else {
            tv_teacher_data.setVisibility(View.GONE);
        }
        if ("2".equals(dataEntity.getOrg_auth())) {
            ll_public.setVisibility(View.VISIBLE);
            iv_select_teacher.setBackgroundResource(R.drawable.order_choose_btn);
        } else {
            ll_public.setVisibility(View.GONE);
            iv_select_teacher.setBackgroundResource(R.drawable.order_nonechoose_btn);
        }

        rl_select_teacher.setVisibility(View.VISIBLE);
        if (dataEntity.isCheck()) {
            iv_select_teacher.setBackgroundResource(R.drawable.order_choose_btn);
        } else {
            iv_select_teacher.setBackgroundResource(R.drawable.order_nonechoose_btn);
        }
        rl_select_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onClickItem(holder.getPosition());
            }
        });
    }
}
