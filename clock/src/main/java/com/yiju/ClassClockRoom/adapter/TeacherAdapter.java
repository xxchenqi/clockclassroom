package com.yiju.ClassClockRoom.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.SpecialTeacherInfo;
import com.yiju.ClassClockRoom.bean.TeacherInfoBean;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;


public class TeacherAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_COUNT = 3;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;

    private LayoutInflater inflater;
    private List<Object> objects;

    public TeacherAdapter(List<Object> objectList) {
        this.inflater = LayoutInflater.from(UIUtils.getContext());
        this.objects = objectList;
    }

    @Override
    public int getCount() {
        return objects.size();
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
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        Object o = objects.get(position);
        if (o instanceof String) {
            return TYPE_1;
        } else if (o instanceof SpecialTeacherInfo) {
            return TYPE_2;
        } else if (o instanceof TeacherInfoBean) {
            return TYPE_3;
        }
        return TYPE_1;
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
                    convertView = inflater.inflate(R.layout.item_teacher_recommend, parent, false);
                    holder3 = new ViewHolderThree();
                    holder3.iv_item_teacher_pic = (ImageView) convertView.findViewById(R.id.iv_item_teacher_pic);
                    holder3.iv_teacher_sex = (ImageView) convertView.findViewById(R.id.iv_supplier_teacher_sex);
                    holder3.tv_teacher_name = (TextView) convertView.findViewById(R.id.tv_teacher_name);
                    holder3.tv_tags = (TextView) convertView.findViewById(R.id.tv_tags);
                    holder3.tv_org_name = (TextView) convertView.findViewById(R.id.tv_org_name);
                    holder3.tv_course_info = (TextView) convertView.findViewById(R.id.tv_course_info);
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
        } else if (o instanceof SpecialTeacherInfo && holder2 != null) {
            //专题
            SpecialTeacherInfo specialTecherInfo = (SpecialTeacherInfo) o;
            String pic = specialTecherInfo.getPic();
            if (StringUtils.isNotNullString(pic)) {
                Glide.with(UIUtils.getContext()).load(pic).into(holder2.imageView);
            }
        } else if (o instanceof TeacherInfoBean && holder3 != null) {
            TeacherInfoBean teacherInfoBean = (TeacherInfoBean) o;

            Glide.with(UIUtils.getContext()).load(teacherInfoBean.getAvatar()).into(holder3.iv_item_teacher_pic);
            if (teacherInfoBean.getName() != null && !"".equals(teacherInfoBean.getName())) {
                holder3.tv_teacher_name.setText(teacherInfoBean.getName());
            } else {
                holder3.tv_teacher_name.setText(R.string.txt_no_data);
            }
            if(StringUtils.isNullString(teacherInfoBean.getTags())){
                holder3.tv_tags.setVisibility(View.GONE);
            }else {
                if ("".equals(teacherInfoBean.getTags().split(",")[0])) {
                    holder3.tv_tags.setVisibility(View.GONE);
                } else {
                    holder3.tv_tags.setVisibility(View.VISIBLE);
                    holder3.tv_tags.setText(teacherInfoBean.getTags().split(",")[0]);
                }
            }
            if (!"".equals(teacherInfoBean.getOrg_name())) {
                holder3.tv_org_name.setText(
                        String.format(
                                UIUtils.getString(R.string.txt_subsidiary_organ),
                                teacherInfoBean.getOrg_name()
                        ));
            } else {
                holder3.tv_org_name.setText(UIUtils.getString(R.string.txt_personal_teacher));
            }
            if (StringUtils.isNotNullString(teacherInfoBean.getCourse_info())) {
                holder3.tv_course_info.setVisibility(View.VISIBLE);
                holder3.tv_course_info.setText(
                        String.format(
                                UIUtils.getString(R.string.txt_open_course_info),
                                teacherInfoBean.getCourse_info()
                        ));
            } else {
                holder3.tv_course_info.setVisibility(View.GONE);
                holder3.tv_course_info.setText(
                        String.format(
                                UIUtils.getString(R.string.txt_open_course_info),
                                UIUtils.getString(R.string.txt_no_data)
                        ));
            }

            if ("0".equals(teacherInfoBean.getSex())) {
                holder3.iv_teacher_sex.setVisibility(View.GONE);
            } else {
                if ("1".equals(teacherInfoBean.getSex())) {                             //设置老师性别
                    holder3.iv_teacher_sex.setImageResource(R.drawable.male);
                } else if ("2".equals(teacherInfoBean.getSex())) {
                    holder3.iv_teacher_sex.setImageResource(R.drawable.female);
                }
                holder3.iv_teacher_sex.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    class ViewHolderOne {
        public TextView tv_type;
        public TextView tv_more;
        public View v_divider;
    }

    class ViewHolderTwo {
        public ImageView imageView;
    }

    class ViewHolderThree {
        public ImageView iv_item_teacher_pic;
        public ImageView iv_teacher_sex;
        public TextView tv_teacher_name;
        public TextView tv_tags;
        public TextView tv_org_name;
        public TextView tv_course_info;
    }

}
