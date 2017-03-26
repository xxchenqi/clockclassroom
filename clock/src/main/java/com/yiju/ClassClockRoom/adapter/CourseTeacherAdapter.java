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
            Glide.with(mContext).load(dataEntity.getAvatar()).placeholder(R.drawable.user_unload).into(iv_item_course_teacher_avatar);
        }
        tv_item_course_teacher_name.setText(dataEntity.getReal_name());
        if ("2".equals(dataEntity.getOrg_auth())) {
            ll_public.setVisibility(View.VISIBLE);
        } else {
            ll_public.setVisibility(View.GONE);
        }

        if (!"2".equals(dataEntity.getIs_verify())) {
            iv_select_teacher.setVisibility(View.INVISIBLE);
            rl_course_teacher_root.setBackgroundColor(UIUtils.getColor(R.color.color_gay_ee));
            if ("0".equals(dataEntity.getShow_teacher())) {
                iv_item_mine_organization_eye.setBackgroundResource(R.drawable.hide_gray);
            }
            if ("0".equals(dataEntity.getFullteacherinfo())) {//0：不全 1：全
                tv_teacher_data.setText(R.string.txt_data_for_completion_teacher);
            }
            //-1新建 1=待(未)审核 2=审核通过 0=审核未通过
            if ("1".equals(dataEntity.getIs_verify())) {
                tv_teacher_data.setText(R.string.person_course_status_wait_check);
            }else if ("0".equals(dataEntity.getIs_verify())) {
                tv_teacher_data.setText(R.string.person_course_status_fail_check);
            }
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.color_gay_99));
        } else {
            iv_select_teacher.setVisibility(View.VISIBLE);
            rl_course_teacher_root.setBackgroundColor(UIUtils.getColor(R.color.white));
            tv_teacher_data.setText(R.string.txt_approve);
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            if ("0".equals(dataEntity.getShow_teacher())) {
                iv_item_mine_organization_eye.setBackgroundResource(R.drawable.hide);
            }
            if (dataEntity.isCheck()) {
                iv_select_teacher.setBackgroundResource(R.drawable.check_icon);
            }
        }
        //-1新建 1=待(未)审核 2=审核通过 0=审核未通过
        /*if ("1".equals(dataEntity.getIs_verify())) {
            tv_teacher_data.setText(R.string.person_course_status_wait_check);
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.color_black_33));
        } else if ("2".equals(dataEntity.getIs_verify())) {
            tv_teacher_data.setText(R.string.txt_approve);
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.app_theme_color));
        } else if ("0".equals(dataEntity.getIs_verify())) {
            tv_teacher_data.setText(R.string.person_course_status_fail_check);
            tv_teacher_data.setTextColor(UIUtils.getColor(R.color.color_gay_99));
        }*/


    }
}
