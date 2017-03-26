package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.Course_DataInfo;
import com.yiju.ClassClockRoom.common.callback.ListItemClickListener;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * Created by Sandy on 2016/6/16/0016.
 */
public class CourseMineAdapter extends BaseAdapter {

    private List<Course_DataInfo> mLists;
    private Context mContext;
    private Course_DataInfo info;
    private final String WAIT_CHECK = "1";
    private final String FAIL_CHECK = "2";
    private final String CANCEL = "3";
    private final String FINISH = "4";
    private final String APPLYING = "5";
    private final String APPLY_FINISH = "6";
    private final int COURSE_DELETE = 1;
    private final int COURSE_EDIT = 2;
    private final int COURSE_FINISH = 3;
    private ListItemClickListener mListener;
    ViewHolder holder;

    public CourseMineAdapter(Context context, List<Course_DataInfo> lists,ListItemClickListener listener) {
        this.mLists = lists;
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Course_DataInfo getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_person_course, null);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        assignViews(view);
        info = getItem(i);
        if (null != info) {
            showPage();
        }
        holder.tvItemPersonCourseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickItem(i,COURSE_DELETE);
            }
        });
        holder.tvItemPersonCourseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickItem(i,COURSE_EDIT);
            }
        });
        return view;
    }

    private void assignViews(View v) {//已报名: 5/12
        holder.ivItemPersonCourse = (ImageView) v.findViewById(R.id.iv_item_person_course);
        holder.tvItemPersonCourseAddress = (TextView) v.findViewById(R.id.tv_item_person_course_address);
        holder.tvItemPersonCourseName = (TextView) v.findViewById(R.id.tv_item_person_course_name);
        holder.tvItemPersonCourseDateTime = (TextView) v.findViewById(R.id.tv_item_person_course_date_time);
        holder.tvItemPersonCoursePrice = (TextView) v.findViewById(R.id.tv_item_person_course_price);
        holder.tvItemPersonCourseCount = (TextView) v.findViewById(R.id.tv_item_person_course_count);
        holder.tvItemPersonCourseStatus = (TextView) v.findViewById(R.id.tv_item_person_course_status);
        holder.rlItemPersonButton = (RelativeLayout) v.findViewById(R.id.rl_item_person_button);
        holder.tvItemPersonCourseDelete = (TextView) v.findViewById(R.id.tv_item_person_course_delete);
        holder.tvItemPersonCourseEdit = (TextView) v.findViewById(R.id.tv_item_person_course_edit);
    }

    class ViewHolder {
        private ImageView ivItemPersonCourse;
        private TextView tvItemPersonCourseAddress;
        private TextView tvItemPersonCourseName;
        private TextView tvItemPersonCourseDateTime;
        private TextView tvItemPersonCoursePrice;
        private TextView tvItemPersonCourseCount;
        private TextView tvItemPersonCourseStatus;
        private RelativeLayout rlItemPersonButton;
        private TextView tvItemPersonCourseDelete;
        private TextView tvItemPersonCourseEdit;
    }

    private void showPage() {

        String name = info.getName();
        String pic_url = info.getPic();
        String single_price = info.getSingle_price();
        String status = info.getCourse_status();
        if (null != pic_url && !"".equals(pic_url)) {
            Glide.with(mContext).load(pic_url).into(holder.ivItemPersonCourse);
        }else {
            holder.ivItemPersonCourse.setImageResource(R.drawable.clock_wait);
        }
        holder.tvItemPersonCourseAddress.setText(info.getSchool_name());
        holder.tvItemPersonCourseDateTime.setText(info.getDate_section());
        holder.tvItemPersonCourseCount.setText(
                String.format(
                        UIUtils.getString(R.string.multiply),
                        info.getAll_course_hour()
                ));
        holder.tvItemPersonCourseName.setText(name);
        holder.tvItemPersonCoursePrice.setText(
                String.format(
                        UIUtils.getString(R.string.string_format_money_hour),
                        single_price
                ));

        switch (status) {
            case WAIT_CHECK:
                //待审核
                holder.tvItemPersonCourseStatus.setText(UIUtils.getString(R.string.person_course_status_wait_check));
                holder.tvItemPersonCourseStatus.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                holder.rlItemPersonButton.setVisibility(View.GONE);
                break;
            case FAIL_CHECK:
                //审核未通过
                holder.tvItemPersonCourseStatus.setText(UIUtils.getString(R.string.person_course_status_fail_check));
                holder.tvItemPersonCourseStatus.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                holder.rlItemPersonButton.setVisibility(View.VISIBLE);
                break;
            case CANCEL:
                //已取消
                holder.tvItemPersonCourseStatus.setText(UIUtils.getString(R.string.person_course_status_cancel));
                holder.tvItemPersonCourseStatus.setTextColor(UIUtils.getColor(R.color.color_gay_99));
                holder.rlItemPersonButton.setVisibility(View.GONE);
                break;
            case FINISH:
                //已完结
                holder.tvItemPersonCourseStatus.setText(UIUtils.getString(R.string.person_course_status_finish));
                holder.tvItemPersonCourseStatus.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                holder.rlItemPersonButton.setVisibility(View.GONE);
                break;
            case APPLYING:
                //进行中
                holder.tvItemPersonCourseStatus.setText(UIUtils.getString(R.string.person_course_status_applying_single));
                holder.tvItemPersonCourseStatus.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                holder.rlItemPersonButton.setVisibility(View.GONE);
                break;
            /*case APPLY_FINISH:
                //进行中-完成报名
                holder.tvItemPersonCourseStatus.setText(UIUtils.getString(R.string.person_course_status_apply_finish));
                holder.rlItemPersonButton.setVisibility(View.GONE);
                break;*/
            default:
                break;
        }
    }

}
