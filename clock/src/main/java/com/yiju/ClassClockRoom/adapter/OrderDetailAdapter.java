package com.yiju.ClassClockRoom.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.ClassroomDetailActivity;
import com.yiju.ClassClockRoom.act.IndexDetailActivity;
import com.yiju.ClassClockRoom.act.StoreDetailActivity;
import com.yiju.ClassClockRoom.bean.AdjustmentData;
import com.yiju.ClassClockRoom.bean.DeviceEntity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Order3;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * --------------------------------------
 * 注释:订单详情适配器
 * 作者: cq
 * 时间: 2015-12-15 下午3:13:43
 * --------------------------------------
 */
public class OrderDetailAdapter extends BaseAdapter {
    private Activity context;
    private List<Order2> datas;
    private Integer currentStatus;
    private String current_date;
    private String coupon_id;
    private Order2 o;

    public OrderDetailAdapter(Activity context, String current_date,
                              List<Order2> datas, Integer currentStatus, String coupon_id) {
        this.context = context;
        this.current_date = current_date;
        this.datas = datas;
        this.currentStatus = currentStatus;
        this.coupon_id = coupon_id;
    }

    public void setDatas(List<Order2> datas, Integer currentStatus) {
        this.datas = datas;
        this.currentStatus = currentStatus;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        o = datas.get(position);
        convertView = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_order_detail, null);
            viewHolder.iv_order_pic = (ImageView)
                    convertView.findViewById(R.id.iv_order_pic);
            viewHolder.tv_item_detail_sname = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_sname);
            viewHolder.rl_detail = (RelativeLayout)
                    convertView.findViewById(R.id.rl_detail);
            viewHolder.tv_item_detail_room_type = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_room_type);
            /*viewHolder.tv_item_detail_use_desc = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_use_desc);*/
            viewHolder.tv_item_detail_max_member = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_max_member);
            /*viewHolder.tv_item_detail_room_count = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_room_count);*/
            viewHolder.tv_item_detail_count = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_count);
            viewHolder.tv_item_detail_date = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_date);
            viewHolder.tv_item_detail_time = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_time);
            viewHolder.tv_item_detail_repeat = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_repeat);
            /*viewHolder.tv_item_detail_fee = (TextView)
                    convertView.findViewById(R.id.tv_item_detail_fee);*/
//            viewHolder.tv_dec = (TextView)
//                    convertView.findViewById(R.id.tv_dec);
            viewHolder.ll_all_up_down = (LinearLayout)
                    convertView.findViewById(R.id.ll_all_up_down);
            viewHolder.ll_up_down = (LinearLayout)
                    convertView.findViewById(R.id.ll_up_down);
            viewHolder.iv_up_down = (ImageView)
                    convertView.findViewById(R.id.iv_up_down);
            viewHolder.ll_edit_individuation = (LinearLayout)
                    convertView.findViewById(R.id.ll_edit_individuation);
            viewHolder.tv_cancel_classroom = (TextView)
                    convertView.findViewById(R.id.tv_cancel_classroom);
            viewHolder.tv_add_classroom = (TextView)
                    convertView.findViewById(R.id.tv_add_classroom);
//            viewHolder.ll_cancel_classroom = (LinearLayout)
//                    convertView.findViewById(R.id.ll_cancel_classroom);
//            viewHolder.ll_add_classroom = (LinearLayout)
//                    convertView.findViewById(R.id.ll_add_classroom);
            viewHolder.rl_all_course = (RelativeLayout)
                    convertView.findViewById(R.id.rl_all_course);
            viewHolder.rl_course_nearly = (RelativeLayout)
                    convertView.findViewById(R.id.rl_course_nearly);
            viewHolder.tv_course_nearly = (TextView)
                    convertView.findViewById(R.id.tv_course_nearly);
//            viewHolder.tv_check_classroom_detail = (TextView)
//                    convertView.findViewById(R.id.tv_check_classroom_detail);
            viewHolder.ll_can_select = (LinearLayout)
                    convertView.findViewById(R.id.ll_can_select);
            viewHolder.ll_no_free_device_add = (LinearLayout)
                    convertView.findViewById(R.id.ll_no_free_device_add);
//            viewHolder.ll_classroom_add = (LinearLayout)
//                    convertView.findViewById(R.id.ll_classroom_add);
            /*viewHolder.rl_item_detail_subtotal = (RelativeLayout)
                    convertView.findViewById(R.id.rl_item_detail_subtotal);//小计*/
//            viewHolder.ll_set_classroom = (LinearLayout)
//                    convertView.findViewById(R.id.ll_set_classroom);
//            viewHolder.ll_set_classroom_top = (LinearLayout)
//                    convertView.findViewById(R.id.ll_set_classroom_top);
//            viewHolder.tv_edit_classroom = (TextView)
//                    convertView.findViewById(R.id.tv_edit_classroom);
//            viewHolder.tv_check_classroom_relevance = (TextView)
//                    convertView.findViewById(R.id.tv_check_classroom_relevance);
            viewHolder.iv_item_detail_type = (ImageView)
                    convertView.findViewById(R.id.iv_item_detail_type);
            viewHolder.tv_day_price = (TextView)
                    convertView.findViewById(R.id.tv_day_price);
            viewHolder.tv_week_price = (TextView)
                    convertView.findViewById(R.id.tv_week_price);
            viewHolder.tv_sigle_more = (TextView)
                    convertView.findViewById(R.id.tv_sigle_more);
            viewHolder.tv_repeat = (TextView)
                    convertView.findViewById(R.id.tv_repeat);
            viewHolder.tv_add_classroom_info = (TextView)
                    convertView.findViewById(R.id.tv_add_classroom_info);
            viewHolder.tv_cancel_classroom_info = (TextView)
                    convertView.findViewById(R.id.tv_cancel_classroom_info);
            if ("1".equals(o.getSchool_type())) {
                //school_type 1代表自营
                viewHolder.iv_item_detail_type.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_item_detail_type.setVisibility(View.GONE);
            }

            //隐藏或显示 个性化调整区域
            if ((o.getRoom_adjust().getCancel_date() == null || o.getRoom_adjust().getCancel_date().size() <= 0)
                    && (o.getRoom_adjust().getAdd_date() == null || o.getRoom_adjust().getAdd_date().size() <= 0)) {
                o.setIsHave(false);
                viewHolder.ll_all_up_down.setVisibility(View.GONE);
            } else {
                o.setIsHave(true);
                viewHolder.ll_all_up_down.setVisibility(View.VISIBLE);
                List<AdjustmentData> newCancel_date = getNewCancel_date(o.getRoom_adjust().getCancel_date());
                // 取消课室动态布局
//                handleClassRoom(
//                        viewHolder.ll_cancel_classroom,
//                        viewHolder.tv_cancel_classroom,
//                        newCancel_date);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < newCancel_date.size(); i++) {
                    if (i == newCancel_date.size() - 1) {
                        sb.append(newCancel_date.get(i).getDate());
                    } else {
                        sb.append(newCancel_date.get(i).getDate()).append("、");
                    }
                }
                if (newCancel_date.size() > 0) {
                    viewHolder.tv_cancel_classroom.setVisibility(View.VISIBLE);
                    viewHolder.tv_cancel_classroom_info.setVisibility(View.VISIBLE);
                    viewHolder.tv_cancel_classroom_info.setText(sb.toString());
                } else {
                    viewHolder.tv_cancel_classroom.setVisibility(View.GONE);
                    viewHolder.tv_cancel_classroom_info.setVisibility(View.GONE);
                }
                List<AdjustmentData> newAdd_date = getNewCancel_date(o.getRoom_adjust().getAdd_date());
                StringBuilder sb_add = new StringBuilder();
                for (int j = 0; j < newAdd_date.size(); j++) {
                    if (j == newAdd_date.size() - 1) {
                        sb_add.append(newAdd_date.get(j).getDate());
                    } else {
                        sb_add.append(newAdd_date.get(j).getDate()).append("、");
                    }
                }
                if (newAdd_date.size() > 0) {
                    viewHolder.tv_add_classroom.setVisibility(View.VISIBLE);
                    viewHolder.tv_add_classroom_info.setVisibility(View.VISIBLE);
                    viewHolder.tv_add_classroom_info.setText(sb_add.toString());
                } else {
                    viewHolder.tv_add_classroom.setVisibility(View.GONE);
                    viewHolder.tv_add_classroom_info.setVisibility(View.GONE);
                }
            }
            //可选设备
            handleDeviceNoFree(viewHolder.ll_can_select, viewHolder.ll_no_free_device_add, o.getDevice_nofree());
            //课室布置
//            handleDeviceFree(viewHolder.ll_classroom_add, o.getDevice_free());

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //个性化调整区域是否显示
        if (o.isHave()) {
            if (!o.isOpen()) {
                viewHolder.ll_edit_individuation.setVisibility(View.VISIBLE);
                viewHolder.iv_up_down.setImageDrawable(UIUtils.getDrawable(R.drawable.more_btn_up));
            } else {
                viewHolder.ll_edit_individuation.setVisibility(View.GONE);
                viewHolder.iv_up_down.setImageDrawable(UIUtils.getDrawable(R.drawable.more_btn_down));
            }
        }
        String pic_url = o.getPic_url();
        if (StringUtils.isNullString(pic_url)) {
            viewHolder.iv_order_pic.setImageResource(R.drawable.bg_placeholder_4_3);
        } else {
            Glide.with(UIUtils.getContext())
                    .load(pic_url)
                    .asBitmap().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.bg_placeholder_4_3)
                    .error(R.drawable.bg_placeholder_4_3)
                    .into(viewHolder.iv_order_pic);
        }
        viewHolder.tv_item_detail_count.setText(
                String.format(
                        UIUtils.getString(R.string.multiply_jian),
                        o.getRoom_count()
                ));
        viewHolder.tv_item_detail_sname.setText(o.getSname());
//        viewHolder.tv_item_detail_use_desc.setText(o.getType_desc());
        viewHolder.tv_item_detail_room_type.setText(o.getType_desc());
        Set<Date> dates = new HashSet<>();
        for (Order3 order3 : o.getOrder3()) {
            Date date = DateUtil.strToDate(order3.getDate());
            dates.add(date);
        }
        if (dates.size() == 1) {
            viewHolder.tv_sigle_more.setText("订单信息(单天)");
            viewHolder.tv_item_detail_date.setText(
                    String.format(
                            UIUtils.getString(R.string.date_to_symbol_single), o.getStart_date()));
            viewHolder.tv_repeat.setVisibility(View.GONE);
        } else if (dates.size() > 1) {
            viewHolder.tv_sigle_more.setText("订单信息(多天)");
            viewHolder.tv_item_detail_date.setText(
                    String.format(
                            UIUtils.getString(R.string.date_to_symbol),
                            o.getStart_date(),
                            o.getEnd_date()
                    ));
            viewHolder.tv_repeat.setVisibility(View.VISIBLE);
            String repeat = o.getRepeat();
            if (StringUtils.isNotNullString(repeat)) {
                StringBuilder sb = new StringBuilder();
                sb.append("循环方式:");
                String[] split = repeat.split(",");
                for (int i = 0; i < split.length; i++) {
                    Integer week = Integer.valueOf(split[i]);
                    switch (week) {
                        case 1:
                            sb.append("每周一");
                            break;
                        case 2:
                            sb.append("每周二");
                            break;
                        case 3:
                            sb.append("每周三");
                            break;
                        case 4:
                            sb.append("每周四");
                            break;
                        case 5:
                            sb.append("每周五");
                            break;
                        case 6:
                            sb.append("每周六");
                            break;
                        case 7:
                            sb.append("每周日");
                            break;
                    }
                    if (i != split.length - 1) {
                        sb.append("、");
                    }
                }
                viewHolder.tv_repeat.setText(sb.toString());
            } else {
                viewHolder.tv_repeat.setText("循环方式:每天");
            }
        }
        viewHolder.tv_item_detail_max_member.setText(
                String.format(
                        UIUtils.getString(R.string.max_member_text),
                        o.getMax_member()
                ));
        /*viewHolder.tv_item_detail_room_count.setText(
                String.format(
                        UIUtils.getString(R.string.total_hour_short_text),
                        o.getTotal_hour()
                ));*/
        viewHolder.tv_day_price.setText("¥" + o.getAvailable_price().get(0).getPrice_weekday() + "/");
        viewHolder.tv_week_price.setText("¥" + o.getAvailable_price().get(0).getPrice_weekend() + "/");
        if (o.getTime_group() != null && o.getTime_group().size() >= 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < o.getTime_group().size(); i++) {
                if (i == o.getTime_group().size() - 1) {
                    String dateShow = String.format(
                            UIUtils.getString(R.string.to_symbol),
                            StringUtils.changeTime(o.getTime_group().get(i).getStart_time()),
                            StringUtils.changeTime(o.getTime_group().get(i).getEnd_time())
                    );
                    sb.append(dateShow);
                } else {
                    String dateShow = String.format(
                            UIUtils.getString(R.string.to_symbol),
                            StringUtils.changeTime(o.getTime_group().get(i).getStart_time()),
                            StringUtils.changeTime(o.getTime_group().get(i).getEnd_time())
                    );
                    sb.append(dateShow).append("，");
                }
            }
            viewHolder.tv_item_detail_time.setText("使用时段: " + sb.toString());
        }
        int weekEndCount = 0;
        for (Date d : dates) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                    calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                weekEndCount++;
            }
        }
        int weekCount = dates.size() - weekEndCount;
        if (weekEndCount == 0 && weekCount != 0) {
            viewHolder.tv_item_detail_repeat.setText("使用天数: " + dates.size() + "天(平日" + (weekCount) + "天)");
        } else if (weekEndCount != 0 && weekCount == 0) {
            viewHolder.tv_item_detail_repeat.setText("使用天数: " + dates.size() + "天(周末" + weekEndCount + "天)");
        } else {
            viewHolder.tv_item_detail_repeat.setText("使用天数: " + dates.size() + "天(平日" +
                    (weekCount) + "天,周末" + weekEndCount + "天)");
        }

        /*viewHolder.tv_item_detail_fee.setText(
                String.format(
                        UIUtils.getString(R.string.how_much_money),
                        o.getFee()
                ));*/
//        if ("0".equals(o.getCourse_id())) {
//            viewHolder.tv_check_classroom_relevance.setText(
//                    UIUtils.getString(R.string.order_no_relevance));
//        } else {
//            viewHolder.tv_check_classroom_relevance.setText(
//                    UIUtils.getString(R.string.order_relevance));
//        }
//        if (StringUtils.isNotNullString(o.getUse_desc())) {
//            viewHolder.tv_dec.setVisibility(View.VISIBLE);
//            if (StringUtils.isNullString(o.getType_name())) {
//                viewHolder.tv_dec.setText(o.getUse_desc());
//            } else {
//                viewHolder.tv_dec.setText(
//                        String.format(
//                                UIUtils.getString(R.string.tan_hao),
//                                o.getUse_desc(),//课室用途
//                                o.getStudent_desc()//学生类型_年龄段
//                        ));
//            }
//        } else {
//            viewHolder.tv_dec.setVisibility(View.GONE);
//        }
        ///////////判断日期是否是最后一天
//        int date_result = DateUtil.compareDate(current_date, o.getEnd_date());
//        if (date_result >= 0) {//超过最后日期
//            viewHolder.tv_edit_classroom.setTextColor(UIUtils.getColor(R.color.color_gay_99));
//            Drawable nav_up = UIUtils.getDrawable(R.drawable.arrow);
//            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//            viewHolder.tv_edit_classroom.setCompoundDrawables(null, null, nav_up, null);
//            viewHolder.ll_set_classroom_top.setClickable(false);
//        } else {
//            viewHolder.tv_edit_classroom.setTextColor(UIUtils.getColor(R.color.color_black_33));
//            Drawable nav_up = UIUtils.getDrawable(R.drawable.arrow);
//            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//            viewHolder.tv_edit_classroom.setCompoundDrawables(null, null, nav_up, null);
//            viewHolder.ll_set_classroom_top.setClickable(true);
//        }
        ///////////

        //门店名点击跳转到门店详情
        viewHolder.tv_item_detail_sname.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_103");
                Intent intent = new Intent(UIUtils.getContext(), StoreDetailActivity.class);
                intent.putExtra(ExtraControl.EXTRA_STORE_ID, o.getSid());
                UIUtils.startActivity(intent);
            }
        });
        //课室名点击跳转到课室详情
        viewHolder.rl_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_104");
                Intent intent = new Intent(UIUtils.getContext(), IndexDetailActivity.class);
                intent.putExtra("sid", o.getSid());
                intent.putExtra("sname", o.getSname());
                intent.putExtra("type_desc", o.getType_desc());
                intent.putExtra(ExtraControl.EXTRA_TYPE_ID, o.getType_id());
                intent.putExtra("from_order_detail", "1");
                BaseApplication.getmForegroundActivity().startActivity(intent);
            }
        });
        // 个性化调整
        viewHolder.ll_up_down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                o.setIsOpen(!o.isOpen());
                notifyDataSetChanged();
            }
        });
        // 查看课室明细_可退单的页面
        viewHolder.rl_all_course.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_108");
                Intent intent = new Intent(context,
                        ClassroomDetailActivity.class);
                intent.putExtra("order2", o);
                intent.putExtra("coupon_id", coupon_id);
                BaseApplication.getmForegroundActivity().startActivity(intent);
            }
        });
        //课室布置
//        viewHolder.ll_set_classroom_top.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getSystemTimeRequest(o);
//            }
//        });

        //小计 点击进入费用清单h5页面
        /*viewHolder.rl_item_detail_subtotal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        context,
                        Common_Show_WebPage_Activity.class);
                intent.putExtra(
                        UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.SERVER_WEB_LISTFREE + "oid2=" + o.getId());
                intent.putExtra(
                        UIUtils.getString(R.string.get_page_name),
                        WebConstant.Expense_list_Page);
                UIUtils.startActivity(intent);
            }
        });*/
        handleViewStatus(viewHolder);

        return convertView;
    }

    // 处理控件在不同状态下显示与隐藏
    private void handleViewStatus(ViewHolder viewHolder) {
        switch (currentStatus) {
            case 0://待支付
            case 9://待支付
                // 明细 隐藏
                viewHolder.rl_all_course.setClickable(false);
                viewHolder.rl_all_course.setVisibility(View.GONE);
                viewHolder.rl_course_nearly.setVisibility(View.GONE);
                // 课室布置整块隐藏
//                viewHolder.ll_set_classroom.setVisibility(View.GONE);
                // 课室布置不可修改
//                viewHolder.ll_set_classroom_top.setClickable(false);
                break;
            case 1://进行中
                // 明细 显示
                viewHolder.rl_all_course.setClickable(true);
                viewHolder.rl_all_course.setVisibility(View.VISIBLE);
                viewHolder.rl_course_nearly.setVisibility(View.VISIBLE);
                // 课室布置整块显示
//                viewHolder.ll_set_classroom.setVisibility(View.VISIBLE);
                // 课室布置可修改
//                viewHolder.ll_set_classroom_top.setClickable(true);
//                viewHolder.tv_edit_classroom.setVisibility(View.VISIBLE);
                for (Order3 order3 : o.getOrder3()) {
                    String dateStr = order3.getDate();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = null;
                    try {
                        date = format.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (null != date && date.getTime() > System.currentTimeMillis()) {
                        viewHolder.tv_course_nearly.setText(order3.getDate()
                                + " "
                                + String.format(
                                UIUtils.getString(R.string.to_symbol),
                                StringUtils.changeTime(order3.getStart_time()),
                                StringUtils.changeTime(order3.getEnd_time()))
                                + " "
                                + order3.getRoom_no());
                        break;
                    }
                }
                break;
            case 2://已关闭
            case 4:
            case 8:
            case 7://待确认
            case 10:
                // 明细 隐藏
                viewHolder.rl_all_course.setClickable(false);
                viewHolder.rl_all_course.setVisibility(View.GONE);
                viewHolder.rl_course_nearly.setVisibility(View.GONE);
                // 课室布置整块隐藏
//                viewHolder.ll_set_classroom.setVisibility(View.GONE);
                break;
            case 6://订单超时
                viewHolder.rl_all_course.setClickable(false);
                viewHolder.rl_all_course.setVisibility(View.GONE);
                viewHolder.rl_course_nearly.setVisibility(View.GONE);
                // 课室布置整块显示
//                viewHolder.ll_set_classroom.setVisibility(View.VISIBLE);
                // 课室布置可修改
//                viewHolder.ll_set_classroom_top.setClickable(true);
//                viewHolder.tv_edit_classroom.setVisibility(View.VISIBLE);
                break;
            case 101://已完成
                // 明细 显示
                viewHolder.rl_all_course.setClickable(true);
                viewHolder.rl_all_course.setVisibility(View.VISIBLE);
                viewHolder.rl_course_nearly.setVisibility(View.GONE);
                // 课室布置整块显示
//                viewHolder.ll_set_classroom.setVisibility(View.VISIBLE);
                // 课室布置不可修改
//                viewHolder.ll_set_classroom_top.setClickable(false);
//                viewHolder.tv_edit_classroom.setVisibility(View.GONE);

                // 课室布置整块显示
                /*viewHolder.ll_set_classroom.setVisibility(View.VISIBLE);
                // 课室布置可修改
                viewHolder.ll_set_classroom_top.setClickable(true);
                viewHolder.tv_edit_classroom.setVisibility(View.VISIBLE);*/
                break;
            case 100://已取消
            case 11:
            case 12:
                // 明细 显示
                viewHolder.rl_all_course.setClickable(true);
                viewHolder.rl_all_course.setVisibility(View.VISIBLE);
                // 课室布置整块显示
//                viewHolder.ll_set_classroom.setVisibility(View.VISIBLE);
                // 课室布置不可修改
//                viewHolder.ll_set_classroom_top.setClickable(false);
//                viewHolder.tv_edit_classroom.setVisibility(View.GONE);
                break;
        }
    }

    public class ViewHolder {

        private ImageView iv_order_pic;
        private TextView tv_item_detail_sname;
        private TextView tv_item_detail_room_type;
        //        private TextView tv_item_detail_use_desc;
        private RelativeLayout rl_detail;
        private TextView tv_item_detail_max_member;
        //        private TextView tv_item_detail_room_count;
        private TextView tv_item_detail_count;
        private TextView tv_item_detail_date;
        private TextView tv_item_detail_time;
        private TextView tv_item_detail_repeat;
        //        private TextView tv_item_detail_fee;
//        private TextView tv_dec;
        private LinearLayout ll_can_select;
        private LinearLayout ll_no_free_device_add;
        //        private LinearLayout ll_classroom_add;
        //        private RelativeLayout rl_item_detail_subtotal;
        private LinearLayout ll_all_up_down;//个性化调整
        private LinearLayout ll_up_down;//个性化调整头
        private ImageView iv_up_down;
        private LinearLayout ll_edit_individuation;//个性化调整区域块
        private TextView tv_cancel_classroom;//取消课室
        private TextView tv_add_classroom;// 增加课室
        private TextView tv_cancel_classroom_info;// 取消课室日期
        private TextView tv_add_classroom_info;// 增加课室日期
        //        private LinearLayout ll_cancel_classroom;//取消课室日期区域
//        private LinearLayout ll_add_classroom;//增加课室日期区域
        private RelativeLayout rl_all_course;//查看课室明细
        private RelativeLayout rl_course_nearly;//近期课室
        private TextView tv_course_nearly;//近期课室
        //        private TextView tv_check_classroom_detail;
//        private LinearLayout ll_set_classroom;//课室布置区域块
//        private LinearLayout ll_set_classroom_top;//课室布置头
//        private TextView tv_edit_classroom;//课室布置 修改
        //        private TextView tv_check_classroom_relevance;//课程是否关联
        private ImageView iv_item_detail_type;//旗舰店
        private TextView tv_day_price;//平日价格
        private TextView tv_week_price;//平日价格
        private TextView tv_sigle_more;//订单信息(单天)
        private TextView tv_repeat;//循环方式

    }

    private List<AdjustmentData> getNewCancel_date(List<AdjustmentData> cancel_date) {
        /* 此方法去重会影响原有数据顺序
        HashMap<String, AdjustmentData> hashMap = new HashMap<>();
        for (AdjustmentData adjustmentData : cancel_date) {
            String date = adjustmentData.getDate();
            if (!hashMap.containsKey(date)) {
                hashMap.put(date, adjustmentData);
            }
        }
        List<AdjustmentData> newCancel_date = new ArrayList<>();
        for (String dateKey : hashMap.keySet()) {
            newCancel_date.add(hashMap.get(dateKey));
        }*/
        List<AdjustmentData> newCancel_date = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (AdjustmentData adjustmentData : cancel_date) {
            if (adjustmentData == null) {
                continue;
            }
            String date = adjustmentData.getDate();
            if (StringUtils.isNotNullString(date)) {
                if (!set.contains(date)) {
                    set.add(date);
                    newCancel_date.add(adjustmentData);
                }
            }
        }
        set.clear();
        return newCancel_date;
    }

    /**
     * 取消课室
     *
     * @param ll_parent   动态添加布局的父控件
     * @param cancel_date 取消课室的日期数据源
     */
    private void handleClassRoom(LinearLayout ll_parent, TextView textView,
                                 List<AdjustmentData> cancel_date) {
        if (cancel_date != null && cancel_date.size() > 0) {
            textView.setVisibility(View.VISIBLE);
            for (int i = 0; i < cancel_date.size(); i++) {
                LinearLayout cancel_layout = (LinearLayout) LayoutInflater.from(
                        UIUtils.getContext()).inflate(
                        R.layout.item_adjustment_date, null);
                TextView tv_adjustment_date = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_date);
                TextView tv_adjustment_time = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_time);
                TextView tv_adjustment_count = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_count);
                AdjustmentData adjustmentData = cancel_date.get(i);
                tv_adjustment_date.setText(adjustmentData.getDate());
                tv_adjustment_time.setText(
                        String.format(
                                UIUtils.getString(R.string.to_symbol),
                                StringUtils.changeTime(adjustmentData.getStart_time()),
                                StringUtils.changeTime(adjustmentData.getEnd_time())
                        ));
                tv_adjustment_count.setText(
                        String.format(
                                UIUtils.getString(R.string.multiply),
                                adjustmentData.getRoom_count()
                        ));
                ll_parent.addView(cancel_layout, i);
            }
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * 收费设备_可选设备
     *
     * @param ll_parent      动态添加布局的父控件
     * @param device_no_free 收费设备数据源
     */
    private void handleDeviceNoFree(LinearLayout ll_can_select, LinearLayout ll_parent, List<DeviceEntity> device_no_free) {
        if (device_no_free != null && device_no_free.size() > 0) {
            ll_can_select.setVisibility(View.VISIBLE);
            for (int i = 0; i < device_no_free.size(); i++) {
                LinearLayout layout = (LinearLayout) LayoutInflater.from(
                        UIUtils.getContext()).inflate(
                        R.layout.item_device, null);
                TextView tv_name = (TextView) layout.findViewById(R.id.tv_name);
                TextView tv_count = (TextView) layout.findViewById(R.id.tv_count);
                DeviceEntity dev = device_no_free.get(i);
                tv_name.setText(dev.getDevice_name());
                tv_count.setText(
                        String.format(
                                UIUtils.getString(R.string.multiply),
                                dev.getNum()
                        ));
                ll_parent.addView(layout, i);
            }
        } else {
            ll_can_select.setVisibility(View.GONE);
        }
    }
}
