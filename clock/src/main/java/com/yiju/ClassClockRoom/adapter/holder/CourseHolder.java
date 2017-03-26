package com.yiju.ClassClockRoom.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.CourseDetailActivity;
import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * Created by Sandy on 2016/6/14/0014.
 */
public class CourseHolder extends BaseHolder<CourseInfo> {

    private ImageView iv_item_course;
    private TextView tv_item_course_type;
    private TextView tv_item_course_people;
    private TextView tv_item_course_address;
    private TextView tv_item_course_price;
    private TextView tv_item_course_reservation;

    public CourseHolder(Context context) {
        super(context);
    }

    @Override
    protected View initView(Context context) {
        View view = View.inflate(context, R.layout.item_course_list, null);
        iv_item_course = (ImageView) view.findViewById(R.id.iv_course_pic);
        tv_item_course_type = (TextView) view.findViewById(R.id.tv_item_course_type);
        tv_item_course_people = (TextView) view.findViewById(R.id.tv_item_course_people);
        tv_item_course_address = (TextView) view.findViewById(R.id.tv_item_course_address);
        tv_item_course_price = (TextView) view.findViewById(R.id.tv_item_course_price);
        tv_item_course_reservation = (TextView) view.findViewById(R.id.tv_item_course_reservation);
        return view;
    }

    @Override
    protected void refreshView() {

        final CourseInfo info = getData();
        if(null != info){
            if(null != info.getPic()&&!"".equals(info.getPic())){
                Glide.with(UIUtils.getContext()).load(info.getPic()).into(iv_item_course);
            }
            tv_item_course_type.setText(info.getName());
            String s;
            String teacher_name = info.getReal_name();
            String org_name = info.getOrg_name();
            if(null != org_name && !"".equals(org_name)){
                s = teacher_name+"/"+org_name;
            }else {
                s = teacher_name;
            }
            tv_item_course_people.setText(s);
            tv_item_course_address.setText(info.getSchool_name());
            tv_item_course_price.setText(String.format(UIUtils.getString(R.string.format_single_price), info.getSingle_price()));
            tv_item_course_reservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UIUtils.getContext(), CourseDetailActivity.class);
                    intent.putExtra("id",info.getId());
                    UIUtils.startActivity(intent);
                }
            });
        }
    }
}
