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
import com.yiju.ClassClockRoom.bean.SchoolTeacherListResult;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者:wh
 * <p/>
 * 时间: on 2016/7/6 16:53
 * ----------------------------------------
 */
public class CourseTeacherAdapter extends CommonBaseAdapter<SchoolTeacherListResult.SchoolTeacherBean.ListEntity> {

    public CourseTeacherAdapter(
            Context context,
            List<SchoolTeacherListResult.SchoolTeacherBean.ListEntity> datas,
            int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(final ViewHolder holder, SchoolTeacherListResult.SchoolTeacherBean.ListEntity dataEntity) {
        ViewHolder.setFlag(true);
        RelativeLayout rl_course_teacher_root = holder.getView(R.id.rl_course_teacher_root);
        ImageView iv_item_course_teacher_avatar = holder.getView(R.id.iv_item_course_teacher_avatar);
        TextView tv_item_course_teacher_name = holder.getView(R.id.tv_item_course_teacher_name);
        LinearLayout ll_public = holder.getView(R.id.ll_public);
        ImageView iv_item_mine_organization_eye = holder.getView(R.id.iv_item_mine_organization_eye);
        ImageView iv_select_teacher = holder.getView(R.id.iv_select_teacher);
        TextView tv_teacher_data = holder.getView(R.id.tv_teacher_data);

        if (dataEntity == null) {
            return;
        }
        if (StringUtils.isNotNullString(dataEntity.getAvatar())) {
            Glide.with(mContext).load(dataEntity.getAvatar()).into(iv_item_course_teacher_avatar);
        }
        tv_item_course_teacher_name.setText(dataEntity.getReal_name());
        if ("0".equals(dataEntity.getShow_teacher())) {
            iv_item_mine_organization_eye.setBackgroundResource(R.drawable.icon_closeeye);
        } else {
            iv_item_mine_organization_eye.setBackgroundResource(R.drawable.icon_openeye);
        }
        if ("2".equals(dataEntity.getOrg_auth())) {
            ll_public.setVisibility(View.VISIBLE);
        } else {
            ll_public.setVisibility(View.GONE);
        }
        if ("0".equals(dataEntity.getFullteacherinfo())) {//0：不全 1：全
            tv_teacher_data.setVisibility(View.VISIBLE);
            rl_course_teacher_root.setBackgroundColor(UIUtils.getColor(R.color.color_gay_ee));
        } else {
            rl_course_teacher_root.setBackgroundColor(UIUtils.getColor(R.color.white));
            tv_teacher_data.setVisibility(View.GONE);
        }
        if (dataEntity.isCheck()) {
            iv_select_teacher.setBackgroundResource(R.drawable.order_choose_btn);
        }
    }
}
