package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.CourseTimeList;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * Created by Sandy on 2016/6/21/0021.
 */
public class CourseDetailMoreAdapter extends BaseAdapter {

    private Context mContext;
    private List<CourseTimeList> mDatas;
    private boolean flag = false;
    private final int APPLY = 1;
    private final int APPLYING = 2;
    private final int FINISH = 3;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public CourseDetailMoreAdapter(Context context, List<CourseTimeList> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {

        int maxCourse = 4;
        if (flag) {
            return mDatas.size();
        } else {
            if (mDatas.size() > maxCourse) {
                return maxCourse;
            } else {
                return mDatas.size();
            }
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_course_detail_more_time, null);
        }
        TextView tv_item_course_time_count = (TextView) view.findViewById(R.id.tv_item_course_time_count);
        TextView tv_item_course_time_date = (TextView) view.findViewById(R.id.tv_item_course_time_date);
        TextView tv_item_course_time_status = (TextView) view.findViewById(R.id.tv_item_course_time_status);
        tv_item_course_time_count.setText(i + 1 + "");
        tv_item_course_time_date.setText(mDatas.get(i).getTime());
        int status = mDatas.get(i).getStatus();
        if (status != FINISH) {
            tv_item_course_time_date.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            tv_item_course_time_status.setTextColor(UIUtils.getColor(R.color.app_theme_color));
        } else {
            tv_item_course_time_date.setTextColor(UIUtils.getColor(R.color.color_gay_99));
            tv_item_course_time_status.setTextColor(UIUtils.getColor(R.color.color_gay_99));
        }
        String str_status = null;
        switch (status) {
            case APPLY:
                str_status = "可报名";
                break;
            case APPLYING:
                str_status = "进行中";
                break;
            case FINISH:
                str_status = "已结束";
                break;
            default:
                break;
        }
        tv_item_course_time_status.setText(str_status);
        return view;
    }
}
