package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.CourseDetailActivity;
import com.yiju.ClassClockRoom.act.IndexDetailActivity;
import com.yiju.ClassClockRoom.act.ReservationActivity;
import com.yiju.ClassClockRoom.act.TeacherDetailActivity;
import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Room;
import com.yiju.ClassClockRoom.bean.RoomTypeInfo;
import com.yiju.ClassClockRoom.bean.TeacherInfoBean;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by wh on 2016/3/4.
 */
public class NewIndexTypeAdapter extends BaseAdapter {
    //默认展示两条课室数据
    public static final int DEFAULT_SHOW_COUNT = 2;

    //各个类型
    private static final int VIEW_TYPE_COUNT = 5;
    private static final int TYPE_1 = 0;
    private static final int TYPE_1_1 = 1;
    private static final int TYPE_2 = 2;
    private static final int TYPE_3 = 3;
    private static final int TYPE_4 = 4;

    private Context mContext;
    private List<RoomTypeInfo> showRoom_types = new ArrayList<>();
    private List<RoomTypeInfo> allRoom_types;
    private Room room;
    private Order2 orderDataEntity;

    private List<Object> objects;

    protected boolean shrink = true;//标识课室更多/收起状态

    private List<CourseInfo> course_recommend;
    private List<TeacherInfoBean> teacher_recommend;

    public NewIndexTypeAdapter(Context context, Room room, Order2 info,
                               List<CourseInfo> course_recommendList,
                               List<TeacherInfoBean> teacher_recommendList) {
        this.mContext = context;
        this.room = room;
        this.orderDataEntity = info;
        this.course_recommend = course_recommendList;
        this.teacher_recommend = teacher_recommendList;
        objects = new ArrayList<>();
        this.allRoom_types = room.getRoom_type();
        if (allRoom_types != null) {
            if (allRoom_types.size() <= DEFAULT_SHOW_COUNT) {
                showRoom_types.addAll(allRoom_types);
            } else {
                for (int i = 0; i < DEFAULT_SHOW_COUNT; i++) {
                    showRoom_types.add(allRoom_types.get(i));
                }
            }
        }
        objects.addAll(showRoom_types);
        if (allRoom_types.size() > DEFAULT_SHOW_COUNT) {
            objects.add(mContext.getString(R.string.course_detial_more_intro));
        }
        addData(objects, course_recommend, teacher_recommend);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        Object o = objects.get(position);
        if (o instanceof String) {
            if (mContext.getString(R.string.course_detial_more_intro).equals(o.toString())
                    || mContext.getString(R.string.txt_shrink).equals(o.toString())) {
                return TYPE_1;
            } else {
                return TYPE_1_1;
            }
        } else if (o instanceof RoomTypeInfo) {//课室
            return TYPE_2;
        } else if (o instanceof CourseInfo) {//课程
            return TYPE_3;
        } else if (o instanceof TeacherInfoBean) {//老师
            return TYPE_4;
        }
        return TYPE_1;
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
        ViewHolderOne1 holder1_1 = null;
        ViewHolderTwo holder2 = null;
        ViewHolderThree holder3 = null;
        ViewHolderFour holder4 = null;
        convertView = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    convertView = View.inflate(mContext, R.layout.item_room_child_type_new_foot, null);
                    holder1 = new ViewHolderOne();
                    holder1.ll_more_view = (LinearLayout) convertView.findViewById(R.id.ll_more_view);
                    holder1.tv_open_close = (TextView) convertView.findViewById(R.id.tv_open_close);
                    holder1.iv_up_down = (ImageView) convertView.findViewById(R.id.iv_up_down);
                    convertView.setTag(holder1);
                    break;
                case TYPE_1_1:
                    convertView = View.inflate(mContext, R.layout.item_index_text, null);
                    holder1_1 = new ViewHolderOne1();
                    holder1_1.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                    convertView.setTag(holder1_1);
                    break;
                case TYPE_2://课室
                    holder2 = new ViewHolderTwo();
                    convertView = View.inflate(mContext, R.layout.item_room_child_type_new, null);
                    holder2.iv_item_room_pic = (ImageView)
                            convertView.findViewById(R.id.iv_item_room_pic);
                    holder2.tv_class_tag = (TextView)
                            convertView.findViewById(R.id.tv_class_tag);
                    holder2.tv_desc = (TextView)
                            convertView.findViewById(R.id.tv_desc);
                    holder2.tv_day_price = (TextView)
                            convertView.findViewById(R.id.tv_day_price);
                    holder2.tv_week_price = (TextView)
                            convertView.findViewById(R.id.tv_week_price);
                    holder2.tv_reserve = (TextView)
                            convertView.findViewById(R.id.tv_reserve);
                    holder2.tv_corner_select_icon = (TextView)
                            convertView.findViewById(R.id.tv_corner_select_icon);
                    convertView.setTag(holder2);
                    break;
                case TYPE_3:// 课程
                    holder3 = new ViewHolderThree();
                    convertView = View.inflate(mContext, R.layout.item_course_list, null);
                    holder3.rl_course_root = (RelativeLayout) convertView.findViewById(R.id.rl_course_root);
                    holder3.iv_course_pic = (ImageView) convertView.findViewById(R.id.iv_course_pic);
                    holder3.tv_item_course_type = (TextView) convertView.findViewById(R.id.tv_item_course_type);
                    holder3.tv_item_course_people = (TextView) convertView.findViewById(R.id.tv_item_course_people);
                    holder3.tv_item_course_address = (TextView) convertView.findViewById(R.id.tv_item_course_address);
                    holder3.tv_item_course_price = (TextView) convertView.findViewById(R.id.tv_item_course_price);
                    holder3.tv_item_course_reservation = (TextView) convertView.findViewById(R.id.tv_item_course_reservation);
                    convertView.setTag(holder3);
                    break;
                case TYPE_4://老师
                    holder4 = new ViewHolderFour();
                    convertView = View.inflate(mContext, R.layout.item_index_teacher_layout, null);
                    holder4.ll_recommend_teacher = (LinearLayout) convertView
                            .findViewById(R.id.ll_recommend_teacher);
                    convertView.setTag(holder4);
                    break;
            }
        }

        final Object o = objects.get(position);
        if (o instanceof String) {
            if (type == TYPE_1) {
                holder1.tv_open_close.setText(o.toString());
                holder1.ll_more_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeDataShow();
                    }
                });
                if (UIUtils.getString(R.string.txt_shrink).equals(o.toString())) {
                    holder1.iv_up_down.setBackgroundResource(R.drawable.up);
                } else {
                    holder1.iv_up_down.setBackgroundResource(R.drawable.down);
                }
            } else if (type == TYPE_1_1) {
                holder1_1.tv_type.setText(o.toString());
            }
        } else if (o instanceof RoomTypeInfo) {//课室
            final RoomTypeInfo info = (RoomTypeInfo) o;
            String typeId = "";// 已选中typeId
            if (holder2 != null) {
                if (orderDataEntity != null) {
                    typeId = orderDataEntity.getType_id();
                    if (typeId.equals(info.getId())) {
                        holder2.tv_corner_select_icon.setVisibility(View.VISIBLE);
                    } else {
                        holder2.tv_corner_select_icon.setVisibility(View.GONE);
                    }
                }

                String picSmall = info.getPic_small();
                if (StringUtils.isNotNullString(picSmall)) {
                    Glide.with(UIUtils.getContext()).load(picSmall).into(holder2.iv_item_room_pic);
                }
                holder2.tv_class_tag.setText(info.getDesc());
                if (StringUtils.isNotNullString(info.getDesc_app())) {
                    holder2.tv_desc.setText("(" + info.getDesc_app() + ")");
                    holder2.tv_desc.setVisibility(View.VISIBLE);
                } else {

                    holder2.tv_desc.setVisibility(View.GONE);
                }
                String dayP = info.getPrice_weekday();
                String wendP = info.getPrice_weekend();
                holder2.tv_day_price.setText(
                        String.format(
                                UIUtils.getString(R.string.rmb_how_much),
                                dayP.substring(0, dayP.indexOf(".")))
                );
                holder2.tv_week_price.setText(
                        String.format(
                                UIUtils.getString(R.string.rmb_how_much),
                                wendP.substring(0, wendP.indexOf(".")))
                );
                // 课室图片点击
                holder2.iv_item_room_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, IndexDetailActivity.class);
                        intent.putExtra("sid", room.getId());
                        intent.putExtra("sname", room.getName());
                        intent.putExtra("type_desc", info.getDesc());
                        intent.putExtra("typeid", info.getId());
                        intent.putExtra("room_start_time", room.getStart_time());
                        intent.putExtra("room_end_time", room.getEnd_time());
                        intent.putExtra("room_name", info.getDesc());
                        intent.putExtra("can_schedule", room.getCan_schedule());
                        intent.putExtra("instruction", room.getInstruction());
                        intent.putExtra("confirm_type", room.getConfirm_type());
                        BaseApplication.getmForegroundActivity().startActivity(intent);
                    }
                });
                //点击预订按钮
                final String finalTypeId = typeId;
                holder2.tv_reserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ReservationActivity.class);
                        if (orderDataEntity != null) {
                            if (finalTypeId.equals(info.getId())) {
                                intent.putExtra("info", orderDataEntity);
                            }
                        }
                        intent.putExtra("sid", room.getId());
                        intent.putExtra("name", room.getName());//name: "测试徐汇虹梅商务大厦旗舰店",
                        intent.putExtra("type_id", info.getId());
                        intent.putExtra("room_start_time", room.getStart_time());
                        intent.putExtra("room_end_time", room.getEnd_time());
                        intent.putExtra("room_name", info.getDesc());//desc: "大间课室(有窗)",
                        intent.putExtra("instruction", room.getInstruction());
                        intent.putExtra("confirm_type", room.getConfirm_type());
                        BaseApplication.getmForegroundActivity().startActivity(intent);
                    }
                });

                //can_schedule判断 =1是可预订 =0是不可预订
                if ("1".equals(room.getCan_schedule())) {
                    holder2.tv_reserve.setText(R.string.reserve_space);
                    holder2.tv_reserve.setBackgroundResource(R.drawable.background_green_1eb482_radius_3);
                    holder2.tv_reserve.setClickable(true);
                } else {
                    holder2.tv_reserve.setText(R.string.coming_reserve_space);
                    holder2.tv_reserve.setBackgroundResource(R.drawable.background_gray_cccccc_radius_3);
                    holder2.tv_reserve.setClickable(false);
                }

            }
        } else if (o instanceof CourseInfo) {//课程
            final CourseInfo courseInfo = (CourseInfo) o;
            String pic = courseInfo.getPic();
            if (holder3 != null) {
                if (StringUtils.isNotNullString(pic)) {
                    Glide.with(UIUtils.getContext()).load(pic).into(holder3.iv_course_pic);
                }
                holder3.tv_item_course_type.setText(courseInfo.getName());
                if (StringUtils.isNotNullString(courseInfo.getReal_name())) {
                    holder3.tv_item_course_people.setText(courseInfo.getReal_name());
                }
                if (StringUtils.isNotNullString(courseInfo.getSchool_name())) {
                    holder3.tv_item_course_address.setText(courseInfo.getSchool_name());
                }
                holder3.tv_item_course_price.setText(
                        String.format(
                                UIUtils.getString(R.string.txt_format_price),
                                courseInfo.getSingle_price()
                        ));
                holder3.tv_item_course_reservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 去订课
//                        UIUtils.showToastSafe("去订课");
                    }
                });
                holder3.rl_course_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CourseDetailActivity.class);
                        intent.putExtra("COURSE_ID", courseInfo.getId());
                        BaseApplication.getmForegroundActivity().startActivity(intent);
                    }
                });
            }
        } else if (o instanceof TeacherInfoBean) {//老师
            int count;                            //放置老师个数(注：如果屏幕大于等于720则放置3个老师，反之放置2个)
            int screenWidth = CommonUtil.getScreenWidth();
            List<TeacherInfoBean> teacherInfoBeans = teacher_recommend;
            if (teacherInfoBeans != null && teacherInfoBeans.size() > 0) {
                if (screenWidth >= 720 && teacherInfoBeans.size() >= 3) {
                    count = 3;
                } else {
                    count = 2;
                }
                for (int i = 0; i < teacherInfoBeans.size(); i++) {
                    if (holder4 != null && i < count) {
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.item_recommend_teacher_child, parent, false);
                        ImageView iv_item_teacher_pic = (ImageView) view
                                .findViewById(R.id.iv_item_teacher_pic);
                        TextView tv_teacher_name = (TextView) view
                                .findViewById(R.id.tv_teacher_name);
                        TextView tv_teacher_type = (TextView) view
                                .findViewById(R.id.tv_teacher_type);
                        TextView tv_course_info = (TextView) view
                                .findViewById(R.id.tv_course_info);

                        final TeacherInfoBean teacherInfoBean = teacherInfoBeans.get(i);
                        String pic = teacherInfoBean.getAvatar();

                        if (StringUtils.isNotNullString(pic)) {
                            pic = pic.replaceAll("\\d{1,3}X\\d{1,3}", "350X350");
                            Glide.with(UIUtils.getContext()).load(pic).into(iv_item_teacher_pic);
                        }

                        tv_teacher_name.setText(teacherInfoBean.getName());

                        if (StringUtils.isNotNullString(teacherInfoBean.getOrg_name())) {
                            tv_teacher_type.setText(
                                    String.format(
                                            UIUtils.getString(R.string.txt_subsidiary_organ),
                                            teacherInfoBean.getOrg_name()
                                    ));
                        } else {
                            tv_teacher_type.setText(UIUtils.getString(R.string.txt_personal_teacher));
                        }
                        if (StringUtils.isNullString(teacherInfoBean.getCourse_info())) {
                            tv_course_info.setVisibility(View.GONE);
                        } else {
                            tv_course_info.setVisibility(View.VISIBLE);
                            tv_course_info.setText(
                                    String.format(
                                            UIUtils.getString(R.string.txt_open_course),
                                            teacherInfoBean.getCourse_info()
                                    ));
                        }

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, TeacherDetailActivity.class);
                                intent.putExtra("id", teacherInfoBean.getId());
                                BaseApplication.getmForegroundActivity().startActivity(intent);
                            }
                        });
                        LinearLayout.LayoutParams linearLayout =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);

                        int spacingWidth = (int) mContext.getResources().getDimension(R.dimen.DIMEN_12DP);
                        int childWidth = (screenWidth - spacingWidth * (count + 1)) / count;
                        linearLayout.width = childWidth;
                        linearLayout.height = childWidth
                                + (int) mContext.getResources().getDimension(R.dimen.DIMEN_62DP);
                        if (i < count - 1) {
                            linearLayout.rightMargin = spacingWidth;
                        }

                        holder4.ll_recommend_teacher.addView(view, linearLayout);
                    }
                }
            }
        }
        return convertView;
    }

    private void changeDataShow() {
        if (objects == null) {
            objects = new ArrayList<>();
        } else {
            objects.clear();
        }
        if (shrink) {
            shrink = false;
            if (allRoom_types != null) {
                objects.addAll(allRoom_types);
            }
            objects.add(mContext.getString(R.string.txt_shrink));
        } else {
            shrink = true;
            if (showRoom_types == null) {
                showRoom_types = new ArrayList<>();
            } else {
                showRoom_types.clear();
            }
            for (int i = 0; i < DEFAULT_SHOW_COUNT; i++) {
                showRoom_types.add(allRoom_types.get(i));
            }
            objects.addAll(showRoom_types);
            objects.add(mContext.getString(R.string.course_detial_more_intro));
        }
        addData(objects, course_recommend, teacher_recommend);
        notifyDataSetChanged();
    }

    private void addData(List<Object> objects,
                         List<CourseInfo> course_recommend,
                         List<TeacherInfoBean> teacher_recommend) {
        if (objects == null) {
            return;
        }
        if (course_recommend != null && course_recommend.size() > 0) {
            objects.add(mContext.getString(R.string.txt_recommend_course));
            objects.addAll(course_recommend);
        }
        if (teacher_recommend != null && teacher_recommend.size() > 0) {
            objects.add(mContext.getString(R.string.txt_recommend_teacher));
            objects.add(teacher_recommend.get(0));
        }
    }

    private class ViewHolderOne {
        private LinearLayout ll_more_view;
        private TextView tv_open_close;
        private ImageView iv_up_down;
    }

    private class ViewHolderOne1 {
        private TextView tv_type;
    }

    private class ViewHolderTwo {
        private ImageView iv_item_room_pic;
        private TextView tv_class_tag;
        private TextView tv_desc;
        private TextView tv_day_price;
        private TextView tv_week_price;
        private TextView tv_reserve;
        private TextView tv_corner_select_icon;
    }

    private class ViewHolderThree {
        private RelativeLayout rl_course_root;
        private ImageView iv_course_pic;
        private TextView tv_item_course_type;
        private TextView tv_item_course_people;
        private TextView tv_item_course_address;
        private TextView tv_item_course_price;
        private TextView tv_item_course_reservation;
    }

    private class ViewHolderFour {
        private LinearLayout ll_recommend_teacher;
    }
}
