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
import com.yiju.ClassClockRoom.util.UIUtils;

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
            iv_item_mine_organization_eye.setBackgroundResource(R.drawable.hide);
        } else {
            iv_item_mine_organization_eye.setBackgroundResource(0);
        }
        if ("0".equals(dataEntity.getFullteacherinfo())) {//0：不全 1：全
            tv_teacher_data.setText(R.string.txt_data_for_completion_teacher);
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.color_orange_fa));
        }
        //-1新建 1=待(未)审核 2=审核通过 0=审核未通过
        if ("1".equals(dataEntity.getIs_verify())) {
            tv_teacher_data.setText(R.string.person_course_status_wait_check);
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.color_black_33));
        } else if ("2".equals(dataEntity.getIs_verify())) {
            tv_teacher_data.setText(R.string.txt_approve);
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.app_theme_color));
        } else if ("0".equals(dataEntity.getIs_verify())) {
            tv_teacher_data.setText(R.string.person_course_status_fail_check);
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.color_red_ff));
        }
        if ("2".equals(dataEntity.getOrg_auth())) {
            ll_public.setVisibility(View.VISIBLE);
            iv_select_teacher.setBackgroundResource(R.drawable.check);
        } else {
            ll_public.setVisibility(View.GONE);
            iv_select_teacher.setBackgroundResource(R.drawable.uncheck);
        }

        rl_select_teacher.setVisibility(View.VISIBLE);
        if (dataEntity.isCheck()) {
            iv_select_teacher.setBackgroundResource(R.drawable.check);
        } else {
            iv_select_teacher.setBackgroundResource(R.drawable.uncheck);
        }
        rl_select_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onClickItem(holder.getPosition());
            }
        });
    }
}
