package com.yiju.ClassClockRoom.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.bean.SpecialInfo;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:课程适配器
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/6/17 17:14
 * ----------------------------------------
 */
public class CourseAdapter extends BaseAdapter {
    //各个类型
    private static final int VIEW_TYPE_COUNT = 3;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;
    //布局
    private LayoutInflater inflater;
    //数据源
    private List<Object> objects;

    public CourseAdapter(List<Object> objectList) {
        this.inflater = LayoutInflater.from(UIUtils.getContext());
        this.objects = objectList;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = objects.get(position);
        if (o instanceof String) {
            return TYPE_1;
        } else if (o instanceof SpecialInfo) {
            return TYPE_2;
        } else if (o instanceof CourseInfo) {
            return TYPE_3;
        }
        return TYPE_1;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderOne holder1 = null;
        ViewHolderTwo holder2 = null;
        ViewHolderThree holder3 = null;
        int type = getItemViewType(position);
        //无convertView，需要new出各个控件
        if (convertView == null) {
            //按当前所需的样式，确定new的布局
            switch (type) {
                case TYPE_1:
                    convertView = inflater.inflate(R.layout.item_text, parent, false);
                    holder1 = new ViewHolderOne();
                    holder1.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                    holder1.tv_more = (TextView) convertView.findViewById(R.id.tv_more);
                    holder1.v_divider = convertView.findViewById(R.id.v_divider);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView = inflater.inflate(R.layout.item_image, parent, false);
                    holder2 = new ViewHolderTwo();
                    holder2.imageView = (ImageView) convertView.findViewById(R.id.iv_course_pic);
                    convertView.setTag(holder2);
                    break;
                case TYPE_3:
                    convertView = inflater.inflate(R.layout.item_course_list, parent, false);
                    holder3 = new ViewHolderThree();
                    holder3.iv_course_pic = (ImageView) convertView.findViewById(R.id.iv_course_pic);
                    holder3.tv_item_course_type = (TextView) convertView.findViewById(R.id.tv_item_course_type);
                    holder3.tv_item_course_people = (TextView) convertView.findViewById(R.id.tv_item_course_people);
                    holder3.tv_item_course_address = (TextView) convertView.findViewById(R.id.tv_item_course_address);
                    holder3.tv_item_course_price = (TextView) convertView.findViewById(R.id.tv_item_course_price);
                    holder3.tv_item_course_reservation = (TextView) convertView.findViewById(R.id.tv_item_course_reservation);
                    convertView.setTag(holder3);
                    break;
            }
        } else {
            //有convertView，按样式，取得不用的布局
            switch (type) {
                case TYPE_1:
                    holder1 = (ViewHolderOne) convertView.getTag();
                    break;
                case TYPE_2:
                    holder2 = (ViewHolderTwo) convertView.getTag();
                    break;
                case TYPE_3:
                    holder3 = (ViewHolderThree) convertView.getTag();
                    break;
            }
        }

        Object o = objects.get(position);
        if (o instanceof String && holder1 != null) {
            if ((o.toString()).contains(UIUtils.getString(R.string.course_zhuan))) {
                holder1.tv_more.setVisibility(View.GONE);
            } else {
                holder1.tv_more.setVisibility(View.VISIBLE);
            }
            if (position != 0) {
                holder1.v_divider.setVisibility(View.VISIBLE);
            } else {
                holder1.v_divider.setVisibility(View.GONE);
            }
            holder1.tv_type.setText(o.toString());
        } else if (o instanceof SpecialInfo && holder2 != null) {
            //专题
            SpecialInfo specialInfo = (SpecialInfo) o;
            String pic = specialInfo.getPic();
            if (StringUtils.isNotNullString(pic)) {
                Glide.with(UIUtils.getContext()).load(pic).into(holder2.imageView);
            }
        } else if (o instanceof CourseInfo && holder3 != null) {
            //课程
            final CourseInfo courseInfo = (CourseInfo) o;
            String pic = courseInfo.getPic();
            Glide.with(UIUtils.getContext()).load(pic).into(holder3.iv_course_pic);
            holder3.tv_item_course_type.setText(courseInfo.getName());
            if (courseInfo.getReal_name() != null && !"".equals(courseInfo.getReal_name())) {
                holder3.tv_item_course_people.setText(courseInfo.getReal_name());
            } else {
                holder3.tv_item_course_people.setText(R.string.txt_no_data);
            }
            if (courseInfo.getSchool_name() != null && !"".equals(courseInfo.getSchool_name())) {
                holder3.tv_item_course_address.setText(courseInfo.getSchool_name());
            } else {
                holder3.tv_item_course_address.setText(R.string.txt_no_data);
            }
            holder3.tv_item_course_price.setText("¥" + courseInfo.getSingle_price() + "/");
        }
        return convertView;
    }

    private class ViewHolderOne {
        private TextView tv_type;
        private TextView tv_more;
        private View v_divider;
    }

    private class ViewHolderTwo {
        private ImageView imageView;
    }

    private class ViewHolderThree {
        private ImageView iv_course_pic;
        private TextView tv_item_course_type;
        private TextView tv_item_course_people;
        private TextView tv_item_course_address;
        private TextView tv_item_course_price;
        private TextView tv_item_course_reservation;
    }
}
